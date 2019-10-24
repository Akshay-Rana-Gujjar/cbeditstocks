package com.buntysinghediting.Cb_edit_stocks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class _1stRecyclerViewAdapter extends RecyclerView.Adapter<_1stRecyclerViewAdapter._1stRecyclerViewHolder> {


    Context context;

    List<FirstActivityNew._1stRecyclerViewModel> recyclerViewModelList;

    public _1stRecyclerViewAdapter(Context context, List<FirstActivityNew._1stRecyclerViewModel> recyclerViewModelList) {
        this.context = context;
        this.recyclerViewModelList = recyclerViewModelList;
    }

    @NonNull
    @Override
    public _1stRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout._1st_recycler_view_item, viewGroup, false);

        return new _1stRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull _1stRecyclerViewHolder stRecyclerViewHolder, final int i) {

        stRecyclerViewHolder.fontAwesomeIcon.setText(Html.fromHtml(recyclerViewModelList.get(i).getIconCode()));

        stRecyclerViewHolder.iconCaption.setText(recyclerViewModelList.get(i).getIconCaption());
        stRecyclerViewHolder.gradientBackground.setBackgroundResource(recyclerViewModelList.get(i).getGradientDrawable());


        stRecyclerViewHolder.gradientBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (i) {
                    case 0:
                        openInstaActivity("background", "most_downloaded");
                        break;
                    case 1:
                        openInstaActivity("background", "most_liked");
                        break;
                    case 2:
                        Intent intent = new Intent(context, SecondActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("buttonIndex", 0);
                        context.startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(context, SecondActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("buttonIndex", 1);
                        context.startActivity(intent);
                        break;
                    case 4:

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://search?q=pub:"+context.getResources().getString(R.string.publisher_id)));
                        context.startActivity(intent);

                        break;
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return recyclerViewModelList.size();
    }

    class _1stRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView iconCaption;
        FontAwesome fontAwesomeIcon;

        LinearLayout gradientBackground;

        public _1stRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            iconCaption = itemView.findViewById(R.id.iconCaption);
            fontAwesomeIcon = itemView.findViewById(R.id.fontAwesomeIcon);
            gradientBackground = itemView.findViewById(R.id.gradient_background);


        }
    }


    void openInstaActivity(String category, String imageFilter) {
        Intent intent = new Intent(context, InstaGridView.class);
        intent.putExtra("category", category);
        intent.putExtra("imageFilter", imageFilter);
        context.startActivity(intent);

    }


}
