package com.ljw.aliocrdemo.ocr.bean;

/**
 * @author Android(JiaWei)
 * @date 2017/10/27.
 */

public class OCRBankCardDataJson {

    /**
     * card_num : 6236612508946
     * request_id : 20171027095218_5fc5b9070a5f5feaad2ec
     * success : true
     */

    private String card_num;
    private String request_id;
    private boolean success;

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
