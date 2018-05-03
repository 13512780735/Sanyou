package com.likeits.sanyou.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class ShopIndentEntity implements Serializable {


    /**
     * id : 70
     * status : 0
     * address_id : 1
     * shop : [{"goods_num":"3","goods_id":"14","goods_name":"商品名称","goods_cat":["商品标签","商品标签2"],"goods_introduct":"商品广告语","money_need":"680","money_raw":"800","path":"http://semyooapp.wbteam.cn//Uploads/Picture/2017-12-04/5a2531b9b634e.jpg"},{"goods_num":"2","goods_id":"15","goods_name":"商品名称1","goods_cat":["商品标签","商品标签2"],"goods_introduct":"商品广告语","money_need":"680","money_raw":"800","path":"http://semyooapp.wbteam.cn//Uploads/Picture/2017-12-04/5a2531b9b634e.jpg"}]
     * "courier": "2313",
     * "courier_number": "321312",
     */


    private String id;
    private String status;
    private String address_id;
    private String number;
    private String total;
    private String sn;
    private String courier;
    private String courier_number;

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getCourier_number() {
        return courier_number;
    }

    public void setCourier_number(String courier_number) {
        this.courier_number = courier_number;
    }

    private List<ShopBean> shop;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public List<ShopBean> getShop() {
        return shop;
    }

    public void setShop(List<ShopBean> shop) {
        this.shop = shop;
    }

    public static class ShopBean {
        /**
         * goods_num : 3
         * goods_id : 14
         * goods_name : 商品名称
         * goods_cat : ["商品标签","商品标签2"]
         * goods_introduct : 商品广告语
         * money_need : 680
         * money_raw : 800
         * path : http://semyooapp.wbteam.cn//Uploads/Picture/2017-12-04/5a2531b9b634e.jpg
         */

        private String goods_num;
        private String goods_id;
        private String goods_name;
        private String goods_introduct;
        private String money_need;
        private String money_raw;
        private String path;
        private List<String> goods_cat;

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

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
    }
}
