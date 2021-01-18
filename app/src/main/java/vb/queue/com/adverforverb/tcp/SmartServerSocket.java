package vb.queue.com.adverforverb.tcp;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vb.queue.com.adverforverb.entry.MessageInfo;
import vb.queue.com.adverforverb.entry.OrderInfo;
import vb.queue.com.adverforverb.entry.ResData;
import vb.queue.com.adverforverb.entry.RoomsInfo;
import vb.queue.com.adverforverb.entry.VideoInfo;
import vb.queue.com.adverforverb.greendao.RoomsInfoDao;
import vb.queue.com.adverforverb.greendao.ctrls.NoticeInfoCtrls;
import vb.queue.com.adverforverb.greendao.ctrls.OrderInfoCtrls;
import vb.queue.com.adverforverb.greendao.ctrls.RoomCtrls;
import vb.queue.com.adverforverb.greendao.ctrls.SettingInfoCtrls;
import vb.queue.com.adverforverb.greendao.ctrls.VideoInfoCtrls;
import vb.queue.com.adverforverb.utils.CmdUtils;
import vb.queue.com.adverforverb.utils.FileUtils;
import vb.queue.com.adverforverb.utils.JsonUtils;
import vb.queue.com.adverforverb.utils.LogUtils;
import vb.queue.com.adverforverb.utils.NaviDebug;
import vb.queue.com.adverforverb.utils.PreferenceUtils;
import vb.queue.com.adverforverb.utils.SendFileUtils;

/**
 * 监听服务
 */
class S411Socket extends Thread {


    private Socket mSocket;
    private byte[] mucRecvData = new byte[1024 * 1024];
    private int mnRecvLength;
    private Context mContext;

    S411Socket(Context context, Socket s) {
        this.mSocket = s;
        this.mContext = context;
    }


    private void sendData(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        byte[] ucData = msg.getBytes();
        try {
            mSocket.getOutputStream().write(ucData, 0, ucData.length);
            mSocket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        try {
            mSocket.setKeepAlive(true);
            while (SmartServerSocket.isRun) {
                InputStream input = mSocket.getInputStream();
                mnRecvLength = input.read(mucRecvData);
                if (mnRecvLength == -1) {
                    System.out.println("远程连接已关闭");
                    return;
                }
                byte[] ucData = new byte[mnRecvLength];
                System.arraycopy(mucRecvData, 0, ucData, 0, mnRecvLength);
                String data = new String(ucData).trim();
                System.out.println("接收的数据" + data);
                if (TextUtils.isEmpty(data)) continue;
                try {
                    ResData.ValueBean valueBean = null;
                    RoomsInfo roomsInfo = null;
                    String res = "";

                    JsonParser jsonParser = new JsonParser();
                    JsonObject object = (JsonObject) jsonParser.parse(data);
                    String tag = object.get("tag").getAsString();

                    if (tag.equals(CmdUtils.CMD1) || tag.equals(CmdUtils.CMD7)) {
                        JsonObject valueObject = object.getAsJsonObject("value");
                        roomsInfo = JsonUtils.deserialize(valueObject, RoomsInfo.class);
                    } else {
                        JsonObject valueObject = object.getAsJsonObject("value");
                        valueBean = JsonUtils.deserialize(valueObject, ResData.ValueBean.class);
                    }

                    switch (tag) {
                        case CmdUtils.CMD0://
                            if (null == valueBean || TextUtils.isEmpty(valueBean.getValue())) {
                                sendData(getFaileMsg(CmdUtils.CMD0));
                                return;
                            }
                            String machineNo = PreferenceUtils.getString(mContext, "machineNo", "");
                            if (machineNo.equals(valueBean.getValue())) {
                                String title = SettingInfoCtrls.getTitleInfo(1);
                                Map<String, String> mapLogin = new HashMap<>();
                                mapLogin.put("code", "0000");
                                mapLogin.put("msg", title);
                                String resLogin = JsonUtils.serialize(new MessageInfo<Map<String, String>>(CmdUtils.CMD0, mapLogin));
                                sendData(resLogin);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD0));
                            }
                            break;
                        case CmdUtils.CMD1://增加空房间号
                            if (null != roomsInfo) {
                                long showNo = RoomCtrls.saveListRoomInfos(roomsInfo.getRoomName(), roomsInfo.getRoomTitle()
                                        , roomsInfo.getRoomDesc());
                                if (showNo == -1) {
                                    sendData(getFaileMsg(CmdUtils.CMD1));
                                } else {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("code", String.valueOf("0000"));
                                    map.put("msg", String.valueOf(showNo));
                                    String info = JsonUtils.serialize(new MessageInfo<Map>(CmdUtils.CMD1, map));
                                    sendData(info);
                                    EventBus.getDefault().post(CmdUtils.CMD1);
//                                    EventBus.getDefault().post(CmdUtils.CMD1 + "," + res + "," + showNo);
                                }
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD1));
                            }
                            break;
                        case CmdUtils.CMD2://删除指定排号人员
                            if (null == valueBean) {
                                sendData(getFaileMsg(CmdUtils.CMD2));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                RoomCtrls.deleteRoomsById(Long.parseLong(res));
                                sendData(getSuccessMsg(CmdUtils.CMD2));
                                EventBus.getDefault().post(CmdUtils.CMD2);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD2));
                            }
                            break;
                        case CmdUtils.CMD3://优先候诊
                            if (null == valueBean) {
                                sendData(getFaileMsg(CmdUtils.CMD3));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                OrderInfoCtrls.updateFirstOrderNoById(Long.parseLong(res));
//                                sendData(getSuccessMsg(CmdUtils.CMD3));
                                sendListdata();
                                EventBus.getDefault().post(CmdUtils.CMD3);
                            } else {
                                String msg = getFaileMsg(CmdUtils.CMD3);
                                sendData(msg);
                            }
                            break;
                        case CmdUtils.CMD4://获取当前排号人员列表
                            sendListdata();
                            break;
                        case CmdUtils.CMD5:
                            if (null == valueBean) {
                                sendData(getFaileMsg(CmdUtils.CMD5));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                List<OrderInfo> listInfo = OrderInfoCtrls.loadAllOrderInfoListByType(Integer.parseInt(res));
                                if (listInfo.size() > 0) {
                                    //已看诊
                                    OrderInfoCtrls.updateOrderInfoByEntry(listInfo.get(0));
                                }
                            }
                            sendData(getSuccessMsg(CmdUtils.CMD5));
                            EventBus.getDefault().post(CmdUtils.CMD5);
                            break;
                        case CmdUtils.CMD6://选择下一位候诊人
                            //获取当前看诊人员,如果存在则修改状态为已看诊
                            OrderInfo orderInfo = OrderInfoCtrls.loadCurOrderInfo();
                            if (null != orderInfo) {
                                OrderInfoCtrls.updateOrderInfoByEntry(orderInfo);
                            }
                            List<OrderInfo> listPerson = OrderInfoCtrls.loadAllOrderInfoList();
                            if (listPerson.size() > 0) {
                                //确认上一个候诊人信息
                                OrderInfoCtrls.updateOrderInfoForCur(listPerson.get(0));
                            }
//                          sendData(getSuccessMsg(CmdUtils.CMD6));
                            sendListdata();
                            EventBus.getDefault().post(CmdUtils.CMD6);
                            break;
                        case CmdUtils.CMD7://修改数据
//                            if (null == valueBean) {
//                                sendData(getFaileMsg(tag));
//                                return;
//                            }
//                            res = valueBean.getValue();
//                            if (!TextUtils.isEmpty(res)) {
//                                List<OrderInfo> listInfo = OrderInfoCtrls.loadAllOrderInfoListByType(Integer.parseInt(res));
//                                if (listInfo.size() > 0) {
//                                    //确认上一个候诊人信息
//                                    OrderInfoCtrls.updateOrderInfoForCur(listInfo.get(0));
//                                }
//                            }
////                          sendData(getSuccessMsg(CmdUtils.CMD6));
//                            sendListdata();
//                            EventBus.getDefault().post(CmdUtils.CMD7);

                            if (null != roomsInfo) {
                                long showNo = RoomCtrls.updateRoomInfo(roomsInfo.getId(), roomsInfo.getRoomName(), roomsInfo.getRoomTitle()
                                        , roomsInfo.getRoomDesc(), roomsInfo.getType());
                                if (showNo == -1) {
                                    sendData(getFaileMsg(CmdUtils.CMD7));
                                } else {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("code", String.valueOf("0000"));
                                    map.put("msg", String.valueOf(showNo));
                                    String info = JsonUtils.serialize(new MessageInfo<Map>(CmdUtils.CMD1, map));
                                    sendData(info);
                                    EventBus.getDefault().post(CmdUtils.CMD7);
                                }
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD7));
                            }

                            break;
                        case CmdUtils.CMD8://过号
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                OrderInfoCtrls.updateDelayOrderNoById(Long.parseLong(res));
//                                sendData(getSuccessMsg(CmdUtils.CMD8));
                                sendListdata();
                                EventBus.getDefault().post(CmdUtils.CMD8);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD8));
                            }
                            break;
                        case CmdUtils.CMD21://解除过号
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                RoomCtrls.updateRoomInfo(Long.parseLong(res));
                                sendData(getSuccessMsg(CmdUtils.CMD21));
                                EventBus.getDefault().post(CmdUtils.CMD21);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD21));
                            }
                            break;
                        case CmdUtils.CMD9://设置标题信息
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                SettingInfoCtrls.insertTitleInfo(res);
                                sendData(getSuccessMsg(CmdUtils.CMD9));
                                EventBus.getDefault().post(CmdUtils.CMD9);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD9));
                            }
                            break;
                        case CmdUtils.CMD10://跑马灯通告信息
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            NoticeInfoCtrls.insertNoticeInfo(res, String.valueOf(valueBean.getType()));
                            sendData(getSuccessMsg(CmdUtils.CMD10));
                            EventBus.getDefault().post(CmdUtils.CMD10);
                            break;
                        case CmdUtils.CMD11://设置语音是否开启
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                PreferenceUtils.putBoolean(mContext.getApplicationContext(), "isVoice", Boolean.valueOf(res));
                                sendData(getSuccessMsg(CmdUtils.CMD11));
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD11));
                            }
                            break;
                        case CmdUtils.CMD12://设置预约名单标题
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                SettingInfoCtrls.insertOrderTitleInfo(res);
                                sendData(getSuccessMsg(CmdUtils.CMD12));
                                EventBus.getDefault().post(CmdUtils.CMD12);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD12));
                            }
                            break;
                        case CmdUtils.CMD13://读取设置消息
                            boolean isVoice = PreferenceUtils.getBoolean(mContext.getApplicationContext(), "isVoice", false);
                            boolean playVideo = PreferenceUtils.getBoolean(mContext.getApplicationContext(), "playVideo", true);
                            int delayTime = PreferenceUtils.getInt(mContext.getApplicationContext(), "delayTime", 6000);
                            int rightDelayTime = PreferenceUtils.getInt(mContext.getApplicationContext(), "rightDelayTime", 6000);
                            Map<String, Object> map = new HashMap<>();
                            map.put("code", String.valueOf(playVideo));
                            map.put("msg", String.valueOf(isVoice));
                            map.put("time", String.valueOf(delayTime));
                            map.put("righttime", String.valueOf(rightDelayTime));
                            String setting = JsonUtils.serialize(new MessageInfo<Map>(CmdUtils.CMD13, map));
                            sendData(setting);
                            break;
                        case CmdUtils.CMD22://修改用户信息
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            String name = valueBean.getTag();
                            if (!TextUtils.isEmpty(res)) {
                                OrderInfoCtrls.updateOrderInfoNoById(Long.parseLong(res), name);
                                sendData(getSuccessMsg(CmdUtils.CMD21));
                                EventBus.getDefault().post(CmdUtils.CMD21);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD21));
                            }
                            break;
                        case CmdUtils.CMD14://增加节目
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                VideoInfoCtrls.insertVideoInfo("", res);
                                sendData(getSuccessMsg(CmdUtils.CMD14));
                                EventBus.getDefault().post(CmdUtils.CMD14);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD14));
                            }
                            break;
                        case CmdUtils.CMD15:
                            sendData(getSuccessMsg(CmdUtils.CMD15));
                            EventBus.getDefault().post(CmdUtils.CMD15);
                            break;
                        case CmdUtils.CMD16:
                            sendData(getSuccessMsg(CmdUtils.CMD16));
                            EventBus.getDefault().post(CmdUtils.CMD16);
                            break;
                        case CmdUtils.CMD17:
                            sendData(getSuccessMsg(CmdUtils.CMD17));
                            EventBus.getDefault().post(CmdUtils.CMD17);
                            break;
                        case CmdUtils.CMD24://统计信息
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            String time = valueBean.getValue();
                            if (TextUtils.isEmpty(time)) {
                                sendData(getFaileMsg(CmdUtils.CMD24));
                            } else {
                                Map<String, Integer> mapTj = new HashMap<>();
                                mapTj.put("tjAll", OrderInfoCtrls.loadAllOrderByTime(time));
                                mapTj.put("tjYk", OrderInfoCtrls.loadYkOrderByTime());
                                mapTj.put("tjWk", OrderInfoCtrls.loadWkOrderByTime());
                                String str = JsonUtils.serialize(new MessageInfo<Map<String, Integer>>(CmdUtils.CMD24, mapTj));
                                LogUtils.sysout("======tjInfo==" + str);
                                sendData(str);
                            }
                            break;
                        case CmdUtils.CMD25://设置媒体播放类型
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                boolean b = true;
                                if (res.equals("0")) b = true;
                                else b = false;
                                PreferenceUtils.putBoolean(mContext.getApplicationContext(), "playVideo", b);
                                sendData(getSuccessMsg(CmdUtils.CMD25));
                                EventBus.getDefault().post(CmdUtils.CMD25);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD25));
                            }
                            break;
                        case CmdUtils.CMD26://清除所有的数据
                            OrderInfoCtrls.deleteAllOrderData();
                            sendData(getSuccessMsg(CmdUtils.CMD26));
                            EventBus.getDefault().post(CmdUtils.CMD26);
                            break;
                        case CmdUtils.CMD27://设置图片时间
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                PreferenceUtils.putInt(mContext.getApplicationContext(), "delayTime", Integer.parseInt(res));
                                sendData(getSuccessMsg(CmdUtils.CMD27));
                                EventBus.getDefault().post(CmdUtils.CMD27);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD27));
                            }
                            break;
                        case CmdUtils.CMD38://设置图片时间
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                PreferenceUtils.putInt(mContext.getApplicationContext(), "rightDelayTime", Integer.parseInt(res));
                                sendData(getSuccessMsg(CmdUtils.CMD38));
                                EventBus.getDefault().post(CmdUtils.CMD38);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD38));
                            }
                            break;
                        case CmdUtils.CMD28://停止看诊
                            OrderInfoCtrls.setAllOrderDengHou();
                            sendListdata();
                            EventBus.getDefault().post(CmdUtils.CMD28);
                            break;
                        case CmdUtils.CMD29:
                            EventBus.getDefault().post(CmdUtils.CMD29);
                            sendData(getSuccessMsg(CmdUtils.CMD29));
                            break;
                        case CmdUtils.CMD30:
                            EventBus.getDefault().post(CmdUtils.CMD30);
                            sendData(getSuccessMsg(CmdUtils.CMD30));
                            break;
                        case CmdUtils.CMD31:
                            sendData(getSuccessMsg(CmdUtils.CMD31));
                            EventBus.getDefault().post(CmdUtils.CMD31);
                            break;
                        case CmdUtils.CMD32://是否显示菜单
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                PreferenceUtils.putBoolean(mContext.getApplicationContext(), "showMenu", Boolean.valueOf(res));
                                sendData(getSuccessMsg(CmdUtils.CMD32));
                                EventBus.getDefault().post(CmdUtils.CMD32);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD32));
                            }
                            break;
                        case CmdUtils.CMD33://
                            boolean isShowScreen = PreferenceUtils.getBoolean(mContext.getApplicationContext(), "isShowScreen", true);
                            boolean isShowMenu = PreferenceUtils.getBoolean(mContext.getApplicationContext(), "showMenu", false);
                            boolean isShowTvNotice = PreferenceUtils.getBoolean(mContext.getApplicationContext(), "isShowTvNotice", false);
                            Map<String, String> mapShow = new HashMap<>();
                            mapShow.put("showMenu", String.valueOf(isShowMenu));
                            mapShow.put("showScreen", String.valueOf(isShowScreen));
                            mapShow.put("showNotice", String.valueOf(isShowTvNotice));
                            mapShow.put("code", "0000");
//                            String settingShow = JsonUtils.serialize(new MessageInfo<Map>(CmdUtils.CMD33, mapShow));
                            sendData(JsonUtils.serialize(mapShow));
                            break;
                        case CmdUtils.CMD34://清空某一条资讯
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            NoticeInfoCtrls.deleteNoticeInfoByType(String.valueOf(valueBean.getType()));
                            sendData(getSuccessMsg(CmdUtils.CMD34));
                            EventBus.getDefault().post(CmdUtils.CMD10);
                            break;
                        case CmdUtils.CMD35://是否全屏
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                PreferenceUtils.putBoolean(mContext.getApplicationContext(), "isShowScreen", Boolean.valueOf(res));
                                sendData(getSuccessMsg(CmdUtils.CMD35));
                                EventBus.getDefault().post(CmdUtils.CMD35);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD35));
                            }
                            break;

                        case CmdUtils.CMD36://跑马灯
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            res = valueBean.getValue();
                            if (!TextUtils.isEmpty(res)) {
                                PreferenceUtils.putBoolean(mContext.getApplicationContext(), "isShowTvNotice", Boolean.valueOf(res));
                                sendData(getSuccessMsg(CmdUtils.CMD36));
                                EventBus.getDefault().post(CmdUtils.CMD36);
                            } else {
                                sendData(getFaileMsg(CmdUtils.CMD36));
                            }
                            break;
                        case CmdUtils.CMD37://清除
                            RoomCtrls.clearAllRoomInfo();
                            sendData(getSuccessMsg(CmdUtils.CMD37));
                            EventBus.getDefault().post(CmdUtils.CMD37);
                            break;
                        case CmdUtils.CMD39://上传参数设置
                            if (null == valueBean) {
                                sendData(getFaileMsg(tag));
                                return;
                            }
                            ConfigUtils.LOCATION_TYPE = Integer.parseInt(valueBean.getValue());
                            ConfigUtils.MEDIAO_TYPE = valueBean.getType();
                            sendData(getSuccessMsg(CmdUtils.CMD39));
                            break;
                        case CmdUtils.CMD40:
                            SendFileUtils.deleteFile(new File(FileUtils.getSdcardPath().concat("/smartinfo")));
                            sendData(getSuccessMsg(CmdUtils.CMD40));
                            EventBus.getDefault().post(CmdUtils.CMD40);
                            break;

                    }
                } catch (JsonSyntaxException e) {
                    LogUtils.sysout("=====解析数据异常", e.toString());
                    NaviDebug.getInstance().saveLog("接受数据异常:" + e.toString());
                    sendData(getFaileMsg(CmdUtils.CMD100));
                }

            }
        } catch (Exception ex) {
            try {
                mSocket.close();
                System.out.println("系统异常1");
                ex.printStackTrace();
            } catch (Exception e) {
                System.out.println("系统异常2");
                e.printStackTrace();
            }
        }
    }


    private void sendListdata() {
        List<RoomsInfo> list = RoomCtrls.loadAllRoomInfo();
        String info = JsonUtils.serialize(new MessageInfo<List<RoomsInfo>>(CmdUtils.CMD4, list));
        LogUtils.sysout("info", info);
        sendData(info);
    }

    private String getFaileMsg(String tag) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "9999");
        map.put("msg", "NO");
        return JsonUtils.serialize(new MessageInfo<Map<String, String>>(tag, map));
    }

    private String getSuccessMsg(String tag) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "0000");
        map.put("msg", "YES");
        return JsonUtils.serialize(new MessageInfo<Map<String, String>>(tag, map));
    }
}


public class SmartServerSocket extends Thread {
    private static ServerSocket m_ServerSocket;
    private static Socket socket;
    private int m_nListernPort;
    public static boolean isRun;
    private Context mContext;

    public SmartServerSocket(Context context, int nListernPort) {
        m_nListernPort = nListernPort;
        isRun = true;
        this.mContext = context;
        try {
            m_ServerSocket = new ServerSocket(m_nListernPort);
            this.run();

        } catch (IOException ex) {
            System.out.println("创建SERVER失败");
        }
    }

    public void run() {
        System.out.println("Ok! waiting for client......");
        while (isRun) {
            try {
                System.out.println("OK,Accepting.....");
                socket = m_ServerSocket.accept();
                System.out.println("OK,Accepted ! host is : " + socket.getRemoteSocketAddress());
                new S411Socket(mContext, socket).start();
                System.out.println("=======客户端IP地址：" + socket.getInetAddress());
            } catch (Exception ex) {
                System.out.println("=======服务异常");
                ex.printStackTrace();
            }
        }
    }

    /**
     * 关闭服务
     */
    public void closeSocket() {
        try {
            isRun = false;
            if (null != m_ServerSocket)
                m_ServerSocket.close();
            if (null != socket)
                socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("=======关闭服务");
        }

    }

}
