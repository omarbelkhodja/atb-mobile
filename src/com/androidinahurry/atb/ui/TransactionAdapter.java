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

package com.androidinahurry.atb.ui;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.androidinahurry.atb.R;
import com.androidinahurry.atb.model.Transaction;
import com.androidinahurry.utils.EasyFormatter;
import com.androidinahurry.utils.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TransactionAdapter extends BaseAdapter {

    private final Context context;
	private List<Transaction> transactionList = Collections.emptyList();

    public TransactionAdapter(Context context) {
        this.context = context;
    }

	@Override
	public int getCount() {
		return transactionList.size();
	}

	@Override
	public Object getItem(int position) {
		return transactionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = createItemView(parent);
		}
		
		updateView(position, convertView);
		
		return convertView;
	}
	
	private View createItemView(ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.list_item_transaction, parent, false);
	}
	
	private void updateView(int position, View convertView) {
		Transaction transaction = transactionList.get(position);
		
		// Get views reference
		TextView transactionAmountTextView = (TextView) ViewHolder.get(convertView, R.id.transactionAmountTextView);
		TextView transactionDescriptionTextView = (TextView) ViewHolder.get(convertView, R.id.transactionDescriptionTextView);
		TextView transactionDateTextView = (TextView) ViewHolder.get(convertView, R.id.transactionDateTextView);
		
		// Update display with data
		transactionDateTextView.setText(EasyFormatter.formatDate(context, transaction.transactionDate));
		int color;
		BigDecimal amount;
		if(transaction.moneyOut.compareTo(new BigDecimal("0")) != 0) {
			color = Color.parseColor("#CC0000");
			amount = transaction.moneyOut.multiply(new BigDecimal("-1"));
		}
		else {
			color = Color.parseColor("#669900");
			amount = transaction.moneyIn;
		}
		// TODO : change hardcoded DTN value
		transactionAmountTextView.setText(EasyFormatter.formatCurrencyWithSign(amount, "TND"));
		transactionAmountTextView.setTextColor(color);
		transactionDescriptionTextView.setText(transaction.description);
	}
	
	public void updateTransactions(List<Transaction> list) {
		transactionList = list;
	}

}
