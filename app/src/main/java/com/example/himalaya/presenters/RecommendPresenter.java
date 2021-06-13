package com.example.himalaya.presenters;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.himalaya.json.Bean;
import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.interfaces.IRecommendPresenter;
import com.example.himalaya.interfaces.IRecommendViewCallback;
import com.example.himalaya.utils.Urls;
import com.google.gson.Gson;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.AccessTokenManager;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.httputil.XimalayaException;
import com.ximalaya.ting.android.opensdk.httputil.util.SignatureUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 此处是逻辑层，用来获取界面的信息，根据情况返回给RecommendFragment对应的函数
 */
public class RecommendPresenter implements IRecommendPresenter {
    private static final String TAG = "RecommendPresenter";

    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();
    private List<Bean> mCurrentRecommend = null;
    public LinkedHashMap<String, String> mHashMaps;
    List<Bean> listBean = new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mCurrentRecommend = (List<Bean>) msg.obj;
                    handlerRecommendResult((List<Bean>) msg.obj);
            }
        }
    };

    private RecommendPresenter() {

    }

    private static RecommendPresenter sInstance = null;

    /**
     * 获取单例对象
     *
     * @return
     */
    public static RecommendPresenter getInstance() {

        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取当前的专辑列表
     *
     * @return 推荐专辑列表，使用之前要判空
     */
    public List<Bean> getCurrentRecommend() {
        return mCurrentRecommend;
    }

    /**
     * 获取推荐内容，其实就是猜你喜欢
     * 这个接口：3.10.6 获取猜你喜欢专辑
     */

    @Override
    public void getRecommendList() {
        //出现加载界面
        updateLoading();
        //1. 创建OKHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Urls.getUrlRecommend();

        //2. 创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //3. 创建一个call对象
        Call call = okHttpClient.newCall(request);
        //4. 将请求添加到调度中
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String data = response.body().string();
                new Thread() {
                    @Override
                    public void run() {
                        mHashMaps = stringToJsonObject(data);
                        for (Map.Entry<String, String> entry : mHashMaps.entrySet()) {
                            Object value = entry.getValue();
                            String ss = value.toString();
                            JSONObject jsonobj = null;
                            try {
                                if (ss != null && ss.startsWith("\ufeff")) {
                                    ss = ss.substring(1);
                                }
                                jsonobj = new JSONObject(ss);
                                JSONObject string1 = jsonobj.getJSONObject("nameValuePairs");
                                Gson gson = new Gson();
                                Bean bean = gson.fromJson(string1.toString(), Bean.class);
                                Log.d(TAG, bean.getAlbum_title());
                                listBean.add(bean);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = listBean;
                        mHandler.sendMessage(msg);
                    }
                }.start();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleError();
            }
        });
    }

    private LinkedHashMap<String, String> stringToJsonObject(String response) {
        try {
            Object json = new JSONTokener(response).nextValue();
            if (json instanceof JSONObject) {
                JSONObject jso = new JSONObject(response);
                return JsonObjectToHashMap(jso);
            } else {
                JSONArray jsa = new JSONArray(response);
                return JsonArrayToHashMap(jsa);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private LinkedHashMap<String, String> JsonObjectToHashMap(JSONObject jso) {
        LinkedHashMap<String, String> hashmap = new LinkedHashMap<>();
        try {
            for (Iterator<String> keyStr = jso.keys(); keyStr.hasNext(); ) {
                String key1 = keyStr.next().trim();
                if (jso.get(key1) instanceof JSONObject) {
                    Gson gson = new Gson();
                    JSONObject NextJSONObject = new JSONObject(jso.get(key1).toString());
                    hashmap.put(key1, gson.toJson(NextJSONObject));
                } else if (jso.get(key1) instanceof JSONArray) {
                    Gson gson = new Gson();
                    JSONArray NextJSONArray = new JSONArray(jso.get(key1).toString());
                    hashmap.put(key1, gson.toJson(NextJSONArray));
                } else {
                    hashmap.put(key1, jso.get(key1).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashmap;
    }

    private LinkedHashMap<String, String> JsonArrayToHashMap(JSONArray jsa) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();

        try {
            for (int i = 0; i < jsa.length(); i++) {
                if (jsa.get(i) instanceof JSONArray) {
                    Gson gson = new Gson();
                    JSONArray NextJSONArray = new JSONArray(jsa.get(i).toString());
                    hashMap.put(String.valueOf(i), gson.toJson(NextJSONArray));
                } else if (jsa.get(i) instanceof JSONObject) {
                    Gson gson = new Gson();
                    JSONObject NextJSONObject = new JSONObject(jsa.get(i).toString());
                    hashMap.put(String.valueOf(i), gson.toJson(NextJSONObject));
                }
            }
        } catch (JSONException e) {
            e.getStackTrace();
        }
        return hashMap;
    }

    private void handleError() {
        if (mCallbacks != null) {
            for (IRecommendViewCallback callback : mCallbacks) {
                callback.onNewWordError();
            }
        }
    }


    private void handlerRecommendResult(List<Bean> albumList) {
        //通知UI更新
        if (albumList != null) {
            //测试，清空一下让界面显示空
            //albumList.clear();
            if (albumList.size() == 0) { //数据为空
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onEmpty();
                }
            } else { //通知更新
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onRecommendListLoaded(albumList);
                }
                this.mCurrentRecommend = albumList;
            }
        }
    }

    private void updateLoading() {
        for (IRecommendViewCallback callback : mCallbacks) {
            callback.onLoading();
        }
    }

    @Override
    public void pull2RefresMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }
}
