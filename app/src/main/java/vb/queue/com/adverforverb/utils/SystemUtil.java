package vb.queue.com.adverforverb.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkProperties;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;

public class SystemUtil {
    /**
     * @return
     */
    public static String getLocalHostIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        }
        return getLocalIp();

//            try {
//            Enumeration<NetworkInterface> en2 = NetworkInterface.getNetworkInterfaces();
//            while (en2.hasMoreElements()) {
//                NetworkInterface networkInterface = (NetworkInterface) en2.nextElement();
//                Enumeration<InetAddress> inet = networkInterface.getInetAddresses();
//                while (inet.hasMoreElements()) {
//                    InetAddress inetAddress = (InetAddress) inet.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) {
//                        return inetAddress.getAddress().toString();
//                    }
//                }
//
//            }

//                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
//                     en.hasMoreElements(); ) {
//                    NetworkInterface intf = en.nextElement();
//                    if (intf.getName().toLowerCase().equals("eth0") ||
//                            intf.getName().toLowerCase().equals("wlan0")) {
//                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
//                             enumIpAddr.hasMoreElements(); ) {
//                            InetAddress inetAddress = enumIpAddr.nextElement();
//                            if (!inetAddress.isLoopbackAddress()) {
//                                String ipaddress = inetAddress.getHostAddress().toString();
//                                if (!ipaddress.contains("::")) {//ipV6的地址
//                                    return ipaddress;
//                                }
//                            }
//                        }
//                    } else {
//                        continue;
//                    }
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                return "0.0.0.0";
//            }
//            return "0.0.0.0";
//        }
    }


    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    /**
     * @return
     */
    public static int getVersionCode(Context context) {
        int a = 0;
        try {
            a = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return a;
    }

    /**
     * @return
     */
    public static String getVersionName(Context context) {
        String a = "";
        try {
            a = "V" + context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return a;
    }

    /**
     * 获取通道
     *
     * @param context
     * @param key
     * @return
     */
    public static String getChanel(Context context, String key) {
        //注意：key是步骤一中的meta的name属性，即“APP_PLANTFORM_KEY”
        PackageManager pm = context.getPackageManager();
        ApplicationInfo appInfo = null;
        try {
            appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 得到有限网关的IP地址
     *
     * @return
     */
    private static String getLocalIp() {
        try {
            // 获取本地设备的所有网络接口
            Enumeration<NetworkInterface> enumerationNi = NetworkInterface
                    .getNetworkInterfaces();
            while (enumerationNi.hasMoreElements()) {
                NetworkInterface networkInterface = enumerationNi.nextElement();
                String interfaceName = networkInterface.getDisplayName();
                Log.i("tag", "网络名字" + interfaceName);

                // 如果是有限网卡
                if (interfaceName.equals("eth0")) {
                    Enumeration<InetAddress> enumIpAddr = networkInterface
                            .getInetAddresses();

                    while (enumIpAddr.hasMoreElements()) {
                        // 返回枚举集合中的下一个IP地址信息
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        // 不是回环地址，并且是ipv4的地址
                        if (!inetAddress.isLoopbackAddress()
                                && inetAddress instanceof Inet4Address) {
                            Log.i("tag", inetAddress.getHostAddress() + "   ");
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";

    }


}
