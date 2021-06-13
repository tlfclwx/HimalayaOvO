package com.example.himalaya.json;

public class QueryResults {

    /**
     * id : 107009430
     * keyword : 我的世界
     * highlight_keyword : <em>我</em>的世界
     */

    private int id;
    private String keyword;
    private String highlight_keyword;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getHighlight_keyword() {
        return highlight_keyword;
    }

    public void setHighlight_keyword(String highlight_keyword) {
        this.highlight_keyword = highlight_keyword;
    }
}
