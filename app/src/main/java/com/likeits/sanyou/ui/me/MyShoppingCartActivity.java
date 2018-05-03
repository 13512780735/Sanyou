package com.likeits.sanyou.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.ShopIndentBean01;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.ui.shop.IndentDetailsActivity;
import com.likeits.sanyou.ui.shop.adapter.ShoppingCartAdapter;
import com.likeits.sanyou.ui.shop.bean.ShoppingCartBean;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.UtilPreference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyShoppingCartActivity extends BaseActivity implements View.OnClickListener, ShoppingCartAdapter.CheckInterface, ShoppingCartAdapter.ModifyCountInterface {
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.ck_all)
    CheckBox ckAll;
    @BindView(R.id.tv_show_price)
    TextView tvShowPrice;
    @BindView(R.id.tv_settlement)
    TextView tvSettlement;
    @BindView(R.id.bt_header_right)
    TextView btnEdit;
    @BindView(R.id.list_shopping_cart)
    ListView list_shopping_cart;


    private ShoppingCartAdapter shoppingCartAdapter;
    private boolean flag = false;
    private List<ShoppingCartBean> shoppingCartBeanList = new ArrayList<>();
    private boolean mSelect;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private String goodId;
    private String goodNum;
    private List<ShopIndentBean01> indentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping_cart);
        ButterKnife.bind(this);
        indentData = new ArrayList<>();
        initView();
    }

    private void initView() {
        btnEdit.setOnClickListener(this);
        ckAll.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        initData();
    }

    //初始化数据
    protected void initData() {
        HttpMethods.getInstance().GetShopCart(new MySubscriber<ArrayList<ShoppingCartBean>>(this) {
            @Override
            public void onHttpCompleted(HttpResult<ArrayList<ShoppingCartBean>> httpResult) {
                if (httpResult.getCode() == 1) {
                    shoppingCartBeanList = httpResult.getData();
                    shoppingCartAdapter = new ShoppingCartAdapter(MyShoppingCartActivity.this);
                    shoppingCartAdapter.setCheckInterface(MyShoppingCartActivity.this);
                    shoppingCartAdapter.setModifyCountInterface(MyShoppingCartActivity.this);
                    list_shopping_cart.setAdapter(shoppingCartAdapter);
                    shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);

                } else {
                    btnEdit.setEnabled(false);
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey);

    }

    String goodIdstr = "";
    String goodNumstr = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选按钮
            case R.id.ck_all:
                if (shoppingCartBeanList.size() != 0) {
                    if (ckAll.isChecked()) {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(true);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(false);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.bt_header_right:
                flag = !flag;
                if (flag) {

                    shoppingCartAdapter.notifyDataSetChanged();
                    btnEdit.setText("完成");
                    shoppingCartAdapter.isShow(false);
                } else {
                    goodIdstr = "";
                    goodNumstr = "";
                    for (ShoppingCartBean bean : shoppingCartBeanList) {
                        boolean choosed = bean.isChoosed();
                        if (choosed) {
                            goodIdstr += bean.getGoods_id() + ",";
                            goodNumstr += bean.getGoods_num() + ",";
                        }
                    }
                    if (StringUtil.isBlank(goodIdstr) || StringUtil.isBlank(goodNumstr)) {
                        showToast("未选中商品");
                        return;
                    } else {
                    String goodIdstrs = goodIdstr.substring(0, goodIdstr.length() - 1);
                    String goodNumstrs = goodNumstr.substring(0, goodNumstr.length() - 1);
                    editShop(goodIdstrs, goodNumstrs);}
                    btnEdit.setText("编辑");
                    shoppingCartAdapter.isShow(true);
                }

                break;
            case R.id.tv_settlement: //结算
                lementOnder();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void editShop(String goodIdstr, String goodNumstr) {
        Log.d("TAG", "goodIdstr-->" + goodIdstr + "goodNumstr-->" + goodNumstr);
        HttpMethods.getInstance().EditBuy(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                if (httpResult.getCode() == 1) {
                    showToast("编辑成功！");
                } else {
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, goodIdstr, goodNumstr, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "0909");
        totalPrice=0.0;
        totalCount=0;
        tvShowPrice.setText("" + totalPrice);
        tvSettlement.setText("结算(" + totalCount + ")");
        initView();
        if (indentData.size() != 0) {
            indentData.clear();
        } else return;
    }

    /**
     * 结算订单、支付
     */
    private void lementOnder() {
        //选中的需要提交的商品清单
        for (ShoppingCartBean bean : shoppingCartBeanList) {
            boolean choosed = bean.isChoosed();
            if (choosed) {
                ShopIndentBean01 shopIndentBean01 = new ShopIndentBean01();
                shopIndentBean01.setId(bean.getId());
                shopIndentBean01.setGood_num(Integer.valueOf(bean.getGoods_num()));
                shopIndentBean01.setGoods_name(bean.getGoods_name());
                shopIndentBean01.setMoney_need(bean.getMoney_need());
                shopIndentBean01.setMoney_vip(bean.getMoney_vip());
                shopIndentBean01.setPath(bean.getPath());
                String shoppingName = bean.getGoods_name();
                int count = Integer.valueOf(bean.getGoods_num());
                double price = Double.valueOf(bean.getMoney_need());
                // int size = bean.getDressSize();
                String attribute = bean.getGoods_introduct();
                int id = Integer.valueOf(bean.getId());
                Log.d("TAG", id + "----id---" + shoppingName + "---" + count + "---" + price + "--size----" + "--attr---" + attribute);
                indentData.add(shopIndentBean01);
                goodIdstr += bean.getGoods_id() + ",";
                goodNumstr += bean.getGoods_num() + ",";
            }
        }
        if (StringUtil.isBlank(goodIdstr) || StringUtil.isBlank(goodNumstr)||"0.0".equals(totalPrice)) {
            showToast("未选择结算商品");
            return;
        } else {
            String goodIdstrs = goodIdstr.substring(0, goodIdstr.length() - 1);
            String goodNumstrs = goodNumstr.substring(0, goodNumstr.length() - 1);
            Intent intent = new Intent(mContext, IndentDetailsActivity.class);
            intent.putExtra("indentData", (Serializable) indentData);
            intent.putExtra("total", String.valueOf(totalPrice));
            intent.putExtra("goodId", goodIdstrs);
            intent.putExtra("goods_num", goodNumstrs);
            intent.putExtra("cart_id", goodIdstrs);
            startActivity(intent);
        }
        // ToastUtil.showL(this, "总价：" + totalPrice);

        //跳转到支付界面
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
            if (shoppingCartBean.isChoosed()) {
                totalCount++;
                String role= UtilPreference.getStringValue(mContext,"role");
                if ("1".equals(role)) {
                    totalPrice += Double.valueOf(shoppingCartBean.getMoney_need()) * Integer.valueOf(shoppingCartBean.getGoods_num());
                }else{
                    totalPrice += Double.valueOf(shoppingCartBean.getMoney_vip()) * Integer.valueOf(shoppingCartBean.getGoods_num());
                }

            }
        }
        //   statistics();
        tvShowPrice.setText("" + totalPrice);
        tvSettlement.setText("结算(" + totalCount + ")");
    }

    @Override
    public void checkGroup(int position, boolean isChecked) {
        shoppingCartBeanList.get(position).setChoosed(isChecked);
        if (isAllCheck())
            ckAll.setChecked(true);
        else
            ckAll.setChecked(false);
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck() {

        for (ShoppingCartBean group : shoppingCartBeanList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 增加
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = Integer.valueOf(shoppingCartBean.getGoods_num());
        currentCount++;
        shoppingCartBean.setGoods_num(String.valueOf(currentCount));
        ((TextView) showCountView).setText(currentCount + "");
        goodIdstr += shoppingCartBeanList.get(position).getGoods_id() + ",";
        goodNumstr += shoppingCartBeanList.get(position).getGoods_num() + ",";
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 删减
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = Integer.valueOf(shoppingCartBean.getGoods_num());
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        shoppingCartBean.setGoods_num(String.valueOf(currentCount));
        ((TextView) showCountView).setText(currentCount + "");
        goodIdstr += shoppingCartBeanList.get(position).getGoods_id() + ",";
        goodNumstr += shoppingCartBeanList.get(position).getGoods_num() + ",";
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 删除
     *
     * @param position
     */

    @Override
    public void childDelete(int position) {
        String pid = shoppingCartBeanList.get(position).getGoods_id();
        shoppingCartBeanList.remove(position);
        shoppingCartAdapter.notifyDataSetChanged();
        delete(pid);
        statistics();
    }

    private void delete(String pid) {
        HttpMethods.getInstance().EditBuy(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                if (httpResult.getCode() == 1) {
                    showToast("删除成功！");
                } else {
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, "", "", pid);
    }
}
