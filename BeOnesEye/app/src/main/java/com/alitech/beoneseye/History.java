package com.alitech.beoneseye;

public class History {
    private String ID = "id";
    private String DATETIME = "datetime";
    private String TEXT = "textextracted";
    private String IMAGE_PATH = "imagePath";
    private String FEEDBACK = "feedback";
    private String ACTION = "Action_";

    public History(String ID, String DATETIME, String TEXT, String IMAGE_PATH, String FEEDBACK, String ACTION) {
        this.ID = ID;
        this.DATETIME = DATETIME;
        this.TEXT = TEXT;
        this.IMAGE_PATH = IMAGE_PATH;
        this.FEEDBACK = FEEDBACK;
        this.ACTION = ACTION;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public void setDATETIME(String DATETIME) {
        this.DATETIME = DATETIME;
    }

    public void setTEXT(String TEXT) {
        this.TEXT = TEXT;
    }

    public void setIMAGE_PATH(String IMAGE_PATH) {
        this.IMAGE_PATH = IMAGE_PATH;
    }

    public void setFEEDBACK(String FEEDBACK) {
        this.FEEDBACK = FEEDBACK;
    }

    public void setACTION(String ACTION) {
        this.ACTION = ACTION;
    }

    public String getID() {
        return ID;
    }

    public String getDATETIME() {
        return DATETIME;
    }

    public String getTEXT() {
        return TEXT;
    }

    public String getIMAGE_PATH() {
        return IMAGE_PATH;
    }

    public String getFEEDBACK() {
        return FEEDBACK;
    }

    public String getACTION() {
        return ACTION;
    }

    @Override
    public String toString() {
        return "History{" +
                "ID='" + ID + '\'' +
                ", DATETIME='" + DATETIME + '\'' +
                ", TEXT='" + TEXT + '\'' +
                ", IMAGE_PATH='" + IMAGE_PATH + '\'' +
                ", FEEDBACK='" + FEEDBACK + '\'' +
                ", ACTION='" + ACTION + '\'' +
                '}';
    }
}
