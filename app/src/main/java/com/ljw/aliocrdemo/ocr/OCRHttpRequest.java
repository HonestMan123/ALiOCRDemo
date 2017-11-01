package com.ljw.aliocrdemo.ocr;

import android.util.Log;

import com.google.gson.Gson;
import com.ljw.aliocrdemo.ocr.bean.OCRIdCardResultJson;
import com.ljw.aliocrdemo.ocr.constant.Constants;
import com.ljw.aliocrdemo.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Android(JiaWei)
 * @date 2017/10/26.
 */

public class OCRHttpRequest {
    public static final String SIDE_FACE = "face";
    public static final String SIDE_BACK = "back";

    /**
     * 身份证识别
     *
     * @param cardImgFile      图片文件
     * @param side             身份证正反面
     * @param readCardCallback
     */
    public static void readIdCardImg(File cardImgFile, final String side, final OCRCallBack<OCRIdCardResultJson> readCardCallback) {
        String base64Img = FileUtils.fileToBase64(cardImgFile);
        String body = "{\"inputs\":[{\"image\":{\"dataType\":50,\"dataValue\":\"" + base64Img + "\"},\"configure\":{\"dataType\":50,\"dataValue\":\"{\\\"side\\\":\\\"" + side + "\\\"}\"}}]}";
        String getPath = "/rest/160601/ocr/ocr_idcard.json";
        HttpUtil.getInstance().httpPostBytes(AppConfiguration.ID_CARD_BASE_URL, getPath, null, null, body.getBytes(Constants.CLOUDAPI_ENCODING), null, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                readCardCallback.onFail(e.getMessage() + "");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                StringBuilder result = new StringBuilder();
                if (response.code() != 200) {
                    readCardCallback.onFail("请选择清晰的身份证照片");
                    Log.d("OCR", result.append("错误原因：").append(response.header("X-Ca-Error-Message")).append(Constants.CLOUDAPI_LF).append(Constants.CLOUDAPI_LF).toString());
                    return;
                }
                result.append(new String(response.body().bytes(), Constants.CLOUDAPI_ENCODING));
                readCardCallback.onSuccess(new Gson().fromJson(result.toString(), OCRIdCardResultJson.class));
            }
        });
    }

    /**
     * 识别银行卡
     * @param cardImgFile
     * @param callBack
     */
    public static void readBankCard(File cardImgFile, final OCRCallBack<OCRIdCardResultJson> callBack){
        String base64Img = FileUtils.fileToBase64(cardImgFile);
        String body = "{\"inputs\": [{\"image\": {\"dataType\": 50,\"dataValue\": \""+base64Img+"\"}}]}";
        String getPath = "/rest/160601/ocr/ocr_bank_card.json";
        HttpUtil.getInstance().httpPostBytes(AppConfiguration.BANK_CARD_BASE_URL, getPath, null, null, body.getBytes(Constants.CLOUDAPI_ENCODING), null, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                callBack.onFail(e.getMessage()+"");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                StringBuilder result = new StringBuilder();
                Log.d("OCR",response.toString());
                if (response.code() != 200) {
                    callBack.onFail("请选择清晰的银行卡照片");
                    Log.d("OCR",result.append("错误原因：").append(response.header("X-Ca-Error-Message")).append(Constants.CLOUDAPI_LF).append(Constants.CLOUDAPI_LF).toString());
                    return;
                }
                result.append(new String(response.body().bytes(), Constants.CLOUDAPI_ENCODING));
                callBack.onSuccess(new Gson().fromJson(result.toString(), OCRIdCardResultJson.class));
            }
        });
    }




    public interface OCRCallBack<T> {
        void onSuccess(T t);

        void onFail(String s);
    }
}
