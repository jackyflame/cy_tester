package com.cyztc.app.httpservice.service;

import android.support.annotation.Nullable;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.constant.Const;
import com.cyztc.app.httpservice.https.SafeHostnameVerifier;
import com.cyztc.app.httpservice.https.SslContextFactory;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.NetUtil;
import com.cyztc.app.utils.ZipHelper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ywl on 2016/5/19.
 */
public class HttpMethod {

  private static final String HTTP_PROTOCOL = "https://";
  public static final String IP = "192.168.89.2";
  public static final String PORT = ":8443";
  public static final String PORT_IMG = ":8443";
  public static final String HTTP_PROTOCOL_IMG = "https://";

//  private static final String HTTP_PROTOCOL = "https://";
//  public static final String IP = "39.104.229.38";
//  public static final String PORT = ":8443";
//  public static final String PORT_IMG = ":3700";
//  public static final String HTTP_PROTOCOL_IMG = "https://";

//  private static final String HTTP_PROTOCOL = "https://";
//  public static final String IP = "94.191.22.124";
//  public static final String PORT = ":443";

  public static final String IMG_URL = HTTP_PROTOCOL_IMG + IP + PORT_IMG + "/TrainingResource/";

  //接口链接
//  public static final String BASE_URL = HTTP_PROTOCOL + IP + PORT + "/training_v2/tl/";
  public static final String BASE_URL = HTTP_PROTOCOL + IP + PORT + "/training_v2/tl/";



  public static final String BASE_IMG_URL = "";
  public static final String ver = "1";//接口版本号
  public static final String p = "1";//平台IOS 0 ,Android 1s
  public static String token = "";//请求时用户的Token
  public static String HttpMethod;//接口方法名
  public static final int HEART_TIME = 10;
  private static Retrofit retrofit;

  //构造方法私有
  private HttpMethod() {
    retrofit = new Retrofit.Builder()
        .client(genericClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .build();
  }

  public static <T> T createApi(Class<T> clazz) {
    return retrofit.create(clazz);
  }

  //在访问HttpMethods时创建单例
  private static class SingletonHolder {
    private static final HttpMethod INSTANCE = new HttpMethod();
  }

  //获取单例
  public static HttpMethod getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public static OkHttpClient genericClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    // set your desired log level
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    Cache cache = null;
    try {
      //设置缓存路径
      File httpCacheDirectory = new File(CommonUtil.getCashDir(), "responses");
      //设置缓存 10M
      cache = new Cache(httpCacheDirectory, 50 * 1024 * 1024);
    } catch (Exception ex) {
      MyLog.e("缓存HTTP数据错误", ex.toString());
    }
    OkHttpClient httpClient = new Builder()
        .addInterceptor(new Interceptor() {
          @Override
          public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                .newBuilder()
                //.addHeader("ver", ver)
                //.addHeader("p", p)
                .addHeader("token", CommonUtil.getToken())
                .addHeader("id", SPUtils.getInstance().getString(Const.SP_ACCOUNT_ID, ""))
                .build();
            if (!NetUtil.getNetworkIsConnected(CyApplication.getInstance())) {
              request = request.newBuilder()
                  .cacheControl(CacheControl.FORCE_CACHE)
                  .build();
              MyLog.d("no network");
            }

            //
            Response originalResponse;
            try {
              originalResponse = chain.proceed(request);
            } catch (Exception e) {
              LogUtils.w("Http Error: " + e);
              throw e;
            }

            ResponseBody responseBody = originalResponse.body();

            //打印响应结果
            String bodyString = null;
            if (responseBody != null && isParseable(responseBody.contentType())) {
              bodyString = printResult(request, originalResponse, false);
            }
            //
            //if (!TextUtils.isEmpty(bodyString)
            //    && isJson(originalResponse.body().contentType())) {
            //  try {
            //    JsonObject jsonObject = new JsonParser().parse(bodyString).getAsJsonObject();
            //    if (jsonObject.has("status")
            //        && "-1".equals(jsonObject.get("status").getAsString())) {
            //      ToastUtils.showShort("登录过期，请重新登录");
            //      ActivityUtils.startActivity(LoginActivity.class);
            //    }
            //  } catch (Exception e) {
            //    e.printStackTrace();
            //    return originalResponse;
            //  }
            //}

            //if (NetUtil.getNetworkIsConnected(CyApplication.getInstance())) {
            //  int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
            //  MyLog.d("has network maxAge=" + maxAge);
            //  response.newBuilder()
            //      .addHeader("Cache-Control", "public, max-age=" + maxAge)
            //      .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
            //      .build();
            //} else {
            //  MyLog.d("network error");
            //  int maxStale = 60 * 60 * 24 * 7; // 无网络时，设置超时为1周
            //  MyLog.d("has maxStale=" + maxStale);
            //  response.newBuilder()
            //      .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
            //      .removeHeader("Pragma")
            //      .build();
            //  MyLog.d("response build maxStale=" + maxStale);
            //}

            return originalResponse;
          }
        }).
            addInterceptor(logging).
            cache(cache)
        .sslSocketFactory(SslContextFactory.getSSLSocketFactoryForOneWay(
            Utils.getApp().getResources().openRawResource(R.raw.cyssl)))
        .hostnameVerifier(new SafeHostnameVerifier())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        //                .proxy(Proxy.NO_PROXY)
        .build();
    return httpClient;
  }

  /**
   * 打印响应结果
   *
   * @throws IOException
   */
  @Nullable
  private static String printResult(Request request, Response response, boolean logResponse)
      throws IOException {
    try {
      //读取服务器返回的结果
      ResponseBody responseBody = response.newBuilder().build().body();
      BufferedSource source = responseBody.source();
      source.request(Long.MAX_VALUE); // Buffer the entire body.
      Buffer buffer = source.buffer();

      //获取content的压缩类型
      String encoding = response
          .headers()
          .get("Content-Encoding");

      Buffer clone = buffer.clone();

      //解析response content
      return parseContent(responseBody, encoding, clone);
    } catch (IOException e) {
      e.printStackTrace();
      return "{\"error\": \"" + e.getMessage() + "\"}";
    }
  }

  /**
   * 解析服务器响应的内容
   */
  private static String parseContent(ResponseBody responseBody, String encoding, Buffer clone) {
    Charset charset = Charset.forName("UTF-8");
    MediaType contentType = responseBody.contentType();
    if (contentType != null) {
      charset = contentType.charset(charset);
    }
    if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
      return ZipHelper.decompressForGzip(clone.readByteArray(), convertCharset(charset));//解压
    } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
      return ZipHelper.decompressToStringForZlib(clone.readByteArray(),
          convertCharset(charset));//解压
    } else {//content没有被压缩
      return clone.readString(charset);
    }
  }

  /**
   * 是否可以解析
   */
  public static boolean isParseable(MediaType mediaType) {
    return isText(mediaType) || isPlain(mediaType)
        || isJson(mediaType) || isForm(mediaType)
        || isHtml(mediaType) || isXml(mediaType);
  }

  public static boolean isText(MediaType mediaType) {
    if (mediaType == null || mediaType.type() == null) return false;
    return mediaType.type().equals("text");
  }

  public static boolean isPlain(MediaType mediaType) {
    if (mediaType == null || mediaType.subtype() == null) return false;
    return mediaType.subtype().toLowerCase().contains("plain");
  }

  public static boolean isJson(MediaType mediaType) {
    if (mediaType == null || mediaType.subtype() == null) return false;
    return mediaType.subtype().toLowerCase().contains("json");
  }

  public static boolean isXml(MediaType mediaType) {
    if (mediaType == null || mediaType.subtype() == null) return false;
    return mediaType.subtype().toLowerCase().contains("xml");
  }

  public static boolean isHtml(MediaType mediaType) {
    if (mediaType == null || mediaType.subtype() == null) return false;
    return mediaType.subtype().toLowerCase().contains("html");
  }

  public static boolean isForm(MediaType mediaType) {
    if (mediaType == null || mediaType.subtype() == null) return false;
    return mediaType.subtype().toLowerCase().contains("x-www-form-urlencoded");
  }

  public static String convertCharset(Charset charset) {
    String s = charset.toString();
    int i = s.indexOf("[");
    if (i == -1) {
      return s;
    }
    return s.substring(i + 1, s.length() - 1);
  }
}
