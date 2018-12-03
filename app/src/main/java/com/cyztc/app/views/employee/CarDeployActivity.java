package com.cyztc.app.views.employee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.DepartMentSubBean;
import com.cyztc.app.httpservice.beans.SchduleCarBodyBean;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.ContactActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2017-7-25.
 */

public class CarDeployActivity extends BaseActivity{

    @BindView(R.id.tv_driver_subtype)
    TextView tvSubType;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_card)
    EditText etCard;
    @BindView(R.id.et_content)
    EditText etRemark;

    private String name;
    private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardeploy_layout);
        setTitle("调配车辆");
        setBackView();
        setTextMenuView("提交");
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        showToast("提交");

        String phone = etPhone.getText().toString().trim();
        String card = etCard.getText().toString().trim();
        String remark = etRemark.getText().toString().trim();

        if(TextUtils.isEmpty(name))
        {
            showToast("请先选择司机");
            return ;
        }
        if(TextUtils.isEmpty(phone))
        {
            showToast("请输入电话号码");
            return;
        }
        if(TextUtils.isEmpty(card))
        {
            showToast("请输入车牌号");
            return;
        }
        if(TextUtils.isEmpty(remark))
        {
            showToast("请添加备注");
            return;
        }

        SchduleCarBodyBean schduleCarBodyBean = new SchduleCarBodyBean();
        schduleCarBodyBean.setDriver(userId);
        schduleCarBodyBean.setMobile(phone);
        schduleCarBodyBean.setVehicleNo(card);
        schduleCarBodyBean.setVehicle(remark);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cardeploy", schduleCarBodyBean);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @OnClick(R.id.rl_driver)
    public void onClickDriver(View view)
    {
        Intent intent = new Intent(this, ChoiceDepartmentActivity.class);
        this.startActivityForResult(intent, 0x1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x1001)
        {
            if(resultCode == RESULT_OK)
            {
                Bundle bundle = data.getExtras();
                if(bundle != null) {
                    DepartMentSubBean departMentSubBean = (DepartMentSubBean) bundle.getSerializable("departMentSubBean");
                    if(departMentSubBean != null)
                    {
                        MyLog.d(departMentSubBean);
                        CarDeployActivity.this.name = departMentSubBean.getName();
                        CarDeployActivity.this.userId = departMentSubBean.getAccountId();
                        tvSubType.setText(name);
                    }
                }
            }
        }
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, CarDeployActivity.class);
        context.startActivity(intent);
    }
}
