package com.hanshao.repositorysample.bean;

import java.util.List;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/13.
 * ACTION:未经Mapper数据
 */

public class News {


    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2017-06-13 10:27","title":"习近平重新定义中国制造","description":"搜狐国内","picUrl":"http://photocdn.sohu.com/20170612/Img496758973_ss.jpeg","url":"http://news.sohu.com/20170613/n496820713.shtml"}]
     */

    private int code;
    private String msg;
    private List<NewslistEntity> newslist;

    @Override
    public String toString() {
        return "News{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", newslist=" + newslist +
                '}';
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setNewslist(List<NewslistEntity> newslist) {
        this.newslist = newslist;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<NewslistEntity> getNewslist() {
        return newslist;
    }

    public static class NewslistEntity {
        /**
         * ctime : 2017-06-13 10:27
         * title : 习近平重新定义中国制造
         * description : 搜狐国内
         * picUrl : http://photocdn.sohu.com/20170612/Img496758973_ss.jpeg
         * url : http://news.sohu.com/20170613/n496820713.shtml
         */

        private String ctime;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCtime() {
            return ctime;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        @Override
        public String toString() {
            return "NewslistEntity{" +
                    "ctime='" + ctime + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", picUrl='" + picUrl + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public String getUrl() {
            return url;
        }


    }
}
