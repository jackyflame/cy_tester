package com.cyztc.app.views.knowledge;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BookListBean;
import com.cyztc.app.bean.TypeBean;
import com.cyztc.app.bean.TypeBeanP;
import com.cyztc.app.bean.VideoBean;
import com.cyztc.app.bean.VideoListBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.BookApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.views.knowledge.adapter.TypeAdapter;
import com.cyztc.app.views.knowledge.adapter.VideoListAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/11/9.
 */

public class VideoActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.typelist)
    ListView typelist;
    @BindView(R.id.v_bg)
    View vBg;

    private int type = 0;
    private String subType = "";
    private int orgtype = 0;
    private int listviewWidth = 0;

    private List<VideoBean> datas;
    private VideoListAdapter videoListAdapter;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    private boolean isShowListView = false;

    private TypeAdapter typeAdapter;
    private List<TypeBean> typeBeens;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_layout);
        type = getIntent().getIntExtra("type", 0);
        subType = getIntent().getStringExtra("subType");
        orgtype = getIntent().getIntExtra("orgtype", 0);

        setBackView();
        setTextMenuView("分类");

        if(type == 3)
        {
            setTitle("视频");
        }
        else if(type == 1)
        {
            setTitle("电子书");
        }
        else if(type == 2)
        {
            setTitle("音频");
        }


        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, subType, "", index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, subType, "", index, pagesize);
                }
            }
        });

        listviewWidth = (int)(CommonUtil.getScreenWidth(this) * 0.6f);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(listviewWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        typelist.setLayoutParams(lp);
        typelist.setVisibility(View.INVISIBLE);
        hideFloor(typelist);
        setTypeAdapter();
        setAdapter();
        showDataLoadMsg("数据加载中");
        getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, subType, "", index, pagesize);

        searchTypes(orgtype, 0, 50);
    }

    public void setTypeAdapter()
    {
        typeBeens = new ArrayList<>();
        TypeBean typeBean = new TypeBean();
        typeBean.setSearchType("全部");
        typeBeens.add(typeBean);
        typeAdapter = new TypeAdapter(this, typeBeens);
        typelist.setAdapter(typeAdapter);
        typelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TypeBean typeBean = (TypeBean) typelist.getItemAtPosition(position);
                if(typeBean != null)
                {
                    if(!isLoading) {
                        hideFloor(typelist);
                        showDataLoadMsg("数据加载中");
                        subType = typeBean.getSearchType();
                        if("全部".equals(subType))
                        {
                            subType = "";
                        }
                        index = 0;
                        getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, subType, "", index, pagesize);
                    }
                }
            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        if(typeBeens.size() == 0)
        {
            showToast("没有类型可选");
            return;
        }
        if(isShowListView)
        {
            hideFloor(typelist);
        }
        else {
            showFloor(typelist);
        }
    }

    @Override
    public void onClickMenu() {
        super.onClickMenu();

    }

    public void showFloor(final View v) {
        isShowListView = true;
        typelist.setVisibility(View.VISIBLE);
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(listviewWidth, 0);
        animator.setTarget(v);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationX((Float) animation.getAnimatedValue());
            }
        });

        vBg.animate().alpha(1).setDuration(200);
        vBg.setVisibility(View.VISIBLE);
    }

    public void hideFloor(final View v)
    {
        isShowListView = false;
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(0, listviewWidth);
        animator.setTarget(v);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        vBg.animate().alpha(0).setDuration(200);
        vBg.setVisibility(View.GONE);

    }

    private void setAdapter()
    {
        datas = new ArrayList<>();
        videoListAdapter = new VideoListAdapter(this, datas);
        videoListAdapter.setType(type);
        listView.setAdapter(videoListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoBean videoBean = (VideoBean) listView.getItemAtPosition(position);
                if(videoBean != null)
                {
                    if(type == 2 || type == 3) {
//                        VideoDetailActivity.startActivity(VideoActivity.this, videoBean.getId(), HttpMethod.IMG_URL + videoBean.getFile(), videoBean.getTitle(), videoBean.getRemark());
                        PlayActivity.startActivity(VideoActivity.this, videoBean.getId(), HttpMethod.IMG_URL + videoBean.getFile(), videoBean.getTitle(), videoBean.getRemark(), videoBean.getPicture());
                    }
                    else if(type == 1)
                    {
                        EbookDetailActivity.startActivity(VideoActivity.this, videoBean.getId());
                    }
                }
            }
        });
    }

    public static void startActivity(Context context, int type, String subType, int orgtype)
    {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("subType", subType);
        intent.putExtra("orgtype", orgtype);
        context.startActivity(intent);
    }

    public void getVideoList(String studentId, int type, String subtype, String title, int start, int rows)
    {
        isLoading = true;
        BookApi.getInstance().getVideoList(studentId, type, subtype, title, start, rows, new HttpSubscriber<VideoListBean>(new SubscriberOnListener<VideoListBean>() {
            @Override
            public void onSucceed(VideoListBean data) {

                if(data != null && data.getData() != null)
                {
                    if(index == 0)
                    {
                        datas.clear();
                    }
                    if(data.getData().size() < pagesize && index != 0)
                    {
                        showToast("没有更多了");
                    }
                    if(data.getData().size() != 0 && data.getData().size() <= pagesize)
                    {
                        index++;
                    }
                    if(data.getData().size() != 0) {
                        datas.addAll(data.getData());
                        videoListAdapter.notifyDataSetChanged();
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
                hideDataLoadMsg();

            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(datas.size() == 0) {
                    showDataLoadWrongMsg(msg);
                }
                hideDataLoadMsg();
                isLoading = false;
            }
        }, this));
    }

    public void searchTypes(int type, int star, int rows)
    {
        BookApi.getInstance().searchTypes(type, star, rows, new HttpSubscriber<TypeBeanP>(new SubscriberOnListener<TypeBeanP>() {
            @Override
            public void onSucceed(TypeBeanP data) {
                if(data != null && data.getData() != null)
                {
                    if(data.getData().size() > 0) {
                        typeBeens.addAll(data.getData());
                        typeAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        showDataLoadWrongMsg("此类型没有数据");
                    }
                }
                else
                {
                    showDataLoadWrongMsg("访问数据失败");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadWrongMsg(msg);
            }
        }, this));
    }
}
