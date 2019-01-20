package com.example.vadim.EtsyViewer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
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
        itemPicture1.setOnClickListener(new OnPictureClickListener(0,currentItem));

        int previewPicturesQty = currentItem.getPictureURL().length;
        int fullscreenPicturesQty = currentItem.getFullscreenURL().size();
        int morePictQty = fullscreenPicturesQty - previewPicturesQty;

        Picasso.with(this).load(currentItem.getPictureURL()[0]).into(itemPicture1);

        for(int i = 1; i < previewPicturesQty; i++) {

            if(currentItem.getPictureURL()[i]!=null) {
                ImageView auxPicture = new ImageView(this);
                auxPicture.setLayoutParams(auxPictureParams);
                auxPicture.setBackgroundResource(R.drawable.image_loading1);
                auxPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                auxPicture.setOnClickListener(new OnPictureClickListener(i,currentItem));
                auxPicturesLayout.addView(auxPicture);

                Picasso.with(this).load(currentItem.getPictureURL()[i]).into(auxPicture);
            }
        }

        if(morePictQty>0) {
            TextView morePictQtyView = findViewById(R.id.more_pict_qty);
            View qtyBackgroindView = findViewById(R.id.more_pict_qty_background);
            String qty_text = "+ "+morePictQty;
            morePictQtyView.setText(qty_text);
            qtyBackgroindView.getLayoutParams().height = auxPictureParams.height;
            qtyBackgroindView.getLayoutParams().width = Math.round((getResources().getConfiguration().screenWidthDp*getResources().getDisplayMetrics().density)/3);
            qtyBackgroindView.setVisibility(View.VISIBLE);
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

    public class OnPictureClickListener implements View.OnClickListener
    {
        int pictureId;
        RecyclerItemData picturesData;

        public OnPictureClickListener(int pictureId, RecyclerItemData picturesData) {
            this.pictureId = pictureId;
            this.picturesData = picturesData;
        }
        @Override
        public void onClick(View v) {
            AppManager.getInstance().getAppListener().onPictureClick(pictureId,picturesData);
        }
    }
}
