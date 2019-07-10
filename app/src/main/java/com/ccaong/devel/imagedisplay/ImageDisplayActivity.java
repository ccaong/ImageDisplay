package com.ccaong.devel.imagedisplay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author devel
 */
public class ImageDisplayActivity extends BaseActivity {

    private List<Integer> imageUrlList;
    private List<String> imageFileList;
    private Banner banner;
    private TextView tvExit;
    private TextView tvCancel;
    private TextView tvSetting;
    private EditText editText;
    private Button btnExit;
    private Button btnCancel;
    private long touchTime = 0;
    private Boolean showExit = false;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (null == this) {
                //走到了onDestroy,则不再进行后续消息处理
                return false;
            }
            if (ImageDisplayActivity.this.isFinishing()) {
                //Activity正在停止，则不再后续处理
                return false;
            }

            if (msg.what == 1) {
                if (System.currentTimeMillis() - touchTime >= 10000 && !showExit) {
                    finish();
                } else {
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        banner = findViewById(R.id.banner);
        tvExit = findViewById(R.id.tv_exit);
        tvCancel = findViewById(R.id.tv_cancel);
        tvSetting = findViewById(R.id.tv_setting);
        editText = findViewById(R.id.et_pwd);
        btnExit = findViewById(R.id.btn_exit);
        btnCancel = findViewById(R.id.btn_cancel);

        initData();


        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnExit.setText("退出程序");

                tvExit.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                tvSetting.setVisibility(View.GONE);

                editText.setVisibility(View.VISIBLE);
                btnExit.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideButton();
            }
        });


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals(Hawk.get("IMAGE_PWD", "11223344"))) {
                    if (btnExit.getText().equals("进入设置")) {
                        startActivity(new Intent(ImageDisplayActivity.this, SettingActivity.class));
                    } else {
                        finishApp();
                    }
                } else {
                    Toast.makeText(ImageDisplayActivity.this, "密码输入错误！", Toast.LENGTH_SHORT).show();
                    hideButton();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideButton();
            }
        });

        tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnExit.setText("进入设置");
                tvExit.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                tvSetting.setVisibility(View.GONE);

                editText.setVisibility(View.VISIBLE);
                btnExit.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        });
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
        imageFileList = Hawk.get("SECOND_IMAGE_LIST");

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

    private long firstTime = 0;

    private void initView() {
        touchTime = System.currentTimeMillis();
        mHandler.sendEmptyMessageDelayed(1, 1000);

        banner.isAutoPlay(false);
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
                //2019/7/8 双击退出
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 800) {
                    firstTime = secondTime;
                } else {
                    showExit = true;
                    tvExit.setVisibility(View.VISIBLE);
                    tvCancel.setVisibility(View.VISIBLE);
                    tvSetting.setVisibility(View.VISIBLE);
                }
            }
        });

        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //计算时间
                touchTime = System.currentTimeMillis();
            }

            @Override
            public void onPageScrollStateChanged(int i) {


            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public void finishApp() {
        removeALLActivity();
    }

    private void hideButton() {
        touchTime = System.currentTimeMillis();
        showExit = false;
        tvExit.setVisibility(View.GONE);
        tvCancel.setVisibility(View.GONE);
        tvSetting.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        btnExit.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeMessages(1);
            mHandler = null;
        }
    }

}
