package com.example.himalaya;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Trace;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.himalaya.json.Tracks;
import com.example.himalaya.adapters.PlayerTrackPagerAdapter;
import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IPlayerCallback;
import com.example.himalaya.presenters.PlayerPresenter;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.SobPopWindow;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends BaseActivity implements IPlayerCallback, ViewPager.OnPageChangeListener {

    private static final String TAG = "PlayerActivity";
    private ImageView mControlBtn;
    private PlayerPresenter mPlayerPresenter;
    private SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourFormat = new SimpleDateFormat("hh:mm:ss");
    private static TextView mTotalDuration;
    private static TextView mCurrentPosition;
    private static SeekBar mDurationBar;
    private int mCurrentProgress = 0;
    private boolean mIsUserTouchProgressBar = false;
    private ImageView mPlayNextBtn;
    private ImageView mPalyPreBtn;
    private TextView mTrackTitle;
    private String mTrackTitleText;
    private ViewPager mTrackPageView;
    private PlayerTrackPagerAdapter mTrackPagerAdapter;
    private boolean mIsUserSlidePager = false;
    private ImageView mPlayModeSwitchBtn;
    private int mCurrentMode = PLAY_MODEL_LIST;
    private static Map<Integer, Integer> sPlayMoleRule = new HashMap<>();
    public final static int PLAY_MODEL_LIST = 0;
    public final static int PLAY_MODEL_LIST_LOOP = 1;
    static {
        sPlayMoleRule.put(PLAY_MODEL_LIST, PLAY_MODEL_LIST_LOOP);
        sPlayMoleRule.put(PLAY_MODEL_LIST_LOOP, PLAY_MODEL_LIST);
    }

    private View mPlayListBtn;
    private SobPopWindow mSobPopWindow;
    private ValueAnimator mEnterBgAnimation;
    private ValueAnimator mOutBgAnimation;
    public final int BG_ANIMATION_DURATION = 500;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_player);
        initView();
        initEvent();
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        //在获取界面之后采取获取数据
        mPlayerPresenter.getPlayList();
        //startPlay();
        initBgAnimation();
    }

    private void initBgAnimation() {
        //点击显现是的效果
        mEnterBgAnimation = ValueAnimator.ofFloat(1.0f,0.7f);
        mEnterBgAnimation.setDuration(BG_ANIMATION_DURATION);
        mEnterBgAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                //处理一下背景，有点透明度
                updateBgAlpha(value);
            }
        });
        //点击其他地方退出时
        mOutBgAnimation = ValueAnimator.ofFloat(0.7f, 1.0f);
        mOutBgAnimation.setDuration(BG_ANIMATION_DURATION);
        mOutBgAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                //处理一下背景，有点透明度
                updateBgAlpha(value);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unregisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.play();
        }
    }
    public static final Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData(); //获取从子线程发送过来的音乐播放进度
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            if(mDurationBar != null) {
                mDurationBar.setMax(duration);
                mDurationBar.setProgress(currentPosition);
                mCurrentPosition.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) currentPosition),
                        TimeUnit.MILLISECONDS.toSeconds((long) currentPosition) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) currentPosition))));
                mTotalDuration.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) duration),
                        TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) duration))));
            }
        }
    };
    /**
     * 给控件设置相关的事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        //暂停和开始按钮
        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果现在的状态时正在播放的，那么就暂停
                if (mPlayerPresenter.isPlaying() == 1) {
                    mPlayerPresenter.pause();
                } else if(mPlayerPresenter.isPlaying() == 0){
                    //如果现在的状态时正暂停的，那么就播放
                    mPlayerPresenter.play();
                } else {
                    mPlayerPresenter.reStart();
                }
            }
        });

        //进度条
        mDurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if (isFromUser) {  //如果用户触摸
                    mPlayerPresenter.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mPlayerPresenter.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayerPresenter.reStart();
            }
        });
        //播放前一个节目
        mPalyPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放前一个节目
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });
        ///播放后一个节目
        mPlayNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放后一个节目
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playNext();
                }
            }
        });
        mTrackPageView.addOnPageChangeListener(this);
        mTrackPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mIsUserSlidePager = true;
                        break;
                }
                return false;
            }
        });
        mPlayListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示播放列表
                mSobPopWindow.showAtLocation(v, Gravity.BOTTOM,0,0);
                //修改背景透明度
                mEnterBgAnimation.start();
            }
        });
        mPlayModeSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPlayMode();
            }
        });
        mSobPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //点击任何其他地方退出时，开始渐变回显
                mOutBgAnimation.start();
            }
        });
        mSobPopWindow.setPlayListItemClickListener(new SobPopWindow.PlayListItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //说明播放列表item‘被点击了
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playByIndex(position);
                }
            }
        });
    }
    public void updateBgAlpha(float alpha) {
         Window window = getWindow();
         WindowManager.LayoutParams attributes = window.getAttributes();
         attributes.alpha = alpha;
         window.setAttributes(attributes);
     }
    private void switchPlayMode() {
        //根据当前的mode获取相对应的mode
        int playMode = sPlayMoleRule.get(mCurrentMode);
        //修改播放模式
        if (mPlayerPresenter != null) {
            mPlayerPresenter.switchPlayMode(playMode);
        }
    }
    /**
     * 找到各个控件
     */
    private void initView() {
        mControlBtn = this.findViewById(R.id.play_or_pause_btn);
        mTotalDuration = this.findViewById(R.id.track_duration);
        mCurrentPosition = this.findViewById(R.id.current_position);
        mDurationBar = this.findViewById(R.id.track_seek_bar);
        mPlayNextBtn = this.findViewById(R.id.play_next);
        mPalyPreBtn = this.findViewById(R.id.play_pre);
        mTrackTitle = this.findViewById(R.id.track_title);
        //此处是为了用来测试mTrackTitleText是否已经有值，如果有的话，设置
        if (!TextUtils.isEmpty(mTrackTitleText)) {
            mTrackTitle.setText(mTrackTitleText);
        }
        mTrackPageView = this.findViewById(R.id.track_pager_view);
        //创建适配器
        mTrackPagerAdapter = new PlayerTrackPagerAdapter();
        //设置适配器
        mTrackPageView.setAdapter(mTrackPagerAdapter);
        //切换播放模式的按钮
        mPlayModeSwitchBtn = this.findViewById(R.id.player_mode_switch_btn);
        mPlayListBtn = this.findViewById(R.id.player_list);
        mSobPopWindow = new SobPopWindow();
    }

    @Override
    public void onPlayStart() {
        //开始播放，修改UI层暂停的按钮
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.drawable.selector_player_play);
        }
    }

    @Override
    public void onPlayPause() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.drawable.selector_player_stop);
        }
    }

    @Override
    public void onPlayStop() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.drawable.selector_player_stop);
        }
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
        //把数据设置到适配器里
        if (mTrackPagerAdapter != null) {
            mTrackPagerAdapter.setData(list);
        }
        //数据回来以后也要给节目列表一份
        if (mSobPopWindow != null) {
            mSobPopWindow.setListData(list);
        }
    }

    @Override
    public void onPlayModeChange(int playMode) {
        //更新播放模式并且修改UI
        mCurrentMode = playMode;
        //连同列表内的UI一同更新
        updatePlayModeBtnImg();
    }

    private void updatePlayModeBtnImg() {
        int resId = R.drawable.selector_player_mode_list_order;
        switch (mCurrentMode) {
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_player_mode_list_order;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_player_mode_list_order_loop;
                break;
        }
        mPlayModeSwitchBtn.setImageResource(resId);
    }
    @Override
    public void onProgressChange(long currentDuration, long total) {
        LogUtil.d(TAG, "onProgressChange --------- > ");
        mDurationBar.setMax((int) total);
        //跟新播放进度，更新进度条
        String totleDuration;
        String currentPosition;
        if (total > 1000 * 60 * 60) {
            totleDuration = mHourFormat.format(total);
            currentPosition = mHourFormat.format(currentDuration);
        } else {
            totleDuration = mMinFormat.format(total);
            currentPosition = mMinFormat.format(currentDuration);
        }
        //更新当前时间
        if (mTotalDuration != null) {
            mTotalDuration.setText(totleDuration);
        }
        if (mCurrentPosition != null) {
            mCurrentPosition.setText(currentPosition);
        }
        //更新进度
        //计算进度
        if (!mIsUserTouchProgressBar) {
            mDurationBar.setProgress((int) currentDuration);
        }
    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void AdFinished() {

    }

    @Override
    public void onTrackUpdate(final Tracks track, final int playIndex) {
        //设置当前节目的标题
        if (track == null) {
            return ;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTrackTitleText = track.getTrack_title();
                if (mTrackTitle != null) {
                    mTrackTitle.setText(mTrackTitleText);
                }
                //当节目改变的时候，我们就获取到当前播放中的播放位置
                //当前节目改变以后，要修改页面的图片.
                if (mTrackPageView != null) {
                    mTrackPageView.setCurrentItem(playIndex, false);
                }
                //修改播放列表里的播放位置
                if (mSobPopWindow != null) {
                    mSobPopWindow.setCurrentPlayPosition(playIndex);
                }
            }
        });

    }

    @Override
    public void updateListOrder(boolean isReverse) {
        mSobPopWindow.updateOrderIcon(!isReverse);
    }

    //===================== 当用手转换节目的时候才会触发下面内容(可以这么理解,因为上面的setCurrentItem的调用会调用到onPageSelected，所以加上一个用手触摸逻辑是没问题的
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //当页面被选中的时候，就去切换播放的内容
        if (mPlayerPresenter != null && mIsUserSlidePager) {
            if(mPlayerPresenter.isPlaying() == 1) mPlayerPresenter.pause();
            mPlayerPresenter.playByIndex(position);
        }
        mIsUserSlidePager = false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //================================================================================================
}
