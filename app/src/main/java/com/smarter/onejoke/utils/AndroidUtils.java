package com.smarter.onejoke.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by panl on 16/1/21.
 */
public class AndroidUtils {
    public static void CopyText(Context context,String content){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("joke",content);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "复制成功！", Toast.LENGTH_SHORT).show();
    }
}
