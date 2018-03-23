package com.example.android.datafrominternet.LBS;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.fence.CreateFenceRequest;
import com.baidu.trace.api.fence.CreateFenceResponse;
import com.baidu.trace.api.fence.DeleteFenceResponse;
import com.baidu.trace.api.fence.FenceListResponse;
import com.baidu.trace.api.fence.HistoryAlarmResponse;
import com.baidu.trace.api.fence.MonitoredStatusByLocationResponse;
import com.baidu.trace.api.fence.MonitoredStatusResponse;
import com.baidu.trace.api.fence.OnFenceListener;
import com.baidu.trace.api.fence.UpdateFenceResponse;
import com.example.android.datafrominternet.Notebook.AddNote;
import com.example.android.datafrominternet.Notebook.NoteData;
import com.example.android.datafrominternet.R;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ucla on 2018/2/14.
 */

public class Map extends AppCompatActivity {

    public LocationClient mLocationClient = null;

    private MyLocationListener myListener = new MyLocationListener();

    private MapView mapView;

    private BaiduMap baiduMap;

    private Boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        getPermission();

        Bmob.initialize(this, "c12ad56b69a6e30cb8cc89b566379d19");

        mapView = (MapView) findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
    }

    private void getPermission(){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(Map.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(Map.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(Map.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(Map.this,permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {

            final double Lat = location.getLatitude();
            final double Lon = location.getLongitude();
            final LatLng ll = new LatLng(Lat,Lon);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(isFirstLoc){
                        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,18f);
                        baiduMap.animateMapStatus(update);
                        isFirstLoc = false;
                    }

                    setMyLocation(Lon, Lat);
                    BmobGeoPoint point = new BmobGeoPoint(Lon, Lat);
                    setBmobInfo(point);
                    setOthersLocation(point);

                }
            });
        }

    }
    private void setMyLocation(double Lon, double Lat){
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(Lat);
        locationBuilder.longitude(Lon);
        MyLocationData locationdata = locationBuilder.build();
        baiduMap.setMyLocationData(locationdata);
    }

    private void setBmobInfo(final BmobGeoPoint point){

        SharedPreferences preferences = getSharedPreferences("userinfo",MODE_PRIVATE);
        final String username = preferences.getString("username",null);
        final String number = preferences.getString("phonenumber",null);

        BmobQuery<LocationData> query = new BmobQuery<>();
        query.addWhereEqualTo("phoneNumber", number);
        query.setLimit(1);
        query.findObjects(new FindListener<LocationData>() {
            @Override
            public void done(List<LocationData> object, BmobException e) {
                if(e==null){
                    String objectId = object.get(0).getObjectId();
                    update(objectId,point,number,username);
                    Log.i("bmob",objectId+"haha");
                }else{
                    save(point,number,username);
                    Log.i("bmob","失败："+e.getMessage()+"\n"+e.getErrorCode());
                }
            }
        });




    }

    private void save(BmobGeoPoint point, String number, String username){

        LocationData locationData = new LocationData();
        locationData.setGpsAdd(point);
        locationData.setPhoneNumber(number);
        locationData.setUserName(username);
        locationData.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e!=null){
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    Toast.makeText(Map.this,"同步修改数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOthersLocation(final BmobGeoPoint point){
        BmobQuery<LocationData> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereWithinKilometers("gpsAdd",point,3000.0);
        bmobQuery.setLimit(10);    //获取最接近用户地点的10条数据
        bmobQuery.findObjects(new FindListener<LocationData>() {
            @Override
            public void done(List<LocationData> object,BmobException e) {
                if(e==null){
                    baiduMap.clear();
                    for(int i=0; i<object.size();i++) {
                        double lat = object.get(i).getGpsAdd().getLatitude();
                        double lon = object.get(i).getGpsAdd().getLongitude();
                        addOverlay(lat, lon);

//                        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;//定位跟随态
//                        //int mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;   //默认为 LocationMode.NORMAL 普通态
//                        //int mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;  //定位罗盘态
//                        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
//                        MyLocationConfiguration config = new MyLocationConfiguration(
//                                mCurrentMode, true, mCurrentMarker,
//                                ColorTemplate.VORDIPLOM_COLORS[i%6],ColorTemplate.VORDIPLOM_COLORS[i%6]);
//                        baiduMap.setMyLocationConfiguration(config);
                    }

                    Log.d("test","查询成功：共" + object.size() + "条数据。");
                }else {
                    Log.d("test","查询失败：" + e.getMessage());
                }
            }
        });
    }

    private void update(final String objectId, final BmobGeoPoint point, final String number, final String username){

        LocationData locationData = new LocationData();
        locationData.setGpsAdd(point);

        locationData.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","更新成功");
                }else if (e.getErrorCode()==9018){
                    save(point,number,username);
                    Log.i("bmob","save"+e.toString());
                }else {
                    Log.i("bmob","更新失败"+e.toString());
                    Toast.makeText(Map.this,"同步地理位置失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addOverlay(double lat, double lon) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
        LatLng latLng = new LatLng(lat, lon);
        Marker marker;
        OverlayOptions options;
        options = new MarkerOptions()
                .position(latLng)//设置位置
                .icon(bitmap);//设置图标样式
        //添加marker
        marker = (Marker) baiduMap.addOverlay(options);
        //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
    }

    private void createFence() {
        // 请求标识
        int tag = 11;
        // 轨迹服务ID
        long serviceId = 0;
        // 围栏名称
        String fenceName = "hit-library";
        // 监控对象
        String monitoredPerson = "myTrace";
        // 多边形顶点集
        List<com.baidu.trace.model.LatLng> vertexes = new ArrayList<>();
        com.baidu.trace.model.LatLng ll = new com.baidu.trace.model.LatLng(23,234);
        vertexes.add(ll);
        // 去噪精度
        int denoise = 100;
        // 坐标类型
        com.baidu.trace.model.CoordType coordType = com.baidu.trace.model.CoordType.bd09ll;

        // 创建服务端多边形围栏请求实例
        CreateFenceRequest request = CreateFenceRequest.buildServerPolygonRequest(tag,
                serviceId, fenceName, monitoredPerson, vertexes, denoise, coordType);

        // 初始化围栏监听器
        // 初始化围栏监听器
        OnFenceListener mFenceListener = new OnFenceListener() {
            // 创建围栏回调
            @Override
            public void onCreateFenceCallback(CreateFenceResponse response) {}
            // 更新围栏回调
            @Override
            public void onUpdateFenceCallback(UpdateFenceResponse response) {}
            // 删除围栏回调
            @Override
            public void onDeleteFenceCallback(DeleteFenceResponse response) {}
            // 围栏列表回调
            @Override
            public void onFenceListCallback(FenceListResponse response) {}
            // 监控状态回调
            @Override
            public void onMonitoredStatusCallback(MonitoredStatusResponse
                                                          response) {}
            // 指定位置监控状态回调
            @Override
            public void onMonitoredStatusByLocationCallback(MonitoredStatusByLocationResponse response) {}
            // 历史报警回调
            @Override
            public void onHistoryAlarmCallback(HistoryAlarmResponse response) {}
        };

        // 创建服务端多边形围栏
        LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
        mTraceClient.createFence(request, mFenceListener);
    }


}


