package com.example.vadim.EtsyViewer;

import android.os.AsyncTask;
import android.widget.Toast;

public class ApiRequestManager extends AsyncTask<String,Void,String[]>
{
    private String responseType;
    private int [] listingsId;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(String... requestParams) {

        this.responseType = requestParams[0];

        switch (this.responseType)
        {
            case "Categories":
                return AppManager.getInstance().getCoreProcess()
                                  .loadAllCategories();

            case "Listings": AppManager.getInstance().getCoreProcess()
                                               .loadSearchListings(requestParams[1], requestParams[2]);

                             listingsId = AppManager.getInstance().getCoreProcess().getSearchListingsId();

                             AppManager.getInstance().getCoreProcess()
                                                .loadListingImageURL(listingsId);

                             AppManager.getInstance().getSearchResults().clear();
                             for(int i = 0; i < listingsId.length; i++)
                             {
                                 AppManager.getInstance().getSearchResults()
                                         .add(AppManager.getInstance().getCoreProcess().getListingDetails(listingsId[i]));
                             }
                             break;

            case "Refresh" : AppManager.getInstance().getCoreProcess()
                                                .loadSearchListings(requestParams[1], requestParams[2]);

                             listingsId = AppManager.getInstance().getCoreProcess().getSearchListingsId();

                             AppManager.getInstance().getCoreProcess()
                                                .loadListingImageURL(listingsId);

                             AppManager.getInstance().getSearchResults().clear();
                             for(int i = 0; i < listingsId.length; i++)
                             {
                                 AppManager.getInstance().getSearchResults()
                                                    .add(AppManager.getInstance().getCoreProcess().getListingDetails(listingsId[i]));
                             }
                             break;

            case "Pagination" : AppManager.getInstance().getCoreProcess()
                                                   .loadSearchListingsPage(requestParams[1], requestParams[2], requestParams[3]);

                                listingsId = AppManager.getInstance().getCoreProcess().getSearchListingsId();

                                AppManager.getInstance().getCoreProcess()
                                               .loadListingImageURL(listingsId);

                                for(int i = 0; i < listingsId.length; i++)
                                  {
                                      AppManager.getInstance().getSearchResults()
                                                 .add(AppManager.getInstance().getCoreProcess().getListingDetails(listingsId[i]));
                                  }
                                  break;
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String[] categories)
    {
        super.onPostExecute(categories);

        switch (responseType)
        {
            case "Categories":
                for(int i = 0 ; i < categories.length; i++)
                {categories[i] = categories[i].replace("_"," ");}
                AppManager.getInstance().getMainActivity().showCategories(categories);
                break;

            case "Listings":
                AppManager.getInstance().getMainActivity().showLoadingDialog(false);
                AppManager.getInstance().getMainActivity().showSearchScreen();
                break;

            case "Refresh":
                AppManager.getInstance().getCurrentRecyclerAdapter().clearItems();
                AppManager.getInstance().getCurrentRecyclerAdapter().setItems(AppManager.getInstance().getSearchResults());
                AppManager.getInstance().getCurrentSearchLayout().setRefreshing(false);

                Toast toastRefresh = Toast.makeText(AppManager.getInstance().getMainActivity(),"Items refreshed", Toast.LENGTH_SHORT);
                toastRefresh.show();
                break;

            case "Pagination":
                AppManager.getInstance().getCurrentRecyclerAdapter().clearItems();
                AppManager.getInstance().getCurrentRecyclerAdapter().setItems(AppManager.getInstance().getSearchResults());

                Toast toastPagination = Toast.makeText(AppManager.getInstance().getMainActivity(),"Next page loaded", Toast.LENGTH_SHORT);
                toastPagination.show();
                break;
        }
    }
}
