package com.buntysinghediting.Cb_edit_stocks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class InstaGridAdapter extends RecyclerView.Adapter<InstaGridAdapter.InstaGridViewHolder> {

    Context context;
    List<InstaGridView.ThirdActivityData> list;

    public InstaGridAdapter(Context context, List<InstaGridView.ThirdActivityData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstaGridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.insta_grid_item, viewGroup, false);

        return new InstaGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstaGridViewHolder instaGridViewHolder, final int i) {
        ImageView imageView = instaGridViewHolder.instaGridImageView;

        Picasso.Priority priority = Picasso.Priority.NORMAL;
        if(i < 6)
            priority = Picasso.Priority.HIGH;
        Picasso
                .with(context)
                .load(list.get(i).getImageURL())
                .priority(priority)
                .placeholder(R.drawable.placeholder_grey)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ForthActivity.class);
                intent.putExtra("image_id", list.get(i).getImageId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InstaGridViewHolder extends RecyclerView.ViewHolder {

        ImageView instaGridImageView;

        public InstaGridViewHolder(@NonNull View itemView) {
            super(itemView);
            instaGridImageView = itemView.findViewById(R.id.instaGridImageView);
        }
    }
}
