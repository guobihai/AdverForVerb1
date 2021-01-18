package vb.queue.com.adverforverb.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import vb.queue.com.adverforverb.tcp.SmartServerFileSocket;
import vb.queue.com.adverforverb.tcp.SmartServerSocket;

/**
 * Created by gbh on 2018/8/28  11:59.
 *
 * @describe
 */

public class SmartServices extends Service {
    private SmartServerSocket mSmartServerSocket;
    private SmartServerFileSocket mSmartServerfileSocket;
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        new RunSmartTask().start();
        new RunFileSmartTask().start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mSmartServerSocket)
            mSmartServerSocket.closeSocket();
        if (null != mSmartServerfileSocket) {
            mSmartServerfileSocket.closeSocket();
        }
    }

    class RunSmartTask extends Thread {
        @Override
        public void run() {
            super.run();
            mSmartServerSocket = new SmartServerSocket(mContext, 10002);
            mSmartServerSocket.start();
        }
    }
    class RunFileSmartTask extends Thread {
        @Override
        public void run() {
            super.run();

            mSmartServerfileSocket = new SmartServerFileSocket(mContext, 10003);
            mSmartServerfileSocket.start();
        }
    }

}
