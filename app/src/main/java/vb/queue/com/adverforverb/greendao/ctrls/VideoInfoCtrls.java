package vb.queue.com.adverforverb.greendao.ctrls;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vb.queue.com.adverforverb.apps.MyApps;
import vb.queue.com.adverforverb.entry.VideoInfo;
import vb.queue.com.adverforverb.greendao.OrderInfoDao;
import vb.queue.com.adverforverb.greendao.VideoInfoDao;
import vb.queue.com.adverforverb.utils.FileUtils;
import vb.queue.com.adverforverb.utils.UrlUtils;

/**
 * Created by gbh on 2018/8/29  17:36.
 *
 * @describe
 */

public class VideoInfoCtrls {

    private static List<String> loadVideoPaths() {
        List<File> listVideoFile = FileUtils.findListVideoFile();
        List<String> stringList = new ArrayList<>();
        for (File file : listVideoFile) {
            stringList.add(file.getAbsolutePath());
        }
//        for (VideoInfo info : list) {
//            stringList.add(info.getUrl());
//        }
        return stringList;
    }

    /**
     * 获取所有的播放信息
     *
     * @return
     */
    public static String[] loadAllVideoInfo() {
        List<File> listVideoFile = FileUtils.findListVideoFile();
        String[] strings = new String[listVideoFile.size()];
        for (int i = 0; i < listVideoFile.size(); i++) {
            strings[i] = listVideoFile.get(i).getAbsolutePath();
        }
        return strings;
    }




    /**
     * 插入节目信息
     *
     * @param title
     * @param url
     * @return
     */
    public static long insertVideoInfo(String title, String url) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url)) return -100L;
        return MyApps.getDaoSession().getVideoInfoDao().insertOrReplace(new VideoInfo(title, url));
    }

    /**
     * 删除节目信息
     *
     * @param ID
     * @return
     */
    public static int deleteVideoInfo(Long ID) {
        List<VideoInfo> list = MyApps.getDaoSession().getVideoInfoDao().queryBuilder()
                .where(VideoInfoDao.Properties.ID.eq(ID))
                .build().list();
        if (list.size() > 0)
            MyApps.getDaoSession().getVideoInfoDao().delete(list.get(0));
        return 0;
    }

}
