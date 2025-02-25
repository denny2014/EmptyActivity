package com.zet;

import com.bdkj.bdlibrary.utils.StorageUtils;

import java.io.File;

/**
 * 常量定义
 * Created by macchen on 15/4/3.
 */
public class Constants {

	/**
	 * 是否需要加密
	 */
	public final static boolean needDecrypt = true;
    /**
     * 默认的服务器ip地址
     */
    public final static String DEFAULT_SERVER_IP = "121.40.209.169";

    public final static boolean is_YS = false;
    /**
     * 演示
     */
    public final static String RELATIVE_URL = "/api/androidys.php";
//    public final static String RELATIVE_URL = "/api/androidkf.php";
    /**
     * 正式
     */
//    public final static String RELATIVE_URL = "/api/android1.php";
    public final static String RELATIVE_URL_TEMP = "/api/show.php?";
    ///api/androidtest.php   /api/android1.php


    /**
     * 设置服务器ip
     * @param ip
     */
    public static void initServerIP(String ip) {
        SERVER_IP = "http://" + ip;
        SERVER_URL = SERVER_IP + RELATIVE_URL;
        OTHER_SERVER_URL = SERVER_IP + RELATIVE_URL;
        PDF_DOWNLOAD_URL = SERVER_IP + "/upload2/";

    }
    //正式
    public static String SERVER_IP = "http://121.40.209.169";
    //测试
//    public static String SERVER_IP = "http://121.43.231.19";
    public static  String[] service_urls_text = new String[] { "服务器一", "服务器二"};
    public static String[]  service_urls_value = new String[]{"121.40.209.169", "121.43.231.19"};
    /**
     * 服务器地址
     */
    public static String SERVER_URL = SERVER_IP + RELATIVE_URL;
    /**
     * 服务器地址
     */
    public static String SERVER_URL_TEMP = SERVER_IP + RELATIVE_URL_TEMP;

    /**
     * 另外一个服务器地址
     */
    public static String OTHER_SERVER_URL = SERVER_IP + RELATIVE_URL;

    /**
     * 应用程序的根目录
     */
    public final static String ROOT_DIRECTION = "/ZET";

    /**
     * 是否直接用IMEI登录
     */
    public final static boolean USE_IMEI = true;

    /**
     * 参数中的debug值
     */
    public final static String PARAMS_DEBUG_VALUE = "1";

    /**
     * 判断通信返回是否成功的标志
     */
    public final static String RESULT_KEY = "status";

    /**
     * 通信成功的标志
     */
    public final static int SUCCESS_CODE = 1;

    /**
     * 直接用IMEI登录操作
     */
    public final static String LOGIN_IMEI_ACTION = "loginimei";
    /**
     * 使用用户名和密码登录操作
     */
    public final static String LOGIN_ACTION = "login";
    /**
     * 获取部门列表操作
     */
    public final static String DEPARTMENT_ACTION = "getdep";
    /**
     * 获取搜索列表操作
     */
    public final static String SEARCH_ACTION = "getlist";
    /**
     * 获取列表详情操作
     */
    public final static String DETAIL_ACTION = "getdescbyfileid";
    /**
     * 检查版本操作
     */
    public final static String CHECK_VERSION_ACTION = "checkver";
    /**
     * 获取文档日期（年）操作
     */
    public final static String GET_DOC_YEARS_ACTION = "getyear";
    /**
     * 修改密码操作
     */
    public final static String CHANGE_PSW_ACTION = "changepasswd";

    /**
     * 获取公司名称
     */
    public final static String GETCOMMPANY_ACTION = "getnamebyimei";

    /**
     * pdf下载路径
     */
    public static String PDF_DOWNLOAD_URL = SERVER_IP + "/upload2/";

    /**
     * 程序目录下最大的文件夹个数
     */
    public final static int MAX_DIRECTORY_SIZE = 100;

    /**
     * 每个目录下的最大文件个数
     */
    public final static int MAX_DIRECTORY_FILE_SIZE = 1000;

    /**
     * 获取下载文件的路径
     *
     * @param fileName
     * @return
     */
    public final static String getDownloadUrl(String fileName) {
        return PDF_DOWNLOAD_URL + fileName;
    }

    /**
     * 寻找文件
     *
     * @param fileName
     * @return
     */
    public final static File findFile(String fileName) {
        File root = new File(StorageUtils.getStorageFile(), ROOT_DIRECTION);
        for (int i = 0; i <= Constants.MAX_DIRECTORY_SIZE; i++) {
            File file = (i == 0 ? new File(root, fileName) : new File(root, i + File.separator + fileName));
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    /**
     * 寻找可保存的目录
     *
     * @return
     */
    public final static File findViableSaveDir() {
        File root = new File(StorageUtils.getStorageFile(), ROOT_DIRECTION);
        for (int i = 1; i <= Constants.MAX_DIRECTORY_SIZE; i++) {
            File dir = new File(root, i + "");
            if (dir.isDirectory() && dir.exists()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    if (files.length < Constants.MAX_DIRECTORY_FILE_SIZE) {
                        return dir;
                    }
                }
            } else if (!dir.exists()) {
                return dir;
            }
        }
        return null;
    }
}
