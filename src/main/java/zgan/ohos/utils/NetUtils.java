package zgan.ohos.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtils {
	private NetUtils()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");
    }  
  
    /** 
     * �ж������Ƿ����� 
     *  
     * @param context 
     * @return 
     */  
    public static boolean isConnected(Context context)
    {  
  
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
  
        if (null != connectivity)  
        {  
  
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected())  
            {  
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
  
    /** 
     * �ж��Ƿ���wifi���� 
     */  
    public static boolean isWifi(Context context)
    {  
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
  
        if (cm == null)  
            return false;  
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
  
    }  
  
    /** 
     * ���������ý��� 
     */  
    public static void openSetting(Activity activity)
    {  
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");  
        intent.setComponent(cm);  
        intent.setAction("android.intent.action.VIEW");  
        activity.startActivityForResult(intent, 0);  
    }
    //解析登录服务器IP
    private static String toGetHostIP(String domin) {
        InetAddress x;
        String strIP = "0,0,0,0";

        try {
            x = java.net.InetAddress.getByName(domin);
            strIP = x.getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block

        }
        return strIP;
    }
}
