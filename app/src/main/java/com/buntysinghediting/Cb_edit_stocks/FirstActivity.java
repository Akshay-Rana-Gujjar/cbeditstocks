package com.buntysinghediting.Cb_edit_stocks;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import static com.buntysinghediting.Cb_edit_stocks.OverrideApp.SERVER_IP;

import com.facebook.ads.*;


public class FirstActivity extends NavigationActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String[] buttonData = {
            "BACKGROUNDS",
            "NEW PNGS",
            "HD BACKGROUNDS",
            "HD PNGS"
    };

    Dialog dialog;
    private AdView adView;
    private AdView adView2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_first);
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_first, null, false);

        contentContainer.addView(contentView);

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayout ads1Container = findViewById(R.id.ads1container);
        String banner_ad_placement_id_1 = getString(R.string.banner_1_placement_id_first_activity);

        // check if you set banner ad placement id in string.xml (in values folder)
        if(!banner_ad_placement_id_1.equalsIgnoreCase("null")){
            adView = new AdView(this, banner_ad_placement_id_1, AdSize.BANNER_HEIGHT_50);

            // Add the ad view to your activity layout
            ads1Container.addView(adView);

            // Request an ad
            adView.loadAd();
        }


        LinearLayout ads2Container = findViewById(R.id.ads2container);
        String banner_ad_placement_id_2 = getString(R.string.banner_2_placement_id_first_activity);

        // check if you set banner ad placement id in string.xml (in values folder)
        if(!banner_ad_placement_id_2.equalsIgnoreCase("null")){
            adView2 = new AdView(this, banner_ad_placement_id_2, AdSize.BANNER_HEIGHT_50);

            // Add the ad view to your activity layout
            ads2Container.addView(adView2);

            // Request an ad
            adView2.loadAd();
        }





        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(getApplicationContext(), buttonData);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        AndroidNetworking.initialize(getApplicationContext());

        findViewById(R.id.inAppCardView).setOnClickListener(new View.OnClickListener() {






            @Override
            public void onClick(View v) {


                dialog = new Dialog(FirstActivity.this);
                ProgressBar progressBar = new ProgressBar(FirstActivity.this,null,android.R.attr.progressBarStyleLarge);
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                dialog.addContentView(progressBar, params);
                dialog.setCancelable(false);
                dialog.show();

                String inAppURL = SERVER_IP+"/inapppromotion";

                AndroidNetworking.get(inAppURL)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {


                        String appPackageName=getPackageName();
                        dialog.dismiss();;

                        try {
                            appPackageName = response.getJSONObject(0).getString("uri");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });



            }
        });

    }

}
