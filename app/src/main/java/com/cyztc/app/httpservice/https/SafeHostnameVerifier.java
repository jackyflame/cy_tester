package com.cyztc.app.httpservice.https;

import com.cyztc.app.httpservice.service.HttpMethod;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by zhpan on 2018/3/21.
 */

public class SafeHostnameVerifier implements HostnameVerifier {
  @Override
  public boolean verify(String hostname, SSLSession session) {
    if (HttpMethod.IP.equals(hostname)) {
      //校验hostname是否正确，如果正确则建立连接
      return true;
    }
    return false;
  }
}
