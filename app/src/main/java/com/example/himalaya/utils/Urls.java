package com.example.himalaya.utils;


import com.example.himalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.AccessTokenManager;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.httputil.XimalayaException;
import com.ximalaya.ting.android.opensdk.httputil.util.SignatureUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Urls {
    private static AccessTokenManager accessTokenManager = AccessTokenManager.getInstanse();
    private static final CommonRequest mXimalaya = CommonRequest.getInstanse();

    public static String getUrlRecommend() {
        Map<String, String> mp = new HashMap<String, String>();
        mp.put("device_type", "2");
        mp.put("app_key", getAppKey());
        mp.put("device_id", getDeviceId());
        mp.put("access_token", accessTokenManager.getAccessToken());
        mp.put("sdk_version", mXimalaya.getSdkVersion());
        mp.put("pack_id", getPackId());
        mp.put("like_count", "50");
        mp.put("client_os_type", "2");
        String appsecret = "8646d66d6abe2efd14f2891f9fd1c8af";
        String www = DTransferConstants.ALBUM_GUESS_LIKE + "?access_token=" + accessTokenManager.getAccessToken() + "&app_key=" + getAppKey() + "&client_os_type=" + mXimalaya.getClientOsType() + "&device_id=" + getDeviceId() + "&device_type=2&like_count=50&pack_id=" + getPackId() + "&sdk_version=" + mXimalaya.getSdkVersion() + "&sig=" + SignatureUtil.generateSignature(appsecret, mp);
        return www;
    }

    public static String getUrlDetail(int currentAlbumId, int currentPageIndex) {
        Map<String, String> mp = new HashMap<String, String>();
        mp.put("app_key", getAppKey());
        mp.put("album_id", "" + currentAlbumId);
        mp.put("device_id", getDeviceId());
        mp.put("access_token", accessTokenManager.getAccessToken());
        mp.put("sdk_version", mXimalaya.getSdkVersion());
        mp.put("count", "" + Constants.COUNT_DEFAULT);
        mp.put("pack_id", getPackId());
        mp.put("sort", "asc");
        mp.put("page", "" + currentPageIndex);
        mp.put("client_os_type", "" + Constants.CLIENT_OS_TYPE);
        String appsecret = "8646d66d6abe2efd14f2891f9fd1c8af";
        String www = DTransferConstants.TRACKLIST_URL + "?access_token=" + accessTokenManager.getAccessToken() + "&album_id=" + currentAlbumId + "&app_key=" + getAppKey() + "&client_os_type=" + mXimalaya.getClientOsType() + "&count=50" + "&device_id=" + getDeviceId() + "&pack_id=" + getPackId() + "&page=" + currentPageIndex + "&sdk_version=" + mXimalaya.getSdkVersion() + "&sig=" + SignatureUtil.generateSignature(appsecret, mp) + "&sort=asc";

        return www;
    }

    public static String getUrlLoaded(int currentAlbumId, int currentPageIndex) {
        Map<String, String> mp = new HashMap<String, String>();
        mp.put("app_key", getAppKey());
        mp.put("album_id", "" + currentAlbumId);
        mp.put("device_id", getDeviceId());
        mp.put("access_token", accessTokenManager.getAccessToken());
        mp.put("sdk_version", mXimalaya.getSdkVersion());
        mp.put("count", "" + Constants.COUNT_DEFAULT);
        mp.put("pack_id", getPackId());
        mp.put("sort", "asc");
        mp.put("page", "" + currentPageIndex);
        mp.put("client_os_type", "" + Constants.CLIENT_OS_TYPE);
        String appsecret = "8646d66d6abe2efd14f2891f9fd1c8af";
        String www = DTransferConstants.TRACKLIST_URL + "?access_token=" + accessTokenManager.getAccessToken() + "&album_id=" + currentAlbumId + "&app_key=" + getAppKey() + "&client_os_type=" + mXimalaya.getClientOsType() + "&count=50" + "&device_id=" + getDeviceId() + "&pack_id=" + getPackId() + "&page=" + currentPageIndex + "&sdk_version=" + mXimalaya.getSdkVersion() + "&sig=" + SignatureUtil.generateSignature(appsecret, mp) + "&sort=asc";
        return www;
    }

    public static String getUrlHotWork() {
        Map<String, String> mp = new HashMap<String, String>();
        mp.put("app_key", getAppKey());
        mp.put("device_id", getDeviceId());
        mp.put("access_token", accessTokenManager.getAccessToken());
        mp.put("sdk_version", mXimalaya.getSdkVersion());
        mp.put("pack_id", getPackId());
        mp.put("client_os_type", "" + Constants.CLIENT_OS_TYPE);
        mp.put("top", "" + Constants.TOP_SIZE);
        String appsecret = "8646d66d6abe2efd14f2891f9fd1c8af";
        String www = DTransferConstants.SEARCH_HOT_WORDS_URL + "?access_token=" + accessTokenManager.getAccessToken() + "&app_key=" + getAppKey() + "&client_os_type=" + mXimalaya.getClientOsType() + "&device_id=" + getDeviceId() + "&pack_id=" + getPackId() + "&sdk_version=" + mXimalaya.getSdkVersion() + "&sig=" + SignatureUtil.generateSignature(appsecret, mp) + "&top=" + Constants.TOP_SIZE;
        return www;
    }

    public static String getUrlBean(String word, int page) {
        Map<String, String> mp = new HashMap<String, String>();
        mp.put("app_key", getAppKey());
        mp.put("device_id", getDeviceId());
        mp.put("access_token", accessTokenManager.getAccessToken());
        mp.put("sdk_version", mXimalaya.getSdkVersion());
        mp.put("pack_id", getPackId());
        mp.put("client_os_type", "" + Constants.CLIENT_OS_TYPE);
        mp.put("page", "" + page);
        mp.put("q", word);
        mp.put("count", "" + Constants.COUNT_DEFAULT);
        String appsecret = "8646d66d6abe2efd14f2891f9fd1c8af";
        String encodeStr = null;
        try {
            encodeStr = URLEncoder.encode(word, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String www = DTransferConstants.SEARCH_ALBUMLIST_URL + "?access_token=" + accessTokenManager.getAccessToken() + "&app_key=" + getAppKey() + "&client_os_type=" + mXimalaya.getClientOsType() + "&count=50" + "&device_id=" + getDeviceId() + "&pack_id=" + getPackId() + "&page=" + page + "&q=" + encodeStr + "&sdk_version=" + mXimalaya.getSdkVersion() + "&sig=" + SignatureUtil.generateSignature(appsecret, mp);
        return www;
    }

    public static String getUrlQueryResult(String word) {

        Map<String, String> mp = new HashMap<String, String>();
        mp.put("app_key", getAppKey());
        mp.put("device_id", getDeviceId());
        mp.put("access_token", accessTokenManager.getAccessToken());
        mp.put("sdk_version", mXimalaya.getSdkVersion());
        mp.put("pack_id", getPackId());
        mp.put("client_os_type", "" + Constants.CLIENT_OS_TYPE);
        mp.put("q", word);
        String appsecret = "8646d66d6abe2efd14f2891f9fd1c8af";
        String encodeStr = null;
        try {
            encodeStr = URLEncoder.encode(word, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String www = DTransferConstants.SEARCH_SUGGEST_WORDS_URL + "?access_token=" + accessTokenManager.getAccessToken() + "&app_key=" + getAppKey() + "&client_os_type=" + mXimalaya.getClientOsType() + "&device_id=" + getDeviceId() + "&pack_id=" + getPackId() + "&q=" + encodeStr + "&sdk_version=" + mXimalaya.getSdkVersion() + "&sig=" + SignatureUtil.generateSignature(appsecret, mp);
        return www;
    }

    private static String getAppKey() {

        String appKey = null;
        try {
            appKey = BaseApplication.getApi().getAppKey();
        } catch (XimalayaException e) {
            e.printStackTrace();
        }
        return appKey;
    }

    private static String getDeviceId() {
        String deviceId = null;
        try {
            deviceId = BaseApplication.getApi().getDeviceId();
        } catch (XimalayaException e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    private static String getPackId() {
        String packId = null;
        try {
            packId = BaseApplication.getApi().getPackId();
        } catch (XimalayaException e) {
            e.printStackTrace();
        }
        return packId;
    }
}
