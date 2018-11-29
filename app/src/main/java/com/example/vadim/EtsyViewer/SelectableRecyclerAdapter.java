package com.example.vadim.EtsyViewer;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.squareup.picasso.Picasso;
import java.util.*;

public class SelectableRecyclerAdapter extends RecyclerView.Adapter<SelectableRecyclerAdapter.RecyclerViewHolder>
{
    private int selectionColor = Color.LTGRAY;
    private List<RecyclerItemData> recyclerItemDataList = new ArrayList<>();
    private ActionModeManager recyclerActionMode = new ActionModeManager();


    public ActionModeManager getRecyclerActionMode() {return recyclerActionMode;}

    public void setSelectionColor(String selectionColor)
    {
        this.selectionColor = Color.parseColor(selectionColor);
    }

    public void setItemCheckbox(View ItemView, boolean active, boolean animation)
    {
        final AnimCheckBox itemCheckbox = ItemView.findViewById(R.id.recycler_checkbox);

        if(active)
        {
            itemCheckbox.setVisibility(View.VISIBLE);
            itemCheckbox.setChecked(true,animation);
        }
        else
            {
                itemCheckbox.setChecked(false,animation);

                OnDelayTaskWithUI onDelayTaskWithUI = new OnDelayTaskWithUI();
                onDelayTaskWithUI.setDelay(400);
                onDelayTaskWithUI.setView(itemCheckbox);
                onDelayTaskWithUI.execute();
            }
    }

    public void setItems(Collection<RecyclerItemData> recyclerItems)
    {
        recyclerItemDataList.addAll(recyclerItems);
        notifyDataSetChanged();
    }

    public void clearItems()
    {
        recyclerItemDataList.clear();
        notifyDataSetChanged();
    }

   public void deleteSelectedItems()
   {
       for (int i = 0; i < recyclerItemDataList.size();i++)
       {
           if(recyclerItemDataList.get(i).isSelected()){recyclerItemDataList.remove(i);i--;}
           notifyItemRemoved(i);
           notifyItemRangeChanged(0,getItemCount());
       }

   }

    public boolean anyItemSelected()
    {
        for (RecyclerItemData item : recyclerItemDataList)
        {
            if (item.isSelected()){return true;}
        }
        return false;
    }

    public void selectItem(int position, View itemView, boolean animation)
    {
        recyclerItemDataList.get(position).setSelected(true);
        itemView.setBackgroundColor(selectionColor);
        setItemCheckbox(itemView,true,animation);
        recyclerActionMode.updateActionModeItemCount();
    }

    public void cancelItemSelection(int position, View itemView)
    {
        recyclerItemDataList.get(position).setSelected(false);

        itemView.setBackgroundColor(Color.TRANSPARENT);
        setItemCheckbox(itemView,false,true);

        if (!anyItemSelected())
        {
            recyclerActionMode.activateActionMode(false);
        }
        recyclerActionMode.updateActionModeItemCount();
    }

    public void cancelAllSelections()
    {
        for (int i = 0; i < recyclerItemDataList.size(); i++)
        {
           recyclerItemDataList.get(i).setSelected(false);
           notifyItemChanged(i);
        }
    }

    public List<RecyclerItemData> getAllSelectedItems()
    {
        List<RecyclerItemData> selectedItems = new ArrayList<>();

        for (RecyclerItemData item : recyclerItemDataList)
        {
            if(item.isSelected()){selectedItems.add(item);}
        }
        return selectedItems;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_layout, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i)
    {
        recyclerViewHolder.bind(recyclerItemDataList.get(i));

        if (recyclerItemDataList.get(i).isSelected())
        {
            selectItem(i, recyclerViewHolder.itemView,false);
        }else
            {
                recyclerViewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                recyclerViewHolder.recyclerCheckBox.setVisibility(View.INVISIBLE);
            }

        OnItemClickListener onItemClickListener = new OnItemClickListener(i);
        recyclerViewHolder.itemView.setOnLongClickListener(onItemClickListener);
        recyclerViewHolder.itemView.setOnClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return recyclerItemDataList.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView recyclerImage;
        private TextView recyclerText;
        private  AnimCheckBox recyclerCheckBox;


        public RecyclerViewHolder(View itemView)
        {
            super(itemView);

            recyclerImage = itemView.findViewById(R.id.recyclerImage);
            recyclerText = itemView.findViewById(R.id.recyclerText);
            recyclerCheckBox = itemView.findViewById(R.id.recycler_checkbox);
        }

        public void bind(RecyclerItemData recyclerItemData)
        {
            recyclerText.setText(recyclerItemData.getHeader());
            Picasso.with(itemView.getContext()).load(recyclerItemData.getImageURL()).into(recyclerImage);
            recyclerCheckBox.setChecked(false,false);
            recyclerCheckBox.setClickable(false);
        }
    }

    private class OnItemClickListener implements View.OnClickListener, View.OnLongClickListener
    {
        private int position;

        public OnItemClickListener(int position)
        {
           this.position = position;
        }

        @Override
        public void onClick(View v)
        {
                RecyclerItemData currentItem = recyclerItemDataList.get(position);

                if (recyclerActionMode.isActionModeactive() && !currentItem.isSelected())
                {
                    selectItem(position, v,true);
                }
                else if (recyclerActionMode.isActionModeactive() && currentItem.isSelected())
                {
                    cancelItemSelection(position, v);
                }
                else
                    {
                        int listingId = recyclerItemDataList.get(position).getListingId();
                        AppManager.getInstance().getMainActivity().showItemDetailsScreen(listingId);
                    }
            }

        @Override
        public boolean onLongClick(View v)
        {
            RecyclerItemData currentItem = recyclerItemDataList.get(position);

            if (!currentItem.isSelected())
            {
                if (!recyclerActionMode.isActionModeactive()){recyclerActionMode.activateActionMode(true);}
                selectItem(position, v,true);
            }
            else {cancelItemSelection(position, v);}

            return true;
        }
    }

    protected class ActionModeManager implements android.support.v7.view.ActionMode.Callback
    {
        private ActionMode selectedItemsMode;
        private boolean isActionModeactive = false;

        public boolean isActionModeactive() {return isActionModeactive;}


        public void activateActionMode(boolean activate)
        {
            if (activate)
            {
                isActionModeactive = true;
                selectedItemsMode = AppManager.getInstance().getMainActivity().startSupportActionMode(recyclerActionMode);
            }
            else if(isActionModeactive) {selectedItemsMode.finish();}
        }

        public void updateActionModeItemCount()
        {
            if(isActionModeactive)
            {
                selectedItemsMode.setTitle(String.valueOf(getAllSelectedItems().size()));
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.action_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            switch(item.getItemId())
            {
                case R.id.menu_delete : AppManager.getInstance().deleteSelectedListings(getAllSelectedItems());
                                        deleteSelectedItems();
                                        if (!anyItemSelected()){activateActionMode(false);}
                break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            isActionModeactive = false;
            cancelAllSelections();
        }
    }

    private class OnDelayTaskWithUI extends AsyncTask
    {
        private int delay = 0;
        private View view;

        public void setDelay(int delay) {this.delay = delay;}
        public void setView(View view) {this.view = view;}

        @Override
        protected Object doInBackground(Object[] objects)
        {
            try{Thread.sleep(delay);}catch(Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            if(!((AnimCheckBox) view).isChecked())
            {view.setVisibility(View.INVISIBLE);}
        }
    }
}
