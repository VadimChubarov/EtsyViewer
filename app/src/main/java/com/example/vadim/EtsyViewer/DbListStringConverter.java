package com.example.vadim.EtsyViewer;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;


public class DbListStringConverter {

    Gson gson = new Gson();

    @TypeConverter
    public String fromListString(List<String> stringList ){
        return gson.toJson(stringList);
    }

    @TypeConverter
    public List<String> toListString(String string){
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(string, listType);
    }
}
