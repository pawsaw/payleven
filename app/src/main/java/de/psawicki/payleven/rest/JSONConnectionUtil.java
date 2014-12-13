/*
 * Copyright (c) 2013 Pawel Sawicki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.psawicki.payleven.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Pawel Sawicki
 */
public class JSONConnectionUtil {
	
	private static final String JSON_CONTENT_TYPE = "application/json;charset=utf-8";
    
    private JSONConnectionUtil() {
    }
    
    public static JSONObject getJSON(String url) throws JSONConnectionException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(new HttpGet(url));
        return getResponseAsJSON(response);
    }
    
    public static JSONObject getJSON(String hostname, int port, String username, String password, String url) throws JSONConnectionException, IOException {
        HttpParams params = new BasicHttpParams();
        params.setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        DefaultHttpClient httpClient = new DefaultHttpClient(params);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(hostname, port), credentials);
        HttpGet request = new HttpGet(url);
        request.addHeader(BasicScheme.authenticate(credentials, "US-ASCII", false));
        HttpResponse response = httpClient.execute(request);
        return getResponseAsJSON(response);
    }
    
    public static JSONObject postJSON(String url, JSONObject json) throws JSONConnectionException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", JSONConnectionUtil.JSON_CONTENT_TYPE);
        httpPost.setHeader("Content-Type", JSONConnectionUtil.JSON_CONTENT_TYPE);
        HttpEntity requestEntity = null;
        try {
            requestEntity = new StringEntity(json.toString(), "UTF-8");
            httpPost.setEntity(requestEntity);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(JSONConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpResponse response = httpClient.execute(httpPost);
        return getResponseAsJSON(response);
    }
    
    public static JSONObject postJSON(String hostname, int port, String username, String password, String url, JSONObject json) throws JSONConnectionException, IOException {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        DefaultHttpClient httpClient = new DefaultHttpClient(params);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(hostname, port), credentials);
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(BasicScheme.authenticate(credentials, "US-ASCII", false));
        httpPost.setHeader("Accept", JSONConnectionUtil.JSON_CONTENT_TYPE);
        httpPost.setHeader("Content-Type", JSONConnectionUtil.JSON_CONTENT_TYPE);
        HttpEntity requestEntity = null;
        try {
            requestEntity = new StringEntity(json.toString(), "UTF-8");
            httpPost.setEntity(requestEntity);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(JSONConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpResponse response = httpClient.execute(httpPost);
        return getResponseAsJSON(response);
    }
    
    @SuppressWarnings("finally")
	private static JSONObject getResponseAsJSON(HttpResponse response) throws JSONConnectionException, IOException {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
        	String message = statusLine.getReasonPhrase();
        	JSONConnectionException exceptionToThrow = null;
            try {
            	exceptionToThrow = new JSONConnectionException(new JSONObject(message));
			} catch (JSONException e) {
				exceptionToThrow = new JSONConnectionException(JSONConnectionException.Error.UNKNOWN, message);
			} finally {
				throw exceptionToThrow;
			}
        }
        
        HttpEntity entity = response.getEntity();
        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
        StringBuilder resultText = new StringBuilder("");
        String line = null;
        while ((line = br.readLine()) != null) {
            resultText.append(line).append("\n");
        }
        
        br.close();
        
        JSONObject resultJSON = null;
        try {
			resultJSON = new JSONObject(resultText.toString());
		} catch (JSONException e) {
			throw new JSONConnectionException(JSONConnectionException.Error.MALFORMED_JSON, "Malformed JSON: " + resultText.toString());
		}
		return resultJSON;
    }
}
