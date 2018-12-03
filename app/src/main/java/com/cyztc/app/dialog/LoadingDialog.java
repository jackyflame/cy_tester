package com.cyztc.app.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;


/**
 * Created by ywl on 2016/5/19.
 */
public class LoadingDialog extends BaseDialog {

    @BindView(R.id.tv_load_msg)
    TextView tvLoadMsg;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;

    private AnimationDrawable animationDrawable;

    public LoadingDialog(Context context) {
        super(context, R.style.StyleDialogTransparent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        animationDrawable = (AnimationDrawable) ivLoading.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        animationDrawable.stop();
    }

    public void setLoadMsg(String msg) {
        if (tvLoadMsg != null) {
            tvLoadMsg.setText(msg);
        }
    }

}
