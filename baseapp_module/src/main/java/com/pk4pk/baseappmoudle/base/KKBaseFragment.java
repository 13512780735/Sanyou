//package com.pk4pk.baseappmoudle.base;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.annotation.StringRes;
//import android.app.Fragment;
//
//import com.pk4pk.baseappmoudle.dialog.LoaddingDialog;
//import com.pk4pk.baseappmoudle.utils.KKToast;
//
//import org.greenrobot.eventbus.EventBus;
//
///**
// * Created by wenfengcai on 2017/2/22.
// *
// * @Autor CaiWF
// * @TODO
// */
//
//public abstract class KKBaseFragment extends Fragment {
//
//    public LoaddingDialog loaddingDialog;
//    public Context context;
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(haveEventBus()){
//            EventBus.getDefault().register(this);
//        }
//        context=getActivity();
//        loaddingDialog=new LoaddingDialog(context);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(haveEventBus()){
//            EventBus.getDefault().unregister(this);
//        }
//    }
//
//    public void LoaddingDismiss(){
//        if(loaddingDialog!=null && loaddingDialog.isShowing()){
//            loaddingDialog.dismiss();
//        }
//    }
//
//    protected void toFinish() {
//        getActivity().finish();
//    }
//
//
//    public void toActivityFinish(Class activity) {
//        Intent intent = new Intent(context, activity);
//        startActivity(intent);
//        toFinish();
//    }
//
//    public void toActivity(Class activity) {
//        Intent intent = new Intent(context, activity);
//        startActivity(intent);
//    }
//
//    public void toActivity(Class activity, Bundle bundle) {
//        Intent intent = new Intent(context, activity);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
//
//
//    public abstract boolean haveEventBus();
//
//    protected void toastShow(String msg){
//        KKToast.show(getActivity(),msg);
//    }
//
//    protected void toastShow(@StringRes int msg){
//        KKToast.show(getActivity(),msg);
//    }
//}
