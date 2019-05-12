package cb.edit.stocks.buntysingh.com.cbeditstocks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;

public class FirstActivity extends NavigationActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String[] buttonData = {
            "Backgrounds",
            "New PNGs",
            "HD Backgrounds",
            "HD PNGs"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_first);
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_first, null, false);

        contentContainer.addView(contentView);

        recyclerView = findViewById(R.id.recyclerView);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(getApplicationContext(), buttonData);
        recyclerView.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Instance ID", FirebaseInstanceId.getInstance().getId());
            }
        }).start();

    }

}
