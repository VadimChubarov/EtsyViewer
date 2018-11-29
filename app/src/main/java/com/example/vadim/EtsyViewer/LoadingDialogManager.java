package com.example.vadim.EtsyViewer;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialogManager
{
    public static ProgressDialog getProgressDialog(String message, boolean cancelable, Context targetContext)
    {
       ProgressDialog progressDialog = new ProgressDialog(targetContext);
       progressDialog.setMessage(message);
       progressDialog.setCancelable(cancelable);

       return progressDialog;
    }
}
