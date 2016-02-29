package zgan.ohos.utils;

import android.content.res.Resources;
import android.os.Handler;

import zgan.ohos.MyApplication;
import zgan.ohos.services.login.ZganLoginService;

public class SystemUtils {
    public static int getScreenOrientation() {
        return Resources.getSystem().getConfiguration().orientation;
    }

    private static boolean isLogin=true;
    public static final String FORRESULT="forresult";
    public static boolean getIsLogin()
    {
        return isLogin;
    }
    public static void setIsLogin(boolean islogin)
    {
        isLogin=islogin;
    }

}
