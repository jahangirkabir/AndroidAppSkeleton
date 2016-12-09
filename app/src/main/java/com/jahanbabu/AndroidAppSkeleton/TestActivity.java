package com.jahanbabu.AndroidAppSkeleton;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jahanbabu.AndroidAppSkeleton.adapter.ProductsAdapterNormal;
import com.jahanbabu.AndroidAppSkeleton.model.Product;

import java.util.ArrayList;
import java.util.Date;

public class TestActivity extends AppCompatActivity {

    Context context;
    private ArrayList<Product> products;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Roboto-Light.ttf")
////                .setDefaultFontPath("fonts/Raleway-Light.ttf")
////                        .setDefaultFontPath("fonts/Raleway-Medium.ttf")
////                .setDefaultFontPath("fonts/Roboto-Bold.ttf")
//                        .setFontAttrId(R.attr.fontPath)
//                        .build()
//        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        products = new ArrayList<>();
        for (int i = 0; i < 30; i++){
            Product p = new Product("Name"+i, "Type "+i, "Description "+i, "Image", new Date().toString(), "Place "+i, "");
            products.add(p);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
//        ProductsAdapter productsAdapter = new ProductsAdapter(products);
        ProductsAdapterNormal productsAdapter = new ProductsAdapterNormal(products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
