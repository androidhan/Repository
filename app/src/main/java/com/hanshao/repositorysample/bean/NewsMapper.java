package com.hanshao.repositorysample.bean;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/12.
 * ACTION:经Mapper数据
 */

public class NewsMapper {


    /**
     * ctime : 2017-06-07 10:12
     * title : 舍不得媳妇套不住流氓！警察带女友约会“钓色魔”
     * description : 搜狐社会
     * picUrl :
     * url : http://news.sohu.com/20170607/n495984947.shtml
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

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "ctime='" + ctime + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
