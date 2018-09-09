package com.example.multiselectapplicationtest;

public class DataModel {


    public DataModel(String test1, String test2) {
        this.test1 = test1;
        this.test2 = test2;
    }

    private String test1;

    private String test2;

    private boolean isSelected;

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void toggle() {
        isSelected = !isSelected;
    }

}
