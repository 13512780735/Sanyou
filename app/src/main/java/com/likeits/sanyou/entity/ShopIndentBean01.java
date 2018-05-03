package com.likeits.sanyou.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ShopIndentBean01 implements Serializable{
    String id;
    String goods_name;
    String path;
    String money_need;
    String money_vip;
    int good_num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney_vip() {
        return money_vip;
    }

    public void setMoney_vip(String money_vip) {
        this.money_vip = money_vip;
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

    public String getMoney_need() {
        return money_need;
    }

    public void setMoney_need(String money_need) {
        this.money_need = money_need;
    }

    public int getGood_num() {
        return good_num;
    }

    public void setGood_num(int good_num) {
        this.good_num = good_num;
    }
}
