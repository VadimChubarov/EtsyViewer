package com.example.vadim.EtsyViewer;

import android.widget.Toast;

public class MessageService {

    public static void showMessage(String message) {
        Toast.makeText(AppManager
                .getInstance()
                .getMainActivity()
                .getBaseContext(),message, Toast.LENGTH_SHORT)
                .show();
    }
}
