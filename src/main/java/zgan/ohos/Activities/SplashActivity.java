package zgan.ohos.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import java.util.Calendar;

import zgan.ohos.GuideIndexActivity;
import zgan.ohos.MyApplication;
import zgan.ohos.R;
import zgan.ohos.services.login.ZganLoginService;
import zgan.ohos.utils.Frame;
import zgan.ohos.utils.PreferenceUtil;
import zgan.ohos.utils.SystemUtils;
import zgan.ohos.utils.generalhelper;

public class SplashActivity extends myBaseActivity {


    Calendar etime;
    Calendar btime;
    private Thread t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_splash2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                btime = Calendar.getInstance();
                Looper.prepare();
                try {
                    Intent intent = new Intent(SplashActivity.this, ZganLoginService.class);
                    startService(intent);
                    // 检查网络是否正常
                    if (!ZganLoginService.isNetworkAvailable(MyApplication.context)) {
                        handler.sendEmptyMessage(0);
                    } else {
                        // 判断自动登录
                        try {
//                            String userName = PreferenceUtil.getUserName();
//                            String userPwd = PreferenceUtil.getPassWord();
                            // SystemUtils.login(userName, userPwd, handler);
                            while (!ZganLoginService.ServiceRin) {
                                Thread.currentThread().sleep(100);
                            }
                            if (!ZganLoginService.toAutoUserLogin(handler)) {
                                Thread_TimerToActivity tt = new Thread_TimerToActivity();
                                t1 = new Thread(tt);
                                t1.start();
                            }
                        } catch (Exception ex) {
                            generalhelper.ToastShow(SplashActivity.this, ex.getMessage());
                        }
                    }
                    etime = Calendar.getInstance();
                    long costtime = etime.getTimeInMillis() - btime.getTimeInMillis();
                    long time = 2000;
                    if (costtime < time) {
                        try {
                            Thread.sleep(time - costtime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendEmptyMessage(2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void ViewClick(View v) {

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//error
//                Intent intent = new Intent(SplashActivity.this, Login.class);
//                startActivityWithAnim(intent);
                generalhelper.ToastShow(SplashActivity.this, "网络未连接");
                //finish();
            } else if (msg.what == 1) {//autologin
                Frame frame = (Frame) msg.obj;
                String result = generalhelper.getSocketeStringResult(frame.strData);
                if (frame.subCmd == 1 && result.equals("0")) {
                    SystemUtils.setIsLogin(true);
                }
            } else if (msg.what == 2)//main
            {
                int usedTimes = PreferenceUtil.getUsedTimes();
                int lastVersion = PreferenceUtil.getLastVersion();
                int thisVersion = 1;
                try {
                    thisVersion = getPackageManager().getPackageInfo("zgan.ohos", PackageManager.GET_CONFIGURATIONS).versionCode;
                } catch (Exception e) {
                }
                if (usedTimes == 0 || (lastVersion != thisVersion)) {
                    if (lastVersion != thisVersion) {
                        PreferenceUtil.setLastVersion(thisVersion);
                        PreferenceUtil.setUsedTimes(0);
                    }
                    handler.sendEmptyMessageDelayed(3, 500);
                } else {
                    PreferenceUtil.setUsedTimes(usedTimes + 1);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivityWithAnim(intent);
                    finish();
                }
            } else if (msg.what == 3) {//first
                Intent intent = new Intent(SplashActivity.this, GuideIndexActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    // 判断数据发送超时
    private class Thread_TimerToActivity implements Runnable {

        private boolean isRun = true;

        @Override
        public void run() {
            // TODO Auto-generated method stub

            while (isRun) {

                try {
                    Thread.sleep(500);

                    isRun = false;
                    //handler.sendEmptyMessage(2);

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}
