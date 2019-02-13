package com.loconav.lookup.base;

/**
 * Created by sejal on 19-07-2018.
 */

public class PubSubEvent {

    private final Object object;
    private final String message;

    protected PubSubEvent(String message, Object object) {
        this.object = object;
        this.message = message;
    }

    protected PubSubEvent(String message) {
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
