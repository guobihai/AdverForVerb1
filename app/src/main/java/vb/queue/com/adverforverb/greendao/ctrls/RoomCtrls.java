package vb.queue.com.adverforverb.greendao.ctrls;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import vb.queue.com.adverforverb.apps.MyApps;
import vb.queue.com.adverforverb.entry.RoomsInfo;
import vb.queue.com.adverforverb.greendao.RoomsInfoDao;
import vb.queue.com.adverforverb.utils.Tool;

public class RoomCtrls {

    public static final int PAGE_INDEX_COUNT = 6;

    /**
     * 保存信息
     *
     * @param roomName
     * @param roomTitle
     * @param roomDesc
     * @return
     */
    public static long saveRoomInfos(String roomName, String roomTitle, String roomDesc) {
        if (TextUtils.isEmpty(roomName))
            return -1;
        int nType = 0;
        if (!TextUtils.isEmpty(roomTitle))
            nType = 1;
        return MyApps.getDaoSession().getRoomsInfoDao().insertOrReplace(new RoomsInfo(roomName, roomTitle, roomDesc, nType));
    }

    public static long saveListRoomInfos(String roomName, String roomName1, String roomName2) {
        if (TextUtils.isEmpty(roomName))
            return -1;
        List<RoomsInfo> infoList = new ArrayList<>();
        infoList.add(new RoomsInfo(roomName, "", "", 0));
        if (!TextUtils.isEmpty(roomName1))
            infoList.add(new RoomsInfo(roomName1, "", "", 0));
        if (!TextUtils.isEmpty(roomName2))
            infoList.add(new RoomsInfo(roomName2, "", "", 0));
        MyApps.getDaoSession().getRoomsInfoDao().insertInTx(infoList);
        return 0;
    }

    /**
     * 获取总条数
     *
     * @return
     */
    public static int loadRoomCount() {
        return MyApps.getDaoSession().getRoomsInfoDao().loadAll().size();
    }

    /**
     * 获取所有的房间信息
     *
     * @return
     */
    public static List<RoomsInfo> loadAllRoomInfo(int offset) {
        return MyApps.getDaoSession().getRoomsInfoDao().queryBuilder()
                .orderDesc(RoomsInfoDao.Properties.Type)
                .orderDesc(RoomsInfoDao.Properties.Time)
                .offset(offset * PAGE_INDEX_COUNT).limit(PAGE_INDEX_COUNT)
                .build().list();
    }

    public static List<RoomsInfo> loadAllRoomInfo() {
        return MyApps.getDaoSession().getRoomsInfoDao().queryBuilder()
                .orderDesc(RoomsInfoDao.Properties.Type)
                .orderDesc(RoomsInfoDao.Properties.Time)
                .build().list();
    }

    /**
     * 获取所有在用房间信息
     *
     * @return
     */
    public static List<RoomsInfo> loadOprRoomInfo() {
        return MyApps.getDaoSession().getRoomsInfoDao().queryBuilder()
                .orderDesc(RoomsInfoDao.Properties.Time)
                .where(RoomsInfoDao.Properties.Type.eq(1))
                .build().list();
    }

    /**
     * 修改房间使用情况
     *
     * @param Id
     * @param roomName
     * @param roomTitle
     * @param roomDesc
     * @return
     */
    public static long updateRoomInfo(long Id, String roomName, String roomTitle, String roomDesc, int type) {
        if (Id == -1) return -1;
        List<RoomsInfo> list = MyApps.getDaoSession().getRoomsInfoDao().queryBuilder().where(RoomsInfoDao.Properties.Id.eq(Id)).build().list();
        if (list.size() > 0) {
            RoomsInfo roomsInfo = list.get(0);
            roomsInfo.setRoomName(roomName);
            roomsInfo.setRoomTitle(roomTitle);
            roomsInfo.setRoomDesc(roomDesc);
            roomsInfo.setType(type);
            roomsInfo.setTime(Tool.getYouXiaoQi());
            MyApps.getDaoSession().getRoomsInfoDao().update(roomsInfo);
            return 0;
        } else {
            return MyApps.getDaoSession().getRoomsInfoDao().insertOrReplace(new RoomsInfo(roomName, roomTitle, roomDesc, type));
        }
    }

    /**
     * 解除房间使用情况
     *
     * @param Id
     * @return
     */
    public static long updateRoomInfo(long Id) {
        if (Id == -1) return -1;
        List<RoomsInfo> list = MyApps.getDaoSession().getRoomsInfoDao().queryBuilder().where(RoomsInfoDao.Properties.Id.eq(Id)).build().list();
        if (list.size() > 0) {
            RoomsInfo roomsInfo = list.get(0);
            roomsInfo.setRoomTitle("");
            roomsInfo.setRoomDesc("");
            roomsInfo.setType(0);
            roomsInfo.setTime(Tool.getYouXiaoQi());
            MyApps.getDaoSession().getRoomsInfoDao().update(roomsInfo);
            return 0;
        }
        return -1;
    }

    /**
     * 解除房间使用情况
     *
     * @return
     */
    public static long clearAllRoomInfo() {
        List<RoomsInfo> list = MyApps.getDaoSession().getRoomsInfoDao().queryBuilder().build().list();
        if (list.size() > 0) {
            for (RoomsInfo roomsInfo : list) {
                roomsInfo.setRoomTitle("");
                roomsInfo.setRoomDesc("");
                roomsInfo.setType(0);
            }
            MyApps.getDaoSession().getRoomsInfoDao().updateInTx(list);
            return 0;
        }
        return -1;
    }

    /**
     * 删除房间
     *
     * @param ID
     */
    public static void deleteRoomsById(long ID) {
        List<RoomsInfo> lists = MyApps.getDaoSession().getRoomsInfoDao().queryBuilder()
                .where(RoomsInfoDao.Properties.Id.eq(ID)).build().list();
        for (RoomsInfo manager : lists) {
            MyApps.getDaoSession().getRoomsInfoDao().delete(manager);
        }
    }
}
