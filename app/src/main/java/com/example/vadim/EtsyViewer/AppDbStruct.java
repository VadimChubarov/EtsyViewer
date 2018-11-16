package com.example.vadim.EtsyViewer;
import android.provider.BaseColumns;

public final class AppDbStruct
{
    private AppDbStruct() {
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_SAVED_DATA = "title";
        public static final String COLUMN_DATA = "subtitle";
    }
}
