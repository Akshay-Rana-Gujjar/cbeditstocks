package cb.edit.stocks.buntysingh.com.cbeditstocks;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static cb.edit.stocks.buntysingh.com.cbeditstocks.SecondActivity.SERVER_IP;

public class ForthActivity extends AppCompatActivity {

    ImageView imageView;

    TextView likes, downloads;

    String imageURL;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forth);

        imageView = findViewById(R.id.largeImage);

        likes = findViewById(R.id.likeTextVew);

        downloads = findViewById(R.id.downloadTextView);

        AndroidNetworking.initialize(this);

       String apiURL = SERVER_IP+"/api/v1/image.php";

       final String image_id = getIntent().getStringExtra("image_id");

        AndroidNetworking.get(apiURL)
        .addQueryParameter("img_id", image_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response

                        try {

                            imageURL = SERVER_IP+response.getJSONObject(0).getString("image_url");
                            Picasso.get()
                                .load(imageURL)
                                .placeholder(R.drawable.placeholder)
                                .into(imageView);

                            likes.setText(String.format("%s Likes", response.getJSONObject(0).getString("likes_format")));
                            downloads.setText(String.format("%s Downloads", response.getJSONObject(0).getString("downloads_format")));
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

    }

    public void downloadImage(View view) {
        verifyPermissions();

        String photoFileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);



        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+getString(R.string.app_name)+"/");

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("App", "failed to create directory");

            }
        }

        String foldername = "/"+getString(R.string.app_name);

//        imageDownload(this, imageURL);
        Toast.makeText(this, dir+"", Toast.LENGTH_SHORT).show();

        startDownload(imageURL , foldername, photoFileName);

    }

    public void verifyPermissions()
    {

        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionExternalMemory != PackageManager.PERMISSION_GRANTED)
        {

            String[] STORAGE_PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(this,STORAGE_PERMISSIONS,1);
        }
    }

    public static void imageDownload(Context ctx, String url){
        Picasso.get()
                .load(url)
                .into(getTarget(url, ctx.getString(R.string.app_name)));
    }

    //target to save
    private static Target getTarget(final String url, final String appName){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String photoFileName = url.substring(url.lastIndexOf('/') + 1);

                        String dir = Environment.getExternalStorageDirectory().getPath() + "/"+appName+"/";



                        File file = new File(dir + photoFileName);
                        try {

                            new File(dir).mkdir();

                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }


    void startDownload(String downloadPath, String folderName, String fileName) {
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle("Downloading a file"); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(folderName, fileName);  // Storage directory path
        ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
    }




}
