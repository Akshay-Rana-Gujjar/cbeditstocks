package cb.edit.stocks.buntysingh.com.cbeditstocks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyRecycleriewHolder> {
    private String[] buttonNames;
    Context context;


    public RecyclerAdapter (Context appContext,String[] buttonNamesArray){
        buttonNames = buttonNamesArray;
        context = appContext;

    }

    @NonNull
    @Override
    public MyRecycleriewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_activity_list_item, viewGroup, false);

//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

        return new MyRecycleriewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyRecycleriewHolder myRecycleriewHolder, int i) {

        final int position = i;
        myRecycleriewHolder.textView.setText(buttonNames[i]);
        myRecycleriewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, SecondActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });



    }

    @Override
    public int getItemCount() {
        return buttonNames.length;
    }

    public class MyRecycleriewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public CardView cardView;


        public MyRecycleriewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.buttonTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
