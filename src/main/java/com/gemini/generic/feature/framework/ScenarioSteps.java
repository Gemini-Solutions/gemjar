package com.gemini.generic.feature.framework;

import com.gemini.generic.remote.invocation.Request;
import com.gemini.generic.remote.invocation.Response;
import com.gemini.generic.utils.GemUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.cucumber.java.en.Given;

public class ScenarioSteps {

    private final String RESPONSE = "response";
    private final String STATUS = "status";
    private final String RESPONSE_MESSAGE = "responseMessage";
    private final String ERROR_MESSAGE = "errorMessage";
    private final String EXEC_TIME = "execTime";
    Request request = new Request();
    Variables variables = new Variables();

    /*private String fixValue(String value) {
        JsonElement target = JsonParser.parseString(value);
        Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        if (value.contains("#")) {
            int startIndex = value.indexOf("#");
            int endIndex = value.substring(startIndex + 1).indexOf("#") + startIndex + 1;
            String key = value.substring(startIndex + 1, endIndex);
            if (key.contains(".")) {
                String[] keyHirerchy = key.split(".");
                for (int i = 0; i < keyHirerchy.length; i++) {
                    target = gson.fromJson(variables.getVariableData(keyHirerchy[i]), JsonElement.class);
                }

            } else {
                target = gson.fromJson(variables.getVariableData(key), JsonElement.class);

            }
        }
        return GemJarUtils.convertJsonElementToString(target);
    }*/
    private String fixValue(String value) {
        String target = "";
        Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        if (value.contains("#")) {
            int startIndex = value.indexOf("#");
            int endIndex = value.substring(startIndex + 1).indexOf("#") + startIndex + 1;
            String key = value.substring(startIndex + 1, endIndex);
            return variables.getVariableData(key);
        } else {
            return value;
        }
    }


    private void updateApiResponseInVariable(Response response) {
        variables.enterNewDataORUpdate(RESPONSE, response.getResponseBody());
        variables.enterNewDataORUpdate(STATUS, response.getStatus());
        variables.enterNewDataORUpdate(RESPONSE_MESSAGE, response.getResponseMessage());
        variables.enterNewDataORUpdate(ERROR_MESSAGE, response.getErrorMessage());
        variables.enterNewDataORUpdate(EXEC_TIME, response.getExecTime());
    }

    @Given("^baseUrl\\h(.+)")
    public void setBaseUrl(String baseUrl) {
        request.setBaseUrl(baseUrl);
    }

    @Given("^path\\h(.+)")
    public void setPath(String path) {
        request.setpath(path);
        request.setURL();
    }

    @Given("^params\\h([\\w]+)\\h=\\h(.+)$")
    public void setParameters(String key, String value) {
        request.setParameter(key, value);
        request.setURL();
    }

    @Given("^headers\\h([\\w]+)\\h=\\h(.+)$")
    public void setHeaders(String headersName, String headerValue) {
        request.setHeader(headersName, headerValue);
    }

    @Given("^method\\h(.+)")
    public void setMethodType(String methodType) {
        request.setMethod(methodType);
        Response response = GemUtils.invokeRequestMethod(request);
        updateApiResponseInVariable(response);
    }

    @Given("^Request\\h:\\h(.+)\\h:$")
    public void requestDocStringDataTable(String step, Object obj) {
        request.createRequest(obj);
        Response response = GemUtils.invokeRequestMethod(request);
        updateApiResponseInVariable(response);
    }

    @Given("^print\\h(.+)")
    public void printAnything(String value) {
        System.out.println(fixValue(value));
    }

    /*@Given("^Request\\h:\\h(.+)\\h:\\hreadfile\\((.+)\\)$")
    public void requestReadDataFromFile(String step,String filepath) {
        System.out.println(step);
        System.out.println(filepath);
    }

    @Given("^Request\\h:\\h(.+)\\h:\\h(\\{+.+\\})$")
    public void requestReadDataInline(String step,String requiredData ) {
        System.out.println(step);
        System.out.println(requiredData);
    }*/
    @Given("^Assert\\h:\\h(.+)\\h:$")
    public void assertUsingDocString(String assertStatement, String object) {
        System.out.println(assertStatement);
        System.out.println(object);
    }

    @Given("^Assert\\h:\\h(.+)\\h:\\h(.+)")
    public void inlineAssertion(String assertStep, String inlineAssertStatement) {
        System.out.println(assertStep);
        System.out.println(inlineAssertStatement);
    }

    @Given("^set\\h(.+)\\h=\\h(.+)")
    public void setKeyValueInline(String key, String value) {
        variables.enterNewDataORUpdate(key, value);
    }

    @Given("^set\\h(.+)\\h=$")
    public void setKeyValueFromObject(String key, Object object) {
        variables.enterNewDataORUpdate(key, object);
    }


}
