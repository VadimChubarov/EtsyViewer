package com.example.vadim.EtsyViewer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class ItemDetailsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(false);

        ImageView itemPicture = findViewById(R.id.ItemPicture);
        TextView itemHeader = findViewById(R.id.ItemHeader);
        TextView itemDescription = findViewById(R.id.ItemDescription);
        TextView itemPrice = findViewById(R.id.ItemPrice);
        Button saveItemButton = findViewById(R.id.SaveItemButton);


        Intent intent = getIntent();
        int listingId = intent.getIntExtra("id",0);

        RecyclerItemData currentItem = AppManager.getInstance().getSearchResultsItem(listingId);

        if(currentItem==null){currentItem = AppManager.getInstance().getSavedItem(listingId); saveItemButton.setVisibility(View.INVISIBLE);}

        saveItemButton.setOnClickListener(AppManager.getInstance().getAppListener());


        Picasso.with(this).load(currentItem.getPictureURL()).into(itemPicture);
        itemHeader.setText(currentItem.getHeader());
        itemDescription.setText(currentItem.getDescription());
        itemPrice.setText(currentItem.getPrice());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {onBackPressed();}
        return false;
    }
}
