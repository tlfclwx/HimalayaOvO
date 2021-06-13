package com.example.himalaya.utils;

public class Constants {

    private static final String TAG = "Constants";
    //获取推荐列表的专辑数量
    public static int COUNT_RECOMMEND = 50;

    //默认列表请求数量
    public static int COUNT_DEFAULT = 50;

    //热词的数量
    public static int COUNT_HOT_WORD = 10;

    public static int CLIENT_OS_TYPE = 2;
    //数据库相关的常量
    public static final String DB_NAME = "ximalaya.db";
    //数据库的版本
    public static final int DB_VERSION_CODE = 1;
    //订阅的表名
    public static final String SUB_TB_NAME = "tb_subscription";
    public static final String SUB_ID = "_id";
    public static final String SUB_COVER_URL = "cover";
    public static final String SUB_TITLE = "title";
    public static final String SUB_DESCRIPTION = "description";
    public static final String SUB_PLAY_COUNT = "playCount";
    public static final String SUB_TRACKS_COUNT = "tracksCount";
    public static final String SUB_AUTHOR_NAME = "authorName";
    public static final String SUB_ALBUM_ID = "albumId";
    //订阅的最多数量
    public static final int MAX_SUB_COUNT = 100;
    //历史记录表名
    public static final String HISTORY_TB_NAME = "tb_history";
    public static final String HISTORY_ID = "_id";
    public static final String HISTORY_TRACK_ID = "track_id";
    public static final String HISTORY_TITLE = "history_title";
    public static final String HISTORY_PLAY_COUNT = "history_play_count";
    public static final String HISTORY_DURATION = "history_duration";
    public static final String HISTORY_UPDATE_TIME = "history_update_time";
    public static final String HISTORY_COVER = "history_cover";
    public static final String HISTORY_DOWN_URL = "history_down_url";
    public static final String HISTORY_NICKNAME = "history_nickname";
    //历史记录最多的数量
    public static final int MAX_HISTORY_COUNT = 100;
    public static int TOP_SIZE = 10;

}
