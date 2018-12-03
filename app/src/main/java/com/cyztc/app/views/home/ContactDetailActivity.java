package com.cyztc.app.views.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.ContactBean;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/22.
 */

public class ContactDetailActivity extends BaseActivity{

    private String contactstr;

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_roomnum)
    TextView tvRoomNum;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_roomphone)
    TextView tvRoomPhone;

    private String phone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail_layout);
        contactstr = getIntent().getStringExtra("contactstr");
        Gson gson = new Gson();
        ContactBean contactBean = gson.fromJson(contactstr, ContactBean.class);
        if(contactBean != null)
        {
            initData(contactBean);
        }
        setBackView();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context, String contactstr)
    {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra("contactstr", contactstr);
        context.startActivity(intent);
    }

    public void initData(ContactBean contactBean)
    {
        phone = contactBean.getPhoneNum();
        tvPhone.setText(contactBean.getPhoneNum());
        tvUnit.setText(contactBean.getWorkUnit());
        tvPosition.setText(contactBean.getPosition());
        tvRoomNum.setText(contactBean.getRoomPhoneNum());
        tvName.setText(contactBean.getStudentName());
        tvRoomPhone.setText(contactBean.getRoomCode());
        ImageLoad.getInstance().displayCircleImage(this, ivHead, HttpMethod.IMG_URL + contactBean.getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
    }

    @OnClick(R.id.rl_call_phone)
    public void onClickCallPhone(View view)
    {

        if(!TextUtils.isEmpty(phone))
        {
            showAskDialog("提示", "是否拨打电话？", "拨打", "取消", new NormalAskComplexDialog.OnDalogListener() {
                @Override
                public void onYes() {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(ContactDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        showToast("请同意拨打电话");
                        return;
                    }
                    startActivity(intent);
                }

                @Override
                public void OnNo() {
                }
            });
        }

    }

}
