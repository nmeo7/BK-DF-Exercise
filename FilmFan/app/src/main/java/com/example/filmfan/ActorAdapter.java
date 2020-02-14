package com.example.filmfan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ActorAdapter extends ArrayAdapter<Actor> {

    private ArrayList<Actor> dataSet;
    Context mContext;
    private final String TAG = "MOVIE_ADAPTER_TAG";

    private static class ViewHolder {
        TextView title1;
        TextView title2;
        TextView releaseDate;
        TextView votesAverage;
        ImageView picture;
        ImageView markFavorite;
    }

    public ActorAdapter(ArrayList<Actor> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Actor dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.title1 = (TextView) convertView.findViewById(R.id.row_title1);
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

        viewHolder.title1.setText(dataModel.getCharacter());
        viewHolder.title2.setText(dataModel.getName());
        viewHolder.picture.setTag(dataModel.getPicture());

        viewHolder.releaseDate.setVisibility(View.GONE);
        viewHolder.markFavorite.setVisibility(View.GONE);
        viewHolder.votesAverage.setVisibility(View.GONE);

        Picasso.get().load(dataModel.getPicture()).into(viewHolder.picture);

        return convertView;
    }
}
