package vb.queue.com.adverforverb.utils;

/**
 * Created by gbh on 17/11/3  09:23.
 *
 * @describe
 */

public class ToolDes {
    public String getValue(String str) {
        return a(str);
    }

    private String a(String str) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if (i % 2 == 0) {
                int a = ~str.charAt(i);
                b.append(a);
            } else {
                int c = ~str.charAt(i) << 2;
                b.append(c);
            }
        }
        String des = b.toString().replaceAll("-", "");
//        System.out.println(des);
        String value = des.substring(0, 4) + des.substring(des.length() - 4, des.length());
//        System.out.println(value);
        return value;
    }
}
