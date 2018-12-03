package com.cyztc.app.views.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.bean.ContactBean;
import com.cyztc.app.bean.UserBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.pay.PayUtils;
import com.cyztc.app.views.employee.EatActivity;
import com.cyztc.app.views.grades.GradesActivity;
import com.cyztc.app.views.grades.TimeTableActivity;
import com.cyztc.app.views.home.ClassEnlistActivity;
import com.cyztc.app.views.home.ContactActivity;
import com.cyztc.app.views.home.ContactDetailActivity;
import com.cyztc.app.views.home.DaibiaoKnowActivity;
import com.cyztc.app.views.home.DaibiaoNameActivity;
import com.cyztc.app.views.home.EmSigninActivity;
import com.cyztc.app.views.home.OpenDoorActivity;
import com.cyztc.app.views.home.PayForActivity;
import com.cyztc.app.views.home.SeatActivity;
import com.cyztc.app.views.home.SigninActivity;
import com.cyztc.app.views.home.TrainHistoryActivity;
import com.cyztc.app.views.home.TrainLevelActivity;
import com.cyztc.app.views.home.UsedPhoneActivity;
import com.cyztc.app.views.home.adapter.PhoneContactAdapter;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/11/2.
 */

public class MeFragment extends BaseFragment{

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_name)
    TextView tvName;
//    @BindView(R.id.iv_userinfo)
//    ImageView ivUserInfo;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.ly_s_menu)
    LinearLayout lySMenu;
    @BindView(R.id.ly_list)
    LinearLayout lyList;
    @BindView(R.id.ly_em_list)
    LinearLayout lyEmList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_layout);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(CyApplication.getInstance().isLogin())
        {
            tvLogin.setVisibility(View.GONE);
            tvName.setText(CyApplication.getInstance().getUserBean().getStudentName());
//            ivUserInfo.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.VISIBLE);
            if(CyApplication.getInstance().getUserBean() != null) {
                ImageLoad.getInstance().displayCircleImage(getActivity(), ivHead, HttpMethod.IMG_URL + CyApplication.getInstance().getUserBean().getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
            }
        }
        else
        {
            tvLogin.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.GONE);
//            ivUserInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle("个人");

        if(CyApplication.getInstance().isStudent())
        {
            lySMenu.setVisibility(View.VISIBLE);
            lyList.setVisibility(View.VISIBLE);
            lyEmList.setVisibility(View.GONE);
        }
        else
        {
            lySMenu.setVisibility(View.GONE);
            lyList.setVisibility(View.GONE);
            lyEmList.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.iv_head)
    public void onClickHead(View view)
    {
//        SettingActivity.startActivity(getActivity());
//        UpdateUserInfoActivity.startActivity(getActivity());
        if(CyApplication.getInstance().getUserBean() != null) {
            UserBean userBean = CyApplication.getInstance().getUserBean();
            ContactBean contactBean = new ContactBean();
            contactBean.setPhoto(userBean.getPhoto());
            contactBean.setRoomPhoneNum(userBean.getBedRoom());
            contactBean.setWorkUnit(userBean.getWorkUnit());
            contactBean.setTrainInfoName(userBean.getTrainingInfoName());
            contactBean.setPhoneNum(userBean.getPhone());
//            contactBean.setRoomCode(userBean.getRoomCode());
            contactBean.setPosition(userBean.getPosition());
            contactBean.setStudentName(userBean.getStudentName());
            if (contactBean != null) {
                Gson gson = new Gson();
                ContactDetailActivity.startActivity(getActivity(), gson.toJson(contactBean));
            }
        }
    }

    @OnClick(R.id.tv_login)
    public void onClickLogin(View v)
    {
        LoginActivity.startActivity(getActivity());
    }

    @OnClick(R.id.ly_contact)
    public void onClickContact(View view)
    {
        ContactActivity.startActivity(getActivity(), 1);
    }

    @OnClick(R.id.ly_score)
    public void onClickScore(View view)
    {
        TimeTableActivity.startActivity(getActivity());
    }

    @OnClick(R.id.ly_train_level)
    public void onClickLevel(View view)
    {
//        TrainLevelActivity.startActivity(getActivity());
        GradesActivity.startActivity(getActivity(), 1, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
    }

    @OnClick(R.id.ly_manag)
    public void onClickManag(View view)
    {
        //TrainLevelActivity.startActivity(getActivity());
    }

    @OnClick(R.id.ly_signin)
    public void onClickSignin(View view)
    {
        SigninActivity.startActivity(getActivity(), 0);
//        EmSigninActivity.startActivity(getActivity(), 1);
    }


    @OnClick(R.id.ly_history)
    public void onClickHistory(View view)
    {
        TrainHistoryActivity.startActivity(getActivity());
    }

    @OnClick(R.id.rl_payfor)
    public void onClickPayfor(View view)
    {
//        PayForActivity.startActivity(getActivity());
        ClassEnlistActivity.startActivity(getActivity());
    }


    @OnClick(R.id.rl_open_door)
    public void onClickOpenDoor(View view)
    {
        OpenDoorActivity.startActivity(getActivity(), 0);
//        PayUtils.alpay(getActivity(), "", "");
    }

    @OnClick(R.id.rl_meet_day)
    public void onClickMeetDay(View veiw)
    {
        TimeTableActivity.startActivity(getActivity());
    }

    @OnClick(R.id.rl_daibiao)
    public void onClickDaibiao(View veiw)
    {
        DaibiaoKnowActivity.startActivity(getActivity());
    }

    @OnClick(R.id.rl_getphone)
    public void onClickPhone(View veiw)
    {
        UsedPhoneActivity.startActivity(getActivity());
    }

    @OnClick(R.id.rl_meetname)
    public void onClickMeetName(View veiw)
    {
        DaibiaoNameActivity.startActivity(getActivity());
    }

    @OnClick(R.id.rl_zuociap)
    public void onClickSeat(View veiw)
    {
        SeatActivity.startActivity(getActivity());
    }

    @OnClick(R.id.rl_changepwd)
    public void onClickChangePwd(View veiw)
    {
//        UpdatePwdActivity.startActivity(getActivity());
        SettingActivity.startActivity(getActivity());
    }

    @OnClick(R.id.rl_resetpwd)
    public void onClickResetPwd(View veiw)
    {
        ResetPwdActivity.startActivity(getActivity());
    }

//    @OnClick(R.id.ly_updateinfo)
//    public void onClickUpdateInfo(View view)
//    {
//        UpdateUserInfoActivity.startActivity(getActivity());
//    }

    @OnClick(R.id.rl_emphones)
    public void onClickEmPhones(View view)
    {
        ContactActivity.startActivity(getActivity(), 1);
    }

    @OnClick(R.id.rl_work)
    public void onClickWork(View view)
    {
        EmSigninActivity.startActivity(getActivity(), 1);
    }

    @OnClick(R.id.rl_eat)
    public void onClickEat(View view)
    {
        EatActivity.startActivit(getActivity());
    }

}
