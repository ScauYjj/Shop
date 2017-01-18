package com.chinamobile.shop.bean;

import java.io.Serializable;

/**
 * Created by yjj on 2016/11/23.
 */

public class Banner extends BaseBean implements Serializable {
    private String name;
    private String imgUrl;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imageUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imgUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
