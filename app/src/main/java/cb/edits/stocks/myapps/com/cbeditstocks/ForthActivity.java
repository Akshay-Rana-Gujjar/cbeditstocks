package cb.edits.stocks.myapps.com.cbeditstocks;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static cb.edits.stocks.myapps.com.cbeditstocks.SecondActivity.SERVER_IP;


public class ForthActivity extends OverrideActivity {

    ImageView imageView;

    TextView likes, downloads, downloadStatus;

    String imageURL,HDImageURL;
    String image_id;

    LikeButton like_button;

    String likeCount, downloadCount;

    long downloadId;

    BroadcastReceiver onDownloadComplete;

    RecyclerView recyclerView;

    Button cancelBtn, setAsWallpaperBtn;
    ImageView shareBtn;
    LinearLayout setAsAndShareLayout;
    String fileName, dirPath;
    Dialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forth);
        super.onCreate(savedInstanceState);

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
                            HDImageURL = SERVER_IP+response.getJSONObject(0).getString("image_url");
                            Picasso.with(getApplicationContext())
                                    .load(imageURL)
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
                                if(i%4==0 && i > 0){
                                    list.add(null);
                                }
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

                    increaseDecreaseUtility("/download_count.php", image_id, new ApiCallback() {
                        @Override
                        public void success() {

                            downloadCount = (Integer.parseInt(downloadCount) + 1)+"";
                            downloads.setText(String.format("%s Downloads", downloadCount));
                            cancelBtn.setVisibility(View.GONE);
                            setAsAndShareLayout.setVisibility(View.VISIBLE);
                            dialog.setCancelable(true);
                            downloadStatus.setText(getString(R.string.completed));


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

        dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+getString(R.string.app_name)+"/";

        File dir = new File(dirPath);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("App", "failed to create directory");

            }
        }

        String foldername = "/"+getString(R.string.app_name);

//        imageDownload(this, imageURL);


        startDownload(HDImageURL , foldername);

        fileName = HDImageURL.substring(HDImageURL.lastIndexOf('/') + 1);


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

        dialog = new Dialog(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.dailog);

        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        dialog.setCancelable(false);
        // Set dialog title
        dialog.setTitle("Downloading...");

        cancelBtn = dialog.findViewById(R.id.cancelDownload);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DownloadManager) Objects.requireNonNull(getSystemService(Context.DOWNLOAD_SERVICE))).remove(downloadId);
                dialog.cancel();
            }
        });

        downloadStatus = dialog.findViewById(R.id.downloadStatus);

        setAsWallpaperBtn = dialog.findViewById(R.id.setAsWallpaper);

        setAsWallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT>=24){
                    try{
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                Uri imageUri = Uri.fromFile(new File(dirPath+fileName));
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("mimeType", "image/*");
                getBaseContext().startActivity(Intent.createChooser(intent, "Set as:"));

            }
        });

        shareBtn = dialog.findViewById(R.id.shareImage);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT>=24){
                    try{
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }


                Uri imageUri;
                Intent intent;

                imageUri = Uri.fromFile(new File(dirPath+fileName));

                intent = new Intent(Intent.ACTION_SEND);
//text
                intent.putExtra(Intent.EXTRA_TEXT, "Download Free CB Editing Stocks from here\n https://play.google.com/store/apps/details?id="+getPackageName());
//image
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//type of things
                intent.setType("*/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//sending
                startActivity(intent);
            }
        });

        setAsAndShareLayout = dialog.findViewById(R.id.setAsAndShareLayout);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();

        String photoFileName = downloadPath.substring(downloadPath.lastIndexOf('/') + 1);
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle("Downloading a file"); // Title for notification.
//        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(folderName, photoFileName);  // Storage directory path
        downloadId = ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);
                Cursor cursor = ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).query(q);
                cursor.moveToFirst();
                int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                cursor.close();
                final double dl_progress = (bytesDownloaded * 1f / bytesTotal) * 100;
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        progressBar.setProgress((int) dl_progress);
                    }
                });
            }
        }, 0, 10);


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
                        Log.d("CALL ERROR", error.getErrorBody());
                        apiCallback.failed();
                    }
                });
    }

    public interface  ApiCallback{
        void success();
        void failed();
    }




}
