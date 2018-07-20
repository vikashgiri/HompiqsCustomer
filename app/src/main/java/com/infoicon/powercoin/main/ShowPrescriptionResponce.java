package com.infoicon.powercoin.main;

/**
 * Created by HP-PC on 5/6/2018.
 */

public class ShowPrescriptionResponce {
    String id;
    String user_id;
    String image_name;
    String refered_by;
    String mail_sent;
    String created_date;

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

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getRefered_by() {
        return refered_by;
    }

    public void setRefered_by(String refered_by) {
        this.refered_by = refered_by;
    }

    public String getMail_sent() {
        return mail_sent;
    }

    public void setMail_sent(String mail_sent) {
        this.mail_sent = mail_sent;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
