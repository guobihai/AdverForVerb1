package vb.queue.com.adverforverb.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import vb.queue.com.adverforverb.utils.Tool;

/**
 * Created by gbh on 2018/8/28  16:09.
 *
 * @describe 公告信息
 */
@Entity
public class NoticeInfo {
    @Id(autoincrement = true)
    private Long ID;
    @Keep
    private String title;//标题
    @Keep
    private String content;//内容
    @Keep
    private String time;//时间
    @Keep
    private String author;//发布人


    private boolean isCurent;//是否当前播放的

    @Generated(hash = 1618824191)
    public NoticeInfo(Long ID, String title, String content, String time,
                      String author, boolean isCurent) {
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.time = time;
        this.author = author;
        this.isCurent = isCurent;
    }

    public NoticeInfo(String content) {
        this.content = content;
        this.title = content;
        this.time = Tool.getYouXiaoQi();
        this.isCurent = true;
    }

    public NoticeInfo(String content, String author) {
        this.content = content;
        this.author = author;
        this.title = content;
        this.time = Tool.getYouXiaoQi();
        this.isCurent = true;
    }

    @Generated(hash = 426617346)
    public NoticeInfo() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getID() {
        return this.ID;
    }

    public boolean getIsCurent() {
        return this.isCurent;
    }

    public void setIsCurent(boolean isCurent) {
        this.isCurent = isCurent;
    }
}
