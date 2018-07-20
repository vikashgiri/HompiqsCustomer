package com.infoicon.powercoin.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.LabBeans;
import com.infoicon.powercoin.bottomNavigation.adapter.HomeFragmentssAdapter;
import com.infoicon.powercoin.bottomNavigation.fragment.HomesFragment;
import com.infoicon.powercoin.utils.HelperMethods;

import java.util.ArrayList;
import java.util.HashMap;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.FragmentLabBinding;

/**
 * Created by HP-PC on 3/2/2018.
 */

public class LabFragments extends Fragment
       {

    HashMap<String, String> HashMapForURL ;
    FragmentLabBinding mBinding;

    public LabFragments() {
        // Required empty public constructor
    }

    private void initView()
    {
        int mNoOfColumns = HelperMethods.calculateNoOfColumns(getActivity());
        LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(lLayout);
        setProductAdapter();
        mBinding.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_lab, container, false);
        initView();
        return mBinding.getRoot();
    }


    private void setProductAdapter() {
        final ArrayList<LabBeans> uniqueLabListArrayList=
                ((App) getActivity().getApplication()).getUniqueLab();
        HomeFragmentssAdapter productFragmentAdapter = new
                HomeFragmentssAdapter(getActivity(),uniqueLabListArrayList, new
                HomeFragmentssAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            int position) {
                        Fragment fragment =new SearchTestFragments();
                        HelperMethods.loadFragment(getActivity(),fragment);
                    }
                });
        mBinding.recyclerView.setAdapter(productFragmentAdapter);
    }
}
