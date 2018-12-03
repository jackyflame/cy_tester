package com.cyztc.app.widget.ywl5320.pickphoto.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyztc.app.R;

/**
 * Created by ywl on 2016/6/14.
 */
public class ServiceTypeFixDialog extends BaseDialog{

    private RelativeLayout rlRoot;

    private Context context;

    private TextView tvCancel;
    private TextView tvClearn;
    private TextView tvChange;
    private TextView tvOther;

    private OnServiceTypeListener onServiceTypeListener;

    public ServiceTypeFixDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void setOnServiceTypeListener(OnServiceTypeListener onServiceTypeListener) {
        this.onServiceTypeListener = onServiceTypeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialot_fix_service_type_layout);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvClearn = (TextView) findViewById(R.id.tv_clearn);
        tvChange = (TextView) findViewById(R.id.tv_change);
        tvOther = (TextView) findViewById(R.id.tv_other);

        rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onServiceTypeListener != null)
                {
                    onServiceTypeListener.onTypeResult(2);
                }
                dismiss();
            }
        });

        tvClearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onServiceTypeListener != null)
                {
                    onServiceTypeListener.onTypeResult(1);
                }
                dismiss();
            }
        });

        tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onServiceTypeListener != null)
                {
                    onServiceTypeListener.onTypeResult(3);
                }
                dismiss();
            }
        });
    }

    public interface OnServiceTypeListener{
        void onTypeResult(int type);
    }
}
