package com.likeits.sanyou.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ShopHomeInfoEntity implements Serializable {


    /**
     * id : 14
     * goods_name : 商品名称
     * goods_cat : ["商品标签","商品标签2"]
     * path : http://semyooapp.wbteam.cn//Uploads/Picture/2017-12-04/5a2531b9b634e.jpg
     * money_need : 680
     * money_raw : 800
     * money_vip : 600
     * sell_num : 1
     */

    private String id;
    private String goods_name;
    private String path;
    private String money_need;
    private String money_raw;
    private String money_vip;
    private String sell_num;
    private List<String> goods_cat;

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

    public String getMoney_vip() {
        return money_vip;
    }

    public void setMoney_vip(String money_vip) {
        this.money_vip = money_vip;
    }

    public String getSell_num() {
        return sell_num;
    }

    public void setSell_num(String sell_num) {
        this.sell_num = sell_num;
    }

    public List<String> getGoods_cat() {
        return goods_cat;
    }

    public void setGoods_cat(List<String> goods_cat) {
        this.goods_cat = goods_cat;
    }
}
