package com.cyztc.app.views.employee.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.DepartMentSubBean;
import com.cyztc.app.bean.EmployeeApplyBean;
import com.cyztc.app.bean.OnlyStringBean;
import com.cyztc.app.dialog.ChoiceDayHourTimeDialog;
import com.cyztc.app.dialog.SelectedTypeDialog;
import com.cyztc.app.httpservice.serviceapi.EmployeeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.views.employee.ChoiceDepartmentActivity;
import com.cyztc.app.views.employee.leave.adapter.ChoiceLeaderAdapater;
import com.cyztc.app.widget.CustomListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2017-5-14.
 */

public class CarApplyActivity extends BaseActivity{

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_subtype)
    TextView tvSubtype;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.listview)
    CustomListView listView;
    @BindView(R.id.et_start)
    EditText etStart;
    @BindView(R.id.et_end)
    EditText etNnd;
    @BindView(R.id.ly_points)
    LinearLayout lyPoints;

    private List<TextView> tvPointViews;


    private List<OnlyStringBean> datas;
    private EmployeeApplyBean employeeApplyBean;

    private List<DepartMentSubBean> departMentBeens;
    private ChoiceLeaderAdapater choiceLeaderAdapater;
    private LayoutInflater layoutInflater;


    private final int REQUEST = 0x1001;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_apply_layout);
        employeeApplyBean = new EmployeeApplyBean();
        setTitle("派车申请");
        setBackView();
        setTextMenuView("提交");
        tvPointViews = new ArrayList<>();
        layoutInflater = LayoutInflater.from(this);
        initItemData();
        setAdapter();
    }

    private void initItemData()
    {
        datas = new ArrayList<>();
        OnlyStringBean onlyStringBean = new OnlyStringBean();
        onlyStringBean.setItem("轿车");
        OnlyStringBean onlyStringBean2 = new OnlyStringBean();
        onlyStringBean2.setItem("商务车");
        OnlyStringBean onlyStringBean3 = new OnlyStringBean();
        onlyStringBean3.setItem("大巴车");
        OnlyStringBean onlyStringBean4 = new OnlyStringBean();
        onlyStringBean4.setItem("其它");
        datas.add(onlyStringBean);
        datas.add(onlyStringBean2);
        datas.add(onlyStringBean3);
        datas.add(onlyStringBean4);
    }

    private void setAdapter()
    {
        departMentBeens = new ArrayList<>();
        choiceLeaderAdapater = new ChoiceLeaderAdapater(this, departMentBeens);
        listView.setAdapter(choiceLeaderAdapater);
        choiceLeaderAdapater.setOnDelListener(new ChoiceLeaderAdapater.OnDelListener() {
            @Override
            public void onDel(DepartMentSubBean departMentSubBean) {
                if(departMentSubBean != null)
                {
                    departMentBeens.remove(departMentSubBean);
                    choiceLeaderAdapater.notifyDataSetChanged();
                }
            }
        });
    }

    private String getPoints()
    {
        String points = "";
        if(tvPointViews != null)
        {
            for(TextView tvPoint : tvPointViews)
            {
                String p = tvPoint.getText().toString().trim();
                if(TextUtils.isEmpty(p))
                {
                    showToast("请输入途经点");
                    tvPoint.setFocusable(true);
                    return "";
                }
                else
                {
                    points += "::::" + p;
                }
            }
        }
        return points;
    }



    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        String type = tvSubtype.getText().toString().trim();
        String startTime = tvStartTime.getText().toString().trim();
        String midPoint = "";
        String endTime = tvEndTime.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String start = etStart.getText().toString().trim();
        String end = etNnd.getText().toString().trim();
        if(TextUtils.isEmpty(type))
        {
            showToast("请选择车辆类型");
            return;
        }
        if(TextUtils.isEmpty(startTime))
        {
            showToast("请选择开始时间");
            return;
        }
        if(TextUtils.isEmpty(endTime))
        {
            showToast("请选择结束时间");
            return;
        }

        long lstartTime = CommonUtil.timeToLong(startTime, "yyyy-MM-dd HH:mm");
        long lendTime = CommonUtil.timeToLong(endTime, "yyyy-MM-dd HH:mm");
        if(lendTime <= lstartTime)
        {
            showToast("结束时间必须大于开始时间");
            return;
        }

        if(TextUtils.isEmpty(start))
        {
            showToast("请填写起点");
            return;
        }
        if(tvPointViews.size() > 0)
        {
            midPoint = getPoints();
            if(TextUtils.isEmpty(midPoint))
            {
                return;
            }
            MyLog.d("midPoint:" + midPoint);
        }

        if(TextUtils.isEmpty(end))
        {
            showToast("请填写终点");
            return;
        }
        if(TextUtils.isEmpty(content))
        {
            showToast("请填写请假理由");
            return;
        }
        if(departMentBeens == null || departMentBeens.size() == 0)
        {
            showToast("请选择审批人");
            return;
        }
        employeeApplyBean.setCreator(CyApplication.getInstance().getUserBean().getAccountId());
        employeeApplyBean.setStatus(0);
        employeeApplyBean.setType(4);
        employeeApplyBean.setSubType(CommonUtil.getCarStatus(type));
        employeeApplyBean.setStartAddress(start + midPoint);
        employeeApplyBean.setEndAddress(end);
        employeeApplyBean.setStartTime(lstartTime);
        employeeApplyBean.setEndTime(lendTime);
        employeeApplyBean.setContent(content);
        employeeApplyBean.setAuditors(getApplyPeo());
        apply(employeeApplyBean);
    }

    @OnClick(R.id.ly_start_time)
    public void onClickStartTime(View view)
    {
        ChoiceDayHourTimeDialog choiceDayHourTimeDialog = new ChoiceDayHourTimeDialog(this);
        Window window = choiceDayHourTimeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        choiceDayHourTimeDialog.show();
        choiceDayHourTimeDialog.setOnBirthListener(new ChoiceDayHourTimeDialog.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day, String hour, String min) {
                MyLog.d(year + "-" + month + "-" + day + " " + hour + ":" + min);
                tvStartTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + min);
            }
        });
    }

    @OnClick(R.id.ly_end_time)
    public void onClickEndTime(View view)
    {
        ChoiceDayHourTimeDialog choiceDayHourTimeDialog = new ChoiceDayHourTimeDialog(this);
        Window window = choiceDayHourTimeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        choiceDayHourTimeDialog.show();
        choiceDayHourTimeDialog.setOnBirthListener(new ChoiceDayHourTimeDialog.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day, String hour, String min) {
                tvEndTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + min);
            }
        });
    }

    @OnClick(R.id.rl_service_type)
    public void onClickServiceType(View view)
    {
        SelectedTypeDialog selectedTypeDialog = new SelectedTypeDialog(this);
        Window window = selectedTypeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        selectedTypeDialog.show();
        selectedTypeDialog.setDatas(datas);
        selectedTypeDialog.setOnItemSelectdListener(new SelectedTypeDialog.OnItemSelectdListener() {
            @Override
            public void onItemSelected(OnlyStringBean onlyStringBean) {
                tvSubtype.setText(onlyStringBean.getItem());
            }
        });
    }

    @OnClick(R.id.ly_add_peo)
    public void onClickAddPeo(View view)
    {
        Intent intent = new Intent(this, ChoiceDepartmentActivity.class);
        this.startActivityForResult(intent, REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST)
            {
                Bundle bundle = data.getExtras();
                if(bundle != null) {
                    DepartMentSubBean departMentSubBean = (DepartMentSubBean) bundle.getSerializable("departMentSubBean");
                    if(departMentSubBean != null)
                    {
                        if(!checkDoubleData(departMentSubBean))
                        {
                            departMentBeens.add(departMentSubBean);
                            choiceLeaderAdapater.notifyDataSetChanged();
                        }
                        else
                        {
                            showToast("审批人已经存在，请重新选择");
                        }
                    }
                }
            }
        }
    }

    @OnClick(R.id.iv_add_point)
    public void onClickAddPoint(View view)
    {
        final View point = layoutInflater.inflate(R.layout.item_midel_point_car_layout, null);
        final TextView tvPoint = (TextView) point.findViewById(R.id.et_point);
        ImageView ivPoint = (ImageView) point.findViewById(R.id.iv_del_point);
        tvPointViews.add(tvPoint);
        ivPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPointViews.remove(tvPoint);
                lyPoints.removeView(point);
            }
        });
        lyPoints.addView(point);
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, CarApplyActivity.class);
        context.startActivity(intent);
    }

    private boolean checkDoubleData(DepartMentSubBean departMentSubBean)
    {

        for(DepartMentSubBean d : departMentBeens)
        {
            if(d.getName().equals(departMentSubBean.getName()))
                return true;
        }
        return false;
    }

    private void apply(EmployeeApplyBean employeeApplyBean)
    {
        showLoadDialog("申请提交中");
        EmployeeApi.getInstance().apply(employeeApplyBean, new HttpSubscriber<String>(new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String String) {
                hideLoadDialog();
                CarApplyActivity.this.finish();
            }

            @Override
            public void onError(int code, String msg) {
                hideLoadDialog();
                showToast(msg);
            }
        }, CarApplyActivity.this));
    }

    private String[] getApplyPeo()
    {
        String[] peos = new String[departMentBeens.size()];
        for(int i = 0; i < departMentBeens.size(); i++)
        {
            peos[i] = departMentBeens.get(i).getAccountId();
        }
        return peos;
    }
}
