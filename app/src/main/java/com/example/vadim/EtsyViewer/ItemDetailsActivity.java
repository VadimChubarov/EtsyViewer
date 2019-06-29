package com.example.vadim.EtsyViewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.squareup.picasso.Picasso;


public class ItemDetailsActivity extends AppCompatActivity {

    RecyclerItemData currentItem;
    boolean isItemSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        receiveActivityData();
        setUpToolBar();
        setUpDescriptions();
        setUpPictures();
    }



    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.item_details_toolbar);
        ImageButton backButton = findViewById(R.id.item_details_toolbar_back_arrow);
        CheckBox favoriteCheckbox = findViewById(R.id.item_details_toolbar_favorites_checkbox);
        favoriteCheckbox.setChecked(isItemSaved);
        OnToolbarListener onToolbarListener = new OnToolbarListener();
        backButton.setOnClickListener(onToolbarListener);
        favoriteCheckbox.setOnCheckedChangeListener(onToolbarListener);
        setSupportActionBar(toolbar);
    }

    private void receiveActivityData() {
        Intent intent = getIntent();
        currentItem = (RecyclerItemData) intent.getSerializableExtra("itemData");
        isItemSaved =  intent.getBooleanExtra("isItemSaved",false);
    }

    private void setUpDescriptions() {
        TextView itemHeader = findViewById(R.id.ItemHeader);
        TextView itemDescription = findViewById(R.id.ItemDescription);
        TextView itemPrice = findViewById(R.id.ItemPrice);
        itemHeader.setText(currentItem.getHeader());
        itemDescription.setText(currentItem.getDescription());
        itemPrice.setText(currentItem.getPrice());
    }


    private void setUpPictures() {
        ImageView mainPicture = findViewById(R.id.ItemPicture1);
        mainPicture.setBackgroundResource(R.drawable.image_loading1);
        LinearLayout auxPicturesLayout = findViewById(R.id.aux_pictures_layout);

        try { Picasso.with(this).load(currentItem.getPictureURL().get(0)).into(mainPicture); }
        catch (Exception e) {}

        mainPicture.setOnClickListener(new OnPictureClickListener(0));

        int previewPicturesQty = currentItem.getPictureURL().size();
        int fullscreenPicturesQty = currentItem.getFullscreenURL().size();
        if(previewPicturesQty > 4 ){
            previewPicturesQty = 4;
        }
        int morePictQty = fullscreenPicturesQty - previewPicturesQty;

        int auxPicturesHeight = mainPicture.getLayoutParams().height / 2;
        int auxPicturesMargin = Math.round(2 * getResources().getDisplayMetrics().density);

        LinearLayout.LayoutParams auxPicturesParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, auxPicturesHeight, 1);
        auxPicturesParams.setMargins(auxPicturesMargin, auxPicturesMargin, auxPicturesMargin, auxPicturesMargin);

        for (int i = 1; i < previewPicturesQty; i++) {
            if(currentItem.getPictureURL().get(i)!=null) {
                ImageView auxPicture = new ImageView(this);
                auxPicture.setLayoutParams(auxPicturesParams);
                auxPicture.setBackgroundResource(R.drawable.image_loading1);
                auxPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                auxPicture.setOnClickListener(new OnPictureClickListener(i));
                auxPicturesLayout.addView(auxPicture);

                try { Picasso.with(this).load(currentItem.getPictureURL().get(i)).into(auxPicture); }
                catch (Exception e) {}
            }
        }

        if(morePictQty > 0) {
            TextView morePictQtyView = findViewById(R.id.more_pict_qty);
            View qtyBackgroindView = findViewById(R.id.more_pict_qty_background);
            String qty_text = "+ "+morePictQty;
            morePictQtyView.setText(qty_text);
            qtyBackgroindView.getLayoutParams().height = auxPicturesParams.height;
            qtyBackgroindView.getLayoutParams().width = Math.round((getResources().getConfiguration().screenWidthDp*getResources().getDisplayMetrics().density)/3);
            qtyBackgroindView.setVisibility(View.VISIBLE);
        }
    }



    public class OnPictureClickListener implements View.OnClickListener {

       private int position;

        public OnPictureClickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            AppManager.getInstance().getAppListener().onPictureClick(position,currentItem);
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
