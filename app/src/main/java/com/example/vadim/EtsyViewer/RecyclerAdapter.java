package com.example.vadim.EtsyViewer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>
{
    private List<RecyclerItemData> recyclerItemDataList = new ArrayList<>();

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


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_layout,viewGroup,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i)
    {

        recyclerViewHolder.bind(recyclerItemDataList.get(i));
        final int listingId = recyclerItemDataList.get(i).getListingId();

        recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppManager.getInstance().createDetailedScreen(listingId);
            }
        });

        if (AppManager.getInstance().getMainActivity().getFavoritesTabFragment().isFavoritesScreenActive())
        {
           final ContextMenuManager contextMenu =  new ContextMenuManager(recyclerViewHolder.itemView,
                    new String[]{"DELETE ITEM"},
                    AppManager.getInstance().getMainActivity().getFavoritesTabFragment(),
                    listingId);

            recyclerViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    AppManager.getInstance().getMainActivity().getFavoritesTabFragment().setFavoritesContextMenu(contextMenu);
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount()
    {
        return recyclerItemDataList.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView recyclerImage;
        private TextView recyclerText;

        public RecyclerViewHolder(View itemView)
        {
            super(itemView);

            recyclerImage = itemView.findViewById(R.id.recyclerImage);
            recyclerText = itemView.findViewById(R.id.recyclerText);
        }

        public void bind(RecyclerItemData recyclerItemData)
        {
            recyclerText.setText(recyclerItemData.getHeader());
            Picasso.with(itemView.getContext()).load(recyclerItemData.getImageURL()).into(recyclerImage);
        }
    }

}
