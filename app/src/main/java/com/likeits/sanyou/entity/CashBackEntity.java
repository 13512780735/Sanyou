package com.likeits.sanyou.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/29.
 */

public class CashBackEntity implements Serializable{

    /**
     * goods_id : 15
     * goods_name : 商品名称1
     * sn : AC30005772626073
     * money : 30
     * time : 2017-12-21
     */

    private String goods_id;
    private String goods_name;
    private String sn;
    private String money;
    private String time;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
