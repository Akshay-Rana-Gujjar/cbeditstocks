package com.buntysinghediting.Cb_edit_stocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstActivityNew extends AppCompatActivity {

    CarouselView carouselView;

    public static final String CATEGORY_API_URL = "http://144.202.12.33/api/v1/category.php";
    private static final String LOAD_CATEGORY_LIMIT = "6";
    public static final String PNG_TABEL = "pngcategory";
    public static final String BACKGROUND_TABEL = "category";

    String[] headerImages = {
            "https://s19.postimg.cc/qzj5uncgj/slide1.jpg"
            , "https://s19.postimg.cc/lmubh3h0j/slide2.jpg"
            , "https://s19.postimg.cc/99hh9lr5v/slide3.jpg"
            , "https://s19.postimg.cc/nenabzsnn/slide4.jpg"
    };


    String[] imageUrl = {
            "http://144.202.12.33/img/stock/Nature-Backgrounds-01.jpg"
            , "http://144.202.12.33/img/stock/Car-Stocks.jpg"
            , "http://144.202.12.33/img/stock/4K-Backgrounds-1.jpg"
            , "http://144.202.12.33/img/stock/Food-Backgrounds.jpg"
            , "http://144.202.12.33/img/stock/Abstract-Backgrounds-02.jpg"
    };


    String[] _4thRecyclerImageUrl = {

            "http://144.202.12.33/img/pngstock/Smoke-PNG-01.jpg"
            , "http://144.202.12.33/img/pngstock/Cars-Png.jpg"
            , "http://144.202.12.33/img/pngstock/Crackers-PNG.jpg"
            , "http://144.202.12.33/img/pngstock/umbrella-png-01.jpg"
            , "http://144.202.12.33/img/pngstock/Guitar-PNG-01.jpg"
    };

    String[] iconCheatCode = {
              "&#xf201;"
            , "&#xf004;"
            , "&#xf03e;"
            , "&#xf53f;"
            , "&#xf141;"
    };

    String[] iconCaption = {
            "Trending"
            , "Most Liked"
            , "Background"
            , "PNGs"
            , "More Apps"

    };

    int[] iconBackgroundGradient = {
            R.drawable.gradient_1st_recycler_view_item
            , R.drawable.gradient_2_1st_recycler_view
            , R.drawable.gradient_home_botton_elements
            , R.drawable.gradient_1st_recycler_view_item
            , R.drawable.gradient_2_1st_recycler_view
    };

    private final String TAG = FirstActivityNew.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View contentView = inflater.inflate(R.layout.activity_first_new, null, false);
//        contentContainer.addView(contentView);

        setContentView(R.layout.activity_first_new);

        carouselView = findViewById(R.id.carouselView);

        carouselView.setPageCount(headerImages.length);
        final RecyclerView horizontalRecyclerView = findViewById(R.id._3rdRecyclerView);


        AndroidNetworking.initialize(this);

        AndroidNetworking.get(CATEGORY_API_URL)
                .addQueryParameter("l", LOAD_CATEGORY_LIMIT)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<HorizontalScrollModel> horizontalScrollModelList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            String bigCaption = "";

                            String imageUrl = "", id = "";

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                bigCaption = jsonObject.getString("category");
                                imageUrl = jsonObject.getString("image");

                                imageUrl = "http://144.202.12.33/img/stock/" + imageUrl;


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(FirstActivityNew.this, "Error while looping in category array!", Toast.LENGTH_SHORT).show();
                            }

                            horizontalScrollModelList
                                    .add(
                                            new HorizontalScrollModel(
                                                    bigCaption
                                                    , "CATEGORY WISE BEST " + bigCaption.toUpperCase() + " STOCKS"
                                                    , imageUrl
                                                    , id)
                                    );
                        }

                        HorizontalScrollAdapter horizontalScrollAdapter = new HorizontalScrollAdapter(FirstActivityNew.this, horizontalScrollModelList);
                        horizontalRecyclerView.setAdapter(horizontalScrollAdapter);
                        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(FirstActivityNew.this, LinearLayoutManager.HORIZONTAL, false));

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(FirstActivityNew.this, "Error Loading Categories!", Toast.LENGTH_SHORT).show();

                    }
                });

        createPngRecyclerView();


        LinearLayout backgroundContainer = findViewById(R.id.backgroundContainer);

        backgroundContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivityNew.this, SecondActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("buttonIndex", 0);
                startActivity(intent);
            }
        });


        findViewById(R.id.pngContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FirstActivityNew.this, SecondActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("buttonIndex", 1);
                startActivity(intent);

            }
        });


        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {

                Picasso.Priority priority = Picasso.Priority.NORMAL;
                if (position < 1) {
                    priority = Picasso.Priority.HIGH;
                }

                Picasso
                        .with(getApplicationContext())
                        .load(headerImages[position])
                        .priority(priority)
                        .into(imageView);
            }
        });


        RecyclerView _1stRecyclerView = findViewById(R.id._1stRecyclerView);


        _1stRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<_1stRecyclerViewModel> _1stRecyclerViewList = new ArrayList<>();

        for (int i = 0; i < iconCaption.length; i++) {

            _1stRecyclerViewList.add(new _1stRecyclerViewModel(iconCheatCode[i], iconCaption[i], iconBackgroundGradient[i]));

        }

        _1stRecyclerViewAdapter stRecyclerViewAdapter = new _1stRecyclerViewAdapter(this, _1stRecyclerViewList);

        _1stRecyclerView.setAdapter(stRecyclerViewAdapter);



//        startActivity(new Intent(this, InstaGridView.class).putExtra("category","Dark Toned"));

        createBottomSlider(BACKGROUND_TABEL);
        createBottomSlider(PNG_TABEL);
        loadNativeAd();

    }


    class HorizontalScrollModel {
        String bigCaption, smallCaption, imageUrl, id;

        public HorizontalScrollModel(String bigCaption, String smallCaption, String imageUrl, String id) {
            this.bigCaption = bigCaption;
            this.smallCaption = smallCaption;
            this.imageUrl = imageUrl;
            this.id = id;
        }

        public String getBigCaption() {
            return bigCaption;
        }

        public String getSmallCaption() {
            return smallCaption;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getId() {
            return id;
        }
    }

    static class BottomRecyclerViewModel {
        String btnText;

        public BottomRecyclerViewModel(String btnText) {
            this.btnText = btnText;
        }

        public String getBtnText() {
            return btnText;
        }
    }

    class _1stRecyclerViewModel {
        String iconCode, iconCaption;
        int gradientDrawable;

        public _1stRecyclerViewModel(String iconCode, String iconCaption, int gradientDrawable) {
            this.iconCode = iconCode;
            this.iconCaption = iconCaption;
            this.gradientDrawable = gradientDrawable;
        }

        public String getIconCode() {
            return iconCode;
        }

        public String getIconCaption() {
            return iconCaption;
        }

        public int getGradientDrawable() {
            return gradientDrawable;
        }
    }


    void createBottomSlider(String tablename) {
        RecyclerView bottomRecyclerView = findViewById(R.id.bottomRecyclerView1);
        final List<BottomRecyclerViewModel> bottomRecyclerViewModelList = new ArrayList<>();

        if (tablename.equals(PNG_TABEL))
            bottomRecyclerView = findViewById(R.id.bottomRecyclerView2);


        final RecyclerView finalBottomRecyclerView = bottomRecyclerView;
        AndroidNetworking.get(CATEGORY_API_URL)
                .addQueryParameter("t", tablename)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String btnText = jsonObject.getString("category");

                                bottomRecyclerViewModelList.add(new BottomRecyclerViewModel(btnText));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        BottomRecyclerViewAdapter bottomRecyclerViewAdapter = new BottomRecyclerViewAdapter(FirstActivityNew.this, bottomRecyclerViewModelList);

                        finalBottomRecyclerView.setAdapter(bottomRecyclerViewAdapter);

                        finalBottomRecyclerView.setLayoutManager(new LinearLayoutManager(FirstActivityNew.this, LinearLayoutManager.HORIZONTAL, false));

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }


    void createPngRecyclerView() {

        final RecyclerView horizontalRecyclerView2 = findViewById(R.id._4thRecyclerView);
        final List<HorizontalScrollModel> horizontalScrollModelList2 = new ArrayList<>();

        AndroidNetworking.get(CATEGORY_API_URL)
                .addQueryParameter("t", "pngcategory")
                .addQueryParameter("l", LOAD_CATEGORY_LIMIT)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < imageUrl.length; i++) {
                            String bigCaption = "", imageUrl = "", id = "";

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                bigCaption = jsonObject.getString("category");
                                imageUrl = jsonObject.getString("image");
                                imageUrl = "http://144.202.12.33/img/pngstock/" + imageUrl;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            horizontalScrollModelList2
                                    .add(
                                            new HorizontalScrollModel(bigCaption
                                                    , "CATEGORY WISE BEST  STOCKS "
                                                    , imageUrl, id)
                                    );

                        }


                        HorizontalScrollAdapter horizontalScrollAdapter2 = new HorizontalScrollAdapter(FirstActivityNew.this, horizontalScrollModelList2);
                        horizontalRecyclerView2.setAdapter(horizontalScrollAdapter2);
                        horizontalRecyclerView2.setLayoutManager(new LinearLayoutManager(FirstActivityNew.this, LinearLayoutManager.HORIZONTAL, false));


                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }

    void loadNativeAd() {
        final NativeAd nativeAd;


        nativeAd = new NativeAd(this, "YOUR_PLACEMENT_ID");

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

                Log.d(TAG, "Native ad is loaded and ready to be displayed!");

                if (nativeAd == null || nativeAd != ad) {
                    return;
                }

                inflateAd(nativeAd);

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });


        nativeAd.loadAd();
    }

    void inflateAd(NativeAd nativeAd) {
        NativeAdLayout nativeAdLayout;
        LinearLayout adView;

        nativeAd.unregisterView();


        nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(FirstActivityNew.this);
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(FirstActivityNew.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);

    }


}
