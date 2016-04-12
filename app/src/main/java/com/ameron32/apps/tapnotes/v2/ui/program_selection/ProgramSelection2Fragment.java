package com.ameron32.apps.tapnotes.v2.ui.program_selection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProgramSelection2Fragment extends Fragment {

    ViewPager pager;

    public ProgramSelection2Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_program_selection_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager = (ViewPager) view.findViewById(R.id.viewPager);
        pager.setPageTransformer(false, new ParallaxTransformer());
        pager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), pager, getItems()));
    }

    @Override
    public void onStart() {
        super.onStart();
//        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
//        if (tabLayout != null) {
//            tabLayout.setupWithViewPager(pager);
//        }
    }

    @Override
    public void onDestroyView() {
        pager.setAdapter(null);
        super.onDestroyView();
    }

    private List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("2015", "https://i.imgur.com/Mgx193t.png"));
        items.add(new Item("2016", "https://i.imgur.com/N8H7jHr.png"));
        return items;
    }

    public static class Item {
        private final String imageUrl;
        private final String name;

        public Item(String name, String imageUrl) {
            this.imageUrl = imageUrl;
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getName() {
            return name;
        }
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final ViewPager pager;
        private final List<Item> items;

        public ViewPagerAdapter(FragmentManager fm, ViewPager pager, List<Item> items) {
            super(fm);
            this.pager = pager;
            this.items = items;
        }

        @Override
        public Fragment getItem(int position) {
            ProgramViewPagerFragment fragment = ProgramViewPagerFragment.newInstance(items.get(position));
            fragment.notifyPosition(position);
            fragment.listenToPager(pager);
            return fragment;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items.get(position).getName();
        }
    }
}
