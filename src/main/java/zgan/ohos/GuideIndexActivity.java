package zgan.ohos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import zgan.ohos.Activities.*;
import zgan.ohos.utils.ImageLoader;
import zgan.ohos.utils.PreferenceUtil;

public class GuideIndexActivity extends Activity {

    ViewPager guidpager;
    LinearLayout viewGoup;
    Button btn_go;
    boolean isContinue = true;
    List<ImageView> imageViews = new ArrayList<>();
    private AtomicInteger what = new AtomicInteger(0);
    private ImageLoader imageLoader;
    static final int ADSINDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_index);
        btn_go=(Button)findViewById(R.id.btn_go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.setUsedTimes(1);
                Intent intent = new Intent(GuideIndexActivity.this, zgan.ohos.Activities.MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.enter, R.animator.exit);
                finish();
            }
        });
        guidpager = (ViewPager) findViewById(R.id.guid_pager);
        guidpager.setVisibility(View.VISIBLE);
        guidpager.setAdapter(new AdvAdapter());
        guidpager.setOnPageChangeListener(new GuidePageChangeListener());
        guidpager.setOnTouchListener(new View.OnTouchListener() {

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
    }

    /****
     * 引导页
     */
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
                imageViews.get(arg0).setImageDrawable(new IconicsDrawable(GuideIndexActivity.this, GoogleMaterial.Icon.gmd_brightness_1).color(Color.RED).sizeDp(20));
                if (arg0 != i) {
                    imageViews.get(i)
                            .setImageDrawable(new IconicsDrawable(GuideIndexActivity.this, GoogleMaterial.Icon.gmd_brightness_1).color(Color.WHITE).sizeDp(20));
                }
            }
            what.getAndSet(arg0);

        }

    }

    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter() {
            views = new ArrayList();
            View view = getLayoutInflater().inflate(R.layout.guideindex1, null, false);
            View iv_go = view.findViewById(R.id.iv_go);
            iv_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceUtil.setUsedTimes(1);
                    Intent intent = new Intent(GuideIndexActivity.this, zgan.ohos.Activities.MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.enter, R.animator.exit);
                    finish();
                }
            });
            views.add(view);
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
