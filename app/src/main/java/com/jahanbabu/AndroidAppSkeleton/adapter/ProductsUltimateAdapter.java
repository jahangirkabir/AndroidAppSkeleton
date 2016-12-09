package com.jahanbabu.AndroidAppSkeleton.adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jahanbabu.AndroidAppSkeleton.R;
import com.jahanbabu.AndroidAppSkeleton.databinding.AdapterProductBinding;
import com.jahanbabu.AndroidAppSkeleton.model.Product;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
 
public class ProductsUltimateAdapter extends UltimateViewAdapter {
 
    private ArrayList<Product> productList;

    public class MyViewHolder extends UltimateRecyclerviewViewHolder {

        private AdapterProductBinding productBinding;

        public MyViewHolder(AdapterProductBinding _productBinding) {
            super(_productBinding.getRoot());
            this.productBinding = _productBinding;
        }

        public void bindConnection(Product product){
            productBinding.setProduct(product);
        }

    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        private AdapterProductBinding productBinding;

        public ViewHolder(AdapterProductBinding _productBinding) {
            super(_productBinding.getRoot());
            this.productBinding = _productBinding;
        }

        public void bindConnection(Product product){
            productBinding.setProduct(product);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.BLUE);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public ProductsUltimateAdapter(ArrayList<Product> productList) {
        this.productList = productList;
    }
 
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        AdapterProductBinding productBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_product, parent, false);
//        return new MyViewHolder(productBinding);
//    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        AdapterProductBinding productBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_product, parent, false);
        return new ViewHolder(productBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.bindConnection(productList.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public int getAdapterItemCount() {
        return productList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }
}
