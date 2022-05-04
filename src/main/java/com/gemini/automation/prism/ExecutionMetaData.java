package com.gemini.automation.prism;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExecutionMetaData {


    @SerializedName("table_type")
    @Expose
    private String tableType;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("run_id")
    @Expose
    private String runId;

    public ExecutionMetaData() {
        this.tableType = "Suite";
        this.value = "abcde";
        this.key = "abcd";
        this.runId = "v2";
    }

    public ExecutionMetaData(String tableType, String key, String value, String runId) {
        super();
        this.tableType = tableType;
        this.key = key;
        this.value = value;
        this.runId = runId;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public ExecutionMetaData withTableType(String tableType) {
        this.tableType = tableType;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ExecutionMetaData withValue(String value) {
        this.value = value;
        return this;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ExecutionMetaData withKey(String key) {
        this.key = key;
        return this;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public ExecutionMetaData withRunId(String runId) {
        this.runId = runId;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("tableType", tableType).append("value", value).append("key", key).append("runId", runId).toString();

    }

}
