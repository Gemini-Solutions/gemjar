package com.gemini.automation.prism;

import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestCaseDetail {

    @SerializedName("tc_run_id")
    @Expose
    private String tcRunId;

    @SerializedName("r_run_id")
    @Expose
    private String rRunId;

    @SerializedName("s_run_id")
    @Expose
    private String sRunId;

    @SerializedName("tc_name")
    @Expose
    private String tcName;

    @SerializedName("tc_category")
    @Expose
    private String tcCategory;

    @SerializedName("tc_status")
    @Expose
    private String tcStatus;

    @SerializedName("tc_machine")
    @Expose
    private String tcMachine;

    @SerializedName("tc_user")
    @Expose
    private String tcUser = System.getProperty("user.name");

    @SerializedName("tc_start_time")
    @Expose
    private long tcStartTime;

    @SerializedName("tc_end_time")
    @Expose
    private long tcEndTime;

    @SerializedName("tc_result_file")
    @Expose
    private String tcResultFile;

    @SerializedName("tc_log_file")
    @Expose
    private String tcLogFile;

    @SerializedName("tc_misc_details")
    @Expose
    private String tcMiscDetails;

    @SerializedName("product_type")
    @Expose
    private String productType;

    @SerializedName("TC_Misc_Mapping")
    @Expose
    private List<TCMiscMapping> tCMiscMapping = new ArrayList<TCMiscMapping>();

    @SerializedName("Execution_Steps")
    @Expose
    private List<ExecutionStep> executionSteps;

    @SerializedName("Execution_MetaData")
    @Expose
    private List<ExecutionMetaData> executionMetaData;


    /**
     * No args constructor for use in serialization
     */

    public TestCaseDetail() {
        this.executionSteps = new ArrayList<ExecutionStep>();
        this.tcStartTime = Instant.now().getEpochSecond();
        this.tcUser = System.getProperty("user.name");
        this.tcStatus = "FINISH";
        this.tcEndTime = Instant.now().getEpochSecond();
        this.tcMiscDetails = "priority:Medium;content_type;JSON";

        this.executionSteps.add(new ExecutionStep());
        try {
            this.tcMachine = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            this.tcMachine = "TestMachine";
        }

        this.tcName = "Testcase";
        this.tcCategory = "test case category";
        this.productType = "TSS";
    }

    public TestCaseDetail(String tcCategory, String tcUser, List<ExecutionStep> executionSteps, String tcMiscDetails, String tcStatus, long tcEndTime, List<TCMiscMapping> tCMiscMapping, String sRunId, String tcResultFile, String tcLogFile, List<ExecutionMetaData> executionMetaData, String tcRunId, String productType, long tcStartTime, String rRunId, String tcName, String tcMachine) {
        super();
        this.tcCategory = tcCategory;
        this.tcUser = tcUser;
        this.executionSteps = executionSteps;
        this.tcMiscDetails = tcMiscDetails;
        this.tcStatus = tcStatus;
        this.tcEndTime = tcEndTime;
        this.tCMiscMapping = tCMiscMapping;
        this.sRunId = sRunId;
        this.tcResultFile = tcResultFile;
        this.tcLogFile = tcLogFile;
        this.tcRunId = tcRunId;
        this.productType = productType;
        this.tcStartTime = tcStartTime;
        this.rRunId = rRunId;
        this.tcName = tcName;
        this.tcMachine = tcMachine;
    }

    public TestCaseDetail(String productType, String testcaseName, List<ExecutionStep> executionSteps, String status, String resultLocation, long tc_start_time, long tc_end_time) {
        this.tcStartTime = (tc_start_time / 1000);
        this.tcEndTime = (tc_end_time / 1000);
        this.tcUser = System.getProperty("user.name");
        this.tcStatus = status;
        this.tcMiscDetails = "priority:Medium;content_type:JSON";
        this.executionSteps = executionSteps;
        try {
            this.tcMachine = InetAddress.getLocalHost().getHostName();
            ;
        } catch (Exception e) {
            this.tcMachine = "TestMachine";
        }
        this.tcName = testcaseName;
        this.tcCategory = testcaseName;
        this.productType = productType;
        this.tcResultFile = resultLocation;
    }

    public TestCaseDetail(String productType, String testcaseName, String testcaseCategory, List<ExecutionStep> executionSteps, String status, String resultLocation, long tc_start_time, long tc_end_time) {
        this.tcStartTime = (tc_start_time / 1000);
        this.tcEndTime = (tc_end_time / 1000);
        this.tcUser = System.getProperty("user.name");
        this.tcStatus = status;
        this.tcMiscDetails = "priority:Medium;content_type:JSON";
        this.executionSteps = executionSteps;
        try {
            this.tcMachine = InetAddress.getLocalHost().getHostName();
            ;
        } catch (Exception e) {
            this.tcMachine = "TestMachine";
        }
        this.tcName = testcaseName;
        this.tcCategory = testcaseName;
        this.productType = productType;
        this.tcResultFile = resultLocation;

    }
    public TestCaseDetail(String testcaseName, String testcaseCategory){
        this.tcName = testcaseName;
        this.tcCategory = testcaseCategory;
        this.tcStartTime = System.currentTimeMillis()/1000;
        this.productType = "Quantic";
        this.executionSteps = new ArrayList<ExecutionStep>();
        try{
            this.tcMachine = InetAddress.getLocalHost().getHostName();
            ;
        } catch (Exception e) {
            this.tcMachine = "TestMachine";
        }
    }

    public void addExecutionStep(String steps, String description, String status){
        ExecutionStep executionStep = new ExecutionStep(steps, description, status);
        this.executionSteps.add(executionStep);
    }

    public void addExecutionStep(String steps, String description, String status, String miscKey, String miscValue){
        ExecutionStep executionStep = new ExecutionStep(steps, description, status, miscKey, miscValue);
        this.executionSteps.add(executionStep);
    }

    public void addExecutionStep(String steps, String description, String status, Map<String,String> miscMap){
        ExecutionStep executionStep = new ExecutionStep(steps, description, status, miscMap);
        this.executionSteps.add(executionStep);
    }

    public void addTestCaseMiscellaneousData(String key, String value){
       TCMiscMapping tcMiscMapping = new TCMiscMapping(this.rRunId, key, value);
       this.tCMiscMapping.add(tcMiscMapping);
    }

    public void addExecutionMetaData(String tableType, String key, String value){
        ExecutionMetaData executionMetadata = new ExecutionMetaData(tableType,value,key, this.tcRunId);
        this.executionMetaData.add(executionMetadata);
    }

    public void submitTestCase(){
        this.tcEndTime = System.currentTimeMillis()/1000;
        this.tcStatus = "Pass";
        for(ExecutionStep executionStep : this.executionSteps){
            if (executionStep.getStatus().equalsIgnoreCase("fail")){
                this.tcStatus = "Fail";
            }
        }
    }

    public List<ExecutionStep> getExecutionSteps(){
        return executionSteps;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this).append("tcCategory",tcCategory).append("tcUser",tcUser).append("executionSteps",executionSteps).append("tcMiscDetails",tcMiscDetails).append("tcStatus",tcStatus).append("tcEndTime",tcEndTime).append("tcMiscMapping",tCMiscMapping).append("sRunId",sRunId).append("tcResultFile",tcResultFile).append("tcLogFile",tcLogFile).append("executionMetaData",executionMetaData).append("tcRunId",tcRunId)
                .append("productType",productType).append("tcStartTime",tcStartTime).append("rRunId",rRunId).append("tcName",tcName).append("tcMachine",tcMachine).toString();
    }

}

