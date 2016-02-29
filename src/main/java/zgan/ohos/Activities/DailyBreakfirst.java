package zgan.ohos.Activities;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import zgan.ohos.R;

public class DailyBreakfirst extends myBaseActivity {

    AnimationDrawable waiteAnim;
    ImageView iv_waite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_daily_breakfirst);
        iv_waite = (ImageView) findViewById(R.id.anim_waite);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        waiteAnim = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (waiteAnim.isRunning())
            waiteAnim.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        waiteAnim = (AnimationDrawable) iv_waite.getDrawable();
        waiteAnim.start();
    }
}
