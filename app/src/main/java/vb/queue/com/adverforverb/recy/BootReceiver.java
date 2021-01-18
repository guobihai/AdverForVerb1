package vb.queue.com.adverforverb.recy;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

import vb.queue.com.adverforverb.utils.SystemUtil;

/**
 * Created by gbh on 2018/4/16  20:23.
 *
 * @describe
 */

public class BootReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("charge start" + "启动完成");

        if (null != intent.getAction() && intent.getAction().equals(action_boot)) {
//            Intent mBootIntent = new Intent(context, SettingsActivity.class);
//            // 下面这句话必须加上才能开机自动运行app的界面
//            mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mBootIntent);

//            openWIFI(context);
            doStartApplicationWithPackageName(context, "vb.queue.com.adverforverb");
        }
    }

    private void doStartApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            // 下面这句话必须加上才能开机自动运行app的界面
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    private WifiInfo openWIFI(Context context) {
        try {
            int nCount = 0;
            WifiInfo wifiInfo = null;
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            while (nCount < 50) {
                if (wifiManager.isWifiEnabled() && isWifiConnected(context)) {
//                    CGenUtil.OutputLog("wifi连接" + nCount);
                    System.out.println("charge wifi 🔗" + "启动完成");
//                    wifiInfo = wifiManager.getConnectionInfo();
//                    String chanel = SystemUtil.getChanel(context, "UMENG_CHANNEL");
                    doStartApplicationWithPackageName(context, "vb.queue.com.adverforverb");
//                    if (chanel.equals("doctor")) {
//                    } else if (chanel.equals("qucan")) {
//                        doStartApplicationWithPackageName(context, "vb.queue.com.queueforverb1");
//                    }
                    break;
                } else {
//                    CGenUtil.OutputLog("等待wifi连接" + nCount);
                    if (nCount == 0) {
                        wifiManager.setWifiEnabled(true);
                    }
                    nCount++;
                    Thread.sleep(400);
                }
            }

            return wifiInfo;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private boolean isWifiConnected(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
    }

}
