package vb.queue.com.adverforverb.entry;

import android.text.TextUtils;

/**
 * Created by gbh on 2018/8/30  13:33.
 *
 * @describe
 */

public class ResData {


    /**
     * tag : C0FF01
     * value : {"tag":"33333"}
     */

    private String tag;
    private ValueBean value;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ValueBean getValue() {
        return value;
    }

    public void setValue(ValueBean value) {
        this.value = value;
    }

    public static class ValueBean {

        private String tag;
        private String value;
        private int type;

        public String getTag() {
            return TextUtils.isEmpty(tag) ? "" : tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getValue() {
            return TextUtils.isEmpty(value) ? "" : value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
