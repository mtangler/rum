package com.app.core;

public class BusEvent {

    // var and objects
    public int EventType = 0;
    public int position = 0;
    public String someStr = "";

    // constants
    public static final int ET_ON_RESUME = 1;
    public static final int ET_CROPPER = 2;
    public static final int ET_BACK_PRESS = 3;
    public static final int ET_DATE_PICK = 5;
    public static final int ET_TOP_TAB_SELECTED = 6;

    public BusEvent(int eventType, String someStr) {
        EventType = eventType;
        this.someStr = someStr;
    }

    public BusEvent(int eventType, int position) {
        EventType = eventType;
        this.position = position;
    }

    public BusEvent() {
    }

    public BusEvent(int eventType) {
        this.EventType = eventType;
    }
}
