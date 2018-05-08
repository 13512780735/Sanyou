package com.likeits.sanyou.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.data.ScanResult;
import com.likeits.sanyou.R;
import com.likeits.sanyou.bluetooth.BluetoothService;
import com.likeits.sanyou.entity.DeviceBean;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.base.BaseFragment;
import com.likeits.sanyou.ui.device.Device01Activity;
import com.likeits.sanyou.ui.device.Device04Activity;
import com.likeits.sanyou.ui.device.Device09Activity;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.CustomDialog;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment02 extends BaseFragment implements View.OnClickListener {

    private TextView mHeader;
    private Button mBtnback;
    //,mBtnDevic,mBtnLight;
    private Button btn_start, btn_stop;
    private ImageView img_loading;
    private ProgressDialog progressDialog;
    private Animation operatingAnim;
    private ResultAdapter mResultAdapter;

    private BluetoothService mBluetoothService;
    private String name;
    private String getAddress;
    private LoaddingDialog loading;
    private List<DeviceBean> deviceData;
    private String name1;
    private String name01;
    private String name3;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_fragment02;
    }

    @Override
    protected void lazyLoad() {
        name1 = UtilPreference.getStringValue(getActivity(), "BlueName");
        loading = new LoaddingDialog(getContext());
        deviceData = new ArrayList<>();
        initDeviceList();
        //loading.show();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initDeviceList();
//        loading.show();
    }

    private void initDeviceList() {
        String url = MyApiService.DeviceList;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                // loading.dismiss();
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    if ("1".equals(code)) {
                        JSONArray array = obj.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            DeviceBean mDeviceBean = new DeviceBean();
                            mDeviceBean.setDevice(object.optString("device"));
                            deviceData.add(mDeviceBean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                // loading.dismiss();
            }
        });
    }


    private void bindService() {
        Intent bindIntent = new Intent(getActivity(), BluetoothService.class);
        getActivity().bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        getActivity().unbindService(mFhrSCon);
    }

    private ServiceConnection mFhrSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothService.BluetoothBinder) service).getService();
            mBluetoothService.setScanCallback(callback);
            mBluetoothService.scanDevice();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothService = null;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBluetoothService != null)
            unbindService();
    }

    private void initView() {
        mHeader = findViewById(R.id.tv_header);
        mBtnback = findViewById(R.id.backBtn);
//        mBtnDevic= findViewById(R.id.device_btButton);
//        mBtnLight = findViewById(R.id.light_btButton);
        mBtnback.setVisibility(View.GONE);
        mHeader.setText("设备连接");
//        mBtnDevic.setOnClickListener(this);
//        mBtnLight.setOnClickListener(this);
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        img_loading = findViewById(R.id.img_loading);
        ListView listView_device = findViewById(R.id.list_device);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        progressDialog = new ProgressDialog(getActivity());
        mResultAdapter = new ResultAdapter(getActivity());
        listView_device.setAdapter(mResultAdapter);
        listView_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBluetoothService != null) {
                    /**
                     * 蓝牙名字获取
                     */
                    name1 = String.valueOf(mResultAdapter.getItem(position).getDevice().getName());
                    String[] all=name1.split("-");
                    name3=all[1];//截取光源字段
                    Log.e("TAG",name3);
                    /**
                     * 第一个字母
                     */
                    name01 = name1.substring(0, 1);
                    getAddress = String.valueOf(mResultAdapter.getItem(position).getDevice().getAddress());

                    Log.d("TAG", "getAddress-->" + getAddress);
                    Log.e("TAG5858", "getName-->" + mResultAdapter.getItem(position).getDevice().getName());
                    //name = name1.substring(0, 4);
                    // System.out.println(aString.substring(aString.length()-1, aString.length()));
                    name = name1.substring(name1.length() - 3, name1.length());
                    Log.e("TAG", "name-->" + name);
                    Log.d("TAG", "name1-->" + name1);
                    if ("X".equals(name01) || "Q".equals(name01)|| "E".equals(name01)|| "H".equals(name01)|| "D".equals(name01)) {
                        mBluetoothService.cancelScan();
                        mBluetoothService.connectDevice(mResultAdapter.getItem(position));
                    }
//                    if ("-00".equals(name) || "-32".equals(name) || "-42".equals(name)) {
//
//                    }
                    else {
                        showToast("不可连接");
                        return;
                    }

                    mResultAdapter.clear();
                    mResultAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.device_btButton:
//                toActivity(Device01Activity.class);
//                break;
//            case R.id.light_btButton:
//                toActivity(Device02Activity.class);
//                break;
            case R.id.btn_start:
                Log.d("TAG", "111");
                checkPermissions();
                break;
            case R.id.btn_stop:
                if (mBluetoothService != null) {
                    mBluetoothService.cancelScan();
                }
                break;
        }
    }

    private CustomDialog customDialog;
    private String address;
    private String name2;
    private BluetoothService.Callback callback = new BluetoothService.Callback() {
        @Override
        public void onStartScan() {
            Log.d("TAG", "222");
            mResultAdapter.clear();
            mResultAdapter.notifyDataSetChanged();
            img_loading.startAnimation(operatingAnim);
            btn_start.setEnabled(false);
            btn_stop.setVisibility(View.VISIBLE);
        }

        @Override
        public void onScanning(ScanResult result) {
            Log.d("TAG", "333");
            if (!StringUtil.isBlank(result.getDevice().getName())) {
//                Log.e("TAG5656", result.getDevice().getName());
                name2 = result.getDevice().getName();
            } else return;
            mResultAdapter.addResult(result);
            mResultAdapter.notifyDataSetChanged();
            if (name2.equals(name1)) {
                mBluetoothService.connectDevice(result);
            } else return;

        }

        @Override
        public void onScanComplete() {
            Log.d("TAG", "444");
            img_loading.clearAnimation();
            btn_start.setEnabled(true);
            btn_stop.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onConnecting() {
            Log.d("TAG", "555");
            progressDialog.show();
        }

        @Override
        public void onConnectFail() {
            Log.d("TAG", "66");
            img_loading.clearAnimation();
            btn_start.setEnabled(true);
            btn_stop.setVisibility(View.INVISIBLE);
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDisConnected() {
            Log.d("TAG", "77");
            progressDialog.dismiss();
            mResultAdapter.clear();
            mResultAdapter.notifyDataSetChanged();
            img_loading.clearAnimation();
            btn_start.setEnabled(true);
            btn_stop.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "连接断开", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServicesDiscovered() {
            progressDialog.dismiss();
            if ("00".equals(name3)) {
                Intent intent = new Intent(getActivity(), Device09Activity.class);
                intent.putExtra("address", getAddress);
                startActivity(intent);
            } else if ("32".equals(name3)) {
                Intent intent = new Intent(getActivity(), Device04Activity.class);
                intent.putExtra("address", getAddress);
                startActivity(intent);
            } else if ("42".equals(name3)) {
                Intent intent = new Intent(getActivity(), Device01Activity.class);
                intent.putExtra("address", getAddress);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), Device01Activity.class);
                intent.putExtra("address", getAddress);
                startActivity(intent);
            }
            UtilPreference.saveString(getActivity(), "BlueName", name1);
//            if (deviceData.size() == 0) {
//                showEnsureDialogTwo();
//                return;
//            } else {
//                for (int i = 0; i < deviceData.size(); i++) {
//                    address = deviceData.get(i).getDevice();
//                }
//                if (getAddress.equals(address)) {
//                    if ("Q200".equals(name)) {
//                        Intent intent = new Intent(getActivity(), Device09Activity.class);
//                        intent.putExtra("address", getAddress);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(getActivity(), Device01Activity.class);
//                        intent.putExtra("address", getAddress);
//                        startActivity(intent);
//                    }
//                }
//            }

        }
    };

    private void showEnsureDialogTwo() {
        customDialog = new CustomDialog(getActivity()).builder()
                .setGravity(Gravity.CENTER)//默认居中，可以不设置
                .setTitle("提示", getResources().getColor(R.color.sd_color_black))//可以不设置标题颜色，默认系统颜色
                .setSubTitle("是否绑定设备")
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {//可以选择设置颜色和不设置颜色两个方法
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setPositiveButton("确认", getResources().getColor(R.color.head_red), new View.OnClickListener() {//可以选择设置颜色和不设置颜色两个方法
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                        bindDevice();

                    }
                });
        customDialog.show();
    }

    private void bindDevice() {
        String url = MyApiService.AddDevice;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("device", getAddress);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    if ("1".equals(code)) {
                        if ("-00".equals(name)) {
                            Intent intent = new Intent(getActivity(), Device09Activity.class);
                            intent.putExtra("address", getAddress);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), Device01Activity.class);
                            intent.putExtra("address", getAddress);
                            startActivity(intent);
                        }
                    } else {
                        Log.d("TAG", obj.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 12:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }


    private void checkPermissions() {
        Log.d("TAG", "999");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions, 12);
        }
    }

    private void onPermissionGranted(String permission) {
        Log.d("TAG", "456");
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (mBluetoothService == null) {
                    Log.d("TAG", "123");
                    bindService();
                } else {
                    Log.d("TAG", "789");
                    mBluetoothService.scanDevice();
                }
                break;
        }
    }

    private class ResultAdapter extends BaseAdapter {

        private Context context;
        private List<ScanResult> scanResultList;

        ResultAdapter(Context context) {
            this.context = context;
            scanResultList = new ArrayList<>();
        }

        void addResult(ScanResult result) {
            scanResultList.add(result);
        }

        void clear() {
            scanResultList.clear();
        }

        @Override
        public int getCount() {
            return scanResultList.size();
        }

        @Override
        public ScanResult getItem(int position) {
            if (position > scanResultList.size())
                return null;
            return scanResultList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ResultAdapter.ViewHolder holder;
            if (convertView != null) {
                holder = (ResultAdapter.ViewHolder) convertView.getTag();
            } else {
                convertView = View.inflate(context, R.layout.adapter_scan_result, null);
                holder = new ResultAdapter.ViewHolder();
                convertView.setTag(holder);
                holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                holder.txt_mac = (TextView) convertView.findViewById(R.id.txt_mac);
                holder.txt_rssi = (TextView) convertView.findViewById(R.id.txt_rssi);
            }

            ScanResult result = scanResultList.get(position);
            BluetoothDevice device = result.getDevice();
            String name = device.getName();
            String mac = device.getAddress();
            int rssi = result.getRssi();
            holder.txt_name.setText(name);
            holder.txt_mac.setText(mac);
            holder.txt_rssi.setText(String.valueOf(rssi));
            return convertView;
        }

        class ViewHolder {
            TextView txt_name;
            TextView txt_mac;
            TextView txt_rssi;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 101:
//                initDeviceList();
//                break;
//        }
//    }
}
