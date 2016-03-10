package zgan.ohos.Fgmt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import zgan.ohos.Activities.BindDevice;
import zgan.ohos.Activities.CommunityCommercial;
import zgan.ohos.Activities.DailyBreakfirst;
import zgan.ohos.Activities.EventList;
import zgan.ohos.Activities.HouseHoldingChat;
import zgan.ohos.Activities.HouseHolderService;
import zgan.ohos.Activities.LeaveMessages;
import zgan.ohos.Activities.Login;
import zgan.ohos.Activities.MessageActivity;
import zgan.ohos.Activities.ProLaundry;
import zgan.ohos.Activities.ServeTrace;
import zgan.ohos.Contracts.IImageloader;
import zgan.ohos.Models.LeaveMessage;
import zgan.ohos.R;
import zgan.ohos.services.login.ZganLoginService;
import zgan.ohos.utils.AppUtils;
import zgan.ohos.utils.Frame;
import zgan.ohos.utils.ImageLoader;
import zgan.ohos.utils.PreferenceUtil;
import zgan.ohos.utils.SystemUtils;
import zgan.ohos.utils.generalhelper;
import zgan.ohos.utils.resultCodes;

/**
 * Created by Administrator on 16-2-24.
 */
public class fg_myfront extends myBaseFragment implements View.OnClickListener {
    View l_shequgonggao, l_yangguangyubei, l_lianxiwuye, l_remoteopen, l_call_mall, l_laundry, l_finance, l_breakfirst, l_housekepping, l_openmsg, ll_eventaction, l_servetrace;
    IconicsImageView iv_location, iv_message;
    AlertDialog opendialog, houseHoldingDialog, telDialog;
    ViewPager advPager;
    LinearLayout viewGoup;
    boolean isContinue = true;
    Toolbar toolbar;
    List<ImageView> imageViews = new ArrayList<>();
    private AtomicInteger what = new AtomicInteger(0);
    private ImageLoader imageLoader;
    static final int ADSINDEX = 0;
    Calendar lastOpent;
    Calendar thisCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_front, container, false);
        initView(view);
        initDialog();
        Log.v(TAG, "fg_myfront view created");
        return view;
    }

    public void ViewClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.l_shequgonggao:
                intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("msgtype", 1);
                //startActivityWithAnim(getActivity(), intent);
                startActivityIfLogin(intent, resultCodes.SOCIALPOST);
                break;
            case R.id.l_yangguangyubei:
                intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("msgtype", 3);
                //startActivityWithAnim(getActivity(), intent);
                startActivityIfLogin(intent, resultCodes.COUNCILPOST);
                break;
            case R.id.l_lianxiwuye:
                if (SystemUtils.getIsLogin())
                    houseHoldingDialog.show();
                else
                    startActivityIfLogin(null, resultCodes.HOUSEHOLDING);
                break;
            case R.id.l_remoteopen:
                if (SystemUtils.getIsLogin())
                    //第一次开门
                    if (lastOpent == null) {
                        remoteOpen();
                        lastOpent = Calendar.getInstance();
                    } else {
                        thisCalendar = Calendar.getInstance();
                        long span = thisCalendar.getTimeInMillis() - lastOpent.getTimeInMillis();
                        //判断上次点击开门和本次点击开门时间间隔是否大于5秒钟
                        if (span > 5000) {
                            remoteOpen();
                            lastOpent = Calendar.getInstance();
                        } else {
                            generalhelper.ToastShow(getActivity(), "请在" + ((5000 - span) / 1000 + 1) + "秒后操作");
                        }
                    }
                else {
                    startActivityIfLogin(null, resultCodes.REMOTEOPEN);
                }
                break;
            case R.id.l_call_mall:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("确定拨打”02367176359“吗?");
//                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02367176359"));
                try {
                    startActivityIfLogin(intent, resultCodes.DIRECTCALL);
                } catch (Exception e) {
                    generalhelper.ToastShow(getActivity(), "呼叫失败" + e.getMessage());
                    //telDialog.dismiss();
                }
                //}
//                });
//                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        telDialog.dismiss();
//                    }
//                });
//                telDialog = builder.create();
//                telDialog.show();
                break;
            case R.id.l_servetrace:
                intent = new Intent(getActivity(), ServeTrace.class);
                startActivityIfLogin(intent, resultCodes.SERVETRACE);
                break;
            case R.id.iv_message:
                generalhelper.ToastShow(getActivity(), "还没有消息哦~");
                break;
            case R.id.ll_eventaction:
                intent = new Intent(getActivity(), EventList.class);
                startActivityIfLogin(intent, resultCodes.RESERVE);
                break;
            case R.id.l_laundry:
                intent = new Intent(getActivity(), ProLaundry.class);
                startActivityIfLogin(intent, resultCodes.LAUNDRY);
                break;
            case R.id.l_finance:
                intent = new Intent(getActivity(), CommunityCommercial.class);
                startActivityIfLogin(intent, resultCodes.FINANCE);
                break;
            case R.id.l_breakfirst:
                intent = new Intent(getActivity(), DailyBreakfirst.class);
                startActivityIfLogin(intent, resultCodes.BREAKFIRST);
                break;
            case R.id.l_housekepping:
                intent = new Intent(getActivity(), HouseHolderService.class);
                startActivityIfLogin(intent, resultCodes.HOUSEHOLDER);
                break;
        }
    }

    private void initDialog() {
        /*********联系物业方式选择（电话、留言）*********/
        AlertDialog.Builder hhbuilder = new AlertDialog.Builder(getActivity());
        View hhview = getActivity().getLayoutInflater().inflate(R.layout.lo_householding_contact_choice, null, false);
        hhbuilder.setView(hhview);
        houseHoldingDialog = hhbuilder.create();
        if (Build.VERSION.SDK_INT > 20) {
            houseHoldingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_transparent_100, getActivity().getTheme()));
            houseHoldingDialog.getWindow().setEnterTransition(new Explode());
            houseHoldingDialog.getWindow().setExitTransition(new Explode());
        }
        View callHouseHolding = hhview.findViewById(R.id.ll_direct_call);
        View msg_chat = hhview.findViewById(R.id.ll_msg_chat);
        callHouseHolding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02367288312"));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    generalhelper.ToastShow(getActivity(), "呼叫失败" + e.getMessage());
                }
                houseHoldingDialog.dismiss();
            }
        });
        msg_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceUtil.getCommunityId().equals("0") || PreferenceUtil.getCommunityId().equals("")) {
                    opendialog.show();
                }
                else {
                    Intent intent = new Intent(getActivity(), LeaveMessages.class);
                    startActivityWithAnim(getActivity(), intent);
                }
                houseHoldingDialog.dismiss();
            }
        });
        /*****打开单元门时发现未绑定室内机则提示*****/
        final AlertDialog.Builder openbuilder = new AlertDialog.Builder(getActivity());
        openbuilder.setTitle("您的手机号码还没有与室内机绑定，是否现在绑定？");
        openbuilder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), BindDevice.class);
                startActivityWithAnim(getActivity(), intent);
                opendialog.dismiss();
            }
        });
        openbuilder.setCancelable(true);
        openbuilder.setNegativeButton("下次绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                opendialog.dismiss();
            }
        });
        opendialog = openbuilder.create();
    }

    private void initView(View v) {
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        iv_message = (IconicsImageView) v.findViewById(R.id.iv_message);

        iv_message.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_commenting_o).sizeDp(25).colorRes(R.color.txt_xiaoqu_color));
        advPager = (ViewPager) v.findViewById(R.id.adv_pager);
        viewGoup = (LinearLayout) v.findViewById(R.id.viewGroup);
        l_shequgonggao = v.findViewById(R.id.l_shequgonggao);
        l_yangguangyubei = v.findViewById(R.id.l_yangguangyubei);
        l_lianxiwuye = v.findViewById(R.id.l_lianxiwuye);
        l_remoteopen = v.findViewById(R.id.l_remoteopen);
        l_call_mall = v.findViewById(R.id.l_call_mall);
        l_servetrace = v.findViewById(R.id.l_servetrace);
        l_laundry = v.findViewById(R.id.l_laundry);
        l_finance = v.findViewById(R.id.l_finance);
        l_breakfirst = v.findViewById(R.id.l_breakfirst);
        l_housekepping = v.findViewById(R.id.l_housekepping);
        ll_eventaction = v.findViewById(R.id.ll_eventaction);
        //l_openmsg=v.findViewById(R.id.l_openmsg);
        l_shequgonggao.setOnClickListener(this);
        l_yangguangyubei.setOnClickListener(this);
        l_lianxiwuye.setOnClickListener(this);
        l_remoteopen.setOnClickListener(this);
        l_call_mall.setOnClickListener(this);
        l_servetrace.setOnClickListener(this);
        l_laundry.setOnClickListener(this);
        l_finance.setOnClickListener(this);
        l_breakfirst.setOnClickListener(this);
        l_housekepping.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        ll_eventaction.setOnClickListener(this);
        loadSampleImage();
    }

    private void remoteOpen() {
        if (PreferenceUtil.getCommunityId().equals("0") || PreferenceUtil.getCommunityId().equals("")) {
            opendialog.show();
        } else {
            ZganLoginService.toGetServerData(
                    20, 254,
                    String.format("%s\t%s", PreferenceUtil.getCommunityId(), PreferenceUtil.getUserName()), handler);//A0000003
        }
    }

    @Override
    public void onClick(View view) {
        ViewClick(view);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Frame frame = (Frame) msg.obj;
                String ret = generalhelper.getSocketeStringResult(frame.strData);
                Log.v(TAG, frame.subCmd + "  " + ret);

                if (frame.subCmd == 20 && ret.equals("0")) {
                    Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "门开了", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "门没开", Snackbar.LENGTH_LONG).show();
                }

            } else if (msg.what == ADSINDEX) {

                advPager.setCurrentItem(msg.arg1);
            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent;
        if (resultCode == resultCodes.LOGIN)
            if (SystemUtils.getIsLogin())
                switch (requestCode) {
                    case resultCodes.SOCIALPOST:
                        intent = new Intent(getActivity(), MessageActivity.class);
                        intent.putExtra("msgtype", 1);
                        //startActivityIfLogin(intent, resultCodes.SOCIALPOST);
                        startActivityWithAnim(getActivity(), intent);
                        break;
                    case resultCodes.COUNCILPOST:
                        intent = new Intent(getActivity(), MessageActivity.class);
                        intent.putExtra("msgtype", 3);
                        //startActivityIfLogin(intent, resultCodes.SOCIALPOST);
                        startActivityWithAnim(getActivity(), intent);
                        break;
                    case resultCodes.HOUSEHOLDING:
                        if (SystemUtils.getIsLogin())
                            houseHoldingDialog.show();
                        else
                            //startActivityIfLogin(null, resultCodes.HOUSEHOLDING);
                            generalhelper.ToastShow(getActivity(), "未登录");
                        break;
                    case resultCodes.REMOTEOPEN:
                        if (SystemUtils.getIsLogin())
                            remoteOpen();
                        else {
                            generalhelper.ToastShow(getActivity(), "未登录");
                            //startActivityIfLogin(null, resultCodes.REMOTEOPEN);
                        }
                        break;
                    case resultCodes.DIRECTCALL:
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02367176359"));
                        try {
                            //startActivityIfLogin(intent, resultCodes.DIRECTCALL);
                            startActivityWithAnim(getActivity(), intent);
                        } catch (Exception e) {
                            generalhelper.ToastShow(getActivity(), "呼叫失败" + e.getMessage());
                        }
                        break;
                    case resultCodes.SERVETRACE:
                        intent = new Intent(getActivity(), ServeTrace.class);
                        startActivityIfLogin(intent, resultCodes.SERVETRACE);
                        break;
                    case resultCodes.RESERVE:
                        intent = new Intent(getActivity(), EventList.class);
                        startActivityIfLogin(intent, resultCodes.RESERVE);
                        break;
                    case resultCodes.LAUNDRY:
                        intent = new Intent(getActivity(), ProLaundry.class);
                        startActivityIfLogin(intent, resultCodes.LAUNDRY);
                        break;
                    case resultCodes.FINANCE:
                        intent = new Intent(getActivity(), CommunityCommercial.class);
                        startActivityIfLogin(intent, resultCodes.FINANCE);
                        break;
                    case resultCodes.BREAKFIRST:
                        intent = new Intent(getActivity(), DailyBreakfirst.class);
                        startActivityIfLogin(intent, resultCodes.BREAKFIRST);
                        break;
                    case resultCodes.HOUSEHOLDER:
                        intent = new Intent(getActivity(), HouseHolderService.class);
                        startActivityIfLogin(intent, resultCodes.HOUSEHOLDER);
                        break;
                    default:
                        generalhelper.ToastShow(getActivity(), "暂未开通");
                        break;
                }
    }

    public void startActivityIfLogin(Intent intent, int requstCode) {
        if (SystemUtils.getIsLogin()) {
            Log.v(TAG, "已登录");
            if (PreferenceUtil.getCommunityId().equals("0") || PreferenceUtil.getCommunityId().equals("")) {
                opendialog.show();
            } else {
                startActivityWithAnim(getActivity(), intent);
            }
        } else {
            Log.v(TAG, "未登录");
            Intent loginIntent = new Intent(getActivity(), Login.class);
            loginIntent.putExtra(SystemUtils.FORRESULT, true);
            startActivityWithAnimForResult(getActivity(), loginIntent, requstCode);
        }
    }

    void loadSampleImage() {//List<Product_Pics> pics
        int[] pics = new int[]{R.drawable.ad1, R.drawable.ad2, R.drawable.ad3, R.drawable.ad4};
        List<View> advPics = new ArrayList<>();
        int i = 0;
        Point p = AppUtils.getWindowSize(getActivity());
        int window_width = p.x;
        for (int pic : pics) {
            ImageView img = new ImageView(getActivity());
            if (imageLoader == null)
                imageLoader = new ImageLoader();
            imageLoader.loadDrawableRS(getActivity(), pic, img, new IImageloader() {
                @Override
                public void onDownloadSucc(Bitmap bitmap, String c_url, View imageView) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                }
            }, window_width, (int) (Integer.valueOf(getString(R.string.adv_pager_height_num)) * AppUtils.getDensity(getActivity())));

            advPics.add(img);
            ImageView simg = new ImageView(getActivity());
            simg.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            simg.setPadding(5, 5, 5, 5);
            if (i == 0)
                simg.setImageDrawable(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_brightness_1).color(Color.RED).sizeDp(30));
            else
                simg.setImageDrawable(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_brightness_1).color(Color.WHITE).sizeDp(30));
            imageViews.add(simg);
            viewGoup.addView(simg);
            i++;
        }
        advPager.setAdapter(new AdvAdapter(advPics));
        advPager.setOnPageChangeListener(new GuidePageChangeListener());
        advPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (isContinue) {
                        Message msg = new Message();
                        msg.what = ADSINDEX;
                        msg.arg1 = what.get();
                        handler.sendMessage(msg);
                        whatOption();
                    }
                }
            }

        }).start();
    }

    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.size() - 1) {
            what.getAndAdd(0 - imageViews.size());
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }

    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageViews.size(); i++) {
                imageViews.get(arg0).setImageDrawable(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_brightness_1).color(Color.RED).sizeDp(20));
                if (arg0 != i) {
                    imageViews.get(i)
                            .setImageDrawable(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_brightness_1).color(Color.LTGRAY).sizeDp(20));
                }
            }
            what.getAndSet(arg0);

        }

    }

    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }
    }
}
