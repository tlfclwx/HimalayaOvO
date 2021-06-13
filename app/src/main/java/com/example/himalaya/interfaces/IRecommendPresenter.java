package com.example.himalaya.interfaces;

import com.example.himalaya.base.IBasePresenter;

/**
 * 逻辑层
 */
public interface IRecommendPresenter extends IBasePresenter<IRecommendViewCallback> {

    /**
     * 获取到推荐内容
     */
    void getRecommendList();

    /**
     * 下拉刷新更多内容
     */
    void pull2RefresMore();

    /**
     * 上拉下载更多
     */
    void loadMore();
}
