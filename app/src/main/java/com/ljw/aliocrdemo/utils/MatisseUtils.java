package com.ljw.aliocrdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

import com.ljw.aliocrdemo.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

/**
 * @author Android(JiaWei)
 * @date 2017/10/20.
 */

public class MatisseUtils {
    public static void selectImg(Context mContext, int requestCode){
        Matisse.from((Activity) mContext)
                .choose(MimeType.allOf())
                //选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                .theme(R.style.MatisseTheme)
                //是否显示数字
                .countable(true)
                //最大选择资源数量
                .maxSelectable(1)
                //添加自定义过滤器
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                //设置列宽
                .gridExpectedSize(mContext.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                //设置屏幕方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                //图片缩放比例
                .thumbnailScale(0.85f)
                //选择图片加载引擎
                .imageEngine(new GlideEngine())
                //是否可以拍照
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.jy.wallets.fileprovider"))
                .forResult(requestCode);
    }
}
