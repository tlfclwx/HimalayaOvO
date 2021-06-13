package com.example.himalaya.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.himalaya.json.Tracks;
import com.example.himalaya.PlayerActivity;
import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.interfaces.IPlayerCallback;
import com.example.himalaya.interfaces.IPlayerPresenter;
import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.utils.Urls;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PlayerPresenter implements IPlayerPresenter {

    private static final int DEFAULT_PLAY_INDEX = 0;
    private List<IPlayerCallback> mIPlayerCallbacks = new ArrayList<>();
    private static final String TAG = "PlayerPresenter";
    private Tracks mCurrentTrack = null;
    private int mCurrentIndex = DEFAULT_PLAY_INDEX;
    //当前的专辑Id
    private int mCurrentAlbumId = -1;
    //当前页
    private int mCurrentPageIndex = 0;
    //sp's key and name
    public static final String PLAY_MODE_SP_NAME = "PlayMode";
    public static final String PLAY_MODE_SP_KEY = "currentPlayMode";
    private List<Tracks> mTracks = new ArrayList<>();
    private int mCurrentProcessPosition = 0;
    private int mProcessDuration = 0;
    private MediaPlayer mediaPlayer;
    private Timer mTimer;
    private int currentState = 0;  //分三种状态，0 还未设置过播放  1  正在播放  2 暂停状态
    private final SharedPreferences mPlayModSP;
    //播放模式
    public final static int PLAY_MODEL_LIST_INT = 0;
    public final static int PLAY_MODEL_LIST_LOOP_INT = 1;
    public final static int PLAY_MODEL_RANDOM_INT = 2;
    public final static int PLAY_MODEL_SINGLE_LOOP_INT = 3;
    private int mCurrentPlayMode = PLAY_MODEL_LIST_INT;
    private PlayerActivity mPlayerActivity;

    private PlayerPresenter() {
        mediaPlayer = new MediaPlayer();
        mPlayerActivity = new PlayerActivity();
        mPlayModSP = BaseApplication.getAppContext().getSharedPreferences(PLAY_MODE_SP_NAME, Context.MODE_PRIVATE);
    }

    public static PlayerPresenter sPlayerPresenter;

    public static PlayerPresenter getPlayerPresenter() {
        if (sPlayerPresenter == null) {
            synchronized (PlayerPresenter.class) {
                if (sPlayerPresenter == null) {
                    sPlayerPresenter = new PlayerPresenter();
                }
            }
        }
        return sPlayerPresenter;
    }


    public void addTime() {
        if (mTimer == null) {
            mTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (currentState != 0) {
                        int currentTime = mediaPlayer.getCurrentPosition();
                        int finalTIme = mediaPlayer.getDuration();
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("duration", finalTIme);
                        bundle.putInt("currentPosition", currentTime);
                        message.setData(bundle);
                        PlayerActivity.mHandler.sendMessage(message);
                    }
                }
            };
            mTimer.schedule(timerTask, 5, 500);
        }
    }

    private boolean isPlayListSet = false;

    public int getCurrentTrack() {
        if (mCurrentTrack != null) {
            return mCurrentTrack.getId();
        }
        return 0;
    }

    public void setPlayList(List<Tracks> list, int playIndex) {
        if (mediaPlayer != null) {
            mTracks = list;
            isPlayListSet = true;
            mCurrentTrack = list.get(playIndex);
            mCurrentIndex = playIndex;
            if (mediaPlayer.isPlaying()) pause();
            play(playIndex);
        } else {
            LogUtil.d(TAG, "setPlayList is null");
        }
    }

    @Override
    public void play() {

        if (isPlayListSet) {
            try {
                currentState = 1;
                mediaPlayer = new MediaPlayer();
                Log.d(TAG, mTracks.get(mCurrentIndex).getDownload_url());
                mediaPlayer.setDataSource(mTracks.get(mCurrentIndex).getDownload_url());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    //异步准备监听
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();//播放
                        addTime();
                        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                            Log.d(TAG, iPlayerCallback.toString());
                            iPlayerCallback.onTrackUpdate(mTracks.get(mCurrentIndex), mCurrentIndex);
                            iPlayerCallback.onPlayStart();
                        }
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mCurrentPlayMode == 0) {
                            mediaPlayer.start();
                        } else {
                            if (mCurrentIndex != mTracks.size() - 1) {
                                setPlayList(mTracks, mCurrentIndex + 1);
                            } else {
                                setPlayList(mTracks, 0);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void play(final int id) {
        try {
            HistoryPresenter historyPresenter = HistoryPresenter.getInstance();
            boolean isGet = historyPresenter.isGet(mTracks.get(id));
            if (!isGet) historyPresenter.addHistory(mTracks.get(id));
            currentState = 1;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(mTracks.get(id).getDownload_url());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                //异步准备监听
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();//播放
                    addTime();
                    for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                        iPlayerCallback.onTrackUpdate(mTracks.get(id), id);
                        iPlayerCallback.onPlayStart();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mCurrentPlayMode == 0) {
                        mediaPlayer.start();
                    } else {
                        if (mCurrentIndex != mTracks.size() - 1) {
                            setPlayList(mTracks, mCurrentIndex + 1);
                        } else {
                            setPlayList(mTracks, 0);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            currentState = 2;
            mTimer.cancel();
            mTimer = null;
            mediaPlayer.pause();
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onPlayPause();
            }
        }

    }

    public void seekTo(int ms) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(ms);
        }
    }

    @Override
    public void stop() {

    }

    public void reStart() {
        if (mediaPlayer != null) {
            currentState = 1;
            mediaPlayer.start();
            addTime();
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onPlayStart();
                iPlayerCallback.onTrackUpdate(mTracks.get(mCurrentIndex), mCurrentIndex);
            }
        }
    }

    @Override
    public void playPre() {
        if (mediaPlayer != null) {
            if (mCurrentIndex != 0) {
                setPlayList(mTracks, mCurrentIndex - 1);
            }
        }
    }

    @Override
    public void playNext() {
        if (mediaPlayer != null) {
            if (mCurrentIndex != mTracks.size() - 1) {
                setPlayList(mTracks, mCurrentIndex + 1);
            }
        }
    }

    @Override
    public void getPlayList() {
        if (mediaPlayer != null) {
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onListLoaded(mTracks);
            }
        }
    }

    @Override
    public void playByIndex(int index) {
        //切换播放器到index的位置进行播放
        if (mediaPlayer != null) {
            if (currentState == 1) pause();
            mCurrentIndex = index;
            play(index);
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onTrackUpdate(mTracks.get(index), index);
            }
        }
    }


    @Override
    public int isPlaying() {
        //返回当前是否正在播放
        return currentState;
    }


    @Override
    public void playByAlbumId(int id) {
        mCurrentAlbumId = id;
        mCurrentPageIndex = 1;
        //1,创建OKHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Urls.getUrlDetail(mCurrentAlbumId, mCurrentPageIndex);
        //2,创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //3,创建一个call对象
        Call call = okHttpClient.newCall(request);

        //4,将请求添加到调度中
        call.enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String data = response.body().string();
                List<Tracks> listTrack = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray tracks = jsonObject.getJSONArray("tracks");
                    for (int i = 0; i < tracks.length(); i++) {
                        JSONObject jsonObject1 = tracks.getJSONObject(i);
                        Gson gson = new Gson();
                        Tracks track = gson.fromJson(jsonObject1.toString(), Tracks.class);
                        listTrack.add(track);
                    }
                    if (listTrack != null && listTrack.size() > 0) {
                        mTracks.clear();
                        mTracks.addAll(listTrack);
                        isPlayListSet = true;
                        play();
                        mCurrentTrack = listTrack.get(DEFAULT_PLAY_INDEX);
                        mCurrentIndex = DEFAULT_PLAY_INDEX;
                        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                            iPlayerCallback.onTrackUpdate(mTracks.get(0), 0);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // 3. 播放
    }


    @Override
    public void switchPlayMode(int mode) {
        if (mediaPlayer != null) {
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onPlayModeChange(mode);
            }
            //保存到sp里面去
            mCurrentPlayMode = mode;
            SharedPreferences.Editor edit = mPlayModSP.edit();
            edit.putInt(PLAY_MODE_SP_KEY, mode);
            edit.commit();
        }
    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        //通知当前的节目
        iPlayerCallback.onTrackUpdate(mCurrentTrack, mCurrentIndex);
        //更新状态
        handlePlayStatus(iPlayerCallback);
        iPlayerCallback.onProgressChange(mCurrentProcessPosition, mProcessDuration);
        mCurrentPlayMode = mPlayModSP.getInt(PLAY_MODE_SP_KEY, PLAY_MODEL_LIST_INT);
        iPlayerCallback.onPlayModeChange(mCurrentPlayMode);
        if (!mIPlayerCallbacks.contains(iPlayerCallback)) {
            mIPlayerCallbacks.add(iPlayerCallback);
        }
    }

    private void handlePlayStatus(IPlayerCallback iPlayerCallback) {
        int playerStatus = isPlaying();
        //根据状态调用接口的方法
        if (playerStatus == 1) {
            iPlayerCallback.onPlayStart();
        } else {
            iPlayerCallback.onPlayPause();
        }
    }

    @Override
    public void unregisterViewCallback(IPlayerCallback iPlayerCallback) {
        mIPlayerCallbacks.remove(iPlayerCallback);
    }

    public boolean hasPlayList() {
        return isPlayListSet;
    }


    //======================上面是播放器相关的回调方法， end======================

}
