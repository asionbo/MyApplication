package com.example.asionbo.myapplication.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.ui.adapter.ImagePickerAdapter;
import com.example.asionbo.myapplication.ui.dialog.SelectDialog;
import com.example.asionbo.myapplication.utils.GlideImageLoader;
import com.example.asionbo.myapplication.utils.PicassoImgaeLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asionbo on 2018/4/18.
 */

public class ImageUploadDialog extends DialogFragment implements ImagePickerAdapter.OnRecyclerViewItemClickListener{

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int IMAGE_ITEM_DEL = -2;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 4;
    private ImagePickerAdapter adapter;
    private RadioButton rbGlide,rbPicasso;
    private ImagePicker imagePicker;
    private Button btnSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_upload,container);
        rbGlide = view.findViewById(R.id.rb_glide);
        rbPicasso = view.findViewById(R.id.rb_picasso);
        btnSave = view.findViewById(R.id.btn_save);

        rbGlide.setChecked(true);
        initImagePicker();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(getActivity(), selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initImagePicker() {//设置图片加载器

        imagePicker = ImagePicker.getInstance();

        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(getActivity(), R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!getActivity().isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    private TextView tvCamera,tvPhoto,tvCancel;
    @Override
    public void onItemClick(View view, int position,boolean isDel) {
        if (rbGlide.isChecked()) {
            imagePicker.setImageLoader(new GlideImageLoader());
        }else {
            imagePicker.setImageLoader(new PicassoImgaeLoader());
        }
        switch (position){
            case IMAGE_ITEM_ADD:
                MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.dialog_bottom_select, false)
                        .backgroundColor(Color.parseColor("#00000001"))
                        .build();
                tvCamera = dialog.getCustomView().findViewById(R.id.tv_camera);
                tvPhoto = dialog.getCustomView().findViewById(R.id.tv_photo);
                tvCancel = dialog.getCustomView().findViewById(R.id.tv_cancel);
                Window window = dialog.getWindow();
                window.setBackgroundDrawableResource(android.R.color.transparent);
                window.setWindowAnimations(R.style.main_menu_animstyle);
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.x = 100;
                wlp.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
                wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                wlp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setAttributes(wlp);

                tvCamera.setOnClickListener(v -> {
                    //打开选择,本次允许选择的数量
//                    ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                    Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                    intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                    startActivityForResult(intent, REQUEST_CODE_SELECT);

                    dialog.dismiss();
                });
                tvPhoto.setOnClickListener(v -> {
                    Intent intent1 = new Intent(getActivity(), ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
                    intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                    startActivityForResult(intent1, REQUEST_CODE_SELECT);

                    dialog.dismiss();
                });
                tvCancel.setOnClickListener(v -> dialog.dismiss());
                dialog.show();
                break;
                default:
                    if (isDel){
                        images.remove(position);
                        if (images != null) {
                            selImageList.clear();
                            selImageList.addAll(images);
                            adapter.setImages(selImageList);
                        }
                    }else {
                        //打开预览
                        Intent intentPreview = new Intent(getActivity(), ImagePreviewDelActivity.class);
                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                    }
                    break;
        }
    }

    ArrayList<ImageItem> images = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }
}
