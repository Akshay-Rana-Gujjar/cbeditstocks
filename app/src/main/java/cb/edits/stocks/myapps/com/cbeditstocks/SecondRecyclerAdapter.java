package cb.edits.stocks.myapps.com.cbeditstocks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SecondRecyclerAdapter extends RecyclerView.Adapter<SecondRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SecondActivity.cardData> arrayList;

    public SecondRecyclerAdapter(Context c, ArrayList<SecondActivity.cardData> list){

        context = c;
        arrayList = list;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.second_list_item,viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

//        viewHolder.imageView.setImageResource();
        final int position = i;
        Picasso.with(context).load(arrayList.get(i).getImageURL()).placeholder(R.drawable.placeholder).resize(200, 200)
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
