package com.ljw.aliocrdemo.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author Android(JiaWei)
 * @date 2017/8/25
 */

public class LuBanUtils {
    public static void compressImg(Context mContext, Uri uri, final OnMyCompressListener compressListener) {
        Luban.with(mContext).load(new File(PathConvertUtils.uri2StringPath(mContext, uri)))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {}

                    @Override
                    public void onSuccess(File file) {
                        compressListener.onSuccess(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        compressListener.onError(e);
                    }
                })
                .launch();
    }

   public interface OnMyCompressListener {
        /**
         * 压缩成功
         * @param file
         */
        void onSuccess(File file);

        /**
         * 压缩失败
         * @param e
         */
        void onError(Throwable e);
    }
}
