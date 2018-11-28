package com.example.vadim.EtsyViewer;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

public class SearchResultsActivity extends AppCompatActivity
{
    private SwipeRefreshLayout searchSwipeLayout;
    private RecyclerView searchResultsRecycler;
    private SelectableRecyclerAdapter recyclerAdapter;
    private LinearLayoutManager layoutManager;
    private String category;
    private String keyWords;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(false);

        runSearchResultsRecycler();
        showRecyclerItems();

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        keyWords = intent.getStringExtra("keyWords");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {onBackPressed();}
        return false;
    }

    private void runSearchResultsRecycler()
    {
        searchSwipeLayout = findViewById(R.id.search_swipe_layout);
        searchResultsRecycler = findViewById(R.id.search_recycler);

        searchSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                AppManager.getInstance().refreshListOfSearchResults(category,keyWords);
            }
        });

        searchSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        layoutManager = new LinearLayoutManager(this);
        searchResultsRecycler.setLayoutManager(layoutManager);

        recyclerAdapter = new SelectableRecyclerAdapter();
        searchResultsRecycler.setAdapter(recyclerAdapter);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItems) >= totalItemCount)
                {
                     AppManager.getInstance().createNextPageOfSearchResults(category,keyWords);
                }
            }
        };
        searchResultsRecycler.setOnScrollListener(scrollListener);

        AppManager.getInstance().setCurrentSearchLayout(searchSwipeLayout);
        AppManager.getInstance().setCurrentRecyclerAdapter(recyclerAdapter);
    }

    private void showRecyclerItems()
    {
        recyclerAdapter.clearItems();
        recyclerAdapter.setItems(AppManager.getInstance().getSearchResults());
    }

}

