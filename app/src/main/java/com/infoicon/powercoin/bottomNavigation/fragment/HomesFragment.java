package com.infoicon.powercoin.bottomNavigation.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.LabBeans;
import com.infoicon.powercoin.bottomNavigation.adapter.HomeFragmentAdapter;
import com.infoicon.powercoin.main.BannerBeans;
import com.infoicon.powercoin.main.BannerTestFragments;
import com.infoicon.powercoin.main.SearchTestFragments;
import com.infoicon.powercoin.main.UploadDescriptionActivity;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.FragmentHomeBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomesFragment extends Fragment
implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener
{
    HashMap<String, String> HashMapForURL ;
    FragmentHomeBinding mBinding;
    ArrayList<LabBeans> beansArrayList;
    ArrayList<BannerBeans> bannerList;

JSONObject sendJson;
    ProgressDialog progressDialog;

    public HomesFragment() {
        // Required empty public constructor
    }

 /*   private void setSlider()
    {
        for(String name : HashMapForURL.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .description(name)
                    .image(HashMapForURL.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            mBinding.slider.addSlider(textSliderView);
        }
        mBinding.slider.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        mBinding.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mBinding.slider.setCustomAnimation(new DescriptionAnimation());

        mBinding.slider.setDuration(3000);

        mBinding.slider.addOnPageChangeListener(this);
    }*/
 void slider() {
     imageSlider = (SliderLayout)getActivity().findViewById(R.id.slider);
     for (int i = 0; i < bannerList.size(); i++) {
String url=bannerList.get(i).getImageRootPath() + "/banner/" +bannerList.get(i).getImage_name();
         TextSliderView textSliderView = new TextSliderView(getActivity());
         // initialize a SliderLayout
         textSliderView
                 .image(url)
                 .setScaleType(BaseSliderView.ScaleType.Fit)
                 .setOnSliderClickListener(this);
         //add your extra information
         textSliderView.bundle(new Bundle());
         textSliderView.getBundle()
                 .putString("extra", "");

         imageSlider.addSlider(textSliderView);
     }
     imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
     imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
     imageSlider.setCustomAnimation(new DescriptionAnimation());
     imageSlider.setDuration(4000);
     imageSlider.addOnPageChangeListener(this);
 }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.slider.stopAutoCycle();
    }

    /*
           * init all views
           * */
    private void initView()
    {
        int mNoOfColumns = HelperMethods.calculateNoOfColumns(getActivity());
        LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(lLayout);
        setProductAdapter();
        mBinding.recyclerView.setNestedScrollingEnabled(false);
  //setSlider();
       mBinding.txtBookTest.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               HelperMethods.showDialogs(getActivity(),"BookTest");
           }
       });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);
        initView();
        getBanner();
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadDescriptionActivity.class);
                startActivity(intent);
             //  onCall();
            }
        });

        return mBinding.getRoot();
   }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:9988776655")));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Toast.makeText(getActivity(), "Call Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

 @Override
    public void onSliderClick(BaseSliderView slider)
    {
        Fragment fragment =new BannerTestFragments();
        Bundle bundle=new Bundle();
        bundle.putString("id",bannerList.get(position).getId());
        fragment.setArguments(bundle);
        HelperMethods.loadFragment(getActivity(),fragment);
    }

int position;
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
this.position=position;
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

 /*   public void AddImagesUrlOnline()
    {
        HashMapForURL = new HashMap<String, String>();
        HashMapForURL.put("test", "https://tse1.mm.bing.net/th?id=OIP.uozrKbNxdUvzIDc4dIQQlwHaE8&pid=15.1&P=0&w=300&h=300");
        HashMapForURL.put("test1", "https://tse2.mm.bing.net/th?id=OIP.rQ0ltHtjl0uKFw4EfFGr9AEyDM&pid=15.1&P=0&w=300&h=300");
        HashMapForURL.put("test2", "http://www.blackrock-clinic.ie/wp-content/uploads/2012/10/Lab-Banner.jpg");
        HashMapForURL.put("test3", "https://www.healthcareers.nhs.uk/sites/default/files/styles/hero_image/public/hero_images/Pathology..jpg?itok=_065qfHK");
        HashMapForURL.put("test4", "https://www.healthcareers.nhs.uk/sites/default/files/styles/hero_image/public/hero_images/Pathology..jpg?itok=_065qfHK");
    }
*/
    SliderLayout imageSlider;
/*
    void slider() {
        imageSlider = (SliderLayout) findViewById(R.id.slider);
        for (int i = 0; i < beansArrayList.size(); i++) {

            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView

                    .image(beansArrayList.get(i).getImageRootPath() + "/" + beansArrayList.get(i).getImage_name())

                    .setScaleType(BaseSliderView.ScaleType.Fit)

                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "");

            imageSlider.addSlider(textSliderView);
        }
        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        imageSlider.setDuration(4000);
        imageSlider.addOnPageChangeListener(this);
    }
*/

    @Override
    public void onStop() {
        super.onStop();
    mBinding.slider.stopAutoCycle();
    }

    /**
 * Setting Guest List on UI
 *

 */
    JSONObject manJson;
    private void getBanner() {
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Keys.BASE_URL_MANAGE_CONTROLLER+"getBanners?page=1&only_lab_data=1&lat="+ SavePref.getPref(getActivity(),SavePref.current_lat)+ "&lng=" + SavePref.getPref(getActivity(),SavePref.current_lon)+"&city_id=" + SavePref.getPref(getActivity(),SavePref.city_id),/*

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Keys.BASE_URL_MANAGE_CONTROLLER+"getBanners?page=1&only_lab_data=1&lat=12.9716&&lng=77.5946°&city_id=5",*/
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString = "";//your json string here
                        try {
                            JSONObject Object = new JSONObject(response);
                            JSONObject jsonObject = Object.getJSONObject("bannerList");

                            if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                                //JSONObject jObject = new JSONObject(response).getJSONObject("data");
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                bannerList=new ArrayList<>();

                                beansArrayList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject innerJObject = jsonArray.getJSONObject(i);
                                    // url_maps.put(""+i,Object.getString("imageRootPath")+"/"+innerJObject.getString("image_name") );
                                    //   JSONObject innerObject = new JSONObject(innerJObject.getString("params"));
                                    BannerBeans bannerBeans = new BannerBeans();

                                    bannerBeans.setImage_name(innerJObject.getString("package_name"));

                                    bannerBeans.setLab_id(innerJObject.getString("lab_id"));


                                    bannerBeans.setImage_name(innerJObject.getString("image_name"));
                                    bannerBeans.setImageRootPath(Object.getString("imageRootPath"));
                                    bannerBeans.setId(innerJObject.getString("id"));
                                    bannerBeans.setStatus(innerJObject.getString("status"));
                                    bannerList.add(bannerBeans);
                                }
                                slider();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                            }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

private void setProductAdapter() {

    getLabList();}



    private void getLabList() {

        progressDialog=new ProgressDialog(getActivity());

        progressDialog.show();
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
String lat= SavePref.getPref(getActivity(),SavePref.current_lat);
        String longs= SavePref.getPref(getActivity(),SavePref.current_lon);
        String city_id= SavePref.getPref(getActivity(),SavePref.city_id);

        //creating a string request to send request to the url

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Keys.BASE_URL_MANAGE_CONTROLLER+"searchLab?lat="+lat+"&lng="+longs+"&city_id="+city_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                          String imageRootPath=jsonObject.getString("imageRootPath");
                            String type=jsonObject.getString("type");
                             beansArrayList=new ArrayList<>();

                  aa:          for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                LabBeans labBeans=new LabBeans();
                                labBeans.setLab_id(jsonObject1.getString("lab_id"));
                                labBeans.setLab_name(jsonObject1.getString("lab_name"));
                                labBeans.setReg_number(jsonObject1.getString("reg_number"));
                                labBeans.setLab_address(jsonObject1.getString("lab_address"));
                                labBeans.setCity_id(jsonObject1.getString("city_id"));
                                labBeans.setLab_google_address(jsonObject1.getString("lab_google_address"));
                                labBeans.setLat(jsonObject1.getString("lat"));
                                labBeans.setLng(jsonObject1.getString("lng"));
                                labBeans.setLabImage(imageRootPath+"/"+type+"/"+jsonObject1.getString("lab_image"));

                                // labBeans.setTest_id(jsonObject1.getString("test_id"));
                              //  labBeans.setPrice(jsonObject1.getString("price"));
                                //labBeans.setDiscount_type(jsonObject1.getString("discount_type"));
                                //labBeans.setDiscount_value(jsonObject1.getString("discount_value"));
                                //labBeans.setTime_consumed_by_test(jsonObject1.getString("time_consumed_by_test"));
                                labBeans.setLab_distance(jsonObject1.getString("lab_distance").substring(0,2)+"\n Km");
                                for(int j=0;j<beansArrayList.size();j++)
                                {
                                    if (beansArrayList.get(j).getLab_id().equalsIgnoreCase(jsonObject1.getString("lab_id")))
                                    {
                                        continue aa;
                                    }
                                }


                                beansArrayList.add(labBeans);
                            }

                            HomeFragmentAdapter productFragmentAdapter = new
                                    HomeFragmentAdapter(getActivity(),beansArrayList, new
                                    HomeFragmentAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(
                                                int position) {
                                           // HelperMethods.showDialogs(getActivity(),"Lab");
                                            ((App)getActivity().getApplication()).setLab_id(beansArrayList.get(position).getLab_id());
                                            Fragment fragment =new SearchTestFragments();
                                            HelperMethods.loadFragment(getActivity(),fragment);
                                        }

                                    });
                            mBinding.recyclerView.setAdapter(productFragmentAdapter);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }


    }
