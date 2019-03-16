package com.example.vadim.EtsyViewer;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;

public class AppManager {
    private CoreProcess coreProcess;
    private MainActivity mainActivity;
    private static AppManager appManager;
    private AppListener appListener;

        private AppManager(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
            this.coreProcess = new CoreProcess(mainActivity);
            this.appListener = new AppListener();
        }

       public static AppManager getInstance(MainActivity mainActivity) //!!!!!!
       {
            if( appManager == null)
            {
                appManager = new AppManager(mainActivity);
            }
            return appManager;
        }

        public static void onDestroy()
        {
           appManager = null;
        }

        public static AppManager getInstance() {return appManager;}


    public CoreProcess getCoreProcess() {return coreProcess;}

    public MainActivity getMainActivity() {return mainActivity;}

    public AppListener getAppListener() {return appListener;}

    public RecyclerItemData getSavedItem (int listingId)
    {
        for(RecyclerItemData item : coreProcess.getAllSavedListings())
        {
            if(item.getListingId()==listingId){return item;}
        }
        return null;
    }


    public void requestAllCategories() {
           coreProcess.loadAllCategories();
    }

    public void onAllCategoriesResponse (Category category){
        mainActivity.showCategories(category.getNames());
    }


    public void requestListOfSearchResults() {
        String category = mainActivity.getSelectedCategory();
        String keyWords = mainActivity.getSearchKeyWord();
        if(keyWords.length()>0) {
            mainActivity.showLoadingDialog(true);
            coreProcess.loadSearchListings(category,keyWords,1);
        }
    }

    public void refreshListOfSearchResults(String category, String keyWords) {
       if(coreProcess.isReadyForSearch()){
           coreProcess.loadSearchListings(category,keyWords,1);
       }
       else{
           mainActivity.getCurrentSearchScreen().showProgressBar("refresh",false);
       }
    }

    public void requestNextPageOfSearchResults(String category, String keywords) {
        if(coreProcess.getNextPageOfSearch()!=0 && coreProcess.isReadyForSearch()) {
            coreProcess.loadSearchListings(category,keywords,coreProcess.getNextPageOfSearch());
            mainActivity.getCurrentSearchScreen().showProgressBar("pagination",true);
        }
    }

    public void onReceivedListOfSearchResults(ArrayList<RecyclerItemData> searchResults){
            if(mainActivity.getCurrentSearchScreen()==null){
                mainActivity.showSearchScreen(searchResults);
                mainActivity.showLoadingDialog(false);
            }
            else if(coreProcess.getNextPageOfSearch()> 2){
                    mainActivity.getCurrentSearchScreen().showRecyclerItems(searchResults,true);
                    mainActivity.getCurrentSearchScreen().showProgressBar("pagination",false);
                }
                else{
                    mainActivity.getCurrentSearchScreen().showRecyclerItems(searchResults,false);
                    mainActivity.getCurrentSearchScreen().showProgressBar("refresh",false);
                }
        }


  public void onErrorResponse(Throwable error){

    }

  //  @Override
    public void createDetailedScreen(RecyclerItemData itemData) {
        mainActivity.showItemDetailsScreen(itemData);
    }

  //  @Override
    public List<RecyclerItemData> getSavedListings()
    {
        return coreProcess.getAllSavedListings();
    }

   // @Override
    public void saveListing(RecyclerItemData itemData)
    {
        coreProcess.saveListing(itemData);
    }

 //   @Override
    public void deleteListing(int listingId) {
        coreProcess.deleteListing(listingId);
    }

    public void deleteSelectedListings(List<RecyclerItemData> selectedItems) {
        for(RecyclerItemData item : selectedItems) {
            deleteListing(item.getListingId());
        }
        if(coreProcess.getAllSavedListings().size() == 0){
            mainActivity.getFavoritesTabFragment().showAddFavorites(true);
        }
    }

    public class AppListener
    {
        public void onSearchSubmitClick() {
            if (coreProcess.isReadyForSearch()) {
                requestListOfSearchResults();
            }
        }

        public void onPictureClick (int pictureId, RecyclerItemData picturesData) {
            mainActivity.showFullScreen(pictureId,picturesData);
        }

        public void onDownloadPictureClick (Drawable picture, String pictureName) {
            Bitmap pictureBitmap = ((BitmapDrawable)picture).getBitmap();
            new ImageStorageManager().savePicture(pictureBitmap,pictureName,"Etsy_Viewer_Pictures");
        }

        public void onFavoriteListingChecked (RecyclerItemData listingItem, boolean isChecked) {
            int listingId = listingItem.getListingId();
            if(isChecked){
                if(getSavedItem(listingId)==null) {
              //  try{ saveListing(getSearchResultsItem(listingId)); }
               // catch(NullPointerException e){saveListing(listingItem);}
                MessageService.showMessage("Item added to favorites");
                }
                else{ MessageService.showMessage("Item already in favorites"); }
            }
            else {
                deleteListing(listingId);
                MessageService.showMessage("Item deleted from favorites");
            }
        }

        public void onAddFavoritesClick() {
            mainActivity.getTabLayout().getTabAt(0).select();
        }

        public void onNoSearchResultsClick() {
            mainActivity.getCurrentSearchScreen().onBackPressed();
            mainActivity.getTabLayout().getTabAt(0);
        }
    }
}
