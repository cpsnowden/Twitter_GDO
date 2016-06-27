package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.tools.javac.util.List;

import java.util.ArrayList;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class Status {

//    public static enum STATUS {
//        RUNNING("Running"),
//        FINISHED("Finished"),
//        ERROR("Error"),
//        ORDERED("Ordered"),
//        STOPPED("Stopped");
//
//        private final String text;
//
//        private STATUS(final String text) {
//            this.text = text;
//        }
//
//        @Override
//        public String toString() {
//            return text;
//        }
//    }

    public static enum STATUS {RUNNING, FINISHED, ERROR, ORDERED, STOPPED, READY_FOR_ANALYTICS}

    ;

    private STATUS status;

    public Status() {
    }

    ;

    public Status(STATUS status) {
        this.status = status;
    }

    @JsonProperty
    public STATUS getStatus() {
        return status;
    }

    @JsonProperty
    public void setStatus(STATUS status) {
        this.status = status;
    }

    public static java.util.List<STATUS> getValidStatus(String s) {

        java.util.List<STATUS> validStatuses = new ArrayList<>();
        STATUS st;

        for (STATUS es : STATUS.values()) {
            if (es.toString().equals(s)) {
                st = es;
                validStatuses.add(st);
                if (st == STATUS.READY_FOR_ANALYTICS) {
                    validStatuses.add(STATUS.FINISHED);
                    validStatuses.add(STATUS.STOPPED);
                }
                return validStatuses;
            }
        }

        return null;
    }

}
