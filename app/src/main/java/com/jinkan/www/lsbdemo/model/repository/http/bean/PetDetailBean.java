package com.jinkan.www.lsbdemo.model.repository.http.bean;

/**
 * Created by Sampson on 2020-03-17.
 * LiveApp
 */
public class PetDetailBean {

    /**
     * id : 1
     * zip : https://cdn.dreamcapsule.top/%E6%81%B6%E9%BE%99.zip
     * name : 恶龙1
     * image : https://cdn.dreamcapsule.top/1560320751496870.jpg
     * useCount : 12200
     * likeCount : 23330
     */

    private int id;
    private String zip;
    private String name;
    private String image;
    private int useCount;
    private int likeCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
