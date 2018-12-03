package com.cyztc.app.fileupload;

import com.blankj.utilcode.util.SPUtils;
import com.cyztc.app.constant.Const;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.utils.CommonUtil;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

import java.io.File;
import java.util.List;

/**
 * Created by ywl on 2016/5/25.
 */
@HttpRequest(
        host = HttpMethod.BASE_URL,
        path = "uploadPhoto.do"
        )
public class FileUploadParams extends RequestParams {

    public List<File> file; // 上传文件数组

    public FileUploadParams() {
        addHeader("token", CommonUtil.getToken());
        addHeader("id", SPUtils.getInstance().getString(Const.SP_ACCOUNT_ID, ""));
    }

    public void setFile(String filepath){
        addBodyParameter("file",new File(filepath),"multipart/form-data");
    }
}
