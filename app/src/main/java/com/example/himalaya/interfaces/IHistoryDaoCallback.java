package com.example.himalaya.interfaces;

import com.example.himalaya.json.Tracks;

import java.util.List;

public interface IHistoryDaoCallback {
    /**
     * 添加历史的结果
     * @param isSuccess
     */
    void onHistoryAdd(boolean  isSuccess);

    /**
     * 删除历史的结果
     * @param isSuccess
     */

    void onHistoryDel(boolean isSuccess);

    /**
     * 历史数据加载结果
     * @param tracks
     */
    void onHistoriesLoaded(List<Tracks> tracks);

    /**
     * 清除历史的结果
     */
    void onHistoriesClear(boolean isSuccess);
}
