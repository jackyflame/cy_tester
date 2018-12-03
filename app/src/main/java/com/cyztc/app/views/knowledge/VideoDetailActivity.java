package com.cyztc.app.views.knowledge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.VideoDetailBean;
import com.cyztc.app.bean.VideoListBean;
import com.cyztc.app.httpservice.serviceapi.BookApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.TimeUtil;


import butterknife.BindView;

/**
 * Created by ywl on 2016/11/10.
 */

public class VideoDetailActivity extends BaseActivity{

    @BindView(R.id.rl_video_bg)
    RelativeLayout rlVideoBg;
    @BindView(R.id.videoview)
    VideoView videoView;
    @BindView(R.id.iv_full_screen)
    ImageView ivFullScreen;
    @BindView(R.id.pb_loading)
    ProgressBar progressBar;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.seekbar)
    SeekBar seekBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_remark)
    TextView tvRemark;

    private String videoId;
    private String videoUrl = "";
    private int lastPost = 0;
    private int length = 0;
    private boolean isfulls = false;

    private String videotitle = "";
    private String videoremark = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail_layout);
        videoId = getIntent().getStringExtra("videoid");
        videoUrl = getIntent().getStringExtra("videoUrl");
        videotitle = getIntent().getStringExtra("videotitle");
        videoremark = getIntent().getStringExtra("videoremark");
        setBackView();

        tvTitle.setText(videotitle);
        tvRemark.setText(videoremark);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height = CommonUtil.getScreenWidth(this) * 3 / 4;
        lp.width = CommonUtil.getScreenWidth(this);
        rlVideoBg.setLayoutParams(lp);

        videoView.setVideoPath(videoUrl);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                MyLog.d("Prepared playing is ok");
                videoView.start();
                progressBar.setVisibility(View.GONE);
                length = videoView.getDuration();
            }
        });

        ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullChangeScreen();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(length != 0) {
                    lastPost = length * progress / 100;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(videoView.canSeekForward() && videoView.canSeekBackward()) {
                    videoView.seekTo(lastPost);
                }
            }
        });
        tvTime.setText(TimeUtil.formatTime(0) + "/" + TimeUtil.formatTime(0));
        handler.postDelayed(runnable, 1000);
        getVideoDetail(CyApplication.getInstance().getUserBean().getStudentId(), videoId);
    }

    public void fullChangeScreen() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)// 切换为竖屏
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isfulls = false;
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isfulls = true;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//竖屏
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rlVideoBg.getLayoutParams();
            lp.height = CommonUtil.getScreenHeight(this);
            lp.width = CommonUtil.getScreenWidth(this);
            rlVideoBg.setLayoutParams(lp);
//            ivFullScreen.setImageResource(R.drawable.bg_live_cancel_fullscreen_seletor);
        } else {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rlVideoBg.getLayoutParams();
            lp.height = CommonUtil.getScreenWidth(this) * 3 / 4;
            lp.width = CommonUtil.getScreenWidth(this);
            rlVideoBg.setLayoutParams(lp);
//            ivFullScreen.setImageResource(R.drawable.bg_live_fullscreen_seletor);
        }
    }



    @Override
    public void onClickBack() {
        super.onClickBack();
        if(isfulls)
        {
            fullChangeScreen();
        }
        else {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isfulls)
        {
            fullChangeScreen();
        }
        else {
            this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(videoView.isPlaying())
        {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
        videoView = null;
        handler.removeCallbacks(runnable);
    }

    public static void startActivity(Context context, String videoId, String videoUrl, String videotitle, String videoremark)
    {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("videoid", videoId);
        intent.putExtra("videoUrl", videoUrl);
        intent.putExtra("videotitle", videotitle);
        intent.putExtra("videoremark", videoremark);
        context.startActivity(intent);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(videoView != null)
            {
                if(videoView.getCurrentPosition() == lastPost)
                {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
                lastPost = videoView.getCurrentPosition();
                tvTime.setText(TimeUtil.formatTime(videoView.getCurrentPosition()) + "/" + TimeUtil.formatTime(length));
                if (length != 0) {
                    seekBar.setProgress(videoView.getCurrentPosition() * 100 / length);
                }
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    public void getVideoDetail(String studentId, String resourceId)
    {
        BookApi.getInstance().getVideoDetail(studentId, resourceId, new HttpSubscriber<VideoDetailBean>(new SubscriberOnListener<VideoDetailBean>() {
            @Override
            public void onSucceed(VideoDetailBean data) {

            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }
}
