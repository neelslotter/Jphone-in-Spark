package com.aizatron.mtn.phone;

/**
Copyright © 2022 AIZATRON | Powered by AIZATRON
*/

/**
 * Please refer to
 * https://github.com/google/libphonenumber
 */

import com.google.i18n.phonenumbers.*;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import java.io.FileNotFoundException;
import java.util.Locale;
import static spark.Spark.get;

public class Launcher {

	private static final Logger mLogger = LogManager.getLogger(Launcher.class);

	public static void main(String[] args)
			throws FileNotFoundException, com.google.i18n.phonenumbers.NumberParseException {

		// Had to set the following to get the system to log
		System.setProperty("org.eclipse.jetty.util.log.class", "org.apache.logging.log4j.appserver.jetty.Log4j2Logger");
		Log.getProperties().setProperty("org.eclipse.jetty.util.log.announce", "false");

		// Please refer http://sparkjava.com/documentation#embedded-web-server
		int maxThreads = 200;
		int minThreads = 10;
		int timeOutMillis = 3000;
		spark.Spark.port(4568);
		spark.Spark.threadPool(maxThreads, minThreads, timeOutMillis);

		/*
		 * If you are looking at implementing some cache LoadingCache<String,
		 * PhoneStructure> phoneCache = CacheBuilder.newBuilder().maximumSize(100000)
		 * .expireAfterAccess(3000, TimeUnit.MINUTES).build(new CacheLoader<String,
		 * PhoneStructure>() {
		 * 
		 * @Override public PhoneStructure load(String vPhone) throws Exception { //
		 * make the expensive call return getFromDatabase(vPhone); } });
		 */

		/*
		 * Reserved for future URL's get("/phonelib/cc/:name", (request, response) -> {
		 * return "Hello: " + request.params(":name"); });
		 */

		// rune me: http://localhost/phonelib:4568/+27829946273
		get("/phonelib/:name", (request, response) -> {

			java.lang.String vFirstArg = null;
			com.aizatron.mtn.phone.PhoneStructure vStruct = null;
			try {
				vFirstArg = request.params(":name");
				try {
					// phoneCache.get(vFirstArg);
					vStruct = AddE164Structure(vFirstArg, "ZZ", false);
					mLogger.log(Level.INFO, vStruct.toString());
				} catch (java.lang.Exception vException) {
					mLogger.log(Level.WARN, vException.getLocalizedMessage());
				}
			} catch (java.lang.Exception vException) {
				mLogger.log(Level.WARN, vException.getLocalizedMessage());
			}

			return vStruct.getCarrier() + "," + vStruct.getCountry() + "," + vStruct.getType();
		});

	}

	/*
	 * make that expensive expensive call private static PhoneStructure
	 * getFromDatabase(String vPhone) { PhoneStructure mPhone = new
	 * PhoneStructure(); return mPhone; }
	 */

	// Reserved for testing
	static void PhoneNumberBuilder() {

		Phonenumber.PhoneNumber rsaMobileNumber = new Phonenumber.PhoneNumber().setCountryCode(27)
				.setNationalNumber(829946478L);

		final PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
		final PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

		System.out.println(geocoder.getDescriptionForNumber(rsaMobileNumber, Locale.ENGLISH));
		System.out.println(carrierMapper.getNameForNumber(rsaMobileNumber, Locale.ENGLISH));
		System.out.println("NationalNumber " + rsaMobileNumber.getNationalNumber());
		System.out.println("CountryCode " + rsaMobileNumber.getCountryCode());
		System.out.println("NumberOfLeadingZeros " + rsaMobileNumber.getNumberOfLeadingZeros());
		System.out.println("Extension " + rsaMobileNumber.getExtension());
		System.out.println("CountryCodeSource " + rsaMobileNumber.getCountryCodeSource());
		System.out.println("PreferredDomesticCarrierCode " + rsaMobileNumber.getPreferredDomesticCarrierCode());
		System.out.println("RawInput " + rsaMobileNumber.getRawInput());
	}

	/**
	 * E.164 – International E.164-number structure for geographic areas MAX 15
	 * digits CC 1-3 digits NDC 15 digits
	 *
	 * @param aReplacedStr (phone number)
	 * @param aCountry
	 * @return PhoneStructure object
	 * @throws Exception
	 */
	static PhoneStructure AddE164Structure(java.lang.String aReplacedStr, final java.lang.String aCountry,
			final boolean ported) throws Exception {

		final PhoneNumberOfflineGeocoder mGeocoder = PhoneNumberOfflineGeocoder.getInstance();
		final PhoneNumberToCarrierMapper mCarrierMapper = PhoneNumberToCarrierMapper.getInstance();

		// Reserve for future use
		// ShortNumberInfo shortInfo = ShortNumberInfo.getInstance();

		// Aizatron structure
		com.aizatron.mtn.phone.PhoneStructure mPhoneStructure = new PhoneStructure();

		try {

			com.google.i18n.phonenumbers.PhoneNumberUtil mPhoneUtil = PhoneNumberUtil.getInstance();
			com.google.i18n.phonenumbers.Phonenumber.PhoneNumber vNumber = null;

			// Implement some pre-processing on numbers here
			// String myNumber = aReplacedStr.replaceAll("[^\\d-]", "");
			// .etc

			// Google libs seems to prefer numbers to start with +
			// Some trail and error required here
			if (aReplacedStr.startsWith("+")) {
				vNumber = mPhoneUtil.parse(aReplacedStr, aCountry);
			} else {
				vNumber = mPhoneUtil.parse("+" + aReplacedStr, aCountry);
			}

			// System.out.println("CountryCodeSource " +
			// carrierMapper.getNameForNumber(vNumber, Locale.ENGLISH));
			// System.out.println("PreferredDomesticCarrierCode " +
			// vNumber.getPreferredDomesticCarrierCode());

			mPhoneStructure.setType(String.valueOf(mPhoneUtil.getNumberType(vNumber)));
			mPhoneStructure.setPhoneNumber(vNumber);
			mPhoneStructure.setValid(mPhoneUtil.isValidNumber(vNumber));
			mPhoneStructure.setCountry(mGeocoder.getDescriptionForNumber(vNumber, Locale.ENGLISH));

			mPhoneStructure.setCarrier(mCarrierMapper.getNameForNumber(vNumber, Locale.ENGLISH));
			// PhoneStructure.setShort(shortInfo.isPossibleShortNumber(vNumber));
			mPhoneStructure.setPorted(ported);

		} catch (com.google.i18n.phonenumbers.NumberParseException vNumberParseException) {
			mLogger.warn(vNumberParseException.getLocalizedMessage());
		}

		if (mPhoneStructure.getPhoneNumber() == null) {
			mLogger.warn("Number is invalid");
		} else {
			mLogger.info(mPhoneStructure.toString());
		}

		return mPhoneStructure;
	}
}
