package com.winning.pregnancy.model;

public class ContentString {
	private String value;
	private int state;
	private int type;
	private String unit;
//	[{"value":"2016-06-01","state":0,"type":9,"unit":""},
// 	{"value":"163","state":0,"type":7,"unit":"cm"},
// 	{"value":"28","state":0,"type":8,"unit":"岁"},
	private String measuredate;
	private String imageName;
// 	{"measuredate":"2015-12-14 02:07:57","value":"","state":0,"imageName":"","type":3,"unit":"mmHg"},
// 	{"measuredate":"2015-12-14 02:07:57","value":"","state":0,"imageName":"","type":4,"unit":"mmol\/L"},
// 	{"measuredate":"2015-12-14 02:07:57","value":"null","state":0,"imageName":"","type":1,"unit":"Kg"},
// 	{"measuredate":"2015-12-14 02:07:57","value":"","state":0,"imageName":"","type":2,"unit":"°C"},
// 	{"measuredate":"2015-12-14 02:07:57","value":"","state":0,"imageName":"","type":5,"unit":""}]
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getMeasuredate() {
		return measuredate;
	}
	public void setMeasuredate(String measuredate) {
		this.measuredate = measuredate;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
   
}
