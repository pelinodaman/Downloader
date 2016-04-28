package com.android.pelin.downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pelin.downloader.Objects.Video;


import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by pelin on 28.04.2016.
 */
public class VideoAdapter  extends ArrayAdapter<Video> {


    public VideoAdapter(Context context, List<Video> objects) {
        super(context, R.layout.layout_list_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View customView = inflater.inflate(R.layout.layout_list_item, parent, false);

        Video video= getItem(position);

        TextView textView = (TextView) customView.findViewById(R.id.videoNameTV);
        textView.setText(video.getName());


        ImageView imageView = (ImageView) customView.findViewById(R.id.videoImageIV);
        Bitmap imageBitmap = BitmapFactory.decodeFile(video.getImagePath());
        imageView.setImageBitmap(imageBitmap);

        return customView;


    }

    @Override
    public void add(Video object) {
        super.add(object);
        notifyDataSetChanged();
    }
}
