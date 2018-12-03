package com.cyztc.app.views.employee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.DepartMentBean;
import com.cyztc.app.bean.DepartMentSubBean;
import com.cyztc.app.bean.LeaveDetailBean;
import com.cyztc.app.httpservice.serviceapi.EmployeeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.employee.leave.adapter.DepartMentAdapter;
import com.cyztc.app.views.employee.leave.adapter.SubEmpleeAdapter;
import com.cyztc.app.widget.CustomListView;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import retrofit2.http.Body;

/**
 * Created by ywl on 2017-5-21.
 */

public class ChoiceDepartmentActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.sear_listview)
    ListView searListView;
//    @BindView(R.id.springview)
//    SpringView springView;

    private List<DepartMentBean> datas;
    private List<DepartMentBean> tempDatas;
    private List<DepartMentSubBean> peos;
    private DepartMentAdapter departMentAdapter;
    private SubEmpleeAdapter subempleeadapter;

    private List<DepartMentBean> partmentStack;

    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_department_layout);
        setTitle("选择部门");
        setBackView();
        setAdapter();

//        springView.setType(SpringView.Type.FOLLOW);
//        springView.setHeader(new DefaultHeader(this));
//        springView.setFooter(new DefaultFooter(this));
//        springView.setListener(new SpringView.OnFreshListener() {
//            @Override
//            public void onRefresh() {
//                if(!isLoading) {
//                    isLoading = true;
//                    getAutoris(CyApplication.getInstance().getUserBean().getAccountId());
//                }
//            }
//
//            @Override
//            public void onLoadmore() {
////                if(!isLoading) {
////                    getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), 1, subType, subName, index, pagesize);
////                }
//            }
//        });


        showDataLoadMsg("数据加载中");
        getAutoris(CyApplication.getInstance().getUserBean().getAccountId());
        searListView.setVisibility(View.GONE);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String name = s.toString().trim();
                if(!TextUtils.isEmpty(name)) {
                    List<DepartMentSubBean> departMentSubBeans = searchEmplee(s.toString().trim());
                    subempleeadapter = new SubEmpleeAdapter(ChoiceDepartmentActivity.this, departMentSubBeans);
                    searListView.setAdapter(subempleeadapter);
                    searListView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    searListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DepartMentSubBean departMentSubBean = (DepartMentSubBean) searListView.getItemAtPosition(position);
                            if(departMentSubBean != null)
                            {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("departMentSubBean", departMentSubBean);
                                intent.putExtras(bundle);
                                setResult(RESULT_OK, intent);
                                ChoiceDepartmentActivity.this.finish();
                            }
                        }
                    });
                }
                else
                {
                    searListView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        onBack();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void setAdapter()
    {
        partmentStack = new ArrayList<>();
        datas = new ArrayList<>();
        tempDatas = new ArrayList<>();
        peos = new ArrayList<>();
        departMentAdapter = new DepartMentAdapter(this, datas);
        listView.setAdapter(departMentAdapter);
        departMentAdapter.setOnDepartItemLisener(new DepartMentAdapter.OnDepartItemLisener() {
            @Override
            public void onDepartClick(DepartMentBean departMentBean) {
                if(departMentBean.getSubDept() == null && departMentBean.getEmployee() == null)
                    return;
                if(departMentBean.getEmployee().size() > 0 || departMentBean.getSubDept().size() > 0) {
                    datas.clear();
                    datas.add(departMentBean);
                    partmentStack.add(departMentBean);
                    departMentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onEmpleeClick(DepartMentSubBean departMentSubBean) {
                if(departMentSubBean != null)
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("departMentSubBean", departMentSubBean);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    ChoiceDepartmentActivity.this.finish();
                }
            }
        });
    }


    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, ChoiceDepartmentActivity.class);
        context.startActivity(intent);
    }

    private void getAutoris(String accountId)
    {
        EmployeeApi.getInstance().getAuditors(accountId, new HttpSubscriber<List<DepartMentBean>>(new SubscriberOnListener<List<DepartMentBean>>() {
            @Override
            public void onSucceed(List<DepartMentBean> data) {
                if(data != null && data.size() > 0)
                {
                    partmentStack.clear();
                    datas.clear();
                    tempDatas.clear();
                    DepartMentBean departMentBean = new DepartMentBean();
                    departMentBean.setSubDept(new ArrayList<DepartMentBean>());
                    departMentBean.getSubDept().addAll(data);
                    partmentStack.add(departMentBean);
                    datas.add(departMentBean);
                    tempDatas.add(departMentBean);
                    departMentAdapter.notifyDataSetChanged();
                    hideDataLoadMsg();
                    showPeo(datas);
                }
                else
                {
                    showDataLoadMsg("获取数据失败，请稍后再试");
                }
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadMsg("获取数据失败，请稍后再试");
                isLoading = false;
            }
        }, this));
    }

    private void onBack()
    {
        String s = etSearch.getText().toString().trim();
        if(TextUtils.isEmpty(s)) {
            if (partmentStack.size() == 0) {
                this.finish();
            } else {
                datas.clear();
                partmentStack.remove(partmentStack.size() - 1);
                if (partmentStack.size() == 0) {
                    this.finish();
                } else {
                    DepartMentBean departMentBean = partmentStack.get(partmentStack.size() - 1);
                    datas.add(departMentBean);
                    departMentAdapter.notifyDataSetChanged();
                }
            }
        }
        else
        {
            searListView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            etSearch.setText("");
        }
    }

//    private DepartMentSubBean searchEmplee(final List<DepartMentBean> departMentBeans, String name)
//    {
//        if(departMentBeans != null)
//        {
//            for(DepartMentBean departMentBean : departMentBeans)
//            {
//                int forcount = 0;
//                if(departMentBean.getEmployee() != null)
//                {
//                    for(DepartMentSubBean d : departMentBean.getEmployee())
//                    {
//                        forcount ++;
//                        if(d.getName().contains(name)) {
//                            return d;
//                        }
//                    }
//                }
//                return searchEmplee(departMentBean.getSubDept(), name);
//            }
//        }
//        return null;
//    }
    private List<DepartMentSubBean> searchEmplee(String name)
    {
        List<DepartMentSubBean> d = new ArrayList<>();
        if(peos != null)
        {
            for(DepartMentSubBean departMentSubBean : peos)
            {
                if(departMentSubBean.getName().contains(name))
                {
                    d.add(departMentSubBean);
                }
            }
        }
        return d;
    }

    private void showPeo(List<DepartMentBean> departMentBean)
    {
        if(departMentBean != null)
        {
            for(DepartMentBean departMent : departMentBean) {
                if (departMent.getEmployee() != null) {
                    for (DepartMentSubBean departMentSubBean : departMent.getEmployee()) {
                        peos.add(departMentSubBean);
                    }
                }
                if (departMent.getSubDept() != null) {
                    showPeo(departMent.getSubDept());
                }
            }
        }
    }


}
