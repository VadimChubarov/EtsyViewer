package com.example.vadim.EtsyViewer;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class CoreProcess implements MainInterface.Model
{

    private MainInterface.API api;

    private Category currentCategoryResponse;

    private Listing currentLinstingResponse;

    private ArrayList<ListingImage> currentListingImagesResponse;

    private Map<Integer,RecyclerItemData> savedListings;

    private boolean imagesURLready;

    private boolean readyForSearch;

    private AppDbManager appDbManager;

    private String backUpData;



    public CoreProcess(Context context)
    {
        Retrofit downLoader = new Retrofit.Builder()
                .baseUrl("https://openapi.etsy.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.api = downLoader.create(MainInterface.API.class);

        this.currentListingImagesResponse = new ArrayList<>();

        this.savedListings = new LinkedHashMap<>();

        this.readyForSearch = true;

        this.appDbManager = new AppDbManager(context);

        recoverSession();
    }

    public boolean isReadyForSearch() {return readyForSearch;}

    @Override
    public String[] loadAllCategories()
    {
        readyForSearch = false;
        currentCategoryResponse = null;

        Call<Category> categoriesCall = api.findAllTopCategory();
        try{currentCategoryResponse = categoriesCall.execute().body();}catch(IOException e){}

        readyForSearch = true;

        return getNamesOfCategories();
    }

    @Override
    public String[] getNamesOfCategories()
    {
        return currentCategoryResponse.getNames();
    }

    @Override
    public void loadSearchListings(String categoty, String keyWords)
    {
        readyForSearch = false;
        currentLinstingResponse = null;

        Call<Listing> listingsCall = api.findAllListingActive(categoty,keyWords,10);
        try{currentLinstingResponse = listingsCall.execute().body();}catch(IOException e){}

        readyForSearch = true;
    }

    public void loadSearchListingsPage(String categoty, String keyWords, String page)
    {
        readyForSearch = false;
        currentLinstingResponse = null;

        int nextPage = Integer.parseInt(page);
        Call<Listing> listingsCall = api.findAllListingActive(categoty,keyWords,10,nextPage);

        try{currentLinstingResponse = listingsCall.execute().body();}catch(IOException e){}

        readyForSearch = true;
    }

    public int getNextListingPage()
    {
        try{return currentLinstingResponse.getPagination().getNextPage();}
        catch(Exception e){return 0;}
    }

    @Override
    public int[] getSearchListingsId()
    {
        return  currentLinstingResponse.getAllListingsId();
    }

    @Override
    public void loadListingImageURL(final int [] listingId)
    {
        readyForSearch = false;
        imagesURLready = false;
        currentListingImagesResponse.clear();

        for(int i = 0; i < listingId.length; i++)
        {
            Call<ListingImage> listingImagesCall = api.findAllListingImages(listingId[i]);
            listingImagesCall.enqueue(new Callback<ListingImage>() {
                @Override
                public void onResponse(Call<ListingImage> call, Response<ListingImage> response)
                {
                    if (response.isSuccessful())
                    {
                        currentListingImagesResponse.add(response.body());
                        if(currentListingImagesResponse.size()==listingId.length){imagesURLready = true;}
                    } else {
                        System.out.println("response code " + response.code());}
                }
                @Override
                public void onFailure(Call<ListingImage> call, Throwable t)
                {System.out.println("failure " + t);}
            });
            try{Thread.sleep(250);}catch(Exception e){}
        }

        int count = 0;
        while (count < 20)
        {
            if (imagesURLready)
            {
                imagesURLready = false;
                break;
            }
            try {Thread.sleep(500);} catch (Exception e){}
            count++;
        }
        readyForSearch = true;
    }

    @Override
    public RecyclerItemData  getListingDetails(int listingId)
    {
        Listing.ListingItem listingItem = currentLinstingResponse.getListingItemById(listingId);

        String header = listingItem.getTitle();
        String description = listingItem.getDescription();
        String price = listingItem.getPrice();
        String listingImageURL = "";
        String listingPictureURL = "";

        for(ListingImage listingImage : currentListingImagesResponse)
        {
          if (listingImage.getResults().get(0).getListingId() == listingId)
          {
              listingImageURL = listingImage.getResults().get(0).getUrl75x75();
              listingPictureURL = listingImage.getResults().get(0).getUrlFullxfull();
          }
        }
        return new RecyclerItemData(listingId,header,description,listingImageURL,listingPictureURL,price);
    }

    public boolean isListingSaved(int listingId)
    {
       if(savedListings.containsKey(listingId)){return true;}
       return false;
    }

    @Override
    public void saveListing(RecyclerItemData itemData)
    {
        if (!isListingSaved(itemData.getListingId()))
        {
            savedListings.put(itemData.getListingId(), itemData);
            saveSession();
        }
    }

    @Override
    public void deleteListing(int listingId)
    {
        savedListings.remove(listingId);
        saveSession();
    }

    @Override
    public List<RecyclerItemData> getAllSavedListings()
    {
       List<RecyclerItemData> listingItems = new ArrayList<>();

        for(Map.Entry<Integer, RecyclerItemData> listingItem : savedListings.entrySet())
       {
           listingItems.add(listingItem.getValue());
       }
       return listingItems;
    }

    @Override
    public void saveSession()
    {
        String currentSessionData = new Gson().toJson(savedListings);
        appDbManager.saveAppData(currentSessionData,backUpData);
        backUpData = appDbManager.loadAppData();
    }

    @Override
    public void recoverSession()
    {
         backUpData = appDbManager.loadAppData();
         Type itemsMapType = new TypeToken<Map<Integer, RecyclerItemData>>() {}.getType();
         if(backUpData!=null){savedListings = new Gson().fromJson(backUpData, itemsMapType);}
    }
}
