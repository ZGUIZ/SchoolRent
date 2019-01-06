package com.example.amia.schoolrent.Presenter;

import android.content.Context;

public interface BaseView<T> {
    void setPresenter(T presenter);
    Context getContext();
}
