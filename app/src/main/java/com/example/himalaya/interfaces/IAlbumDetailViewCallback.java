package com.example.himalaya.interfaces;

import com.example.himalaya.json.Bean;
import com.example.himalaya.json.Tracks;

import java.util.List;

public interface IAlbumDetailViewCallback {

    /**
     * 专辑内容加载出来了
     * @param tracks
     */
    void onDetailListLoaded(List<Tracks> tracks);

    /**
     * 网络错误
     */
     void onNetWorkError();

    /**
     * 把album传给UI使用
     * @param album
     */
    void onAlbumLoaded(Bean album);

    /**
     * 上拉加载更多
     * @param size > 0表示加载成功，否则加载失败
     */
    void onLoaderMoreFinished(int size);

    /**
     * 下拉加载更多
     * @param size > 0表示加载成功，否则加载失败
     */
    void onRefreshFinished(int size);
}
