package vb.queue.com.adverforverb.greendao.ctrls;

import java.util.List;

import vb.queue.com.adverforverb.apps.MyApps;
import vb.queue.com.adverforverb.entry.SettingInfo;
import vb.queue.com.adverforverb.greendao.SettingInfoDao;

/**
 * Created by gbh on 2018/8/30  15:24.
 *
 * @describe
 */

public class SettingInfoCtrls {
    /**
     * 门店标题信息
     *
     * @param content
     * @return
     */
    public static long insertTitleInfo(String content) {
        return MyApps.getDaoSession().getSettingInfoDao().insertOrReplace(new SettingInfo(content, 1));
    }

    public static long insertOrderTitleInfo(String content) {
        return MyApps.getDaoSession().getSettingInfoDao().insertOrReplace(new SettingInfo(content, 2));
    }


    public static String getTitleInfo(int type) {
        List<SettingInfo> list = MyApps.getDaoSession().getSettingInfoDao().queryBuilder()
                .where(SettingInfoDao.Properties.Type.eq(1))
                .orderDesc(SettingInfoDao.Properties.ID).build().list();
        if (list.size() > 0) {
            return list.get(0).getName();
        }
        return "";
    }

    public static String getOrderTitleInfo(int type) {
        List<SettingInfo> list = MyApps.getDaoSession().getSettingInfoDao().queryBuilder()
                .where(SettingInfoDao.Properties.Type.eq(2))
                .orderDesc(SettingInfoDao.Properties.ID).build().list();
        if (list.size() > 0) {
            return list.get(0).getName();
        }
        return type == 1 ? "包廂使用情況" : "包廂使用情況";
    }
}
