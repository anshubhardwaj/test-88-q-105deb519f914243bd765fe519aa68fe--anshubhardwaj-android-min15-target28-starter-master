package org.codejudge.android;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.codejudge.android.apicall.RestoInterface;
import org.codejudge.android.apicall.restbeandata.Result;

import java.util.ArrayList;

public class RestListAdapter extends RecyclerView.Adapter {
    Context context;
    private ArrayList<Result> smtoplist;
    RestoInterface lisner;
    public RestListAdapter(Context context, ArrayList<Result> smtoplist, RestoInterface lisner) {
        this.context = context;
        this.smtoplist = smtoplist;
        this.lisner = lisner;
    }
    @Override
    public int getItemCount() {
        return smtoplist.size();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rest_list_row, parent, false);
        vh = new BlogListHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        if (holder instanceof BlogListHolder) {
            //Log.e("Slug",smtoplist.get(pos).getSlug());
            ((BlogListHolder) holder).resto_name.setText(smtoplist.get(pos).getName());
            ((BlogListHolder) holder).rating.setText(String.valueOf(smtoplist.get(pos).getRating()));
            ((BlogListHolder) holder).resto_loc.setText(smtoplist.get(pos).getFormattedAddress());
            ((BlogListHolder) holder).restorate.setText(smtoplist.get(pos).getPriceLevel());
            if (smtoplist.get(pos).getOpeningHours().getOpenNow()){
                ((BlogListHolder) holder).resto_closetime.setVisibility(View.VISIBLE);
            ((BlogListHolder) holder).resto_closetime.setText("Open Now");}
            ((BlogListHolder) holder).restorate.setText(smtoplist.get(pos).getPriceLevel());
//            Context context = ((BlogListHolder) holder).image_article.getContext();
            if(smtoplist.get(pos).getIcon()!=null) {
                    Picasso.get().load(smtoplist.get(pos).getIcon())
                            .placeholder(R.drawable.ic_launcher_background) // optional
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .transform(new RoundedTransformation(25, 2))
                            //.resize(200,200)
                            //.onlyScaleDown()
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.drawable.ic_launcher_background) // optional
                            .into(((BlogListHolder) holder).image_resto);
            }
        }
    }
    private class BlogListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView product;
        TextView resto_name, rating,resto_closetime;
        TextView resto_loc,resto_promo,resto_des;
        ImageView image_resto;
        TextView restorate;
        private BlogListHolder(View view) {
            super(view);
            resto_name = view.findViewById(R.id.resto_name);
            rating = view.findViewById(R.id.rating);
            resto_closetime = view.findViewById(R.id.resto_closetime);
            resto_loc = view.findViewById(R.id.resto_loc);
            image_resto = view.findViewById(R.id.image_resto);
            resto_promo = view.findViewById(R.id.resto_promo);
            resto_des = view.findViewById(R.id.resto_des);
            restorate = view.findViewById(R.id.restorate);
        }
        @Override
        public void onClick(View v) {

            final int pos = getAdapterPosition();

        }
    }
}
