package vb.queue.com.adverforverb.tcp;

import android.content.Context;
import android.text.TextUtils;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import vb.queue.com.adverforverb.entry.MessageInfo;

import vb.queue.com.adverforverb.utils.JsonUtils;
import vb.queue.com.adverforverb.utils.SendFileUtils;

/**
 * 监听服务
 */
class S411FileSocket extends Thread {


    private Socket mSocket;
    private byte[] mucRecvData = new byte[1024 * 1024];
    private int mnRecvLength;
    private Context mContext;

    S411FileSocket(Context context, Socket s) {
        this.mSocket = s;
        this.mContext = context;
    }


    private void sendData(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        byte[] ucData = msg.getBytes();
        try {
            mSocket.getOutputStream().write(ucData, 0, ucData.length);
            mSocket.getOutputStream().flush();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        try {
            mSocket.setKeepAlive(true);
//            while (SmartServerSocket.isRun) {

                SendFileUtils.ReceiveFile(mSocket);

//            }
        } catch (Exception ex) {
            try {
                mSocket.close();
                System.out.println("系统异常1");
                ex.printStackTrace();
            } catch (Exception e) {
                System.out.println("系统异常2");
                e.printStackTrace();
            }
        }
    }


    private String getFaileMsg(String tag) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "9999");
        map.put("msg", "NO");
        return JsonUtils.serialize(new MessageInfo<Map<String, String>>(tag, map));
    }

    private String getSuccessMsg(String tag) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "0000");
        map.put("msg", "YES");
        return JsonUtils.serialize(new MessageInfo<Map<String, String>>(tag, map));
    }
}


public class SmartServerFileSocket extends Thread {
    private static ServerSocket m_ServerSocket;
    private static Socket socket;
    private int m_nListernPort;
    public static boolean isRun;
    private Context mContext;

    public SmartServerFileSocket(Context context, int nListernPort) {
        m_nListernPort = nListernPort;
        isRun = true;
        this.mContext = context;
        try {
            m_ServerSocket = new ServerSocket(m_nListernPort);
            this.run();

        } catch (IOException ex) {
            System.out.println("创建SERVER失败");
        }
    }

    public void run() {
        System.out.println("Ok! waiting for client...file...");
        while (isRun) {
            try {
                System.out.println("OK,Accepting...file..");
                socket = m_ServerSocket.accept();
                System.out.println("OK,Accepted ! file host is : " + socket.getRemoteSocketAddress());
                new S411FileSocket(mContext, socket).start();
                System.out.println("=======客户端IP地址：" + socket.getInetAddress());
            } catch (Exception ex) {
                System.out.println("=======服务异常");
                ex.printStackTrace();
            }
        }
    }

    /**
     * 关闭服务
     */
    public void closeSocket() {
        try {
            isRun = false;
            if (null != m_ServerSocket)
                m_ServerSocket.close();
            if (null != socket)
                socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("=======关闭服务");
        }

    }

}
