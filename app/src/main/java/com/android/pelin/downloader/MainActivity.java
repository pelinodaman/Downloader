package com.android.pelin.downloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.pelin.downloader.Objects.Video;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int pBarType = 0;

    public ArrayList<String> videoPool = null;

    public ArrayAdapter<Video> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv = (ListView) findViewById(R.id.listView);

        videoPool = new ArrayList<String>();

        fillVideoPool();

        ArrayList<Video> videoList = new ArrayList<Video>();

        adapter = new ArrayAdapter<Video>(getBaseContext(),R.layout.layout_list_item,R.id.textView, videoList);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemPosition = i;
                Video vid = (Video) adapterView.getItemAtPosition(itemPosition);


                if(vid.isDownloaded())
                {
                    openVideo(vid);
                }
                else
                {

                    if(vid.getPercentage() ==0)
                    {
                        downloadVideo(vid);
                    }

                    Snackbar.make(view, "İndiriliyor...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });
        //adapter.add("another row");
        adapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoPool.size()>0)
                {
                    addNewVideo(adapter);
                }
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });





    }



    private void fillVideoPool() { // filling video pool
        String[] videos = {
                "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4",
                "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_2mb.mp4",
                "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_5mb.mp4",
                "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_10mb.mp4",
                "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_20mb.mp4"
        };
        videoPool.addAll(Arrays.asList(videos));

    }

    private void openVideo(Video vid) { // starts new activity for downloaded video
        Intent intent = new Intent(getBaseContext(), VideoPlayer.class);
        intent.putExtra("videoPath",vid.getPath());
        startActivity(intent);
    }

    public void addNewVideo(ArrayAdapter adapter){ // adding new video to list
        Random r = new Random();
        int randomIndex = r.nextInt(videoPool.size()) ;

        Video video = new Video(videoPool.get(randomIndex));
        videoPool.remove(randomIndex);

        adapter.add(video);

    }

    public void downloadVideo(Video video){
        new DownloadFileFromURL().execute(video);
    }


    class DownloadFileFromURL extends AsyncTask<Video,Video,Video> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("VideoDownloader","Download ");
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected Video doInBackground(Video... params)  {
            int count;
            Video video = params[0];
            try {

                URL url = new URL(video.getUrl());

                Log.e("SASASA",url.toString());
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();


                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);


                File sp = Environment.getExternalStorageDirectory();

                File newFile = new File(sp, video.getName());

                video.setPath(newFile.getAbsolutePath());

                byte data[] = new byte[1024];
                OutputStream output = new FileOutputStream( newFile);

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    Log.e("Error: ", "TOTAL " + total);

                    video.setPercentage((int) ((total * 100) / lenghtOfFile));
                    publishProgress(video);
                    // writing data to file

                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                //Log.e("Error: ", e.getMessage());
                e.printStackTrace();

                Log.e("SASASA" , e.getMessage());

                //Log.v("SASASA" , e.);
            }

            return video;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(Video... videos) {

            //Video
            // setting progress percentage
            //pDialog.setProgress(Integer.parseInt(progress[0]));
           Log.e("SASASA",videos[0].getPercentage()+"");
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(Video video) {

            video.setDownloaded(true);
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);

        }

    }
}
