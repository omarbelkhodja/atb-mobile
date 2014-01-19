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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.SparseArray;

public class EasyFormatter {
	public static String formatCurrencyWithSign(BigDecimal value, String symbol) {
		DecimalFormat decimalFormat = new DecimalFormat("0.000");
		decimalFormat.setGroupingUsed(true);

		String sign = (value.signum() > 0) ? "+" : "";

		return sign + decimalFormat.format(value) + " " + symbol;
	}
	
	public static String formatCurrencyWihtoutSign(BigDecimal value, String symbol) {
		DecimalFormat decimalFormat = new DecimalFormat("0.000");
		decimalFormat.setGroupingUsed(true);

		return decimalFormat.format(value) + " " + symbol;
	}

	public static String formatCurrencyWithSign(double value, String symbol) {
		DecimalFormat decimalFormat = new DecimalFormat("0.000");
		decimalFormat.setGroupingUsed(true);

		String sign = (value > 0) ? "+" : "";

		return sign + decimalFormat.format(value) + " " + symbol;
	}

	public static String currencyFromDouble(int numericIsoCode, double value) {
		// Get a currency reference from the numeric iso code
		String stringIsoCode = iso4217Map.get(numericIsoCode);
		Currency currency = Currency.getInstance(stringIsoCode);

		// Format with "number currency"
		DecimalFormat decimalFormat = new DecimalFormat("0.000 ¤");
		decimalFormat.setCurrency(currency);
		decimalFormat.setGroupingUsed(true);
		return decimalFormat.format(value);
	}

	public static String formatCurrencyWihtoutSign(double value, String symbol) {
		DecimalFormat decimalFormat = new DecimalFormat("0.000");
		decimalFormat.setGroupingUsed(true);

		return decimalFormat.format(value) + " " + symbol;
	}

	public static String formatDate(Context context, Date date) {
		return DateFormat.getDateFormat(context).format(date);
	}
	
	final static SparseArray<String> iso4217Map = new SparseArray<String>() {
		{
			put(971, "AFN");
			put(978, "EUR");
			put(8, "ALL");
			put(12, "DZD");
			put(840, "USD");
			put(978, "EUR");
			put(973, "AOA");
			put(951, "XCD");
			put(951, "XCD");
			put(32, "ARS");
			put(51, "AMD");
			put(533, "AWG");
			put(36, "AUD");
			put(978, "EUR");
			put(944, "AZN");
			put(44, "BSD");
			put(48, "BHD");
			put(50, "BDT");
			put(52, "BBD");
			put(974, "BYR");
			put(978, "EUR");
			put(84, "BZD");
			put(952, "XOF");
			put(60, "BMD");
			put(64, "BTN");
			put(356, "INR");
			put(68, "BOB");
			put(984, "BOV");
			put(840, "USD");
			put(977, "BAM");
			put(072, "BWP");
			put(578, "NOK");
			put(986, "BRL");
			put(840, "USD");
			put(96, "BND");
			put(975, "BGN");
			put(952, "XOF");
			put(108, "BIF");
			put(116, "KHR");
			put(950, "XAF");
			put(124, "CAD");
			put(132, "CVE");
			put(136, "KYD");
			put(950, "XAF");
			put(950, "XAF");
			put(990, "CLF");
			put(152, "CLP");
			put(156, "CNY");
			put(36, "AUD");
			put(36, "AUD");
			put(170, "COP");
			put(970, "COU");
			put(174, "KMF");
			put(950, "XAF");
			put(976, "CDF");
			put(554, "NZD");
			put(188, "CRC");
			put(952, "XOF");
			put(191, "HRK");
			put(931, "CUC");
			put(192, "CUP");
			put(532, "ANG");
			put(978, "EUR");
			put(203, "CZK");
			put(208, "DKK");
			put(262, "DJF");
			put(951, "XCD");
			put(214, "DOP");
			put(840, "USD");
			put(818, "EGP");
			put(222, "SVC");
			put(840, "USD");
			put(950, "XAF");
			put(232, "ERN");
			put(978, "EUR");
			put(230, "ETB");
			put(978, "EUR");
			put(238, "FKP");
			put(208, "DKK");
			put(242, "FJD");
			put(978, "EUR");
			put(978, "EUR");
			put(978, "EUR");
			put(953, "XPF");
			put(978, "EUR");
			put(950, "XAF");
			put(270, "GMD");
			put(981, "GEL");
			put(978, "EUR");
			put(936, "GHS");
			put(292, "GIP");
			put(978, "EUR");
			put(208, "DKK");
			put(951, "XCD");
			put(978, "EUR");
			put(840, "USD");
			put(320, "GTQ");
			put(826, "GBP");
			put(324, "GNF");
			put(952, "XOF");
			put(328, "GYD");
			put(332, "HTG");
			put(840, "USD");
			put(036, "AUD");
			put(978, "EUR");
			put(340, "HNL");
			put(344, "HKD");
			put(348, "HUF");
			put(352, "ISK");
			put(356, "INR");
			put(360, "IDR");
			put(960, "XDR");
			put(364, "IRR");
			put(368, "IQD");
			put(978, "EUR");
			put(826, "GBP");
			put(376, "ILS");
			put(978, "EUR");
			put(388, "JMD");
			put(392, "JPY");
			put(826, "GBP");
			put(400, "JOD");
			put(398, "KZT");
			put(404, "KES");
			put(036, "AUD");
			put(408, "KPW");
			put(410, "KRW");
			put(414, "KWD");
			put(417, "KGS");
			put(418, "LAK");
			put(428, "LVL");
			put(422, "LBP");
			put(426, "LSL");
			put(710, "ZAR");
			put(430, "LRD");
			put(434, "LYD");
			put(756, "CHF");
			put(440, "LTL");
			put(978, "EUR");
			put(446, "MOP");
			put(807, "MKD");
			put(969, "MGA");
			put(454, "MWK");
			put(458, "MYR");
			put(462, "MVR");
			put(952, "XOF");
			put(978, "EUR");
			put(840, "USD");
			put(978, "EUR");
			put(478, "MRO");
			put(480, "MUR");
			put(978, "EUR");
			put(965, "XUA");
			put(484, "MXN");
			put(979, "MXV");
			put(840, "USD");
			put(498, "MDL");
			put(978, "EUR");
			put(496, "MNT");
			put(978, "EUR");
			put(951, "XCD");
			put(504, "MAD");
			put(943, "MZN");
			put(104, "MMK");
			put(516, "NAD");
			put(710, "ZAR");
			put(036, "AUD");
			put(524, "NPR");
			put(978, "EUR");
			put(953, "XPF");
			put(554, "NZD");
			put(558, "NIO");
			put(952, "XOF");
			put(566, "NGN");
			put(554, "NZD");
			put(036, "AUD");
			put(840, "USD");
			put(578, "NOK");
			put(512, "OMR");
			put(586, "PKR");
			put(840, "USD");
			put(590, "PAB");
			put(840, "USD");
			put(598, "PGK");
			put(600, "PYG");
			put(604, "PEN");
			put(608, "PHP");
			put(554, "NZD");
			put(985, "PLN");
			put(978, "EUR");
			put(840, "USD");
			put(634, "QAR");
			put(978, "EUR");
			put(946, "RON");
			put(643, "RUB");
			put(646, "RWF");
			put(978, "EUR");
			put(654, "SHP");
			put(951, "XCD");
			put(951, "XCD");
			put(978, "EUR");
			put(978, "EUR");
			put(951, "XCD");
			put(882, "WST");
			put(978, "EUR");
			put(678, "STD");
			put(682, "SAR");
			put(952, "XOF");
			put(941, "RSD");
			put(690, "SCR");
			put(694, "SLL");
			put(702, "SGD");
			put(532, "ANG");
			put(994, "XSU");
			put(978, "EUR");
			put(978, "EUR");
			put(90, "SBD");
			put(706, "SOS");
			put(710, "ZAR");
			put(728, "SSP");
			put(978, "EUR");
			put(144, "LKR");
			put(938, "SDG");
			put(968, "SRD");
			put(578, "NOK");
			put(748, "SZL");
			put(752, "SEK");
			put(947, "CHE");
			put(756, "CHF");
			put(948, "CHW");
			put(760, "SYP");
			put(901, "TWD");
			put(972, "TJS");
			put(834, "TZS");
			put(764, "THB");
			put(840, "USD");
			put(952, "XOF");
			put(554, "NZD");
			put(776, "TOP");
			put(780, "TTD");
			put(788, "TND");
			put(949, "TRY");
			put(934, "TMT");
			put(840, "USD");
			put(036, "AUD");
			put(800, "UGX");
			put(980, "UAH");
			put(784, "AED");
			put(826, "GBP");
			put(840, "USD");
			put(997, "USN");
			put(998, "USS");
			put(840, "USD");
			put(940, "UYI");
			put(858, "UYU");
			put(860, "UZS");
			put(548, "VUV");
			put(978, "EUR");
			put(937, "VEF");
			put(704, "VND");
			put(840, "USD");
			put(840, "USD");
			put(953, "XPF");
			put(504, "MAD");
			put(886, "YER");
			put(967, "ZMW");
			put(932, "ZWL");
			put(955, "XBA");
			put(956, "XBB");
			put(957, "XBC");
			put(958, "XBD");
			put(000, "XFU");
			put(963, "XTS");
			put(999, "XXX");
			put(959, "XAU");
			put(964, "XPD");
			put(962, "XPT");
			put(961, "XAG");
		}
	};


}
