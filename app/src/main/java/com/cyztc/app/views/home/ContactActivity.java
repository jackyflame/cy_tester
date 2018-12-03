package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.ContactBean;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.ListIndexUtil;
import com.cyztc.app.views.home.adapter.PhoneContactAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/12/19.
 */

public class ContactActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.layout)
    LinearLayout layoutIndex;
    @BindView(R.id.et_search)
    EditText metsearch;

    private ArrayList<ContactBean> mArrayContacts = new ArrayList<ContactBean>();
    private ArrayList<ContactBean> mArrayContacts2 = new ArrayList<ContactBean>();
    private ArrayList<ContactBean> sortDatas = new ArrayList<ContactBean>();
    private ArrayList<ContactBean> sortDatastemp = new ArrayList<ContactBean>();
    private HashMap<String, Integer> selector;// 存放含有索引字母的位置
    private PhoneContactAdapter phoneContactAdapter;
    private ListIndexUtil listIndexUtil = new ListIndexUtil();
    private String[] indexStr;
    private int height = 0;
    private boolean flag = false;

    private int ismain = 1;//1主课 0:历史

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_layout);
        ismain = getIntent().getIntExtra("ismain", 1);
        if(ismain == 1) {
            setTitle("通讯录");
        }
        else if(ismain == 0)
        {
            setTitle("历史通讯录");
        }
        setBackView();

        indexStr = listIndexUtil.getIndexStr();
        phoneContactAdapter = new PhoneContactAdapter(this, sortDatas);
        metsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString();
                searchContactBykey(key);
            }
        });
        listView.setAdapter(phoneContactAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactBean contactBean = (ContactBean) listView.getItemAtPosition(position);
                if(contactBean != null)
                {
                    Gson gson = new Gson();
                    ContactDetailActivity.startActivity(ContactActivity.this, gson.toJson(contactBean));
                }
            }
        });
        showDataLoadMsg("数据加载中");
        getAddressBook(CyApplication.getInstance().getUserBean().getAccountId(), ismain);

    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context, int ismain)
    {
        Intent intent = new Intent(context, ContactActivity.class);
        intent.putExtra("ismain", ismain);
        context.startActivity(intent);
    }

    public void getAddressBook(String accountId, int isMain)
    {
        UserApi.getInstance().getAddressBook(accountId, isMain, new HttpSubscriber<List<ContactBean>>(new SubscriberOnListener<List<ContactBean>>() {
            @Override
            public void onSucceed(List<ContactBean> data) {
                mArrayContacts = initRepeat(data);
                listIndexUtil.setData(mArrayContacts);
                sortDatas.clear();
                sortDatas.addAll(listIndexUtil.getDatas());
                sortDatastemp.addAll(listIndexUtil.getDatas());

                selector = listIndexUtil.getSelector();
                phoneContactAdapter.notifyDataSetChanged();
                if (!flag) {
                    height = layoutIndex.getMeasuredHeight() / indexStr.length;
                    drawIndexView();
                    flag = true;
                    if(sortDatas.size() > 10)
                        layoutIndex.setVisibility(View.VISIBLE);
                    else
                        layoutIndex.setVisibility(View.GONE);
                }
                hideDataLoadMsg();
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadWrongMsg(msg);
            }
        }, this));
    }

    public ArrayList<ContactBean> initRepeat(List<ContactBean> mArrayContacts)
    {
        int size = mArrayContacts.size();
        for(int i = 0; i < size; i++)
        {
            ContactBean contactBean = new ContactBean();
            contactBean.setWorkUnit(mArrayContacts.get(i).getWorkUnit());
            contactBean.setRoomPhoneNum(mArrayContacts.get(i).getRoomPhoneNum());
            contactBean.setBedRoom(mArrayContacts.get(i).getBedRoom());
            contactBean.setPhoto(mArrayContacts.get(i).getPhoto());
            contactBean.setPosition(mArrayContacts.get(i).getPosition());
            contactBean.setStudentName(mArrayContacts.get(i).getStudentName());
            contactBean.setPhoneNum(mArrayContacts.get(i).getPhoneNum());
            contactBean.setPinyin(listIndexUtil.getPinYin(mArrayContacts.get(i).getStudentName()));
            contactBean.setType(0);
            mArrayContacts2.add(contactBean);
        }

        for(int i = 0; i < size; i++)
        {
            int n = -1;
            for(int j = 0; j < size; j++)
            {
                if(mArrayContacts.get(i).getStudentName().equals(mArrayContacts2.get(j).getStudentName()))
                {
                    n++;
                    if(n > 0) {
                        mArrayContacts2.get(j).setPinyin(mArrayContacts2.get(j).getPinyin() + n);
                    }
                }
            }
        }
        return mArrayContacts2;
    }

    /**
     * 绘制索引条
     */
    public void drawIndexView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height);
        for (int i = 0; i < indexStr.length; i++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(params);
            tv.setText(indexStr[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(this.getResources().getColor(R.color.color_333333));
            tv.setTextSize(13);

            layoutIndex.addView(tv);

            layoutIndex.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float y = event.getY();
                    int index = (int) y / height;// 得到点击字母位置的索引
                    String key = "";
                    if (index < indexStr.length && index > -1) {
                        key = indexStr[index];
                        if (selector.containsKey(key)) {
                            int position = selector.get(key);
                            if (listView.getHeaderViewsCount() > 0) {// 防止ListView有标题栏。
                                listView.setSelectionFromTop(position
                                        + listView.getHeaderViewsCount(), 0);
                            } else {
                                listView.setSelectionFromTop(position, 0);// 滑动到第一项
                            }
                        }
                        if (key.equals("↑")) {
                            listView.setSelectionFromTop(0, 0);// 滑动到第一项
                        }
                    }
                    if (!key.equals("")) {
//                        tvMsg.setText(key);
//                        tvMsg.setVisibility(View.VISIBLE);
                    }

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_OUTSIDE:
//                            tvMsg.setVisibility(View.GONE);
                            break;
                        case MotionEvent.ACTION_DOWN:
                            // layoutIndex.setBackground();
                            break;
                    }

                    return true;
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void searchContactBykey(String key)
    {
        if(!TextUtils.isEmpty(key))
        {
            layoutIndex.setVisibility(View.GONE);
        }
        else
        {
            layoutIndex.setVisibility(View.VISIBLE);
        }
        int size = sortDatastemp.size();
        sortDatas.clear();
        for(int i = 0; i < size; i++)
        {
            if((!TextUtils.isEmpty(sortDatastemp.get(i).getStudentName()) && sortDatastemp.get(i).getStudentName().contains(key)) || (!TextUtils.isEmpty(sortDatastemp.get(i).getPhoneNum()) && sortDatastemp.get(i).getPhoneNum().contains(key)) || (!TextUtils.isEmpty(sortDatastemp.get(i).getPinyin()) && sortDatastemp.get(i).getPinyin().contains(key)))
            {
                ContactBean contactBean = sortDatastemp.get(i);
                sortDatas.add(contactBean);
            }
        }
        phoneContactAdapter.notifyDataSetChanged();
    }
}
