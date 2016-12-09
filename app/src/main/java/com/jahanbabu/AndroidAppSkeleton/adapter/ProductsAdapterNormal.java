package com.jahanbabu.AndroidAppSkeleton.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jahanbabu.AndroidAppSkeleton.R;
import com.jahanbabu.AndroidAppSkeleton.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapterNormal extends RecyclerView.Adapter<ProductsAdapterNormal.MyViewHolder> {

    private ArrayList<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView, typeTextView, dateTextView, descriptionTextView, placeTextView;
        private ImageView pImageView;

        public MyViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            typeTextView = (TextView) view.findViewById(R.id.typeTextView);
            dateTextView = (TextView) view.findViewById(R.id.dateTextView);
            descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
            placeTextView = (TextView) view.findViewById(R.id.placeTextView);
            pImageView = (ImageView) view.findViewById(R.id.pImageView);
        }
    }

    public ProductsAdapterNormal(ArrayList<Product> productList) {
        this.productList = productList;
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.nameTextView.setText(p.getName());
        holder.typeTextView.setText(p.getType());
        holder.dateTextView.setText(p.getDate());
        holder.descriptionTextView.setText(p.getDescription());
        holder.placeTextView.setText(p.getPlace());

        Picasso.with(holder.pImageView.getContext())
                .load(p.getImage()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                .into(holder.pImageView);
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
}
