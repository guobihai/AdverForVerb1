package vb.queue.com.adverforverb.utils;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import vb.queue.com.adverforverb.entry.MessageInfo;
import vb.queue.com.adverforverb.tcp.ConfigUtils;

public class SendFileUtils {

    /**
     * 发送文件
     * Socket name = new Socket(ipAddress, port);
     * OutputStream outputName = name.getOutputStream();
     * OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
     * BufferedWriter bwName = new BufferedWriter(outputWriter);
     * bwName.write(fileName);
     * bwName.close();
     * outputWriter.close();
     * outputName.close();
     * name.close();
     * <p>
     * Socket data = new Socket(ipAddress, port);
     *
     * @return
     */
    public int SendFile(Socket data, String path) {
        try {
            if (TextUtils.isEmpty(path)) return -1;
            OutputStream outputData = data.getOutputStream();
            FileInputStream fileInput = new FileInputStream(path);
            int size = -1;
            byte[] buffer = new byte[1024];
            while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                outputData.write(buffer, 0, size);
            }
            outputData.close();
            fileInput.close();
            data.close();
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    // 文件接收方法
    public static int ReceiveFile(Socket socket) {
        try {
//            // 接收文件名
//            Socket name = server.accept();
//            InputStream nameStream = name.getInputStream();
//            InputStreamReader streamReader = new InputStreamReader(nameStream);
//            BufferedReader br = new BufferedReader(streamReader);
//            String fileName = br.readLine();
//            br.close();
//            streamReader.close();
//            nameStream.close();
//            name.close();

            String rootPath = "";
            String fileType = ".png";
            if (ConfigUtils.MEDIAO_TYPE == 1) {
                fileType = ".mp4";
                rootPath = FileUtils.getSdcardPath().concat("/smartinfo/movies");
            } else {
                if (ConfigUtils.LOCATION_TYPE == 0) {
                    rootPath = FileUtils.getSdcardPath().concat("/smartinfo/images");
                } else {
                    rootPath = FileUtils.getSdcardPath().concat("/smartinfo/rightimages");
                }
            }


            // 接收文件数据
            InputStream dataStream = socket.getInputStream();
            //"/sdcard/smartinfo/images"
            File dir = new File(rootPath); // 创建文件的存储路径
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String savePath = rootPath + "/" + System.currentTimeMillis() + fileType; // 定义完整的存储路径
            FileOutputStream file = new FileOutputStream(savePath, false);
            byte[] buffer = new byte[1024 * 1024];
            int size = -1;
            while ((size = dataStream.read(buffer)) != -1) {
                file.write(buffer, 0, size);
            }
            file.close();
            ConfigUtils.TEMP_PATH = savePath;
            EventBus.getDefault().post(CmdUtils.CMD39);
            socket.getOutputStream().flush();
            dataStream.close();
            socket.close();
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    public static void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //打印文件名
            String name = file.getName();
            System.out.println(name);
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFile(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }
}
