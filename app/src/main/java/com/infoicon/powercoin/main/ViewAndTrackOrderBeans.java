package com.infoicon.powercoin.main;

import java.util.ArrayList;

/**
 * Created by HP-PC on 3/20/2018.
 */

public class ViewAndTrackOrderBeans
{
    String id;
    String user_id;
    String  order_id;
    String  parent_order_id;
    String   lab_id;
    String  shipping_address_id;
    String   amount;
    String  payable_amount;
    String   discount_amount;
    String  commission_amount;
    String   tax_amount;
    String   payment_status;
    String   order_status;
    String   test_location;
    String  created_date;
    String  updated_date;
    String  rider_id;
    String  shipping_address;
    String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getRider_id() {
        return rider_id;
    }

    public void setRider_id(String rider_id) {
        this.rider_id = rider_id;
    }

    ArrayList<ViewAndTrackOrderDetail> viewAndTrackItemOrderBeanArrayList;

    public ArrayList<ViewAndTrackOrderDetail> getViewAndTrackItemOrderBeanArrayList() {
        return viewAndTrackItemOrderBeanArrayList;
    }

    public void setViewAndTrackItemOrderBeanArrayList(ArrayList<ViewAndTrackOrderDetail> viewAndTrackItemOrderBeanArrayList) {
        this.viewAndTrackItemOrderBeanArrayList = viewAndTrackItemOrderBeanArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getParent_order_id() {
        return parent_order_id;
    }

    public void setParent_order_id(String parent_order_id) {
        this.parent_order_id = parent_order_id;
    }

    public String getLab_id() {
        return lab_id;
    }

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public String getShipping_address_id() {
        return shipping_address_id;
    }

    public void setShipping_address_id(String shipping_address_id) {
        this.shipping_address_id = shipping_address_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getCommission_amount() {
        return commission_amount;
    }

    public void setCommission_amount(String commission_amount) {
        this.commission_amount = commission_amount;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getTest_location() {
        return test_location;
    }

    public void setTest_location(String test_location) {
        this.test_location = test_location;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
}
