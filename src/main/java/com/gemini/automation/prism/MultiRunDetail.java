package com.gemini.automation.prism;

import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultiRunDetail {
    @SerializedName("r_machine")
    @Expose
    private String rMachine;

    public String getEnd(){
        return end;
    }

    public void setEnd(String end){
        this.end = end;
    }

    @SerializedName("end")
    @Expose
    private String end;

    @SerializedName("run_type")
    @Expose
    private String runType;

    ////////////////////////////////////////// Automation Details ///////////////////////////////
    @SerializedName("auto_env")
    @Expose
    private String autoEnv;

    @SerializedName("auto_runnum")
    @Expose
    private String autoRunnum;

    @SerializedName("auto_jobname")
    @Expose
    private String autoJobname;

    //-------------------------------------------------------------------------------------------
    @SerializedName("r_end_time")
    @Expose
    private String rEndTime;

    @SerializedName("r_status")
    @Expose
    private String rStatus;

    @SerializedName("r_run_id")
    @Expose
    private String rRunId;

    @SerializedName("s_run_id")
    @Expose
    private String sRunId;

    @SerializedName("r_envt")
    @Expose
    private String rEnvt;

    @SerializedName("r_start_time")
    @Expose
    private long rStartTime;

    /*
    * @SerializedName("Excecution_MetaData")
    * @Expose
    * private List<ExecutionMetaData> executionMetaData = new ArrayList<>();
     */

    @SerializedName("TestCase_Details")
    @Expose
    private List<TestCaseDetail> testCaseDetails = new ArrayList<TestCaseDetail>();

    @SerializedName("r_user")
    @Expose
    private String rUser;
   /////////////////////////////////////////////// Autosys Details //////////////////////////////

    @SerializedName("autosys_job_name")
    @Expose
    private String autosysJobName;

    @SerializedName("autosys_env")
    @Expose
    private String autosysEnv;

    @SerializedName("autosys_runnum")
    @Expose
    private String autosysRunnum;

    ///////////////////////////////// Person Details /////////////////////////////////////////

    @SerializedName("exp_testcases")
    @Expose
    private int expectedNumberOfTestCases;

    /**
     * No args constructor for use in serialization
     */

    public MultiRunDetail(){
        try{
            this.rMachine = InetAddress.getLocalHost().getHostName();
        } catch (Exception e){
            this.rMachine = "TestMachine";
        }
        this.runType = "ON DEMAND";
        this.rStartTime = Instant.now().getEpochSecond();
        this.rUser = System.getProperty("user.name");
        this.rStatus = "EXE";

        // this.executionMetaData.add(new ExecutionMetaData());
        this.testCaseDetails.add(new TestCaseDetail());
        this.rEnvt = "BETA";
    }


    /**
     *
     *
     */

    public MultiRunDetail(String rMachine, String runType, String autosysRunnum, String autoEnv, String rEndTime,
                          String rStatus, String rRunId, String sRunId, String rEnvt, String autoRunnum, long rStartTime,
                          List<ExecutionMetaData> executionMetaData, List<TestCaseDetail> testCaseDetails, String autosysEnv,
                          String autoJobname, String rUser, String autosysJobName){
        super();
        this.rMachine = rMachine;
        this.runType = runType;
        this.autosysRunnum = autosysRunnum;
        this.autoEnv = autoEnv;
        this.rEndTime = rEndTime;
        this.rStatus = rStatus;
        this.rRunId = rRunId;
        this.sRunId = sRunId;
        this.rEnvt = rEnvt;
        this.autoRunnum = autoRunnum;
        this.rStartTime = rStartTime;
        //this.executionMetaData = executionMetaData;
        this.testCaseDetails = testCaseDetails;
        this.autosysEnv = autosysEnv;
        this.autoJobname = autoJobname;
        this.rUser = rUser;
        this.autosysJobName = autosysJobName;

    }

    public MultiRunDetail(String env){
        try{
            this.rMachine = InetAddress.getLocalHost().getHostName();
        } catch (Exception e){
            this.rMachine = "TestMachine";
        }
        this.runType = "ON DEMAND";
        this.rStartTime = Instant.now().getEpochSecond();
        this.rUser = System.getProperty("user.name");

        //this.rStatus = "EXE";
        //this.executionMetaData.add(new ExecutionMetaData());
        this.testCaseDetails.add(new TestCaseDetail());
        this.rEnvt = env;
    }

    public void addTestcaseDetail(TestCaseDetail testcase){
        this.testCaseDetails.add(testcase);
    }

    public void setRStatus(String rStatus){
        this.rStatus = rStatus;
    }

    public MultiRunDetail withRStatus(String rStatus){
        this.rStatus = rStatus;
        return this;
    }

    public String getRRunId(){
        return rRunId;
    }

    public void setRRunId(String rRunId){
        this.rRunId = rRunId;
    }

    public MultiRunDetail withRRunId(String rRunId){
        this.rRunId = rRunId;
        return this;
    }

    public String getSRunId(){
        return sRunId;
    }

    public void setSRunId(String sRunId){
        this.sRunId = sRunId;
    }

    public MultiRunDetail withSRunId(String sRunId){
        this.sRunId = sRunId;
        return this;
    }

    public String getREnvt(){
        return rEnvt;
    }

    public void setREnvt(String rEnvt){
        this.rEnvt = rEnvt;
    }

    public MultiRunDetail withREnvt(String rEnvt){
        this.rEnvt = rEnvt;
        return this;
    }

    public String getAutoRunnum(){
        return autoRunnum;
    }

    public MultiRunDetail withAutoRunnum(String autoRunnum){
        this.autoRunnum = autoRunnum;
        return this;
    }

    public long getRStartTime(){
        return rStartTime;
    }

    public void setRStartTime(long rStartTime){
        this.rStartTime = rStartTime;
    }

    public MultiRunDetail withRStartTime(long rStartTime){
        this.rStartTime = rStartTime;
        return this;
    }

    public List<TestCaseDetail> getTestCaseDetails(){
        return testCaseDetails;
    }

    public void setTestCaseDetails(List<TestCaseDetail> testCaseDetails){
        this.testCaseDetails = testCaseDetails;
    }

    public MultiRunDetail withTestCaseDetails(List<TestCaseDetail> testCaseDetails){
        this.testCaseDetails = testCaseDetails;
        return this;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this).append("rMachine",rMachine).append("runType",runType)
                .append("autosysRunnum",autosysRunnum).append("autoEnv",autoEnv).append("rEndTime",rEndTime)
                .append("rStatus",rStatus).append("rRunId",rRunId).append("sRunId",sRunId).append("rEnvt",rEnvt)
                .append("autoRunnum",autoRunnum).append("rStartTime",rStartTime)
                .append("testCaseDetails",testCaseDetails).append("autosysEnv",autosysEnv)
                .append("autoJobname",autoJobname).append("rUser",rUser).append("autosysJobName",autosysJobName)
                .toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public MultiRunDetail(String sRunId, String runEnv, String rRunId, String rStatus, int expectedNumberOfTestCases){
        this.rStartTime = Instant.now().getEpochSecond();
        this.rEnvt = runEnv;
        this.rRunId = rRunId;
        this.rStatus = rStatus;
        this.rUser = System.getProperty("user.name");

        try{
            this.rMachine = InetAddress.getLocalHost().getHostName();
        } catch (Exception e){
            this.rMachine = "TestMachine";
        }
        this.runType = "ON DEMAND";
        this.expectedNumberOfTestCases = expectedNumberOfTestCases;
        this.end = false+"";
        this.testCaseDetails = new ArrayList<TestCaseDetail>();
        this.sRunId = sRunId;
    }





}
