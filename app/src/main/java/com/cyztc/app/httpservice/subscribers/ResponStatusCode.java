package com.cyztc.app.httpservice.subscribers;

/**
 * Created by ywl on 2016/6/21.
 */
public class ResponStatusCode {

    /**
     * 判断code 是否成功
     * @param code
     * @return
     */
    public static String getCode(int code, String msg){
        if(code == 1){
            return "1";
        }
        else if(code == -1){
            return "参数不合法";
        }else if(code == -2){
            return "签名不合法";
        }else if(code == -3){
            return "时间戳不合法";
        }else if(code == -4){
            return "请求已超时";
        }else if(code == -9){
            return "没有登录";
        }else if(code == -10){
            return "缺少参数gameid";
        }else if(code == -11){
            return "缺少参数phone";
        }else if(code == -12){
            return "缺少短信模板参数template";
        }else if(code == -13){
            return "缺少动态密码参数dynamicPwd";
        }else if(code == -14){
            return "手机号码格式不正确";
        }else if(code == -17){
            return "手机发送验证码达到每日上线";
        }else if(code == -18){
            return "手机发送验证码过于频繁";
        }else if(code == -19){
            return "手机动态密码错误";
        }else if(code == -20){
            return "密码错误";
        }else if(code == -21){
            return "发送短信失败";
        }else if(code == -22){
            return "验证码已过期";
        }else if(code == -23){
            return "验证码错误";
        }else if(code == -24){
            return "用户账户不存在";
        }else if(code == -25){
            return "更新账户信息失败";
        }else if(code == -26){
            return "自动登录次数已达上限，请输入手机验证码";
        }else if(code == -27){
            return "登录失败";
        }else if(code == -28){
            return "用户注册失败";
        }else if(code == -29){
            return "重置账户动态密码登录次数统计失败";
        }else if(code == -30){
            return "短信一键注册时手机号已存在";
        }else if(code == -31){
            return "任务key无效";
        }else if(code == -32){
            return "短信一键注册时验证码错误";
        }else if(code == -40){
            return "没有token";
        }else if(code == -41){
            return "token不存在";
        }else if(code == -42){
            return "账号不合法";
        }else if(code == -43){
            return "未知账号类型";
        }else if(code == -44){
            return "找不到游戏对应cp";
        }else if(code == -45){
            return "礼包已领取完";
        }else if(code == -46){
            return "已经领取过礼包";
        }else if(code == -47){
            return "找不到游戏分区";
        }else if(code == -48){
            return "平台参数不合法";
        }else if(code == -49){
            return "充值订单校验失败";
        }else if(code == -50){
            return "充值订单已支付";
        }else if(code == -51){
            return "支付密码验证失败";
        }else if(code == -52){
            return "用户鸭蛋余额不足";
        }else if(code == -99){
            return "服务端出现异常";
        }else{
            return msg;
        }
    }

}
