package me.weishu.epic.samples.hook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;
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


    Button btnLocation;
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
//        //hook imsi获取方法
//        XposedHelpers.findAndHookMethod(
//                android.telephony.TelephonyManager.class.getName(),
//                lpparam.classLoader,
//                "getSubscriberId",
//                int.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) {
//                      //  XposedBridge.log("调用getSubscriberId获取了imsi");
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log(getMethodStack());
//                        super.afterHookedMethod(param);
//                    }
//                }
//        );
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
//        //hook获取mac地址方法
//        XposedHelpers.findAndHookMethod(
//                java.net.NetworkInterface.class.getName(),
//                lpparam.classLoader,
//                "getHardwareAddress",
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) {
//                     //   XposedBridge.log("调用getHardwareAddress()获取了mac地址");
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log(getMethodStack());
//                        super.afterHookedMethod(param);
//                    }
//                }
//        );



//        try {
//            DexposedBridge.findAndHookMethod(
//                    Class.forName("com.loc.d"),
//                    "startLocation",
//                    new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            super.beforeHookedMethod(param);
//                            Log.i("hook", Thread.currentThread().getName());
//                            Log.i("hook", Log.getStackTraceString(new Throwable()));
//                        }
//                    }
//            );
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        try {
            DexposedBridge.findAndHookMethod(
                    Class.forName("com.loc.d"),
                    "startLocation",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.i(TAG, Thread.currentThread().getName());
                            Log.i(TAG, Log.getStackTraceString(new Throwable()));
                        }
                    }
            );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //hook定位方法
//        XposedHelpers.findAndHookMethod(
//                LocationManager.class.getName(),
//                lpparam.classLoader,
//                "getLastKnownLocation",
//                String.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) {
//                        XposedBridge.log("调用getLastKnownLocation获取了GPS地址");
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log(getMethodStack());
//                        super.afterHookedMethod(param);
//                    }
//                }
//        );

    }





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
