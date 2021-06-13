package com.example.himalaya.interfaces;

import com.example.himalaya.json.Bean;

import java.util.List;

public interface ISubDaoCallback  {
    /**
     * 添加的结果的回调方法
     * @param isSuccess
     */
    void onAddResult(boolean isSuccess);

    /**
     * 删除的结果的回调方法
     * @param isSuccess
     */
    void onDeleteResult(boolean isSuccess);

    /**
     * 加载的结果
     * @param result
     */
    void onSubListLoaded(List<Bean> result);
}
