package com.cyztc.app.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.log.MyLog;

import butterknife.OnClick;

/**
 * Created by ywl on 2017/1/4.
 */

public class PayDialog extends BaseDialog{

    private TextView tvOffline;
    private PayListener paylistener;

    public PayDialog(Context context) {
        super(context, R.style.StyleDialog);
        this.context = context;
    }

    public void setPaylistener(PayListener paylistener) {
        this.paylistener = paylistener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_layout);
        tvOffline = (TextView) findViewById(R.id.btn_offline);
    }

    public void setShowOffline()
    {
        if(tvOffline != null)
        {
            tvOffline.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_alipay)
    public void onClickAlipay(View view)
    {
        if(paylistener != null)
        {
            paylistener.onPay(1);
        }
        dismiss();
    }

    @OnClick(R.id.btn_wxpay)
    public void onClickWxPay(View viw)
    {
        if(paylistener != null)
        {
            paylistener.onPay(2);
        }
        dismiss();
    }

    @OnClick(R.id.btn_offline)
    public void onClickOffline(View view)
    {
        if(paylistener != null)
        {
            paylistener.onPay(3);
        }
    }

    @OnClick(R.id.tv_cancel)
    public void onClickCancel(View view)
    {
        dismiss();
    }

    @OnClick(R.id.rl_root)
    public void onClickClose(View veiw)
    {
        dismiss();
    }

    public interface PayListener
    {
        /**
         * 1：支付宝
         * 2：微信
         * 3：线下
         * @param paytype
         */
        void onPay(int paytype);
    }
}
