package com.cyztc.app.views.knowledge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.bean.VideoBean;
import com.cyztc.app.bean.VideoListBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.BookApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.knowledge.adapter.VideoListAdapter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/11/2.
 */

public class KnowFragment extends BaseFragment{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private LinearLayout lyVideo;
    private LinearLayout lyEbook;
    private LinearLayout lyTushu;
    private LinearLayout lyAudio;

    private LayoutInflater headerInflater;
    private View headerView;
    private View footerVlew;
    private TextView tvMore;

    private List<VideoBean> datas;
    private VideoListAdapter videoListAdapter;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;
    private int type = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_know_layout);
        headerInflater = LayoutInflater.from(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle("智库");
        headerView = headerInflater.inflate(R.layout.header_know_layout, null);
        footerVlew = headerInflater.inflate(R.layout.footer_know_layout, null);
        tvMore = (TextView) footerVlew.findViewById(R.id.tv_more);
        lyVideo = (LinearLayout) headerView.findViewById(R.id.ly_video);
        lyEbook = (LinearLayout) headerView.findViewById(R.id.ly_ebook);
        lyTushu = (LinearLayout) headerView.findViewById(R.id.ly_tushu);
        lyAudio = (LinearLayout) headerView.findViewById(R.id.ly_audio);

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orgtype = 0;
                if(type == 1)
                    orgtype = 3;
                else if(type == 2)
                    orgtype = 4;
                else if(type == 3)
                    orgtype = 5;
                VideoActivity.startActivity(getActivity(), type, "", orgtype);
            }
        });

        lyVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 3;
                if(!isLoading) {
                    index = 0;
                    showLoadDialog("加载中");
                    videoListAdapter.setType(type);
                    getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, "");
                }
            }
        });

        lyEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                if(!isLoading) {
                    index = 0;
                    showLoadDialog("加载中");
                    videoListAdapter.setType(type);
                    getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, "");
                }
            }
        });

        lyTushu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListActivity.startActivity(getActivity());
            }
        });

        lyAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                if(!isLoading) {
                    index = 0;
                    showLoadDialog("加载中");
                    videoListAdapter.setType(type);
                    getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, "");
                }
            }
        });


        listView.addHeaderView(headerView);
        listView.addFooterView(footerVlew);

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(getActivity()));
//        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, "");
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, "");
                }
            }
        });
        showDataLoadMsg("数据加载中");
        setAdapter();
        if(CyApplication.getInstance().getUserBean() != null) {
            getVideoList(CyApplication.getInstance().getUserBean().getStudentId(), type, "");
        }
        else
        {
            MyLog.d("user is null");
        }
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        videoListAdapter = new VideoListAdapter(getActivity(), datas);
        listView.setAdapter(videoListAdapter);
        videoListAdapter.setType(type);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoBean videoBean = (VideoBean) listView.getItemAtPosition(position);
                if(videoBean != null)
                {
                    if(type == 2 || type == 3) {
                        if(!TextUtils.isEmpty(videoBean.getFile()))
                        {
                            if(videoBean.getFile().toLowerCase().endsWith(".mp4") || videoBean.getFile().toLowerCase().endsWith(".mp3"))
                            {
                                PlayActivity.startActivity(getActivity(), videoBean.getId(), HttpMethod.IMG_URL + videoBean.getFile(), videoBean.getTitle(), videoBean.getRemark(), videoBean.getPicture());
                            }
                            else
                            {
                                NetFileDetailActivity.startActivity(getActivity(), videoBean.getId());
                            }
                        }
                        else
                        {
                            showToast("文件无效");
                        }
                    }
                    else if(type == 1)
                    {
                        EbookDetailActivity.startActivity(getActivity(), videoBean.getId());
                    }
                }
            }
        });
    }

    public void getVideoList(String stucentId, int type, String subtype )
    {
        BookApi.getInstance().getVideoList(stucentId, type, subtype, "", index, pagesize, new HttpSubscriber<VideoListBean>(new SubscriberOnListener<VideoListBean>() {
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
                hideDataLoadMsg();
                hideLoadDialog();
                if(datas.size() == 0)
                {
                    showToast("没有相关数据");
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(datas.size() == 0) {
                    showDataLoadWrongMsg(msg);
                    showToast(msg);
                }
                hideDataLoadMsg();
                hideLoadDialog();
                isLoading = false;
            }
        }, getActivity()));
    }

}
