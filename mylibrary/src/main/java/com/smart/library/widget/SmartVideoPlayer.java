package com.smart.library.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;
import com.smart.library.R;

/**
 * @author guobihai
 * 创建日期：2020/9/16
 * desc：
 */
public class SmartVideoPlayer extends StandardGSYVideoPlayer {

    private ImageView sdvCover;
    private OnVideoStateCallback onVideoStateCallback;
    private VideoCallBack videoCallBack;
    private Callback callback;

    public SmartVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SmartVideoPlayer(Context context) {
        super(context);
    }

    public SmartVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.smart_video_player;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
//        setLooping(true);
        setVideoAllCallBack(new GSYSampleCallBack() {

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                if (null != onVideoStateCallback) {
                    onVideoStateCallback.onComplete();
                }
            }

            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
                if (null != callback) {
                    callback.onVideoError(url);
                }
                sdvCover.setAlpha(1.0f);
            }

        });
    }

    @Override
    public void onVideoResume() {
        if (getCurrentState() == GSYVideoView.CURRENT_STATE_NORMAL) {
            startPrepare();
            return;
        }
        super.onVideoResume();
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;
        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;
        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }


    @Override
    public void touchDoubleUp() {
//        super.touchDoubleUp();
        //不需要双击暂停
    }

    @Override
    public void clickStartIcon() {
        if (null != videoCallBack) {
            videoCallBack.onState(GSYVideoView.CURRENT_STATE_PLAYING);
        }
    }

    public void clickStart() {
        super.clickStartIcon();
    }

    public void onInfo(Integer what, Integer extra) {
        super.onInfo(what, extra);
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            sdvCover.animate().alpha(0f).setDuration(100).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    sdvCover.setVisibility(View.INVISIBLE);
                    sdvCover.setAlpha(1f);
                }
            }).start();
        }
    }


    @Override
    public void updateStartImage() {
        mStartButton.setVisibility(View.GONE);
    }


    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        setViewShowState(mStartButton, View.INVISIBLE);
        setViewShowState(sdvCover, View.INVISIBLE);
    }

    public void setOnVideoStateCallback(OnVideoStateCallback onVideoStateCallback) {
        this.onVideoStateCallback = onVideoStateCallback;
    }

    public void setVideoCallBack(VideoCallBack videoCallBack) {
        this.videoCallBack = videoCallBack;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface OnVideoStateCallback {
        void onComplete();
    }

    public interface OnVideoClickListener {
        void onClickVideo();
    }

    public interface VideoCallBack {
        void onState(Integer state);
    }

    public interface Callback {
        void onVideoError(String s);
    }

}
