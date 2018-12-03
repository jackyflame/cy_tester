package com.cyztc.app.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.cyztc.app.R;
import com.cyztc.app.dialog.LoadingDialog;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.log.SWToast;
import com.cyztc.app.utils.CommonUtil;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by ubuntu on 16-2-1.
 */
public abstract class BaseFragment extends Fragment {

  public View contentView;
  protected int layoutResId;
  private ImageView mivBack;
  private ImageView mivMenu;
  private TextView mtvTitle;
  private LinearLayout lyLoadmsg;
  private LinearLayout lySystemBar;
  private LayoutInflater mlayoutInflate;

  protected boolean isVisible;
  protected boolean isFirst = true;
  private LoadingDialog loadingDialog;
  private NormalAskComplexDialog normalAskComplexDialog;
  private Unbinder mUnbinder;

  /**
   * 在这里实现Fragment数据的缓加载.
   */
  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    boolean b = getUserVisibleHint();
    if (isVisibleToUser && isVisible && b) {
      onVisible();
      isVisible = false;
    } else {
      onInvisible();
    }
    super.setUserVisibleHint(isVisibleToUser);
  }

  /**
   * fragment被设置为可见时调用
   */
  protected void onVisible() {
    lazyLoad();
  }

  protected void lazyLoad() {
  }

  /**
   * fragment被设置为不可见时调用
   */
  protected void onInvisible() {
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    setUserVisibleHint(true);
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // 注入类属性
    contentView = inflater.inflate(layoutResId, container, false);
    mlayoutInflate = LayoutInflater.from(getActivity());
    SWToast.getToast().init(getActivity());
    mUnbinder = ButterKnife.bind(this, contentView);
    mivBack = (ImageView) contentView.findViewById(R.id.iv_back);
    mivMenu = (ImageView) contentView.findViewById(R.id.iv_right);
    mtvTitle = (TextView) contentView.findViewById(R.id.tv_title);
    lyLoadmsg = (LinearLayout) contentView.findViewById(R.id.ly_loadmsg);
    lySystemBar = (LinearLayout) contentView.findViewById(R.id.ly_system_bar);
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
    isVisible = true;
    return contentView;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mUnbinder != null) {
      mUnbinder.unbind();
    }
    EventBus.getDefault().unregister(this);
  }

  public void initSystembar(View lySystemBar) {
    if (Build.VERSION.SDK_INT >= 19) {
      if (lySystemBar != null) {
        lySystemBar.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lySystemBar.getLayoutParams();
        lp.height = CommonUtil.getStatusHeight(getActivity());
        lySystemBar.requestLayout();
      }
    }
  }

  public void setContentView(int layoutResId) {
    this.layoutResId = layoutResId;
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

  /**
   * 设置标题
   */
  public void setTitle(String title) {
    if (mtvTitle != null && !TextUtils.isEmpty(title)) {
      mtvTitle.setText(title);
    }
  }

  public void onClickBack() {

  }

  public void onClickMenu() {

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
      //            Glide.with(getActivity()).load(R.drawable.gif_load_data).into(ivdh);
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
      View view = mlayoutInflate.inflate(R.layout.data_loading_wrong_layout, lyLoadmsg, false);
      TextView tvmsg = (TextView) view.findViewById(R.id.tv_msg);
      tvmsg.setText(wrongmsg);
      lyLoadmsg.addView(view);
      lyLoadmsg.setVisibility(View.VISIBLE);
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

  /**
   * 显示loading对话框
   */
  public void showLoadDialog(String msg) {
    if (loadingDialog == null) {
      loadingDialog = new LoadingDialog(getActivity());
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

  public void showAskDialog(String title, String mesg, String yes, String no,
      NormalAskComplexDialog.OnDalogListener onDalogListener) {
    if (normalAskComplexDialog == null) {
      normalAskComplexDialog = new NormalAskComplexDialog(getActivity());
    }
    normalAskComplexDialog.setDialogUtil(title, mesg, yes, no, onDalogListener);
    normalAskComplexDialog.show();
  }
}
