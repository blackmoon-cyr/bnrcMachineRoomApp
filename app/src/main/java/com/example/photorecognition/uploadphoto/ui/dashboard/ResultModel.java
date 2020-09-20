package com.example.photorecognition.uploadphoto.ui.dashboard;

import java.util.ArrayList;
import java.util.List;

public class ResultModel {

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public List<List<Double>> getBboxResult() {
        return bboxResult;
    }

    public void setBboxResult(List<List<Double>> bboxResult) {
        this.bboxResult = bboxResult;
    }

    public List<String> getBrandResult() {
        return brandResult;
    }

    public void setBrandResult(List<String> brandResult) {
        this.brandResult = brandResult;
    }

    public List<String> getVersionResult() {
        return versionResult;
    }

    public void setVersionResult(List<String> versionResult) {
        this.versionResult = versionResult;
    }

    public List<String> getTypeResult() {
        return typeResult;
    }

    public void setTypeResult(List<String> typeResult) {
        this.typeResult = typeResult;
    }

    public List<Integer> getHeightResult() {
        return heightResult;
    }

    public void setHeightResult(List<Integer> heightResult) {
        this.heightResult = heightResult;
    }

    public List<Integer> getLocationResult() {
        return locationResult;
    }

    public void setLocationResult(List<Integer> locationResult) {
        this.locationResult = locationResult;
    }

    public String getResultImg() {
        return resultImg;
    }

    public void setResultImg(String resultImg) {
        this.resultImg = resultImg;
    }

    private Integer requestId;

    private String requestTime;


    private List<List<Double>> bboxResult = new ArrayList<List<Double>>();

    private List<String> brandResult = new ArrayList<String>();

    private List<String> versionResult = new ArrayList<String>();

    private List<String> typeResult = new ArrayList<String>();

    private List<Integer> heightResult = new ArrayList<Integer>();

    private List<Integer> locationResult = new ArrayList<Integer>();

    private String resultImg;
}
