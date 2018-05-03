package com.pk4pk.baseappmoudle.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mayi.pk4pk.baseappmoudle.R;


public class LoaddingDialog extends Dialog {


    public LoaddingDialog(@NonNull Context context) {
        super(context, R.style.dialogStyle);
        setContentView(R.layout.dialog_loadding);
    }
}
