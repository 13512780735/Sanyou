package com.pk4pk.baseappmoudle.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by mayi on 16/12/26.
 *
 * @Autor CaiWF
 * @Email 401885064@qq.com
 * @TODO  Toast工具类
 */

public class KKToast {
    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show_middle(Context context, int resId) {
        show_middle(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    public static void show_middle(Context context, CharSequence text, int duration) {
        Toast toast=Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }
}
