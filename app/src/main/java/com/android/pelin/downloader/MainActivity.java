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
    public ArrayList<String> imagePool = null;
    ArrayList<Video> videoList = new ArrayList<Video>();
    public VideoAdapter  customAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv = (ListView) findViewById(R.id.listView);

        videoPool = new ArrayList<String>();
        imagePool = new ArrayList<String>();

        fillVideoPool();
        fillImagePool();



        customAdapter = new VideoAdapter(getBaseContext(),videoList);
        lv.setAdapter(customAdapter);

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

        customAdapter.notifyDataSetChanged();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoPool.size()>0)
                {
                    addNewVideo(customAdapter);
                }
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

    private void fillImagePool()
    {
        String[] images = {
                "https://pbs.twimg.com/profile_images/378800000532546226/dbe5f0727b69487016ffd67a6689e75a.jpeg",
                "http://g-ecx.images-amazon.com/images/G/01/img15/pet-products/small-tiles/23695_pets_vertical_store_dogs_small_tile_8._CB312176604_.jpg",
                "http://www.hickerphoto.com/images/1024/endangered-animal-list_5406.jpg",
                "http://cdn.earthporm.com/wp-content/uploads/2014/10/animal-family-portraits-2__880.jpg",
                "https://elifyaaman.files.wordpress.com/2014/08/animal-hd-collection-329357.jpg"
        };
        imagePool.addAll(Arrays.asList(images));
    }

    private void openVideo(Video vid) { // starts new activity for downloaded video
        Intent intent = new Intent(getBaseContext(), VideoPlayer.class);
        intent.putExtra("videoPath",vid.getPath());
        startActivity(intent);
    }

    public void addNewVideo(VideoAdapter adapter){ // adding new video to list
        Random r = new Random();
        int randomIndex = r.nextInt(videoPool.size()) ;

        Video video = new Video(videoPool.get(randomIndex) , imagePool.get(randomIndex));
        videoPool.remove(randomIndex);
        imagePool.remove(randomIndex);

        downloadVideo(video);
        videoList.add(video);
        adapter.notifyDataSetChanged();



    }

    public void downloadVideo(Video video){
        new DownloadImageFromURL().execute(video);
        new DownloadFileFromURL().execute(video);


    }

    public void notifyArrayAdapter()
    {
        customAdapter.notifyDataSetChanged();
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
                //Log.e("LENGTH OF VIDEO" , lenghtOfFile+"")

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
                    //Log.e("Error: ", "TOTAL " + total);

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


    class DownloadImageFromURL extends AsyncTask<Video,Video,Video> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */


        /**
         * Downloading file in background thread
         * */
        @Override
        protected Video doInBackground(Video... params)  {
            int count;
            Video video = params[0];
            try {

                URL url = new URL(video.getImageUrl());

                URLConnection conection = url.openConnection();
                conection.connect();


                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);


                File sp = Environment.getExternalStorageDirectory();

                String imageName = video.getImageUrl().substring(video.getImageUrl().lastIndexOf("/")+1);
                File newFile = new File(sp,imageName );

                video.setImagePath(newFile.getAbsolutePath());

                byte data[] = new byte[1024];
                OutputStream output = new FileOutputStream( newFile);


                while ((count = input.read(data)) != -1) {



                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {

                e.printStackTrace();

                Log.e("ImageDownloadException" , e.getMessage());

            }

            return video;
        }

        @Override
        protected void onPostExecute(Video video) {
            super.onPostExecute(video);

            notifyArrayAdapter();

        }
    }
}
