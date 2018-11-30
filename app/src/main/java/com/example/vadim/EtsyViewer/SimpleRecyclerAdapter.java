package com.example.vadim.EtsyViewer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.RecyclerViewHolder>
{
    private List<RecyclerItemData> recyclerItemDataList = new ArrayList<>();

    public void setItems(Collection<RecyclerItemData> recyclerItems)
    {
        clearItems();
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
    public SimpleRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_layout, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i)
    {
        recyclerViewHolder.bind(recyclerItemDataList.get(i));
         SimpleRecyclerAdapter.OnItemClickListener onItemClickListener = new OnItemClickListener(i);
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

    private class OnItemClickListener implements View.OnClickListener
    {
        private int position;

        public OnItemClickListener(int position)
        {
            this.position = position;
        }

        @Override
        public void onClick(View v)
        {
            int listingId = recyclerItemDataList.get(position).getListingId();
            AppManager.getInstance().getMainActivity().showItemDetailsScreen(listingId);
        }
    }
}
