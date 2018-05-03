package com.likeits.sanyou.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/15.
 */

public class HomeAdInfoEntity implements Serializable{

    /**
     * path : http://semyooapp.wbteam.cn//Uploads/Picture/2017-12-14/5a3235c8cdc63.png
     * goodsid : 14
     * goods_name : 商品名称
     * goods_introduct : 商品广告语
     * money_need : 680
     * money_raw : 800
     *  "money_vip": "600"
     */

    private String path;
    private String goodsid;
    private String goods_name;
    private String goods_introduct;
    private String money_need;
    private String money_raw;
    private String money_vip;

    public String getMoney_vip() {
        return money_vip;
    }

    public void setMoney_vip(String money_vip) {
        this.money_vip = money_vip;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_introduct() {
        return goods_introduct;
    }

    public void setGoods_introduct(String goods_introduct) {
        this.goods_introduct = goods_introduct;
    }

    public String getMoney_need() {
        return money_need;
    }

    public void setMoney_need(String money_need) {
        this.money_need = money_need;
    }

    public String getMoney_raw() {
        return money_raw;
    }

    public void setMoney_raw(String money_raw) {
        this.money_raw = money_raw;
    }
}
