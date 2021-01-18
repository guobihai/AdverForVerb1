package vb.queue.com.adverforverb.entry;


import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import vb.queue.com.adverforverb.greendao.ctrls.OrderInfoCtrls;
import vb.queue.com.adverforverb.utils.LogUtils;
import vb.queue.com.adverforverb.utils.Tool;

/**
 * Created by gbh on 2018/8/28  16:05.
 *
 * @describe 排队类
 */
@Entity
public class OrderInfo {
    @Id(autoincrement = true)
    private Long ID;
    @Keep
    private String time;//创建时间 yyyyMMdd
    @Keep
    private String fullTime;//创建时间yyyyMMdd HHmmss
    @Keep
    private String roomName;//房间名
    @Keep
    private String name;//姓名或标题
    @Keep
    private String content;//内容
    @Keep
    private int orderNo;//系统默认排序序号 (1：优先排序 4：设定过号排序  3:正常排序)
    @Keep
    private int showOrderNo;//自定义显示的序号
    @Keep
    private int setOrderNo;//优先排序序号
    @Keep
    private int type;//1:已使用，0：未使用


    @Generated(hash = 1695813404)
    public OrderInfo() {
    }

    public OrderInfo(String name) {
        this.name = name;
        this.time = Tool.getSystemTime();
        this.fullTime = Tool.getYouXiaoQi();
        this.type = 2;
        this.orderNo = 3;
        this.showOrderNo = OrderInfoCtrls.getCurOrderNo();
        LogUtils.sysout("======showOrderNo====", showOrderNo);
    }

    public OrderInfo(String name, int showOrderNo, int type) {
        this.name = name;
        this.showOrderNo = showOrderNo;
        this.type = type;
        this.time = Tool.getSystemTime();
        this.fullTime = Tool.getYouXiaoQi();
        this.orderNo = 3;
    }

    @Generated(hash = 307080518)
    public OrderInfo(Long ID, String time, String fullTime, String roomName,
            String name, String content, int orderNo, int showOrderNo,
            int setOrderNo, int type) {
        this.ID = ID;
        this.time = time;
        this.fullTime = fullTime;
        this.roomName = roomName;
        this.name = name;
        this.content = content;
        this.orderNo = orderNo;
        this.showOrderNo = showOrderNo;
        this.setOrderNo = setOrderNo;
        this.type = type;
    }


    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        if(TextUtils.isEmpty(name))return "";

        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getShowOrderNo() {
        return this.showOrderNo;
    }

    public void setShowOrderNo(int showOrderNo) {
        this.showOrderNo = showOrderNo;
    }

    public int getSetOrderNo() {
        return this.setOrderNo;
    }

    public void setSetOrderNo(int setOrderNo) {
        this.setOrderNo = setOrderNo;
    }


    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFullTime() {
        return this.fullTime;
    }

    public void setFullTime(String fullTime) {
        this.fullTime = fullTime;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getID() {
        return this.ID;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
