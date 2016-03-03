package zgan.ohos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import zgan.ohos.MyApplication;
import zgan.ohos.R;
import zgan.ohos.services.login.ZganLoginService;
import zgan.ohos.utils.Frame;
import zgan.ohos.utils.LocationUtil;
import zgan.ohos.utils.PreferenceUtil;
import zgan.ohos.utils.SystemUtils;
import zgan.ohos.utils.generalhelper;
import zgan.ohos.utils.resultCodes;

public class Login extends myBaseActivity {

    TextInputLayout til_Phone;
    TextInputLayout til_pwd;
    EditText et_Phone;
    EditText et_pwd;
    String PhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void initView() {
        setContentView(R.layout.lo_activity_login);
        til_Phone = (TextInputLayout) findViewById(R.id.til_phone);
        til_pwd = (TextInputLayout) findViewById(R.id.til_pwd);
        et_Phone = (EditText) findViewById(R.id.et_Phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        til_Phone.setHint("请输入您的电话号码");
        til_pwd.setHint("请输入您的密码");
    }

    public void ViewClick(View view) {
        boolean CheckName = false;
        boolean CheckPwd = false;
        switch (view.getId()) {
            case R.id.btn_login:
                String Phone = et_Phone.getText().toString().trim();
                String Pwd = et_pwd.getText().toString().trim();
                if (Phone.length() == 0) {
                    til_Phone.setError("电话号码不能为空");
                    til_Phone.setErrorEnabled(true);
                } else if (Phone.toCharArray().length > 11) {
                    til_Phone.setError("电话号码填写错误");
                    til_Phone.setErrorEnabled(true);
                } else {
                    CheckName = true;
                    til_Phone.setErrorEnabled(false);
                }
                if (Pwd.length() == 0) {
                    til_pwd.setError("密码不能为空");
                    til_pwd.setErrorEnabled(true);
                } else if (Pwd.length() > 20) {
                    til_pwd.setError("密码长度不能超过20位");
                    til_pwd.setErrorEnabled(true);
                } else {
                    CheckPwd = true;
                    til_pwd.setErrorEnabled(false);
                }
                try {
                    if (CheckName && CheckPwd) {
                        toSetProgressText("请稍等...");
                        toShowProgress();
                        this.PhoneNum = Phone;
                        //SystemUtils.login(Phone, Pwd, handler);
                        String imei = LocationUtil.getDrivenToken(MyApplication.context, Phone);
                        ZganLoginService.toUserLogin(Phone, Pwd, imei, handler);
                        Log.v(TAG, "log click");
                    }

                } catch (Exception ex) {
                    generalhelper.ToastShow(Login.this, ex.getMessage());
                }
                break;
            case R.id.btn_register:
                //Intent intent = new Intent(Login.this, UserCommSelect.class);
                Intent intent = new Intent(Login.this, Register.class);
                startActivityWithAnim(intent);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Frame frame = (Frame) msg.obj;
                String result = generalhelper.getSocketeStringResult(frame.strData);
                if (frame.subCmd == 1 && result.equals("0")) {
                    logined();
                    ZganLoginService.toGetServerData(24, 0, PreferenceUtil.getUserName(), handler);
                } else if (frame.subCmd == 1 && (result.equals("6") || result.equals("8"))) {
                    generalhelper.ToastShow(Login.this, "用户不存在");
                } else if (frame.subCmd == 24) {
                    String communityId = PreferenceUtil.getCommunityId();
                    String results[] = result.split(",");
                    if (results.length == 2 && results[0].equals("0")) {
                        Log.v(TAG, "小区ID：" + results[1]);
                        if (!communityId.equals(results[1])) {
                            PreferenceUtil.setCommunityId(results[1]);
                        }
                    }
                    finish();
                }
                toCloseProgress();
            }
        }
    };

    private void logined() {
        SystemUtils.setIsLogin(true);
        Intent intent = getIntent();
        boolean forresult = false;
        toCloseProgress();
        Log.v(TAG, "logined");
        PreferenceUtil.setUserName(PhoneNum);
        PreferenceUtil.setPassWord(et_pwd.getText().toString());
        if (intent.hasExtra(SystemUtils.FORRESULT)) {
            forresult = intent.getBooleanExtra(SystemUtils.FORRESULT, false);
            setResult(resultCodes.LOGIN);
        }
        if (!forresult) {
            Intent returnintent = new Intent(Login.this, zgan.ohos.Activities.MainActivity.class);
            returnintent.putExtra("phonenum", PhoneNum);
            //MyApplication.appUser.setPhoneNum(PhoneNum);
            startActivity(returnintent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}
