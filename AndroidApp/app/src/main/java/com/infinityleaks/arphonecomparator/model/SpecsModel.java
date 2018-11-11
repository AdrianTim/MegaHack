package com.infinityleaks.arphonecomparator.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecsModel {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Path")
    @Expose
    private String path;
    @SerializedName("Weight")
    @Expose
    private String weight;
    @SerializedName("Width")
    @Expose
    private String width;
    @SerializedName("Height")
    @Expose
    private String height;
    @SerializedName("Thickness")
    @Expose
    private String thickness;
    @SerializedName("BatteryHours")
    @Expose
    private String batteryHours;
    @SerializedName("Cpu")
    @Expose
    private String cpu;
    @SerializedName("Ram")
    @Expose
    private String ram;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getBatteryHours() {
        return batteryHours;
    }

    public void setBatteryHours(String batteryHours) {
        this.batteryHours = batteryHours;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }
}
