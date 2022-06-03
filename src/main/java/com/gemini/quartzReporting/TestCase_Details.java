package com.gemini.quartzReporting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class TestCase_Details {

	private String tc_run_id;
	private long start_time;
	private long end_time;
	private String name;
	private String category;
	private String log_file;
	private String status;
	private String user;
	private String machine;
	private String result_file;
	private boolean ignore;

	public TestCase_Details(String testcaseName, String category, String user, boolean ignore) {
		this.tc_run_id = testcaseName + "_" + UUID.randomUUID();
		this.name = testcaseName;
		this.start_time = GemReportingUtility.getCurrentTimeInMilliSecond();
		this.category = category;
		this.user = user;
		this.machine = GemReportingUtility.getMachineName();
		this.ignore = ignore;

	}

	public String toString() {
		return "tc_run_id = " + this.tc_run_id + ", start_time = " + this.start_time + ", end_time = " + this.end_time
				+ ", name = " + this.name + ", category = " + this.category + ", log_file = " + this.log_file
				+ ", status = " + this.status;
	}

	public TestCase_Details(String testcaseName, String category, String user, String productType) {
		this(testcaseName, category, user, false);
	}

	public TestCase_Details(String testcaseName, String category, boolean ignore) {
		this(testcaseName, category, GemReportingUtility.getCurrentUserName(), false);
	}

	public TestCase_Details(String testcaseName, String category) {
		this(testcaseName, category, GemReportingUtility.getCurrentUserName(), false);
	}

	public void endTestCase() {
		this.end_time = GemReportingUtility.getCurrentTimeInMilliSecond();

	}

	public String getTc_run_id() {
		return tc_run_id;
	}

	public long getStart_time() {
		return start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getLog_file() {
		return log_file;
	}

	public String getStatus() {
		return status;
	}

	public String getUser() {
		return user;
	}

	public String getMachine() {
		return machine;
	}

	public String getResult_file() {
		return result_file;
	}


	public boolean getIgnore() {
		return ignore;
	}

	// Setter Methods

	public void setTc_run_id(String tc_run_id) {
		this.tc_run_id = tc_run_id;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setLog_file(String log_file) {
		this.log_file = log_file;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatus(JsonArray testCaseSteps) {
		Set<String> statusSet = new HashSet<String>();
		for (JsonElement testCaseStep : testCaseSteps) {
			String status = testCaseStep.getAsJsonObject().get("status").getAsString().toUpperCase();
			statusSet.add(status);
		}
		if (statusSet.contains("FAIL")) {
			this.status = "FAIL";
		} else if (statusSet.contains("WARN")) {
			this.status = "WARN";
		} else if (statusSet.contains("EXE")) {
			this.status = "EXE";
		} else {
			this.status = "PASS";
		}
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public void setResult_file(String result_file) {
		this.result_file = result_file;
	}


	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
}
