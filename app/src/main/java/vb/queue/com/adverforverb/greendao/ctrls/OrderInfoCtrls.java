package vb.queue.com.adverforverb.greendao.ctrls;

import android.text.TextUtils;

import java.util.List;

import vb.queue.com.adverforverb.apps.MyApps;
import vb.queue.com.adverforverb.entry.OrderInfo;
import vb.queue.com.adverforverb.greendao.OrderInfoDao;
import vb.queue.com.adverforverb.utils.Tool;

/**
 * Created by gbh on 2018/8/28  21:35.
 *
 * @describe
 */

public class OrderInfoCtrls {

    private static final int youxian = 1;//优先
    private static final int guohao = 4;//过号(原先为2，是为了过号 下移一位，现在改为4，是为了排序到最后)
    private static final int zhenchang = 3;//正常排序

    /**
     * 插入数据
     *
     * @param name
     * @return
     */
    public static int insertOrderInfo(String name) {
        if (TextUtils.isEmpty(name)) return -100;
        OrderInfo orderInfo = new OrderInfo(name);
        MyApps.getDaoSession().getOrderInfoDao().insertOrReplace(orderInfo);
        return orderInfo.getShowOrderNo();
    }


    /**
     * 获取当前序号
     *
     * @return
     */
    public static int getCurOrderNo() {
        String time = Tool.getSystemTime();
        List<OrderInfo> list = MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
//                .where(OrderInfoDao.Properties.Time.eq(time))
                .orderDesc(OrderInfoDao.Properties.ID)
                .offset(0).limit(1).build().list();
        if (list.size() > 0) {
            int orderNo = list.get(0).getShowOrderNo();
            if (orderNo == 999) return 801;
            orderNo += 1;
            return orderNo;
        }
        return 801;
    }

    /**
     * 获取今天所有挂号人员总数
     *
     * @return
     */
    public static int loadAllOrderByTime() {
        String time = Tool.getSystemTime();
        return MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
//                .where(OrderInfoDao.Properties.Time.eq(time))
                .build().list().size();
    }

    /**
     * 获取今天所有挂号人员总数
     *
     * @return
     */
    public static int loadAllOrderByTime(String time) {
        return MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
//                .where(OrderInfoDao.Properties.Time.eq(time))
                .build().list().size();
    }

    /**
     * 获取今天已看病人数
     *
     * @return
     */
    public static int loadYkOrderByTime() {
        String time = Tool.getSystemTime();
        return MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
//                .where(OrderInfoDao.Properties.Time.eq(time))
                .where(OrderInfoDao.Properties.Type.eq(1))
                .build().list().size();
    }

    /**
     * 获取今天未看病人数
     *
     * @return
     */
    public static int loadWkOrderByTime() {
//        String time = Tool.getSystemTime();
        return MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
//                .where(OrderInfoDao.Properties.Time.eq(time))
                .where(OrderInfoDao.Properties.Type.notEq(1))
                .build().list().size();
    }

    /**
     * 查询未看诊所有候诊人员列表
     *
     * @return
     */
    public static List<OrderInfo> loadAllOrderInfoListByType(int ID) {
        return MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
//                .where(OrderInfoDao.Properties.Time.eq(time))
                .where(OrderInfoDao.Properties.ID.eq(ID))
                .orderAsc(OrderInfoDao.Properties.Type)
                .orderAsc(OrderInfoDao.Properties.OrderNo)
                .build().list();
    }

    /**
     * 查询所有候诊人员列表
     *
     * @return
     */
    public static List<OrderInfo> loadAllOrderInfoList() {
//        String time = Tool.getSystemTime();
        return MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
//                .where(OrderInfoDao.Properties.Time.eq(time))
                .where(OrderInfoDao.Properties.Type.notEq(1))
                .where(OrderInfoDao.Properties.Type.notEq(4))
                .orderAsc(OrderInfoDao.Properties.Type)
                .orderAsc(OrderInfoDao.Properties.OrderNo)
                .build().list();
    }


    /**
     * 获取当前看诊人员
     *
     * @return
     */
    public static OrderInfo loadCurOrderInfo() {
        String time = Tool.getSystemTime();
        List<OrderInfo> list = MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
                .where(OrderInfoDao.Properties.Type.eq(0))
                .build().list();
        if (list.size() > 0) return list.get(0);
        return null;
    }

    /**
     * 修改候诊人信状态,改为已候诊
     *
     * @param ID
     * @return
     */
    public static int updateOrderInfoTypeById(long ID) {
        List<OrderInfo> list = MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
                .where(OrderInfoDao.Properties.ID.eq(ID))
                .build().list();
        if (list.size() > 0) {
            OrderInfo orderInfo = list.get(0);
            orderInfo.setType(1);
            orderInfo.setTime(Tool.getSystemTime());
            MyApps.getDaoSession().getOrderInfoDao().update(orderInfo);
        }
        return 1;
    }

    /**
     * 修改候诊人为正在看诊
     *
     * @param orderInfo
     * @return
     */
    public static int updateOrderInfoForCur(OrderInfo orderInfo) {
        orderInfo.setType(0);
        MyApps.getDaoSession().getOrderInfoDao().update(orderInfo);
        return 0;
    }

    /**
     * 修改候诊人信状态,改为已候诊
     *
     * @param orderInfo
     * @return
     */
    public static int updateOrderInfoByEntry(OrderInfo orderInfo) {
        orderInfo.setType(1);
        MyApps.getDaoSession().getOrderInfoDao().update(orderInfo);
        return 0;
    }

    /**
     * 优先排队
     *
     * @param ID
     * @return
     */
    public static int updateFirstOrderNoById(long ID) {
        List<OrderInfo> list = MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
                .where(OrderInfoDao.Properties.ID.eq(ID))
                .build().list();
        if (list.size() > 0) {
            OrderInfo orderInfo = list.get(0);
            orderInfo.setOrderNo(youxian);
            orderInfo.setType(2);
            orderInfo.setTime(Tool.getSystemTime());
            MyApps.getDaoSession().getOrderInfoDao().update(orderInfo);
        }
        return 1;
    }

    /**
     * 设定过号排序
     *
     * @param ID
     * @return
     */
    public static int updateDelayOrderNoById(long ID) {
        List<OrderInfo> list = MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
                .where(OrderInfoDao.Properties.ID.eq(ID))
                .build().list();
        if (list.size() > 0) {
            OrderInfo orderInfo = list.get(0);
            orderInfo.setOrderNo(guohao);
            orderInfo.setType(3);
            orderInfo.setTime(Tool.getSystemTime());
            MyApps.getDaoSession().getOrderInfoDao().update(orderInfo);
        }
        return 1;
    }

    /**
     * 解除过号排序
     *
     * @param ID
     * @return
     */
    public static int removeDelayOrderNoById(long ID) {
        List<OrderInfo> list = MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
                .where(OrderInfoDao.Properties.ID.eq(ID))
                .build().list();
        if (list.size() > 0) {
            OrderInfo orderInfo = list.get(0);
            orderInfo.setOrderNo(zhenchang);
            orderInfo.setType(2);
            orderInfo.setTime(Tool.getSystemTime());
            MyApps.getDaoSession().getOrderInfoDao().update(orderInfo);
        }
        return 1;
    }

    /**
     * 停止看诊,恢复为等候
     *
     * @return
     */
    public static int setAllOrderDengHou() {
        List<OrderInfo> list = MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
                .build().list();
        for (OrderInfo info : list) {
            info.setType(2);
        }
        MyApps.getDaoSession().getOrderInfoDao().updateInTx(list);
        return 1;
    }

    /**
     * 修改用户信息(废弃)
     *
     * @param ID
     * @return
     */
    public static int updateOrderInfoNoById(long ID, String name) {
        if (TextUtils.isEmpty(name)) return 1;
        List<OrderInfo> list = MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
                .where(OrderInfoDao.Properties.ID.eq(ID))
                .build().list();
        if (list.size() > 0) {
            OrderInfo orderInfo = list.get(0);
            orderInfo.setOrderNo(zhenchang);
            orderInfo.setName(name);
            orderInfo.setTime(Tool.getSystemTime());
            MyApps.getDaoSession().getOrderInfoDao().update(orderInfo);
        }
        return 0;
    }

    /**
     * 删除候诊人
     *
     * @param ID
     */
    public static void deleteOrderById(long ID) {
//        List<OrderInfo> lists = MyApps.getDaoSession().getOrderInfoDao().queryBuilder()
//                .whergnegxe(OrderInfoDao.Properties.ID.eq(ID)).build().list();
//        for (OrderInfo manager : lists) {
//            MyApps.getDaoSession().getOrderInfoDao().delete(manager);
//        }
    }

    /**
     * 删除所有的订单信息
     */
    public static void deleteAllOrderData() {
        MyApps.getDaoSession().getOrderInfoDao().deleteAll();
    }
}
