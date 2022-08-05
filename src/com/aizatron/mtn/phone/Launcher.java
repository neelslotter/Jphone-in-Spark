package com.aizatron.mtn.phone;


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

		System.setProperty("org.eclipse.jetty.util.log.class", "org.apache.logging.log4j.appserver.jetty.Log4j2Logger");
		Log.getProperties().setProperty("org.eclipse.jetty.util.log.announce", "false");

		int maxThreads = 200;
		int minThreads = 10;
		int timeOutMillis = 3000;
		spark.Spark.port(4568);
		spark.Spark.threadPool(maxThreads, minThreads, timeOutMillis);

//		LoadingCache<String, PhoneStructure> phoneCache = CacheBuilder.newBuilder().maximumSize(100000)
//				.expireAfterAccess(3000, TimeUnit.MINUTES).build(new CacheLoader<String, PhoneStructure>() { 
//
//					@Override
//					public PhoneStructure load(String vPhone) throws Exception {
//						// make the expensive call
//						return getFromDatabase(vPhone);
//					}
//				});

		get("/phonelib/cc/:name", (request, response) -> {
			return "Hello: " + request.params(":name");
		});

		/// someRoute/:var1/:var2/:var3
		get("/phonelib/:name", (request, response) -> {

			java.lang.String vFirstArg = null;
			PhoneStructure vStruct = null;
			try {
				vFirstArg = request.params(":name");
				try {

					// phoneCache.get(vFirstArg);

					vStruct = AddE164Structure(vFirstArg, "ZZ", false);
					//mLogger.log(Level.INFO, vStruct.toString());
				} catch (java.lang.Exception vException) {
					//mLogger.log(Level.WARN, vException.getLocalizedMessage());
				}
			} catch (java.lang.Exception vException) {
				//mLogger.log(Level.WARN, vException.getLocalizedMessage());
			}

			return vStruct.getCarrier() + "," + vStruct.getCountry() + "," + vStruct.getType();
		});

	}

	// make expensive call
//	private static PhoneStructure getFromDatabase(String vPhone) {
//		PhoneStructure mPhone = new PhoneStructure();
//		return mPhone;
//	}

	// Reserved for testing
	static void PhoneNumberBuilder() {

		Phonenumber.PhoneNumber rsaMobileNumber = new Phonenumber.PhoneNumber().setCountryCode(27)
				.setNationalNumber(829946470L);

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
	 * @param aReplacedStr
	 * @param aCountry
	 * @return PhoneStructure object
	 * @throws Exception
	 */
	static PhoneStructure AddE164Structure(java.lang.String aReplacedStr, final java.lang.String aCountry,
			final boolean ported) throws Exception {

		final PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
		final PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();
	
		// Reserve for future use
		// ShortNumberInfo shortInfo = ShortNumberInfo.getInstance();

		// Aizatron structure
		com.aizatron.mtn.phone.PhoneStructure vPhoneStructure = new PhoneStructure();

		try {

			PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
			Phonenumber.PhoneNumber vNumber = null;

			// Google libs seems to prefer numbers to start with +
			// do some pre-processing

			//String myNumber = aReplacedStr.replaceAll("[^\\d-]", "");

			if (aReplacedStr.startsWith("+")) {
				vNumber = phoneUtil.parse(aReplacedStr, aCountry);
			} else {
				vNumber = phoneUtil.parse("+" + aReplacedStr, aCountry);
			}

			// System.out.println("CountryCodeSource " +
			// carrierMapper.getNameForNumber(vNumber, Locale.ENGLISH));
			// System.out.println("PreferredDomesticCarrierCode " +
			// vNumber.getPreferredDomesticCarrierCode());

			vPhoneStructure.setType(String.valueOf(phoneUtil.getNumberType(vNumber)));
			vPhoneStructure.setPhoneNumber(vNumber);
			vPhoneStructure.setValid(phoneUtil.isValidNumber(vNumber));
			vPhoneStructure.setCountry(geocoder.getDescriptionForNumber(vNumber, Locale.ENGLISH));

			vPhoneStructure.setCarrier(carrierMapper.getNameForNumber(vNumber, Locale.ENGLISH));
			// PhoneStructure.setShort(shortInfo.isPossibleShortNumber(vNumber));
			vPhoneStructure.setPorted(ported);

		} catch (com.google.i18n.phonenumbers.NumberParseException vNumberParseException) {
			//mLogger.warn(vNumberParseException.getLocalizedMessage());

		}

		if (vPhoneStructure.getPhoneNumber() == null) {
			//mLogger.warn("Number is invalid");
		} else {
			//mLogger.info(vPhoneStructure.toString());
		}

		return vPhoneStructure;
	}
}
