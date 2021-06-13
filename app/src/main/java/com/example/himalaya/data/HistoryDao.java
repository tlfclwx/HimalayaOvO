package com.example.himalaya.data;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.interfaces.IHistoryDao;
import com.example.himalaya.interfaces.IHistoryDaoCallback;
import com.example.himalaya.json.Tracks;
import com.example.himalaya.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static com.example.himalaya.utils.Constants.HISTORY_COVER;
//import static com.example.himalaya.utils.Constants.HISTORY_COVER_URL_MIDDLE;
import static com.example.himalaya.utils.Constants.HISTORY_DOWN_URL;
import static com.example.himalaya.utils.Constants.HISTORY_DURATION;
import static com.example.himalaya.utils.Constants.HISTORY_ID;
import static com.example.himalaya.utils.Constants.HISTORY_NICKNAME;
import static com.example.himalaya.utils.Constants.HISTORY_PLAY_COUNT;
import static com.example.himalaya.utils.Constants.HISTORY_TB_NAME;
import static com.example.himalaya.utils.Constants.HISTORY_TITLE;
import static com.example.himalaya.utils.Constants.HISTORY_TRACK_ID;
import static com.example.himalaya.utils.Constants.HISTORY_UPDATE_TIME;
import static com.example.himalaya.utils.Constants.SUB_ALBUM_ID;

public class HistoryDao implements IHistoryDao {

    private static final String TAG = "HistoryDao";
    private final XImalayaDBHelper mDbHelper;
    private IHistoryDaoCallback mCallback = null;
    private Object mLock = new Object();

    public HistoryDao(){
        mDbHelper = new XImalayaDBHelper(BaseApplication.getAppContext());
    }
    @Override
    public void setCallback(IHistoryDaoCallback callback) {
         this.mCallback = callback;
    }

    @Override
    public void addHistory(Tracks tracks) {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            boolean isSuccess = false;
            try {
                db = mDbHelper.getWritableDatabase();
                db.beginTransaction();
                ContentValues values = new ContentValues();
                //封装数据
                values.put(HISTORY_TRACK_ID, tracks.getId());
                values.put(HISTORY_COVER, tracks.getCover_url_large());
                values.put(HISTORY_TITLE, tracks.getTrack_title());
                values.put(HISTORY_DOWN_URL, tracks.getDownload_url());
                values.put(HISTORY_PLAY_COUNT, tracks.getPlay_count());
                values.put(HISTORY_DURATION, tracks.getDuration());
                values.put(HISTORY_UPDATE_TIME, tracks.getUpdated_at());
                values.put(HISTORY_NICKNAME, tracks.getAnnouncer().getNickname());
                //values.put(HISTORY_COVER_URL_MIDDLE, tracks.getCover_url_middle());
                Log.d(TAG, "``````````````````   " + tracks.getAnnouncer().getNickname());
                //插入数据
                db.insert(Constants.HISTORY_TB_NAME, null, values);
                db.setTransactionSuccessful();
                isSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                isSuccess = false;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallback != null) {
                    mCallback.onHistoryAdd(isSuccess);
                }
            }
        }

    }

    @Override
    public void deleteHistory(Tracks tracks) {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            boolean isDeleted = false;
            try {
                db = mDbHelper.getWritableDatabase();
                db.beginTransaction();
                //删除数据,返回的是删除的个数
                int delete = db.delete(Constants.HISTORY_TB_NAME, HISTORY_TRACK_ID + "=?", new String[]{tracks.getId() + ""});
                Log.d(TAG, "delete -- > " + delete);
                db.setTransactionSuccessful();
                isDeleted = true;
            } catch (Exception e) {
                e.printStackTrace();
                isDeleted = false;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallback != null) {
                    mCallback.onHistoryDel(isDeleted);
                }
            }
        }
    }

    @Override
    public void clearHistory() {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            boolean isDeleted = false;
            try {
                db = mDbHelper.getWritableDatabase();
                db.beginTransaction();
                //删除数据,返回的是删除的个数
                db.delete(HISTORY_TB_NAME, null, null);
                db.setTransactionSuccessful();
                isDeleted = true;
            } catch (Exception e) {
                e.printStackTrace();
                isDeleted = false;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallback != null) {
                    mCallback.onHistoriesClear(isDeleted);
                }
            }
        }
    }

    @Override
    public void listHistory() {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            List<Tracks> historyTracks = new ArrayList<>();
            try {
                db = mDbHelper.getReadableDatabase();
                db.beginTransaction();
                Cursor cursor = db.query(HISTORY_TB_NAME, null, null, null, null, null, "_id desc");

                while (cursor.moveToNext()) {
                    Tracks tracks = new Tracks();
                    int trackId = cursor.getInt(cursor.getColumnIndex(HISTORY_TRACK_ID));
                    String cover = cursor.getString(cursor.getColumnIndex(HISTORY_COVER));
                    String title = cursor.getString(cursor.getColumnIndex(HISTORY_TITLE));
                    int playCount = cursor.getInt(cursor.getColumnIndex(HISTORY_PLAY_COUNT));
                    String downUrl = cursor.getString(cursor.getColumnIndex(HISTORY_DOWN_URL));
                    int duration = cursor.getInt(cursor.getColumnIndex(HISTORY_DURATION));
                    Long updateTime = cursor.getLong(cursor.getColumnIndex(HISTORY_UPDATE_TIME));
                    String nickname = cursor.getString(cursor.getColumnIndex(HISTORY_NICKNAME));
                    //String coverMid = cursor.getString(cursor.getColumnIndex(HISTORY_COVER_URL_MIDDLE));

                    tracks.setId(trackId);
                    tracks.setCover_url_large(cover);
                    tracks.setCover_url_middle(cover);
                    tracks.setCover_url_small(cover);
                    tracks.setTrack_title(title);
                    tracks.setPlay_count(playCount);
                    tracks.setDuration(duration);
                    tracks.setUpdated_at(updateTime);
                    tracks.setDownload_url(downUrl);
                    Tracks.AnnouncerBean announcerBean = new Tracks.AnnouncerBean();
                    announcerBean.setNickname(nickname);
                    tracks.setAnnouncer(announcerBean);
                    //tracks.setCover_url_middle(coverMid);
                    historyTracks.add(tracks);
                }
                //cursor.close();
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallback != null) {
                    mCallback.onHistoriesLoaded(historyTracks);
                }
            }
        }
    }
}
