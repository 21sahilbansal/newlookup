package com.loconav.lookup.base;

/**
 * Created by sejal on 19-07-2018.
 */

public class PubSubEvent {

    private Object object;
    private String message;

    public PubSubEvent(String message, Object object) {
        this.object = object;
        this.message = message;
    }

    public PubSubEvent(String message) {
        this.message = message;
        this.object = null;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getObject(){
        return this.object;
    }


}
