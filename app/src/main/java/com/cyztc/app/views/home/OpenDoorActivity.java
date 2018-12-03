package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.DoorBean;
import com.cyztc.app.bean.DoorStatueBean;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.views.home.adapter.DoorStatueAdapter;
import com.cyztc.app.widget.ywl5320.ColorProgressBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/21.
 */

public class OpenDoorActivity extends BaseActivity{


    @BindView(R.id.colorProgressbar)
    ColorProgressBarView colorProgressBarView;
    @BindView(R.id.rl_open_door)
    RelativeLayout rlOpenDoor;
    @BindView(R.id.iv_lock)
    ImageView ivLock;
    @BindView(R.id.tv_statue)
    TextView tvStatue;
    @BindView(R.id.listview)
    ListView listView;

    private List<DoorStatueBean> datas;
    private DoorStatueAdapter doorStatueAdapter;
    private boolean isAlive = true;

    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", -1);
        setContentView(R.layout.activity_open_door_layout);
        setTitle("门锁");
        setBackView();
        setAdapter();

        MyLog.d(CyApplication.getInstance().getUserBean().getDoors());
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        if(CyApplication.getInstance().isStudent())
        {
            DoorStatueBean doorStatueBean = new DoorStatueBean();
            doorStatueBean.setName("寝室门");
            doorStatueBean.setCode(CyApplication.getInstance().getUserBean().getRoomCode());
            doorStatueBean.setType(1);
            datas.add(doorStatueBean);
        }
        if(CyApplication.getInstance().getUserBean().getDoors() != null)
        {
            for(DoorBean doorBean : CyApplication.getInstance().getUserBean().getDoors())
            {
                DoorStatueBean doorStatueBean = new DoorStatueBean();
                doorStatueBean.setOpen(false);
                doorStatueBean.setType(doorBean.getDoorType());
                doorStatueBean.setCode(doorBean.getDoorCode());
                doorStatueBean.setName(doorBean.getDoorName());
                datas.add(doorStatueBean);
            }
        }

        doorStatueAdapter = new DoorStatueAdapter(this, datas);
        listView.setAdapter(doorStatueAdapter);
        doorStatueAdapter.setOnOpenListener(new DoorStatueAdapter.OnOpenListener() {
            @Override
            public void onOpen(boolean success) {
                if(success)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(isAlive) {
                                showToast("开门成功");
                            }
                        }
                    }, 5000);
                }
                else
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(isAlive) {
                                showToast("开门失败，请稍后再试");
                            }
                        }
                    }, 5000);
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
    protected void onDestroy() {
        super.onDestroy();
        isAlive = false;
    }

    public static void startActivity(Context context, int type)
    {
        Intent intent = new Intent(context, OpenDoorActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
