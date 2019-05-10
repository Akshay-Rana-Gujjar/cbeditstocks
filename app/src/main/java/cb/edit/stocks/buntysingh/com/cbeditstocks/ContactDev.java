package cb.edit.stocks.buntysingh.com.cbeditstocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ContactDev extends OverrideActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contact_dev);
        super.onCreate(savedInstanceState);

        String dev_img = "https://drive.google.com/uc?export=view&id=1380qmmgs7s1Vhk85QV73RHpmLry9_LOY";
        ImageView profilepic = findViewById(R.id.profilepic);

        Picasso.with(ContactDev.this).load(dev_img).placeholder(R.drawable.hire_logo).error(R.drawable.hire_logo).into(profilepic);





    }

    public void email(View view) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"iamakshayranagujjar@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "New Order - "+getResources().getString(R.string.app_name));
        i.putExtra(Intent.EXTRA_TEXT   , "Hello Akshay, I want to hire you for my new awesome project.  ");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactDev.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }


    }
}
