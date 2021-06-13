package com.example.himalaya.presenters;

import android.util.Log;

import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.data.HistoryDao;
import com.example.himalaya.interfaces.IHistoryCallback;
import com.example.himalaya.interfaces.IHistoryDao;
import com.example.himalaya.interfaces.IHistoryDaoCallback;
import com.example.himalaya.interfaces.IHistoryPresenter;
import com.example.himalaya.json.Tracks;
import com.example.himalaya.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * 历史数量最多是100条，超过的话，就删除最前面添加的历史
 */
public class HistoryPresenter implements IHistoryPresenter, IHistoryDaoCallback {

    private static final String TAG = "HistoryPresenter";
    private final IHistoryDao mHistoryDao;
    private List<IHistoryCallback> mCallbacks = new ArrayList<>();
    private List<Tracks> mCurrentHistories = null;
    private Tracks mCurrentAddTrack = null;

    private HistoryPresenter(){
        mHistoryDao = new HistoryDao();
        mHistoryDao.setCallback(this);
        listHistories();
    }
    static HistoryPresenter mHistoryPresenter = null;
    public static HistoryPresenter getInstance() {
        if(mHistoryPresenter == null) {
            synchronized (HistoryPresenter.class) {
                mHistoryPresenter = new HistoryPresenter();
            }
        }
        return mHistoryPresenter;
    }
    @Override
    public void listHistories() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //只调用，不处理结果
                if (mHistoryDao != null) {
                    mHistoryDao.listHistory();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }
    private boolean isDoDelete = false;
    @Override
    public void addHistory(final Tracks tracks) {
        //判断有没有超过100条
        if (mCurrentHistories != null && mCurrentHistories.size() >= Constants.MAX_HISTORY_COUNT) {
            isDoDelete = true;
            this.mCurrentAddTrack = tracks;
            delHistory(mCurrentHistories.get(mCurrentHistories.size() - 1));
        } else {
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                    //只调用，不处理结果
                    if (mHistoryDao != null) {
                        mHistoryDao.addHistory(tracks);
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    @Override
    public void delHistory(final Tracks tracks) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //只调用，不处理结果
                if (mHistoryDao != null) {
                    mHistoryDao.deleteHistory(tracks);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void clearHistory() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //只调用，不处理结果
                if (mHistoryDao != null) {
                    mHistoryDao.clearHistory();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void registerViewCallback(IHistoryCallback iHistoryCallback) {
      //UI注册过来的
        if (!mCallbacks.contains(iHistoryCallback)) {
            mCallbacks.add(iHistoryCallback);
        }
    }

    @Override
    public void unregisterViewCallback(IHistoryCallback iHistoryCallback) {
          mCallbacks.remove(iHistoryCallback);
    }

    @Override
    public void onHistoryAdd(boolean isSuccess) {
        listHistories();
    }

    @Override
    public void onHistoryDel(boolean isSuccess) {
        if(isDoDelete && mCurrentAddTrack != null) {
            isDoDelete = false;
            addHistory(mCurrentAddTrack);
        }else {
            listHistories();
        }
    }

    @Override
    public void onHistoriesLoaded(final List<Tracks> tracks) {
        this.mCurrentHistories = tracks;
        Log.d(TAG, "tracks -- > " + tracks.size());;
         //通知UI更新数据
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                    for (IHistoryCallback callback : mCallbacks) {
                    callback.onHistoriesLoaded(tracks);
                }
            }
        });
    }

    @Override
    public void onHistoriesClear(boolean isSuccess) {
        listHistories();
    }

    public boolean isGet(Tracks tracks) {
        if(mCurrentHistories != null) {
            for (Tracks currentHistory : mCurrentHistories) {
                if(currentHistory.getId() == tracks.getId()) return true;
            }
        }
        return false;
    }
}
