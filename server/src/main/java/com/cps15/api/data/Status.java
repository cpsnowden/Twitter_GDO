package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public static enum STATUS{RUNNING, FINISHED, ERROR, ORDERED, STOPPED};

    private STATUS status;

    public Status() {};

    public Status(STATUS status) {
        this.status = status;
    }

    @JsonProperty
    public STATUS getStatus(){
        return status;
    }

    @JsonProperty
    public void setStatus(STATUS status) {
       this.status = status;
    }

}
