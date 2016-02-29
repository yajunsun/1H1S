package zgan.ohos.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zgan.ohos.Dals.HouseHoldingMsgDal;
import zgan.ohos.Models.HouseHoldingMsg;
import zgan.ohos.R;
import zgan.ohos.utils.PreferenceUtil;
import zgan.ohos.utils.generalhelper;

public class HouseHoldingChat extends myBaseActivity {

    TextView txtbaseinfo;
    ListView messagelst;
    LinearLayout loadingbar;
    EditText txtinputText;
    Button btnsend;
    final int newitem = 2;
    Toolbar toolbar;
    List<HouseHoldingMsg> messagelist;
    MessageListAdapter adapter;
    HouseHoldingMsgDal houseHoldingMsgDal;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    bindData();
                    break;
                case newitem:
                    //adapter.addNewsItem(message);
                    loadData();
                    txtinputText.setText("");
                    txtinputText.clearFocus();
                    //setResult(resultCodes.chat);
                    break;
                case 400:
                    generalhelper.ToastShow(HouseHoldingChat.this, msg.obj);
                default:
                    break;
            }
            toCloseProgress();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_house_holding_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        houseHoldingMsgDal = new HouseHoldingMsgDal();
        txtbaseinfo = (TextView) findViewById(R.id.txtbaseinfo);
        messagelst = (ListView) findViewById(R.id.lstmessage);
        txtinputText = (EditText) findViewById(R.id.txtinput);
        View back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadData() {
        toSetProgressText(getResources().getString(R.string.loading));
        toShowProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Looper.prepare();
                    messagelist = houseHoldingMsgDal.GetHouseHoldingMsgs(PreferenceUtil.getUserName(), 20, 0);
                    if (messagelist==null)
                        messagelist=new ArrayList();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Message msg = new Message();
                    msg.what = 400;
                    msg.obj = "网络异常:" + e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    @Override
    public void ViewClick(View v) {
        if (v.getId()==R.id.btnsend) {
            // TODO Auto-generated method stub
            if (txtinputText.getText().toString() == "")
                return;
            final String lastdate = generalhelper.getStringFromDate(new Date());

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        Looper.prepare();
                        HouseHoldingMsg message = new HouseHoldingMsg();
                        message.setFtime(lastdate);
                        message.setFcontent(txtinputText.getText());
                        message.setFfrom(PreferenceUtil.getUserName());
                        message.setFto(getResources().getString(R.string.str_househ_chat_id));

                        houseHoldingMsgDal.SendHouseHoldingMsg(message);
                        handler.sendEmptyMessage(newitem);
                    } catch (Exception e) {
                        // TODO: handle exception
                        Message msg = new Message();
                        msg.what = 400;
                        msg.obj = "网络异常:" + e.getMessage();
                        handler.sendMessage(msg);
                    }

                }
            }).start();
        }
        else if (v.getId()==R.id.back)
        {
            finish();
        }
    }

    private void bindData() {
        if (adapter == null) {
            adapter = new MessageListAdapter();
            messagelst.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        messagelst.setSelection(messagelst.getCount() - 1);
    }

    class MessageListAdapter extends BaseAdapter // RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    {
        private static final int COME_MSG = 0;
        private static final int TO_MSG = 1;

        @Override
        public int getCount() {
            return messagelist.size();
        }

        @Override
        public Object getItem(int position) {
            return messagelist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HouseHoldingMsg message = messagelist.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                if (message.getFfrom().equals(getResources().getString(R.string.str_househ_chat_id))) {
                    convertView = getLayoutInflater().inflate(R.layout.chatleftview, parent, false);
                } else {
                    convertView = getLayoutInflater().inflate(R.layout.chatrightview, parent, false);
                }
                viewHolder = new ViewHolder();
                viewHolder.nameText = (TextView) convertView.findViewById(R.id.txtname);
                viewHolder.contentText = (TextView) convertView.findViewById(R.id.txtcontent);
                viewHolder.logtimeText = (TextView) convertView.findViewById(R.id.txtlogtime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.contentText.setText(message.getFcontent());
            viewHolder.logtimeText.setText(message.getFtime());
            if (message.getFfrom().equals(getResources().getString(R.string.str_househ_chat_id))) {
                viewHolder.nameText.setText("物管");
            } else
                viewHolder.nameText.setText("我");
            return convertView;
        }

        class ViewHolder {
            TextView nameText;
            TextView contentText;
            TextView logtimeText;


        }
    }
}
