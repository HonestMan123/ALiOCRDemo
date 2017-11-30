package com.ljw.aliocrdemo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ljw.aliocrdemo.R;
import com.ljw.aliocrdemo.ocr.OCRHttpRequest;
import com.ljw.aliocrdemo.ocr.bean.OCRIdCardBackDataJson;
import com.ljw.aliocrdemo.ocr.bean.OCRIdCardFaceDataJson;
import com.ljw.aliocrdemo.ocr.bean.OCRIdCardResultJson;
import com.ljw.aliocrdemo.utils.LuBanUtils;
import com.ljw.aliocrdemo.utils.MatisseUtils;
import com.soundcloud.android.crop.Crop;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.IOException;

/**
 * 身份证识别（包含正反面）
 *
 * @author Android(JiaWei)
 * @date 2017/10/26.
 */

public class IdCardRecognitionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int FACE_IMG_CODE = 1;
    private static final int FACE_CROP_IMG_CODE = 2;
    private static final int BACK_IMG_CODE = 3;
    private static final int BACK_CROP_IMG_CODE = 4;
    private static final String TAG = IdCardRecognitionActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private TextView nameTv;
    private TextView genderTv;
    private TextView birthTv;
    private TextView addressTv;
    private TextView numTv;
    private TextView issueTv;
    private TextView timeTv;

    private String name;
    private String gender;
    private String birth;
    private String address;
    private String num;
    private String issue;
    private String time;

    private OCRIdCardFaceDataJson faceDataJson;
    private OCRIdCardBackDataJson backDataJson;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //身份证正面识别完成
                    name = faceDataJson.getName();
                    gender = faceDataJson.getSex();
                    birth = faceDataJson.getBirth();
                    address = faceDataJson.getAddress();
                    num = faceDataJson.getNum();
                    nameTv.setText("姓名：" + name);
                    genderTv.setText("性别：" + gender);
                    birthTv.setText("出生：" + birth);
                    addressTv.setText("住址：" + address);
                    numTv.setText("身份证号码：" + num);
                    dismissLoadingDialog();
                    Snackbar.make(nameTv, "识别完成", Snackbar.LENGTH_SHORT).show();
                    break;
                case 1:
                    dismissLoadingDialog();
                    if (TextUtils.isEmpty((CharSequence) msg.obj)) {
                        return;
                    }
                    Snackbar.make(nameTv, (String) msg.obj, Snackbar.LENGTH_SHORT).show();
                    break;
                case 2:
                    //身份证反面识别完成
                    issue = backDataJson.getIssue();
                    time = backDataJson.getStart_date() + "" + backDataJson.getEnd_date();
                    issueTv.setText("发证机关：" + issue);
                    timeTv.setText("有效期限：" + time);
                    dismissLoadingDialog();
                    Snackbar.make(nameTv, "识别完成", Snackbar.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        initViews();

    }

    private void initViews() {
        mProgressDialog = new ProgressDialog(this);
        nameTv = (TextView) findViewById(R.id.idcard_name_tv);
        genderTv = (TextView) findViewById(R.id.idcard_gender_tv);
        birthTv = (TextView) findViewById(R.id.idcard_birth_tv);
        addressTv = (TextView) findViewById(R.id.idcard_address_tv);
        numTv = (TextView) findViewById(R.id.idcard_num_tv);
        issueTv = (TextView) findViewById(R.id.idcard_issue_tv);
        timeTv = (TextView) findViewById(R.id.idcard_time_tv);

        findViewById(R.id.idcard_face_img_btn).setOnClickListener(this);
        findViewById(R.id.idcard_back_img_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.idcard_face_img_btn:
                //身份证正面
                MatisseUtils.selectImg(IdCardRecognitionActivity.this, FACE_IMG_CODE);
                break;
            case R.id.idcard_back_img_btn:
                //身份证反面
                MatisseUtils.selectImg(IdCardRecognitionActivity.this, BACK_IMG_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FACE_IMG_CODE:
                    //选择的正面照片
                    Uri mUri1 = Matisse.obtainResult(data).get(0);
                    try {
                        Uri outputUri = Uri.fromFile(File.createTempFile("corp1", ".jpg"));
                        Crop.of(mUri1, outputUri).withAspect(18, 11).start(IdCardRecognitionActivity.this, FACE_CROP_IMG_CODE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case FACE_CROP_IMG_CODE:
                    //裁剪后的正面照片
                    showLodingDialog("识别中...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            compressImg(Crop.getOutput(data), OCRHttpRequest.SIDE_FACE);
                        }
                    }).start();
                    break;
                case BACK_IMG_CODE:
                    //选择的反面照片
                    Uri mUri = Matisse.obtainResult(data).get(0);
                    try {
                        Uri outputUri = Uri.fromFile(File.createTempFile("corp1", ".jpg"));
                        Crop.of(mUri, outputUri).withAspect(18, 11).start(IdCardRecognitionActivity.this, BACK_CROP_IMG_CODE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case BACK_CROP_IMG_CODE:
                    //裁剪后的反面照片
                    showLodingDialog("识别中...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            compressImg(Crop.getOutput(data), OCRHttpRequest.SIDE_BACK);
                        }
                    }).start();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 压缩图片
     *
     * @param uri
     */
    private void compressImg(Uri uri, final String side) {
        LuBanUtils.compressImg(IdCardRecognitionActivity.this, uri, new LuBanUtils.OnMyCompressListener() {
                    @Override
                    public void onSuccess(final File file) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                readIdCard(file, side);
                            }
                        }).start();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = "识别失败，请稍后重试";
                        mHandler.sendMessage(message);
                        Log.d(TAG, "图片压缩失败：" + e.getMessage());
                    }
                }
        );
    }

    /**
     * 识别身份证
     *
     * @param cardFile
     * @param side
     */
    private void readIdCard(File cardFile, final String side) {
        OCRHttpRequest.readIdCardImg(cardFile, side, new OCRHttpRequest.OCRCallBack<OCRIdCardResultJson>() {
            @Override
            public void onFail(String errorMessage) {
                Message message = new Message();
                message.what = 1;
                message.obj = errorMessage;
                mHandler.sendMessage(message);
            }

            @Override
            public void onSuccess(OCRIdCardResultJson ocrIdCardResultJson) {
                if (OCRHttpRequest.SIDE_FACE.equals(side)) {
                    faceDataJson = new Gson().fromJson(ocrIdCardResultJson.getOutputs().get(0).getOutputValue().getDataValue(), OCRIdCardFaceDataJson.class);
                    Log.d(TAG, "姓名：" + faceDataJson.getName()
                            + "\n" + "性别：" + faceDataJson.getSex()
                            + "\n" + "生日：" + faceDataJson.getBirth()
                            + "\n" + "住址：" + faceDataJson.getAddress()
                            + "\n" + "身份证号码：" + faceDataJson.getNum());
                    mHandler.sendEmptyMessage(0);
                } else {
                    backDataJson = new Gson().fromJson(ocrIdCardResultJson.getOutputs().get(0).getOutputValue().getDataValue(), OCRIdCardBackDataJson.class);
                    StringBuilder stringBuilder = new StringBuilder(backDataJson.getStart_date());
                    stringBuilder.insert(4, ".");
                    stringBuilder.insert(7, ".");
                    stringBuilder.insert(10, "\t-\t");
                    backDataJson.setStart_date(stringBuilder.toString());
                    if (!"长期".equals(backDataJson.getEnd_date())) {
                        StringBuilder stringBuilder1 = new StringBuilder(backDataJson.getEnd_date());
                        stringBuilder1.insert(4, ".");
                        stringBuilder1.insert(7, ".");
                        backDataJson.setEnd_date(stringBuilder1.toString());
                    }
                    Log.d(TAG, "签发机关：" + backDataJson.getIssue()
                            + "\n" + "有效期限：" + backDataJson.getStart_date() + backDataJson.getEnd_date());
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
    }

    private void showLodingDialog(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    private void dismissLoadingDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
