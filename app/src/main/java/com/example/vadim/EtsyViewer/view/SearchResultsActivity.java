package com.example.vadim.EtsyViewer.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.vadim.EtsyViewer.presenter.AppManager;
import com.example.vadim.EtsyViewer.R;
import com.example.vadim.EtsyViewer.model.data.RecyclerItemData;
import com.example.vadim.EtsyViewer.view.support.SimpleRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    private SwipeRefreshLayout searchSwipeLayout;
    private RecyclerView searchResultsRecycler;
    private SimpleRecyclerAdapter recyclerAdapter;
    private LinearLayoutManager layoutManager;
    private ViewGroup noSearchResultsImage;
    private ProgressBar progressBar;
    private String category;
    private String keyWords;
    private List<RecyclerItemData> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        setUpToolbar();
        receiveData();
        initViews();
        runSearchResultsRecycler();
        showRecyclerItems(searchResults,false);
        AppManager.getInstance().getMainActivity().setCurrentSearchScreen(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().getMainActivity().setCurrentSearchScreen(null);
    }

    private void setUpToolbar(){
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
    }

    private void receiveData(){
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        keyWords = intent.getStringExtra("keyWords");
        Type listType = new TypeToken<ArrayList<RecyclerItemData>>(){}.getType();
        this.searchResults = new Gson().fromJson(intent.getStringExtra("searchResults"), listType);
    }

    private void initViews(){
        searchSwipeLayout = findViewById(R.id.search_swipe_layout);
        searchResultsRecycler = findViewById(R.id.search_recycler);
        noSearchResultsImage = findViewById(R.id.vg_no_search_results_image);
        noSearchResultsImage.setOnClickListener(new SearchResultsScreenListener());
        progressBar = findViewById(R.id.pagination_loading);
        searchSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }

    private void runSearchResultsRecycler() {
        searchSwipeLayout.setOnRefreshListener(() ->
                AppManager.getInstance().refreshListOfSearchResults(category, keyWords));

        layoutManager = new LinearLayoutManager(this);
        searchResultsRecycler.setLayoutManager(layoutManager);
        recyclerAdapter = new SimpleRecyclerAdapter();
        searchResultsRecycler.setAdapter(recyclerAdapter);

        OnScrollListener scrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                    AppManager.getInstance().requestNextPageOfSearchResults(category, keyWords);
                }
            }
        };
        searchResultsRecycler.setOnScrollListener(scrollListener);
    }

    public void showProgressBar(String type, boolean show) {
        switch (type) {
            case "pagination":
                if (show) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.getLayoutParams().height = progressBar.getWidth();
                    progressBar.requestLayout();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.getLayoutParams().height = 0;
                    progressBar.requestLayout();
                }
                break;

            case "refresh":
                if (show) {
                    searchSwipeLayout.setRefreshing(true);
                } else {
                    searchSwipeLayout.setRefreshing(false);
                }
                break;
        }
    }

    public void showRecyclerItems(Collection<RecyclerItemData> recyclerItems, boolean nextPage) {
        if (recyclerItems.size() > 0) {
           if(nextPage){recyclerAdapter.addItems(recyclerItems);}
           else{recyclerAdapter.setItems(recyclerItems);}
        } else {
            showNoSearchResult(true);
        }
    }

    public void showNoSearchResult(boolean show) {
        if (show) {
            searchResultsRecycler.setVisibility(View.INVISIBLE);
            noSearchResultsImage.setVisibility(View.VISIBLE);
        } else {
            noSearchResultsImage.setVisibility(View.GONE);
            searchResultsRecycler.setVisibility(View.VISIBLE);
        }
    }

    private class SearchResultsScreenListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.vg_no_search_results_image:
                    AppManager.getInstance().getAppListener().onNoSearchResultsClick();
                    break;
            }
        }

    }
}

