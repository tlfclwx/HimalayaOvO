package com.example.himalaya.interfaces;

import com.example.himalaya.json.Bean;

import java.util.List;

public interface ISubscriptionCallback {
    /**
     * 添加结果，通知UI
     * @param isSuccess
     */
    void onAddResult(boolean isSuccess);

    /**
     * 删除结果，通知UI
     * @param isSuccess
     */
    void onDeleteResult(boolean isSuccess);

    /**
     *  订阅专辑加载的结果的回调方法
     * @param beans
     */
    void onSubscriptionsLoaded(List<Bean> beans);

    /**
     * 订阅数量已满
     */
    void onSubFULL();
}
