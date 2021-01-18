package vb.queue.com.adverforverb.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import vb.queue.com.adverforverb.entry.VideoInfo;
import vb.queue.com.adverforverb.greendao.ctrls.VideoInfoCtrls;

public class Texts {
    private String[] arrays;

    private void find() {
    }


    public static void tra(View view) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0);
//        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0, 0);

        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationX); //设置动画
        animatorSet.setDuration(1000);  //设置动画时间
        animatorSet.start(); //启动
    }


    private void replace() {
        String name = "你好XX";
        name.replaceFirst(String.valueOf(name.charAt(1)), "O");
        System.out.print(name);

        StringBuilder builder = new StringBuilder(name);
        builder.replace(1, 1, "P");
        System.out.print(builder.toString());
    }
}
