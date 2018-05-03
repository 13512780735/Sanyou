package com.likeits.sanyou.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/6.
 */

public class CollectInfoEntity implements Serializable{

    /**
     * id : 14
     * goods_name : 商品名称
     * path : http://semyooapp.wbteam.cn//Uploads/Picture/2017-11-29/5a1e2236c917b.jpg
     * money_need : 10
     */

    private String id;
    private String goods_name;
    private String path;
    private String money_need;

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
}
