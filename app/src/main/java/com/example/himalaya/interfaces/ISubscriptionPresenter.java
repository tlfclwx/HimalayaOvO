package com.example.himalaya.interfaces;

import com.example.himalaya.json.Bean;
import com.example.himalaya.base.IBasePresenter;

/**
 * 订阅一般都有上限，这个地方我们设定不能超过100个
 */
public interface ISubscriptionPresenter extends IBasePresenter<ISubscriptionCallback> {

    /**
     * 添加订阅
     */
    void addSubscription(Bean bean);

    /**
     * 删除订阅
     */
    void deleteSubscription(Bean bean);

    /**
     * 获取订阅列表
     */
    void getSubscriptionList();

    /**
     * 判断当前的专辑是否已经收藏
     * @param bean
     */
    boolean isSub(Bean bean);
}
