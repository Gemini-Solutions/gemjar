package com.gemini.automation.prism;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuitsDetails {

    @SerializedName("s_report_type")
    @Expose
    private String sReportType;

    @SerializedName("end")
    @Expose
    private String end;

    @SerializedName("app_name")
    @Expose
    private String appName;

    @SerializedName("s_end_time")
    @Expose
    private String sEndTime;

    @SerializedName("s_overall_status")
    @Expose
    private String sOverallStatus;

    @SerializedName("s_run_id")
    @Expose
    private String sRunId;

    @SerializedName("s_start_time")
    @Expose
    private long sStartTime;

    @SerializedName("Multi_Run_Details")
    @Expose
    private List<MultiRunDetail> multiRunDetails = new ArrayList<MultiRunDetail>();

    public SuitsDetails(){
        this.sStartTime = Instant.now().getEpochSecond();
        this.multiRunDetails.add(new MultiRunDetail());
        this.sReportType = "Regression";
        this.appName = "Test";
    }

    public SuitsDetails(String projectName, String suiteName, String env){
        this.sStartTime = Instant.now().getEpochSecond();
        this.multiRunDetails.add(new MultiRunDetail(env));
        this.sReportType = suiteName;
        this.appName = projectName;
    }

    public SuitsDetails(String s_run_id, String r_run_id, String projectName, String reportName, String env){
        this.sRunId = s_run_id;
        this.appName = projectName;
        this.sReportType = reportName;
        this.sStartTime = Instant.now().getEpochSecond();
    }

    public SuitsDetails(String sReportType, String end, String appName, String sEndTime, String sOverallStatus, String sRunId, List<ExecutionMetaData> executionMetaData, long sStartTime, List<MultiRunDetail> multiRunDetails){
        super();
        this.sReportType = sReportType;
        this.end = end;
        this.appName = appName;
        this.sEndTime = sEndTime;
        this.sOverallStatus = sOverallStatus;
        this.sRunId = sRunId;
        this.sStartTime = Instant.now().getEpochSecond();
        this.multiRunDetails = multiRunDetails;
    }

    public SuitsDetails(String projectName, String suiteName, String env, String rRunId, int expectedNoOfTestCases){
        //this.sRunId = UGTFapi.s_run_id;
        this.sStartTime = Instant.now().getEpochSecond();
        String quanticSuiteName = System.getProperty("QuanticReportName");
        this.sReportType = quanticSuiteName == null ? suiteName : quanticSuiteName;
        String quanticProjectName = System.getProperty("QuanticProjectName");
        this.appName = quanticProjectName == null ? projectName : quanticProjectName;
        this.multiRunDetails = new ArrayList<MultiRunDetail>();
        this.multiRunDetails.add(new MultiRunDetail(this.sRunId,env, rRunId,"exe", expectedNoOfTestCases));
    }

    public MultiRunDetail addNewMultiRunDetails(String env, String rRunId, int expectedNoOfTestCases) {
        this.multiRunDetails = new ArrayList<MultiRunDetail>();
        this.multiRunDetails.add(new MultiRunDetail(this.sRunId,env,rRunId,"exe",expectedNoOfTestCases));
        return this.multiRunDetails.get(0);
    }

    public String getSReportType(){
        return sReportType;
    }

    public void setSReportType(String sReportType){
        this.sReportType = sReportType;
    }

    public SuitsDetails withSReportType(String sReportType){
        this.sReportType = sReportType;
        return this;
    }

    public String getEnd(){
        return end;
    }

    public void setEnd(String end){
        this.end = end;
    }

    public SuitsDetails withEnd(String end) {
        this.end = end;
        return this;
    }

    public String getAppName(){
        return appName;
    }

    public void setAppName(String appName){
        this.appName = appName;
    }

    public SuitsDetails withAppName(String appName){
        this.appName = appName;
        return this;
    }

    public Object getSEndTime(){
        return sEndTime;
    }

    public void setSEndTime(String sEndTime){
        this.sEndTime = sEndTime;
    }

    public SuitsDetails withSEndTime(String sEndTime){
        this.sEndTime = sEndTime;
        return this;
    }

    public String getSOverallStatus(){
        return sOverallStatus;
    }

    public void setSOverallStatus(String sOverallStatus){
        this.sOverallStatus = sOverallStatus;
    }

    public SuitsDetails withSOverallStatus(String sOverallStatus){
        this.sOverallStatus = sOverallStatus;
        return this;
    }

    public String getSRunId(){
        return sRunId;
    }

    public void setSRunId(String sRunId){
        this.sRunId = sRunId;
    }

    public SuitsDetails withSRunId(String sRunId){
        this.sRunId = sRunId;
        return this;
    }

    public long getSStartTime(){
        return sStartTime;
    }

    public void setSStartTime(long sStartTime){
        this.sStartTime = sStartTime;
    }

    public SuitsDetails withSStartTime(long sStartTime){
        this.sStartTime = sStartTime;
        return this;
    }

    public List<MultiRunDetail> getMultiRunDetails(){
        return multiRunDetails;
    }

    public void setMultiRunDetails(List<MultiRunDetail> multiRunDetails){
        this.multiRunDetails = multiRunDetails;
    }

    public SuitsDetails withMultiRunDetails(List<MultiRunDetail> multiRunDetails){
        this.multiRunDetails = multiRunDetails;
        return this;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this).append("sReportType",sReportType).append("end",end).append("appName",appName).append("sEndTime",sEndTime).append("sOverallStatus",sOverallStatus).append("sRunId",sRunId).append("sStartTime",sStartTime).append("multiRunDetails",multiRunDetails).toString();
    }

    public void endSuiteExecution(){
        this.end = true +"";
        this.sEndTime = Instant.now().getEpochSecond()+"";
        this.multiRunDetails.get(0).setEnd("true");
        this.multiRunDetails.get(0).setRStatus(null);

    }

}
