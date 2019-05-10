package cb.edit.stocks.buntysingh.com.cbeditstocks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static cb.edit.stocks.buntysingh.com.cbeditstocks.SecondActivity.SERVER_IP;

public class ThirdActivity extends OverrideActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_third);
        super.onCreate(savedInstanceState);
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
