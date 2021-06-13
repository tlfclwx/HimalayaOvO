package com.example.himalaya.json;

import android.os.Parcel;
import android.os.Parcelable;

public class Bean{

    /**
     * id : 4519297
     * kind : album
     * category_id : 1
     * album_title : 新闻早餐（听更多新闻，上喜马拉雅APP）
     * album_tags : 新闻,资讯,东方网,早新闻,社会,民生
     * album_intro : 新闻早餐
     * cover_url : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png
     * cover_url_small : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=5&upload_type=album&device_type=ios&name=mobile_small&magick=png
     * cover_url_middle : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=5&upload_type=album&device_type=ios&name=medium&magick=png
     * cover_url_large : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=5&upload_type=album&device_type=ios&name=mobile_large&magick=png
     * tracks_natural_ordered : false
     * announcer : {"nameValuePairs":{"id":52622741,"kind":"user","nickname":"东方网","avatar_url":"https://imgopen.xmcdn.com/group20/M05/19/EA/wKgJJ1eDW1_QCA5MAABNXnIlT3I433.png!op_type=3&columns=110&rows=110","is_verified":true,"anchor_grade":15}}
     * play_count : 886887306
     * favorite_count : 0
     * share_count : 11698
     * subscribe_count : 1574359
     * include_track_count : 1804
     * last_uptrack : {"nameValuePairs":{"track_id":406706967,"track_title":"先后发现4名死者！锡林浩特公布一重大刑事案侦破细节 | 新闻早餐 2021.4.18 星期日","duration":416,"created_at":1618698000000,"updated_at":1618698000000}}
     * can_download : true
     * is_finished : 1
     * updated_at : 1618698000000
     * created_at : 1465871528000
     * is_paid : false
     * estimated_track_count : 0
     * album_rich_intro : <p style="font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica, Arial, sans-serif;font-weight:normal" data-flag="normal">一日之计在于晨，新闻早餐不能少。<span><br /></span></p>
     * speaker_intro :
     * free_track_count : 0
     * free_track_ids :
     * sale_intro :
     * expected_revenue :
     * buy_notes :
     * speaker_title :
     * speaker_content :
     * has_sample : false
     * composed_price_type : 0
     * detail_banner_url :
     * album_score : 0.0
     * is_vipfree : false
     * is_vip_exclusive : false
     * is_free_listen : false
     * wrap_cover_url : https://imgopen.xmcdn.com/group63/M0A/90/A4/wKgMcl0UXx2QZSqCAAFGSchj8jg475.png
     * short_rich_intro : <p style="font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;color:#333333;word-break:break-all;font-weight:normal;" data-flag="normal">一日之计在于晨，新闻早餐不能少。<span><br /></span></p>
     * copyright_source : 2
     * quality_score : 8.7
     * is_activity : false
     * is_gradient_activity : false
     * homemade : 1
     * meta : 实时快讯,综合资讯
     * selling_point :
     * recommend_reason : 一日之计在于晨，新闻早餐不能少。
     */

    private Long id;
    private String kind;
    private int category_id;
    private String album_title;
    private String album_tags;
    private String album_intro;
    private String cover_url;
    private String cover_url_small;
    private String cover_url_middle;
    private String cover_url_large;
    private boolean tracks_natural_ordered;
    private AnnouncerBean announcer;
    private String  play_count;
    private int favorite_count;
    private int share_count;
    private int subscribe_count;
    private int include_track_count;
    private LastUptrackBean last_uptrack;
    private boolean can_download;
    private int is_finished;
    private long updated_at;
    private long created_at;
    private boolean is_paid;
    private int estimated_track_count;
    private String album_rich_intro;
    private String speaker_intro;
    private int free_track_count;
    private String free_track_ids;
    private String sale_intro;
    private String expected_revenue;
    private String buy_notes;
    private String speaker_title;
    private String speaker_content;
    private boolean has_sample;
    private int composed_price_type;
    private String detail_banner_url;
    private String album_score;
    private boolean is_vipfree;
    private boolean is_vip_exclusive;
    private boolean is_free_listen;
    private String wrap_cover_url;
    private String short_rich_intro;
    private int copyright_source;
    private String quality_score;
    private boolean is_activity;
    private boolean is_gradient_activity;
    private int homemade;
    private String meta;
    private String selling_point;
    private String recommend_reason;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getAlbum_tags() {
        return album_tags;
    }

    public void setAlbum_tags(String album_tags) {
        this.album_tags = album_tags;
    }

    public String getAlbum_intro() {
        return album_intro;
    }

    public void setAlbum_intro(String album_intro) {
        this.album_intro = album_intro;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getCover_url_small() {
        return cover_url_small;
    }

    public void setCover_url_small(String cover_url_small) {
        this.cover_url_small = cover_url_small;
    }

    public String getCover_url_middle() {
        return cover_url_middle;
    }

    public void setCover_url_middle(String cover_url_middle) {
        this.cover_url_middle = cover_url_middle;
    }

    public String getCover_url_large() {
        return cover_url_large;
    }

    public void setCover_url_large(String cover_url_large) {
        this.cover_url_large = cover_url_large;
    }

    public boolean isTracks_natural_ordered() {
        return tracks_natural_ordered;
    }

    public void setTracks_natural_ordered(boolean tracks_natural_ordered) {
        this.tracks_natural_ordered = tracks_natural_ordered;
    }

    public AnnouncerBean getAnnouncer() {
        return announcer;
    }

    public void setAnnouncer(AnnouncerBean announcer) {
        this.announcer = announcer;
    }

    public String  getPlay_count() {
        return play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public int getSubscribe_count() {
        return subscribe_count;
    }

    public void setSubscribe_count(int subscribe_count) {
        this.subscribe_count = subscribe_count;
    }

    public int getInclude_track_count() {
        return include_track_count;
    }

    public void setInclude_track_count(int include_track_count) {
        this.include_track_count = include_track_count;
    }

    public LastUptrackBean getLast_uptrack() {
        return last_uptrack;
    }

    public void setLast_uptrack(LastUptrackBean last_uptrack) {
        this.last_uptrack = last_uptrack;
    }

    public boolean isCan_download() {
        return can_download;
    }

    public void setCan_download(boolean can_download) {
        this.can_download = can_download;
    }

    public int getIs_finished() {
        return is_finished;
    }

    public void setIs_finished(int is_finished) {
        this.is_finished = is_finished;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public int getEstimated_track_count() {
        return estimated_track_count;
    }

    public void setEstimated_track_count(int estimated_track_count) {
        this.estimated_track_count = estimated_track_count;
    }

    public String getAlbum_rich_intro() {
        return album_rich_intro;
    }

    public void setAlbum_rich_intro(String album_rich_intro) {
        this.album_rich_intro = album_rich_intro;
    }

    public String getSpeaker_intro() {
        return speaker_intro;
    }

    public void setSpeaker_intro(String speaker_intro) {
        this.speaker_intro = speaker_intro;
    }

    public int getFree_track_count() {
        return free_track_count;
    }

    public void setFree_track_count(int free_track_count) {
        this.free_track_count = free_track_count;
    }

    public String getFree_track_ids() {
        return free_track_ids;
    }

    public void setFree_track_ids(String free_track_ids) {
        this.free_track_ids = free_track_ids;
    }

    public String getSale_intro() {
        return sale_intro;
    }

    public void setSale_intro(String sale_intro) {
        this.sale_intro = sale_intro;
    }

    public String getExpected_revenue() {
        return expected_revenue;
    }

    public void setExpected_revenue(String expected_revenue) {
        this.expected_revenue = expected_revenue;
    }

    public String getBuy_notes() {
        return buy_notes;
    }

    public void setBuy_notes(String buy_notes) {
        this.buy_notes = buy_notes;
    }

    public String getSpeaker_title() {
        return speaker_title;
    }

    public void setSpeaker_title(String speaker_title) {
        this.speaker_title = speaker_title;
    }

    public String getSpeaker_content() {
        return speaker_content;
    }

    public void setSpeaker_content(String speaker_content) {
        this.speaker_content = speaker_content;
    }

    public boolean isHas_sample() {
        return has_sample;
    }

    public void setHas_sample(boolean has_sample) {
        this.has_sample = has_sample;
    }

    public int getComposed_price_type() {
        return composed_price_type;
    }

    public void setComposed_price_type(int composed_price_type) {
        this.composed_price_type = composed_price_type;
    }

    public String getDetail_banner_url() {
        return detail_banner_url;
    }

    public void setDetail_banner_url(String detail_banner_url) {
        this.detail_banner_url = detail_banner_url;
    }

    public String getAlbum_score() {
        return album_score;
    }

    public void setAlbum_score(String album_score) {
        this.album_score = album_score;
    }

    public boolean isIs_vipfree() {
        return is_vipfree;
    }

    public void setIs_vipfree(boolean is_vipfree) {
        this.is_vipfree = is_vipfree;
    }

    public boolean isIs_vip_exclusive() {
        return is_vip_exclusive;
    }

    public void setIs_vip_exclusive(boolean is_vip_exclusive) {
        this.is_vip_exclusive = is_vip_exclusive;
    }

    public boolean isIs_free_listen() {
        return is_free_listen;
    }

    public void setIs_free_listen(boolean is_free_listen) {
        this.is_free_listen = is_free_listen;
    }

    public String getWrap_cover_url() {
        return wrap_cover_url;
    }

    public void setWrap_cover_url(String wrap_cover_url) {
        this.wrap_cover_url = wrap_cover_url;
    }

    public String getShort_rich_intro() {
        return short_rich_intro;
    }

    public void setShort_rich_intro(String short_rich_intro) {
        this.short_rich_intro = short_rich_intro;
    }

    public int getCopyright_source() {
        return copyright_source;
    }

    public void setCopyright_source(int copyright_source) {
        this.copyright_source = copyright_source;
    }

    public String getQuality_score() {
        return quality_score;
    }

    public void setQuality_score(String quality_score) {
        this.quality_score = quality_score;
    }

    public boolean isIs_activity() {
        return is_activity;
    }

    public void setIs_activity(boolean is_activity) {
        this.is_activity = is_activity;
    }

    public boolean isIs_gradient_activity() {
        return is_gradient_activity;
    }

    public void setIs_gradient_activity(boolean is_gradient_activity) {
        this.is_gradient_activity = is_gradient_activity;
    }

    public int getHomemade() {
        return homemade;
    }

    public void setHomemade(int homemade) {
        this.homemade = homemade;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getSelling_point() {
        return selling_point;
    }

    public void setSelling_point(String selling_point) {
        this.selling_point = selling_point;
    }

    public String getRecommend_reason() {
        return recommend_reason;
    }

    public void setRecommend_reason(String recommend_reason) {
        this.recommend_reason = recommend_reason;
    }



    public static class AnnouncerBean {
        /**
         * nameValuePairs : {"id":52622741,"kind":"user","nickname":"东方网","avatar_url":"https://imgopen.xmcdn.com/group20/M05/19/EA/wKgJJ1eDW1_QCA5MAABNXnIlT3I433.png!op_type=3&columns=110&rows=110","is_verified":true,"anchor_grade":15}
         */

        private NameValuePairsBean nameValuePairs = new NameValuePairsBean();

        public NameValuePairsBean getNameValuePairs() {
            return nameValuePairs;
        }

        public void setNameValuePairs(NameValuePairsBean nameValuePairs) {
            this.nameValuePairs = nameValuePairs;
        }

        public static class NameValuePairsBean {
            /**
             * id : 52622741
             * kind : user
             * nickname : 东方网
             * avatar_url : https://imgopen.xmcdn.com/group20/M05/19/EA/wKgJJ1eDW1_QCA5MAABNXnIlT3I433.png!op_type=3&columns=110&rows=110
             * is_verified : true
             * anchor_grade : 15
             */

            private int id;
            private String kind;
            private String nickname;
            private String avatar_url;
            private boolean is_verified;
            private int anchor_grade;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public boolean isIs_verified() {
                return is_verified;
            }

            public void setIs_verified(boolean is_verified) {
                this.is_verified = is_verified;
            }

            public int getAnchor_grade() {
                return anchor_grade;
            }

            public void setAnchor_grade(int anchor_grade) {
                this.anchor_grade = anchor_grade;
            }
        }
    }

    public static class LastUptrackBean {
        /**
         * nameValuePairs : {"track_id":406706967,"track_title":"先后发现4名死者！锡林浩特公布一重大刑事案侦破细节 | 新闻早餐 2021.4.18 星期日","duration":416,"created_at":1618698000000,"updated_at":1618698000000}
         */

        private NameValuePairsBeanX nameValuePairs;

        public NameValuePairsBeanX getNameValuePairs() {
            return nameValuePairs;
        }

        public void setNameValuePairs(NameValuePairsBeanX nameValuePairs) {
            this.nameValuePairs = nameValuePairs;
        }

        public static class NameValuePairsBeanX {
            /**
             * track_id : 406706967
             * track_title : 先后发现4名死者！锡林浩特公布一重大刑事案侦破细节 | 新闻早餐 2021.4.18 星期日
             * duration : 416
             * created_at : 1618698000000
             * updated_at : 1618698000000
             */

            private int track_id;
            private String track_title;
            private int duration;
            private long created_at;
            private long updated_at;

            public int getTrack_id() {
                return track_id;
            }

            public void setTrack_id(int track_id) {
                this.track_id = track_id;
            }

            public String getTrack_title() {
                return track_title;
            }

            public void setTrack_title(String track_title) {
                this.track_title = track_title;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public long getCreated_at() {
                return created_at;
            }

            public void setCreated_at(long created_at) {
                this.created_at = created_at;
            }

            public long getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(long updated_at) {
                this.updated_at = updated_at;
            }
        }
    }
}
