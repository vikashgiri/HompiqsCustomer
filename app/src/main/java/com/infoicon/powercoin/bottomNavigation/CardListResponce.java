package com.infoicon.powercoin.bottomNavigation;

/**
 * Created by HP-PC on 3/17/2018.
 */

public class CardListResponce {

    String id;
    String user_id;
    String guest_user_id;
    String merchant_inventry_id;
    String test_date;
    String time_slot_start_time;
    String time_slot_end_time;
    String number_of_item;
    String created_date;
    CardListDetailResponce cardListDetailResponceArrayList;
String test_location;

    public String getTest_location() {
        return test_location;
    }

    public void setTest_location(String test_location) {
        this.test_location = test_location;
    }

    public CardListDetailResponce getCardListDetailResponceArrayList() {
        return cardListDetailResponceArrayList;
    }

    public void setCardListDetailResponceArrayList(CardListDetailResponce cardListDetailResponceArrayList) {
        this.cardListDetailResponceArrayList = cardListDetailResponceArrayList;
    }

    public String getId()
    {
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

    public String getGuest_user_id() {
        return guest_user_id;
    }

    public void setGuest_user_id(String guest_user_id) {
        this.guest_user_id = guest_user_id;
    }

    public String getMerchant_inventry_id() {
        return merchant_inventry_id;
    }

    public void setMerchant_inventry_id(String merchant_inventry_id) {
        this.merchant_inventry_id = merchant_inventry_id;
    }

    public String getTest_date() {
        return test_date;
    }

    public void setTest_date(String test_date) {
        this.test_date = test_date;
    }

    public String getTime_slot_start_time() {
        return time_slot_start_time;
    }

    public void setTime_slot_start_time(String time_slot_start_time) {
        this.time_slot_start_time = time_slot_start_time;
    }

    public String getTime_slot_end_time() {
        return time_slot_end_time;
    }

    public void setTime_slot_end_time(String time_slot_end_time) {
        this.time_slot_end_time = time_slot_end_time;
    }

    public String getNumber_of_item() {
        return number_of_item;
    }

    public void setNumber_of_item(String number_of_item) {
        this.number_of_item = number_of_item;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
