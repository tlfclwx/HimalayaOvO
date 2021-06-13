package com.example.himalaya.interfaces;

import com.example.himalaya.json.Bean;

import java.util.List;

public interface IRecommendViewCallback {

    /**
     * 获取推荐内容的结果
     * @param result
     */
    void onRecommendListLoaded(List<Bean> result);

    /**
     * 网络错误
     */
    void onNewWordError();

    /**
     * 页面为空
     */
    void onEmpty();

    /**
     * 正在加载
     */
    void onLoading();
}
