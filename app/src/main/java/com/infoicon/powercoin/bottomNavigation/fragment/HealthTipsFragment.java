package com.infoicon.powercoin.bottomNavigation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infoicon.powercoin.bottomNavigation.adapter.HealthTipsFragmentAdapter;

import infoicon.com.powercoin.R;

/**
 * Created by info on 22/2/18.
 */

public class HealthTipsFragment extends Fragment
{
    RecyclerView rView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_health_tips, container, false);
        LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
        rView = (RecyclerView)view.findViewById(R.id.recyclerView);
        rView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        setFeatureAdapter();
        rView.setNestedScrollingEnabled(false);


        return view;
    }
    private void setFeatureAdapter() {
        HealthTipsFragmentAdapter messageFragmentAdapter = new
                HealthTipsFragmentAdapter(getActivity(), new
                HealthTipsFragmentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            int position) {
                    }

                });
        rView.setAdapter(messageFragmentAdapter);
    }
}