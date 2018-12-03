package com.cyztc.app.utils;

import android.text.TextUtils;
import android.util.Base64;
import com.blankj.utilcode.util.EncryptUtils;

/**
 * @author yinhao
 * @date 2018/10/11
 */
public class RSAUtils {
  private static final String CHARSET = "UTF-8";
  private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
  private static final String RSA_PUBLIC_APP =
      "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCx9cdHsOruQ5yyZ/4+CzYelB/to5RjR8Ml5KeXcFzllzvb1HUoDJRFpKBvPddlOXs8Z3KXlV98Hq9Zwl61SngL2IciR1bQKaBf5rHuKTi1nnSRTWslah7ZUVJkh6voa7V+V+7vCUbsY1HoSMzryRjTznAwQ7d4ln7XcwVOBq88MQIDAQAB";
  private static final String RSA_PRIVATE_SERVER =
      "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANen8xnkvkjkWrI1kTiioiy6DUpU8XW/91ihv6khXfeeEQ2Y3ZGxcqydYCcaaqQnygK5ksRCbQSfhWGhfd64+8y70dVQO6EwLD+3sUF+sUsvUgZV/iHjctbPLCpYke6pyQudy+sN38VA654YjiSZOPnpsoboDxHWe7w+weiuJapXAgMBAAECgYBso5/04GX3pIoIErI4vL1EGOoPEyGctvss/DboRfXngwqGNgcQmTryuEkzCbzFO4Ipw0Cre+cdmBsCZu9Y3wmBWZjYVY/mfZNinv4z9vUnrhY588ntrXGW7XuMl5nb26s9djrsckcg4dpt2+0o6WWXt1h9xNy81PODjCDBsHtpWQJBAPxtfrq8FJlpQTRVDDFgZFzNVjn57JuixQwwae5l1q0mxx8WU9n8t6BGZdHwH8Pkga+BrwWb+hiiApbk24wzGqUCQQDatTyczP0My3nAygV7Rfqf0rN9dmf7O1ASmSqBJtkwwX3610jUt41AaE5lOOw9rBzgl1PkcjoGUTIeIDvbTqxLAkAuTg+vRY0pzysM5IUGEb6XX3tvyy7iJeurnr3v43KL9M8WHsxm8nrkxcUry7aoAqTfgxSOHNy15BJBh93WTBGZAkEAryoivo7rSTkabY8f7shwX1r4yM5xn2S00AB+a2w63XLLPID6YuwTpHcl+qopg4d6pWJuHXRjBrY3RquYZSBQmwJAV8ntz75+bpiOkGBByv8ZEU7ZDWP92Urn54Bm4fse9gUYdeSopqx11OT5j/R44e3kvQyJIIy6Jky5gYdpqskXDw==";

  public static String encryptRSA(String data) {
    if (TextUtils.isEmpty(data)) {
      return null;
    }
    try {
      byte[] keyBytes = Base64.decode(RSA_PUBLIC_APP, Base64.DEFAULT);
      byte[] bytes = EncryptUtils.encryptRSA(data.getBytes(), keyBytes, true, RSA_ALGORITHM);
      byte[] encode = Base64.encode(bytes, Base64.NO_WRAP);
      return new String(encode, CHARSET);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String decryptRSA(String data) {
    if (TextUtils.isEmpty(data)) {
      return null;
    }
    try {
      byte[] keyBytes = Base64.decode(RSA_PRIVATE_SERVER, Base64.DEFAULT);
      byte[] result =
          Base64.decode(data.getBytes(CHARSET), Base64.NO_WRAP);
      byte[] bytes = EncryptUtils.decryptRSA(result, keyBytes, false, RSA_ALGORITHM);
      String origin = new String(bytes, CHARSET);
      return origin;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
