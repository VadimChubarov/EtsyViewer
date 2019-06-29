package com.example.vadim.EtsyViewer.utils;

import android.widget.Toast;

import com.example.vadim.EtsyViewer.presenter.AppManager;

public class MessageService {

    public static void showMessage(String message) {
        Toast.makeText(AppManager
                .getInstance()
                .getMainActivity()
                .getBaseContext(),message, Toast.LENGTH_SHORT)
                .show();
    }
}
