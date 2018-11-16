package com.example.vadim.EtsyViewer;

import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;


public class ContextMenuManager
{
    private NoteContextViewListener[] contextViewListeners;
    private String[] menuItems;
    private View viewForContextMenu;
    private int listingId;

    public ContextMenuManager(View viewForContextMenu, String[] menuItems, Fragment targetActivity, int listingId )
    {
        targetActivity.registerForContextMenu(viewForContextMenu);

        this.viewForContextMenu = viewForContextMenu;
        this.menuItems = menuItems;
        this.contextViewListeners = new NoteContextViewListener[menuItems.length];
        this.listingId = listingId;

        for (int i = 0; i < contextViewListeners.length; i++)
        {
            contextViewListeners[i] = new NoteContextViewListener();
        }
    }

    public void showContextMenu(ContextMenu menu, ContextMenu.ContextMenuInfo menuInfo)
    {
        for (int i = 0; i < menuItems.length; i++)
        {
            menu.add(0, i, 0, menuItems[i]);
            menu.getItem(i).setOnMenuItemClickListener(contextViewListeners[i]);
        }
    }

    private class NoteContextViewListener implements MenuItem.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
           AppManager.getInstance().deleteListing(listingId);
           return false;
        }
    }

}