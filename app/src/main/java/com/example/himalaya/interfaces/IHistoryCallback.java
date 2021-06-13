package com.example.himalaya.interfaces;

import com.example.himalaya.json.Tracks;

import java.util.List;

public interface IHistoryCallback {
    /**
     * 历史内容加载结果
     * @param tracks
     */
    void onHistoriesLoaded(List<Tracks> tracks);
}
