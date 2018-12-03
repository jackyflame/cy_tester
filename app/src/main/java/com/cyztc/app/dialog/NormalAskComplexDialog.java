package com.cyztc.app.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;


/**
 * Created by ywl on 2016/6/16.
 */
public class NormalAskComplexDialog extends BaseDialog {

    @BindView(R.id.ly_root)
    LinearLayout lyRoot;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.no)
    Button no;
    @BindView(R.id.yes)
    Button yes;

    private boolean showTitle = false;


    public NormalAskComplexDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_normal_ask_complex_layout);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                {
                    listener.onYes();
                }
                dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                {
                    listener.OnNo();
                }
                dismiss();
            }
        });

        lyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        title.setText(titlemsg);
        message.setText(msg);
        no.setText(nomsg);
        yes.setText(yesmsg);
        if(showTitle)
        {
            title.setVisibility(View.VISIBLE);
        }


    }
    private String titlemsg="提示";
    private String msg="提示内容";
    private String yesmsg="确定";
    private String nomsg="取消";
    private OnDalogListener listener;

    public void setDialogUtil(String title,String message,String yes,String no,OnDalogListener listeners){
        listener=listeners;
        this.titlemsg=title;
        msg=message;
        yesmsg=yes;
        nomsg=no;
    }

    public void setDialogUtil(boolean showTitle, String title,String message,String yes,String no,OnDalogListener listeners){
        listener=listeners;
        this.titlemsg=title;
        msg=message;
        yesmsg=yes;
        nomsg=no;
        this.showTitle = showTitle;
    }
    public interface  OnDalogListener{
        void onYes();
        void OnNo();
    }
}
