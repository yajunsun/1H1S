package zgan.ohos.Activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.File;
import java.util.List;

import zgan.ohos.Dals.ReplyMessageDal;
import zgan.ohos.Models.ReplyMessage;
import zgan.ohos.R;
import zgan.ohos.services.login.ZganLoginService;
import zgan.ohos.utils.Frame;
import zgan.ohos.utils.PreferenceUtil;
import zgan.ohos.utils.generalhelper;
import zgan.ohos.utils.picturefile;

public class ReplyMessages extends myBaseActivity {
    int sessionId;
    String lmsgContent;
    String msgDate;
    int pageindex = 0;
    RecyclerView messagelst;
    final int newitem = 2;
    EditText txtinputText;
    TextView txt_leavemsg, txt_pub_time;
    SwipeRefreshLayout refreshview;
    File pictureFile;
    String LOCALHEADERFILENAME;
    LinearLayoutManager mLayoutManager;
    boolean isLoadingMore = false;
    Toolbar toolbar;
    List<ReplyMessage> messagelist;
    myAdapter adapter;
    ReplyMessageDal replyMsgDal;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    Frame f = (Frame) msg.obj;
                    String result = f.strData;
                    String[] results = result.split("\t");
                    if (f.subCmd == 32) {
                        if (results.length == 2 && results[0].equals("0")) {
                            String xmlstr = results[1].substring(results[1].indexOf("<li>"), results[1].length());
                            messagelist = replyMsgDal.getReplyMessages(xmlstr);
                            bindData();
                        }
                    }
                    if (f.subCmd == 30) {
                        if (results[0].equals("0")) {
                            loadData();
                        }
                    }

                    break;
                case newitem:
                    //adapter.addNewsItem(message);
                    loadData();
                    //setResult(resultCodes.chat);
                    break;
                case 400:
                    generalhelper.ToastShow(ReplyMessages.this, msg.obj);
                default:
                    break;
            }
            toCloseProgress();
            refreshview.setRefreshing(false);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        sessionId = intent.getIntExtra("sessionid", 0);
        lmsgContent = intent.getStringExtra("content");
        msgDate = intent.getStringExtra("date");
        txt_leavemsg.setText(lmsgContent);
        txt_pub_time.setText(msgDate);
        toSetProgressText(getResources().getString(R.string.loading));
        toShowProgress();
        loadData();
    }

    @Override
    protected void initView() {

        setContentView(R.layout.activity_reply_messages);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLayoutManager = new LinearLayoutManager(this);
        txt_leavemsg = (TextView) findViewById(R.id.txt_leavemsg);
        txt_pub_time = (TextView) findViewById(R.id.txt_pub_time);
        replyMsgDal = new ReplyMessageDal();
        messagelst = (RecyclerView) findViewById(R.id.lst_replymsg);
        messagelst.setLayoutManager(mLayoutManager);
        txtinputText = (EditText) findViewById(R.id.txtinput);
        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshview = (SwipeRefreshLayout) findViewById(R.id.refreshview);
        refreshview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageindex = 0;
                isLoadingMore=false;
                loadData();
                //adapter.notifyDataSetChanged();
                //refreshview.setRefreshing(false);
            }
        });
        messagelst.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem == totalItemCount - 1 && !isLoadingMore && dy > 0) {

                    loadMoreData();//这里多线程也要手动控制isLoadingMore
                    //isLoadingMore = true;
                }
            }
        });
    }
    public void loadMoreData() {
        try {
//            toSetProgressText(getResources().getString(R.string.loading));
//            toShowProgress();
            refreshview.setRefreshing(true);
            pageindex++;
            isLoadingMore = true;
            ZganLoginService.toGetServerData(32, 254, String.format("%s\t%s\t%d\t%d", PreferenceUtil.getCommunityId(), PreferenceUtil.getUserName(), sessionId, pageindex), handler);
        } catch (Exception ex) {
            generalhelper.ToastShow(this, ex.getMessage());
        }
    }
    private void loadData() {
        pageindex=0;
        //refreshview.setRefreshing(true);
        ZganLoginService.toGetServerData(32, 254, String.format("%s\t%s\t%d\t%d", PreferenceUtil.getCommunityId(), PreferenceUtil.getUserName(), sessionId, pageindex), handler);
    }

    @Override
    public void ViewClick(View v) {
        if (v.getId() == R.id.btnsend) {
            String input = txtinputText.getText().toString().trim();
            if (!input.equals(""))
                ZganLoginService.toGetServerData(30, 254, String.format("%s\t%s\t%d\t%d\t%s", PreferenceUtil.getCommunityId(), PreferenceUtil.getUserName(), sessionId, 1, input), handler);
        }
    }

    private void bindData() {
        txtinputText.setText("");
        if (adapter == null) {
            adapter = new myAdapter();
            messagelst.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    class myAdapter extends  RecyclerView.Adapter<myAdapter.ViewHolder>
    {
        public myAdapter()
        {
            LOCALHEADERFILENAME = PreferenceUtil.getUserName() + "_header";
            pictureFile = picturefile.getdochead(LOCALHEADERFILENAME);
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.chatrightview,parent,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ReplyMessage message = messagelist.get(position);
            holder.contentText.setText(message.getMsgContent());
            holder.logtimeText.setText(message.getMsgDate());
            if (message.getMsgType()==2) {
                holder.ivname.setImageResource(R.drawable.wuguanheader);
            } else {
                if (pictureFile != null && pictureFile.exists())
                    holder.ivname.setImageBitmap(BitmapFactory.decodeFile(pictureFile.getPath()));
                else
                    holder.ivname.setImageDrawable(new IconicsDrawable(ReplyMessages.this, GoogleMaterial.Icon.gmd_account_box).colorRes(R.color.md_white_1000));

            }
        }

        @Override
        public int getItemCount() {
            return messagelist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivname;
            TextView contentText;
            TextView logtimeText;

            public ViewHolder(View itemView) {
                super(itemView);
                ivname = (ImageView) itemView.findViewById(R.id.ivname);
                contentText = (TextView) itemView.findViewById(R.id.txtcontent);
                logtimeText = (TextView) itemView.findViewById(R.id.txtlogtime);
            }
        }
    }
}
