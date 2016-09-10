package com.example.martin.try4;


import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable {

    private String objectName;
    private ArrayList<String> arrayColorList;
    private ArrayList<Float> gradients;

    public Profile(String objectName, ArrayList<String> arrayList, ArrayList<Float> gradients){
        this.objectName=objectName;
        this.arrayColorList=arrayList;
        this.gradients=gradients;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public ArrayList<String> getArrayList() {
        return arrayColorList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayColorList = arrayList;
    }

    public void setGradients(ArrayList<Float> arrayList) {
        this.gradients = gradients;
    }

    public ArrayList<Float> getGradients() {
        return gradients;
    }

    public void addGradient(Float f){
        gradients.add(f);
    }
}
