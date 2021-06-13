package com.example.himalaya.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.PlayerActivity;
import com.example.himalaya.R;
import com.example.himalaya.adapters.TrackListAdapter;
import com.example.himalaya.adapters.TrackListHistoryAdapter;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.interfaces.IHistoryCallback;
import com.example.himalaya.json.Tracks;
import com.example.himalaya.presenters.HistoryPresenter;
import com.example.himalaya.presenters.PlayerPresenter;
import com.example.himalaya.views.ConfirmCheckBoxDialog;
import com.example.himalaya.views.UILoader;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class HistoryFragment extends BaseFragment implements IHistoryCallback, TrackListHistoryAdapter.ItemClickListener, TrackListHistoryAdapter.ItemLongClickListener, ConfirmCheckBoxDialog.onDialogActionClickListener {

    private static final String TAG = "HistoryFragment";
    private UILoader mUiLoader = null;
    private TrackListHistoryAdapter mTrackListHistoryAdapter;
    private HistoryPresenter mHistoryPresenter;
    private Tracks mCurrentClickHistrory = null;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        FrameLayout rootView = (FrameLayout) (FrameLayout) layoutInflater.inflate(R.layout.fragment_history, container, false);
        if (mUiLoader == null) {
            mUiLoader = new UILoader(getActivity()) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }

                @Override
                protected View getEmptyView() {
                    View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view, this, false);
                    TextView tips = emptyView.findViewById(R.id.empty_view_tips_tv);
                    tips.setText("没有历史记录");
                    return emptyView;
                }
            };
        } else {
            if (mUiLoader.getParent() instanceof ViewGroup) {
                ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
            }
        }
        mHistoryPresenter = HistoryPresenter.getInstance();
        mHistoryPresenter.registerViewCallback(this);
        mHistoryPresenter.listHistories();
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        rootView.addView(mUiLoader);
        return rootView;
    }

    private View createSuccessView(ViewGroup container) {
        View successView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_history, container, false);
        TwinklingRefreshLayout twinklingRefreshLayout = successView.findViewById(R.id.over_scroll_view);
        twinklingRefreshLayout.setEnableRefresh(false);
        twinklingRefreshLayout.setEnableLoadmore(false);
        RecyclerView historyList = successView.findViewById(R.id.history_list);
        historyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTrackListHistoryAdapter = new TrackListHistoryAdapter();
        historyList.setAdapter(mTrackListHistoryAdapter);
        mTrackListHistoryAdapter.setItemClickListener(this);
        mTrackListHistoryAdapter.setItemLongClickListener(this);
        historyList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //这个地方不能直接传5，5的单为是px，需要转换为dp
                //这里用到了一个他人写的工具类，自己如果使用的话可以利用
                outRect.top = UIUtil.dip2px(view.getContext(), 4);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 4);
                outRect.left = UIUtil.dip2px(view.getContext(), 4);
                outRect.right = UIUtil.dip2px(view.getContext(), 4);
            }
        });
        return successView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHistoryPresenter != null) {
            mHistoryPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onHistoriesLoaded(List<Tracks> tracks) {

        if (tracks == null || tracks.size() == 0) {
            mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
        } else {
            //更新数据
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
            mTrackListHistoryAdapter.setData(tracks);
        }
    }

    @Override
    public void onItemClick(List<Tracks> detailData, int position) {
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        //Log.d(TAG, "onItemClick  === " + position);

        if (playerPresenter.getCurrentTrack() != detailData.get(position).getId()) {
            //Log.d(TAG, "88888888888888888");
            playerPresenter.setPlayList(detailData, position);
        }
        //跳转到播放器界面
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Tracks tracks) {
        this.mCurrentClickHistrory = tracks;
        ConfirmCheckBoxDialog confirmChecxBoxDialog = new ConfirmCheckBoxDialog(getActivity());
        confirmChecxBoxDialog.setonDialogActionClickListener(this);
        confirmChecxBoxDialog.show();
    }

    @Override
    public void onCancelClick() {
        //甚麽都不用做
    }

    @Override
    public void onConfirmClick(boolean isCheck) {
        //去删除历史
        if (mHistoryPresenter != null && mCurrentClickHistrory != null) {
            if (isCheck) {
                mHistoryPresenter.clearHistory();
            } else {
                mHistoryPresenter.delHistory(mCurrentClickHistrory);
            }
        }
    }
}
