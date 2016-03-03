package zgan.ohos.Activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import zgan.ohos.Dals.MessageDal;
import zgan.ohos.Models.Message;
import zgan.ohos.R;
import zgan.ohos.services.login.ZganLoginService;
import zgan.ohos.utils.Frame;
import zgan.ohos.utils.PreferenceUtil;
import zgan.ohos.utils.generalhelper;

public class MessageActivity extends myBaseActivity {
    Toolbar toolbar;
    TextView txt_title;
    RecyclerView rvmsg;
    SwipeRefreshLayout refreshview;
    List<Message> msglst;
    MessageDal messageDal;
    int pageindex = 0;
    int pagesize = 20;
    int msgtype = 0;
    myAdapter adapter;
    boolean[] isopen;
    LinearLayoutManager mLayoutManager;
    boolean isLoadingMore = false;
    final static String SHEQUGONGGAO = "社区公告";
    final static String YANGGUANGYUBEI = "阳光渝北";
    Dialog detailDialog;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        detailDialog = new Dialog(this);
//        Window dialogWindow = detailDialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP | Gravity.RIGHT | Gravity.BOTTOM);
//        lp.x = 0; // 新位置X坐标
//        lp.y = 0; // 新位置Y坐标
//        Point p = AppUtils.getWindowSize(this);
//        lp.width = p.x; // 宽度
//        lp.height = p.y; // 高度
//        lp.alpha = 1f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        // dialogWindow.setAttributes(lp);

        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }

    @Override
    public void ViewClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void initView() {
        msgtype = getIntent().getIntExtra("msgtype", 0);
        messageDal = new MessageDal();
        mLayoutManager = new LinearLayoutManager(this);
        setContentView(R.layout.lo_activity_message);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_title = (TextView) findViewById(R.id.txt_title);
        switch (msgtype) {
            case 1:
                txt_title.setText(SHEQUGONGGAO);
                break;
            case 3:
                txt_title.setText(YANGGUANGYUBEI);
                break;
        }
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvmsg = (RecyclerView) findViewById(R.id.rv_msg);
        refreshview = (SwipeRefreshLayout) findViewById(R.id.refreshview);
        refreshview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageindex = 0;
                loadData();
                adapter.notifyDataSetChanged();
                refreshview.setRefreshing(false);
            }
        });
        rvmsg.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem == totalItemCount - 1 && !isLoadingMore && dy > 0) {

                    loadMoreData();//这里多线程也要手动控制isLoadingMore
                    isLoadingMore = false;
                }
            }
        });
    }

    public void loadMoreData() {
//        try {
//            pageindex++;
//            msglst.addAll(messageDal.GetMessages(pagesize, pageindex, msgtype));
//            int count = msglst.size();
//            int oldcount = isopen.length;
//            boolean[] newisopen = new boolean[count];
//            //isopen=new boolean[count];
//            for (int i = 0; i < oldcount; i++) {
//                newisopen[i] = isopen[i];
//            }
//            for (int i = oldcount; i < count; i++) {
//                newisopen[i] = false;
//            }
//            isopen = newisopen;
//            adapter.notifyDataSetChanged();
//        } catch (Exception ex) {
//            generalhelper.ToastShow(this, ex.getMessage());
//        }
    }

    public void loadData() {
        toSetProgressText(getResources().getString(R.string.loading));
        toShowProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    //小区ID\t帐号\t消息类型ID\t开始时间\t结束时间
                    ZganLoginService.toGetServerData(26, 254, String.format("%s\t%s\t%s\t%s\t%s", PreferenceUtil.getCommunityId(), PreferenceUtil.getUserName(), 0, "2015-01-01", "2016-03-01"), handler);
                    //msglst = messageDal.GetMessages(pagesize, pageindex, msgtype);
                    int count = msglst.size();
                    isopen = new boolean[count];
                    for (int i = 0; i < count; i++) {
                        isopen[i] = false;
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            bindData();
                            toCloseProgress();
                        }
                    });
                } catch (Exception ex) {
                    android.os.Message msg = new android.os.Message();
                    msg.what = 0;
                    msg.obj = ex.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    generalhelper.ToastShow(MessageActivity.this, msg.obj);
                    toCloseProgress();
                    break;
                case 1:
                    Frame f = (Frame) msg.obj;
                    String result = f.strData;
                    String[] results = f.strData.split("\t");
                    if (f.subCmd == 26) {
                        if (results.length == 2 && results[0].equals("0")) {
                            try {
                                msglst = messageDal.GetMessages(results[1]);
                                int count = msglst.size();
                                isopen = new boolean[count];
                                for (int i = 0; i < count; i++) {
                                    isopen[i] = false;
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        bindData();
                                        toCloseProgress();
                                    }
                                });
                            } catch (Exception ex) {
                                android.os.Message msg1 = new android.os.Message();
                                msg1.what = 0;
                                msg1.obj = ex.getMessage();
                                handler.sendMessage(msg1);
                            }
                        }
                    }
                    break;
            }
        }
    };

    void bindData() {
        date = new Date();
        adapter = new myAdapter();
        rvmsg.setAdapter(adapter);
        rvmsg.setLayoutManager(mLayoutManager);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.enter);

        //得到一个LayoutAnimationController对象；

        LayoutAnimationController lac = new LayoutAnimationController(animation);

        //设置控件显示的顺序；

        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);

        //设置控件显示间隔时间；

        lac.setDelay(1);

        //为ListView设置LayoutAnimationController属性；

        rvmsg.setLayoutAnimation(lac);
    }

    class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
        Message msg;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.lo_message_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            msg = msglst.get(position);
            holder.txt_msg_type.setText(msg.getMsgType().getMsgTypeName());
            holder.txt_pub_time.setText(generalhelper.getStringFromDate(
                    generalhelper.getDateFromString(
                            msg.getMsgPublishTime(), date), "yyyy-MM-dd HH:mm"));
            holder.txt_title.setText(msg.getMsgTitle());
            holder.txt_content.setText(msg.getMsgContent());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MessageActivity.this, MessageDetailActivity.class);
                    intent.putExtra("content", holder.txt_content.getText());
                    intent.putExtra("type", holder.txt_msg_type.getText());
                    intent.putExtra("tile", holder.txt_title.getText());
                    intent.putExtra("time", holder.txt_pub_time.getText());
                    if (Build.VERSION.SDK_INT > 20)
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MessageActivity.this, holder.txt_content, "content").toBundle());
                    else
                        startActivity(intent);
//                    try {
//                        if (isopen[position] == false) {
//                            holder.minHeight_content = holder.txt_content.getHeight();
//                            holder.minHeight_title = holder.txt_title.getHeight();
//                            holder.txt_content.setHeight(holder.minHeight_content);
//                            holder.txt_title.setHeight(holder.minHeight_title);
//
//                            holder.txt_content.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    int maxHeight_content = getMaxWrapHeight(holder.txt_content);
//                                    int maxHeight_title = getMaxWrapHeight(holder.txt_title);
//                                    AnimatorSet as = new AnimatorSet();
//                                    ObjectAnimator oa1 = ObjectAnimator.ofInt(holder.txt_content, "Height", maxHeight_content);
//                                    ObjectAnimator oa2 = ObjectAnimator.ofInt(holder.txt_title, "Height", maxHeight_title);
//                                    as.setInterpolator(new DecelerateInterpolator());
//                                    as.play(oa1).with(oa2);
//                                    as.setDuration(500).start();
//                                }
//                            });
//                            detailDialog.setTitle(holder.txt_title.getText());
//                            TextView tv=new TextView(MessageActivity.this);
//                            //tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//                            Point p=AppUtils.getWindowSize(MessageActivity.this);
//                            tv.setLayoutParams(new ViewGroup.LayoutParams(p.x,p.y));
//                            tv.setText(holder.txt_content.getText());
//                            tv.setTextSize(20);
//                            detailDialog.setContentView(tv);
//                            detailDialog.show();
//                            isopen[position] = true;
//                        } else {
//                            holder.txt_content.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    AnimatorSet as = new AnimatorSet();
//                                    ObjectAnimator oa1 = ObjectAnimator.ofInt(holder.txt_content, "Height", holder.minHeight_content);
//                                    ObjectAnimator oa2 = ObjectAnimator.ofInt(holder.txt_title, "Height", holder.minHeight_title);
//                                    as.setInterpolator(new DecelerateInterpolator());
//                                    as.play(oa1).with(oa2);
//                                    as.setDuration(500).start();
//                                }
//                            });
//                            detailDialog.dismiss();
//                            isopen[position] = false;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }


        private int getMaxWrapHeight(TextView pTextView) {
            Layout layout = pTextView.getLayout();
            int desired = layout.getLineTop(pTextView.getLineCount());
            int padding = pTextView.getCompoundPaddingTop() + pTextView.getCompoundPaddingBottom();
            return desired + padding;
        }

        @Override
        public int getItemCount() {
            return msglst.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_msg_type, txt_pub_time, txt_title, txt_content;
            //boolean isopen = false;
            int minHeight_content, minHeight_title;

            public ViewHolder(View itemView) {
                super(itemView);
                txt_msg_type = (TextView) itemView.findViewById(R.id.txt_msg_type);
                txt_pub_time = (TextView) itemView.findViewById(R.id.txt_pub_time);
                txt_title = (TextView) itemView.findViewById(R.id.txt_title);
                txt_content = (TextView) itemView.findViewById(R.id.txt_content);
            }
        }
    }
}
