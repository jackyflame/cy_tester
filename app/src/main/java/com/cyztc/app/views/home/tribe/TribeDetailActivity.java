package com.cyztc.app.views.home.tribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.AssessListBeanP;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.TribeDetailBean;
import com.cyztc.app.bean.TribeTopicBean;
import com.cyztc.app.bean.TribeTopicBeanP;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.TrainMainInfoActivity;
import com.cyztc.app.views.home.adapter.TribeDetailAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;
import com.google.gson.Gson;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by ywl on 2016/11/21.
 */

public class TribeDetailActivity extends BaseActivity{

    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout lySrf;
    @BindView(R.id.listview)
    ListView listView;
    ImageView ivbg;
    TextView tvName;
    TextView tvRemember;
    TextView tvTops;
    @BindView(R.id.ly_trans)
    LinearLayout lysystemparent;
    @BindView(R.id.ly_system_bar2)
    LinearLayout systembar2;
    @BindView(R.id.iv_publish)
    ImageView ivPublish;
    TextView tvCreateName;


    private List<TribeTopicBean> datas;
    private TribeDetailAdapter tribeDetailAdapter;
    private TribeDetailBean tribeDetailBean;

    private String tribeId = "";
    private String title = "";
    private View listveiwHeader;
    private View listviewFooter;
    private TextView tvMsg;
    private ImageView ivTribehead;
    private TextView tvJoin;

    private int currentIndex = 0;
    private int pagesize = 10;
    private boolean isNoMore = false;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tribedtail_layout);
        tribeId = getIntent().getStringExtra("tribeid");
        title = getIntent().getStringExtra("title");
        initSystembar(systembar2);
        setBackView(R.mipmap.icon_back);
        listviewFooter = LayoutInflater.from(this).inflate(R.layout.footer_main_listview_layout, null);
        listveiwHeader = LayoutInflater.from(this).inflate(R.layout.header_tribe_detail_layout, null);
        AutoUtils.auto(listveiwHeader);
        AutoUtils.auto(listviewFooter);
        tvMsg = (TextView) listviewFooter.findViewById(R.id.tv_load_msg);
        ivTribehead = (ImageView) listveiwHeader.findViewById(R.id.iv_tribe_head);
        ivbg = (ImageView) listveiwHeader.findViewById(R.id.iv_bg);
        tvJoin = (TextView) listveiwHeader.findViewById(R.id.tv_join);

        tvName = (TextView) listveiwHeader.findViewById(R.id.tv_name);
        tvRemember = (TextView) listveiwHeader.findViewById(R.id.tv_remb);
        tvTops = (TextView) listveiwHeader.findViewById(R.id.tv_tops);
        tvCreateName = (TextView) listveiwHeader.findViewById(R.id.tv_createname);


        listView.addHeaderView(listveiwHeader);
        listView.addFooterView(listviewFooter);

        ivTribehead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tribeDetailBean != null) {
                    TribeInfoActivity.startActivity(TribeDetailActivity.this, tribeId, tribeDetailBean.getCreator());
                }
            }
        });
        lySrf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    currentIndex = 0;
                    getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), tribeId, currentIndex, pagesize);
                }
            }
        });

        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadDialog("加入部落中");
                HomeApi.getInstance().joinTrible(CyApplication.getInstance().getUserBean().getStudentId(), tribeId, new HttpSubscriber<BaseBean>(new SubscriberOnListener<BaseBean>() {
                    @Override
                    public void onSucceed(BaseBean data) {
                        showToast("加入成功");
                        hideLoadDialog();
                        getTribeDetail(tribeId);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        showToast(msg);
                        hideLoadDialog();
                    }
                }, TribeDetailActivity.this));
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() == listView.getCount() - 1)
                {
                    if(!isLoading) {
                        if (!isNoMore) {
                            getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), tribeId, currentIndex, pagesize);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                float alpha = Math.abs(listveiwHeader.getTop() / 300f);
                lysystemparent.setAlpha(alpha);
                if(alpha > 0.8f)
                {
//                    setBackView(R.mipmap.icon_back);
                    setTitle(title);
                }
                else
                {
//                    setBackView(R.mipmap.icon_back_blue);
                    setTitle("");
                }
            }
        });

        setAdapter();
        getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), tribeId, currentIndex, pagesize);
        setBackView();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTribeDetail(tribeId);
    }

    @OnClick(R.id.iv_publish)
    public void onClickPublish(View view)
    {
        Intent intent = new Intent(this, TribePublishTopicActivity.class);
        intent.putExtra("trible", tribeId);
        this.startActivityForResult(intent, 0x1001);
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        showAskDialog("提示", "确定删除部落？", "删除", "取消", new NormalAskComplexDialog.OnDalogListener() {
            @Override
            public void onYes() {
                showLoadDialog("删除中");
                HomeApi.getInstance().deleteTribe(tribeId, CyApplication.getInstance().getUserBean().getStudentId(), new HttpSubscriber<Object>(new SubscriberOnListener() {
                    @Override
                    public void onSucceed(Object data) {
                        showToast("部落删除成功");
                        hideLoadDialog();
                        TribeDetailActivity.this.finish();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        showToast(msg);
                    }
                }, TribeDetailActivity.this));
            }

            @Override
            public void OnNo() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x1001 && resultCode == RESULT_OK)
        {
            if(!isLoading) {
                currentIndex = 0;
                getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), tribeId, currentIndex, pagesize);
            }
        }
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        tribeDetailAdapter = new TribeDetailAdapter(this, datas);
        listView.setAdapter(tribeDetailAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TribeTopicBean tribeTopicBean = (TribeTopicBean) listView.getItemAtPosition(position);
                if(tribeTopicBean != null)
                {
                    Gson gson = new Gson();
                    String topstr = gson.toJson(tribeTopicBean);
                    TribeCommentActivity.startActivity(TribeDetailActivity.this, topstr);
                }
            }
        });

    }

    public static void startActivity(Context context, String tribeId, String title)
    {
        Intent intent = new Intent(context, TribeDetailActivity.class);
        intent.putExtra("tribeid", tribeId);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    public void getTribeDetail(String tribeId)
    {
        HomeApi.getInstance().getTribeDetail(CyApplication.getInstance().getUserBean().getStudentId(), tribeId, new HttpSubscriber<TribeDetailBean>(new SubscriberOnListener<TribeDetailBean>() {
            @Override
            public void onSucceed(TribeDetailBean data) {
                tribeDetailBean = data;
                if(data != null)
                {
                    initViews(data);
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }

    public void getTribeList(String studentId, String tribleId, int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getTribeTopic(studentId, tribleId, start, rows, new HttpSubscriber<TribeTopicBeanP>(new SubscriberOnListener<TribeTopicBeanP>() {
            @Override
            public void onSucceed(TribeTopicBeanP data) {
                if(data != null && data.getData() != null)
                {
                    if(data.getData().size() < pagesize)
                    {
                        if(currentIndex == 0)
                        {
                            datas.clear();
                            datas.addAll(data.getData());
                            isNoMore = true;//没有更多了
                            if(data.getData().size() < 5) {
                                listviewFooter.setVisibility(View.GONE);
                            }
                            else
                            {
                                listviewFooter.setVisibility(View.VISIBLE);
                                tvMsg.setText("没有更多了");
                            }
                        }
                        else
                        {
                            datas.addAll(data.getData());
                            isNoMore = true;//没有更多了
                            listviewFooter.setVisibility(View.VISIBLE);
                            tvMsg.setText("没有更多了");
                        }
                    }
                    else
                    {
                        if(currentIndex == 0)
                        {
                            datas.clear();
                        }
                        isNoMore = false;
                        datas.addAll(data.getData());
                        currentIndex++;
                        listviewFooter.setVisibility(View.VISIBLE);
                        tvMsg.setText("加载更多");
                    }

//                    if(datas.size() == 0)
//                    {
//                        listviewFooter.setVisibility(View.GONE);
//                    }
//                    else
//                    {
//                        listviewFooter.setVisibility(View.VISIBLE);
//                    }
                }
                tribeDetailAdapter.notifyDataSetChanged();
                lySrf.setRefreshing(false);
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                lySrf.setRefreshing(false);
                isLoading = false;
            }
        }, this));
    }

    public void initViews(TribeDetailBean tribeDetailBean)
    {
        if(tribeDetailBean.getCoverPicture().endsWith(".png"))
        {
            Glide.with(this).load(HttpMethod.IMG_URL + tribeDetailBean.getCoverPicture()).bitmapTransform(new BlurTransformation(this, 20, 1)).into(ivbg);
        }
        else
        {
            ImageLoad.getInstance().displayImage(this, ivbg, HttpMethod.IMG_URL + tribeDetailBean.getCoverPicture(), 0, 0);
        }
        ImageLoad.getInstance().displayImage(this, ivTribehead, HttpMethod.IMG_URL + tribeDetailBean.getThumbnail(), 0, 0);
        tvName.setText(tribeDetailBean.getName());
        tvRemember.setText("成员：" + tribeDetailBean.getMemberNum());
        tvTops.setText("话题：" + tribeDetailBean.getTopicNum());
        tvCreateName.setText("创建者：" + tribeDetailBean.getCreatorName());
        if(tribeDetailBean.isMember())
        {
            ivPublish.setVisibility(View.VISIBLE);
            tvJoin.setVisibility(View.GONE);
            tvCreateName.setVisibility(View.VISIBLE);
        }
        else
        {
            ivPublish.setVisibility(View.GONE);
            tvJoin.setVisibility(View.VISIBLE);
            tvCreateName.setVisibility(View.GONE);
        }

        if(tribeDetailBean.getCreator().equals(CyApplication.getInstance().getUserBean().getStudentId()))
        {
            setTextMenuView("删除");
        }
    }
}
