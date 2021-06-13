package com.example.himalaya;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.himalaya.json.Bean;
import com.example.himalaya.json.Tracks;
import com.example.himalaya.adapters.IndicatorAdapter;
import com.example.himalaya.adapters.MainContentAdapter;
import com.example.himalaya.interfaces.IPlayerCallback;
import com.example.himalaya.presenters.PlayerPresenter;
import com.example.himalaya.presenters.RecommendPresenter;
import com.example.himalaya.views.RoundRectImageView;
import com.squareup.picasso.Picasso;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.List;

/**
 * MagicIndicator使用步骤
 * 1. 找到MagicIndicator的控件
 * 2. 创建适配器继承自CommonNavigatorAdapter
 * 3. 创建指示器CommonNavigator，方法commonNavigator.setAdjustMode(true)(自我调节，平分他们的位置)
 * 4. 把适配器放进指示器
 * 5. 把指示器放进MagicIndicator控件
 * <p>
 * ViewPager使用步骤
 * 1. 找到ViewPager的控件
 * 2. 先使用方法getSupportFragmentManager();得到布局管理器
 * 3. 创建适配器继承自FragmentPagerAdapter
 * 4. 创建一个抽象类，使用三个界面继承他，然后根据传进去的位置调用getItem(int positioon)方法，得到界面
 * 5. 把适配器放进ViewPager控件
 * <p>
 * 最后，利用ViewPagerHelper.bind方法将两个控件绑定在一起
 */
public class MainActivity extends FragmentActivity implements IPlayerCallback {

    private static final String TAG = "MainActivity";
    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;
    private IndicatorAdapter mIndicatirAdapter;
    private RoundRectImageView mRoundRectImageView;
    private TextView mHeaderTitle;
    private TextView mSubTitle;
    private ImageView mPlayControl;
    private PlayerPresenter mPlayerPresenter;
    private View mPlayControlItem;
    private View mSearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initPresenter();
    }

    /**
     * 和音频播放绑定
     */
    private void initPresenter() {
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
    }


    private void initEvent() {
        /**
         * 对于MagicIndicator中的点击事件作出回应，更新相应的ViewPager界面
         */
        mIndicatirAdapter.setOnIndicatorTapClickListener(new IndicatorAdapter.OnIndicatorTapClickListener() {
            @Override
            public void onTabClick(int index) {
                if (mContentPager != null) {
                    //切换界面
                    mContentPager.setCurrentItem(index, false);
                }
            }
        });
        /**
         * 按钮点击之后节目播放或暂停，若第一次播放的话同时更新图标
         */
        mPlayControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    if (mPlayerPresenter.isPlaying() == 0) {
                        //没有设置过播放，那我们就设置默认的第一个默认的推荐专辑
                        //第一个推荐专辑，每天都会变得
                        playFirstRecommend();
                    } else {
                        if (mPlayerPresenter.isPlaying() == 1) {
                            mPlayerPresenter.pause();
                        } else {
                            mPlayerPresenter.reStart();
                        }
                    }

                }
            }
        });
        /**
         * 如果点击整个部分进入当前的音频播放界面
         */
        mPlayControlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter.isPlaying() == 0) {
                    playFirstRecommend();
                }
                //跳转到播放器界面
                startActivity(new Intent(MainActivity.this, PlayerActivity.class));
            }
        });
        /**
         * 点击搜索，跳转到搜索界面
         */
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }

    /**
     * 播放第一个默认推荐专辑
     */
    private void playFirstRecommend() {
        RecommendPresenter instance = RecommendPresenter.getInstance();
        List<Bean> currentRecommend = instance.getCurrentRecommend();
        if (currentRecommend != null && currentRecommend.size() > 0) {
            Bean album = currentRecommend.get(0);
            int id = (int) album.getId();
            mPlayerPresenter.playByAlbumId(id);
        }
    }

    private void initView() {
        mMagicIndicator = this.findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));
        //创建indicator的适配器，此处可在构造函数中得到标题数据
        mIndicatirAdapter = new IndicatorAdapter(this);
        //指示器
        CommonNavigator commonNavigator = new CommonNavigator(this);
        //自我调节，平分他们的位置
        commonNavigator.setAdjustMode(true);
        //此处只调用了getCount方法
        commonNavigator.setAdapter(mIndicatirAdapter);
        mMagicIndicator.setNavigator(commonNavigator);
        //ViewPager
        mContentPager = this.findViewById(R.id.content_pager);

        //创建内容适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);

        mContentPager.setAdapter(mainContentAdapter);

        //把ViewPager和indicator绑定到一起,此处调用getTitleView和getIndicator

        ViewPagerHelper.bind(mMagicIndicator, mContentPager);

        //播放控件相关的
        mRoundRectImageView = this.findViewById(R.id.main_track_cover);
        mHeaderTitle = this.findViewById(R.id.main_head_title);
        mHeaderTitle.setSelected(true);
        mSubTitle = this.findViewById(R.id.main_sub_title);
        mPlayControl = this.findViewById(R.id.main_play_control);
        mPlayControlItem = this.findViewById(R.id.main_play_control_item);
        //搜索
        mSearchBtn = this.findViewById(R.id.search_btn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onPlayStart() {
        if (mPlayControl != null) {
            updatePlayControl(true);
        }
    }

    private void updatePlayControl(boolean isPlaying) {
        if (mPlayControl != null) {
             mPlayControl.setImageResource(isPlaying? R.drawable.selector_player_play : R.drawable.selector_player_stop);
        }
    }

    @Override
    public void onPlayPause() {
        updatePlayControl(false);
    }

    @Override
    public void onPlayStop() {
        updatePlayControl(false);
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
                    String trackTitle = track.getTrack_title();
                    String nickname = track.getAnnouncer().getNickname();
                    String coverUrlMiddle = track.getCover_url_middle();
                    if (mHeaderTitle != null) {
                        mHeaderTitle.setText(trackTitle);
                    }
                    if (mSubTitle != null) {
                        mSubTitle.setText(nickname);
                    }
                    Picasso.with(MainActivity.this).load(coverUrlMiddle).into(mRoundRectImageView);
                }
            });

        }
    }

    @Override
    public void updateListOrder(boolean isReverse) {

    }
}