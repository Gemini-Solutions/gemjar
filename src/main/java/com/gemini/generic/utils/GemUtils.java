package com.gemini.generic.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;

import com.gemini.generic.remote.invocation.ApiRemoteInvocation;
import com.gemini.generic.remote.invocation.Request;
import com.gemini.generic.remote.invocation.Response;

public class GemUtils {

    private final static Logger logger = Logger.getLogger(GemUtils.class);

    public static String getDecryptedPwd(String encryptedPwd) {
        String decryptedPwd = "";
        for (int i = encryptedPwd.length() - 1; i >= 0; i--) {
            decryptedPwd += (char) ((int) encryptedPwd.charAt(i) - 1);
        }
        return decryptedPwd;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public static void writeDataToOutputStream(final OutputStream outputStream, final String jsonStringPayload) {
        OutputStream os = null;
        try {
            os = outputStream;
            os.write(jsonStringPayload.getBytes());

        } catch (Exception e) {
            logger.info("Exception Occured while Writing Data to Stream");
        } finally {
            try {
                os.close();
                os.flush();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                logger.info("Exception Occured while closing Stream");
                throw new RuntimeException(e);
            }
        }
    }
    public static String readPayLoad(Object requestPayload){
        StringBuilder payload = null;
        if(requestPayload instanceof  File){
            File requestPayLoadFile = (File)requestPayload;
            FileReader fr = null;
            payload = new StringBuilder();
            try {
                fr = new FileReader(requestPayLoadFile);
                int i;
                // Holds true till there is nothing to read
                while ((i = fr.read()) != -1) {
                    payload.append((char) i);
                }
            } catch (Exception e) {
                logger.info("Exception Occured while Reading Payload");
                return null;
            }finally {
                try {
                    fr.close();
                } catch (IOException e) {
                    logger.info("Exception Occured while closing Stream");
                    throw new RuntimeException(e);
                }
            }
        }else{
            payload = new StringBuilder((String)requestPayload);
        }
        return payload.toString();
    }

    public static String getDataFromBufferedReader(final InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        String output;
        if (inputStream != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            try {
                while ((output = br.readLine()) != null) {
                    builder.append(output);
                }
            } catch (IOException e) {
                logger.info("Exception Occured while getting Data From Buffered Reader");
                builder = new StringBuilder(e.getMessage());
            }finally {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.info("I/O Exception Occured while getting Data From Buffered Reader");
                    throw new RuntimeException(e);
                }
            }
            return builder.toString();
        } else {
            return null;
        }

    }

    public static Object genericInvokeMethod(Object obj, String methodName,
                                             Object... params) {
        int paramCount = params.length;
        Method method;
        Object requiredObj = null;
        Class<?>[] classArray = new Class<?>[paramCount];
        for (int i = 0; i < paramCount; i++) {
            classArray[i] = params[i].getClass();
        }
        try {
            method = obj.getClass().getDeclaredMethod(methodName, classArray);
            method.setAccessible(true);
            requiredObj = method.invoke(obj, params);
        } catch (NoSuchMethodException e) {
            logger.info("No Such Method Exception Occured invoking Method");
        } catch (IllegalArgumentException e) {
            logger.info("Exception Occured in Arguments Provided Reader");
        } catch (IllegalAccessException e) {
            logger.info("Illegal Access Exception");
        } catch (InvocationTargetException e) {
            logger.info("Inovation Target Exception");
        }

        return requiredObj;
    }

    public static Response invokeRequestMethod(Request request){
        Object obj = GemUtils.genericInvokeMethod(new ApiRemoteInvocation(), "executeRequest", request);
        if(null!=obj){
            return (Response) obj;
        }
        return  null;
    }
}
