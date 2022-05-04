package com.gemini.automation.prism;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TCMiscMapping {

    @SerializedName("tc_run_id")
    @Expose
    private String tcRunId;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("key")
    @Expose
    private String key;

    /*
    No args constructor for use in serialization
     */

    public TCMiscMapping(){

    }

    /**
     * @param tcRunId
     * @param value
     * @param key
     */

    public TCMiscMapping(String tcRunId, String key, String value){
        super();
        this.tcRunId = tcRunId;
        this.key = key;
        this.value = value;
    }

    public String getTcRunId(){
        return tcRunId;
    }

    public void setTcRunId(String tcRunId){
        this.tcRunId=tcRunId;
    }

    public TCMiscMapping withTcRunId(String tcRunId){
        this.tcRunId = tcRunId;
        return this;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public TCMiscMapping withValue(String value){
        this.value = value;
        return this;
    }

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public TCMiscMapping withKey(String key){
        this.key = key;
        return this;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this).append("tcRunId",tcRunId).append("value",value).append("key",key).toString();
    }



}
