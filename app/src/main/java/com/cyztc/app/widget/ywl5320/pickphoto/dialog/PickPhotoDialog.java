package com.cyztc.app.widget.ywl5320.pickphoto.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.UriUtils;
import com.cyztc.app.R;
import com.cyztc.app.widget.ywl5320.pickphoto.beans.ImgBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by ywl on 2016/6/14.
 */
public class PickPhotoDialog extends BaseDialog {

  private OnPhotoResultListener onPhotoResultListener;
  private static final int REQUEST_CAMERA_CODE = 0x0002;
  private static final int REQUEST_EXTERNAL_STORAGE_CODE = 0x0003;
  private static final int REQUEST_CAMERA_RESULT_CODE = 0x0004;
  private static final int REQUEST_CLICK_PHOTO_CODE = 0x0005;
  private Activity activity;
  private File file;
  private Uri imageUri;
  private String imgname = "";
  private boolean isCutImg = false;
  private int maxcount = 1;
  private TextView btnCamera;
  private TextView btnPhoto;
  private TextView tvCancel;
  private RelativeLayout rlRoot;

  public PickPhotoDialog(Context context, Activity activity) {
    super(context);
    this.activity = activity;
  }

  public void setCutImg(boolean cutImg, int maxcount) {
    isCutImg = cutImg;
    this.maxcount = maxcount;
    if (isCutImg) {
      this.maxcount = 1;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_choice_photo_layout);
    btnCamera = (TextView) findViewById(R.id.btn_takephoto);
    btnPhoto = (TextView) findViewById(R.id.btn_photo);
    tvCancel = (TextView) findViewById(R.id.tv_cancel);
    rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
    btnCamera.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
          Toast.makeText(context, "当前存储卡不可用", Toast.LENGTH_SHORT).show();
          return;
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED//相机权限
            || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED//读取存储卡权限
            ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(activity,
              new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_CODE);
        } else {
          file = new File(getImgPath());
          if (!file.exists()) {
            file.mkdirs();
          }
          imgname = getHeadImgName();
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          imageUri = Uri.fromFile(new File(file, imgname));
          intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
          activity.startActivityForResult(intent, REQUEST_CAMERA_RESULT_CODE);
        }
      }
    });
    btnPhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
          Toast.makeText(context, "当前存储卡不可用", Toast.LENGTH_SHORT).show();
          return;
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(activity,
              new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_CODE);
        } else {
          PhotoListDialog photoListDialog = new PhotoListDialog(context, activity);
          photoListDialog.setMAX_COUNT(maxcount);
          photoListDialog.show();
          photoListDialog.setOnChoicePhotoListener(new PhotoListDialog.OnChoicePhotoListener() {
            @Override
            public void onResult(List<ImgBean> imgBeens) {
              if (isCutImg && imgBeens != null && imgBeens.size() > 0) {
                if (onPhotoResultListener != null) {
                  onPhotoResultListener.onPhotoResult(imgBeens);
                }
                try {
                  String path =
                      activity.getExternalCacheDir().getAbsolutePath() + File.separator + System
                          .currentTimeMillis() + ".jpg";
                  File newFile = new File(path);
                  FileUtils.copyFile(new File(imgBeens.get(0).getPath()), newFile);
                  cropPhoto(Uri.fromFile(newFile));
                } catch (Exception e) {
                  e.printStackTrace();
                }
              } else {
                if (onPhotoResultListener != null) {
                  onPhotoResultListener.onPhotoResult(imgBeens);
                }
                dismiss();
              }
            }
          });
        }
      }
    });
    tvCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
    rlRoot.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
  }

  public void setOnPhotoResultListener(OnPhotoResultListener onPhotoResultListener) {
    this.onPhotoResultListener = onPhotoResultListener;
  }

  public interface OnPhotoResultListener {

    void onCameraResult(String path);

    void onCutPhotoResult(Bitmap bitmap);

    void onPhotoResult(List<ImgBean> selectedImgs);
  }

  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    if (requestCode == REQUEST_CAMERA_CODE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        file = new File(getImgPath());
        if (!file.exists()) {
          file.mkdirs();
        }
        imgname = getHeadImgName();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //imageUri = Uri.fromFile(new File(file, imgname));
        imageUri = UriUtils.file2Uri(new File(file, imgname));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, REQUEST_CAMERA_RESULT_CODE);
      } else {
        Toast.makeText(context, "请允许打开摄像头权限", Toast.LENGTH_SHORT).show();
      }
      return;
    } else if (requestCode == REQUEST_EXTERNAL_STORAGE_CODE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        PhotoListDialog photoListDialog = new PhotoListDialog(context, activity);
        photoListDialog.setMAX_COUNT(9);
        photoListDialog.show();
      } else {
        Toast.makeText(context, "请允许读取存储卡权限", Toast.LENGTH_SHORT).show();
      }
      return;
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != activity.RESULT_OK) {
      return;
    }
    if (requestCode == REQUEST_CAMERA_RESULT_CODE) {
      try {
        MediaStore.Images.Media.insertImage(context.getContentResolver(),
            imageUri.getPath(), imgname, null);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      // 最后通知图库更新
      context.sendBroadcast(
          new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
      if (!isCutImg) {
        if (onPhotoResultListener != null) {
          onPhotoResultListener.onCameraResult(imageUri.getPath());
        }
        dismiss();
      } else {
        cropPhoto(imageUri);
      }
    } else if (requestCode == REQUEST_CLICK_PHOTO_CODE) {
      Bitmap photo = data.getParcelableExtra("data");
      if (photo != null) {
        if (onPhotoResultListener != null) {
          onPhotoResultListener.onCutPhotoResult(photo);
        }
      }
      dismiss();
    }
  }

  public void cropPhoto(Uri uri) {
    Intent intent = new Intent();
    intent.setAction("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
    intent.putExtra("crop", "true");
    intent.putExtra("aspectX", 1);// 裁剪框比例
    intent.putExtra("aspectY", 1);
    intent.putExtra("outputX", 320);// 输出图片大小
    intent.putExtra("outputY", 320);
    intent.putExtra("return-data", true);
    activity.startActivityForResult(intent, REQUEST_CLICK_PHOTO_CODE);
  }

  public String getImgPath() {
    return Environment.getExternalStorageDirectory().getPath() + "/bdgames/images/";
  }

  public String getHeadImgName() {
    return System.currentTimeMillis() + ".jpg";
  }
}
