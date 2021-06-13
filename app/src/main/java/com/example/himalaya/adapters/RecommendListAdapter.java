package com.example.himalaya.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.json.Bean;
import com.example.himalaya.R;
//import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 此处用来获取是界面数据
 * 在onBindViewHolder绑定数据的同时设置一个RecyclerView的点击事件，用来跳转到相对应的页面
 * 获取数据是时，调用setData方法，先清除先前的数据，然后把现在的数据全部加入，最后用notifyDataSetChanged()方法，用来重新加载数据（重新调用此类的方法）
 */
public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {
    private static final String TAG = "RecommendListAdapter";
    private List<Bean> mData = new ArrayList<>();
    private OnRecommendItemClickListener mItemClickListener;

    @Override
    public RecommendListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //这里是找到view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendListAdapter.InnerHolder holder, int position) {
       //这里是设定数据
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    int clickPosition = (int) v.getTag();
                    mItemClickListener.OnItemClick(clickPosition, mData.get(clickPosition));
                }
                Log.d(TAG, "you click item -- > " + v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        //返回要显示的个数
        if (mData != null) {
            Log.d(TAG, "size -- > " + mData.size());
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Bean> albumList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);
        }
        //更新一下UI
        //然后进行上面的onCreateViewHolder......
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Bean album) {
            ImageView albumCoverTv = itemView.findViewById(R.id.album_cover);
            //title
            TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);
            //描述
            TextView albumDesrcTv = itemView.findViewById(R.id.album_description_tv);
            //播放数量
            TextView albumPlayCountTv = itemView.findViewById(R.id.album_play_count);

            //专辑内容数量
            TextView albumContentCountTv = itemView.findViewById(R.id.album_content_size);
            albumTitleTv.setText(album.getAlbum_title());
            Log.d(TAG, "title ----> " + album.getAlbum_title());
            albumDesrcTv.setText(album.getAlbum_intro());
            albumPlayCountTv.setText("" + album.getPlay_count());
            albumContentCountTv.setText("" + album.getInclude_track_count());
            //Glide.with(itemView.getContext()).load(album.getCover_url_large()).into(albumCoverTv);
            Picasso.with(itemView.getContext()).load(album.getCover_url_large()).into(albumCoverTv);
        }
    }
    public void setOnRecommendItemClickListener(OnRecommendItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    public interface OnRecommendItemClickListener{
        void OnItemClick(int position, Bean album);
    }
}
