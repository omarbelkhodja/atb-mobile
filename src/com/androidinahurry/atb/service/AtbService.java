/*
 * Copyright (C) 2013 Omar BELKHODJA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidinahurry.atb.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.htmlcleaner.XPatherException;

import android.util.Log;

import com.androidinahurry.atb.model.Account;
import com.androidinahurry.atb.model.Transaction;
import com.androidinahurry.network.utils.FakeSSLSocketFactory;

public class AtbService {
	
	final static String BASE_URL = "https://www.atbnet.com.tn";
	final static String HOME_PAGE = "/home.asp";
	final static String ACCOUNT_BALANCE_PAGE = "/mesComptes/MesPositions/Solde_Operation.asp";
	final static String ACCOUNT_STATEMENT_PAGE = "/mesComptes/MesPositions/extret_compte.asp";
	final static String ACCOUNT_HISTORY_PAGE = "/mesComptes/MesPositions/historique_ope.asp";
	final static String LOGOUT_PAGE = "/message/connect_stop.asp";
	
	private static class AtbContext {
		HttpClient mHttpClient;
		HttpContext mHttpContext;
		String mUser;
		String mPassword;

		public AtbContext(String user, String password) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
			mUser = user;
			mPassword = password;
			
			mHttpClient = createHttpClient();
			mHttpContext = createHttpContext();
		}
		
		public HttpClient getHttpClient() {
			return mHttpClient;
		}

		public HttpContext getHttpContext() {
			return mHttpContext;
		}

		public String getUser() {
			return mUser;
		}

		public String getPassword() {
			return mPassword;
		}

		private static HttpClient createHttpClient() throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
			// Setup a custom SSL Factory object which simply ignore the certificates
			// validation and accept all type of self signed certificates
			SSLSocketFactory sslFactory = new FakeSSLSocketFactory(null);
			sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			 
			// Enable HTTP parameters
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			 
			// Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sslFactory, 443));
			 
			// Create a new connection manager using the newly created registry and then create a new HTTP client
			// using this connection manager
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
			return new DefaultHttpClient(ccm, params);
		}
		
		private static HttpContext createHttpContext() {
			CookieStore cookieStore = new BasicCookieStore();

		    HttpContext localContext = new BasicHttpContext();
		    localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		    
		    return localContext;
		}
	}

	private static String getSessionId(List<NameValuePair> loginParams) {
		for (NameValuePair param : loginParams) {
			if(param.getName().equals("txtIdsess")) {
				return param.getValue();
			}
		}
		return null;
	}

	private static HttpResponse doPost(AtbContext context, String url, List<NameValuePair> formParams) throws KeyManagementException, UnrecoverableKeyException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException {
		String URL = BASE_URL + url;

		// Create the entity for the Http Post
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, HTTP.ISO_8859_1);
		HttpPost httpPost = new HttpPost(URL);
		httpPost.setEntity(entity);

		// Execute the HTTP request
		return context.getHttpClient().execute(httpPost, context.getHttpContext());
	}
	
	private static HttpResponse doGet(AtbContext context, String url) throws KeyManagementException, UnrecoverableKeyException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException {
		HttpGet httpGet = new HttpGet(url);
		
		return context.getHttpClient().execute(httpGet, context.getHttpContext());
	}
	
	private static List<NameValuePair> getLoginParams(AtbContext context, String accesPage) throws KeyManagementException, UnrecoverableKeyException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException, AuthenticationFailureException {
		// Create the query parameters
		List<NameValuePair> homePageParams = new ArrayList<NameValuePair>();
		homePageParams.add(new BasicNameValuePair("txtIdsess", "Oudkhil"));
		homePageParams.add(new BasicNameValuePair("txtLogin", context.getUser()));
		homePageParams.add(new BasicNameValuePair("txtPassw", context.getPassword()));
		homePageParams.add(new BasicNameValuePair("cmbAccpag", accesPage));
		// Send the Http request
		HttpResponse homeResponse = doPost(context, HOME_PAGE, homePageParams);
		// Parse the HTML page, and extract the usefull parameters
		return HtmlParser.parseHomePage(homeResponse);
	}
	
	public static boolean login(String user, String password) throws KeyManagementException, UnrecoverableKeyException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException {
		AtbContext context = new AtbContext(user, password);
		boolean isLoginSuccessfull;

		try {
			// Login and extract the login params
			List<NameValuePair> loginParams;
			loginParams = getLoginParams(context, ACCOUNT_BALANCE_PAGE);
			// Logout
			logout(context, getSessionId(loginParams));
			
			isLoginSuccessfull = true;
		} catch (AuthenticationFailureException e) {
			isLoginSuccessfull = false;
		}
		
		return isLoginSuccessfull;
	}
	
	private static void logout(AtbContext context, String sessionId) throws KeyManagementException, UnrecoverableKeyException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException {
		String url = String.format("%s%s?CodMes=%s&txtIdsess=%s", BASE_URL, LOGOUT_PAGE, "340103" , sessionId);
		
		doGet(context, url);
	}

	public synchronized static Account getAccount(String user, String password) throws KeyManagementException, UnrecoverableKeyException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException, XPatherException, AuthenticationFailureException, ParseException {
		Log.i("service", "getAccount()");
		AtbContext context = new AtbContext(user, password);
		
		// Login and extract the login params
		List<NameValuePair> loginParams = getLoginParams(context, ACCOUNT_BALANCE_PAGE);
		// Create the query parameters for the Account Statement page
		loginParams.add(new BasicNameValuePair("txtNatuse", "HNI"));
		loginParams.add(new BasicNameValuePair("txtRefere", "FIRST_LOG"));
		// Send the Http request
		HttpResponse soldeResponse = doPost(context, ACCOUNT_BALANCE_PAGE, loginParams);
		Account account = HtmlParser.parseSoldePage(soldeResponse);
		// Logout
		logout(context, getSessionId(loginParams));
		
		Log.i("service", "getAccount() => OK");
		return account;
	}
	
	public synchronized static List<Transaction> getHistory(String user, String password, Date start, Date stop) throws KeyManagementException, UnrecoverableKeyException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException, IllegalStateException, XPatherException, ParseException, AuthenticationFailureException {
		Log.i("service", "getHistory()");
		
		AtbContext context = new AtbContext(user, password);
		// Login and extract the login params
		List<NameValuePair> loginParams = getLoginParams(context, ACCOUNT_STATEMENT_PAGE);
		// Create the query parameters for the History page
		loginParams.add(new BasicNameValuePair("txtNatuse", "HNI"));
		loginParams.add(new BasicNameValuePair("txtRefere", "FIRST_LOG"));
		// Send the Http request
		HttpResponse soldeResponse = doPost(context, ACCOUNT_STATEMENT_PAGE, loginParams);
		List<NameValuePair> statementParams = HtmlParser.parseStatementPage(soldeResponse);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String startDateFormatted = formatter.format(start);
		String stopDateFormatted = formatter.format(stop);
		
		statementParams.addAll(loginParams);
		statementParams.add(new BasicNameValuePair("txtPeriod", "DF"));
		statementParams.add(new BasicNameValuePair("txtDatdeb", startDateFormatted));
		statementParams.add(new BasicNameValuePair("txtDatfin", stopDateFormatted));
		statementParams.add(new BasicNameValuePair("canClick", "1"));
		statementParams.add(new BasicNameValuePair("txtclickSorting", "0"));
		statementParams.add(new BasicNameValuePair("txtpremiersubmit", "0"));
		statementParams.add(new BasicNameValuePair("txtNbRecTot", "2"));
		statementParams.add(new BasicNameValuePair("txtRowStart", "1"));
		statementParams.add(new BasicNameValuePair("txtIndice", "1"));
		statementParams.add(new BasicNameValuePair("txtSortse", "concat(substring(@DATOPE, 7, 4), substring(@DATOPE, 4, 2),substring(@DATOPE, 1, 2))"));
		statementParams.add(new BasicNameValuePair("txtSortyp", "number"));
		statementParams.add(new BasicNameValuePair("txtSorsen", "descending"));
		statementParams.add(new BasicNameValuePair("txtDatfinAFB", stopDateFormatted));
		statementParams.add(new BasicNameValuePair("cmbDay1", startDateFormatted.substring(0, 2)));
		statementParams.add(new BasicNameValuePair("cmbMonth1", startDateFormatted.substring(3, 5)));
		statementParams.add(new BasicNameValuePair("cmbYear1", startDateFormatted.substring(6, 10)));
		statementParams.add(new BasicNameValuePair("cmbDay2", stopDateFormatted.substring(0, 2)));
		statementParams.add(new BasicNameValuePair("cmbMonth2", stopDateFormatted.substring(3, 5)));
		statementParams.add(new BasicNameValuePair("cmbYear2", stopDateFormatted.substring(6, 10)));	
		// Send the Http request
		HttpResponse historyResponse = doPost(context, ACCOUNT_HISTORY_PAGE, statementParams);
		List<Transaction> transactionList = HtmlParser.parseHistoryPage(historyResponse);
		// Logout
		logout(context, getSessionId(loginParams));
		
		Log.i("service", String.format("getHistory()=%d", transactionList.size()));
		return transactionList;
	}

}
