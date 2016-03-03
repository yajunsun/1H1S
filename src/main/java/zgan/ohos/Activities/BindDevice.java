package zgan.ohos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
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
    Button btn_cancel;
    boolean showcancel=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        Intent intent=getIntent();
        showcancel=intent.getBooleanExtra("showcancel",false);
        setContentView(R.layout.activity_bind_device);
        til_input = (TextInputLayout) findViewById(R.id.til_input);
        et_input = (EditText) findViewById(R.id.et_input);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_cancel.setVisibility(showcancel?View.VISIBLE:View.GONE);
        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    //ZganLoginService.toGetServerData(23, 254, String.format("%s\t%s\t%s", PreferenceUtil.getCommunityId(), PreferenceUtil.getUserName(), SID), handler);
                    ZganLoginService.toGetServerData(23, 254, String.format("%s\t%s", PreferenceUtil.getUserName(), SID), handler);
                }
                break;
            case R.id.btn_cancel:
                Intent intent = new Intent(BindDevice.this, MainActivity.class);
                startActivityWithAnim(intent);
                finish();
                break;
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Frame f = (Frame) msg.obj;
                String result = generalhelper.getSocketeStringResult(f.strData);
                System.out.print(f.strData);
                if (f.subCmd == 23) {
                    if (result.equals("0")) {
                        generalhelper.ToastShow(BindDevice.this, "绑定室内机成功");
                        ZganLoginService.toGetServerData(24, 0, String.format("%s", PreferenceUtil.getUserName()), handler);
                    } else {
                        generalhelper.ToastShow(BindDevice.this, "绑定室内机失败");
                    }
                } else if (f.subCmd == 24) {
                    String[] results = result.split(",");
                    if (results.length == 2 && results[0].equals("0")) {
                        PreferenceUtil.setCommunityId(results[1]);
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (showcancel)
                                {
                                    Intent intent=new Intent(BindDevice.this,MainActivity.class);
                                    startActivityWithAnim(intent);
                                }
                                finish();
                            }
                        }, 500);
                    }
                }
            }
        }
    };
}
