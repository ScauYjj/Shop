package com.chinamobile.shop.bean;

import java.io.Serializable;

/**
 * Created by yjj on 2017/1/17.
 */

public class Campaign implements Serializable{
    private Long id;
    private String title;
    private String imgUrl;
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imgurl='" + imgUrl + '\'' +
                '}';
    }
}
