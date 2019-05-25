package com.example.dahir.sqlitesimpleplaceapp.models;


public class PendingplaceModel {
    private int placeID;
    private String placeTitle,placeContent,placeDate,placeTag;
    private Byte[] placeImage;

    public PendingplaceModel(){}

    public PendingplaceModel(String placeTitle, String placeContent,String placeTag, String placeDate) {
        this.placeTitle = placeTitle;
        this.placeContent = placeContent;
        this.placeDate = placeDate;
        this.placeTag = placeTag;
    }

    public PendingplaceModel(int placeID, String placeTitle, String placeContent,String placeTag, String placeDate) {
        this.placeID = placeID;
        this.placeTitle = placeTitle;
        this.placeContent = placeContent;
        this.placeDate = placeDate;
        this.placeTag = placeTag;
    }

    public int getplaceID() {
        return placeID;
    }

    public void setplaceID(int placeID) {
        this.placeID = placeID;
    }

    // Place Title
    public String getplaceTitle() {
        return placeTitle;
    }

    public void setplaceTitle(String placeTitle) {
        this.placeTitle = placeTitle;
    }

    // Place Address
    public String getplaceContent() {
        return placeContent;
    }

    public void setplaceContent(String placeContent) {
        this.placeContent = placeContent;
    }

    // Place date
    public String getplaceDate() {
        return placeDate;
    }

    public void setplaceDate(String placeDate) {
        this.placeDate = placeDate;
    }

    // Place Category
    public String getplaceTag() {
        return placeTag;
    }

    public void setplaceTag(String placeTag) {
        this.placeTag = placeTag;
    }
}
