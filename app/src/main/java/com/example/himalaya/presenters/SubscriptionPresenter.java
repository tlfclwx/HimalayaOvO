package com.example.himalaya.presenters;

import android.util.Log;

import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.data.SubscriptionDao;
import com.example.himalaya.interfaces.ISubDaoCallback;
import com.example.himalaya.interfaces.ISubscriptionPresenter;
import com.example.himalaya.interfaces.ISubscriptionCallback;
import com.example.himalaya.json.Bean;
import com.example.himalaya.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class SubscriptionPresenter implements ISubscriptionPresenter, ISubDaoCallback {
    private static final String TAG = "SubscriptionPresenter";
    private  final SubscriptionDao mSubscriptionDao;
    private Map<Long, Bean> mData = new HashMap<>();
    private List<ISubscriptionCallback> mCallback = new ArrayList<>();
    private SubscriptionPresenter() {
        mSubscriptionDao = SubscriptionDao.getInstance();
        mSubscriptionDao.setCallback(this);
    }

    private void listSubscriptions() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                  //只调用，不处理结果
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.listBeans();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }
    private static SubscriptionPresenter sInstance = null;
    public static SubscriptionPresenter getInstance() {
        if(sInstance == null) {
            synchronized (SubscriptionPresenter.class) {
                sInstance = new SubscriptionPresenter();
            }
        }
        return sInstance;
    }
    @Override
    public void addSubscription(final Bean bean) {
        //判断当前的订阅数量，不能超度100个
        if(mData.size() >= Constants.MAX_SUB_COUNT) {
            for (ISubscriptionCallback iSubscriptionCallback : mCallback) {
                iSubscriptionCallback.onSubFULL();
            }
            return ;
        }
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //只调用，不处理结果
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.addBean(bean);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void deleteSubscription(final Bean bean) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //只调用，不处理结果
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.deleteBean(bean);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void getSubscriptionList() {
        listSubscriptions();
    }


    @Override
    public void registerViewCallback(ISubscriptionCallback iSubsctiptionCallback) {
        if(!mCallback.contains(iSubsctiptionCallback)) {
            mCallback.add(iSubsctiptionCallback);
        }
    }

    @Override
    public void unregisterViewCallback(ISubscriptionCallback iSubsctiptionCallback) {
         mCallback.remove(iSubsctiptionCallback);
    }

    @Override
    public void onAddResult(final boolean isSuccess) {
       //添加结果的回调
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallback) {
                    callback.onAddResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onDeleteResult(final boolean isSuccess) {
       //删除订阅的回调
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallback) {
                    callback.onDeleteResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onSubListLoaded(final List<Bean> result) {
       //加载数据的回调
        mData.clear();
        for (Bean bean : result) {
            mData.put(bean.getId(), bean);
        }
        //通知UI
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallback) {
                    callback.onSubscriptionsLoaded(result);
                }
            }
        });

    }
    @Override
    public boolean isSub(Bean bean) {
        Log.d(TAG, "mData -- > " + mData.size());
        Bean result = null;
        if (mData.size() > 0) {
           result = mData.get(bean.getId());
        }
        //不为空，表示已经订阅
        return result != null;
    }
}
