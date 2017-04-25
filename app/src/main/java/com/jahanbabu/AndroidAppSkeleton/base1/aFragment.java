package com.jahanbabu.AndroidAppSkeleton.base1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jahanbabu.AndroidAppSkeleton.R;
import com.jahanbabu.AndroidAppSkeleton.adapter.ProductsAdapterNormal;
import com.jahanbabu.AndroidAppSkeleton.model.Product;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link aFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link aFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class aFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static Context mContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Product> products;



//    @BindView(R.id.ultimate_recycler_view) UltimateRecyclerView ultimateRecyclerView;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
//    RecyclerView recyclerView;

    public aFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static aFragment newInstance(Context context) {
        aFragment fragment = new aFragment();
        Bundle args = new Bundle();
        args.putString("index", context.toString());
        fragment.setArguments(args);
        mContext = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus myEventBus = EventBus.getDefault();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        products = new ArrayList<>();
        for (int i = 0; i < 30; i++){
            Product p = new Product("Name"+i, "Type "+i, "Description "+i, "Image", new Date().toString(), "Place "+i, "");
            products.add(p);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        ButterKnife.bind(mContext, view);
//        ultimateRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.ultimate_recycler_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
//        ProductsAdapter productsAdapter = new ProductsAdapter(products);
        ProductsAdapterNormal productsAdapter = new ProductsAdapterNormal(products);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(productsAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(mContext);
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(mContext);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
