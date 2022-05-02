package com.gemini.automation.dataProvider;

import com.gemini.automation.generic.QuanticGlobalVar;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class QuanticDataProvider {
    @DataProvider(name = "QuanticDataProvider")
    public static Object[][] QuanticTestDataProvider(ITestNGMethod testNGMethod) {
        try {
            String methodName = testNGMethod.getMethodName();
            String data;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            if (QuanticGlobalVar.testCaseDataJsonPath != null) {
                data = new String(Files.readAllBytes(new File(QuanticGlobalVar.testCaseDataJsonPath).toPath()));
            } else {
                data = IOUtils.toString(ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.testCaseFileName), StandardCharsets.UTF_8);
            }
            JsonElement jsonElement = gson.fromJson(data, JsonElement.class);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement methodData = jsonObject.get(methodName).getAsJsonObject().get("inputData");
            if (methodData.isJsonArray()) {
                JsonArray methodDataArray = methodData.getAsJsonArray();
                int methodDataSize = methodDataArray.size();
                Object[][] obj = new Object[methodDataSize][1];
                for (int i = 0; i < methodDataSize; i++) {
                    obj[i][0] = methodDataArray.get(i).getAsJsonObject();
                }
                return obj;
            } else {
                return new Object[][]{{methodData}};
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }

    }
}
