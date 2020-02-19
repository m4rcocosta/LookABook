package com.example.textrecognition;

/**
 * Created by arthonsystechnologiesllp on 10/03/17.
 */

public class TitleModel {

    boolean isSelected;
    String titleName;

    //now create constructor and getter setter method using shortcut like command+n for mac & Alt+Insert for window.


    public TitleModel(boolean isSelected, String titleName) {
        this.isSelected = isSelected;
        this.titleName = titleName;
    }
    public TitleModel(String titleName){    //by default isSelected is set false
        this.isSelected = false;
        this.titleName = titleName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String gettitleName() {
        return titleName;
    }

    public void settitleName(String titleName) {
        this.titleName = titleName;
    }
}