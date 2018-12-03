package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ListView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.ExamDetailBean;
import com.cyztc.app.bean.QuestionBean;
import com.cyztc.app.bean.QuestionOptionsBean;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.adapter.ExamDetailAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by ywl on 2016/12/26.
 */

public class TrainLevelDetailActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;

    private int type;
    private String examId;
    private String targetId;

    private ExamDetailAdapter examDetailAdapter;
    private List<QuestionBean> questionBeens;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_level_detail_layout);
        type = getIntent().getIntExtra("type", -1);
        targetId = getIntent().getStringExtra("id");
        setBackView();
        setTitle("评估测试");
        setTextMenuView("提交");
        setAdapter();
        getExamDetail(type);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        String [] ans = getAnswers();
        int length = ans.length;
        for(int i = 0; i < length; i++)
        {
            if(TextUtils.isEmpty(ans[i]))
            {
                showToast("第" + (i + 1) + "道问题还没有选择答案，不能提交");
                return;
            }
        }
        MyLog.d(getAnswers());
        showLoadDialog("提交中");
        subAnsw(getAnswers(), examId, CyApplication.getInstance().getUserBean().getStudentId(), targetId);
    }

    public void setAdapter()
    {
        questionBeens = new ArrayList<>();
        examDetailAdapter = new ExamDetailAdapter(this, questionBeens);
        listView.setAdapter(examDetailAdapter);
    }

    public void getExamDetail(int type)
    {
        HomeApi.getInstance().getExamDetail(type, new HttpSubscriber<ExamDetailBean>(new SubscriberOnListener<ExamDetailBean>() {
            @Override
            public void onSucceed(ExamDetailBean data) {
                if(data != null && data.getQuestions() != null)
                {
                    questionBeens.addAll(data.getQuestions());
                    examDetailAdapter.notifyDataSetChanged();
                    examId = data.getId();
                }
                hideDataLoadMsg();
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadMsg(msg);
            }
        }, this));
    }

    public static void startActivity(Context context, int type, String id)
    {
        Intent intent = new Intent(context, TrainLevelDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    public String[] getAnswers()
    {
        int size = questionBeens.size();
        String [] answers = new String[size];

        for(int i = 0; i < size; i++)
        {
            String an = "";
            for(QuestionOptionsBean questionOptionsBean : questionBeens.get(i).getOptions())
            {
                if(questionOptionsBean.isSelected())
                {
                    an += questionOptionsBean.getItemKey();
                }
            }
            answers[i] = an;
        }
        return answers;
    }

    public void subAnsw(String[] answer, String examId, String studentId, String targetId)
    {
        HomeApi.getInstance().subExamAnswer(answer, examId, studentId, targetId, new HttpSubscriber<BaseBean>(new SubscriberOnListener<BaseBean>() {
            @Override
            public void onSucceed(BaseBean data) {
                showToast("提交评估问卷成功");
                hideLoadDialog();
                TrainLevelDetailActivity.this.finish();
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
                hideLoadDialog();
            }
        }, this));
    }
}
