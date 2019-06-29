package com.example.vadim.EtsyViewer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import java.util.List;
import io.reactivex.Maybe;


@Database(entities = {RecyclerItemData.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    private static volatile AppDataBase appDataBase;
    public abstract FavoritesDao favoritesDao();

    public static synchronized AppDataBase getDatabase(Context context){
        if(appDataBase == null){
            appDataBase = Room.databaseBuilder(context,
                    AppDataBase.class, "AppDatabase").build();
        }
        return appDataBase;
    }


    @Dao
    @TypeConverters({DbListIntegerConverter.class,DbListStringConverter.class})
    public interface FavoritesDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(RecyclerItemData data);

        @Query("DELETE FROM saved_listings WHERE listingId = :listingId")
        void deleteItem(int listingId);

        @Query("DELETE FROM saved_listings WHERE listingId in (:listingIdList)")
        void deleteItems(List<Integer> listingIdList);

        @Query("DELETE FROM saved_listings")
        void deleteAllItems();

        @Query("SELECT * from saved_listings ORDER BY listingId ASC")
        Maybe<List<RecyclerItemData>> getAllItems();

        @Query("SELECT * FROM saved_listings WHERE listingId = :listingId ")
        Maybe<RecyclerItemData> getItem(String listingId);
    }
}
