package zgan.ohos.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import zgan.ohos.R;

public class CommunityCommercial extends myBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_community_commercial);
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

    }
}
