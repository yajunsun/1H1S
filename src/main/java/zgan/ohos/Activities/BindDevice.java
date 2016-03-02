package zgan.ohos.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import zgan.ohos.R;
import zgan.ohos.services.login.ZganLoginService;
import zgan.ohos.utils.Frame;
import zgan.ohos.utils.PreferenceUtil;
import zgan.ohos.utils.generalhelper;

public class BindDevice extends myBaseActivity {

    TextInputLayout til_input;
    EditText et_input;
    String SID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_bind_device);
        til_input = (TextInputLayout) findViewById(R.id.til_input);
        et_input = (EditText) findViewById(R.id.et_input);
    }

    @Override
    public void ViewClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind:
                SID = et_input.getText().toString().trim();
                if (SID.equals("")) {
                    til_input.setError("SID不能为空");
                    til_input.setErrorEnabled(true);
                } else if (SID.length() > 40) {
                    til_input.setError("SID不正确");
                    til_input.setErrorEnabled(true);
                } else {
                    til_input.setErrorEnabled(false);
                    System.out.print(String.format("%s\t%s\t%s", PreferenceUtil.getCommunityId(), PreferenceUtil.getUserName(), SID));
                    ZganLoginService.toGetServerData(23, 254, String.format("%s\t%s\t%s", PreferenceUtil.getCommunityId(), PreferenceUtil.getUserName(), SID), handler);
                }
                break;
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Frame f = (Frame) msg.obj;
                if (f.subCmd == 23) {
                    String result = generalhelper.getSocketeStringResult(f.strData);
                    System.out.print(f.strData);
                    if (result.equals("0")) {
                        generalhelper.ToastShow(BindDevice.this, "绑定室内机成功");
                        PreferenceUtil.setCommunityId(SID);
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    } else {
                        generalhelper.ToastShow(BindDevice.this, "绑定室内机失败");
                    }
                }
            }
        }
    };
}
