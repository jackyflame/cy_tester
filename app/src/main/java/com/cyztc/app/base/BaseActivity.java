package com.cyztc.app.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.cyztc.app.R;
import com.cyztc.app.dialog.LoadingDialog;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.log.SWToast;
import com.cyztc.app.utils.CommonUtil;
import com.zhy.autolayout.AutoLayoutActivity;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by ywl on 2016/10/20.
 */

public class BaseActivity extends AutoLayoutActivity {

  private LoadingDialog loadingDialog;
  private ImageView mivBack;
  private ImageView mivMenu;
  private TextView mtvTitle;
  private TextView mtvRight;
  private LinearLayout lySystemBar;
  private LinearLayout lyLoadmsg;
  private LayoutInflater mlayoutInflate;

  private NormalAskComplexDialog normalAskComplexDialog;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mlayoutInflate = LayoutInflater.from(this);
    SWToast.getToast().init(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
      Window window = getWindow();
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Color.TRANSPARENT);
    }
    //透明状态栏
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4全透明
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }
  }

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);
    mtvRight = (TextView) findViewById(R.id.tv_right);
    mtvTitle = (TextView) findViewById(R.id.tv_title);
    mivBack = (ImageView) findViewById(R.id.iv_back);
    mivMenu = (ImageView) findViewById(R.id.iv_right);
    lySystemBar = (LinearLayout) findViewById(R.id.ly_system_bar);
    lyLoadmsg = (LinearLayout) findViewById(R.id.ly_loadmsg);
    initSystembar(lySystemBar);
    if (mivBack != null) {
      mivBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onClickBack();
        }
      });
    }

    if (mivMenu != null) {
      mivMenu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onClickMenu();
        }
      });
    }

    if (mtvRight != null) {
      mtvRight.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onClickTextMenu();
        }
      });
    }

    if (mtvTitle != null) {
      mtvTitle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onClickTitle();
        }
      });
    }
  }

  /**
   * 显示返回图标
   */
  public void setBackView() {
    if (mivBack != null) {
      mivBack.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 显示返回图标
   */
  public void setBackView(int resId) {
    if (mivBack != null) {
      mivBack.setVisibility(View.VISIBLE);
      mivBack.setImageResource(resId);
    }
  }

  /**
   * 显示菜单图标
   */
  public void setMenuView() {
    if (mivMenu != null) {
      mivMenu.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 显示菜单图标
   */
  public void setMenuView(int resId) {
    if (mivMenu != null) {
      mivMenu.setVisibility(View.VISIBLE);
      mivMenu.setImageResource(resId);
    }
  }

  public void setTextMenuViewVisible(boolean show) {
    if (mtvRight != null) {
      if (show) {
        mtvRight.setVisibility(View.VISIBLE);
      } else {
        mtvRight.setVisibility(View.INVISIBLE);
      }
    }
  }

  public void setTextMenuView(String title) {
    if (mtvRight != null) {
      mtvRight.setVisibility(View.VISIBLE);
      mtvRight.setText(title);
    }
  }

  public void hideTextMenuView() {
    if (mtvRight != null) {
      mtvRight.setVisibility(View.GONE);
    }
  }

  public void showTextMenuView() {
    if (mtvRight != null) {
      mtvRight.setVisibility(View.GONE);
    }
  }

  /**
   * 设置标题
   */
  public void setTitle(String title) {
    if (mtvTitle != null) {
      mtvTitle.setText(title);
    }
  }

  /**
   * 返回键点击事件
   */
  public void onClickBack() {

  }

  /**
   * 菜单键点击事件
   */
  public void onClickMenu() {

  }

  /**
   * 菜单键点击事件
   */
  public void onClickTextMenu() {

  }

  public void onClickTitle() {

  }

  public void initSystembar(View lySystemBar) {
    if (Build.VERSION.SDK_INT >= 19) {
      if (lySystemBar != null) {
        lySystemBar.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lySystemBar.getLayoutParams();
        lp.height = CommonUtil.getStatusHeight(this);
        lySystemBar.requestLayout();
      }
    } else {
      if (lySystemBar != null) {
        lySystemBar.setVisibility(View.GONE);
      }
    }
  }

  /**
   * 显示loading对话框
   */
  public void showLoadDialog(String msg) {
    if (loadingDialog == null) {
      loadingDialog = new LoadingDialog(this);
    }
    loadingDialog.show();
    loadingDialog.setLoadMsg(msg);
  }

  /**
   * 关闭loading对话框
   */
  public void hideLoadDialog() {
    if (loadingDialog != null) {
      loadingDialog.dismiss();
    }
  }

  /**
   * 加载数据view
   */
  public void showDataLoadMsg(String msg) {
    if (lyLoadmsg != null) {
      lyLoadmsg.removeAllViews();
      lyLoadmsg.setVisibility(View.VISIBLE);
      View view = mlayoutInflate.inflate(R.layout.data_loading_layout, lyLoadmsg, false);
      TextView tvmsg = (TextView) view.findViewById(R.id.tv_msg);
      ImageView ivdh = (ImageView) view.findViewById(R.id.iv_dh);
      //            Glide.with(this).load(R.drawable.gif_load_data).into(ivdh);
      tvmsg.setText(msg);
      lyLoadmsg.addView(view);
    }
  }

  /**
   * 数据加载失败view
   */
  public void showDataLoadWrongMsg(String wrongmsg) {
    if (lyLoadmsg != null) {
      lyLoadmsg.removeAllViews();
      lyLoadmsg.setVisibility(View.VISIBLE);
      View view = mlayoutInflate.inflate(R.layout.data_loading_wrong_layout, lyLoadmsg, false);
      TextView tvmsg = (TextView) view.findViewById(R.id.tv_msg);
      tvmsg.setText(wrongmsg);
      lyLoadmsg.addView(view);
    }
  }

  public void hideDataLoadMsg() {
    if (lyLoadmsg != null) {
      lyLoadmsg.setVisibility(View.GONE);
    }
  }

  public void showToast(String message) {
    SWToast.getToast().toast(message, true);
  }

  public void showAskDialog(String title, String mesg, String yes, String no,
      NormalAskComplexDialog.OnDalogListener onDalogListener) {
    if (normalAskComplexDialog == null) {
      normalAskComplexDialog = new NormalAskComplexDialog(this);
    }
    normalAskComplexDialog.setDialogUtil(title, mesg, yes, no, onDalogListener);
    normalAskComplexDialog.show();
  }

  public void showAskDialog(boolean showTitle, String title, String mesg, String yes, String no,
      NormalAskComplexDialog.OnDalogListener onDalogListener) {
    if (normalAskComplexDialog == null) {
      normalAskComplexDialog = new NormalAskComplexDialog(this);
    }
    normalAskComplexDialog.setDialogUtil(showTitle, title, mesg, yes, no, onDalogListener);
    normalAskComplexDialog.show();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
