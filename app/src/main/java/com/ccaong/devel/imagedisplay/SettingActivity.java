package com.ccaong.devel.imagedisplay;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author devel
 */
public class SettingActivity extends BaseActivity {


    private EditText etPwd;
    private EditText etPwdRe;
    private RecyclerView recyclerViewFirst;
    private RecyclerView recyclerViewSecond;

    List<LocalMedia> list1;
    List<LocalMedia> list2;
    GridImageAdapter adapterFirst;
    GridImageAdapter adapterSecond;

    List<String> listFirst = new ArrayList<>();
    List<String> listSecond = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        etPwd = findViewById(R.id.et_pwd);
        etPwdRe = findViewById(R.id.et_pwd_re);

        recyclerViewFirst = findViewById(R.id.recycle_view_1);
        recyclerViewSecond = findViewById(R.id.recycle_view_2);


        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageList();
            }
        });

        findViewById(R.id.btn_save_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePwd();
            }
        });
        initPictureFirst();
        initPictureSecond();
    }


    public void initPictureFirst() {
        //图片多选
        list1 = new ArrayList<>();
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerViewFirst.setLayoutManager(manager);
        adapterFirst = new GridImageAdapter(this, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                requestPermission(list1,2333);
            }
        });
        adapterFirst.setList(list1);
        adapterFirst.setSelectMax(100);
        recyclerViewFirst.setAdapter(adapterFirst);
        adapterFirst.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {//点击看大图，或者播放视频
                if (list1.size() > 0) {
                    LocalMedia media = list1.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(SettingActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, list1);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }


    public void initPictureSecond() {
        //图片多选
        list2 = new ArrayList<>();
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerViewSecond.setLayoutManager(manager);
        adapterSecond = new GridImageAdapter(this, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                requestPermission(list2,91);
            }
        });
        adapterSecond.setList(list2);
        adapterSecond.setSelectMax(100);
        recyclerViewSecond.setAdapter(adapterSecond);
        adapterSecond.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {//点击看大图，或者播放视频
                if (list2.size() > 0) {
                    LocalMedia media = list2.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(SettingActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, list2);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    public void requestPermission(final List<LocalMedia> list,final int code) {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    selectMultipleImage(SettingActivity.this, 100, list, code);
                } else {
                    Toast.makeText(SettingActivity.this, getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }


    public static void selectMultipleImage(final Activity activity, final int maxSelectNum, final List<LocalMedia> selectList, final int requestCode) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(true)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                .minimumCompressSize(1000)// 小于100kb的图片不压缩
                .forResult(requestCode);//结果回调onActivityResult code

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2333:
                    if (PictureSelector.obtainMultipleResult(data) == null) {
                        Toast.makeText(this, "选择照片失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    list1 = PictureSelector.obtainMultipleResult(data);
                    listFirst.clear();
                    for (LocalMedia media : list1) {
                        listFirst.add(media.getPath());
                    }
                    adapterFirst.setList(list1);
                    adapterFirst.notifyDataSetChanged();
                    //上传

                    break;
                case 91:
                    if (PictureSelector.obtainMultipleResult(data) == null) {
                        Toast.makeText(this, "选择照片失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    list2 = PictureSelector.obtainMultipleResult(data);
                    listSecond.clear();
                    for (LocalMedia media : list2) {
                        listSecond.add(media.getPath());
                    }
                    adapterSecond.setList(list2);
                    adapterSecond.notifyDataSetChanged();
                    //上传

                default:
                    break;

            }
        }
    }


    private void savePwd() {

        if (etPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        }

        if (etPwdRe.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
        }

        if (etPwdRe.getText().toString().trim().equals((etPwd.getText().toString().trim()))) {
            Hawk.put("IMAGE_PWD", etPwdRe.getText().toString().trim());
            Toast.makeText(this, "密码重置成功", Toast.LENGTH_SHORT).show();
            etPwd.setText("");
            etPwdRe.setText("");
        } else {
            Toast.makeText(this, "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageList() {

        if (listFirst != null && listFirst.size() > 0) {
            Hawk.put("FIRST_IMAGE_LIST", listFirst);
        }

        if (listSecond != null && listSecond.size() > 0) {
            Hawk.put("SECOND_IMAGE_LIST", listSecond);
        }

        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }

}
