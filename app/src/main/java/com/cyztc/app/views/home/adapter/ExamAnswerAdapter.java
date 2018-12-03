package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.QuestionBean;
import com.cyztc.app.bean.QuestionOptionsBean;
import com.cyztc.app.log.MyLog;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/26.
 */

public class ExamAnswerAdapter extends BaseDataAdapter{


    private Map<String, String> answers;

    public ExamAnswerAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionBean questionBean = (QuestionBean) mDatas.get(position);
        ViewHolder viewHolder;
        List<View> views = null;
        if(views == null)
        {
            views = new ArrayList<>();
        }
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_exam_adapter_detail_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            AutoUtils.auto(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(questionBean.isMulti())
        {
            viewHolder.tvTitle.setText((position + 1) + "、" + questionBean.getTitle() + "--多选");
        }
        else
        {
            viewHolder.tvTitle.setText((position + 1) + "、" + questionBean.getTitle());
        }

        MyLog.d("key:" + answers.get(position + ""));
        viewHolder.lyOption.removeAllViews();
        for(QuestionOptionsBean questionOptionsBean : questionBean.getOptions())
        {
            View view = mlayoutInflate.inflate(R.layout.item_exam_options_layout, null);
            AutoUtils.auto(view);
            TextView tvname = (TextView) view.findViewById(R.id.tv_name);
            ImageView ivimg = (ImageView) view.findViewById(R.id.iv_img);
            tvname.setText(questionOptionsBean.getContent());

            String ans = answers.get(position + "");
            if(!TextUtils.isEmpty(ans))
            {
                int length = ans.length();
                for(int i = 0; i < length; i++)
                {
                    if(String.valueOf(ans.charAt(i)).equals(questionOptionsBean.getItemKey()))
                    {
                        questionOptionsBean.setSelected(true);
                    }
                }
            }

            if(questionOptionsBean.getItemKey().equals("A"))
            {
                ivimg.setImageResource(R.drawable.exam_a_selector);
            }
            else if(questionOptionsBean.getItemKey().equals("B"))
            {
                ivimg.setImageResource(R.drawable.exam_b_selector);
            }
            else if(questionOptionsBean.getItemKey().equals("C"))
            {
                ivimg.setImageResource(R.drawable.exam_c_selector);
            }
            else if(questionOptionsBean.getItemKey().equals("D"))
            {
                ivimg.setImageResource(R.drawable.exam_d_selector);
            }
            else if(questionOptionsBean.getItemKey().equals("E"))
            {
                ivimg.setImageResource(R.drawable.exam_e_selector);
            }
            else if(questionOptionsBean.getItemKey().equals("F"))
            {
                ivimg.setImageResource(R.drawable.exam_f_selector);
            }
            else if(questionOptionsBean.getItemKey().equals("G"))
            {
                ivimg.setImageResource(R.drawable.exam_g_selector);
            }
            else if(questionOptionsBean.getItemKey().equals("H"))
            {
                ivimg.setImageResource(R.drawable.exam_h_selector);
            }
            else if(questionOptionsBean.getItemKey().equals("I"))
            {
                ivimg.setImageResource(R.drawable.exam_i_selector);
            }

            if(questionOptionsBean.isSelected())
            {
                ivimg.setSelected(true);
            }
            else
            {
                ivimg.setSelected(false);
            }
            views.add(view);
            viewHolder.lyOption.addView(view);
        }
//        actionsViews(views, questionBean.getOptions(), questionBean.isMulti());
        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.ly_options)
        LinearLayout lyOption;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void actionsViews(final List<View> views, final List<QuestionOptionsBean> questionOptionsBeens, final boolean ismulit)
    {
        final int size = questionOptionsBeens.size();
        for (int i = 0; i < size; i++) {
            final int po = i;
                views.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ismulit) {
                            questionOptionsBeens.get(po).setSelected(!questionOptionsBeens.get(po).isSelected());
                            views.get(po).findViewById(R.id.iv_img).setSelected(questionOptionsBeens.get(po).isSelected());
                        }
                        else
                        {
                            setSingalSelected(questionOptionsBeens.get(po), questionOptionsBeens);
                        }
                        notifyDataSetChanged();
                    }
                });
        }

    }

    public void setSingalSelected(QuestionOptionsBean questionOptionsBean, List<QuestionOptionsBean> questionOptionsBeens)
    {
        for(QuestionOptionsBean q : questionOptionsBeens)
        {
            if(q.getId().equals(questionOptionsBean.getId()))
            {
                q.setSelected(!q.isSelected());
            }
            else {
                q.setSelected(false);
            }
        }
    }
}
