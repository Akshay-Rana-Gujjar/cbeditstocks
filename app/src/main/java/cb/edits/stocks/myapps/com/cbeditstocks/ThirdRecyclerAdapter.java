package cb.edits.stocks.myapps.com.cbeditstocks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ThirdRecyclerAdapter extends RecyclerView.Adapter<ThirdRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ThirdActivity.ThirdActivityData> list;
    private int ADS_TYPE= 1,CONTENT_TYPE=0;


    public ThirdRecyclerAdapter(Context context, ArrayList<ThirdActivity.ThirdActivityData> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ThirdRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;

        if(viewType == ADS_TYPE){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.banner_ads, viewGroup, false);

        }else{

            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.third_list_item, viewGroup, false);
        }




        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThirdRecyclerAdapter.ViewHolder viewHolder, int i) {



        if(list.get(i) != null){

            final int position = i;

            Picasso.with(context).load(list.get(i).getImageURL())
                    .placeholder(R.drawable.placeholder).into(viewHolder.cardImageView);

            viewHolder.viewFullImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ForthActivity.class);
                    intent.putExtra("image_id", list.get(position).getImageId());
                    context.startActivity(intent);
                }
            });

            viewHolder.cardImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ForthActivity.class);
                    intent.putExtra("image_id", list.get(position).getImageId());
                    context.startActivity(intent);
                }
            });
        }


    }


    @Override
    public int getItemViewType(int position) {

        if(list.get(position) == null){
            return ADS_TYPE;
        }

        return CONTENT_TYPE;
    }

    @Override
    public int getItemCount() {


        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button viewFullImageButton;
        ImageView cardImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewFullImageButton = itemView.findViewById(R.id.thirdViewFullButton);
            cardImageView = itemView.findViewById(R.id.thirdCardImage);

        }
    }
}
