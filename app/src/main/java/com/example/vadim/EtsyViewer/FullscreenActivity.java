package com.example.vadim.EtsyViewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;


public class FullscreenActivity extends AppCompatActivity {

    private boolean isUIvisible;
    private ExtendedViewPager screenContent;
    private TouchImageAdapter fullScreenAdapter;
    int pictureId;
    private RecyclerItemData currentItem;
    private final Handler navigationBarToggle = new Handler();

    private final Runnable hideUI = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {actionBar.hide();}
            screenContent.setSystemUiVisibility
                     (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            isUIvisible = false;
        }
    };

    private final Runnable showUI = new Runnable() {
        @Override
        public void run() {
            screenContent.setSystemUiVisibility
                    ( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {actionBar.show();}
            isUIvisible = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        receiveActivityData();
        setUpToolbar();
        setUpFullScreenContent();
        showUI(0);
    }

   private void receiveActivityData() {
       Intent intent = getIntent();
       pictureId = intent.getIntExtra("pictureId",0);
       currentItem = (RecyclerItemData) intent.getSerializableExtra("picturesData");
   }

   private void setUpToolbar(){
       TextView toolbarHeader = findViewById(R.id.fullscreen_toolbar_header);
       Button toolbarBackButton = findViewById(R.id.fullscren_toolbar_back);
       Button toobarDownloadButton = findViewById(R.id.fullscren_toolbar_download);
       Toolbar toolbar = findViewById(R.id.fullscreen_toolbar);

       toolbarHeader.setText(currentItem.getHeader());

       ToolbarListener toolbarListener = new ToolbarListener();
       toolbarBackButton.setOnClickListener(toolbarListener);
       toobarDownloadButton.setOnClickListener(toolbarListener);

       setSupportActionBar(toolbar);
   }

   private void setUpFullScreenContent(){
       List<TouchImageView> fullScreenImages = new ArrayList<>();
       for(String pictureURL : currentItem.getFullscreenURL()){
           TouchImageView touchImageView = new TouchImageView(this);
           touchImageView.setOnClickListener( view -> {toggle();});
           Picasso.with(this).load(pictureURL).into(touchImageView);
           fullScreenImages.add(touchImageView);
       }

       fullScreenAdapter = new TouchImageAdapter(fullScreenImages,currentItem.getFullscreenId());
       screenContent = findViewById(R.id.full_screen_content);
       screenContent.setAdapter(fullScreenAdapter);
       screenContent.setCurrentItem(pictureId);
   }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        hideUI(2000);
    }

    private void toggle() {
       if(isUIvisible){ hideUI(200);}
       else{showUI(200);}
    }

    private void hideUI(int delay) {
        navigationBarToggle.removeCallbacks(hideUI);
        navigationBarToggle.postDelayed(hideUI, delay);
    }

    @SuppressLint("InlinedApi")
    private void showUI( int delay) {
        navigationBarToggle.removeCallbacks(showUI);
        navigationBarToggle.postDelayed(showUI, delay);
    }


    private class TouchImageAdapter extends PagerAdapter {
        private List <TouchImageView> images;
        private List<Integer> imagesId;

        public Drawable getImage(int position) {
            return images.get(position).getDrawable();
        }

        public int getImageId(int position) {return imagesId.get(position);}

        public TouchImageAdapter(List<TouchImageView> images, List<Integer> imagesId) {
            this.images = images;
            this.imagesId = imagesId;
        }

        public void resetAllZoom() {
            for(TouchImageView image : images){image.resetZoom();}
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            resetAllZoom();
            container.addView(images.get(position), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return images.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    private class ToolbarListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fullscren_toolbar_back :
                    onBackPressed();
                    break;

                case R.id.fullscren_toolbar_download :
                    Drawable picture = fullScreenAdapter.getImage(screenContent.getCurrentItem());
                    String pictureName = "Etsy_picture_"+ fullScreenAdapter.getImageId(screenContent.getCurrentItem());
                    AppManager.getInstance().getAppListener().
                            onDownloadPictureClick(picture,pictureName);
                    break;
            }
        }
    }
}
