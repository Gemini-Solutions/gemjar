package com.gemini.generic.feature.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gemini.generic.base.provider.GemJARGlobalVar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.cucumber.docstring.DocString;

public class Variables {

    private final Map<String, String> variableMap;

    public Variables() {
        variableMap = new HashMap<String, String>();
        loadConfigData();
    }

    public void loadConfigData() {
        JsonObject configData = GemJarUtils.getConfigObject();
        Set<String> configParentKey = configData.keySet();
        String environment = GemJARGlobalVar.environment;
        variableMap.put("environment", environment);
        JsonElement envObject = configData.get(environment);
        if (envObject != null) {
            configParentKey.remove(environment);
            Set<String> envKeySet = envObject.getAsJsonObject().keySet();
            for (String envKey : envKeySet) {
                variableMap.put(envKey, GemJarUtils.convertJsonElementToString(envObject.getAsJsonObject().get(envKey)));
                configParentKey.remove(envKey);
            }
        }
        for (String remainingParentKey : configParentKey) {
            variableMap.put(remainingParentKey, GemJarUtils.convertJsonElementToString(configData.get(remainingParentKey)));
        }
    }


    public void enterNewDataORUpdate(String key, Object value) {
        if (value.getClass().getSimpleName().equals("String")) {
            variableMap.put(key, (String) value);
        } else if (value.getClass().getSimpleName().equals("DocString")) {
            variableMap.put(key, ((DocString) value).getContent());
        }
    }

    public String getVariableData(String key) {
        return variableMap.get(key);
    }


}
