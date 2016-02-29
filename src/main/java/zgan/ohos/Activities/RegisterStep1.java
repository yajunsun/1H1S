package zgan.ohos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import zgan.ohos.Dals.UserCommDal;
import zgan.ohos.R;
import zgan.ohos.utils.generalhelper;
import zgan.ohos.utils.resultCodes;

public class RegisterStep1 extends myBaseActivity {

    Button btn_register_step1;
    View rl_commselect;
    TextInputLayout til_yzname, til_yzphone;
    EditText input_yzname, input_yzphone;
    TextView txt_commname;
    int CommId = 0;
    String CommName;
    Toolbar toolbar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == resultCodes.COMMSELECTED) {
            if (data != null && data.hasExtra("commid")) {
                CommId = data.getIntExtra("commid", 0);
                CommName = data.getStringExtra("commname");
                txt_commname.setText(CommName);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.lo_register_step1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_register_step1 = (Button) findViewById(R.id.btn_register_step1);
        rl_commselect = findViewById(R.id.rl_commselect);
        til_yzname = (TextInputLayout) findViewById(R.id.til_yzname);
        til_yzphone = (TextInputLayout) findViewById(R.id.til_yzphone);
        input_yzname = (EditText) findViewById(R.id.input_yzname);
        input_yzphone = (EditText) findViewById(R.id.input_yzphone);
        txt_commname = (TextView) findViewById(R.id.txt_commname);
        View back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void ViewClick(View v) {
        Intent intent;
        boolean CheckName = false;
        boolean CheckPhone = false;
        switch (v.getId()) {
            case R.id.rl_commselect:
                intent = new Intent(RegisterStep1.this, UserCommSelect.class);
                startActivityWithAnimForResult(intent, resultCodes.COMMSELECTED);
                break;
            case R.id.btn_register_step1:
                String hostName = input_yzname.getText().toString().trim();
                String hostPhone = input_yzphone.getText().toString().trim();
                if (hostName.length() == 0) {
                    til_yzname.setError("业主姓名不能为空");
                    til_yzname.setErrorEnabled(true);
                } else if (hostName.length() > 20) {
                    til_yzname.setError("业主姓名太长");
                    til_yzname.setErrorEnabled(true);
                } else {
                    if (hostPhone.length() == 0) {
                        til_yzphone.setError("业主手机号码不能为空");
                        til_yzphone.setErrorEnabled(true);
                    } else if (hostPhone.toCharArray().length > 11) {
                        til_yzphone.setError("电话号码有误");
                        til_yzphone.setErrorEnabled(true);
                    } else {
                        til_yzphone.setErrorEnabled(false);
                        til_yzname.setErrorEnabled(false);
                        CheckPhone = true;
                        CheckName = true;
                    }
                }
                if (CheckName && CheckPhone) {
                    try {
                        String hostNameAndPhone = new UserCommDal().GetHostNameAndPhone(CommId);
                        String[] hostNameAndPhoneArr = hostNameAndPhone.split(",");
                        if (hostNameAndPhoneArr.length != 2) {
                            generalhelper.ToastShow(RegisterStep1.this, "无法注册，该房业主未登记");
                            return;
                        }
                        if (!hostName.equals(hostNameAndPhoneArr[0].trim())) {
                            til_yzname.setError("业主名字填写错误");
                            til_yzname.setErrorEnabled(true);
                            CheckName = false;
                        } else {
                            if (!hostPhone.equals(hostNameAndPhoneArr[1].trim())) {
                                til_yzphone.setError("业主电话填写错误");
                                til_yzphone.setErrorEnabled(true);
                                CheckPhone = false;
                            } else {
                                til_yzphone.setErrorEnabled(false);
                                CheckPhone = true;
                                CheckName = true;
                                til_yzname.setErrorEnabled(false);
                            }
                        }
                        if (CheckName && CheckPhone) {
                            intent = new Intent(RegisterStep1.this, Register.class);
                            intent.putExtra("commid", CommId);
                            intent.putExtra("commname", CommName);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
