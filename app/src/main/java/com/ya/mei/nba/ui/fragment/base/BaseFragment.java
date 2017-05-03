package com.ya.mei.nba.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ya.mei.nba.app.AppService;

import butterknife.ButterKnife;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public abstract class BaseFragment extends Fragment {

    private int taskId;
    protected View rootView;

    protected abstract void initView();
    protected abstract int getContentViewId();

    protected int getTaskId(){
        return taskId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskId = getActivity().getTaskId();
        AppService.getInstance().getBus().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentViewId(), container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppService.getInstance().getBus().unregister(this);
    }
}
