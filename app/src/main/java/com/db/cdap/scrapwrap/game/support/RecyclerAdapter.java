package com.db.cdap.scrapwrap.game.support;

/**
 * Created by Tharushan on 2018-06-21.
 */

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.db.cdap.scrapwrap.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] avatarTitles = {"Brave Knight",
            "Ninja Girl",
            "Ninja Boy",
            "Scrap Robot",
            "Pumpkin Boy",
            "Zombie Boy",
            "Zombie Girl",
            "Courageous Dog"};

    private String[] avatarDetails = {"Brave Knight details",
            "Ninja Girl details", "Ninja Boy details",
            "Scrap Robot details", "Pumpkin Boy details",
            "Zombie Boy details", "Zombie Girl details",
            "Courageous Dog details"};

    private int[] avatarImages = { R.drawable.avatar_knight,
            R.drawable.avatar_ninja_girl,
            R.drawable.avatar_ninja_boy,
            R.drawable.avatar_robot,
            R.drawable.avatar_pumpkin,
            R.drawable.avatar_zombie_boy,
            R.drawable.avatar_zombie_girl,
            R.drawable.avatar_dog };


    private String[] otherTitles = {"Brave Knight",
            "Ninja Girl",
            "Ninja Boy",
            "Scrap Robot",
            "Pumpkin Boy",
            "Zombie Boy",
            "Zombie Girl",
            "Courageous Dog"};

    private String[] otherDetails = {"Brave Knight details",
            "Ninja Girl details", "Ninja Boy details",
            "Scrap Robot details", "Pumpkin Boy details",
            "Zombie Boy details", "Zombie Girl details",
            "Courageous Dog details"};

    private int[] otherImages = { R.drawable.avatar_knight,
            R.drawable.avatar_ninja_girl,
            R.drawable.avatar_ninja_boy,
            R.drawable.avatar_robot,
            R.drawable.avatar_pumpkin,
            R.drawable.avatar_zombie_boy,
            R.drawable.avatar_zombie_girl,
            R.drawable.avatar_dog };


    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;
        public Button btnAddToCart;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail =
                    (TextView)itemView.findViewById(R.id.item_detail);

            btnAddToCart = (Button)itemView.findViewById(R.id.btnAddtoCart);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Added to the cart!",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.store_item_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(avatarTitles[i]);
        viewHolder.itemDetail.setText(avatarDetails[i]);
        viewHolder.itemImage.setImageResource(avatarImages[i]);
    }

    @Override
    public int getItemCount() {
        return avatarTitles.length;
    }
}