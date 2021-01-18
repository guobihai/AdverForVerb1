package vb.queue.com.adverforverb.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gbh on 2018/8/28  16:04.
 *
 * @describe 视频类
 */
@Entity
public class VideoInfo {
    @Id (autoincrement = true)
    private Long ID;
    @Keep
    private String title;
    @Keep
    private String url;
    @Keep
    private int type;
    @Generated(hash = 1458226052)
    public VideoInfo(Long ID, String title, String url, int type) {
        this.ID = ID;
        this.title = title;
        this.url = url;
        this.type = type;
    }

    public VideoInfo(String title, String url) {
        this.title = title;
        this.url = url;
        this.type = 1;
    }

    @Generated(hash = 296402066)
    public VideoInfo() {
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
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
