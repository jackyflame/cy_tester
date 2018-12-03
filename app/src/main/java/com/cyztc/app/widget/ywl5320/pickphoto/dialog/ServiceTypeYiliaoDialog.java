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
public class ServiceTypeYiliaoDialog extends BaseDialog{

    private RelativeLayout rlRoot;

    private Context context;

    private TextView tvCancel;
    private TextView tvDoor;
    private TextView tvTea;
    private TextView tvNeed;
    private TextView tvOther;

    private OnServiceTypeListener onServiceTypeListener;

    public ServiceTypeYiliaoDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void setOnServiceTypeListener(OnServiceTypeListener onServiceTypeListener) {
        this.onServiceTypeListener = onServiceTypeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialot_yiliao_service_type_layout);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        tvDoor = (TextView) findViewById(R.id.tv_door);
        tvTea = (TextView) findViewById(R.id.tv_tea);
        tvNeed = (TextView) findViewById(R.id.tv_need);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
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

        tvDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onServiceTypeListener != null)
                {
                    onServiceTypeListener.onTypeResult(1);
                }
                dismiss();
            }
        });

        tvTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onServiceTypeListener != null)
                {
                    onServiceTypeListener.onTypeResult(2);
                }
                dismiss();
            }
        });

        tvNeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onServiceTypeListener != null)
                {
                    onServiceTypeListener.onTypeResult(3);
                }
                dismiss();
            }
        });

        tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onServiceTypeListener != null)
                {
                    onServiceTypeListener.onTypeResult(4);
                }
                dismiss();
            }
        });
    }

    public interface OnServiceTypeListener{
        void onTypeResult(int type);
    }
}
