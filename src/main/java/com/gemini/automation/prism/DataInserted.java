package com.gemini.automation.prism;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataInserted {

    @SerializedName("Suite_Details")
    @Expose
    private SuitsDetails suiteDetails;

    public DataInserted() {
        this.suiteDetails = new SuitsDetails();
    }

    public DataInserted(String s_run_id, String r_run_id, String projectName, String reportName, String env) {
        this.suiteDetails = new SuitsDetails();
    }

    public DataInserted(SuitsDetails suitsDetails) {
        super();
        this.suiteDetails = suitsDetails;
    }

    public DataInserted(String projectName, String suiteName, String env, String r_run_id, int expectedNoTestCases) {
        this.suiteDetails = new SuitsDetails(projectName, suiteName, env, r_run_id, expectedNoTestCases);
    }

    public void endSuiteExecution() {
        this.suiteDetails.endSuiteExecution();
    }

    public DataInserted(String projName, String suiteName, String env) {
        this.suiteDetails = new SuitsDetails(projName, suiteName, env);
    }

    public SuitsDetails getSuiteDetails() {
        return suiteDetails;
    }

    public void setSuitsDetails(SuitsDetails suiteDetails) {
        this.suiteDetails = suiteDetails;
    }

    public DataInserted withSuitsDetails(SuitsDetails suiteDetails) {
        this.suiteDetails = suiteDetails;
        return this;
    }
}

