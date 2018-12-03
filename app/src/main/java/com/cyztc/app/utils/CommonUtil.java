package com.cyztc.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.blankj.utilcode.util.SPUtils;
import com.cyztc.app.CyApplication;
import com.cyztc.app.bus.RxBus;
import com.cyztc.app.constant.Const;
import com.cyztc.app.constant.Constant;
import com.cyztc.app.utils.pay.wxpay.MD5;
import com.cyztc.app.utils.pay.wxpay.WxMd5;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by ywl on 2016/10/20.
 */

public class CommonUtil {

  public static int getStatusHeight(Activity activity) {
    int statusBarHeight = 0;
    try {
      Class<?> c = Class.forName("com.android.internal.R$dimen");
      Object o = c.newInstance();
      Field field = c.getField("status_bar_height");
      int x = (Integer) field.get(o);
      statusBarHeight = activity.getResources().getDimensionPixelSize(x);
    } catch (Exception e) {
      e.printStackTrace();
      Rect frame = new Rect();
      activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
      statusBarHeight = frame.top;
    }
    return statusBarHeight;
  }

  public static String getCashDir() {
    return CyApplication.getInstance().getExternalCacheDir().getAbsolutePath();
  }

  /**
   * 获取设备号
   */
  public static String getDeviceID(Context ctx) {
    String strResult = null;
    TelephonyManager telephonyManager = (TelephonyManager) ctx
        .getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager != null) {
      strResult = telephonyManager.getDeviceId();
    }
    if (strResult == null) {
      strResult = Settings.Secure.getString(ctx.getContentResolver(),
          Settings.Secure.ANDROID_ID);
    }
    return strResult;
  }

  public static int getScreenWidth(Activity context) {
    DisplayMetrics dm = new DisplayMetrics();
    context.getWindowManager().getDefaultDisplay().getMetrics(dm);
    int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
    return mScreenWidth;
  }

  public static int getScreenHeight(Activity context) {
    DisplayMetrics dm = new DisplayMetrics();
    context.getWindowManager().getDefaultDisplay().getMetrics(dm);
    int mScreenHeight = dm.heightPixels;
    return mScreenHeight;
  }

  public static int dip2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  @SuppressWarnings("unchecked")
  public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters,
      String key) {
    StringBuffer sb = new StringBuffer();
    Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
    Iterator it = es.iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      String k = (String) entry.getKey();
      Object v = entry.getValue();
      if (null != v && !"".equals(v)
          && !"sign".equals(k) && !"key".equals(k)) {
        sb.append(k + "=" + v + "&");
      }
    }
    sb.append("key=" + key);
    System.out.println(sb.toString());
    String sign = WxMd5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
    System.out.println(sign);
    return sign;
  }

  /**
   * 获取时间戳
   */
  public static long genTimeStamp() {
    return System.currentTimeMillis() / 1000;
  }

  /**
   * 获得随机字符串
   */
  public static String genNonceStr() {
    Random random = new Random();
    return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
  }

  public static String forMatPrice(float price) {
    DecimalFormat df = new DecimalFormat("#.00");
    String result = df.format(price);
    if (result.equals(".00")) {
      return "0.00";
    }
    if (result.startsWith(".")) {
      return "0" + result;
    }
    return df.format(price);
  }

  public static String getStringFromContent(String content) {
    StringBuffer stringBuffer = new StringBuffer("");
    String[] datas = content.split("\\[img\\]");
    for (int i = 0; i < datas.length; i++) {
      if (!datas[i].contains("[/img]")) {
        String[] t = datas[i].split("\\n");
        int l = t.length;
        for (int j = 0; j < l; j++) {
          if (!TextUtils.isEmpty(t[j])) {
            stringBuffer.append(t[j]);
          }
        }
      }
    }
    return stringBuffer.toString();
  }

  public static boolean isNumeric(String str) {
    for (int i = 0; i < str.length(); i++) {
      System.out.println(str.charAt(i));
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * 时间戳转换成日期格式字符串
   *
   * @param seconds 精确到秒的字符串
   */
  public static String timeStamp2Date(String seconds, String format) {
    if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
      return "";
    }
    if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(new Date(Long.valueOf(seconds + "000")));
  }

  /**
   * 日期格式字符串转换成时间戳
   *
   * @param format 如：yyyy-MM-dd HH:mm:ss
   */
  public static String date2TimeStamp(String date_str, String format) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return String.valueOf(sdf.parse(date_str).getTime() / 1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static int getApplyLeavetype(String type) {
    switch (type) {
      case "事假":
        return 0;
      case "病假":
        return 1;
      case "年假":
        return 2;
      case "产假":
        return 3;
      case "其他":
        return 4;
    }
    return -1;
  }

  public static String getApplyLeaveStr(int type) {
    switch (type) {
      case 0:
        return "事假";
      case 1:
        return "病假";
      case 2:
        return "年假";
      case 3:
        return "产假";
      case 4:
        return "其他";
    }
    return "";
  }

  public static String getTitle(int type) {
    switch (type) {
      case 1:
        return "请假申请";
      case 2:
        return "差旅申请";
      case 3:
        return "印章申请";
      case 4:
        return "派车申请";
      case 5:
        return "其他";
    }
    return "";
  }

  //["公章","财务章","其它"],
  public static String getSeal(int type) {
    switch (type) {
      case 0:
        return "公章";
      case 1:
        return "财务章";
      case 2:
        return "合同章";
      case 3:
        return "其它";
    }
    return "";
  }

  public static int getApplySealType(String type) {
    switch (type) {
      case "公章":
        return 0;
      case "财务章":
        return 1;
      case "合同章":
        return 2;
      case "其它":
        return 3;
    }
    return -1;
  }

  public static String getTrivTitle(int type) {
    switch (type) {
      case 0:
        return "普通出差";
      case 1:
        return "其它";
    }
    return "";
  }

  public static int getTrivStatus(String type) {
    switch (type) {
      case "普通出差":
        return 0;
      case "其它":
        return 1;
    }
    return -1;
  }

  public static int getCarStatus(String type) {
    switch (type) {
      case "轿车":
        return 0;
      case "商务车":
        return 1;
      case "大巴车":
        return 2;
      case "其它":
        return 3;
    }
    return -1;
  }

  public static String getCarTitle(int status) {
    switch (status) {
      case 0:
        return "轿车";
      case 1:
        return "商务车";
      case 2:
        return "大巴车";
      case 3:
        return "其它";
    }
    return "";
  }

  public static long timeToLong(String time, String format) {
    String res;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    Date date = null;
    try {
      date = simpleDateFormat.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    long ts = date.getTime();
    return ts;
  }

  /*
   * 将时间戳转换为时间
   */
  public static String stampToDate(String s, String foramt) {
    String res;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(foramt);
    long lt = new Long(s);
    Date date = new Date(lt);
    res = simpleDateFormat.format(date);
    return res;
  }

  public static String getConnectWifiSsid(Context context) {
    WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    if (wifiInfo == null) {
      return "";
    }
    return wifiInfo.getSSID().replace("\"", "");
  }

  public static String getIpAddress(Context context) {
    //获取wifi服务
    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    int ipAddress = wifiInfo.getIpAddress();
    String ip = intToIp(ipAddress);
    return ip;
  }

  private static String intToIp(int i) {

    return (i & 0xFF) + "." +
        ((i >> 8) & 0xFF) + "." +
        ((i >> 16) & 0xFF) + "." +
        (i >> 24 & 0xFF);
  }

  public static String getDoor(int index) {
    String door = "";
    switch (index) {
      case 1:
        door = "寝室门";
        break;
      case 2:
        door = "大门1";
        break;
      case 3:
        door = "大门2";
        break;
      case 4:
        door = "员工通道";
        break;
      case 5:
        door = "办公楼正门";
        break;
      case 6:
        door = "办公楼侧门";
        break;
      case 7:
        door = "我的办公室";
        break;
    }
    return door;
  }

  public static String getStrFromRichContent(String content) {
    String str = "";
    List<String> soruce = sortData(content);

    int count = 0;
    if (soruce == null) {
      return "";
    }
    int size = soruce.size();

    for (int i = 0; i < size; i++) {
      if (soruce.get(i).toLowerCase().endsWith(".jpg") || soruce.get(i)
          .toLowerCase()
          .endsWith(".png") || soruce.get(i).toLowerCase().endsWith(".gif")) {

      } else {
        str = soruce.get(i);
        break;
      }
    }

    return str;
  }

  private static List<String> sortData(String content) {
    List<String> soruce = new ArrayList<>();
    String[] datas = content.split("\\[img\\]");
    for (int i = 0; i < datas.length; i++) {
      if (datas[i].contains("[/img]")) {
        String[] data = datas[i].split("\\[/img\\]");
        for (int j = 0; j < data.length; j++) {
          soruce.add(data[j]);
        }
      } else if (datas[i].toLowerCase().endsWith(".jpg") || datas[i].toLowerCase()
          .endsWith(".png")) {
        soruce.add(datas[i]);
      }
    }
    return soruce;
  }

  public static String getToken() {
    return SPUtils.getInstance().getString(Const.SP_TOKEN_ENCRYPT);
  }

  public static void loginOut(Context context) {
    SPUtils.getInstance().put(Const.SP_ACCOUNT_ID, "");
    SPUtils.getInstance().put(Const.SP_TOKEN, "");
    SPUtils.getInstance().put(Const.SP_TOKEN_ENCRYPT, "");
    SharedpreferencesUtil.write(context, Constant.SP_USER, Constant.SP_USER_LOGIN_DATA, "");

    CyApplication.getInstance().setUserBean(null);
    RxBus.getInstance().post("loginout", true);
  }
}
