package com.infoicon.powercoin.main;

/**
 * Created by HP-PC on 4/10/2018.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infoicon.powercoin.login.DoctorNameHint;
import com.infoicon.powercoin.navigationdrawer.adapter.CustomerAdapter;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import infoicon.com.powercoin.Manifest;
import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityUploadDescriptionBinding;

public class UploadDescriptionActivity extends AppCompatActivity {

    ImageView user_profile_photo;
    String message, encodedImage;
    Button btnSubmit;
    private ProgressDialog pDialog;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    Bitmap thumbnail = null;
    ActivityUploadDescriptionBinding uploadDescriptionBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadDescriptionBinding= DataBindingUtil.setContentView(this,R.layout.activity_upload_description);
       user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        ButtonClick();
        uploadDescriptionBinding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        uploadDescriptionBinding.toolbar.txtTitle.setText("Upload description");
        uploadDescriptionBinding.doctorName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if (newText.length() >=1)
                    Hint(newText);
            }

        });
    }

    public void ButtonClick()
    {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String image = getStringImage(thumbnail);

                login(image);
            }
        });

        user_profile_photo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(ContextCompat.checkSelfPermission(UploadDescriptionActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                   selectImage();
                        }
                else{
                    if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
                }                return false;
            }
        });
    }
    JSONObject sendJson=null;


    private void login (String encodedImage)
    {
       /* http://localhost/lab/application/index?parameters={user_id:3,image:data:image\/jpeg;base64,\/9j\/4AAQSkZJRgABAQEAYABgAAD\/2wBDAAIBAQIBAQICAgIzrSn3\/pfgf\/2Q==,image_type:subscription,method:uploadImage,refered_by:ravi*/
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "uploadImage");
            sendJson.put("user_id", SavePref.getPref(UploadDescriptionActivity.this,SavePref.User_id));
            sendJson.put("image", encodedImage);
            sendJson.put("image_type", "subscription");
            sendJson.put("refered_by", uploadDescriptionBinding.doctorName.getText().toString()+","+uploadDescriptionBinding.patientAds.getText().toString());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Keys.BASE_URL +"application/index?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                HelperMethods.showSnackBar(UploadDescriptionActivity.this, "Uploaded successfully");
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                pDialog.dismiss();
                                finish();
                            }
                            else
                            {
                                HelperMethods.showSnackBar(UploadDescriptionActivity.this, "Login Invalid");
                                pDialog.dismiss();
                            }
                        }
                        catch (JSONException e)
                        {
                            pDialog.dismiss();
                            HelperMethods.showSnackBar(UploadDescriptionActivity.this,"Login Error");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }


    private void Hint (String encodedImage)
    {



        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.67.139.216:3000/usercontroller/getDoctors?doctor_name=v",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try {
                            JSONObject jObject = new JSONObject(response);
                            ArrayList<DoctorNameHint> aDoctorNameHints=new ArrayList<>();
                            if (jObject.getString("status").equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    DoctorNameHint doctorNameHint = new DoctorNameHint();
                                    doctorNameHint.setDoctor_name(jsonObject.getString("doctor_name"));
                                    aDoctorNameHints.add(doctorNameHint);

                                }
                            }


                            CustomerAdapter customerAdapter = new CustomerAdapter(UploadDescriptionActivity.this, R.layout.customer_auto, aDoctorNameHints);
                            uploadDescriptionBinding.doctorName.setAdapter(customerAdapter);
                                uploadDescriptionBinding.doctorName.setAdapter(customerAdapter);
                        }
                        catch (JSONException e)
                        {
                            HelperMethods.showSnackBar(UploadDescriptionActivity.this,"Login Error");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("doctor_name",uploadDescriptionBinding.doctorName.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Take Photo")) {
                        userChoosenTask = "Take Photo";
                            cameraIntent();

                    } else if (items[item].equals("Choose from Library")) {
                        userChoosenTask = "Choose from Library";
                            galleryIntent();

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }                }



        });
        builder.show();
    }
    private final int CAMERA_RESULT = 101;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
               selectImage();                        }
            else{
                Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        user_profile_photo.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(UploadDescriptionActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        user_profile_photo.setImageBitmap(thumbnail);
    }

    public String getStringImage(Bitmap bmp){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG,60, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }catch (Exception e){

        }
        return encodedImage;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}