package com.buntysinghediting.Cb_edit_stocks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.buntysinghediting.Cb_edit_stocks.OverrideApp.SERVER_IP;

public class InstaGridView extends NavigationActivity {

    ArrayList<ThirdActivityData> list = new ArrayList<>();
    String currentOpenedTab = "latest";
    String imageByCategoryURL;
    String category;
    RecyclerView.Adapter adapter = null;
    RecyclerView instaGridRecyclerView;
    ProgressBar loading;
    private boolean shouldLoad;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page = 1;
    SpannedGridLayoutManager layoutManager;
    View tabIndicator;
    int spanIndex = 0;
    int nativeAdCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_insta_grid_view, null, false);
        contentContainer.addView(contentView);
//        setContentView(R.layout.activity_insta_grid_view);
        shouldLoad = false;

        final float scale = getResources().getDisplayMetrics().density;
        tabIndicator = new View(this);
        tabIndicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (2 * scale)));
        tabIndicator.setBackground(getResources().getDrawable(R.drawable.gradient));


//        === Tab Functionality START ======

        final LinearLayout latestTabContainer = findViewById(R.id.latestTabContainer);
        final LinearLayout popularTabContainer = findViewById(R.id.popularTabContainer);
        LinearLayout parentTabsContainer = findViewById(R.id.parentTabsContainer);


        tabIndicator = new View(getApplicationContext());
        tabIndicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (2 * scale)));
        tabIndicator.setBackground(getResources().getDrawable(R.drawable.gradient));

        latestTabContainer
                .addView(tabIndicator);

        latestTabContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if((latestTabContainer.getChildCount() > 1 || list.size() < 1))
                    return;
                
                tabIndicator = new View(getApplicationContext());
                tabIndicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (2 * scale)));
                tabIndicator.setBackground(getResources().getDrawable(R.drawable.gradient));

                latestTabContainer
                        .addView(tabIndicator);

                if (popularTabContainer.getChildCount() > 1)
                    popularTabContainer.removeViewAt(popularTabContainer.getChildCount() - 1);

                page = 1;
                currentOpenedTab = "latest";
                SpannedGridLayoutManager layoutManager = (SpannedGridLayoutManager) instaGridRecyclerView.getLayoutManager();
                layoutManager.scrollToPosition(0);
                list.clear();
                updateList(page, currentOpenedTab);

            }
        });
        latestTabContainer.setClickable(false);

        popularTabContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((popularTabContainer.getChildCount() > 1 || list.size() < 1))
                    return;

                tabIndicator = new View(getApplicationContext());
                tabIndicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (2 * scale)));
                tabIndicator.setBackground(getResources().getDrawable(R.drawable.gradient));

                popularTabContainer
                        .addView(tabIndicator);

                if (latestTabContainer.getChildCount() > 1)
                    latestTabContainer.removeViewAt(latestTabContainer.getChildCount() - 1);

                latestTabContainer.setClickable(true);

                page = 1;

                currentOpenedTab = "most_downloaded";
                SpannedGridLayoutManager layoutManager = (SpannedGridLayoutManager) instaGridRecyclerView.getLayoutManager();
                layoutManager.scrollToPosition(0);
                list.clear();
                updateList(page, currentOpenedTab);

            }
        });

//        ==== Tab Functionality END ====

//        == Main RecyclerView START ===

        instaGridRecyclerView = findViewById(R.id.instaGridRecyclerView);
        AndroidNetworking.initialize(this);

//        == Getting category value from previous activity ==
        category = getIntent().getStringExtra("category");

        String imageFilterFromIntent = getIntent().getStringExtra("imageFilter");

        if(imageFilterFromIntent != null && !imageFilterFromIntent.isEmpty()){
            currentOpenedTab = imageFilterFromIntent;
            parentTabsContainer.setVisibility(View.GONE);
        }


        imageByCategoryURL = SERVER_IP + "/api/v1/images.php";

        loading = findViewById(R.id.instaLoading);



        layoutManager = new SpannedGridLayoutManager(
                new SpannedGridLayoutManager.GridSpanLookup() {
                    @Override
                    public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                        // Conditions for 2x2 items
                        if (position % 18 == 1 || position % 18 == 9) {

                            return new SpannedGridLayoutManager.SpanInfo(2, 2);
                        }
                        else {
                            return new SpannedGridLayoutManager.SpanInfo(1, 1);
                        }
                    }
                },
                3, // number of columns
                1f // how big is default item
        );

//        == Calling fn for latest tab ==
        updateList(page, currentOpenedTab);


//        InstaGridAdapter instaGridAdapter = new InstaGridAdapter(this, list);
//        instaGridRecyclerView.setAdapter(instaGridAdapter);

//      == Check if user at end of list and then load more ==
        instaGridRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int lm, int dy) {

                if (dy > 0 && !recyclerView.canScrollVertically(1)) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.getFirstVisibleItemPosition();

                    if (shouldLoad) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading.setVisibility(View.VISIBLE);
                            shouldLoad = false;
                            updateList(page++, currentOpenedTab);
                        }
                    }
                }
            }
        });

    }


    public static class ThirdActivityData {

        String imageURL, imageId;

        ThirdActivityData(String imageURL, String imageId) {
            this.imageURL = imageURL;
            this.imageId = imageId;
        }

        String getImageURL() {
            return imageURL;
        }

        public String getImageId() {
            return imageId;
        }
    }

    void updateList(int pageNumber, String imageFilter) {

        AndroidNetworking.get(imageByCategoryURL)
                .addQueryParameter("category", category)
                .addQueryParameter("page", pageNumber + "")
                .addQueryParameter("type", imageFilter)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response

                        if (response.length() == 0) {
                            shouldLoad = false;
                            loading.setVisibility(View.GONE);
                            Snackbar.make(findViewById(R.id.instaGridLayout), "No more images!", Snackbar.LENGTH_SHORT).show();
                            return;

                        }
                        Toast.makeText(InstaGridView.this, "response.length() "+response.length(), Toast.LENGTH_SHORT).show();
//                      == Get list size before updating ==
                        int listSize = list.size();
                        int newListSize = response.length() + listSize;

                        for (int i = listSize, index = 0; i < newListSize; i++, index++) {
                            try {
//
//                                if(i%19 ==0){
//                                    list.add(null);
//                                }

                                list.add(new ThirdActivityData
                                                (
                                        SERVER_IP + "/" + response.getJSONObject(index).getString("image_url"),
                                                response.getJSONObject(index).getString("id")
                                        )
                                );

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (adapter == null) {
                            adapter = new InstaGridAdapter(InstaGridView.this, list);
                            instaGridRecyclerView.setAdapter(adapter);
                            instaGridRecyclerView.setLayoutManager(layoutManager);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        loading.setVisibility(View.GONE);
                        shouldLoad = true;
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
}
