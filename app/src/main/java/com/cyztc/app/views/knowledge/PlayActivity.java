package com.cyztc.app.views.knowledge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.VideoDetailBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.BookApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.TimeUtil;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/9.
 */

public class PlayActivity extends BaseActivity {

    @BindView(R.id.rl_video_bg)
    RelativeLayout rlVideoBg;
    @BindView(R.id.pb_loading)
    ProgressBar progressBar;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.seekbar)
    SeekBar seekBar;
    @BindView(R.id.rl_actions)
    RelativeLayout rlActions;

    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private MappingTrackSelector trackSelector;
    private Handler mainHandler;
    private DataSource.Factory mediaDataSourceFactory;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private long lastPost = 0;

    private String videoId;
    private String videoUrl = "";
    private String videotitle = "";
    private String videoremark = "";
    private String picture;
    private long length;
    private boolean isPause = false;
    private boolean isfulls = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoId = getIntent().getStringExtra("videoid");
        videoUrl = getIntent().getStringExtra("videoUrl");
        videotitle = getIntent().getStringExtra("videotitle");
        videoremark = getIntent().getStringExtra("videoremark");
        picture = getIntent().getStringExtra("picture");
        mainHandler = new Handler();
        mediaDataSourceFactory = buildDataSourceFactory(true);
        setContentView(R.layout.activity_play_layout);
        setBackView();
        tvTitle.setText(videotitle);
        tvRemark.setText(videoremark);

        if(videoUrl.endsWith(".mp4"))
        {
            ivBg.setVisibility(View.GONE);
            rlActions.setVisibility(View.GONE);
            setMenuView(R.mipmap.icon_video_fullscreen);
        }
        else
        {
            ivBg.setVisibility(View.VISIBLE);
            rlActions.setVisibility(View.VISIBLE);
            ImageLoad.getInstance().displayImage(this, ivBg, HttpMethod.IMG_URL + picture, 0, 0);
        }

        playerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
//        initPlayer();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height = CommonUtil.getScreenWidth(this) * 3 / 4;
        lp.width = CommonUtil.getScreenWidth(this);
        rlVideoBg.setLayoutParams(lp);
        tvTime.setText(TimeUtil.formatTime(0) + "/" + TimeUtil.formatTime(0));

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
                if(player != null) {
                    player.seekTo(lastPost);
                }
            }
        });

        handler.postDelayed(runnable, 1000);
        getDetail();
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
//        super.onBackPressed();
        if(isfulls)
        {
            fullChangeScreen();
        }
        else {
            this.finish();
        }
    }

    @Override
    public void onClickMenu() {
        super.onClickMenu();
        fullChangeScreen();
    }

    //    public static void startActivity(Context context, String playurl, String videotitle, String videoremark)
//    {
//        Intent intent = new Intent(context, PlayActivity.class);
//        intent.putExtra("playurl", playurl);
//        context.startActivity(intent);
//    }

    public static void startActivity(Context context, String videoId, String videoUrl, String videotitle, String videoremark, String picture)
    {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra("videoid", videoId);
        intent.putExtra("videoUrl", videoUrl);
        intent.putExtra("videotitle", videotitle);
        intent.putExtra("videoremark", videoremark);
        intent.putExtra("picture", picture);
        context.startActivity(intent);
    }

    public void initPlayer()
    {
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);

        trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
//        trackSelector.addListener(this);
//        trackSelector.addListener(eventLogger);
//        trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl());
//        player.addListener(this);
//        player.addListener(eventLogger);
//        player.setAudioDebugListener(eventLogger);
//        player.setVideoDebugListener(eventLogger);
//        player.setId3Output(eventLogger);
        playerView.setPlayer(player);


        player.setPlayWhenReady(true);

        //https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8
        //http://hls.hz.qingting.fm/live/386.m3u8?bitrate=64&format=mpegts&deviceid=56c5166a-ee91-45d3-c525-ee2f441fb60a&time=1481264840362
       // player.prepare(new HlsMediaSource(Uri.parse(playurl), mediaDataSourceFactory, mainHandler, null));
        //mp4
        MyLog.d("playurl：" + videoUrl);
        player.prepare(new ExtractorMediaSource(Uri.parse(videoUrl), mediaDataSourceFactory, new DefaultExtractorsFactory(), mainHandler, null));
        //mp3
//        player.prepare(new SsMediaSource(Uri.parse(playurl), buildDataSourceFactory(false),
//                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, null));
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                showToast("不能播放");
                PlayActivity.this.finish();
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });
    }

//    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
//        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension
//                : uri.getLastPathSegment());
//        switch (type) {
//            case C.TYPE_SS:
//                return new SsMediaSource(uri, buildDataSourceFactory(false),
//                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
//            case C.TYPE_DASH:
//                return new DashMediaSource(uri, buildDataSourceFactory(false),
//                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
//            case C.TYPE_HLS:
//                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, null);
//            case C.TYPE_OTHER:
//                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
//                        mainHandler, eventLogger);
//            default: {
//                throw new IllegalStateException("Unsupported type: " + type);
//            }
//        }
//    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return CyApplication.getInstance().buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            Timeline timeline = player.getCurrentTimeline();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(player != null)
            {
                length = player.getDuration();
                if(player.getCurrentPosition() == lastPost)
                {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
                lastPost = player.getCurrentPosition();
                tvTime.setText(TimeUtil.formatTime(player.getCurrentPosition()) + "/" + TimeUtil.formatTime(player.getDuration()));
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    @OnClick(R.id.iv_pause)
    public void onClickPause(View veiw)
    {
        if(player != null)
        {
            if(isPause)
            {
                isPause = false;
                initPlayer();
                player.seekTo(lastPost);
            }
            else {
                isPause = true;
                player.stop();
            }
        }

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

    public void getDetail()
    {
        BookApi.getInstance().getVideoDetail(CyApplication.getInstance().getUserBean().getStudentId(), videoId, new HttpSubscriber<VideoDetailBean>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {

            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }


}
