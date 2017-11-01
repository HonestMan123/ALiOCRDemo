package com.ljw.aliocrdemo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author Android(JiaWei)
 * @date 2017/10/26.
 */

public class PathConvertUtils {
    /**
     * uri转String path
     * @param context
     * @param uri
     * @return
     */
    public static String uri2StringPath(final Context context, final Uri uri ) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String path = null;
        if (scheme == null) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        path = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return path;
    }
}
