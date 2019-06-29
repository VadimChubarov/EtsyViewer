package com.example.vadim.EtsyViewer.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;

public class ImageStorageManager {

        public void savePicture(Bitmap bitmap, String filename, String folderName) {

            File storageDirectory = Environment.getExternalStorageDirectory();
            File targetFolder = new File(storageDirectory.getAbsoluteFile(), "/"+folderName);
            File file = new File(targetFolder.getAbsoluteFile(), filename + ".jpg") ;

            if (file.exists()) { MessageService.showMessage("Picture already saved");}
            else {
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    MessageService.showMessage("Picture successfully saved");
                } catch (Exception e) { MessageService.showMessage("Picture saving error");}
            }
        }
    }
