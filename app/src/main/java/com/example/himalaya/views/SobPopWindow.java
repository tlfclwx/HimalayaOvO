package com.example.himalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.json.Tracks;
import com.example.himalaya.R;
import com.example.himalaya.adapters.PlayListAdapter;
import com.example.himalaya.base.BaseApplication;

import java.util.List;

public class SobPopWindow extends PopupWindow {

    private final View mPopView;
    private TextView mCloseBtn;
    private RecyclerView mTracksList;
    private PlayListAdapter mPlayListAdapter;
    private TextView mPlayModeTv;
    private ImageView mPlayModeIv;
    private View mPlayModeContainer;
    private View mOrderBtnContainer;
    private ImageView mOrderIcon;
    private TextView mOrderText;

    public SobPopWindow() {
         //设置他的宽高
         super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
         //这里要注意，设置setOutsideTouchable前要先设置setBackgroundDrawable
         //否则无法点击外部关闭pop(但是我不用设置就关闭了)
         setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //此处为设置透明
         setOutsideTouchable(true);
         //载进来VIew
         mPopView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null);
         //设置内容
         setContentView(mPopView);
         //设置窗口进入和退出的动画
         setAnimationStyle(R.style.pop_animation);
         initView();
         initEvent();
     }
    private void initView() {
        mCloseBtn = mPopView.findViewById(R.id.play_list_close_btn);
        //先找到控件
        mTracksList = mPopView.findViewById(R.id.play_list_rv);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(BaseApplication.getAppContext());
        mTracksList.setLayoutManager(layoutManager);
        //设置适配器
        mPlayListAdapter = new PlayListAdapter();
        mTracksList.setAdapter(mPlayListAdapter);
        mPlayModeContainer = mPopView.findViewById(R.id.play_list_play_mode_container);
    }

    private void initEvent() {
        //点击关闭之后，窗口消失
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SobPopWindow.this.dismiss();
            }
        });
    }

    /**
     * 给适配器设置数据
     * @param data
     */
    public void setListData(List<Tracks> data) {
        if (mPlayListAdapter != null) {
            mPlayListAdapter.setData(data);
        }
    }
    public void setCurrentPlayPosition(int position) {
        if (mPlayListAdapter != null) {
            mPlayListAdapter.setCurrentPlayPosition(position);
            //使此时播放的位置置顶(到达最上面)
            mTracksList.scrollToPosition(position);
        }
    }
    public void setPlayListItemClickListener(PlayListItemClickListener listener) {
        mPlayListAdapter.setOnItemClickListener(listener);
    }
    //此处更新列表内的UI
    public void updatePlayMode(int playMode) {
        updatePlayModeBtnImg(playMode);
    }

    /**
     * 更新切换列表顺序和逆序的按钮和文字更新
     * @param isReverse
     */
    public void updateOrderIcon(boolean isReverse) {
        mOrderIcon.setImageResource(isReverse?R.drawable.selector_player_mode_list_order:R.drawable.selector_player_mode_list_reverse);
        mOrderText.setText(BaseApplication.getAppContext().getResources().getString(isReverse?R.string.order_text:R.string.reverse_text));
    }
    /**
     * 根据当前的状态，跟新播放模式图标
     * PLAY_MODEL_LIST
     * PLAY_MODEL_LIST_LOOP
     * PLAY_MODEL_RANDOM
     * PLAY_MODEL_SINGLE_LOOP
     */
    public final static int PLAY_MODEL_LIST = 0;
    public final static int PLAY_MODEL_LIST_LOOP = 1;
    private void updatePlayModeBtnImg(int playMode) {
        int resId = R.drawable.selector_player_mode_list_order;
        int textId = R.string.play_mode_order_text;
        switch (playMode) {
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_player_mode_list_order;
                textId = R.string.play_mode_order_text;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_player_mode_list_order_loop;
                textId = R.string.play_mode_list_play_text;
                break;
        }
        mPlayModeIv.setImageResource(resId);
        mPlayModeTv.setText(textId);
    }

    public interface PlayListItemClickListener {
        void onItemClick(int position);
    }
}
