package vb.queue.com.adverforverb.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static String ROOTPATH = Environment.getExternalStorageDirectory().getPath();
    //U盘路径
    public static String ROOTPATH1 = "/mnt/sda/sda1";

    /**
     * 获取外置SD卡路径
     *
     * @return 应该就一条记录或空
     */
    private static List<String> getExtSDCardPath() {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }


    public static String getSdcardPath() {
        File file = new File(ROOTPATH1);
        if (file.isDirectory()) return ROOTPATH1;
        return ROOTPATH;
    }

    public static ArrayList<String> findListFile() {
        ArrayList<String> list = new ArrayList<>();
        File scanner5Directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/info/Camera/");
        LogUtils.sysout("=====ab=====" + scanner5Directory.getAbsolutePath());
        for (File file : scanner5Directory.listFiles()) {
            String path = file.getAbsolutePath();
            if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
                list.add(path);
            }
        }

        return list;
    }


    public static ArrayList<File> findListFile1() {
        ArrayList<File> list = new ArrayList<>();
//        File scanner5Directory = new File(getSdcardPath() + "/smartinfo/images");
//        File scanner5Directory = new File("/mnt");
//        if (!scanner5Directory.exists())
//            return list;
//        findUsbListFile(scanner5Directory, list);
//        if (list.size() == 0) {
//            scanner5Directory = new File("/storage");
//            findstorageListFile(scanner5Directory, list);
//        }
//        if (list.size() == 0) {
        list.addAll(findListFile2("/smartinfo/images"));
        list.addAll(findListFile2("/smartinfo/rightImages"));
        list.addAll(findListVideoFile1());
//        }
        return list;
    }


    /**
     * 寻找U盘目录
     *
     * @param scanner5Directory
     * @param list
     */
    private static void findUsbListFile(File scanner5Directory, List<File> list) {
        if (scanner5Directory.isDirectory()) {
            File[] files = scanner5Directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory() && (file.getName().toLowerCase().startsWith("sda") ||
                            file.getName().toLowerCase().startsWith("usb") ||
                            file.getName().toLowerCase().startsWith("sdb1") ||
                            file.getName().toUpperCase().startsWith("EBFB"))) {
                        findSmartInfoListFile(file, list);
                    }
                }
            }
        }
    }

    /**
     * 寻找U盘目录
     *
     * @param scanner5Directory
     * @param list
     */
    private static void findstorageListFile(File scanner5Directory, List<File> list) {
        if (scanner5Directory.isDirectory()) {
            File[] files = scanner5Directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    findSmartInfoListFile(file, list);
                }
            }
        }
    }


    /**
     * 寻找smartinfo目录
     *
     * @param smartInfoFile
     * @param list
     */
    private static void findSmartInfoListFile(File smartInfoFile, List<File> list) {
        if (smartInfoFile.isDirectory()) {
            File[] files = smartInfoFile.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory() && file.getName().startsWith("smartinfo")) {
                        files = file.listFiles();
                        if (null != files) {
                            for (File imgFiles : files) {
//                                System.out.println("=====目录====" + imgFiles.getName());
                                //找到图片
                                if (imgFiles.isDirectory() && (imgFiles.getName().startsWith("images") ||
                                        imgFiles.getName().startsWith("rightImages"))) {
                                    files = imgFiles.listFiles();
                                    if (null != files) {
                                        for (File imgFile : files) {
                                            //判断是否有smartinfo 目录
                                            String path = imgFile.getPath();
                                            if (!imgFile.getName().startsWith("._")) {
                                                if (path.toLowerCase().endsWith(".jpg") ||
                                                        path.toLowerCase().endsWith(".jpeg") ||
                                                        path.toLowerCase().endsWith(".png")) {
                                                    list.add(imgFile);
                                                }
                                            }
                                        }
                                    }
                                } else if (imgFiles.isDirectory() && imgFiles.getName().startsWith("movies")) {//找到视频
                                    files = imgFiles.listFiles();
                                    if (null != files) {
                                        for (File imgFile : files) {
                                            String path = imgFile.getAbsolutePath();
//                                            System.out.println("===movies==path========" + path);
                                            if (path.toLowerCase().endsWith(".mp4") ||
                                                    path.toLowerCase().endsWith(".3gp") ||
                                                    path.toLowerCase().endsWith(".avi") ||
                                                    path.toLowerCase().endsWith(".wma") ||
                                                    path.toLowerCase().endsWith(".wmv") ||
                                                    path.toLowerCase().endsWith(".rmvb")) {
                                                list.add(imgFile);
                                            }
                                        }
                                    }

                                }
                            }
                        }

                    } else {
                        //继续往下一级目录寻找
                        findSmartInfoListFile(file, list);
                    }

                }
            }
        }
    }


    public static List<File> findListVideoFile() {
        List<File> list = new ArrayList<>();
        File scanner5Directory = new File("/mnt");
        if (!scanner5Directory.exists())
            return list;
        findUsbListFile(scanner5Directory, list);
        if (list.size() == 0)
            return findListVideoFile1();
        return list;
    }


    public static ArrayList<File> findListFile2(String filePath) {
        ArrayList<File> list = new ArrayList<>();
        File scanner5Directory = new File(getSdcardPath() + filePath);
//        File scanner5Directory = new File("/mnt/usb_storage2/info/Camera");
        if (!scanner5Directory.exists())
            scanner5Directory.mkdirs();
        if (scanner5Directory.isDirectory()) {
            File[] files = scanner5Directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    String path = file.getAbsolutePath();
                    if (path.toLowerCase().endsWith(".jpg") ||
                            path.toLowerCase().endsWith(".jpeg") ||
                            path.toLowerCase().endsWith(".png")) {
                        list.add(file);
                    }
                }
            }
        }

        return list;
    }


    public static List<File> findListVideoFile1() {
        List<File> list = new ArrayList<>();
        File scanner5Directory = new File(getSdcardPath()
                + "/smartinfo/movies");
        if (!scanner5Directory.exists())
            scanner5Directory.mkdirs();
        if (scanner5Directory.isDirectory()) {
            File[] files = scanner5Directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    String path = file.getAbsolutePath();
                    if (path.toLowerCase().endsWith(".mp4") ||
                            path.toLowerCase().endsWith(".3gp") ||
                            path.toLowerCase().endsWith(".avi") ||
                            path.toLowerCase().endsWith(".wma") ||
                            path.toLowerCase().endsWith(".wmv") ||
                            path.toLowerCase().endsWith(".rmvb")) {
                        list.add(file);
                    }
                }
            }
        }

        return list;
    }


    /**
     * 过滤图片
     *
     * @param fileList
     * @return
     */
    public static ArrayList<File> spitImgListFile(List<File> fileList) {
        ArrayList<File> list = new ArrayList<>();
        if (null == fileList || fileList.size() == 0) return list;
        for (File file : fileList) {
            String path = file.getAbsolutePath().toLowerCase();
            if (!file.getName().startsWith(".") && path.contains("images") && !path.contains("rightimages") && ((path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")))) {
                list.add(file);
            }
        }

        return list;
    }

    /**
     * 过滤图片
     *
     * @param fileList
     * @return
     */
    public static ArrayList<File> spitRightImgListFile(List<File> fileList) {
        ArrayList<File> list = new ArrayList<>();
        if (null == fileList || fileList.size() == 0) return list;
        for (File file : fileList) {
            String path = file.getAbsolutePath().toLowerCase();
            if (!file.getName().startsWith(".") && path.contains("rightimages") && ((path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")))) {
                list.add(file);
            }
        }

        return list;
    }

    /**
     * 过滤视频
     *
     * @param fileList
     * @return
     */
    public static String[] spitVideoListFile(List<File> fileList) {
        ArrayList<File> list = new ArrayList<>();
        if (null == fileList || fileList.size() == 0) return new String[0];

        for (File file : fileList) {
            String path = file.getAbsolutePath();
            if (path.toLowerCase().endsWith(".mp4") ||
                    path.toLowerCase().endsWith(".3gp") ||
                    path.toLowerCase().endsWith(".avi") ||
                    path.toLowerCase().endsWith(".wma") ||
                    path.toLowerCase().endsWith(".wmv") ||
                    path.toLowerCase().endsWith(".rmvb")) {
                list.add(file);
            }
        }
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = list.get(i).getAbsolutePath();
        }
        return strings;
    }

}
