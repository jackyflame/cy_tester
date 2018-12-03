package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;

/**
 * Created by ywl on 2016/12/18.
 */

public class UsedPhoneActivity extends BaseActivity{

    @BindView(R.id.ly_nums)
    LinearLayout lyNums;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_phones_layout);
        setTitle("服务电话");
        setBackView();
        getNormalPhoneNumb("");
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, UsedPhoneActivity.class);
        context.startActivity(intent);
    }

    public void getServicePhone(String meetingId)
    {
        HomeApi.getInstance().getMeetingPhone(meetingId, new HttpSubscriber<String>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {

            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }

    public void getNormalPhoneNumb(String meetingId)
    {
        UserApi.getInstance().getNormalPhonNums(meetingId, new HttpSubscriber<String[]>(new SubscriberOnListener<String[]>() {
            @Override
            public void onSucceed(String[] data) {
                initDatas(data, lyNums);
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }

    public void initDatas(final String[] nums, LinearLayout lyNums)
    {
        if(lyNums != null) {
            int length = nums.length;
            for (int i = 0; i < length; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_phonenums_layout, null);
                AutoUtils.auto(view);
                TextView tvnum = (TextView) view.findViewById(R.id.tv_num);
                tvnum.setText(nums[i]);
                lyNums.addView(view);
                final int index = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] phones = nums[index].split("：");
                        if(phones.length == 2)
                        {
                            if(CommonUtil.isNumeric(phones[1].trim()))
                            {
                                final String phone = phones[1].trim();
                                showAskDialog("提示", "是否拨打电话：" + phone, "拨打", "取消", new NormalAskComplexDialog.OnDalogListener() {
                                    @Override
                                    public void onYes() {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:" + phone));
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void OnNo() {
                                    }
                                });
                            }
                            else
                            {
                                showToast("没有电话号码");
                            }
                        }
                        else
                        {
                            showToast("没有电话号码");
                        }
                    }
                });
            }
        }
    }
}
