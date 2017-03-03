package com.sanginfo.placefinder.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanginfo.placefinder.R;
import com.sanginfo.placefinder.utils.CommonMethods;
import com.sanginfo.placesapi.Place;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by Sang.24 on 3/3/2017.
 */

public class PlaceListAdapter extends ArrayAdapter<Place> {
    private List<Place> placeList;
    private Context mContext;
    private static final String TAG = "PlaceListAdapter";

    public PlaceListAdapter(Context context, int resource, List<Place> placeList) {
        super(context, resource, placeList);
        this.mContext = context;
        this.placeList = placeList;
    }

    @Override
    public Place getItem(int position) {
        return this.placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return this.placeList.size();
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        String url = "";
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.place_list_item_layout, null);
            holder = new ViewHolder();
            holder.placeImage = (ImageView) view.findViewById(R.id.place_image);
            holder.placeHeader = (TextView) view.findViewById(R.id.place_header);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        try {
            Place place = placeList.get(position);
            holder.placeHeader.setText(place.getName());
            if (place.getLargeURL(CommonMethods.getPlacesAPIKey(mContext)) != null && !place.getLargeURL(CommonMethods.getPlacesAPIKey(mContext)).isEmpty()) {
                url = place.getLargeURL(CommonMethods.getPlacesAPIKey(mContext));
                Picasso.with(mContext)
                        .load(url)
                        .into(new Target() {
                                  @Override
                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                      holder.placeImage.setImageBitmap(bitmap);
                                  }

                                  @Override
                                  public void onBitmapFailed(Drawable errorDrawable) {
                                      holder.placeImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bar_background));

                                  }

                                  @Override
                                  public void onPrepareLoad(Drawable placeHolderDrawable) {
                                      holder.placeImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bar_background));
                                  }
                              }
                        );
            } else {
                Picasso.with(mContext).load("http:").placeholder(mContext.getResources().getDrawable(R.drawable.bar_background)).error(mContext.getResources().getDrawable(R.drawable.bar_background)).into(holder.placeImage);
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return view;
    }

    public static class ViewHolder {
        public ImageView placeImage;
        public TextView placeHeader;
    }
}
