package com.example.vadim.EtsyViewer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        TextView itemHeader = findViewById(R.id.ItemHeader);
        TextView itemDescription = findViewById(R.id.ItemDescription);
        TextView itemPrice = findViewById(R.id.ItemPrice);
        Button saveItemButton = findViewById(R.id.SaveItemButton);

        ImageView itemPicture1 = findViewById(R.id.ItemPicture1);
        itemPicture1.setBackgroundResource(R.drawable.image_loading1);

        int auxPictureHeight = itemPicture1.getLayoutParams().height/2;
        int auxPictureMargin = Math.round(2 * getResources().getDisplayMetrics().density);

        LinearLayout auxPicturesLayout = findViewById(R.id.aux_pictures_layout);
        LinearLayout.LayoutParams auxPictureParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,auxPictureHeight,1);
        auxPictureParams.setMargins(auxPictureMargin,auxPictureMargin,auxPictureMargin,auxPictureMargin);

        Intent intent = getIntent();
        int listingId = intent.getIntExtra("id",0);

        RecyclerItemData currentItem = AppManager.getInstance().getSearchResultsItem(listingId);

        if(currentItem==null){currentItem = AppManager.getInstance().getSavedItem(listingId); saveItemButton.setVisibility(View.INVISIBLE);}

        saveItemButton.setOnClickListener(AppManager.getInstance().getAppListener());

        String[] picturesURL = currentItem.getPictureURL();

        Picasso.with(this).load(picturesURL[0]).into(itemPicture1);

        for(int i = 1; i < picturesURL.length;i++)
        {
            if(picturesURL[i]!=null)
            {
                ImageView auxPicture = new ImageView(this);
                auxPicture.setLayoutParams(auxPictureParams);
                auxPicture.setBackgroundResource(R.drawable.image_loading1);
                auxPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                auxPicturesLayout.addView(auxPicture);

                Picasso.with(this).load(picturesURL[i]).into(auxPicture);
            }
        }

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
