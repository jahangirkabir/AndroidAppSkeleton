package com.jahanbabu.AndroidAppSkeleton.base1;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jahanbabu.AndroidAppSkeleton.R;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Base1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Base1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Base1Fragment extends Fragment implements MaterialTabListener {

    public static Context mContext;
    private ViewPager viewPager;
    private MaterialTabHost tabHost;
    private static final int Losts = 0;
    private static final int Finds = 1;
    private View v = null;
    public MyPagerAdapter myPagerAdapter;
    public static MyPagerAdapter staticMyPagerAdapter;
    String TAG = "Base1Fragment";
    private OnFragmentInteractionListener mListener;
    public Base1Fragment() {
        // Required empty public constructor
    }

    public static Base1Fragment newInstance(Context context) {
        Base1Fragment fragment = new Base1Fragment();
        Bundle args = new Bundle();
        args.putString("index", context.toString());
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        if (menu.findItem(R.id.action_flag) != null && menu.findItem(R.id.action_search) != null) {
//            menu.findItem(R.id.action_flag).setVisible(false);
//            menu.findItem(R.id.action_search).setVisible(false);
//        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim;
        if (enter) {
            anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        } else {
            anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        }

        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) { }

            public void onAnimationRepeat(Animation animation) { }

            public void onAnimationStart(Animation animation) { }
        });

        return anim;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_base, container, false);

//        tabHost = (MaterialTabHost) v.findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
//        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont(tabLayout);
        return v;
    }

    private void changeTabsFont(TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int k = 0; k < tabChildsCount; k++) {
                View tabViewChild = vgTab.getChildAt(k);
                if (tabViewChild instanceof TextView) {
                    Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/Raleway-SemiBold.ttf");
                    ((TextView) tabViewChild).setTypeface(custom_font);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        Log.e("onTabSelected : ", " "+tab.getPosition());
        //adapter.notifyDataSetChanged();
        //viewPager.setOffscreenPageLimit(4);
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        Log.e("onTabReselected : ", " "+tab.getPosition());
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }



    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        String[] tabs;
        Context c;
        public MyPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            c = context;
            tabs = c.getResources().getStringArray(R.array.tabs);
        }


        @Override
        public Fragment getItem(int position) {
            //MyFragment myFragment = MyFragment.getInstance(position);
            Fragment fragment = null;
            switch (position) {
                case Losts:
                    fragment = aFragment.newInstance(mContext);
//                    fragment = Fragment_Map_View.newInstance(mContext);
//                    fragment.setUserVisibleHint(true);
                    break;
                case Finds:
                    fragment = bFragment.newInstance(mContext);
//                    fragment = Pending_request.newInstance(mContext);
                    break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
            //return super.getItemPosition(object);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
//        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        ///Here we have to pass ChildFragmentManager instead of FragmentManager.
        myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), getContext());
        staticMyPagerAdapter = myPagerAdapter;
//        adapter = new MyPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(new aFragment(), "Fragment1");
//        adapter.addFragment(new aFragment(), "Fragment2");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected : ", " "+position);
                if (position==0){
//                    (Fragment_Map_View) myPagerAdapter.getItem(position).initialAPICall();

                } else if (position==1){

                } else if (position==2){

                } else if (position==3){

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(0);

    }

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
