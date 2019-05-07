package cb.edit.stocks.buntysingh.com.cbeditstocks;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static cb.edit.stocks.buntysingh.com.cbeditstocks.SecondActivity.SERVER_IP;

public class ForthActivity extends AppCompatActivity {

    ImageView imageView;

    TextView likes, downloads;

    String imageURL;
    String image_id;

    LikeButton like_button;

    String likeCount, downloadCount;

    long downloadId;

    BroadcastReceiver onDownloadComplete;

    RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forth);

        imageView = findViewById(R.id.largeImage);

        likes = findViewById(R.id.likeTextVew);

        like_button = findViewById(R.id.like_button);

        downloads = findViewById(R.id.downloadTextView);

        AndroidNetworking.initialize(this);

       String apiURL = SERVER_IP+"/api/v1/image.php";

       image_id = getIntent().getStringExtra("image_id");

       String similarApiURL = SERVER_IP+"/api/v1/similar.php";

        recyclerView = findViewById(R.id.forthRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        AndroidNetworking.get(apiURL)
        .addQueryParameter("img_id", image_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response

                        try {

                            imageURL = SERVER_IP+"/"+response.getJSONObject(0).getString("thumbnail");
                            Picasso.get()
                                .load(imageURL).fit().centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .into(imageView);

                            likeCount = response.getJSONObject(0).getString("likes_format");

                            likes.setText(String.format("%s Likes", likeCount));

                            downloadCount = response.getJSONObject(0).getString("downloads_format");
                            downloads.setText(String.format("%s Downloads", downloadCount));
                            Toast.makeText(ForthActivity.this, "img_id "+image_id, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });


        AndroidNetworking.get(similarApiURL)
                .addQueryParameter("img_id", image_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response

                        ArrayList<ThirdActivity.ThirdActivityData> list = new ArrayList<>();


                        for(int i=0; i < response.length();i++){


                            try {
                                list.add(new ThirdActivity.ThirdActivityData(
                                        SERVER_IP+"/"+response.getJSONObject(i).getString("image_url"),response.getJSONObject(i).getString("id")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        RecyclerView.Adapter adapter = new ThirdRecyclerAdapter(ForthActivity.this, list);

                        recyclerView.setAdapter(adapter);


                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });



        like_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                increaseDecreaseUtility("/like_count.php", image_id, new ApiCallback() {
                    @Override
                    public void success() {
                        likeCount = (Integer.parseInt(likeCount) + 1) +"";
                        likes.setText(String.format("%s Likes", likeCount));
                    }

                    @Override
                    public void failed() {
                        Toast.makeText(ForthActivity.this, "Like Request Failed!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                increaseDecreaseUtility("/unlike_count.php", image_id, new ApiCallback() {
                    @Override
                    public void success() {
                        likeCount = (Integer.parseInt(likeCount) - 1) +"";
                        likes.setText(String.format("%s Likes", likeCount));
                    }

                    @Override
                    public void failed() {
                        Toast.makeText(ForthActivity.this, "Unlike Request Failed!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Fetching the download id received with the broadcast
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //Checking if the received broadcast is for our enqueued download by matching download id
                if (downloadId == id) {

                    Toast.makeText(ForthActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                    increaseDecreaseUtility("/download_count.php", image_id, new ApiCallback() {
                        @Override
                        public void success() {

                            downloadCount = (Integer.parseInt(downloadCount) + 1)+"";
                            downloads.setText(String.format("%s Downloads", downloadCount));

                        }

                        @Override
                        public void failed() {
                            Toast.makeText(ForthActivity.this, "Download Count Failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };


        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    public void downloadImage(View view) {

        if(!verifyPermissions()){
            return;
        }

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+getString(R.string.app_name)+"/";

        File dir = new File(dirPath);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("App", "failed to create directory");

            }
        }

        String foldername = "/"+getString(R.string.app_name);

//        imageDownload(this, imageURL);
        Toast.makeText(this, dir+"", Toast.LENGTH_SHORT).show();

        startDownload(imageURL , foldername);

//        String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);


//        AndroidNetworking.download(imageURL,dirPath,fileName)
//
//                .setTag("downloadTest")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .setDownloadProgressListener(new DownloadProgressListener() {
//
//
//
//                    @Override
//                    public void onProgress(long bytesDownloaded, long totalBytes) {
//                        // do anything with progress
//
//                        Toast.makeText(ForthActivity.this, (100 * (bytesDownloaded/totalBytes))+"%", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .startDownload(new DownloadListener() {
//                    @Override
//                    public void onDownloadComplete() {
//                        // do anything after completion
//                        Toast.makeText(ForthActivity.this, "Download Completed.", Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//
//
//                });



    }

    public Boolean verifyPermissions()
    {

        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionExternalMemory != PackageManager.PERMISSION_GRANTED)
        {

            String[] STORAGE_PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(this,STORAGE_PERMISSIONS,1);
            return false;
        }

        return true;

    }



    void startDownload(String downloadPath, String folderName) {
        String photoFileName = downloadPath.substring(downloadPath.lastIndexOf('/') + 1);
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle("Downloading a file"); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(folderName, photoFileName);  // Storage directory path
        downloadId = ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(this, "Please grant storage permission to download images.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    public void increaseDecreaseUtility(String EndURL, String img_id, final ApiCallback apiCallback){

        Log.d("increaseDecreaseU", "increaseDecreaseUtility: "+SERVER_IP+EndURL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("img_id", img_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.put(SERVER_IP+EndURL)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // do anything with response
                        Log.d("RESPONSE :: ", response);
                        apiCallback.success();


                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("API CALL ERROR", error.getErrorBody());
                        apiCallback.failed();
                    }
                });
    }

    public interface  ApiCallback{
        void success();
        void failed();
    }


}
