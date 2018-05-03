package com.likeits.sanyou.ui.shop.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ShoppingCartBean {

    /**
     * id : 68
     * goods_id : 15
     * goods_num : 3
     * goods_name : 商品名称1
     * goods_cat : ["商品标签","商品标签2"]
     * goods_introduct : 商品广告语
     * money_need : 680
     * money_raw : 800
     * money_vip : 600
     * path : http://semyooapp.wbteam.cn//Uploads/Picture/2017-12-04/5a2531b9b634e.jpg
     */

    private String id;
    private String goods_id;
    private String goods_num;
    private String goods_name;
    private String goods_introduct;
    private String money_need;
    private String money_raw;
    private String path;
    private List<String> goods_cat;
    public boolean isChoosed;
    public boolean isCheck = false;
    /**
     * money_vip : 600
     */

    private String money_vip;

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getGoods_cat() {
        return goods_cat;
    }

    public void setGoods_cat(List<String> goods_cat) {
        this.goods_cat = goods_cat;
    }

    public String getMoney_vip() {
        return money_vip;
    }

    public void setMoney_vip(String money_vip) {
        this.money_vip = money_vip;
    }
}
