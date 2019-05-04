package cb.edit.stocks.buntysingh.com.cbeditstocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    ArrayList<cardData> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        arrayList = new ArrayList<>();

        String[] imCt = {
          "Vijay Mahar VM","Bike Background", "HD Background", "Car Background", "Swappy Background"
        };

        int [] imurl = {
                R.drawable.img0,
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
        };

        for (int i =0; i < imCt.length;i++){
            arrayList.add(new cardData(imCt[i], imurl[i]));
        }




    }



    public class cardData{
        String imageCategory;
        int imageURL;


        public cardData(String imgCat, int imgUrl){

            imageCategory = imgCat;
            imageURL = imgUrl;

        }


        public String getImageCategory() {
            return imageCategory;
        }

        public int getImageURL() {
            return imageURL;
        }
    }
}
