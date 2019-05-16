package cb.edits.stocks.myapps.com.cbeditstocks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static cb.edits.stocks.myapps.com.cbeditstocks.OverrideApp.SERVER_IP;

public class ThirdActivity extends NavigationActivity {

    RecyclerView recyclerView;
    private boolean shouldLoad = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    ProgressBar loading;
    ArrayList<ThirdActivityData> list = new ArrayList<>();
    int page =1;
    String category;
    String imageByCategoryURL;
    RecyclerView.Adapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_third, null, false);

        contentContainer.addView(contentView);


        recyclerView = findViewById(R.id.recyclerView3);
        loading = findViewById(R.id.loading);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        AndroidNetworking.initialize(this);

        category = getIntent().getStringExtra("category");
        imageByCategoryURL = SERVER_IP +"/api/v1/images.php";


        updateList(page++);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int lm, int dy) {

                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (shouldLoad)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading.setVisibility(View.VISIBLE);
                            shouldLoad = false;
                            updateList(page++);
                        }
                    }
                }
            }
        });

    }



    public static class ThirdActivityData{

        String imageURL, imageId;

        public ThirdActivityData(String imageURL, String imageId) {
            this.imageURL = imageURL;
            this.imageId = imageId;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getImageId() {
            return imageId;
        }
    }


    void updateList(int pageNumber){

//        Toast.makeText(this, "pageNumber = "+pageNumber, Toast.LENGTH_SHORT).show();

        AndroidNetworking.get(imageByCategoryURL)
                .addQueryParameter("category", category)
                .addQueryParameter("page",pageNumber+"")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response

                        if(response.length() == 0)
                        Toast.makeText(ThirdActivity.this, "No more Images!!", Toast.LENGTH_SHORT).show();

                        int listSize = list.size();
                        int newListSize= response.length()+listSize;

                        for(int i= listSize, index = 0 ; i < newListSize;i++, index++){
                            try {

                                if(i%4==0){
                                    list.add(null);
                                }

                                list.add(new ThirdActivityData(
                                        SERVER_IP+"/"+response.getJSONObject(index).getString("image_url"),response.getJSONObject(index).getString("id")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(adapter == null){
                            adapter = new ThirdRecyclerAdapter(ThirdActivity.this, list);

                            recyclerView.setAdapter(adapter);

                        }

                        else{
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
