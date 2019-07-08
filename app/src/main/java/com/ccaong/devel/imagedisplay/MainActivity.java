package com.ccaong.devel.imagedisplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author devel
 */
public class MainActivity extends AppCompatActivity {

    private List<Integer> imageUrlList;
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner = findViewById(R.id.banner);

        initData();
        initView();

    }

    /**
     * 设置默认数据
     */
    private void initData() {
        imageUrlList = new ArrayList<>();
        imageUrlList.add(R.mipmap.ic_a);
        imageUrlList.add(R.mipmap.ic_b);
        imageUrlList.add(R.mipmap.ic_c);
        imageUrlList.add(R.mipmap.ic_b);
        imageUrlList.add(R.mipmap.ic_a);
        imageUrlList.add(R.mipmap.ic_c);
        imageUrlList.add(R.mipmap.ic_c);
    }

    private void initView() {
        banner.isAutoPlay(true);
        banner.setViewPagerIsScroll(true);
        //设置主题
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载框架
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(imageUrlList);
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


}
