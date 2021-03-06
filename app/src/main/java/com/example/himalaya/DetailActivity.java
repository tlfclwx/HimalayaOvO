package com.example.himalaya;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.interfaces.ISubscriptionCallback;
import com.example.himalaya.json.Bean;
import com.example.himalaya.json.Tracks;
import com.example.himalaya.adapters.TrackListAdapter;
import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.interfaces.IPlayerCallback;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.presenters.PlayerPresenter;
import com.example.himalaya.presenters.SubscriptionPresenter;
import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.ImageBlur;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.RoundRectImageView;
import com.example.himalaya.views.UILoader;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, UILoader.OnRetryClickListener, TrackListAdapter.ItemClickListener, IPlayerCallback, ISubscriptionCallback {

    private static final String TAG = "DetailActivity";
    private ImageView mLargeCover;
    private RoundRectImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private int mCurrentPage = 1;
    private RecyclerView mDetailList;
    private TrackListAdapter mDetailListAdapter;
    private FrameLayout mDetailListContainer;
    private UILoader mUiLoader;
    private long mCurrentId = -1;
    private ImageView mPlayControlBtn;
    private TextView mPlayControlTips;
    private PlayerPresenter mPlayerPresenter;
    private List<Tracks> mCurrentTracks = null;
    private final static int DEFAULT_PLAY_INDEX = 0;
    private TwinklingRefreshLayout mRefreshLayout;
    private String mTrackTitle;
    private TextView mSubBtn;
    private SubscriptionPresenter mSubscriptionPresenter;
    private Bean mCurrentBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_detail);
        //??????????????????
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        initPresenter();
        updatePlaySate(mPlayerPresenter.isPlaying() == 1);
        initListener();
    }


    private void initPresenter() {
        //????????????????????????Presenter
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);
        //?????????????????????Presenter
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        //???????????????presenter
        mSubscriptionPresenter = SubscriptionPresenter.getInstance();
        mSubscriptionPresenter.getSubscriptionList();
        mSubscriptionPresenter.registerViewCallback(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.unregisterViewCallback(this);
        }
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unregisterViewCallback(this);
        }
        if (mSubscriptionPresenter != null) {
            mSubscriptionPresenter.unregisterViewCallback(this);
        }
    }

    private void initListener() {
        mSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubscriptionPresenter != null) {
                    boolean isSub = mSubscriptionPresenter.isSub(mCurrentBean);
                    //?????????????????????????????????????????????????????????????????????????????????
                    if (isSub) {
                        mSubscriptionPresenter.deleteSubscription(mCurrentBean);
                    } else {
                        mSubscriptionPresenter.addSubscription(mCurrentBean);
                    }
                }
            }
        });
        if (mPlayControlBtn != null) {
            mPlayControlBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlayerPresenter != null) {
                        //????????????????????????????????????
                        if (mPlayerPresenter.isPlaying() == 0) {
                            handleNoPlayList();
                        } else {
                            handlePlayControl();
                        }
                    }
                }
            });
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????
     */
    private void handleNoPlayList() {
        mPlayerPresenter.setPlayList(mCurrentTracks, DEFAULT_PLAY_INDEX);
    }

    private void handlePlayControl() {
        if (mPlayerPresenter.isPlaying() == 1) {
            //????????????????????????
            mPlayerPresenter.pause();
        } else {
            //?????????????????????
            mPlayerPresenter.reStart();
        }
    }

    private void initView() {
        mDetailListContainer = this.findViewById(R.id.detail_list_container);
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(mUiLoader);
            //????????????????????????????????????????????????
            mUiLoader.setOnRetryClickListener(DetailActivity.this);
        }
        mLargeCover = this.findViewById(R.id.iv_large_cover);
        mSmallCover = this.findViewById(R.id.iv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);
        //?????????????????????
        mPlayControlBtn = this.findViewById(R.id.detail_play_control);
        mPlayControlTips = this.findViewById(R.id.play_control_tv);
        mPlayControlTips.setSelected(true);
        //???????????????
        mSubBtn = this.findViewById(R.id.detail_sub_btn);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param playing
     */
    private void updatePlaySate(boolean playing) {
        if (mPlayControlTips != null && mPlayControlBtn != null) {
            mPlayControlBtn.setImageResource(playing ? R.drawable.selector_play_control_pause : R.drawable.selector_play_control_play);
            if (!playing) {
                mPlayControlTips.setText(R.string.click_play_tips_text);
            } else {
                mPlayControlTips.setText(mTrackTitle);
            }
        }
    }

    private boolean mIsRefreshMore = false;

    private View createSuccessView(ViewGroup container) {
        View detailListView = LayoutInflater.from(this).inflate(R.layout.item_detail_list, container, false);
        mDetailList = detailListView.findViewById(R.id.album_detail_list);
        mRefreshLayout = detailListView.findViewById(R.id.refreshLayout);
        //RecyclerView???????????????
        //?????????????????????????????????
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailList.setLayoutManager(layoutManager);
        //???????????????????????????
        mDetailListAdapter = new TrackListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        //??????Item???????????????
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //???????????????????????????5???5????????????px??????????????????dp
                //?????????????????????????????????????????????????????????????????????????????????
                outRect.top = UIUtil.dip2px(view.getContext(), 4);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 4);
                outRect.left = UIUtil.dip2px(view.getContext(), 4);
                outRect.right = UIUtil.dip2px(view.getContext(), 4);
            }
        });
        mDetailListAdapter.setItemClickListener(this);
        //?????????????????????????????????
        BezierLayout layout = new BezierLayout(this);
        mRefreshLayout.setHeaderView(layout);
        mRefreshLayout.setMaxHeadHeight(140);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            //????????????
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                        mRefreshLayout.finishRefreshing();
                    }
                }, 2000);

            }

            //????????????
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                //????????????????????????
                if (mAlbumDetailPresenter != null) {
                    mAlbumDetailPresenter.loadMore();
                    mIsRefreshMore = true;
                }
            }
        });
        return detailListView;
    }

    @Override
    public void onDetailListLoaded(final List<Tracks> tracks) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRefreshLayout != null && mIsRefreshMore) {
                    mRefreshLayout.finishLoadmore();
                    mIsRefreshMore = false;
                }
                mCurrentTracks = tracks;
                //???????????????????????????????????????UI??????
                if (tracks == null || tracks.size() == 0) {
                    if (mUiLoader != null) {
                        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
                    }
                }
                if (mUiLoader != null) {
                    mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
                }
                //??????/??????UI??????
                mDetailListAdapter.setData(tracks);
            }
        });

    }

    @Override
    public void onNetWorkError() {
        //?????????????????????????????????????????????
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
        }
    }

    @Override
    public void onAlbumLoaded(Bean album) {

        this.mCurrentBean = album;
        //???????????????????????????
        long id = album.getId();
        mCurrentId = id;
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int) id, mCurrentPage);
        }
        //??????????????????Loading??????
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        }
        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbum_title());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNameValuePairs().getNickname());
        }

        //??????????????????
        if (mLargeCover != null) {
            Picasso.with(this).load(album.getCover_url_large()).into(mLargeCover, new Callback() {
                @Override
                public void onSuccess() {
                    Drawable drawable = mLargeCover.getDrawable();
                    if (drawable != null) {
                        //?????????????????????????????????
                        ImageBlur.makeBlur(mLargeCover, DetailActivity.this);
                    }
                }
                @Override
                public void onError() {
                    LogUtil.d(TAG, "onError");
                }
            });
        }
        if (mSmallCover != null) {
            Picasso.with(this).load(album.getCover_url_large()).into(mSmallCover);
        }
    }

    @Override
    public void onLoaderMoreFinished(int size) {
        if (size > 0) {
            Toast.makeText(this, "????????????" + size + "?????????", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefreshFinished(int size) {

    }

    @Override
    public void onRetryClick() {
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int) mCurrentId, mCurrentPage);
        }
    }

    @Override
    public void onItemClick(List<Tracks> detailData, int position) {
        //????????????????????????
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        if (playerPresenter.getCurrentTrack() != detailData.get(position).getId())
            playerPresenter.setPlayList(detailData, position);
        //????????????????????????
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPlayStart() {
        //??????????????????????????????????????????????????????
        updatePlaySate(true);
    }

    @Override
    public void onPlayPause() {
        //????????????????????????????????????????????????yi??????
        updatePlaySate(false);
    }

    @Override
    public void onPlayStop() {
        //??????????????????????????????????????????yi??????
        updatePlaySate(false);
    }


    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Trace trace) {

    }

    @Override
    public void onPrePlay(Trace trace) {

    }

    @Override
    public void onListLoaded(List<Tracks> list) {

    }

    @Override
    public void onPlayModeChange(int playMode) {

    }


    @Override
    public void onProgressChange(long currentProcess, long total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void AdFinished() {

    }

    @Override
    public void onTrackUpdate(final Tracks track, int playIndex) {
        if (track != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTrackTitle = track.getTrack_title();
                    if (!TextUtils.isEmpty(mTrackTitle) && mPlayControlTips != null) {
                        mPlayControlTips.setText(mTrackTitle);
                    }
                }
            });

        }
    }

    @Override
    public void updateListOrder(boolean isReverse) {

    }

    @Override
    public void onAddResult(boolean isSuccess) {
        if (isSuccess) {
            mSubBtn.setText(R.string.cancel_sub_tips_text);
        }
        String tipsText = isSuccess ? "????????????" : "????????????";
        Toast.makeText(this, tipsText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteResult(boolean isSuccess) {
        if (isSuccess) {
            mSubBtn.setText(R.string.sub_tips_text);
        }
        String tipsText = isSuccess ? "????????????" : "????????????";
        Toast.makeText(this, tipsText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubscriptionsLoaded(List<Bean> beans) {
         //?????????????????????????????????
        for (Bean bean : beans) {
            Log.d(TAG, "bean -- > " + bean.getAlbum_title());
        }
        if (mSubscriptionPresenter != null) {
            boolean isSub = mSubscriptionPresenter.isSub(mCurrentBean);
            mSubBtn.setText(isSub ? R.string.cancel_sub_tips_text : R.string.sub_tips_text);
        }
    }

    @Override
    public void onSubFULL() {
         Toast.makeText(this, "????????????????????????" + Constants.MAX_SUB_COUNT, Toast.LENGTH_SHORT).show();
    }
}
