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

package com.androidinahurry.network.utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.*;
import javax.net.ssl.*;

public class FakeSSLSocketFactory extends org.apache.http.conn.ssl.SSLSocketFactory {
	private SSLSocketFactory sslFactory = HttpsURLConnection.getDefaultSSLSocketFactory();

	public FakeSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		super(null);

		try {
			SSLContext context = SSLContext.getInstance("TLS");

			// Create a trust manager that does not validate certificate chains
			// and simply
			// accept all type of certificates
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}

				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
			} };

			// Initialize the socket factory
			context.init(null, trustAllCerts, new SecureRandom());
			sslFactory = context.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
			UnknownHostException {
		return sslFactory.createSocket(socket, host, port, autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslFactory.createSocket();
	}
}