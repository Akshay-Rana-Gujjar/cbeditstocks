package cb.edits.stocks.myapps.com.cbeditstocks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.*;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SecondRecyclerAdapter extends RecyclerView.Adapter<SecondRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SecondActivity.cardData> arrayList;
    private int ADS_TYPE= 1,CONTENT_TYPE=0;
    String TAG = "+SecondRecyclerAdapter+";

    public SecondRecyclerAdapter(Context c, ArrayList<SecondActivity.cardData> list){

        context = c;
        arrayList = list;
        Log.d(TAG,"In the constructor");

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.d(TAG,"In the onCreateViewHolder");
        View view;
        if(viewType == ADS_TYPE){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.banner_ads, viewGroup, false);

            String banner_ad_placement_id = context.getString(R.string.banner_1_placement_id_second_activity);
            // check if banner ad placement id is set or not
            if(!banner_ad_placement_id.equalsIgnoreCase("null")){
                LinearLayout adContainer = view.findViewById(R.id.adContainerSecondActivity);
                AdView adView;
                adView = new AdView(context, banner_ad_placement_id, AdSize.BANNER_HEIGHT_50);

                adContainer.removeAllViews();

                // Add the ad view to your activity layout
                adContainer.addView(adView);

                // Request an ad
                adView.loadAd();
            }

        }

        else
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.second_list_item,viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if(arrayList.get(i) != null) {
            final int position = i;
            Picasso.with(context).load(arrayList.get(i).getImageURL()).placeholder(R.drawable.placeholder).resize(200, 200).error(R.drawable.android_logo)
                    .centerCrop().into(viewHolder.imageView);


            viewHolder.textView.setText(arrayList.get(i).getImageCategory());

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, ThirdActivity.class);
                    intent.putExtra("category", arrayList.get(position).getImageCategory());

                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {

        Log.d(TAG,"In the getItemViewType");


        if(arrayList.get(position) == null){
            return ADS_TYPE;
        }

        return CONTENT_TYPE;
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivImage);
            textView = itemView.findViewById(R.id.tvTitle);
            cardView = itemView.findViewById(R.id.cardview);

        }
    }
}
