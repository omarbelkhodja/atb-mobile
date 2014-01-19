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

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class WebServiceLoader<D> extends AsyncTaskLoader<D> {

	D mData;
	
	protected WebServiceLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(D newData) {
		if (isReset()) {
			releaseResources(newData);
			return;
		}

		D oldData = mData;
		mData = newData;

		if (isStarted()) {
			super.deliverResult(newData);
		}

		if (oldData != null && oldData != newData) {
			releaseResources(oldData);
		}
	}

	@Override
	protected void onStartLoading() {
		if (mData != null) {
			deliverResult(mData);
		}

		if ((mData == null) || (takeContentChanged())) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		onStopLoading();

		if (mData != null) {
			releaseResources(mData);
			mData = null;
		}
	}
	
	@Override
	public void onCanceled(D data) {
		super.onCanceled(data);
		releaseResources(data);
	}
	
	protected void storeLoadedData(D data) {
		mData = data;
	}

	abstract protected void releaseResources(D data);

}
