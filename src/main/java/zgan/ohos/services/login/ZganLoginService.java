package zgan.ohos.services.login;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import zgan.ohos.MyApplication;
import zgan.ohos.utils.Frame;
import zgan.ohos.utils.LocationUtil;
import zgan.ohos.utils.PreferenceUtil;
import zgan.ohos.utils.SystemUtils;
import zgan.ohos.utils.generalhelper;

public class ZganLoginService extends Service {

    public static boolean ServiceRin = false;
    private static Thread _threadListen;
    private static Thread _threadMain;
    private static ZganLoginService_Listen ztl;
    public static String UserName = "";
    public static String UPwd = "";
    public static String UIMIE = "";
    public static int Tag = 0;

    //正式115.29.147.12  测试60.172.246.193
    private final static String ZGAN_LOGIN_DOMAINNAME = "cloudlogin1.zgantech.com";//"cloudlogin.zgantech.com";
    private final static String ZGAN_LOGIN_IP = "115.28.202.130";
    public final static int ZGAN_LOGIN_PORT = 31001;//21000;
    public static String LoginService_IP = "115.28.202.130";

    public final static int PLATFORM_APP = 0xF;
    public final static int VERSION_1 = 0x01;
    public final static int VERSION_2 = 0x02;
    public final static int MAIN_CMD = 0x01;
    public final static String ZGAN_USERNAME = PreferenceUtil.getUserName();
    public final static String ZGAN_DBNAME = "ZGANDB";
    public final static String ZGAN_JTWSSERVER = "ZGAN_JTWSSERVER";
    public final static String ZGAN_FILESERVER = "ZGAN_FILESERVER";
    public final static String ZGAN_PUSHSERVER = "ZGAN_PUSHSERVER";
    public final static String ZGAN_SOCKETE_ERR = "zgan.ohos.ZGAN_SOCKETE_ERR";

    public static Context _zgan_context;
    private static SharedPreferences ZganInfo;
    public static int LoginServerState = 0;  //0:登录用户,1:获取IP列表,2:其它方法

    public ZganLoginService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        _zgan_context = MyApplication.context;
        toStartLoginService();
    }

    public static Handler myhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0)
            {
                toRestartLoginSerice();
            }
            if (msg.what == 1) {//autologin
                Frame frame = (Frame) msg.obj;
                String result = generalhelper.getSocketeStringResult(frame.strData);
                String[] results = result.split(",");
                if (frame.subCmd == 1 && results[0].equals("0")) {
                    SystemUtils.setIsLogin(true);
                    toGetServerData(24, 0, PreferenceUtil.getUserName(), myhandler);
                    Log.v("suntest", "自动重新登录成功");
                } else if (frame.subCmd == 24) {
                    String communityId = PreferenceUtil.getCommunityId();
                    if (results.length == 2 && results[0].equals("0")) {
                        Log.v("suntest", "小区ID：" + results[1]);
                        if (!communityId.equals(results[1])) {
                            PreferenceUtil.setCommunityId(results[1]);
                        }
                    }
                } else {
                    Log.v("suntest", "自动重新登录失败");
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 用户登录
     */
    public static void toUserLogin(String strUName, String strPwd, String strImei, Handler _handler) {
        Log.v("suntest", " log in");
        Frame f = createFrame();
        f.subCmd = 1;
        f.strData = strUName + "\t" + strPwd + "\t" + strImei + "\t0";
        f._handler = _handler;
        f.version = 1;
        ztl.toConnectServer();
        toGetData(f);

        UserName = strUName;
        UPwd = strPwd;
        UIMIE = strImei;


    }

    /**
     * 用户自动登录
     */
    public static boolean toAutoUserLogin(Handler _handler) {
        //PreferenceUtil sp= PreferenceUtil.getInstance(MyApplication.context);

        String strUserName = PreferenceUtil.getUserName(); //"15223796495";
        String strPwd = PreferenceUtil.getPassWord(); //"123456";//toGetDB(ZGAN_USERPWD);
        //"8886c1f212ae6576";//toGetDB(ZGAN_USERIMEI);

        Log.v("suntest", "auto login");
        if (!TextUtils.isEmpty(strUserName) && !TextUtils.isEmpty(strPwd)) {
            try {
                String strImei = LocationUtil.getDrivenToken(MyApplication.context, strUserName);
                toUserLogin(strUserName, strPwd, strImei, _handler);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 用户注销登录
     */
    public static void toUserQuit(Handler _handler) {
        String strUserName = PreferenceUtil.getUserName(); //getUserName();

        Frame f = createFrame();
        f.subCmd = 5;
        f.strData = strUserName;
        f._handler = _handler;

        ZganLoginService.LoginServerState = 2;

        ztl.toConnectServer();

        toGetData(f);
    }

    /**
     * 获取服务器数据(通用)
     */
    public static void toGetServerData(int subcmd, String strData, Handler _handler) {
        Frame f = createFrame();
        f.subCmd = subcmd;
        f.strData = strData;
        f._handler = _handler;

        ztl.toConnectServer();

        ZganLoginService.LoginServerState = 2;

        toGetData(f);
    }

    public static void toGetServerData(int subcmd, int zip, String strData, Handler _handler) {
        Frame f = createFrame();
        f.subCmd = subcmd;
        f.strData = strData;
        f.zip = zip;
        f._handler = _handler;

//        ztl.toConnectServer();
//
//        ZganLoginService.LoginServerState = 2;

        toGetData(f);
    }

    /**
     * 获取服务器数据(通用)
     */
    public static void toGetServerData(int subcmd, String[] aryParam, Handler _handler) {
        Frame f = createFrame();
        f.subCmd = subcmd;
        f.strData = getParam(aryParam);
        f._handler = _handler;

        ZganLoginService.LoginServerState = 2;

        ztl.toConnectServer();

        toGetData(f);
    }

    /**
     * 获取服务器数据(通用)
     */
    public static void toGetServerData(int subcmd, String[] aryParam, Handler _handler, int intVar) {
        Frame f = createFrame();
        f.subCmd = subcmd;
        f.strData = getParam(aryParam);
        f._handler = _handler;
        f.version = intVar;

        ZganLoginService.LoginServerState = 2;

        ztl.toConnectServer();

        toGetData(f);
    }

    /**
     * 获取服务器数据(通用)
     */
    public static void toGetServerData(int subcmd, String[] aryParam, Handler _handler, int intVar, int mainCmd) {
        Frame f = createFrame();
        f.mainCmd = (byte) mainCmd;
        f.subCmd = subcmd;
        f.strData = getParam(aryParam);
        f._handler = _handler;
        f.version = intVar;

        ZganLoginService.LoginServerState = 2;

        ztl.toConnectServer();

        toGetData(f);
    }

    private static String getParam(String[] aryParam) {
        String strParam = "";

        if (aryParam != null) {
            for (String oneRow : aryParam) {
                strParam += "\t" + oneRow;
            }

            if (strParam != null && !strParam.equals("")) {
                strParam = strParam.substring(1);
            }
        }

        return strParam;
    }

    /**
     * 创建数据包
     */
    public static Frame createFrame() {
        Frame f = new Frame();
        f.platform = PLATFORM_APP;
        f.mainCmd = MAIN_CMD;
        f.version = VERSION_1;
        return f;
    }

    public static void toGetData(Frame f) {
        ZganLoginServiceTools.toGetFunction(f);
    }

    public static void toRestartLoginSerice() {
        ServiceRin = false;
        ztl.toDisConnectServer();
        _threadMain.interrupt();
        _threadListen.interrupt();
        toStartLoginService();
        Log.v("suntest","重新登录");
        toAutoUserLogin(myhandler);
    }

    //启动登录服务线程
    public static void toStartLoginService() {
        if (!ServiceRin) {
            Log.v("suntest", "start service");
            LoginService_IP = toGetHostIP();
            Log.v("suntest", "get host ip");
            //_zgan_context = context;

            ZganInfo = _zgan_context.getSharedPreferences(ZGAN_DBNAME, Context.MODE_PRIVATE);

            //启动监听线程
            ztl = new ZganLoginService_Listen(_zgan_context);
            _threadListen = new Thread(ztl);
            _threadListen.start();

            //启动主线程
            ZganLoginService_Main zm = new ZganLoginService_Main(ZganLoginServiceTools.PushQueue_Receive,
                    ZganLoginServiceTools.PushQueue_Function);
            _threadMain = new Thread(zm);
            _threadMain.start();

            ServiceRin = true;
        }
    }

    private static boolean toSaveDB(String strKey, String strValue) {
        Editor editor = ZganInfo.edit();

        editor.putString(strKey, strValue);
        return editor.commit();
    }

    public static String toGetDB(String strKey) {
        if (ZganInfo.getString(strKey, null) != null) {
            return ZganInfo.getString(strKey, null);
        } else {
            return null;
        }
    }

    //解析登录服务器IP
    public static String toGetHostIP() {
        InetAddress x;
        String strIP = ZGAN_LOGIN_IP;

        try {
            x = java.net.InetAddress.getByName(ZGAN_LOGIN_DOMAINNAME);
            strIP = x.getHostAddress();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return strIP;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {

        } else {
            //如果仅仅是用来判断网络连接则可以使用 cm.getActiveNetworkInfo().isAvailable();  
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void BroadError(String error) {
        Intent intent = new Intent(ZganLoginService.ZGAN_SOCKETE_ERR);
        Bundle bundle = new Bundle();
        bundle.putString("msg", error);
        intent.putExtras(bundle);
        MyApplication.context.sendBroadcast(intent);
    }
}
