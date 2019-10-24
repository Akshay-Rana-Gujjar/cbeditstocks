package com.buntysinghediting.Cb_edit_stocks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalScrollAdapter extends RecyclerView.Adapter<HorizontalScrollAdapter.HorizontalScrollViewHolder>{


    Context context;
    List<FirstActivityNew.HorizontalScrollModel> horizontalScrollModel;


    public HorizontalScrollAdapter(Context context, List<FirstActivityNew.HorizontalScrollModel> horizontalScrollModel) {
        this.context = context;
        this.horizontalScrollModel = horizontalScrollModel;
    }



    @NonNull
    @Override
    public HorizontalScrollViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_image_with_caption_item, viewGroup,false);


        return new HorizontalScrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalScrollViewHolder horizontalScrollViewHolder, final int i) {

        final int positon = i;

        horizontalScrollViewHolder
            .bigCaption
            .setText(horizontalScrollModel.get(i).getBigCaption());

        horizontalScrollViewHolder
            .smallCaption
            .setText(horizontalScrollModel.get(i).getSmallCaption());

        Picasso.Priority priority = Picasso.Priority.LOW;
        if(i<1)
            priority = Picasso.Priority.HIGH;

        Picasso
            .with(context)
            .load(horizontalScrollModel.get(i).getImageUrl())
                .priority(priority)
            .into(horizontalScrollViewHolder.image);

        horizontalScrollViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InstaGridView.class);
                intent.putExtra("category", horizontalScrollModel.get(i).getBigCaption());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalScrollModel.size();
    }

    class HorizontalScrollViewHolder extends RecyclerView.ViewHolder{
        TextView bigCaption, smallCaption;
        ImageView image;

        public HorizontalScrollViewHolder(@NonNull View itemView) {
            super(itemView);

            bigCaption = itemView.findViewById(R.id.bigCaption);
            smallCaption = itemView.findViewById(R.id.imageSmallCaption);
            image = itemView.findViewById(R.id.horizontalImageView);

        }
    }
}
