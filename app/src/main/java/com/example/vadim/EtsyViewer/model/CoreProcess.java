package com.example.vadim.EtsyViewer.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.vadim.EtsyViewer.model.database.AppDataBase;
import com.example.vadim.EtsyViewer.model.data.Listing;
import com.example.vadim.EtsyViewer.model.data.ListingImage;
import com.example.vadim.EtsyViewer.utils.MessageService;
import com.example.vadim.EtsyViewer.model.data.RecyclerItemData;
import com.example.vadim.EtsyViewer.model.network.RequestsRepository;
import com.example.vadim.EtsyViewer.presenter.AppManager;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.*;

public class CoreProcess {

    private RequestsRepository requestsRepository;
    private ArrayList<RecyclerItemData> currentSearchResults = new ArrayList<>();
    private int nextPageOfSearch = 0;
    private boolean readyForSearch;
    private AppDataBase appDataBase;

    public int getNextPageOfSearch() {
        return nextPageOfSearch;
    }

    public boolean isReadyForSearch() {
        return readyForSearch;
    }

    public CoreProcess(Context context) {
        this.requestsRepository = new RequestsRepository();
        this.readyForSearch = true;
        this.appDataBase = AppDataBase.getDatabase(context);
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

    public void saveListingToDB(RecyclerItemData itemData) {
        Completable.fromAction(() -> {
            appDataBase.favoritesDao().insert(itemData);
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void deleteListingFromDB(int listingId) {
        Completable.fromAction(() -> {
           appDataBase.favoritesDao().deleteItem(listingId);
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void deleteListingsFromDB(List<Integer> listngIdList) {
        Completable.fromAction(() -> {
            appDataBase.favoritesDao().deleteItems(listngIdList);
            checkIsDbEmpty();
        }).subscribeOn(Schedulers.io()).subscribe();
    }


    public void getListingFromDB(int listingId){
        appDataBase.favoritesDao().getItem(String.valueOf(listingId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<RecyclerItemData>() {
                    @Override
                    public void onSuccess(RecyclerItemData savedListing) {
                        AppManager.getInstance().onIsListingSavedInDB(savedListing);
                    }

                    @Override
                    public void onError(Throwable e) {
                       onErrorResponse(e);
                    }

                    @Override
                    public void onComplete() {
                        AppManager.getInstance().onIsListingSavedInDB(null);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getAllListingsFromDB() {
        appDataBase.favoritesDao()
                .getAllItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( this::onLoadAllListingsFromDB, this::onDBErrorResponse, this::onDBisEmpty);
    }

    private void onLoadAllListingsFromDB(List<RecyclerItemData> itemData){
       if(itemData.size()!=0) {
           AppManager.getInstance().onSavedListingsReceived(itemData);
       }else{
           onDBisEmpty();
       }
    }

    private void onDBisEmpty(){
       AppManager.getInstance().onSavedListingsReceived(new ArrayList<>());
    }

    @SuppressLint("CheckResult")
    public void checkIsDbEmpty(){
        appDataBase.favoritesDao()
                .getAllItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( this::onCheckDbIsEmpty, this::onDBErrorResponse, this::onDBisEmpty);
    }

    private void onCheckDbIsEmpty(List<RecyclerItemData> itemData){
       if(itemData.size()==0){
           onDBisEmpty();
       }
    }


    private void onDBErrorResponse(Throwable error){
        MessageService.showMessage(error.toString());
        AppManager.getInstance().onErrorResponse(error);
    }
}
