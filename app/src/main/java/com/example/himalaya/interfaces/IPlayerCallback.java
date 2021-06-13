package com.example.himalaya.interfaces;

import android.os.Trace;

import com.example.himalaya.json.Tracks;

import java.util.List;

public interface IPlayerCallback {

    /**
     * 开始播放
     */
    void onPlayStart();

    /**
     * 播放暂停
     */
    void onPlayPause();

    /**
     * 播放停止
     */
    void onPlayStop();

    /**
     * 播放错误
     */
    void onPlayError();

    /**
     * 下一首
     */
    void nextPlay(Trace trace);

    /**
     * 上一首
     */
    void onPrePlay(Trace trace);

    /**
     * 播放列表数据加载完成
     *
     * @param list 播放器列表数据
     */
    void onListLoaded(List<Tracks> list);

    /**
     * 播放器模式改变了
     *
     * @param playMode
     */
    void onPlayModeChange(int playMode);

    /**
     * 进度条的改变
     *
     * @param currentProcess
     * @param total
     */
    void onProgressChange(long currentProcess, long total);

    /**
     * 广告正在加载
     */
    void onAdLoading();

    /**
     * 广告结束
     */
    void AdFinished();

    /**
     * 更新当前节目
     * @param track
     */
    void onTrackUpdate(Tracks track, int playIndex);

    /**
     * 通知UI更新播放列表的文字和图标
     * @param isReverse
     */
    void updateListOrder(boolean isReverse);
}
