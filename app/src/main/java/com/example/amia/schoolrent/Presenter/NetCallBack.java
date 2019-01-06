package com.example.amia.schoolrent.Presenter;

/**
 * 网络请求回调
 */
public interface NetCallBack {
    void finish(String json);
    void error(String... msg);
}
