package com.example.himalaya.presenters;


import android.util.Log;

import com.example.himalaya.json.Bean;
import com.example.himalaya.json.Hotworks;
import com.example.himalaya.json.QueryResults;
import com.example.himalaya.data.XimalayApi;
import com.example.himalaya.interfaces.ISearchCallback;
import com.example.himalaya.interfaces.ISearchPresenter;
import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.utils.Urls;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchPresenter implements ISearchPresenter {

    private List<Bean> mSearchResult = new ArrayList<>();

    public LinkedHashMap<String, String> mHashMaps = new LinkedHashMap<>();
    private static final String TAG = "SearchPresenter";
    //当前的搜索关键字
    private String mCurrentKeyword = null;
    private XimalayApi mXimalayApi;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;

    private SearchPresenter() {
        mXimalayApi = XimalayApi.getXimalayApi();
    }

    private static SearchPresenter sSearchPresenter = null;

    public static SearchPresenter getSearchPresenter() {
        if (sSearchPresenter == null) {
            synchronized (SearchPresenter.class) {
                if (sSearchPresenter == null) {
                    sSearchPresenter = new SearchPresenter();
                }
            }
        }
        return sSearchPresenter;
    }


    private List<ISearchCallback> mCallback = new ArrayList<>();

    @Override
    public void doSearch(String keyword) {
        mCurrentPage = DEFAULT_PAGE;
        mSearchResult.clear();
        //用于得新搜索
        //当网络不好的时候 ,用户会点击重新搜索
        this.mCurrentKeyword = keyword;
        search(keyword);
    }

    private void search(String keyword) {
        //1,创建OKHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Urls.getUrlBean(keyword, mCurrentPage);
        //注意我这里用的是http而不是https

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
                for (ISearchCallback iSearchCallback : mCallback) {
                    if (mIsLoadMore) {
                        iSearchCallback.onLoadMoreResult(mSearchResult, false);
                        mCurrentPage--;
                        mIsLoadMore = false;
                    } else {
                        iSearchCallback.onError();
                    }
                }

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String data = response.body().string();
                List<Bean> listBeans = new ArrayList<>();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(data);
                    JSONArray tracks = jsonObject.getJSONArray("albums");
                    mHashMaps = stringToJsonObject(tracks.toString());
                    for(Map.Entry<String, String> entry : mHashMaps.entrySet()) {

                        Object value = entry.getValue();
                        String ss = value.toString();
                        JSONObject jsonobj = null;
                        try {
                            if(ss != null && ss.startsWith("\ufeff"))
                            {
                                ss =  ss.substring(1);
                            }
                            jsonobj = new JSONObject(ss);
                            JSONObject string1 = jsonobj.getJSONObject("nameValuePairs");
                            Gson gson = new Gson();
                            Bean bean = gson.fromJson(string1.toString(), Bean.class);
                            listBeans.add(bean);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mSearchResult.addAll(listBeans);
                    if (listBeans != null) {
                        if (mIsLoadMore) {
                            for (ISearchCallback iSearchCallback : mCallback) {
                                iSearchCallback.onLoadMoreResult(mSearchResult, listBeans.size() != 0);
                            }
                            mIsLoadMore = false;
                        } else {
                            for (ISearchCallback iSearchCallback : mCallback) {
                                iSearchCallback.onSearchResultLoaded(mSearchResult);
                            }
                        }
                    } else {
                        LogUtil.d(TAG, "album is null..");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
        private LinkedHashMap<String, String> stringToJsonObject(String response) {
            try {
                Object json=new JSONTokener(response).nextValue();
                if(json instanceof JSONObject){
                    JSONObject jso = new JSONObject(response);
                    return JsonObjectToHashMap(jso);
                }else{
                    JSONArray jsa=new JSONArray(response);
                    return JsonArrayToHashMap(jsa);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        private LinkedHashMap<String, String> JsonObjectToHashMap(JSONObject jso) {
            LinkedHashMap<String, String> hashmap = new LinkedHashMap<>();
            try {
                for (Iterator<String> keyStr = jso.keys(); keyStr.hasNext(); ) {
                    String key1 = keyStr.next().trim();
                    if (jso.get(key1) instanceof JSONObject) {
                        Gson gson = new Gson();
                        JSONObject NextJSONObject=new JSONObject(jso.get(key1).toString());
                        hashmap.put(key1, gson.toJson(NextJSONObject));
                    }else if(jso.get(key1) instanceof JSONArray) {
                        Gson gson = new Gson();
                        JSONArray NextJSONArray =new JSONArray(jso.get(key1).toString());
                        hashmap.put(key1, gson.toJson(NextJSONArray));
                    }else {
                        hashmap.put(key1, jso.get(key1).toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return hashmap;
        }
        private LinkedHashMap<String, String> JsonArrayToHashMap(JSONArray jsa){
            LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();

            try {
                for (int i = 0; i < jsa.length(); i++) {
                    if(jsa.get(i) instanceof JSONArray) {
                        Gson gson = new Gson();
                        JSONArray NextJSONArray=new JSONArray(jsa.get(i).toString());
                        hashMap.put(String.valueOf(i), gson.toJson(NextJSONArray));
                    }else if(jsa.get(i) instanceof JSONObject){
                        Gson gson = new Gson();
                        JSONObject NextJSONObject=new JSONObject(jsa.get(i).toString());
                        hashMap.put(String.valueOf(i),gson.toJson(NextJSONObject));
                    }
                }
            }catch (JSONException e){
                e.getStackTrace();
            }
            return hashMap;
        }
    @Override
    public void reSearch() {
        search(mCurrentKeyword);
    }

    private boolean mIsLoadMore = false;

    @Override
    public void loadMore() {
        //判断有没有必要进行加载更多
        if (mSearchResult.size() < Constants.COUNT_DEFAULT) {
            for (ISearchCallback iSearchCallback : mCallback) {
                iSearchCallback.onLoadMoreResult(mSearchResult, false);
            }
        } else {
            mIsLoadMore = true;
            mCurrentPage++;
            search(mCurrentKeyword);
        }
    }

    @Override
    public void getHotWord() {
        //做一个热词缓存
        //1,创建OKHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Urls.getUrlHotWork();
        //注意我这里用的是http而不是https

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
                Log.d(TAG, "///////////////////");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String data = response.body().string();
                List<Hotworks> listHotwords = new ArrayList<>();
                try {
                    JSONArray tracks = new JSONArray(data);

                    for (int i = 0; i < tracks.length(); i++) {
                        JSONObject jsonObject1 = tracks.getJSONObject(i);
                        Gson gson = new Gson();
                        Hotworks track = gson.fromJson(jsonObject1.toString(), Hotworks.class);
                        listHotwords.add(track);
                    }
                    for (ISearchCallback iSearchCallback : mCallback) {
                        iSearchCallback.onHotWordLoaded(listHotwords);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getRecommendWord(String keyword) {
        //1,创建OKHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Urls.getUrlQueryResult(keyword);
        //注意我这里用的是http而不是https

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
                Log.d(TAG, "///////////////////");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String data = response.body().string();
                List<QueryResults> listQueryResults = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray tracks = jsonObject.getJSONArray("keywords");
                    for (int i = 0; i < tracks.length(); i++) {
                        JSONObject jsonObject1 = tracks.getJSONObject(i);
                        Gson gson = new Gson();
                        QueryResults track = gson.fromJson(jsonObject1.toString(), QueryResults.class);
                        listQueryResults.add(track);
                    }
                    for (ISearchCallback iSearchCallback : mCallback) {
                        iSearchCallback.onRecommendWordLoaded(listQueryResults);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchCallback iSearchCallback) {
        if (!mCallback.contains(iSearchCallback)) {
            mCallback.add(iSearchCallback);
        }
    }

    @Override
    public void unregisterViewCallback(ISearchCallback iSearchCallback) {
        mCallback.remove(iSearchCallback);
    }
}
