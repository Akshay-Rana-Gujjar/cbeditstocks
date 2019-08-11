package cb.edits.stocks.myapps.com.cbeditstocks;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static cb.edits.stocks.myapps.com.cbeditstocks.OverrideApp.SERVER_IP;

public class SecondActivity extends NavigationActivity {

    ArrayList<cardData> arrayList;
    RecyclerView recyclerView;

    String imageEndpoint = SERVER_IP +"/img/stock/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_second, null, false);

        contentContainer.addView(contentView);

        AndroidNetworking.initialize(this);



        String categoryUrl = SERVER_IP +"/api/v1/category.php";


        arrayList = new ArrayList<>();

        ANRequest.GetRequestBuilder getRequestBuilder = AndroidNetworking.get(categoryUrl);

        switch (getIntent().getIntExtra("buttonIndex", 0)){

            case 0:
            default:
                Log.d("SecondActivity","switch default");
                break;
            case 1:
                getRequestBuilder.addQueryParameter("t","pngcategory"); // t is mandatory you can change pngcategory to load different category
                imageEndpoint = SERVER_IP +"/img/pngstock/";

                break;
//                    case 2:
//                        getRequestBuilder.addQueryParameter("t","pngcategory");  <========= uncomment for button 3rd click action or add more case for click
//                        break;
        }

        getRequestBuilder
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response

                        for (int i =0; i < response.length();i++){

                            try {
                                if(i%4==0 && i > 0){
                                    arrayList.add(null);
                                }
                                arrayList.add(new cardData(response.getJSONObject(i).getString("category"), imageEndpoint+response.getJSONObject(i).getString("image")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        recyclerView = findViewById(R.id.recyclerView2);

                        GridLayoutManager layoutManager = new GridLayoutManager(SecondActivity.this, 2);

                        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int i) {

                                if(arrayList.get(i)==null)
                                    return 2;

                                return 1;
                            }
                        });


                        recyclerView.setLayoutManager(layoutManager);

                        RecyclerView.Adapter adapter = new SecondRecyclerAdapter(SecondActivity.this, arrayList);

                        recyclerView.setAdapter(adapter);

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(getApplicationContext(), "Error "+error.getErrorBody(), Toast.LENGTH_SHORT).show();
                        Log.d("\n\nERROR321",""+error.getErrorDetail());
                        Log.d("\n\nERROR321",""+error.getErrorCode());
                        Log.d("\n\nERROR321",""+error.getResponse());
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
