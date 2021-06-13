package com.example.himalaya.interfaces;

import com.example.himalaya.json.Tracks;

public interface IHistoryDao {
    /**
     * 设置回调接口
     * @param callback
     */
    void setCallback(IHistoryDaoCallback callback);

    /**
     * 添加历史
     * @param tracks
     */
    void addHistory(Tracks tracks);

    /**
     * 删除历史
     */
    void deleteHistory(Tracks tracks);

    /**
     * 清除历史内容
     */
    void clearHistory();

    /**
     * ；获取历史那日容
     */
    void listHistory();
}
