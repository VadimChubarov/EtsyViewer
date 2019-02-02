package com.example.vadim.EtsyViewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.squareup.picasso.Picasso;


public class ItemDetailsActivity extends AppCompatActivity
{
    int listingId;
    RecyclerItemData currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Toolbar toolbar = findViewById(R.id.item_details_toolbar);
        ImageButton backButton = findViewById(R.id.item_details_toolbar_back_arrow);
        CheckBox favoriteCheckbox = findViewById(R.id.item_details_toolbar_favorites_checkbox);
        OnToolbarListener onToolbarClickListener = new OnToolbarListener();
        backButton.setOnClickListener(onToolbarClickListener);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView itemHeader = findViewById(R.id.ItemHeader);
        TextView itemDescription = findViewById(R.id.ItemDescription);
        TextView itemPrice = findViewById(R.id.ItemPrice);
        ImageView itemPicture1 = findViewById(R.id.ItemPicture1);
        itemPicture1.setBackgroundResource(R.drawable.image_loading1);

        int auxPictureHeight = itemPicture1.getLayoutParams().height/2;
        int auxPictureMargin = Math.round(2 * getResources().getDisplayMetrics().density);

        LinearLayout auxPicturesLayout = findViewById(R.id.aux_pictures_layout);
        LinearLayout.LayoutParams auxPictureParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,auxPictureHeight,1);
        auxPictureParams.setMargins(auxPictureMargin,auxPictureMargin,auxPictureMargin,auxPictureMargin);

        Intent intent = getIntent();
        listingId = intent.getIntExtra("id",0);
        currentItem = AppManager.getInstance().getSearchResultsItem(listingId);

        if(AppManager.getInstance().getSavedItem(listingId)!=null){favoriteCheckbox.setChecked(true);}
        favoriteCheckbox.setOnCheckedChangeListener(onToolbarClickListener);

       if(currentItem==null){currentItem = AppManager.getInstance().getSavedItem(listingId);}


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

    public class OnToolbarListener implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.item_details_toolbar_back_arrow:
                    onBackPressed();
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getId()){
                case R.id.item_details_toolbar_favorites_checkbox:
                       AppManager.getInstance().getAppListener().onFavoriteListingChecked(currentItem,isChecked);
                       break;
            }
        }
    }
}
