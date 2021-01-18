package vb.queue.com.adverforverb.apps;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import androidx.multidex.MultiDex;

//import com.tencent.bugly.Bugly;

//import com.tencent.bugly.Bugly;

import vb.queue.com.adverforverb.greendao.DaoMaster;
import vb.queue.com.adverforverb.greendao.DaoSession;

/**
 * Created by gbh on 2018/8/28  21:29.
 *
 * @describe
 */

public class MyApps extends Application {
    private static DaoSession sDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "smartOrder.db", null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        sDaoSession = daoMaster.newSession();
//        Bugly.init(this,"8101f750f6",false);
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
