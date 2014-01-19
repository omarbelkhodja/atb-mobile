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

package com.androidinahurry.utils;

import com.androidinahurry.atb.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.webkit.WebView;

public class DialogUtils {
	
	final static String LICENCES_HTML_FILE = "file:///android_asset/licenses.html";
	
	public static void showAboutDialog(FragmentActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("about_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
		
        new HtmlDialogFragment().show(ft, "dialog_licenses");
    }

	public static class HtmlDialogFragment extends DialogFragment {
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
			WebView webView = new WebView(getActivity());
	        webView.loadUrl(LICENCES_HTML_FILE);

	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setView(webView)
	        		.setTitle(R.string.action_about)
	        		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   HtmlDialogFragment.this.dismiss();
	                   }
	               });
	        return builder.create();
	    }

	}
	
	public static class LoginFailureDialogFragment extends DialogFragment {
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(R.string.logging_failure)
	        		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   LoginFailureDialogFragment.this.dismiss();
	                   }
	               });
	        return builder.create();
	    }

	}


}
