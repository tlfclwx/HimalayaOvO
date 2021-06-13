package com.example.himalaya.interfaces;

import com.example.himalaya.base.IBasePresenter;
import com.example.himalaya.json.Tracks;
import com.ximalaya.ting.android.opensdk.model.track.Track;

public interface IHistoryPresenter extends IBasePresenter<IHistoryCallback> {
    /**
     * 获取历史内容
     */
   void listHistories();

    /**
     * 添加历史
     * @param tracks
     */
   void addHistory(Tracks tracks);

    /**
     * 删除历史
     * @param tracks
     */

   void delHistory(Tracks tracks);

    /**
     * 清除历史
     */
   void clearHistory();
}
