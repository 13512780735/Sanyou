package com.likeits.sanyou.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/15.
 */

public class HomeNewInfoEntity implements Serializable{

    /**
     * id : 1
     * author : 三友钓鱼官网
     * uid : 1
     * headimg : http://semyooapp.wbteam.cn/./Uploads/Avatar/1/123.jpg
     * easemob_id : 1
     * category_name : 今日资讯
     * title : 标题
     * cover : http://semyooapp.wbteam.cn//Uploads/Picture/2017-11-29/5a1e2236c917b.jpg
     * cover_width : 256
     * cover_height : 256
     * description : 内容内容内容内容内容内容内容内容内容内容内容内容内容内容...
     * interval : 1小时前
     * view : 4
     * comment : 0
     * content : <p>内容内容内容内容内容内容内容内容内容内容内容内容内容内容</p>
     * likes : 0
     * status : 0
     */

    private String id;
    private String author;
    private String uid;
    private String headimg;
    private String easemob_id;
    private String category_name;
    private String title;
    private String cover;
    private String cover_width;
    private String cover_height;
    private String description;
    private String interval;
    private String view;
    private String comment;
    private String content;
    private String likes;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover_width() {
        return cover_width;
    }

    public void setCover_width(String cover_width) {
        this.cover_width = cover_width;
    }

    public String getCover_height() {
        return cover_height;
    }

    public void setCover_height(String cover_height) {
        this.cover_height = cover_height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
