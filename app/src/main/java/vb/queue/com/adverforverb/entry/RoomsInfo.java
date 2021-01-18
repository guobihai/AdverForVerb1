package vb.queue.com.adverforverb.entry;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

import vb.queue.com.adverforverb.utils.Tool;

/**
 * 房间信息
 */
@Entity
public class RoomsInfo {
    @org.greenrobot.greendao.annotation.Id(autoincrement = true)
    private Long Id;
    @Keep
    private String roomName;
    @Keep
    private String roomTitle;
    @Keep
    private String roomDesc;
    @Keep
    private int type;//使用类型 0未使用，1，已使用
    @Keep
    private String remark;//备注

    @Keep
    private String time;//操作时间


    public RoomsInfo(String roomName, String roomTitle, String roomDesc, int type) {
        this.roomName = roomName;
        this.roomTitle = roomTitle;
        this.roomDesc = roomDesc;
        this.type = type;
        this.time = Tool.getYouXiaoQi();
    }

    @Generated(hash = 395876773)
    public RoomsInfo(Long Id, String roomName, String roomTitle, String roomDesc,
                     int type, String remark, String time) {
        this.Id = Id;
        this.roomName = roomName;
        this.roomTitle = roomTitle;
        this.roomDesc = roomDesc;
        this.type = type;
        this.remark = remark;
        this.time = time;
    }

    @Generated(hash = 2050971085)
    public RoomsInfo() {
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getRoomName() {
        return TextUtils.isEmpty(this.roomName) ? "" : this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomTitle() {
        return TextUtils.isEmpty(this.roomTitle) ? "" : this.roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getRoomDesc() {
        return TextUtils.isEmpty(this.roomDesc) ? "" : this.roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
