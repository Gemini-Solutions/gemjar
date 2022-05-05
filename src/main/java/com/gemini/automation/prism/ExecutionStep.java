package com.gemini.automation.prism;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExecutionStep {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("steps")
    @Expose
    private String steps;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("run_id")
    @Expose
    private String runId;
    @SerializedName("test_step_misc")
    @Expose
    private ArrayList<TestStepMisc> test_step_misc;

    public ExecutionStep() {
        this.status = "EXE";
        this.steps = "Test Step";
        this.description = "Test Description";
    }

    public ExecutionStep(String status, String steps, String description, String runId) {
        super();
        this.status = status;
        this.steps = steps;
        this.description = description;
        this.runId = runId;
    }

    public ExecutionStep(String steps, String description, String status) {
        this.steps = steps;
        this.description = description;
        this.status = status;
        TestStepMisc testStepMisc = new TestStepMisc("Execution Time", Instant.now() + "");
        test_step_misc = new ArrayList<TestStepMisc>();
        test_step_misc.add(testStepMisc);
    }

    public ExecutionStep(String steps, String description, String status, String miscKey, String miscValue) {
        this.steps = steps;
        this.description = description;
        this.status = status;
        test_step_misc = new ArrayList<TestStepMisc>();
        TestStepMisc userTestStepMisc = new TestStepMisc(miscKey, miscValue);
        test_step_misc.add(userTestStepMisc);
    }

    public ExecutionStep(String steps, String description, String status, Map<String, String> testStepMiscMap) {
        this.steps = steps;
        this.description = description;
        this.status = status;
        test_step_misc = new ArrayList<TestStepMisc>();
        TestStepMisc testStepMisc = new TestStepMisc("Execution Time", Instant.now() + "");
        test_step_misc.add(testStepMisc);
        Set<String> miscKeySet = testStepMiscMap.keySet();
        for (String miscKey : miscKeySet) {
            TestStepMisc userTestStepMisc = new TestStepMisc(miscKey, testStepMiscMap.get(miscKey));
            test_step_misc.add(userTestStepMisc);
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ExecutionStep withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public ExecutionStep withSteps(String steps) {
        this.steps = steps;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExecutionStep withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public ExecutionStep withRunId(String runId) {
        this.runId = runId;
        return this;
    }

//    public ArrayList<TestStepMisc> getTest_step_misc() {
//        return test_step_misc;
//    }
//
//    public void setTest_step_misc(ArrayList<TestStepMisc> test_step_misc) {
//        this.test_step_misc = test_step_misc;
//    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("steps", steps).append("description", description).append("runId", runId).toString();
    }

}
