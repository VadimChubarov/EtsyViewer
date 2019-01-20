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


public class FullscreenActivity extends AppCompatActivity
{
    private boolean isUIvisible;
    private final Handler hideHandler = new Handler();
    private ExtendedViewPager screenContent;
    private TouchImageAdapter fullScreenAdapter;

    private final Runnable hideUIwithDelay = new Runnable()
    {
        @SuppressLint("InlinedApi")
        @Override
        public void run()
        {
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

    private final Runnable showUIwithDelay = new Runnable()
    {

        @Override
        public void run()
        {
            screenContent.setSystemUiVisibility
                    ( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {actionBar.show();}
            isUIvisible = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        TextView toolbarHeader = findViewById(R.id.fullscreen_toolbar_header);
        Button toolbarBackButton = findViewById(R.id.fullscren_toolbar_back);
        Button toobarDownloadButton = findViewById(R.id.fullscren_toolbar_download);
        Toolbar toolbar = findViewById(R.id.fullscreen_toolbar);
        screenContent = findViewById(R.id.full_screen_content);

        setSupportActionBar(toolbar);showUI(0);

        Intent intent = getIntent();
        int pictureId = intent.getIntExtra("pictureId",0);
        RecyclerItemData picturesData = (RecyclerItemData) intent.getSerializableExtra("picturesData");

        TouchImageView[] fullScreenImages = new TouchImageView[picturesData.getFullscreenURL().size()];
        int[] picturesId = new int [picturesData.getFullscreenId().size()];

        for (int i = 0; i < fullScreenImages.length; i++) {
            TouchImageView touchImageView = new TouchImageView(this);
            Picasso.with(this).load(picturesData.getFullscreenURL().get(i)).into(touchImageView);
            touchImageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {toggle();}
            });

            fullScreenImages[i] = touchImageView;
            picturesId[i] = picturesData.getFullscreenId().get(i);
        }
        fullScreenAdapter = new TouchImageAdapter(fullScreenImages,picturesId);
        screenContent.setAdapter(fullScreenAdapter);
        screenContent.setCurrentItem(pictureId);

        toolbarHeader.setText(picturesData.getHeader());

        ToolbarClickListener toolbarClickListener = new ToolbarClickListener();
        toolbarBackButton.setOnClickListener(toolbarClickListener);
        toobarDownloadButton.setOnClickListener(toolbarClickListener);
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
        hideHandler.removeCallbacks(hideUIwithDelay);
        hideHandler.postDelayed(hideUIwithDelay, delay);
    }

    @SuppressLint("InlinedApi")
    private void showUI( int delay) {
        hideHandler.removeCallbacks(showUIwithDelay);
        hideHandler.postDelayed(showUIwithDelay, delay);
    }

    private class TouchImageAdapter extends PagerAdapter
    {
        private TouchImageView [] images;
        private int [] imagesId;

        public Drawable getImage(int position) {
            return images[position].getDrawable();
        }

        public int getImageId(int position) {return imagesId[position];}

        public TouchImageAdapter(TouchImageView [] images, int [] imagesId)
        {
            this.images = images;
            this.imagesId = imagesId;
        }

        public void resetAllZoom()
        {
            for(TouchImageView image : images){image.resetZoom();}
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            resetAllZoom();
            container.addView(images[position], ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return images[position];

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

    private class ToolbarClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fullscren_toolbar_back :
                    FullscreenActivity.this.onBackPressed();
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
