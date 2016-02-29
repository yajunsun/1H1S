package zgan.ohos.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import zgan.ohos.R;

public class UpdatePassword extends myBaseActivity {

    Toolbar toolbar;
    TextInputLayout til_oldpwd, til_newpwd;
    EditText et_oldpwd, et_newpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void initView() {
        setContentView(R.layout.lo_update_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        til_oldpwd = (TextInputLayout) findViewById(R.id.til_oldpwd);
        til_newpwd = (TextInputLayout) findViewById(R.id.til_newpwd);
        et_oldpwd = (EditText) findViewById(R.id.et_oldpwd);
        et_newpwd = (EditText) findViewById(R.id.et_newpwd);
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
        switch (v.getId()) {
            case R.id.btn_ensure:
                finish();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
