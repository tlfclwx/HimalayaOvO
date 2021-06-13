package com.example.himalaya.interfaces;

import com.example.himalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public interface IPlayerPresenter extends IBasePresenter<IPlayerCallback> {
    /**
     * 播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 播放上一首
     */
    void playPre();

    /**
     * 播放下一首
     */
    void playNext();


    /**
     * 获取播放列表
     */
    void getPlayList();

    /**
     * 根据节目的位置进行播放
     */
    void playByIndex(int index);

    /**
     * 判断播放器是否正在播放
     *
     * @return
     */
    int isPlaying();

    /**
     * 播放专辑的第一首节目。
     *
     * @param id
     */
    void playByAlbumId(int id);
    /**
     * 切换播放模式
     */
    void switchPlayMode(int mode);

}
