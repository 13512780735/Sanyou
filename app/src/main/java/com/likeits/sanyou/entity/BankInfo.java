package com.likeits.sanyou.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/30.
 */

public class BankInfo implements Serializable{

    /**
     * id : 1
     * bank : 建设银行
     */

    private String id;
    private String bank;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
