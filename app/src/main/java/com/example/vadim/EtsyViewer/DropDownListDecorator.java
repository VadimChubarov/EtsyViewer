package com.example.vadim.EtsyViewer;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Spinner;
import java.lang.reflect.Field;

public class DropDownListDecorator
{
    public void setMaxiDropDownHeight(Context targetContext, Spinner spinner, double dropDownMaxHeight /* % to screen size : 1.0 - fullScreen*/) {
        Display display = ((WindowManager) targetContext.getSystemService(targetContext.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int height = point.y;
        int popupMaxsize = (int) Math.round(height * dropDownMaxHeight);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);
            popupWindow.setHeight(popupMaxsize);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }
    }
}
