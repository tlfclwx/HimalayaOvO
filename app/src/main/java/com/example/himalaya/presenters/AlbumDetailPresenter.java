package com.example.himalaya.presenters;

import android.util.Log;

import com.example.himalaya.json.Bean;
import com.example.himalaya.json.Tracks;
import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.Urls;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {
    private static final String TAG = "AlbumDetailPresenter";
    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();
    private List<Tracks> mTracks = new ArrayList<>();

    private Bean mTargetAlbum = null;
    //当前的专辑Id
    private int mCurrentAlbumId = -1;
    //当前页
    private int mCurrentPageIndex = 0;

    private AlbumDetailPresenter() {
    }

    private static AlbumDetailPresenter sInstance = null;

    public static AlbumDetailPresenter getInstance() {
        if (sInstance == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (sInstance == null) {
                    sInstance = new AlbumDetailPresenter();
                }
            }
        }
        return sInstance;
    }


    @Override
    public void loadMore() {
        //去加载更多的内容
        mCurrentPageIndex++;
        //传入true，表示结果会追加到结果的后方
        doLoaded(true);
    }

    private void doLoaded(final boolean isLoaderMore) {
        //1,创建OKHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Urls.getUrlLoaded(mCurrentAlbumId, mCurrentPageIndex);
        //2,创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //3,创建一个call对象
        Call call = okHttpClient.newCall(request);
        //4,将请求添加到调度中
        call.enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (isLoaderMore) {
                    mCurrentPageIndex--;
                }
                handlerError();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String data = response.body().string();
                List<Tracks> listTrack = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray tracks = jsonObject.getJSONArray("tracks");

                    for(int i = 0; i < tracks.length(); i++) {
                        JSONObject jsonObject1 = tracks.getJSONObject(i);
                        Gson gson = new Gson();
                        Tracks track = gson.fromJson(jsonObject1.toString(), Tracks.class);
                        listTrack.add(track);
                    }
                    if (isLoaderMore) {
                        //这个是上拉加载，结果放到后面
                        mTracks.addAll(listTrack);  //mTracks.addAll(Tracks.size() - 1, tracks);也可以，两者相等
                    } else {
                        //这个是下拉加载，结果放到前面
                        mTracks.addAll(0, listTrack);
                    }
                    handleAlbumDetailResult(mTracks);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }});
    }


    @Override
    public void getAlbumDetail(int albumId, int page) {
        mTracks.clear();
        this.mCurrentAlbumId = albumId;
        this.mCurrentPageIndex = page;
        //去根据页码和专辑Id
        doLoaded(false);
    }

    /**
     * 如果发生错误，就通知UI
     *
     * @param
     * @param
     */
    private void handlerError() {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onNetWorkError();
        }
    }

    private void handleAlbumDetailResult(List<Tracks> tracks) {
        for (IAlbumDetailViewCallback mCallback : mCallbacks) {
            mCallback.onDetailListLoaded(tracks);
        }
    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
            if (mTargetAlbum != null) {
                callback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unregisterViewCallback(IAlbumDetailViewCallback callback) {
        mCallbacks.remove(callback);
    }

    public void setTargetAlbum(Bean targetAlbum) {
        this.mTargetAlbum = targetAlbum;
    }
}
