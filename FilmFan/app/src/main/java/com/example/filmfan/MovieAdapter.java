package com.example.filmfan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static com.example.filmfan.DetailsActivity.MOVIE_ID_TOKEN;

public class MovieAdapter extends ArrayAdapter<Movie> implements View.OnClickListener{

    private ArrayList<Movie> dataSet;
    Context mContext;
    private final String TAG = "MOVIE_ADAPTER_TAG";

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView title2;
        TextView releaseDate;
        TextView votesAverage;
        ImageView picture;
        ImageView markFavorite;
    }

    public MovieAdapter(ArrayList<Movie> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position= (Integer) v.getTag();

        switch (v.getId())
        {
            case R.id.row_mark_favorite:
                Snackbar.make(v, "Marking movie favorite", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                Movie m = new Movie( getContext() );
                m.addToFavorites(v, position);
                break;
            case R.id.row_image: case R.id.row_title1: case R.id.row_title2:
            Log.i(TAG, "onClick: " + position);


            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra(DetailsActivity.MOVIE_ID_TOKEN, position);
            getContext().startActivity(intent);

            break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.row_title1);
            viewHolder.title2 = (TextView) convertView.findViewById(R.id.row_title2);
            viewHolder.releaseDate = (TextView) convertView.findViewById(R.id.row_subtitle);
            viewHolder.votesAverage = (TextView) convertView.findViewById(R.id.row_numeric_value);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.row_image);
            viewHolder.markFavorite = (ImageView) convertView.findViewById(R.id.row_mark_favorite);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.title.setText(dataModel.getTitle());
        viewHolder.title2.setText(dataModel.getTitle2());
        viewHolder.releaseDate.setText(dataModel.getReleaseDate());
        viewHolder.votesAverage.setText(dataModel.getVotesAverage());
        viewHolder.picture.setTag(dataModel.getPicture());

        viewHolder.markFavorite.setOnClickListener(this);
        viewHolder.title.setOnClickListener(this);
        viewHolder.picture.setOnClickListener(this);

        viewHolder.markFavorite.setTag( dataModel.getId() );
        viewHolder.title.setTag( dataModel.getId() );
        viewHolder.picture.setTag( dataModel.getId() );

        viewHolder.title2.setVisibility(View.GONE);

        Picasso.get().load(dataModel.getPicture()).into(viewHolder.picture);

        return convertView;
    }
}
