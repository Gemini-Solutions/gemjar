package com.gemini.automation.listners;

import com.gemini.automation.generic.QuanticGlobalVar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.json.Json;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class QuanticTestngTestFilter implements IMethodInterceptor {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        List<IMethodInstance> testCasesToRun = new ArrayList<IMethodInstance>();
        String data = null;
        if (QuanticGlobalVar.testCaseDataJsonPath != null) {
            try {
                data = new String(Files.readAllBytes(new File(QuanticGlobalVar.testCaseDataJsonPath).toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                data = IOUtils.toString(ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.testCaseFileName), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JsonElement jsonElement = gson.fromJson(data, JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (QuanticGlobalVar.testCasesToRun != null) {
            for (IMethodInstance iMethodInstance : methods) {
                String methodName = iMethodInstance.getMethod().getConstructorOrMethod().getMethod().getName().toString();
                JsonObject methodJson = jsonObject.get(methodName) != null ? jsonObject.get(methodName).getAsJsonObject() : null;
                if ((methodJson != null) && (methodJson.get("runFlag") != null)) {
                    if ((methodJson.get("runFlag").getAsString().equalsIgnoreCase("Y") &&
                            QuanticGlobalVar.testCasesToRun.contains(methodName))) {
                        testCasesToRun.add(iMethodInstance);
                    }
                }
            }
        } else {
            for (IMethodInstance iMethodInstance : methods) {
                String methodName = iMethodInstance.getMethod().getConstructorOrMethod().getMethod().getName().toString();
                JsonObject methodJson = jsonObject.get(methodName) != null ? jsonObject.get(methodName).getAsJsonObject() : null;
                if ((methodJson != null) && (methodJson.get("runFlag") != null)) {
                    if (methodJson.get("runFlag").getAsString().equalsIgnoreCase("Y")) {
                        testCasesToRun.add(iMethodInstance);
                    }
                }
            }
        }
        return testCasesToRun;
    }
}
