package zgan.ohos.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import zgan.ohos.R;

public class MessageDetailActivity extends AppCompatActivity {

    TextView txt_msg_type, txt_pub_time, txt_title, txt_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        String type=intent.getStringExtra("type");
        String title=intent.getStringExtra("tile");
        String time=intent.getStringExtra("time");
        setContentView(R.layout.lo_message_detail);


        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_msg_type = (TextView) findViewById(R.id.txt_msg_type);
        txt_pub_time = (TextView) findViewById(R.id.txt_pub_time);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_content = (TextView) findViewById(R.id.txt_content);

        txt_title.setMaxLines(20);
        txt_content.setMaxLines(1000);

        txt_msg_type.setText(type);
        txt_pub_time.setText(time);
        txt_title.setText(title);
        txt_content.setText(content);
//        setContentView(R.layout.lo_message_detail);
//        TextView tv = (TextView) findViewById(R.id.txt_content);
//        tv.setText(content);

    }
}
