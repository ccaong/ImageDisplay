package com.ccaong.devel.imagedisplay;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.orhanobut.hawk.Hawk;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author devel
 */
public class MainActivity extends BaseActivity {

    private HomeReceiver innerReceiver;
    private List<Integer> imageUrlList;
    private List<String> imageFileList;

    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner = findViewById(R.id.banner);

        initData();


        innerReceiver = new HomeReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(innerReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 设置默认数据
     */
    private void initData() {
        if (imageFileList == null) {
            imageFileList = new ArrayList<>();
        } else {
            imageFileList.clear();
        }
        imageFileList = Hawk.get("FIRST_IMAGE_LIST");

        if (imageFileList == null || imageFileList.size() == 0) {
            if (imageUrlList == null) {
                imageUrlList = new ArrayList<>();
                imageUrlList.add(R.mipmap.ic_a);
                imageUrlList.add(R.mipmap.ic_b);
                imageUrlList.add(R.mipmap.ic_c);
                imageUrlList.add(R.mipmap.ic_b);
                imageUrlList.add(R.mipmap.ic_a);
                imageUrlList.add(R.mipmap.ic_c);
                imageUrlList.add(R.mipmap.ic_c);

            } else {
                if (imageUrlList.size() == 0) {
                    imageUrlList.add(R.mipmap.ic_a);
                    imageUrlList.add(R.mipmap.ic_b);
                    imageUrlList.add(R.mipmap.ic_c);
                    imageUrlList.add(R.mipmap.ic_b);
                    imageUrlList.add(R.mipmap.ic_a);
                    imageUrlList.add(R.mipmap.ic_c);
                    imageUrlList.add(R.mipmap.ic_c);
                }
            }
        }
        initView();
    }

    private void initView() {
        banner.isAutoPlay(true);
        banner.setViewPagerIsScroll(true);
        //设置主题
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载框架
        banner.setImageLoader(new GlideImageLoader());
        if (imageFileList != null && imageFileList.size() > 0) {
            banner.setImages(imageFileList);
        } else {
            banner.setImages(imageUrlList);
        }
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //进入到下一页
                Intent intent = new Intent(MainActivity.this, ImageDisplayActivity.class);
                startActivity(intent);
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onPause() {
        super.onPause();
        for (int j = 0; j < 50; j++) {
            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(getTaskId(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(innerReceiver);
    }
}
