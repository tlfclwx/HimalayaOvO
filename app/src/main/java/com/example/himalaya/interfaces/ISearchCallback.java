package com.example.himalaya.interfaces;

import com.example.himalaya.json.Bean;
import com.example.himalaya.json.Hotworks;
import com.example.himalaya.json.QueryResults;

import java.util.List;

public interface ISearchCallback {


    /**
     * 搜索结果的回调方法
     *
     * @param result
     */
    void onSearchResultLoaded(List<Bean> result);


    /**
     * 获取推荐热词的结果回调方法
     *
     * @param hotWordList
     */
    void onHotWordLoaded(List<Hotworks> hotWordList);

    /**
     * 加载更多的结果返回
     *
     * @param result 结果
     * @param isOkay true表示加载更多成功，false表示没有更多
     */
    void onLoadMoreResult(List<Bean> result, boolean isOkay);

    /**
     * 联想关键字的结果回调方法
     *
     * @param keyWordList
     */
    void onRecommendWordLoaded(List<QueryResults> keyWordList);


    /**
     * 错误通知回调
     *
     */
    void onError();
}