package com.infoicon.powercoin.navigationdrawer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import infoicon.com.powercoin.R;


public class TermsAndConditionFragment extends Fragment {



    public TermsAndConditionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_terms_and_condition, container, false);
       WebView webView=(WebView)view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(
                "http://52.67.139.216/lab/application/index?parameters={%22method%22:%22gettermandcondition%22}");
        webView.setWebViewClient(new WebViewClient(){

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    });
    return view;
    }


}
