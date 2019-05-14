package cb.edits.stocks.myapps.com.cbeditstocks;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static cb.edits.stocks.myapps.com.cbeditstocks.SecondActivity.SERVER_IP;

public class ThirdActivity extends NavigationActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_third, null, false);

        contentContainer.addView(contentView);


        recyclerView = findViewById(R.id.recyclerView3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        AndroidNetworking.initialize(this);

        String category = getIntent().getStringExtra("category");
        String imageByCategoryURL = SERVER_IP +"/api/v1/images.php";


        AndroidNetworking.get(imageByCategoryURL)
        .addQueryParameter("category", category)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response

                        ArrayList<ThirdActivityData> list = new ArrayList<>();


                        for(int i=0; i < response.length();i++){


                            try {
                                list.add(new ThirdActivityData(
                                        SERVER_IP+"/"+response.getJSONObject(i).getString("image_url"),response.getJSONObject(i).getString("id")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        RecyclerView.Adapter adapter = new ThirdRecyclerAdapter(ThirdActivity.this, list);

                        recyclerView.setAdapter(adapter);





                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
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
}
