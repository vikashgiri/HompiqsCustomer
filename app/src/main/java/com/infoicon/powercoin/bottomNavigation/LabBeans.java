package com.infoicon.powercoin.bottomNavigation;

import java.util.ArrayList;

/**
 * Created by HP-PC on 3/12/2018.
 */

public class LabBeans {
      String lab_id;
    String lab_name;
    String reg_number;
    String lab_address;
    String city_id;
    String lab_google_address;
    String lat;
    String lng;
    String test_id;
    String price;
    String discount_type;
    String discount_value;
    String time_consumed_by_test;
    String lab_distance;
ArrayList<TestList>testListArrayList;
    String inventry_id;
    String labImage;

    public String getLabImage() {
        return labImage;
    }

    public void setLabImage(String labImage) {
        this.labImage = labImage;
    }

    public String getInventry_id() {
        return inventry_id;
    }

    public void setInventry_id(String inventry_id) {
        this.inventry_id = inventry_id;
    }

    public ArrayList<TestList> getTestListArrayList() {
        return testListArrayList;
    }

    public void setTestListArrayList(ArrayList<TestList> testListArrayList) {
        this.testListArrayList = testListArrayList;
    }

    public String getLab_id() {
        return lab_id;
    }

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public String getLab_name() {
        return lab_name;
    }

    public void setLab_name(String lab_name) {
        this.lab_name = lab_name;
    }

    public String getReg_number() {
        return reg_number;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }

    public String getLab_address() {
        return lab_address;
    }

    public void setLab_address(String lab_address) {
        this.lab_address = lab_address;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getLab_google_address() {
        return lab_google_address;
    }

    public void setLab_google_address(String lab_google_address) {
        this.lab_google_address = lab_google_address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(String discount_value) {
        this.discount_value = discount_value;
    }

    public String getTime_consumed_by_test() {
        return time_consumed_by_test;
    }

    public void setTime_consumed_by_test(String time_consumed_by_test) {
        this.time_consumed_by_test = time_consumed_by_test;
    }

    public String getLab_distance() {
        return lab_distance;
    }

    public void setLab_distance(String lab_distance) {
        this.lab_distance = lab_distance;
    }
}
