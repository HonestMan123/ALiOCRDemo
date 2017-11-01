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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ljw.aliocrdemo.R;
import com.ljw.aliocrdemo.ocr.OCRHttpRequest;
import com.ljw.aliocrdemo.ocr.bean.OCRBankCardDataJson;
import com.ljw.aliocrdemo.ocr.bean.OCRIdCardResultJson;
import com.ljw.aliocrdemo.utils.BankCardNumUtils;
import com.ljw.aliocrdemo.utils.LuBanUtils;
import com.ljw.aliocrdemo.utils.MatisseUtils;
import com.soundcloud.android.crop.Crop;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.IOException;

/**
 * 识别银行卡
 * @author Android(JiaWei)
 * @date 2017/10/26.
 */

public class BankCardRecognitionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int IMG_CODE = 1;
    private static final int CROP_IMG_CODE = 2;
    private static final String TAG = BankCardRecognitionActivity.class.getSimpleName();
    private TextView resultv;
    private ProgressDialog mProgressDialog;
    private OCRBankCardDataJson bankCardDataJson;
    private String bankCardNum;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //银行卡识别完成
                    bankCardNum = bankCardDataJson.getCard_num();
                    resultv.setText("银行卡账号：" + bankCardNum);
                    dismissLoadingDialog();
                    try {
                        String binNum = bankCardNum.substring(0, 6);
                        String cardInfo = BankCardNumUtils.getNameOfBank(binNum.toCharArray(), 0);
                        Log.d(TAG, "binNum==" + binNum + "-----cardInfo==" + cardInfo);
                        resultv.append("\n发卡行及卡种："+cardInfo);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(resultv, "识别完成", Snackbar.LENGTH_SHORT).show();
                    break;
                case 1:
                    dismissLoadingDialog();
                    bankCardNum = "";
                    resultv.setText("");
                    if (TextUtils.isEmpty((CharSequence) msg.obj)) {
                        return;
                    }
                    Snackbar.make(resultv, (String) msg.obj, Snackbar.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcard);
        initViews();
        initProgressDialog();
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
    }

    private void initViews() {
        resultv = (TextView) findViewById(R.id.bankcard_result_tv);
        findViewById(R.id.bankcard_img_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bankcard_img_btn:
                //选择银行卡照片
                MatisseUtils.selectImg(BankCardRecognitionActivity.this, IMG_CODE);

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
                case IMG_CODE:
                    //选择完的银行卡照片

                    Uri mUri1 = Matisse.obtainResult(data).get(0);
                    try {
                        Uri outputUri = Uri.fromFile(File.createTempFile("corp1", ".jpg"));
                        Crop.of(mUri1, outputUri).withAspect(18, 11).start(BankCardRecognitionActivity.this, CROP_IMG_CODE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case CROP_IMG_CODE:
                    //裁剪完的照片
                    showLodingDialog("识别中...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            compressImg(Crop.getOutput(data));
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
    private void compressImg(Uri uri) {
        LuBanUtils.compressImg(BankCardRecognitionActivity.this, uri, new LuBanUtils.OnMyCompressListener() {
                    @Override
                    public void onSuccess(final File file) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                readBankCard(file);
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
     * 识别银行卡
     *
     * @param file
     */
    private void readBankCard(File file) {
        OCRHttpRequest.readBankCard(file, new OCRHttpRequest.OCRCallBack<OCRIdCardResultJson>() {
            @Override
            public void onSuccess(OCRIdCardResultJson resultJson) {
                bankCardDataJson = new Gson().fromJson(resultJson.getOutputs().get(0).getOutputValue().getDataValue(), OCRBankCardDataJson.class);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onFail(String errorMessage) {
                Message message = new Message();
                message.what = 1;
                message.obj = errorMessage;
                mHandler.sendMessage(message);
            }
        });
    }


    private void showLodingDialog(String message) {
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void dismissLoadingDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
