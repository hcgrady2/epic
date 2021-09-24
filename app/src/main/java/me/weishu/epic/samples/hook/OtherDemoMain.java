package me.weishu.epic.samples.hook;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import me.weishu.epic.samples.R;


/**
 * @Author: wanghaichao
 * @Date: 2021/9/23
 * @Description:
 * @Copyright: all rights reserved.
 */
public class OtherDemoMain extends Activity {

    private static final String TAG = "HookTag";
    public static final int LOCATION_CODE = 301;
    private LocationManager locationManager;
    private String locationProvider = null;


    public LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
        }
        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //如果位置发生变化，重新显示地理位置经纬度
                Toast.makeText(OtherDemoMain.this, location.getLongitude() + " " +
                        location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "监视地理位置变化-经纬度："+location.getLongitude()+"   "+location.getLatitude());
            }
        }
    };


    Button btnLocation,btnGetMac;
    Button btnImei,btnImsi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ohter_main);
        initHook();
        
        btnLocation = findViewById(R.id.btn_location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        btnGetMac = findViewById(R.id.btn_mac);
        btnGetMac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mac = getMacAddress();

                Log.i(TAG, "onClick:  mac :" + mac);
            }
        });
        

        btnImei = findViewById(R.id.btn_read_phone);
        btnImsi = findViewById(R.id.btn_imsi);

        btnImei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imei = getIMEI(OtherDemoMain.this);
                Log.i(TAG, "onClick: imei:"  + imei);
            }
        });

        btnImsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imsi = getSimOperator(0);
                Log.i(TAG, "onClick: imsi:" + imsi);
            }
        });
    }

    private void initHook() {

//        //hook获取设备信息方法
//        XposedHelpers.findAndHookMethod(
//                android.telephony.TelephonyManager.class.getName(),
//                lpparam.classLoader,
//                "getDeviceId",
//                int.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) {
//                       // XposedBridge.log("调用getDeviceId(int)获取了imei");
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log(getMethodStack());
//                        super.afterHookedMethod(param);
//                    }
//                }
//        );
//


        DexposedBridge.findAndHookMethod(TelephonyManager.class, "getSubscriberId",new XC_MethodHook() {

            // To be invoked before Activity.onCreate().
            @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //打印了堆栈，在这里处理是否违规获取权限，是否同意之前，是否后台，频率是否超限等。
                //   Log.i(TAG, Log.getStackTraceString(new Throwable()));

                Log.i(TAG, "beforeHookedMethod: getSubscriberId 获取 imei");
            }

            // To be invoked after Activity.onCreate()
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.callMethod(param.thisObject, "sampleMethod", 2);
            }
        });





        DexposedBridge.findAndHookMethod(TelephonyManager.class, "getSimOperator",new XC_MethodHook() {

            // To be invoked before Activity.onCreate().
            @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //打印了堆栈，在这里处理是否违规获取权限，是否同意之前，是否后台，频率是否超限等。
                //   Log.i(TAG, Log.getStackTraceString(new Throwable()));

                Log.i(TAG, "beforeHookedMethod: getSimOperator 获取 imei");
            }

            // To be invoked after Activity.onCreate()
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.callMethod(param.thisObject, "sampleMethod", 2);
            }
        });





        DexposedBridge.findAndHookMethod(TelephonyManager.class, "getSimOperator",new XC_MethodHook() {

            // To be invoked before Activity.onCreate().
            @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //打印了堆栈，在这里处理是否违规获取权限，是否同意之前，是否后台，频率是否超限等。
                //   Log.i(TAG, Log.getStackTraceString(new Throwable()));

                Log.i(TAG, "beforeHookedMethod: getSimOperator 获取 imei");
            }

            // To be invoked after Activity.onCreate()
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.callMethod(param.thisObject, "sampleMethod", 2);
            }
        });





        DexposedBridge.findAndHookMethod(TelephonyManager.class, "getImei",new XC_MethodHook() {

            // To be invoked before Activity.onCreate().
            @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //打印了堆栈，在这里处理是否违规获取权限，是否同意之前，是否后台，频率是否超限等。
             //   Log.i(TAG, Log.getStackTraceString(new Throwable()));

                Log.i(TAG, "beforeHookedMethod: getImei");
            }

            // To be invoked after Activity.onCreate()
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.callMethod(param.thisObject, "sampleMethod", 2);
            }
        });



        DexposedBridge.findAndHookMethod(TelephonyManager.class, "getDeviceId",new XC_MethodHook() {

            // To be invoked before Activity.onCreate().
            @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //打印了堆栈，在这里处理是否违规获取权限，是否同意之前，是否后台，频率是否超限等。
               // Log.i(TAG, Log.getStackTraceString(new Throwable()));
                Log.i(TAG, "beforeHookedMethod: getDeviceId");

            }

            // To be invoked after Activity.onCreate()
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.callMethod(param.thisObject, "sampleMethod", 2);
            }
        });





        ///todo,
//        //hook低版本系统获取mac地方方法
//        XposedHelpers.findAndHookMethod(
//                android.net.wifi.WifiInfo.class.getName(),
//                lpparam.classLoader,
//                "getMacAddress",
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) {
//                      //  XposedBridge.log("调用getMacAddress()获取了mac地址");
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log(getMethodStack());
//                        super.afterHookedMethod(param);
//                    }
//                }
//        );



        DexposedBridge.findAndHookMethod(NetworkInterface.class, "getHardwareAddress",new XC_MethodHook() {

            // To be invoked before Activity.onCreate().
            @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //打印了堆栈，在这里处理是否违规获取权限，是否同意之前，是否后台，频率是否超限等。
                Log.i(TAG, Log.getStackTraceString(new Throwable()));

            }

            // To be invoked after Activity.onCreate()
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.callMethod(param.thisObject, "sampleMethod", 2);
            }
        });


        // Target class, method with parameter types, followed by the hook callback (XC_MethodHook).
        DexposedBridge.findAndHookMethod(LocationManager.class, "getLastKnownLocation", String.class, new XC_MethodHook() {

            // To be invoked before Activity.onCreate().
            @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //打印了堆栈，在这里处理是否违规获取权限，是否同意之前，是否后台，频率是否超限等。
               Log.i(TAG, Log.getStackTraceString(new Throwable()));

            }
            // To be invoked after Activity.onCreate()
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.callMethod(param.thisObject, "sampleMethod", 2);
            }
        });




    }




    /**
     * 反射获取 getSubscriberId ，既imsi
     *
     * @param subId
     * @return
     */
    public  String getSubscriberId(int subId) {
        TelephonyManager telephonyManager = (TelephonyManager)OtherDemoMain.this.getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务
        Class<?> telephonyManagerClass = null;
        String imsi = null;
        try {
            telephonyManagerClass = Class.forName("android.telephony.TelephonyManager");

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
                Method method = telephonyManagerClass.getMethod("getSubscriberId", int.class);
                imsi = (String) method.invoke(telephonyManager, subId);
            } else if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
                Method method = telephonyManagerClass.getMethod("getSubscriberId", long.class);
                imsi = (String) method.invoke(telephonyManager, (long) subId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "IMSI==" + imsi);
        return imsi;
    }

    /**
     * 反射获取 getSubscriptionId ，既 subid
     *
     * @param slotId 卡槽位置（0，1）
     * @return
     */
    public  int getSubscriptionId(int slotId) {
        try {
            Method datamethod;
            int setsubid = -1;//定义要设置为默认数据网络的subid
            //获取默认数据网络subid   getDefaultDataSubId
            Class<?> SubscriptionManager = Class.forName("android.telephony.SubscriptionManager");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) { // >= 24  7.0
                datamethod = SubscriptionManager.getDeclaredMethod("getDefaultDataSubscriptionId");
            } else {
                datamethod = SubscriptionManager.getDeclaredMethod("getDefaultDataSubId");
            }
            datamethod.setAccessible(true);
            int SubId = (int) datamethod.invoke(SubscriptionManager);


            Method subManagermethod = SubscriptionManager.getDeclaredMethod("from", Context.class);
            subManagermethod.setAccessible(true);
            Object subManager = subManagermethod.invoke(SubscriptionManager, OtherDemoMain.this);

            //getActiveSubscriptionInfoForSimSlotIndex  //获取卡槽0或者卡槽1  可用的subid
            Method getActivemethod = SubscriptionManager.getDeclaredMethod("getActiveSubscriptionInfoForSimSlotIndex", int.class);
            getActivemethod.setAccessible(true);
            Object msubInfo = getActivemethod.invoke(subManager, slotId);  //getSubscriptionId

            Class<?> SubInfo = Class.forName("android.telephony.SubscriptionInfo");

            //slot0   获取卡槽0的subid
            int subid = -1;
            if (msubInfo != null) {
                Method getSubId0 = SubInfo.getMethod("getSubscriptionId");
                getSubId0.setAccessible(true);
                subid = (int) getSubId0.invoke(msubInfo);
            }
            Log.i(TAG, "slotId=" + slotId + ", subid=" + subid);
            return subid;
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return -1;
    }

    /**
     * 获取运营商 IMSI
     * 默认为 IMEI1对应的 IMSI
     *
     * @return
     */
    public  String getSimOperator() {
        TelephonyManager telephonyManager = (TelephonyManager) OtherDemoMain.this.getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务
        return telephonyManager.getSimOperator();
    }

    /**
     * 根据卡槽位置 获取运营商 IMSI
     *
     * @param slotId 卡槽位置（0，1）
     * @return
     */
    public  String getSimOperator(int slotId) {
        int subid = getSubscriptionId(slotId);
        if (subid == -1) {
            return null;
        }

        String imsi = getSubscriberId(subid);
        if (!TextUtils.isEmpty(imsi)) {
            return imsi;
        }

        return null;
    }

    /**
     * 通过卡槽位置拿 IMEI
     *
     * @param slotId (0, 1卡槽位置）
     * @return
     */
    public  String getImei(int slotId) {
        if (slotId != 0 && slotId != 1) {
            return null;
        }

        TelephonyManager tm = (TelephonyManager) OtherDemoMain.this.getSystemService(TELEPHONY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return tm.getDeviceId(slotId);

        } else if (slotId == 0){
            return tm.getDeviceId();

        } else {
            TelephonyManager telephonyManager = (TelephonyManager) OtherDemoMain.this.getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务
            Class<?> telephonyManagerClass = null;
            String imei = null;

            try {
                telephonyManagerClass = Class.forName("android.telephony.TelephonyManager");
                Method method = telephonyManagerClass.getMethod("getImei", int.class);
                imei = (String) method.invoke(telephonyManager, slotId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i(TAG, "imei==" + imei);

            return imei;
        }
    }



    /**
     * 获取 imei
     * @param context
     * @return
     */
    private String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm.getImei();
        } else {
            return tm.getDeviceId();
        }
    }




    /**
     * 获取 mac 地址
     */
    public  String getMacAddress() {
        String macAddr = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            Log.d("HookTag","6.0及以上、7.0以下");
            macAddr = MacUtils.getMacAddress(OtherDemoMain.this);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Log.d("HookTag","7.0及以上");
            macAddr = MacUtils.getMachineHardwareAddress();
        }else{
            Log.d("HookTag","6.0以下");
            macAddr = MacUtils.getMacAddress0(OtherDemoMain.this);
        }
        Log.d("HookTag",macAddr);
        return macAddr;
    }
    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     * @return
     */
    private  String getMacFromHardware() {
        Log.i(TAG, "getMacFromHardware: 获取 mac 地址");
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }




    /**
     * 获取定位
     */
    private void getLocation(){
        //1.获取位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
            Log.v(TAG, "定位方式GPS");
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Log.v(TAG, "定位方式Network");
        }else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
            } else {
                //3.获取上次的位置，一般第一次运行，此值为null
                Location location = locationManager.getLastKnownLocation(locationProvider);
                if (location!=null){
                    Toast.makeText(this, location.getLongitude() + " " +
                            location.getLatitude() + "",Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "获取上次的位置-经纬度："+location.getLongitude()+"   "+location.getLatitude());
                    getAddress(location);

                }else{
                    //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                    locationManager.requestLocationUpdates(locationProvider, 3000, 1,locationListener);
                }
            }
        } else {
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location!=null){
                Toast.makeText(this, location.getLongitude() + " " +
                        location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "获取上次的位置-经纬度："+location.getLongitude()+"   "+location.getLatitude());
                getAddress(location);

            }else{
                //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                locationManager.requestLocationUpdates(locationProvider, 3000, 1,locationListener);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "申请权限", Toast.LENGTH_LONG).show();
                    try {
                        List<String> providers = locationManager.getProviders(true);
                        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                            //如果是Network
                            locationProvider = LocationManager.NETWORK_PROVIDER;

                        }else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                            //如果是GPS
                            locationProvider = LocationManager.GPS_PROVIDER;
                        }
                        Location location = locationManager.getLastKnownLocation(locationProvider);
                        if (location!=null){
                            Toast.makeText(this, location.getLongitude() + " " +
                                    location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                            Log.v(TAG, "获取上次的位置-经纬度："+location.getLongitude()+"   "+location.getLatitude());
                        }else{
                            // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                            locationManager.requestLocationUpdates(locationProvider, 0, 0,locationListener);
                        }

                    }catch (SecurityException e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "缺少权限", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }


    //获取地址信息:城市、街道等信息
    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                Toast.makeText(this, "获取地址信息："+result.toString(), Toast.LENGTH_LONG).show();
                Log.v(TAG, "获取地址信息："+result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }




}
