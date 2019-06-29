package com.example.vadim.EtsyViewer.model.database.support;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class DbListIntegerConverter {

    Gson gson = new Gson();

    @TypeConverter
    public String fromListInteger(List<Integer> integerList ){
        return gson.toJson(integerList);
    }

    @TypeConverter
    public List<Integer> toListInteger(String string){
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(string, listType);
    }
}
