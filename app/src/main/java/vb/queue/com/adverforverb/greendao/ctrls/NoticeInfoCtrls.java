package vb.queue.com.adverforverb.greendao.ctrls;

import android.text.TextUtils;

import org.w3c.dom.Text;

import java.util.List;

import vb.queue.com.adverforverb.apps.MyApps;
import vb.queue.com.adverforverb.entry.NoticeInfo;
import vb.queue.com.adverforverb.greendao.NoticeInfoDao;
import vb.queue.com.adverforverb.utils.Tool;

/**
 * Created by gbh on 2018/8/29  17:46.
 *
 * @describe 跑马灯信息
 */

public class NoticeInfoCtrls {

    /**
     * 公告信息
     *
     * @param content
     * @return
     */
    public static long insertNoticeInfo(String content) {
        return MyApps.getDaoSession().getNoticeInfoDao().insertOrReplace(new NoticeInfo(content));
    }


    /**
     * 添加公告信息
     *
     * @param content 内容
     * @param type    类型
     * @return
     */
    public static long insertNoticeInfo(String content, String type) {
        List<NoticeInfo> list = MyApps.getDaoSession().getNoticeInfoDao().queryBuilder().where(NoticeInfoDao.Properties.Author.eq(type)).build().list();
        if (null == list || list.size() == 0)
            return MyApps.getDaoSession().getNoticeInfoDao().insertOrReplace(new NoticeInfo(content, type));
        else {
            NoticeInfo noticeInfo = list.get(0);
            noticeInfo.setContent(content);
            noticeInfo.setTime(Tool.getYouXiaoQi());
            MyApps.getDaoSession().getNoticeInfoDao().update(noticeInfo);
            return noticeInfo.getID();
        }
    }

    /**
     * 获取所有的通知
     *
     * @return
     */
    public static List<NoticeInfo> loadAllNoticeInfo() {
        return MyApps.getDaoSession().getNoticeInfoDao().queryBuilder().orderDesc(NoticeInfoDao.Properties.Time).build().list();
    }

    /**
     * 获取当前通告信息
     *
     * @return
     */
    public static String loadCurNoticeInfo(int type) {
        List<NoticeInfo> list = MyApps.getDaoSession().getNoticeInfoDao().queryBuilder()
                .where(NoticeInfoDao.Properties.IsCurent.eq(true))
                .orderDesc(NoticeInfoDao.Properties.Time).build().list();
        if (list.size() > 0) {
            return list.get(0).getContent();
        }
        return type == 1 ? "國民健康署針對40~64歲的民眾，提供每3年1次的免費健檢。符合資格者，只要拿健保卡到特約醫療院所，就可以接受檢查，而且不只大醫院，也可以到社區附近的診所找家庭醫師，幫你省下等候或通車的時間，是不是很方便呢！ 適用對象： 1. 40~64歲，每三年一次。 2. 65歲以上，每年一次。 3. 35歲以上小兒麻痺患者，每年一次。 4. 55歲以上原住民身分者，每年一次。" : "伊莎貝爾正火熱舉辦中秋禮盒特賣會\n" +
                "五大好康好禮加碼送，還有超多優惠贈送專區:買一送一，任選兩盒再送指定一盒，花博卡特定商品打九五折，還有更多優惠。另外，免費試吃，免費咖啡，紅茶，還有超好拍照的館區環境喔！\n" +
                "地址:台中市大雅區中清路三段737號";
    }

    public static List<NoticeInfo> loadCurNoticeInfo() {
        List<NoticeInfo> list = MyApps.getDaoSession().getNoticeInfoDao().queryBuilder()
                .where(NoticeInfoDao.Properties.IsCurent.eq(true))
                .orderDesc(NoticeInfoDao.Properties.Time).build().list();
        return list;
    }


    public static int deleteNoticeInfoByType(String type) {
        if (TextUtils.isEmpty(type)) return -1;
        List<NoticeInfo> list = MyApps.getDaoSession().getNoticeInfoDao().queryBuilder()
                .where(NoticeInfoDao.Properties.Author.eq(type))
                .build().list();
        if (list.size() > 0)
            MyApps.getDaoSession().getNoticeInfoDao().delete(list.get(0));
        return 0;
    }

    /**
     * 删除通告信息
     *
     * @param ID
     * @return
     */
    public static int deleteNoticeInfo(long ID) {
        List<NoticeInfo> list = MyApps.getDaoSession().getNoticeInfoDao().queryBuilder()
                .where(NoticeInfoDao.Properties.ID.eq(ID))
                .build().list();
        if (list.size() > 0)
            MyApps.getDaoSession().getNoticeInfoDao().delete(list.get(0));
        return 0;
    }

    /**
     * 修改通告信息
     *
     * @param ID
     * @param content
     * @return
     */
    public static int updateNoticeInfo(Long ID, String content) {
        List<NoticeInfo> list = MyApps.getDaoSession().getNoticeInfoDao().queryBuilder()
                .where(NoticeInfoDao.Properties.ID.eq(ID))
                .build().list();
        if (list.size() > 0) {
            NoticeInfo noticeInfo = list.get(0);
            noticeInfo.setContent(content);
            MyApps.getDaoSession().getNoticeInfoDao().update(noticeInfo);
        }
        return -100;
    }
}
