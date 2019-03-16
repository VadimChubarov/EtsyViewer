package com.example.vadim.EtsyViewer;

import android.annotation.SuppressLint;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Type;
import java.util.*;

public class CoreProcess {

    private RequestsRepository requestsRepository;
    private Map<Integer,RecyclerItemData> savedListings;
    private ArrayList<RecyclerItemData> currentSearchResults = new ArrayList<>();
    private int nextPageOfSearch = 0;
    private boolean readyForSearch;
    private AppDbManager appDbManager;
    private String backUpData;

    public ArrayList<RecyclerItemData> getCurrentSearchResults() {
        return currentSearchResults;
    }

    public int getNextPageOfSearch() {
        return nextPageOfSearch;
    }

    public boolean isReadyForSearch() {
        return readyForSearch;
    }

    public CoreProcess(Context context) {
        this.requestsRepository = new RequestsRepository();
        this.savedListings = new LinkedHashMap<>();
        this.readyForSearch = true;
        this.appDbManager = new AppDbManager(context);

        recoverSession();
    }

    // network requests

    @SuppressLint("CheckResult")
    public void loadAllCategories() {
        requestsRepository.getAllCategoriesRequest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(AppManager.getInstance()::onAllCategoriesResponse,this::onErrorResponse);
    }


    @SuppressLint("CheckResult")
    public void loadSearchListings(String category, String keyWords,int page) {
            readyForSearch = false;
            requestsRepository.getSearchListingsRequest(category, keyWords, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onListingsDataReceived, this::onErrorResponse);
    }
    @SuppressLint("CheckResult")
    private void onListingsDataReceived(Listing listingsData){
        nextPageOfSearch = listingsData.getPagination().getNextPage();

        for(Listing.ListingItem listingItem : listingsData.getResults()){
            currentSearchResults.add(new RecyclerItemData(
                    listingItem.getListingId(),
                    listingItem.getTitle(),
                    listingItem.getDescription(),
                    listingItem.getPrice(),
                    listingItem.getCurrencyCode()));
        }
        int [] listingId = listingsData.getAllListingsId();
        Observable.range(0, listingId.length)
                  .flatMap(i ->  requestsRepository.getListnigImagesRequest(listingId[i]))
                  .toList()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(this::onListingsImagesReceived, this::onErrorResponse);
    }
    private void onListingsImagesReceived(List<ListingImage> listingsImageData){
       for(int i = 0; i < currentSearchResults.size(); i++){
           RecyclerItemData itemData = currentSearchResults.get(i);
           ListingImage listingImage = listingsImageData.get(i);

           itemData.setImageURL(listingImage.getPreviewURL());
           itemData.setPictureURL(listingImage.getPicturesURL());
           itemData.setFullscreenURL(listingImage.getPicturesFullScreenURL());
           itemData.setFullscrenId(listingImage.getPicturesFullScreenId());
       }
       AppManager.getInstance().onReceivedListOfSearchResults(new ArrayList<>(currentSearchResults));
       currentSearchResults.clear();
       readyForSearch = true;
    }

    private void onErrorResponse(Throwable error){
        readyForSearch = true;
        MessageService.showMessage(error.toString());
        AppManager.getInstance().onErrorResponse(error);
    }


    // DataBase requests

    public boolean isListingSaved(int listingId)
    {
       if(savedListings.containsKey(listingId)){return true;}
       return false;
    }

   // @Override
    public void saveListing(RecyclerItemData itemData)
    {
        if (!isListingSaved(itemData.getListingId()))
        {
            savedListings.put(itemData.getListingId(), itemData);
            saveSession();
        }
    }

   // @Override
    public void deleteListing(int listingId)
    {
        savedListings.remove(listingId);
        saveSession();
    }

   // @Override
    public List<RecyclerItemData> getAllSavedListings()
    {
       List<RecyclerItemData> listingItems = new ArrayList<>();

        for(Map.Entry<Integer, RecyclerItemData> listingItem : savedListings.entrySet())
       {
           listingItems.add(listingItem.getValue());
       }
       return listingItems;
    }

   // @Override
    public void saveSession()
    {
        String currentSessionData = new Gson().toJson(savedListings);
        appDbManager.saveAppData(currentSessionData,backUpData);
        backUpData = appDbManager.loadAppData();
    }

   // @Override
    public void recoverSession()
    {
         backUpData = appDbManager.loadAppData();
         Type itemsMapType = new TypeToken<Map<Integer, RecyclerItemData>>() {}.getType();
         if(backUpData!=null){savedListings = new Gson().fromJson(backUpData, itemsMapType);}
    }
}
