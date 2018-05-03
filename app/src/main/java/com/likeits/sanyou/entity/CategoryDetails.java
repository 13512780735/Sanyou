package com.likeits.sanyou.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/5.
 */

public class CategoryDetails implements Serializable{

    /**
     * id : 14
     * goods_name : 商品名称
     * path : http://semyooapp.wbteam.cn//Uploads/Picture/2017-11-29/5a1e2236c917b.jpg
     * goods_detail : <p><span style="color: rgb(52, 73, 94); font-family: &quot;Segoe UI&quot;, &quot;Lucida Grande&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, FreeSans, Arimo, &quot;Droid Sans&quot;, &quot;wenquanyi micro hei&quot;, &quot;Hiragino Sans GB&quot;, &quot;Hiragino Sans GB W3&quot;, FontAwesome, sans-serif; font-weight: bold; background-color: rgb(255, 255, 255);">商品详情</span></p>
     * money_need : 10
     * sell_num : 1
     * status : 1
     *  "money_vip": "400"
     */

    private String id;
    private String goods_name;
    private String path;
    private String goods_detail;
    private String money_need;
    private String sell_num;
    private String status;
    private String  money_vip;

    public String getMoney_vip() {
        return money_vip;
    }

    public void setMoney_vip(String money_vip) {
        this.money_vip = money_vip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(String goods_detail) {
        this.goods_detail = goods_detail;
    }

    public String getMoney_need() {
        return money_need;
    }

    public void setMoney_need(String money_need) {
        this.money_need = money_need;
    }

    public String getSell_num() {
        return sell_num;
    }

    public void setSell_num(String sell_num) {
        this.sell_num = sell_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
