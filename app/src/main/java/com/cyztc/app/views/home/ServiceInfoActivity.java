package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by ywl on 2016/12/18.
 */

public class ServiceInfoActivity extends BaseActivity{

    @BindView(R.id.bmapView)
    MapView mapView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info_layout);
        setTitle("园区地图");
        setBackView();

//        MyLocationData locData = new MyLocationData.Builder()
//                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(100).latitude(30.9053630000)
//                .longitude(103.6618620000).build();
//// 设置定位数据
//        mapView.getMap().setMyLocationData(locData);
//        mapView.setMyLocationData(locData);

        LatLng latLng = new LatLng(30.9053630000, 103.6618620000);
//        MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(latLng, 14);
//        mapView.getMap().setMapStatus(mapStatus);

        MapStatus mMapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(14)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mapView.getMap().setMapStatus(mMapStatusUpdate);

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mapView.getMap().addOverlay(option);



    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, ServiceInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
}
