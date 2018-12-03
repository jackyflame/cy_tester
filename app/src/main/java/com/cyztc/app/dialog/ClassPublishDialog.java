package com.cyztc.app.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.BaseDialog;

/**
 * Created by ywl on 2016/6/14.
 */
public class ClassPublishDialog extends BaseDialog {

    private RelativeLayout rlRoot;

    private Context context;

    private TextView tvCancel;
    private TextView tvXxgw;
    private TextView tvBjxx;
    private TextView tvBjgl;
    private TextView tvBjhd;
    private TextView tvXczc;

    private OnClassTypeListener onClassTypeListener;

    public ClassPublishDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void setOnClassTypeListener(OnClassTypeListener onClassTypeListener) {
        this.onClassTypeListener = onClassTypeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choice_classtype_layout);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        tvXxgw = (TextView) findViewById(R.id.btn_xxgw);
        tvBjxx = (TextView) findViewById(R.id.btn_bjxx);
        tvBjgl = (TextView) findViewById(R.id.btn_bjgl);
        tvBjhd = (TextView) findViewById(R.id.btn_bjhd);
        tvXczc = (TextView) findViewById(R.id.btn_xxzc);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

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

        tvXxgw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClassTypeListener != null)
                {
                    onClassTypeListener.onTypeResult(1);
                }
            }
        });

        tvBjxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClassTypeListener != null)
                {
                    onClassTypeListener.onTypeResult(2);
                }
            }
        });

        tvBjgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClassTypeListener != null)
                {
                    onClassTypeListener.onTypeResult(3);
                }
            }
        });

        tvBjhd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClassTypeListener != null)
                {
                    onClassTypeListener.onTypeResult(4);
                }
            }
        });

        tvXczc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClassTypeListener != null)
                {
                    onClassTypeListener.onTypeResult(5);
                }
            }
        });


    }

    public interface OnClassTypeListener{
        void onTypeResult(int type);
    }
}
