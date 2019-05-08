package cb.edit.stocks.buntysingh.com.cbeditstocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SecondActivity extends OverrideActivity {

    ArrayList<cardData> arrayList;
    RecyclerView recyclerView;
    public final static String SERVER_IP = "http://155.138.247.166";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        AndroidNetworking.initialize(this);



        String categoryUrl = SERVER_IP +"/api/v1/category.php";

        final String imageEndpoint = SERVER_IP +"/img/stock/";
        arrayList = new ArrayList<>();


        AndroidNetworking.get(categoryUrl)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response

                        for (int i =0; i < response.length();i++){

                            try {
                                arrayList.add(new cardData(response.getJSONObject(i).getString("category"), imageEndpoint+response.getJSONObject(i).getString("image")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        recyclerView = findViewById(R.id.recyclerView2);

                        GridLayoutManager layoutManager = new GridLayoutManager(SecondActivity.this, 2);
                        recyclerView.setLayoutManager(layoutManager);

                        RecyclerView.Adapter adapter = new SecondRecyclerAdapter(SecondActivity.this, arrayList);

                        recyclerView.setAdapter(adapter);

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });








    }



    public class cardData{
        String imageCategory;
        String imageURL;


        public cardData(String imgCat, String imgUrl){

            imageCategory = imgCat;
            imageURL = imgUrl;

        }


        public String getImageCategory() {
            return imageCategory;
        }

        public String getImageURL() {
            return imageURL;
        }
    }
}
