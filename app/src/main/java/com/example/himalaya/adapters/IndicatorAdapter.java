package com.example.himalaya.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.example.himalaya.MainActivity;
import com.example.himalaya.R;
import com.example.himalaya.utils.LogUtil;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * 创建的关于MagicIndicator的适配器
 * 其中在getTitleView方法中设置了监听，在点击的时候可以根据点击的位置而调换下方一起绑定的页面的变化
 */
public class IndicatorAdapter extends CommonNavigatorAdapter {
    private final static String TAG = "IndicatorAdapter";
    private final String[] mTitles;
    private OnIndicatorTapClickListener mOnTabClickListener;

    public  IndicatorAdapter(Context context) {
        Log.d(TAG, "IndicatorAdapter: ");
        mTitles = context.getResources().getStringArray(R.array.indicator_title);
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: ");
        if (mTitles != null) {
            return mTitles.length;
        }
        return 0;
    }

    /**
     * 要获取的UI界面
     * @param context
     * @param index
     * @return
     */
    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        LogUtil.d(TAG, "getTitleView.....");
        ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
        clipPagerTitleView.setText(mTitles[index]);
        clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
        clipPagerTitleView.setClipColor(Color.WHITE);
        clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mViewPager.setCurrentItem(index);
                //TODO:
                if (mOnTabClickListener != null) {
                    mOnTabClickListener.onTabClick(index);
                }
            }
        });
        return clipPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LogUtil.d(TAG, "getIndicator..........");
        return null;
    }
    public void setOnIndicatorTapClickListener(OnIndicatorTapClickListener listener) {
        this.mOnTabClickListener = listener;
    }
    public interface OnIndicatorTapClickListener{
        void onTabClick(int index);
    }
}
