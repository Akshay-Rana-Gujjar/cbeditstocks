package com.buntysinghediting.Cb_edit_stocks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BottomRecyclerViewAdapter extends RecyclerView.Adapter<BottomRecyclerViewAdapter.BottomRecyclerViewHolder> {


    Context context;
    private List<FirstActivityNew.BottomRecyclerViewModel> bottomRecyclerViewModels;

    BottomRecyclerViewAdapter(Context context, List<FirstActivityNew.BottomRecyclerViewModel> bottomRecyclerViewModels) {
        this.context = context;
        this.bottomRecyclerViewModels = bottomRecyclerViewModels;
    }

    @NonNull
    @Override
    public BottomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bottom_gradient_recyclerview_item, viewGroup, false);

        return new BottomRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomRecyclerViewHolder bottomRecyclerViewHolder, final int i) {

         bottomRecyclerViewHolder.btnText.setText(bottomRecyclerViewModels.get(i).getBtnText());

        bottomRecyclerViewHolder.btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InstaGridView.class);
                intent.putExtra("category", bottomRecyclerViewModels.get(i).getBtnText());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bottomRecyclerViewModels.size();
    }

    class BottomRecyclerViewHolder extends  RecyclerView.ViewHolder{


        TextView btnText;

        public BottomRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            btnText = itemView.findViewById(R.id.bottomTextView);
        }
    }
}
