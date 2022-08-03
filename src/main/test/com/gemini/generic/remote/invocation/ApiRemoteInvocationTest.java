package com.gemini.generic.remote.invocation;

import org.junit.Test;

import com.gemini.generic.feature.framework.Assert;

public class ApiRemoteInvocationTest {
	
	public Assert assertGem = new Assert();
	
	@Test
	public void testhandleRequestGet() throws Exception {
		
		Request request = new Request(null, "Get", "https://www.google.com", null,null,null);
		Response response =ApiRemoteInvocation.handleRequest(request);
		int statusCode = response.getStatus();
		assertGem.assertEquals(String.valueOf(statusCode),String.valueOf(200));
	}
	
	@Test
	public void testhandleRequestPut() throws Exception {
		
		String requestPayLoad="{\r\n"
				+ "    \"name\": \"morpheus\",\r\n"
				+ "    \"job\": \"zion resident\"\r\n"
				+ "}";
		Request request = new Request(null, "Put", "https://reqres.in/api/users/2", requestPayLoad,null,null);
		Response response =ApiRemoteInvocation.handleRequest(request);
		int statusCode = response.getStatus();
		assertGem.assertEquals(String.valueOf(statusCode),String.valueOf(200));
	}
	
	@Test
	public void testhandleRequestPost() throws Exception {
		
		String requestPayLoad="{\r\n"
				+ "\r\n"
				+ "    \"name\": \"Raghav-Hem\",\r\n"
				+ "\r\n"
				+ "    \"job\": \"zion resident\"\r\n"
				+ "\r\n"
				+ "}";
		Request request = new Request(null, "Post", "https://reqres.in/api/users", requestPayLoad,null,null);
		Response response =ApiRemoteInvocation.handleRequest(request);
		int statusCode = response.getStatus();
		assertGem.assertEquals(String.valueOf(statusCode),String.valueOf(201));
	}

}
