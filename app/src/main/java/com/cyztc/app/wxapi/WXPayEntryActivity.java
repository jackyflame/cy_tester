package com.cyztc.app.wxapi;


import com.cyztc.app.CyApplication;
import com.cyztc.app.log.MyLog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, "wx57fb538196f4f157");
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == 0)
			{
				if(CyApplication.getInstance() != null && CyApplication.getInstance().getOnPayListener() != null)
				{
					CyApplication.getInstance().getOnPayListener().onPayResult(0, "支付成功");
				}
				MyLog.d("支付成功");
				finish();
			}
			else if(resp.errCode == -1)
			{
				if(CyApplication.getInstance() != null && CyApplication.getInstance().getOnPayListener() != null)
				{
					CyApplication.getInstance().getOnPayListener().onPayResult(-1, "支付出错：" + resp.errStr);
				}
				MyLog.d("支付出错：" + resp.errStr);
				finish();
			}
			else if(resp.errCode == -2)
			{
				if(CyApplication.getInstance() != null && CyApplication.getInstance().getOnPayListener() != null)
				{
					CyApplication.getInstance().getOnPayListener().onPayResult(-2, "取消支付");
				}
				MyLog.d("取消支付");
				finish();
			}
		}
	}
}