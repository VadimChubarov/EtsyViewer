package com.example.vadim.EtsyViewer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity implements MainInterface.View
{

    private SerachTabFragment searchTabFragment;
    private FavoritesTabFragment favoritesTabFragment;
    private ProgressDialog progressDialog;
    private Intent searchResultsIntent;
    private Intent itemDetailsIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createToolbar();
        createViewPager();

        searchResultsIntent = new Intent(this,SearchResultsActivity.class);
        itemDetailsIntent = new Intent(this,ItemDetailsActivity.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


        AppManager appManager = AppManager.getInstance(this);
        appManager.createListOfCategories();
    }


    private void createToolbar()
    {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void createViewPager()
    {
        searchTabFragment = new SerachTabFragment();
        favoritesTabFragment = new FavoritesTabFragment();

        ViewPager viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(searchTabFragment,"SEARCH");
        adapter.addFragment(favoritesTabFragment,"FAVORITES");

        viewPager.setAdapter(adapter);
        TabLayout tabLayout =  findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void showLoadingDialog(boolean show)
    {
        if (show)
        {progressDialog.show();}
        else {progressDialog.dismiss();}
    }

    public Intent getItemDetailsIntent() {return itemDetailsIntent;}

    public FavoritesTabFragment getFavoritesTabFragment() {return favoritesTabFragment;}

    public void showCategories(String[] categories)
    {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,categories);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTabFragment.setSpinner(spinnerAdapter);
    }

    @Override
    public String getSearchKeyWord()
    {
        return searchTabFragment.getSearchBar().getText().toString();
    }

    public String getSelectedCategory()
    {
        return searchTabFragment.getSpinner().getSelectedItem().toString();
    }

    @Override
    public void showSearchScreen()
    {
        searchResultsIntent.putExtra("category",getSelectedCategory());
        searchResultsIntent.putExtra("keyWords",getSearchKeyWord());

        startActivity(searchResultsIntent);
    }

    public void showItemDetailsScreen(int listingId)
    {
        itemDetailsIntent.putExtra("id",listingId);

        startActivity(itemDetailsIntent);
    }
}






