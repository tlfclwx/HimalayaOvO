package com.example.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.R;
import com.example.himalaya.json.Tracks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TrackListHistoryAdapter extends RecyclerView.Adapter<TrackListHistoryAdapter.InnerHolder>{
    private static final String TAG = "TrackListAdapter";
    private static List<Tracks> mDetailData1 = new ArrayList<>();
    //格式化时间
    private SimpleDateFormat mUpdateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");
    private TrackListHistoryAdapter.ItemClickListener mItemClickListener = null;
    private TrackListHistoryAdapter.ItemLongClickListener mItemLongClickListener = null;

    @NonNull
    @Override
    public TrackListHistoryAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail, parent, false);
        return new TrackListHistoryAdapter.InnerHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull TrackListHistoryAdapter.InnerHolder holder, final int position) {
        //找到控件，设置数据
        //此处的itemView其实就是下面的super(itemView)中的itemView
        View itemView = holder.itemView;
        //顺序ID
        TextView orderTv = itemView.findViewById(R.id.order_text);
        //标题Title
        TextView titleTv = itemView.findViewById(R.id.detail_item_title);
        //播放次数
        TextView playCountTv = itemView.findViewById(R.id.detaile_item_play_count);
        //时长
        TextView durationTv = itemView.findViewById(R.id.detail_item_duration);
        //更新日期
        TextView updateDateTv = itemView.findViewById(R.id.detail_item_update_time);
        //设置数据
        final Tracks track = mDetailData1.get(position);
        orderTv.setText(position + 1 + "");
        titleTv.setText(track.getTrack_title());
        playCountTv.setText(track.getPlay_count() + "");
        int durationMil = track.getDuration() * 1000;
        String duration = mDurationFormat.format(durationMil);
        durationTv.setText(duration + "");
        String updateTimeText = mUpdateDateFormat.format(track.getUpdated_at());
        updateDateTv.setText(updateTimeText);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置一个点击事件
                //Toast.makeText(view.getContext(), "you click the view about position -- > " + position, Toast.LENGTH_SHORT).show();
                if (mItemClickListener != null) {
                    //参数需要有列表和位置
                    mItemClickListener.onItemClick(mDetailData1, position);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemLongClickListener != null) {
                    mItemLongClickListener.onItemLongClick(track);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDetailData1 != null) {
            return mDetailData1.size();
        }
        return 0;
    }

    public void setData(List<Tracks> tracks) {
        //更新Ui
        //清除原来的数据
        //添加新的数据
        if (mDetailData1 != null) {
            mDetailData1.clear();
            mDetailData1.addAll(tracks);
        }
        //更新Ui
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setItemClickListener(TrackListHistoryAdapter.ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(List<Tracks> detailData, int position);
    }
    //长按取消
    public void setItemLongClickListener(TrackListHistoryAdapter.ItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }
    public interface ItemLongClickListener{
        void onItemLongClick(Tracks tracks);
    }
}
