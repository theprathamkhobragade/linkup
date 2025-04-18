package com.project.linkup;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<GridItem> gridItemList;
    private LayoutInflater inflater;

    public GridAdapter(Context context, List<GridItem> gridItemList) {
        this.context = context;
        this.gridItemList = gridItemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return gridItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        ViewHolder holder;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.card_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = itemView.findViewById(R.id.imageView);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        GridItem gridItem = gridItemList.get(position);

        // Load image using Picasso
        // Load image using Glide
        Glide.with(context)
                .load(gridItem.ImageUri)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.icon_eye_on)
                        .error(R.drawable.icon_error)
                        .centerCrop())
                .into(holder.imageView);


        return itemView;
    }
    private static class ViewHolder {
        ImageView imageView;
    }
}

