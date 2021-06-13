package com.example.himalaya.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.interfaces.ISubDao;
import com.example.himalaya.interfaces.ISubDaoCallback;
import com.example.himalaya.json.Bean;
import com.example.himalaya.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.example.himalaya.utils.Constants.SUB_ALBUM_ID;
import static com.example.himalaya.utils.Constants.SUB_AUTHOR_NAME;
import static com.example.himalaya.utils.Constants.SUB_COVER_URL;
import static com.example.himalaya.utils.Constants.SUB_DESCRIPTION;
import static com.example.himalaya.utils.Constants.SUB_PLAY_COUNT;
import static com.example.himalaya.utils.Constants.SUB_TITLE;
import static com.example.himalaya.utils.Constants.SUB_TRACKS_COUNT;

public class SubscriptionDao implements ISubDao {
    private static final String TAG = "SubscriptionDao";
    private XImalayaDBHelper mXImalayaDBHelper;
    private static final SubscriptionDao ourInstance = new SubscriptionDao();
    private ISubDaoCallback mCallback = null;

    public static SubscriptionDao getInstance() {
        return ourInstance;
    }

    private SubscriptionDao() {
        mXImalayaDBHelper = new XImalayaDBHelper(BaseApplication.getAppContext());
    }

    @Override
    public void setCallback(ISubDaoCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void addBean(Bean bean) {
        SQLiteDatabase db = null;
        boolean isSuccess = false;
        try {
            db = mXImalayaDBHelper.getWritableDatabase();
            db.beginTransaction();
            //数据填入
            ContentValues contentValues = new ContentValues();
            //封装数据
            contentValues.put(SUB_COVER_URL, bean.getCover_url_large());
            contentValues.put(SUB_TITLE, bean.getAlbum_title());
            contentValues.put(SUB_DESCRIPTION, bean.getAlbum_intro());
            contentValues.put(SUB_PLAY_COUNT, bean.getPlay_count());
            contentValues.put(SUB_TRACKS_COUNT, bean.getInclude_track_count());
            contentValues.put(SUB_AUTHOR_NAME, bean.getAnnouncer().getNameValuePairs().getNickname());
            contentValues.put(SUB_ALBUM_ID, bean.getId());
            //插入数据
            db.insert(Constants.SUB_TB_NAME, null, contentValues);
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
                mCallback.onAddResult(isSuccess);
                listBeans();
            }
        }

    }

    @Override
    public void deleteBean(Bean bean) {
        SQLiteDatabase db = null;
        boolean isDeleted = false;
        try {
            db = mXImalayaDBHelper.getWritableDatabase();
            db.beginTransaction();
            //删除数据,返回的是删除的个数
            int delete = db.delete(Constants.SUB_TB_NAME, SUB_ALBUM_ID + "=?", new String[]{bean.getId() + ""});
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
                mCallback.onDeleteResult(isDeleted);
                listBeans();
            }
        }
    }

    @Override
    public void listBeans() {
        SQLiteDatabase db = null;
        List<Bean> result = new ArrayList<>();
        try {
            db = mXImalayaDBHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor query = db.query(Constants.SUB_TB_NAME, null, null, null, null, null, "_id desc");
            //封装数据
            while (query.moveToNext()) {
                Bean bean = new Bean();
                //图片封面
                String cover = query.getString(query.getColumnIndex(SUB_COVER_URL));
                bean.setCover_url_large(cover);
                //标题
                String title = query.getString(query.getColumnIndex(SUB_TITLE));
                bean.setAlbum_title(title);
                //描述
                String intro = query.getString(query.getColumnIndex(SUB_DESCRIPTION));
                bean.setAlbum_intro(intro);

                String  play_count = query.getString(query.getColumnIndex(SUB_PLAY_COUNT));
                bean.setPlay_count(play_count);
                int tracks_count = query.getInt(query.getColumnIndex(SUB_TRACKS_COUNT));
                bean.setInclude_track_count(tracks_count);
                String author_name = query.getString(query.getColumnIndex(SUB_AUTHOR_NAME));
                Bean.AnnouncerBean announcerBean = new Bean.AnnouncerBean();
                announcerBean.getNameValuePairs().setNickname(author_name);
                bean.setAnnouncer(announcerBean);
                long album_id = query.getLong(query.getColumnIndex(SUB_ALBUM_ID));
                bean.setId(album_id);
                result.add(bean);
            }

            query.close();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
            //把数据通知出去
            if (mCallback != null) {
                mCallback.onSubListLoaded(result);
            }
        }
    }
}
