package com.example.himalaya.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 设置一个抽象类，一个抽象方法，让三个实现类实现它，可以有效减少代码冗余
 * 利用实现类中的onSubViewLoaded方法得到每个界面
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    private View mRootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView.......................................");
        mRootView = onSubViewLoaded(inflater,container);
        return mRootView;
    }

    protected abstract View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container);
}
