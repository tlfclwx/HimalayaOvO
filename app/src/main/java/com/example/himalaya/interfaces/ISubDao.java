package com.example.himalaya.interfaces;

import com.example.himalaya.json.Bean;

public interface ISubDao {

    void setCallback(ISubDaoCallback callback);
    /**
     * 添加专辑订阅
     * @param bean
     */
    void addBean(Bean bean);

    /**
     * 删除订阅
     * @param bean
     */
    void deleteBean(Bean bean);

    /**
     * 获取订阅内容
     */
    void listBeans();
}
