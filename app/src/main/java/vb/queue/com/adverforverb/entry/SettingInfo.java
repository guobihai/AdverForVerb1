package vb.queue.com.adverforverb.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gbh on 2018/8/28  14:34.
 *
 * @describe 标题信息设置类
 */
@Entity
public class SettingInfo {
    @Id (autoincrement = true)
    private Long ID;
    @Keep
    private String name;
    @Keep
    private int type;

    @Generated(hash = 1310033936)
    public SettingInfo(Long ID, String name, int type) {
        this.ID = ID;
        this.name = name;
        this.type = type;
    }
    @Generated(hash = 598560875)
    public SettingInfo() {
    }


    public SettingInfo(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public SettingInfo(String name) {
        this.name = name;
        this.type = 1;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public Long getID() {
        return this.ID;
    }
}
