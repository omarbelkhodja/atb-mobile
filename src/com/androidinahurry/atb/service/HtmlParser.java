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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.androidinahurry.atb.model.Account;
import com.androidinahurry.atb.model.Transaction;

public class HtmlParser {
	
	private static String getFirstNodeText(TagNode root, String xPath) throws XPatherException {
		return ((TagNode)(root.evaluateXPath(xPath))[0]).getText().toString();
	}
	
	public static Account parseSoldePage(HttpResponse soldePage) throws IOException, XPatherException, ParseException {
		Account account = new Account();
		InputStream inputStream = soldePage.getEntity().getContent();
		
		CleanerProperties props = new CleanerProperties();
		HtmlCleaner cleaner = new HtmlCleaner(props);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		// Get the root node
		TagNode root = cleaner.clean(inputStream, "iso-8859-1");
		
		// Search for all the column of the second line of the table
		Object[] columns = root.evaluateXPath("//body/table/tbody/tr/td[1]/table/tbody/tr/td[3]/form/table[2]/tbody/tr/td[2]/table[3]/tbody/tr/td/table/tbody/tr[2]/td");
		
		if(columns != null) {
			account.number = getFirstNodeText((TagNode)columns[1], "//font/a");   
			account.userName = getFirstNodeText((TagNode)columns[2], "//font"); 
			account.currency = getFirstNodeText((TagNode)columns[3], "//font");  
			account.balance = new BigDecimal(getFirstNodeText((TagNode)columns[4], "//font").replace(",", ""));  
			account.lastTransactionDate = formatter.parse(getFirstNodeText((TagNode)columns[6], "//font"));   
		}
		
		return account;
	}
	
	
	public static List<NameValuePair> parseHomePage(HttpResponse homePage) throws IOException, AuthenticationFailureException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		// Search for the line with the javascript
		// top.frames.frmfoot.goIn('90176345183736~~HNI','OMARBELKHODJA','HNI','/mesComptes/MesPositions/Solde_Operation.asp','0')
		InputStream inputStream = homePage.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String line;
		int searchIndex = -1;
		line = reader.readLine();
		while (line != null) {
			searchIndex = line.indexOf("top.frames.frmfoot.goIn");
			if (searchIndex != -1) {
				break;
			}
			line = reader.readLine();
		}
		reader.close();

		// Parse the line and extract the parameters
		if (searchIndex != -1) {
			// Param1 is "txtIdsess"
			int firstApostrophe = line.indexOf("'", searchIndex + "top.frames.frmfoot.goIn".length());
			int secondApostrophe = line.indexOf("'", firstApostrophe + 1);
			String sessionId = line.substring(firstApostrophe + 1, secondApostrophe);

			// Param2 is "txtCoduse"
			firstApostrophe = line.indexOf("'", secondApostrophe + 1);
			secondApostrophe = line.indexOf("'", firstApostrophe + 1);
			String userCode = line.substring(firstApostrophe + 1, secondApostrophe);

			// Param3 is "txtNatuse"
			firstApostrophe = line.indexOf("'", secondApostrophe + 1);
			secondApostrophe = line.indexOf("'", firstApostrophe + 1);
			String userType = line.substring(firstApostrophe + 1, secondApostrophe);

			// Param4 is "txtAccpag"
			firstApostrophe = line.indexOf("'", secondApostrophe + 1);
			secondApostrophe = line.indexOf("'", firstApostrophe + 1);
			String accessPage = line.substring(firstApostrophe + 1, secondApostrophe);

			// Param5 is "txtGrpuse"
			firstApostrophe = line.indexOf("'", secondApostrophe + 1);
			secondApostrophe = line.indexOf("'", firstApostrophe + 1);
			String userGroup = line.substring(firstApostrophe + 1, secondApostrophe);

			params.add(new BasicNameValuePair("txtIdsess", sessionId));
			params.add(new BasicNameValuePair("txtCoduse", userCode));
			params.add(new BasicNameValuePair("txtNatuse", userType));
			params.add(new BasicNameValuePair("txtAccpag", accessPage));
			params.add(new BasicNameValuePair("txtGrpuse", userGroup));
			
			// Check session parameter for bad bad authentication parameters
			if(sessionId.equals("~~VISITOR")) {
				throw new AuthenticationFailureException(); 
			}
		}
		
		return params;
	}

	public static List<Transaction> parseHistoryPage(HttpResponse historyPage) throws IllegalStateException, IOException, XPatherException, ParseException {
		List<Transaction> list = new ArrayList<Transaction>();
		
		//InputStream inputStream = new FileInputStream("assets/historique.xml");
		InputStream inputStream = historyPage.getEntity().getContent();
		
		HtmlCleaner cleaner = new HtmlCleaner();

		TagNode root = cleaner.clean(inputStream, "iso-8859-1");
		Object[] lines = root.evaluateXPath("//body/table/tbody/tr/td[1]/table/tbody/tr/td[3]/form/table[4]/tbody/tr/td/table/tbody/tr");
		
		if(lines != null) { 
			for (int i = 1; i < lines.length; i++) {
				/*
				<tr class="tablo-bg-td">
				<td align="center"><font class="texteTablodonnee">02/01/2014</font></td>
				<td align="center"><font class="texteTablodonnee">31/12/2013</font></td>
				<td align="left"><font class="texteTablodonnee">Retrait GAB-ATM BT MEGRINE</font>
				</td><td height="20" align="center"><a class="texteTablodonnee" href="javascript:ordre_onDetail('S111028007557   ','24660','02/01/2014')">S111028007557</a></td>
				<td align="right"><font class="texteTablodonnee">201.600</font></td><td align="right"><font class="texteTablodonnee">0.000</font></td>
				</tr>
				*/

				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Transaction t = new Transaction();
				/*
				System.out.println(
						String.format("%s, %s, %s, %s, %s, %s", 
								getFirstNodeText((TagNode)lines[i], "//td[1]/font"),
								getFirstNodeText((TagNode)lines[i], "//td[2]/font"),
								getFirstNodeText((TagNode)lines[i], "//td[3]/font"),
								getFirstNodeText((TagNode)lines[i], "//td[4]/a"),
								getFirstNodeText((TagNode)lines[i], "//td[5]/font"),
								getFirstNodeText((TagNode)lines[i], "//td[6]/font")
								));
				*/
				t.transactionDate =  formatter.parse(getFirstNodeText((TagNode)lines[i], "//td[1]/font"));   
				t.valueDate = formatter.parse(getFirstNodeText((TagNode)lines[i], "//td[2]/font"));
				t.description = getFirstNodeText((TagNode)lines[i], "//td[3]/font");
				t.reference = getFirstNodeText((TagNode)lines[i], "//td[4]/a");
				t.moneyOut = new BigDecimal(getFirstNodeText((TagNode)lines[i], "//td[5]/font").replace(",",""));
				t.moneyIn = new BigDecimal(getFirstNodeText((TagNode)lines[i], "//td[6]/font").replace(",",""));
				
				list.add(t);
			}
		}
		
		return list;
	}

	public static List<NameValuePair> parseStatementPage(HttpResponse statementPage) throws IllegalStateException, IOException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		// Search for the line with the javascript
		// tabCptCli[0][0]='780777'; tabCptCli[1][0]='0501110264703'
		InputStream inputStream = statementPage.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String line;
		int searchIndex = -1;
		line = reader.readLine();
		while (line != null) {
			searchIndex = line.indexOf("tabCptCli[0][0]=");
			if (searchIndex != -1) {
				break;
			}
			line = reader.readLine();
		}
		reader.close();
		
		// Parse the line and extract the parameters
		if (searchIndex != -1) {
			// Param1 is "txtCodcli"
			int firstApostrophe = line.indexOf("'", searchIndex + "tabCptCli[0][0]=".length());
			int secondApostrophe = line.indexOf("'", firstApostrophe + 1);
			String customerCode = line.substring(firstApostrophe + 1, secondApostrophe);
			
			// Param2 is "txtCptcli"
			firstApostrophe = line.indexOf("'", secondApostrophe + 1);
			secondApostrophe = line.indexOf("'", firstApostrophe + 1);
			String accountCode = line.substring(firstApostrophe + 1, secondApostrophe);
			
			params.add(new BasicNameValuePair("txtCodcli", customerCode));
			params.add(new BasicNameValuePair("txtCptcli", accountCode));
			params.add(new BasicNameValuePair("cmbCptcli", accountCode));
		}
		
		return params;
	}

}
