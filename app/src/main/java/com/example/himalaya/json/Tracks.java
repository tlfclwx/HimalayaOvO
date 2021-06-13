package com.example.himalaya.json;

import android.os.Parcel;
import android.os.Parcelable;

public class Tracks implements Parcelable {

        /**
         * id : 408715661
         * kind : track
         * category_id : 1
         * track_title : 涉嫌藏大量爆炸品，“港独”组织成员认罪被判刑12年 | 新闻早餐 2021.4.24 星期六
         * track_tags :
         * track_intro :
         * cover_url_small : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=3&columns=100&rows=100
         * cover_url_middle : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=3&columns=180&rows=180
         * cover_url_large : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=3&columns=640&rows=640
         * announcer : {"id":52622741,"kind":"user","nickname":"东方网","avatar_url":"https://imgopen.xmcdn.com/group20/M05/19/EA/wKgJJ1eDW1_QCA5MAABNXnIlT3I433.png!op_type=3&columns=110&rows=110","is_verified":true}
         * duration : 499
         * play_count : 100922
         * favorite_count : 159
         * comment_count : 75
         * download_count : 0
         * play_url_32 :
         * play_size_32 : 1996844
         * play_url_64 :
         * play_size_64 : 3993435
         * play_url_64_m4a :
         * play_size_64_m4a : 4042319
         * play_url_24_m4a :
         * play_size_24_m4a : 1545942
         * play_url_amr :
         * play_size_amr : 449232
         * contain_video : false
         * can_download : true
         * download_url : https://aod.cos.tx.xmcdn.com/storages/9330-audiofreehighqps/38/20/CKwRIUEEWLu_ABeW1gCiQq0V.m4a
         * download_size : 1545942
         * subordinated_album : {"id":4519297,"album_title":"新闻早餐（听更多新闻，上喜马拉雅APP）","cover_url_small":"https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=5&upload_type=album&device_type=ios&name=mobile_small&magick=png","cover_url_middle":"https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=5&upload_type=album&device_type=ios&name=medium&magick=png","cover_url_large":"https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=5&upload_type=album&device_type=ios&name=mobile_large&magick=png"}
         * source : 1
         * vip_first_status : 0
         * updated_at : 1619216400000
         * created_at : 1619216400000
         * order_num : 0
         */

        private int id;
        private String kind;
        private int category_id;
        private String track_title;
        private String track_tags;
        private String track_intro;
        private String cover_url_small;
        private String cover_url_middle;
        private String cover_url_large;
        private AnnouncerBean announcer;
        private int duration;
        private int play_count;
        private int favorite_count;
        private int comment_count;
        private int download_count;
        private String play_url_32;
        private int play_size_32;
        private String play_url_64;
        private int play_size_64;
        private String play_url_64_m4a;
        private int play_size_64_m4a;
        private String play_url_24_m4a;
        private int play_size_24_m4a;
        private String play_url_amr;
        private int play_size_amr;
        private boolean contain_video;
        private boolean can_download;
        private String download_url;
        private int download_size;
        private SubordinatedAlbumBean subordinated_album;
        private int source;
        private int vip_first_status;
        private long updated_at;
        private long created_at;
        private int order_num;

    public Tracks(Parcel in) {
        id = in.readInt();
        kind = in.readString();
        category_id = in.readInt();
        track_title = in.readString();
        track_tags = in.readString();
        track_intro = in.readString();
        cover_url_small = in.readString();
        cover_url_middle = in.readString();
        cover_url_large = in.readString();
        duration = in.readInt();
        play_count = in.readInt();
        favorite_count = in.readInt();
        comment_count = in.readInt();
        download_count = in.readInt();
        play_url_32 = in.readString();
        play_size_32 = in.readInt();
        play_url_64 = in.readString();
        play_size_64 = in.readInt();
        play_url_64_m4a = in.readString();
        play_size_64_m4a = in.readInt();
        play_url_24_m4a = in.readString();
        play_size_24_m4a = in.readInt();
        play_url_amr = in.readString();
        play_size_amr = in.readInt();
        contain_video = in.readByte() != 0;
        can_download = in.readByte() != 0;
        download_url = in.readString();
        download_size = in.readInt();
        source = in.readInt();
        vip_first_status = in.readInt();
        updated_at = in.readLong();
        created_at = in.readLong();
        order_num = in.readInt();
    }
    public Tracks(){}
    public static final Creator<Tracks> CREATOR = new Creator<Tracks>() {
        @Override
        public Tracks createFromParcel(Parcel in) {
            return new Tracks(in);
        }

        @Override
        public Tracks[] newArray(int size) {
            return new Tracks[size];
        }
    };

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

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public String getTrack_title() {
            return track_title;
        }

        public void setTrack_title(String track_title) {
            this.track_title = track_title;
        }

        public String getTrack_tags() {
            return track_tags;
        }

        public void setTrack_tags(String track_tags) {
            this.track_tags = track_tags;
        }

        public String getTrack_intro() {
            return track_intro;
        }

        public void setTrack_intro(String track_intro) {
            this.track_intro = track_intro;
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

        public AnnouncerBean getAnnouncer() {
            return announcer;
        }

        public void setAnnouncer(AnnouncerBean announcer) {
            this.announcer = announcer;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getPlay_count() {
            return play_count;
        }

        public void setPlay_count(int play_count) {
            this.play_count = play_count;
        }

        public int getFavorite_count() {
            return favorite_count;
        }

        public void setFavorite_count(int favorite_count) {
            this.favorite_count = favorite_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getDownload_count() {
            return download_count;
        }

        public void setDownload_count(int download_count) {
            this.download_count = download_count;
        }

        public String getPlay_url_32() {
            return play_url_32;
        }

        public void setPlay_url_32(String play_url_32) {
            this.play_url_32 = play_url_32;
        }

        public int getPlay_size_32() {
            return play_size_32;
        }

        public void setPlay_size_32(int play_size_32) {
            this.play_size_32 = play_size_32;
        }

        public String getPlay_url_64() {
            return play_url_64;
        }

        public void setPlay_url_64(String play_url_64) {
            this.play_url_64 = play_url_64;
        }

        public int getPlay_size_64() {
            return play_size_64;
        }

        public void setPlay_size_64(int play_size_64) {
            this.play_size_64 = play_size_64;
        }

        public String getPlay_url_64_m4a() {
            return play_url_64_m4a;
        }

        public void setPlay_url_64_m4a(String play_url_64_m4a) {
            this.play_url_64_m4a = play_url_64_m4a;
        }

        public int getPlay_size_64_m4a() {
            return play_size_64_m4a;
        }

        public void setPlay_size_64_m4a(int play_size_64_m4a) {
            this.play_size_64_m4a = play_size_64_m4a;
        }

        public String getPlay_url_24_m4a() {
            return play_url_24_m4a;
        }

        public void setPlay_url_24_m4a(String play_url_24_m4a) {
            this.play_url_24_m4a = play_url_24_m4a;
        }

        public int getPlay_size_24_m4a() {
            return play_size_24_m4a;
        }

        public void setPlay_size_24_m4a(int play_size_24_m4a) {
            this.play_size_24_m4a = play_size_24_m4a;
        }

        public String getPlay_url_amr() {
            return play_url_amr;
        }

        public void setPlay_url_amr(String play_url_amr) {
            this.play_url_amr = play_url_amr;
        }

        public int getPlay_size_amr() {
            return play_size_amr;
        }

        public void setPlay_size_amr(int play_size_amr) {
            this.play_size_amr = play_size_amr;
        }

        public boolean isContain_video() {
            return contain_video;
        }

        public void setContain_video(boolean contain_video) {
            this.contain_video = contain_video;
        }

        public boolean isCan_download() {
            return can_download;
        }

        public void setCan_download(boolean can_download) {
            this.can_download = can_download;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public int getDownload_size() {
            return download_size;
        }

        public void setDownload_size(int download_size) {
            this.download_size = download_size;
        }

        public SubordinatedAlbumBean getSubordinated_album() {
            return subordinated_album;
        }

        public void setSubordinated_album(SubordinatedAlbumBean subordinated_album) {
            this.subordinated_album = subordinated_album;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getVip_first_status() {
            return vip_first_status;
        }

        public void setVip_first_status(int vip_first_status) {
            this.vip_first_status = vip_first_status;
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

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(kind);
        dest.writeInt(category_id);
        dest.writeString(track_title);
        dest.writeString(track_tags);
        dest.writeString(track_intro);
        dest.writeString(cover_url_small);
        dest.writeString(cover_url_middle);
        dest.writeString(cover_url_large);
        dest.writeInt(duration);
        dest.writeInt(play_count);
        dest.writeInt(favorite_count);
        dest.writeInt(comment_count);
        dest.writeInt(download_count);
        dest.writeString(play_url_32);
        dest.writeInt(play_size_32);
        dest.writeString(play_url_64);
        dest.writeInt(play_size_64);
        dest.writeString(play_url_64_m4a);
        dest.writeInt(play_size_64_m4a);
        dest.writeString(play_url_24_m4a);
        dest.writeInt(play_size_24_m4a);
        dest.writeString(play_url_amr);
        dest.writeInt(play_size_amr);
        dest.writeByte((byte) (contain_video ? 1 : 0));
        dest.writeByte((byte) (can_download ? 1 : 0));
        dest.writeString(download_url);
        dest.writeInt(download_size);
        dest.writeInt(source);
        dest.writeInt(vip_first_status);
        dest.writeLong(updated_at);
        dest.writeLong(created_at);
        dest.writeInt(order_num);
    }

    public static class AnnouncerBean {
            /**
             * id : 52622741
             * kind : user
             * nickname : 东方网
             * avatar_url : https://imgopen.xmcdn.com/group20/M05/19/EA/wKgJJ1eDW1_QCA5MAABNXnIlT3I433.png!op_type=3&columns=110&rows=110
             * is_verified : true
             */

            private int id;
            private String kind;
            private String nickname;
            private String avatar_url;
            private boolean is_verified;

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
        }

        public static class SubordinatedAlbumBean {
            /**
             * id : 4519297
             * album_title : 新闻早餐（听更多新闻，上喜马拉雅APP）
             * cover_url_small : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=5&upload_type=album&device_type=ios&name=mobile_small&magick=png
             * cover_url_middle : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=5&upload_type=album&device_type=ios&name=medium&magick=png
             * cover_url_large : https://imgopen.xmcdn.com/group18/M01/19/DE/wKgJKleDXj2QJYOuAAFbNixU8BI237.png!op_type=5&upload_type=album&device_type=ios&name=mobile_large&magick=png
             */

            private int id;
            private String album_title;
            private String cover_url_small;
            private String cover_url_middle;
            private String cover_url_large;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAlbum_title() {
                return album_title;
            }

            public void setAlbum_title(String album_title) {
                this.album_title = album_title;
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
    }
}
