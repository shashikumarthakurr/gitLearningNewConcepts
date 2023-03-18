package com.business.RingPay;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertNotNull;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Driver;
import java.time.Instant;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.Datasheet.RingPay_TestData_DataProvider;
import com.android.RingPayPages.AddAddressPage;
import com.android.RingPayPages.GmailLoginPage;
import com.android.RingPayPages.HomePage;
import com.android.RingPayPages.KycDocument;
import com.android.RingPayPages.MobileLoginPage;
import com.android.RingPayPages.OfferPage;
import com.android.RingPayPages.PAN_DetailsPage;
import com.android.RingPayPages.PaymentPage;
import com.android.RingPayPages.PermissionPage;
import com.android.RingPayPages.RingLoginPage;
import com.android.RingPayPages.RingPayMerchantFlowPage;
import com.android.RingPayPages.RingPromoCodeLogin;
import com.android.RingPayPages.RingUserDetailPage;
import com.android.RingPayPages.SignUP_LoginPage;
import com.android.RingPayPages.SimValidationPage;
import com.android.RingPayPages.TestingPortalWebPage;
import com.android.RingPayPages.TransectionPIN;
import com.android.RingPayPages.UserRegistrationPage;
import com.driverInstance.CommandBase;
import com.driverInstance.DriverInstance;
import com.driverInstance.Drivertools;
import com.excel.ExcelFunctions;
import com.extent.ExtentReporter;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.StartsActivity;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class RingPayBusinessLogic extends Utilities {

	public RingPayBusinessLogic(String Application) throws InterruptedException {
		new CommandBase(Application);
		init();
	}
	public  AppiumDriver<WebElement> appium;
	RingPay_TestData_DataProvider dataProvider = new RingPay_TestData_DataProvider();
	private int timeout;
	public long age;
	SoftAssert softAssertion = new SoftAssert();
	boolean launch = "" != null;
	/** Retry Count */
	private int retryCount;
	ExtentReporter extent = new ExtentReporter();

	/** The Constant logger. */
	static LoggingUtils logger = new LoggingUtils();

	/** The Android driver. */
	//public AndroidDriver<AndroidElement> androidDriver;
	public String amount[] = { "1001", "0", "1" };
	public static boolean relaunchFlag = false;
	public static boolean appliTools = false;
	public String noTxt;
	public static boolean PopUp = false;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;

	}

	/**
	 * Initiate Property File.
	 *
	 * @param byLocator the by locator
	 */

	public void init() {

		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
		logger.info(
				"Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
	}

	public void TearDown() {
		logger.info("App tear Down");
		getDriver().quit();
	}

	/**
	 * Business method for UserPlayStore Flow
	 * 
	 */

	public void User_Play_Store_Flow(String validMob, String editMob, String lessThanTenMob, String moreThanTenMob,
			String specialCharMob, String spaceMob, String lessOtp, String invalidOtp) throws Exception {
		extent.HeaderChildNode("User Play store Flow Module");

		cameraPermission();
		extent.extentLogger("PASS","TC_Ring_Core_01 - To Verify the Login screen when user opens the app by clicking on App Icon");

		enablePermissions();
		extent.extentLoggerPass("TC_Ring_Core_02","TC_Ring_Core_02 - To verify When User selects Enable Permission option");

		promoCodeModule();
		extent.extentLoggerPass("TC_Ring_Core_66", "TC_Ring_Core_66 - To verify  user Scans the Promo Code QR");

		String loginHeaderTxt = getText(RingLoginPage.objLoginHeader);
		softAssertion.assertEquals(loginHeaderTxt, "Sign Up / Login");

		String mobileTxt = getText(RingLoginPage.objLoginMobile);
		String googleTxt = getText(RingLoginPage.objLoginGoogle);
		String facebookTxt = getText(RingLoginPage.objLoginFacebook);
		String termsTxt = getText(RingLoginPage.objTermsLink_PrivacyFooter);
		// String privacyTxt = getText(RingLoginPage.objPrivacyPolicyFooter);

		softAssertion.assertEquals(mobileTxt, "Continue with Mobile");
		softAssertion.assertEquals(googleTxt, "Continue with Google");
		softAssertion.assertEquals(facebookTxt, "Continue with Facebook");
		softAssertion.assertEquals(termsTxt, " Terms of Services &  Privacy Policy");

		extent.extentLoggerPass("TC_Ring_Core_03",
				"TC_Ring_Core_03 - To verify User Selects signup/Login option under Don't have a QR Code?");

		loginMobile();
		extent.extentLoggerPass("TC_Ring_Core_04", "TC_Ring_Core_04 - To Verify when user Continue with mobile option");

		extent.extentLoggerPass("TC_Ring_Core_05", "TC_Ring_Core_05 - To Verify the Verify mobile screen");

		// type(RingLoginPage.objMobTextField,lessThanTenMob,"Mobile text field");
		logger.info("Verify mobile number with <10 digits");
		// Aclick(RingLoginPage.objNextBtn, "Next Button");
		mobileNoValidation1(lessThanTenMob);
		explicitWaitVisibility(RingLoginPage.objMobError, 10);
		String errorMsg = getText(RingLoginPage.objMobError);
		softAssertion.assertEquals(errorMsg, " Please enter valid mobile number");
		extent.extentLoggerPass("TC_Ring_Core_07",
				"TC_Ring_Core_07 - To Verify User enter mobile number less than 10 digit");

		clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
		logger.info("Verify mobile number with >10 digits");
		mobileNoValidation1(moreThanTenMob);
		explicitWaitVisibility(RingLoginPage.OtpAutoRead, 10);
		String otpAutoRead = getText(RingLoginPage.OtpAutoRead);
		softAssertion.assertEquals(otpAutoRead, "Auto Reading OTP");
		extent.extentLoggerPass("TC_Ring_Core_08",
				"TC_Ring_Core_08 - To Verify User enter mobile number more than 10 digit");

		// type(RingLoginPage.objMobTextField,specialCharMob,"Mobile text field");
		Back(1);
		// trueCallerPopup();
//		explicitWaitVisibility(RingLoginPage.objNoneBtn, 15);
//		Aclick(RingLoginPage.objNoneBtn, "None of the above");
		logger.info("Verify mobile number with special characters");
		mobileNoValidation(specialCharMob);
		explicitWaitVisibility(RingLoginPage.objMobError, 10);
		softAssertion.assertEquals(errorMsg, " Please enter valid mobile number");
		extent.extentLoggerPass("TC_Ring_Core_10",
				"TC_Ring_Core_10 - To Verify User tries enter punctuations or special character in field");

		clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
		// type(RingLoginPage.objMobTextField,spaceMob,"Mobile text field");
		logger.info("Verify mobile number with space in between");
		mobileNoValidation1(spaceMob);
		explicitWaitVisibility(RingLoginPage.objMobError, 10);
		softAssertion.assertEquals(errorMsg, " Please enter valid mobile number");
		extent.extentLoggerPass("TC_Ring_Core_11",
				"TC_Ring_Core_11 - To Verify User tries enter punctuations or special character in field");

		clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
		// type(RingLoginPage.objMobTextField,validMob,"Mobile text field");
		logger.info("Verify mobile number with entering valid number");
		mobileNoValidation1(validMob);
		explicitWaitVisibility(RingLoginPage.OtpAutoRead, 10);
		// String otpAutoRead = getText(RingLoginPage.OtpAutoRead);
		softAssertion.assertEquals(otpAutoRead, "Auto Reading OTP");
		extent.extentLoggerPass("TC_Ring_Core_13", "TC_Ring_Core_13 - To Verify User tries enter valid mobile number");

		explicitWaitClickable(RingLoginPage.objEditMobNo, 10);

		Aclick(RingLoginPage.objEditMobNo, "Edit Mobile number");
		extent.extentLoggerPass("TC_Ring_Core_14",
				"TC_Ring_Core_14 - To Verify User should able to see Edit mobile no option");

		// trueCallerPopup();

		// String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
		// softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
		// Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		trueCallerPopup();
		explicitWaitVisibility(RingLoginPage.objNoneBtn, 10);
		Aclick(RingLoginPage.objNoneBtn, "None of the above");
		// verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above button");
		mobileNoValidation1(editMob);
		// type(RingLoginPage.objMobTextField,editMob,"Mobile text field");
		explicitWaitVisibility(RingLoginPage.getEditMob(editMob), 10);
		String mobNoTxt = getText(RingLoginPage.getEditMob(editMob));
		String mobNoText = mobNoTxt.substring(16, 26);
		System.out.println(mobNoText);
		softAssertion.assertNotEquals(validMob, mobNoText);
		extent.extentLoggerPass("TC_Ring_Core_16",
				"TC_Ring_Core_16 - To verify user clicks continue button after mobile number modification");
		// 18 TO DO
		explicitWaitVisibility(RingLoginPage.OtpAutoRead, 10);
		WebElement resendOtp = findElement(RingLoginPage.resendOtpTxt);
		String clickable = getAttributValue("clickable", RingLoginPage.resendOtpTxt);
		softAssertion.assertEquals("false", clickable);
		extent.extentLoggerPass("TC_Ring_Core_19",
				"TC_Ring_Core_19 - To Verify the text given below the OTP number box when the timer is in progress");

		explicitWaitClickable(RingLoginPage.resendOtpTxt, 10);
		extent.extentLoggerPass("TC_Ring_Core_20",
				"TC_Ring_Core_20 - To Verify the text given below the OTP number box when the timer is completed.");

		String focused_before = getAttributValue("focused", RingLoginPage.objOtpTxtField1);
		System.out.println(focused_before);
		softAssertion.assertEquals("false", focused_before);
		extent.extentLoggerPass("TC_Ring_Core_21",
				"TC_Ring_Core_21 - To Verify the OTP number box behaviour when the timer is started.");

		explicitWaitVisibility(RingLoginPage.resendOtpTxt, 10);
		String focused_after = getAttributValue("clickable", RingLoginPage.objOtpTxtField1);
		System.out.println(focused_after);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		softAssertion.assertEquals("true", focused_after);
		extent.extentLoggerPass("TC_Ring_Core_22",
				"TC_Ring_Core_22 - To Verify the OTP number box behaviour when the timer is completed.");

		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, invalidOtp, "Enter OTP");
		explicitWaitVisibility(RingLoginPage.OtpError, 10);
		logger.info("OTP Error message");
		String otpErrorTxt = getText(RingLoginPage.OtpError);
		softAssertion.assertEquals(otpErrorTxt, "You have entered incorrect or expired OTP");
		extent.extentLoggerPass("TC_Ring_Core_23", "TC_Ring_Core_23 - To Verify User enter invalid OTP");

		clearField(RingLoginPage.objOtpTxtField1, "Enter OTP");
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, lessOtp, "Enter OTP");
		boolean Otp_flag = verifyElementNotPresent(RingLoginPage.OtpError, 10);
		softAssertion.assertEquals(false, Otp_flag);
		extent.extentLoggerPass("TC_Ring_Core_26",
				"TC_Ring_Core_26 - To Verify if user enters less than 6 digit number");

		explicitWaitClickable(RingLoginPage.resendOtpTxt, 10);
		extent.extentLoggerPass("TC_Ring_Core_27", "TC_Ring_Core_27 - To Verify Resend OTP should clickable");
		clearField(RingLoginPage.objOtpTxtField1, "Enter OTP");

		waitTime(3000);
		Back(1);
		Back(1);
		// trueCallerPopup();
//		explicitWaitVisibility(RingLoginPage.objNoneBtn, 15);
//		Aclick(RingLoginPage.objNoneBtn, "None of the above");
		String blockMobileNo = "9" + RandomIntegerGenerator(9);
		mobileNoValidation(blockMobileNo);

		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		// Aclick(MobileLoginPage.txtResendOtp, "Resend OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 1 Time");
		String getAutoreadValidation = getText(MobileLoginPage.txtAutoReadingOTP);
		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");
		explicitWaitVisibility(MobileLoginPage.txtOtpTimeStamp, 20);
		String otpTimeStamp = getText(MobileLoginPage.txtOtpTimeStamp);
		otpTimeStamp = otpTimeStamp.substring(0, 5);
		System.out.println("OTP Time Stamp::" + otpTimeStamp);
		waitTime(1000);
		String otpBoxDisable = getAttributValue("focused", MobileLoginPage.enterOTPNumberFiled);
		System.out.println("Element Focus:: " + otpBoxDisable);
		softAssertion.assertEquals(" 00:0", otpTimeStamp);
		if (otpBoxDisable.equals("false")) {
			softAssertion.assertEquals("false", otpBoxDisable);
			logger.info("OTP Box Disable");
			extent.extentLoggerPass("OTP Box Disable", "OTP Box Disable");
		} else {
			logger.info("OTP Box Enabled");
			extent.extentLoggerFail("OTP Box Enable", "OTP Box Enabled");
		}

		extent.extentLoggerPass("TC_Ring_Core_28", "TC_Ring_Core_28 - To Verify Resend OTP option");

		// Attemp 2
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, generateRandomInt(1000000), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 2 Time");
		String getAutoreadValidation1 = getText(MobileLoginPage.txtAutoReadingOTP);
		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation1);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation1);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");

		// Attemp 3
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, generateRandomInt(1000000), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 3 Time");

		// Attemp 4
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);

		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, generateRandomInt(1000000), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 4 Time");

		// Attemp 5
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);

		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, generateRandomInt(1000000), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 5 Time");
//		Aclick(MobileLoginPage.txtResendOtp, "Resend Button 5 Time");
		if (verifyElementDisplayed(MobileLoginPage.txtResendOtp)) {
			Aclick(MobileLoginPage.txtResendOtp, "Resend Button 5 Time");
		}

		explicitWaitVisibility(RingLoginPage.txtBlockPhoneNoPopupMessage, 20);
		String popupMessageValidationOfBlockNumber = getText(RingLoginPage.txtBlockPhoneNoPopupMessage);
		logger.info("Expected: " + popupMessageValidationOfBlockNumber);
		softAssertion.assertEquals(
				"Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.",
				popupMessageValidationOfBlockNumber);
		logger.info("Temprory Block Your Number For 2 Minutes Validated PopUp Message");
		extent.extentLoggerPass("PopUp Of Block User For 2 Minutes",
				"Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.");
		Aclick(RingLoginPage.btnOkGotIt, "Button Ok, Got It!");
		trueCallerPopup();
		explicitWaitVisibility(RingLoginPage.objNoneBtn, 15);
		Aclick(RingLoginPage.objNoneBtn, "None of the above");
		// logger.info("TC 29----------------->PASSED");
		extent.extentLoggerPass("TC_Ring_Core_29",
				"TC_Ring_Core_29 To Verify user enters wrong otp and attempts for 5th time  by clicking on Resend button ");
		waitTime(4000);
		// TC30
		explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 20);
		String verifyMoblieNumberEnter = getText(RingLoginPage.objVerifyMobHeader);
		softAssertion.assertEquals("Verify Mobile", verifyMoblieNumberEnter);
		logger.info(verifyMoblieNumberEnter);
		extent.extentLoggerPass("Verify Mobile Page", "Verify Mobile Page is visible");
		// logger.info("TC 30----------------->PASSED");
		extent.extentLoggerPass("TC_Ring_Core_30",
				"TC_Ring_Core_30 To Verify when user click on Ok, Got It! on the bottom sheet ");
		// TC31
		waitTime(4000);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, blockMobileNo, "Mobile text field");
		System.out.println(blockMobileNo);
		waitTime(5000);
		Aclick(RingLoginPage.objOkGotitBtn, "Button Ok, Got It!");
		click(RingLoginPage.objMobTextField, "Mobile Number Field");
		clearField(RingLoginPage.objMobTextField, "Mobile Number Field");

		logger.info("Expected: " + popupMessageValidationOfBlockNumber);
		softAssertion.assertEquals(
				"Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.",
				popupMessageValidationOfBlockNumber);
		logger.info("Temprory Block Your Number For 2 Minutes Validated PopUp Message");
		extent.extentLoggerPass("PopUp Of Block User For 2 Minutes",
				"Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.");
//		Aclick(RingLoginPage.btnOkGotIt, "Button Ok, Got It!");
		// enterOtp("888888");
		logger.info("TC_Ring_Core_31 To Verify when user enter the same mobile number which is blocked------>PASSED");
		extent.extentLoggerPass("TC_Ring_Core_31",
				"TC_Ring_Core_31 To Verify when user enter the same mobile number which is blocked------>PASSED");

		waitTime(4000);
		// mobileNoValidation("9" + RandomIntegerGenerator(9));
		explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
		logger.info("Verify Mobile Header");
		String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
		softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
		waitTime(4000);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, "9" + RandomIntegerGenerator(9), "Mobile text field");
		enterOtp("888888");
		// TC32
		explicitWaitVisibility(MobileLoginPage.btnReadAndAccept, 10);
		String txtReadAndAccept = getText(MobileLoginPage.btnReadAndAccept);
		logger.info(txtReadAndAccept);
		softAssertion.assertEquals("Read & Accept", txtReadAndAccept);
		extent.extentLoggerPass("Permission Validatation", "Permission Page is visible");
		logger.info("TC_Ring_Core_32 To Verify user enters valid OTP---->PASSED");
		extent.extentLoggerPass("TC_Ring_Core_32", "TC_Ring_Core_32 To Verify user enters valid OTP---->PASSED");

		explicitWaitVisibility(RingLoginPage.objRingPermissionsHeader, 10);

		waitTime(7000);
		explicitWaitVisibility(RingLoginPage.ckycCheckBox, 10);
		Aclick(RingLoginPage.ckycCheckBox, "Terms and Conditions checkbox");

		Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
		waitTime(1000);
		String kycError = getText(UserRegistrationPage.objToast);
		softAssertion.assertEquals("Please accept terms and conditions", kycError);
		extent.extentLoggerPass("TC_Ring_Core_47",
				"TC_Ring_Core_47 To Verify if user only checked in whatsapp notification check box and Continue with \n"
						+ "read & accept");
		extent.extentLoggerPass("TC_Ring_Core_49",
				"TC_Ring_Core_49 To Verify when user clicks on read and accept with unchecked CKYC consent");

		Aclick(RingLoginPage.ckycCheckBox, "Terms and Conditions checkbox");

		if (verifyElementPresent(RingLoginPage.objRingPermissionsHeader, "RingPay permissions")) {

			logger.info("Ring Pay Permissions page (SMS, LOCATION & PHONE)");
			String ringPermissionTxt = getText(RingLoginPage.objRingPermissionsHeader);
			Assert.assertEquals(ringPermissionTxt, "Permissions");

			Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");

			explicitWaitVisibility(RingLoginPage.objLocAccess, 10);

			Aclick(RingLoginPage.objLocAccess, "Location Access option");
			Aclick(RingLoginPage.objPhoneAccess, "Phone access option");
			Aclick(RingLoginPage.objSMSAccess, "SMS access option");
		}
		extent.extentLoggerPass("TC_Ring_Core_44",
				"TC_Ring_Core_44 To Verify after successful login, user should navigate to permission page");
		extent.extentLoggerPass("TC_Ring_Core_54", "TC_Ring_Core_54 To verify when users allow all the permission");
		softAssertion.assertAll();

	}

	/**
	 * Business method for User Registration Flow
	 * 
	 */
	public void User_Registration_Flow(String month, String date, String year, String gender) throws Exception {
		extent.HeaderChildNode("User Registration Flow Module");

		// hideKeyboard();
		logger.info("User Registration Details Page");
		extent.extentLogger("INFO", "User Registration Details Page");
		explicitWaitVisibility(UserRegistrationPage.objUserDetailsHeader, 10);
		hideKeyboard();
		explicitWaitVisibility(UserRegistrationPage.objFirstName, 10);
		explicitWaitVisibility(UserRegistrationPage.objLastName, 10);
		explicitWaitVisibility(UserRegistrationPage.objUserDOB, 10);
		explicitWaitVisibility(UserRegistrationPage.objGenderSelect, 10);

		extent.extentLoggerPass("TC_Ring_Core_68", "TC_Ring_Core_68 - To verify the 'User Details' screen");

		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		String firstNameErrorTxt = getText(UserRegistrationPage.objFirstNameError);
		String lastNameErrorTxt = getText(UserRegistrationPage.objLastNameError);
		String dobErrorTxt = getText(UserRegistrationPage.objDOBError);
		String genderErrorTxt = getText(UserRegistrationPage.objGender);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		softAssertion.assertEquals("Please select Date of Birth", dobErrorTxt);
		softAssertion.assertEquals("Please Select Gender.", genderErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_71",
				"TC_Ring_Core_71 - To verify the response by clicking on 'Register' button when "
						+ "all required fields are empty");
		extent.extentLoggerPass("TC_Ring_Core_94",
				"TC_Ring_Core_94 - To verify whether the user is not able to 'Continue' when the 'Gender' is not selected "
						+ "from the drop down");

		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "Huss", "Last Name text field");
		hideKeyboard();
		dateOfBirth(month, date, year);
		click(UserRegistrationPage.objGenderSelect, "Gender dropdown");
		explicitWaitVisibility(UserRegistrationPage.objMale, 10);
		click(UserRegistrationPage.objMale, "Male gender");
		hideKeyboard();
		// genderSelect(gender);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_72",
				"TC_Ring_Core_72 - To verify the user is not able to 'Continue' with by keeping 'First Name' field "
						+ "empty with all valid details");

		Aclick(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, generateRandomString(1), "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationPage.objRegister, 10);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_73",
				"TC_Ring_Core_73 - To verify the user is not able to 'Continue' with entering only 'First Name' "
						+ "initial in first name field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "Xyz123", "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationPage.objRegister, 10);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_74",
				"TC_Ring_Core_74 - To verify the user is not able to 'Continue' with alphanumeric characters"
						+ "entered in 'First Name' field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "Xyz123:+$", "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationPage.objRegister, 10);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_75",
				"TC_Ring_Core_75 - To verify the user is not able to 'Continue' with special characters "
						+ "entered in 'First Name' field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "  ", "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationPage.objRegister, 10);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_76",
				"TC_Ring_Core_76 - To verify the user is not able to 'Continue' with <Space> entered in 'First "
						+ " Name' field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, generateRandomString(5), "First Name text field");
		hideKeyboard();

		Aclick(UserRegistrationPage.objLastName, "Last Name text field");
		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_79",
				"TC_Ring_Core_79 - To verify the user is not able to 'Continue' with by keeping 'Last Name' field "
						+ "empty with all valid details");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		type(UserRegistrationPage.objLastName, generateRandomString(1), "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_80",
				"TC_Ring_Core_80 - To verify the user is not able to 'Continue' with entering only 'Last Name' "
						+ "initial in Last name field with all valid details");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		type(UserRegistrationPage.objLastName, "pqr123", "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_81",
				"TC_Ring_Core_81 - To verify the user is not able to 'Continue' with alphanumeric characters "
						+ "entered in 'Last Name' field with all valid details");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		type(UserRegistrationPage.objLastName, "pqr123+=", "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_82",
				"TC_Ring_Core_82 - To verify the user is not able to 'Continue' with special characters "
						+ "entered in 'Last Name' field with all valid details");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		type(UserRegistrationPage.objLastName, "  ", "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_83",
				"TC_Ring_Core_83 - To verify the user is not able to 'Continue' with <Space> entered in 'Last "
						+ " Name' field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First Name text field");
		Aclick(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "Shak", "First Name text field");
		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "Shak", "Last name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		hideKeyboard();
		Thread.sleep(3000);
		String toast_85 = getText(UserRegistrationPage.objToast);
		System.out.println(toast_85);
		softAssertion.assertEquals("First and Last name should be different", toast_85);
		extent.extentLoggerPass("TC_Ring_Core_85",
				"TC_Ring_Core_85 - To verify the user is not able to 'Continue' with entering same 'First Name' "
						+ "and  'Last Name'");

		clearField(UserRegistrationPage.objFirstName, "First Name text field");
		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		Aclick(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "Shak Shak", "First Name text field");
		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "huss", "First Name text field");
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		Thread.sleep(3000);
		String toast_86 = getText(UserRegistrationPage.objToast);
		System.out.println(toast_86);
		softAssertion.assertEquals("Enter valid First Name ", toast_86);
		extent.extentLoggerPass("TC_Ring_Core_86",
				"TC_Ring_Core_86 - To verify the user is not able to 'Continue' with entering same 'First Name' repeatedly "
						+ "in same field");

		clearField(UserRegistrationPage.objFirstName, "First Name text field");
		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		Aclick(UserRegistrationPage.objFirstName, "Last name text field");
		type(UserRegistrationPage.objFirstName, "shak", "First Name text field");
		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "huss huss", "First Name text field");
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		Thread.sleep(3000);
		String toast_87 = getText(UserRegistrationPage.objToast);
		System.out.println(toast_87);
		softAssertion.assertEquals("Enter valid Last Name ", toast_87);
		extent.extentLoggerPass("TC_Ring_Core_87",
				"TC_Ring_Core_87 - To verify the user is not able to 'Continue' with entering same 'Last Name' repeatedly "
						+ "in same field");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "huss", "First Name text field");
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		Thread.sleep(3000);
		String toast_88 = getText(UserRegistrationPage.objToast);
		System.out.println(toast_88);
		softAssertion.assertEquals("The email field is required.", toast_88);
		extent.extentLoggerPass("TC_Ring_Core_88",
				"TC_Ring_Core_88 - To verify the user is not able to 'Continue' with by keeping 'Email Address' field "
						+ "empty with all valid details");

		Aclick(UserRegistrationPage.objUserEmail, "Email text field");
		explicitWaitVisibility(UserRegistrationPage.objNoneOfAbove, 10);
		Aclick(UserRegistrationPage.objNoneOfAbove, "None of the above button");
		type(UserRegistrationPage.objUserEmail, "huss^^@gmail.com", "Email text field");
		Thread.sleep(3000);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objEmailError, 10);
		String emailErrorTxt = getText(UserRegistrationPage.objEmailError);
		softAssertion.assertEquals("Please enter valid email id", emailErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_89",
				"TC_Ring_Core_89 - To verify the user is not able to 'Continue' with by entering 'Email Address' in "
						+ "invalid format OR with special characters with all valid details");

		clearField(UserRegistrationPage.objUserEmail, "Email text field");
		type(UserRegistrationPage.objUserEmail, "huss  @gmail.com", "Email text field");
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objEmailError, 10);
		softAssertion.assertEquals("Please enter valid email id", emailErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_90",
				"TC_Ring_Core_90 - To verify the user is not able to 'Continue' with <Space> entered in 'Email Address' "
						+ " field with all valid details");

		hideKeyboard();
		clearField(UserRegistrationPage.objUserEmail, "Email text field");
		setWifiConnectionToONOFF("Off");
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objInternetError, 10);
		String intError = getText(UserRegistrationPage.objInternetError);
		softAssertion.assertEquals(" Check your connection & try again ", intError);
		extent.extentLoggerPass("TC_Ring_Core_96",
				"TC_Ring_Core_96 - To verify the user is getting 'Check internet connection' screen after clicking on "
						+ "'Continue' button when the Device internet connection is down");

		setWifiConnectionToONOFF("On");
		explicitWaitClickable(UserRegistrationPage.objGotItBtn, 10);
		Aclick(UserRegistrationPage.objGotItBtn, "Okay Got It button");
		explicitWaitVisibility(UserRegistrationPage.objUserDetailsHeader, 10);
		extent.extentLoggerPass("TC_Ring_Core_97",
				"TC_Ring_Core_97 - To verify the user is able to 'Continue' after 'Okay Got It' once the device internet "
						+ " connection Up");

		hideKeyboard();
		userDetails();
		ageSelect("greaterthanequalto18 || lessthanequalto55", "Sep", "20", "1998", 1998, 20);
		ageCheckGreaterThanEqualTo18AndLessThanEqualTo55("greaterthanequalto18 || lessthanequalto55", "Sep", "20",
				"1998", 1998, 20);
		extent.extentLoggerPass("TC_Ring_Customer_Seg_59",
				"TC_Ring_Customer_Seg_59 - To verify users provided age exact 18 years");
		extent.extentLoggerPass("TC_Ring_Customer_Seg_60",
				"TC_Ring_Customer_Seg_60 - To verify users provided age exact 55 years");
		extent.extentLoggerPass("TC_Ring_Customer_Seg_61",
				"TC_Ring_Customer_Seg_61 - To verify users age between 18 to 55 years");
		userDetails();
		ageSelect("lessthan18", "Oct", "12", "2010", 2010, 12);
		ageCheckLessThan18("lessthan18", "Oct", "12", "2010", 2010, 12);
		extent.extentLoggerPass("TC_Ring_Customer_Seg_57",
				"TC_Ring_Customer_Seg_57 - To verify users provided age <18 years");
		userDetails();
		ageSelect("greaterthan55", "Oct", "12", "1966", 1966, 12);
		ageCheckLessThan18("greaterthan55", "Oct", "12", "1966", 1966, 12);
		extent.extentLoggerPass("TC_Ring_Customer_Seg_58",
				"TC_Ring_Customer_Seg_58 - To verify users provided age >55 years");

		userDetails();
		dateOfBirth("Oct", "12", "1995");
		Aclick(RingUserDetailPage.objRegisterBtn, "Register Button");
		waitTime(5000);
		offerScreen1();

		extent.extentLoggerPass("TC_Ring_Core_78",
				"TC_Ring_Core_78 - To verify the user is able to 'Continue' with entering valid 'First Name' with all "
						+ "valid details");
		extent.extentLoggerPass("TC_Ring_Core_84",
				"TC_Ring_Core_84 - To verify the user is able to 'Continue' with entering valid 'Last Name' with all "
						+ "valid details");
		extent.extentLoggerPass("TC_Ring_Core_91",
				"TC_Ring_Core_91 - To verify the user is able to 'Continue' with entering valid 'Email Address' with all "
						+ "valid details");
		extent.extentLoggerPass("TC_Ring_Core_95",
				"TC_Ring_Core_95 - To verify whether the user is able to 'Continue' when the 'Gender' is selected "
						+ "from the drop down");
		extent.extentLoggerPass("TC_Ring_Core_100",
				"TC_Ring_Core_100 - To verify when user successfully enters all valid details and clicks on continue button");
		// New push//
		ringPayLogout();

	}

	public void merchantFlow() throws Exception {

		extent.HeaderChildNode("RingPay App Merchant Flow");
		explicitWaitVisibility(RingPayMerchantFlowPage.objScanQRCodeText, 10);
		if (verifyIsElementDisplayed(RingPayMerchantFlowPage.objScanQRCodeText,
				getTextVal(RingPayMerchantFlowPage.objScanQRCodeText, "Text"))) {
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objScanQRCodeText),
					"Scan any QR to get started");
			verifyElementPresent(RingPayMerchantFlowPage.obDontHaveQRCodeText,
					getTextVal(RingPayMerchantFlowPage.obDontHaveQRCodeText, "Text"));
			verifyElementPresent(RingPayMerchantFlowPage.objSignUpORLoginLink,
					getTextVal(RingPayMerchantFlowPage.objSignUpORLoginLink, "Text"));
			logger.info("Scanning the QR Code");
			extent.extentLogger("QR Code", "Scanning the QR Code");
			extent.extentLoggerPass("TC_Ring_Core_56",
					"TC_Ring_Core_56-To verify When User selects Enable Permission option");
		}
		verifyIsElementDisplayed(RingPayMerchantFlowPage.objCreditAtZeroPopUp,
				getTextVal(RingPayMerchantFlowPage.objCreditAtZeroPopUp, "Pop up"));
		extent.extentLoggerPass("TC_Ring_Core_57", "TC_Ring_Core_57-To verify when user Scans the QR  code");
		waitTime(3000);
		if (verifyElementPresent(RingPayMerchantFlowPage.objUseCreditLimitText,
				"Use your credit limit to complete this payment Text")) {
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objUseCreditLimitText),
					"Use your credit limit to complete this payment.");
			logger.info("Navigated to Paying to Merchant UPI details page");
			extent.extentLogger("Upi Details Page", "Navigated to Paying to Merchant UPI details page");
			verifyElementPresent(RingPayMerchantFlowPage.objPayingTo,
					getTextVal(RingPayMerchantFlowPage.objPayingTo, "Text"));
			verifyElementPresent(RingPayMerchantFlowPage.objPayTypeMethod,
					getTextVal(RingPayMerchantFlowPage.objPayTypeMethod, "Payment method"));
			verifyElementPresent(RingPayMerchantFlowPage.objUpiID,
					getTextVal(RingPayMerchantFlowPage.objUpiID, "UPI Id"));
			verifyElementPresent(RingPayMerchantFlowPage.objBenefitMsg,
					getTextVal(RingPayMerchantFlowPage.objBenefitMsg, "Text"));
			verifyElementPresent(RingPayMerchantFlowPage.objTransactionMsg,
					getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Fresh user message"));

			for (int i = 0; i <= amount.length; i++) {
				type1(RingPayMerchantFlowPage.objAmountTextField, amount[i], "Amount Field");
				String validationText = getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Text is Displayed");
				if (validationText.contains("You can pay up to")) {
					break;
				}
				logger.warn(validationText);
				extent.extentLoggerWarning("validation", validationText);
				clearField(RingPayMerchantFlowPage.objAmountTextField, "Amount text field");
			}
			click(RingPayMerchantFlowPage.objPayNowBtn, "Pay Now Button");
		} else {
			logger.info("Failed to Navigate Paying to Merchant UPI details page");
			extent.extentLogger("Upi Details Page", "Failed to Navigate Paying to Merchant UPI details page");
		}
		verifyIsElementDisplayed(RingPayMerchantFlowPage.objLoginPageHeader,
				getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
		softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign Up / Login");
		logger.info("User redirected to Signup/Login Screen");
		extent.extentLogger("login", "User redirected to Signup/Login Screen");
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithMobileCTA,
				getTextVal(RingPayMerchantFlowPage.objContinueWithMobileCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithGoogleCTA,
				getTextVal(RingPayMerchantFlowPage.objContinueWithGoogleCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithFacebookCTA,
				getTextVal(RingPayMerchantFlowPage.objContinueWithFacebookCTA, "CTA"));

		loginMobile();
		mobileNoValidation1("8123267268");
		enterOtp("888888");
		Aclick(UserRegistrationPage.objGotItBtn, "Okay Got it button");
		logger.info("User Navigated to HomePage");
		extent.extentLoggerPass("HomePage", "User Navigated to HomePage");
		explicitWaitVisibility(RingUserDetailPage.objCrossBtn, 25);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
		String availLimHeader = getText(RingLoginPage.objAvailLimitHeader);
		softAssertion.assertEquals("Available Limit", availLimHeader);

		extent.extentLoggerPass("TC_Ring_Core_58", "TC_Ring_Core_58-To verify the Screen when the Pop up disappers");
		extent.extentLoggerPass("TC_Ring_Core_59", "TC_Ring_Core_59-To verify the merchant details ");
		extent.extentLoggerPass("TC_Ring_Core_60", "TC_Ring_Core_60-To verify the First Transcation  fee message ");
		extent.extentLoggerPass("TC_Ring_Core_61",
				"TC_Ring_Core_61-To Verify  when User cliks on  enter transaction amount on screen ");
		extent.extentLoggerPass("TC_Ring_Core_62",
				"TC_Ring_Core_62-To verify when user  tries to enter amount more than first transaction limit");
		extent.extentLoggerPass("TC_Ring_Core_63", "TC_Ring_Core_63-To Verify when user tries to enter 0 amount");
		extent.extentLoggerPass("TC_Ring_Core_64",
				"TC_Ring_Core_64-To Verify user enters valid amt. and clicks on the Pay now button on merchant detail page");

		softAssertion.assertAll();

	}

	public void promoCodeModule() throws Exception {
		extent.HeaderChildNode("Promo code Module");

		explicitWaitVisibility(RingPromoCodeLogin.objPromoCodePageHeaderText, 10);
		WebElement QRPromoCodeLogin = getDriver().findElement(RingPromoCodeLogin.objPromoCodePageHeaderText);
		verifyElementExist1(QRPromoCodeLogin, getText(RingPromoCodeLogin.objPromoCodePageHeaderText));
		type(RingPromoCodeLogin.objEnterAmt, "1", "Amount Field");
		String rupeeSymbol = getText(RingPromoCodeLogin.objRupeeSymbol);
		String amount = getText(RingPromoCodeLogin.objEnterAmt);
		logger.info(rupeeSymbol + amount);
		extent.extentLogger("Amount", rupeeSymbol + amount);
		verifyElementPresentAndClick(RingPromoCodeLogin.objPayBtn, getText(RingPromoCodeLogin.objPayBtn));
		explicitWaitVisibility(RingPromoCodeLogin.objLoginPageHeader, 10);
		verifyElementPresent(RingPromoCodeLogin.objLoginPageHeader, getText(RingPromoCodeLogin.objLoginPageHeader));
	}

	public void userDetails() throws Exception {
		extent.HeaderChildNode("Age Criteria");

		explicitWaitVisibility(RingUserDetailPage.objFirstName, 10);
		click(RingUserDetailPage.objFirstName, "First Name Field");
		type(RingUserDetailPage.objFirstName, "Sunil", "First Name field");

		explicitWaitVisibility(RingUserDetailPage.objLastName, 10);
		click(RingUserDetailPage.objLastName, "Last Name Field");
		type(RingUserDetailPage.objLastName, "Chatla", "Last Name field");

		explicitWaitVisibility(RingUserDetailPage.objEmail, 10);
		click(RingUserDetailPage.objEmail, "Email Field");
		click(RingUserDetailPage.objNone, "None of the Above Button");
		String email = generateRandomString(8) + "@gmail.com";
		type(RingUserDetailPage.objEmail, email, "Email Filed");
		hideKeyboard();
		// waitTime(2000);
		genderSelect("male");
	}

	public void userDetailsFromAPI(String firstName, String lastName, String email, String motherName, String gender)
			throws Exception {
		extent.HeaderChildNode("Age Criteria");

		explicitWaitVisibility(RingUserDetailPage.objFirstName, 10);
		click(RingUserDetailPage.objFirstName, "First Name Field");
		type(RingUserDetailPage.objFirstName, firstName, "First Name field");

		explicitWaitVisibility(RingUserDetailPage.objLastName, 10);
		click(RingUserDetailPage.objLastName, "Last Name Field");
		type(RingUserDetailPage.objLastName, lastName, "Last Name field");
		hideKeyboard();
		//Swipe("UP", 2);
		Aclick(UserRegistrationPage.objMothersName, "Mother's Name Text Field");
		type(UserRegistrationPage.objMothersName, motherName, "Mother's Name Text Field");
		hideKeyboard();
		explicitWaitVisibility(RingUserDetailPage.objEmail, 10);
		click(RingUserDetailPage.objEmail, "Email Field");
		if (verifyElementPresent(RingLoginPage.objNoneBtn, "None of the above popup")) {
			Aclick(RingLoginPage.objNoneBtn, "None of the above");
		}
		
		
		type(RingUserDetailPage.objEmail, email, "Email Filed");
		hideKeyboard();
		// waitTime(2000);
		genderSelect(gender);
	}

	public void ageSelect(String key, String Month, String Date, String Year, int year, int date) throws Exception {
		extent.HeaderChildNode("Age Criteria");

		switch (key) {
		case "lessthan18":
			dateOfBirth(Month, Date, Year);

			age = ageCal(year, date);
			break;

		case "greaterthan55":
			dateOfBirth(Month, Date, Year);

			age = ageCal(year, date);
			break;

		case "greaterthanequalto18 || lessthanequalto55":
			dateOfBirth(Month, Date, Year);

			age = ageCal(year, date);
			break;

		default:
			logger.info("invalid age!!");
			break;
		}

		waitTime(3000);
		click(RingUserDetailPage.objRegisterBtn, "Register Button");

		if (age < 18) {
			waitTime(10000);
			logger.warn("The present age is <18 therefore the age is: " + age);
			extent.extentLoggerWarning("age", "The present age is <18 therefore the age is: " + age);
			waitTime(10000);
			verifyElementPresent(RingUserDetailPage.objSorryMsg, getText(RingUserDetailPage.objSorryMsg));
			logger.info("Age Criteria Failed");
			extent.extentLogger("Age Verification", "Age Criteria Failed");
			verifyElementPresentAndClick(RingUserDetailPage.objHamburgerTab, "Hamburger Tab");
			verifyElementPresentAndClick(RingUserDetailPage.objProfileTabCompletedPercentage,
					"Profile Completed Percentage tab");
			verifyElementPresentAndClick(RingUserDetailPage.objLogoutBtn, "Logout Button");
			logger.info("Are you sure you want to Logout?");
			explicitWaitVisibility(RingUserDetailPage.objLogOutYesBtn, 10);
			click(RingUserDetailPage.objLogOutYesBtn, "Yes Button");
			// ringPayLogout();

		} else if (age > 55) {

			logger.warn("The present age is >55 therefore the age is: " + age);
			extent.extentLoggerWarning("age", "The present age is >55 therefore the age is: " + age);
			waitTime(10000);
			verifyElementPresent(RingUserDetailPage.objSorryMsg, getText(RingUserDetailPage.objSorryMsg));
			logger.info("Age Criteria Failed");
			extent.extentLogger("Age Verification", "Age Criteria Failed");
			verifyElementPresentAndClick(RingUserDetailPage.objHamburgerTab, "Hamburger Tab");
			verifyElementPresentAndClick(RingUserDetailPage.objProfileTabCompletedPercentage,
					"Profile Completed Percentage tab");
			verifyElementPresentAndClick(RingUserDetailPage.objLogoutBtn, "Logout Button");
			logger.info("Are you sure you want to Logout?");
			explicitWaitVisibility(RingUserDetailPage.objLogOutYesBtn, 10);
			click(RingUserDetailPage.objLogOutYesBtn, "Yes Button");
			// ringPayLogout();
		} else if (age >= 18 || age <= 55) {
			waitTime(10000);
			logger.info("The present age is >=18 & <=55 and therefor the age is: " + age);
			extent.extentLogger("age", "The present age is >=18 & <=55 and therefor the age is: " + age);
			offerScreen();
			logger.info("Age Criteria Passed");
			extent.extentLogger("Age Verification", "Age Criteria Passed");

			ringPayLogout();
		}
	}

	public void offerScreen() throws Exception {
		waitTime(10000);
		verifyElementPresent(RingUserDetailPage.objOfferPageHeader, getText(RingUserDetailPage.objOfferPageHeader));
		verifyElementPresentAndClick(RingUserDetailPage.objIAcceptCheckBox,
				"I accept the ring's Terms & Conditions and IT Act 2000. checkbox");
		verifyElementPresentAndClick(RingUserDetailPage.objAcceptOfferBtn, "Accept Button");
		explicitWaitVisibility(RingUserDetailPage.objSetPinHeader, 10);
		verifyElementPresent(RingUserDetailPage.objSetPinHeader, "Set Pin Page");
		click(RingUserDetailPage.objEnterPin, "Enter Pin Field");
		type(RingUserDetailPage.objEnterPin, "1234", "Enter pin Field");
		click(RingUserDetailPage.objReEnterPin, "Re-Enter Pin Field");
		type(RingUserDetailPage.objReEnterPin, "1234", "Re-Enter pin Field");
		verifyElementPresentAndClick(RingUserDetailPage.objSubmitBtn,
				getText(RingUserDetailPage.objSubmitBtn) + "Button");
		verifyElementPresentAndClick(RingLoginPage.objHomePageBtn, "Home Page button");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		verifyElementPresentAndClick(RingLoginPage.objNoBtn, "No button");
	}

	public void offerScreen1() throws Exception {
		waitTime(10000);
		verifyElementPresent(RingUserDetailPage.objOfferPageHeader, getText(RingUserDetailPage.objOfferPageHeader));
		verifyElementPresentAndClick(RingUserDetailPage.objIAcceptCheckBox,
				"I accept the ring's Terms & Conditions and IT Act 2000. checkbox");
		verifyElementPresentAndClick(RingUserDetailPage.objAcceptOfferBtn, "Accept Button");
		explicitWaitVisibility(RingUserDetailPage.objSetPinHeader, 10);
		verifyElementPresent(RingUserDetailPage.objSetPinHeader, "Set Pin Page");
		click(RingUserDetailPage.objEnterPin, "Enter Pin Field");
		type(RingUserDetailPage.objEnterPin, "1234", "Enter pin Field");
		click(RingUserDetailPage.objReEnterPin, "Re-Enter Pin Field");
		type(RingUserDetailPage.objReEnterPin, "1234", "Re-Enter pin Field");
		verifyElementPresentAndClick(RingUserDetailPage.objSubmitBtn,
				getText(RingUserDetailPage.objSubmitBtn) + "Button");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");

	}

	public void ageCheckLessThan18(String key, String Month, String Date, String Year1, int year1, int date)
			throws Exception {
		extent.HeaderChildNode(key);
		explicitWaitVisibility(RingLoginPage.objQrCodeHeader, 10);
		if (verifyElementPresent(RingLoginPage.objQrCodeHeader, "Don't have QR Code header text")) {
			logger.info("Don't have QR Code header");
			extent.extentLoggerPass("Don't have QR Code header", "Don't have QR Code header is displayed");

			if (verifyElementPresent(RingLoginPage.objLoginLink, "Signup/Login link")) {
				logger.info("Signup/Login link");
				Aclick(RingLoginPage.objLoginLink, "Signup/Login link");

				explicitWaitVisibility(RingLoginPage.objLoginHeader, 10);
				if (verifyElementPresent(RingLoginPage.objLoginHeader, "Signup/Login header")) {
					logger.info("Signup/Login Header");
					String loginHeaderTxt = getText(RingLoginPage.objLoginHeader);
					Assert.assertEquals(loginHeaderTxt, "Sign Up / Login");
					extent.extentLoggerPass("Signup/Login header", "Signup/Login header is displayed");

					loginMobile();

					click(RingLoginPage.objMobTextField, "Mobile text field");
					String phoneNo = "9" + RandomIntegerGenerator(9);
					type(RingLoginPage.objMobTextField, phoneNo, "Mobile text field");

					enterOtp("888888");

				}
			}

		}
	}

	public void ageCheckGreaterThanEqualTo18AndLessThanEqualTo55(String key, String Month, String Date, String Year2,
			int year2, int date) throws Exception {
		extent.HeaderChildNode("Age >=18 & <=55");
		explicitWaitVisibility(RingLoginPage.objQrCodeHeader, 10);
		if (verifyElementPresent(RingLoginPage.objQrCodeHeader, "Don't have QR Code header text")) {
			logger.info("Don't have QR Code header");
			extent.extentLoggerPass("Don't have QR Code header", "Don't have QR Code header is displayed");

			if (verifyElementPresent(RingLoginPage.objLoginLink, "Signup/Login link")) {
				logger.info("Signup/Login link");
				Aclick(RingLoginPage.objLoginLink, "Signup/Login link");

				explicitWaitVisibility(RingLoginPage.objLoginHeader, 10);
				if (verifyElementPresent(RingLoginPage.objLoginHeader, "Signup/Login header")) {
					logger.info("Signup/Login Header");
					String loginHeaderTxt = getText(RingLoginPage.objLoginHeader);
					Assert.assertEquals(loginHeaderTxt, "Sign Up / Login");
					extent.extentLoggerPass("Signup/Login header", "Signup/Login header is displayed");

					loginMobile();

					click(RingLoginPage.objMobTextField, "Mobile text field");
					String phoneNo = "9" + RandomIntegerGenerator(9);
					type(RingLoginPage.objMobTextField, phoneNo, "Mobile text field");

					enterOtp("888888");
				}
			}
		}

	}

	public void cameraPermission() throws Exception {
		explicitWaitVisibility(RingLoginPage.objCamPermHeader, 30);
		verifyElementPresent(RingLoginPage.objCamPermHeader, "Camera Permission required");
		String camPermHeaderTxt = getText(RingLoginPage.objCamPermHeader);
		softAssertion.assertEquals(camPermHeaderTxt, "Camera Permission required");
		logger.info("Camera Permission required popup");
	}

	public void enablePermissions() throws Exception {
		explicitWaitVisibility(RingLoginPage.objCamPermPopUp, 10);
		Aclick(RingLoginPage.objCamPermPopUp, "Enable permissions button");
		logger.info("Foreground allow camera permissions");
		extent.extentLoggerPass("Foreground allow camera permissions", "Foreground allow camera permissions options");
		explicitWaitVisibility(RingLoginPage.allowBtnforcam, 10);
		Aclick(RingLoginPage.allowBtnforcam, "While using the app foreground camera permission option");

		explicitWaitVisibility(RingLoginPage.objQrCodeHeader, 10);
		logger.info("Don't have QR Code header");
		String qrCodeHeader = getText(RingLoginPage.objQrCodeHeader);
		Assert.assertEquals(qrCodeHeader, "Don’t have a QR code?");

		/*
		 * explicitWaitVisibility(RingLoginPage.objLoginLink, 15);
		 * Aclick(RingLoginPage.objLoginLink, "Signup/Login link");
		 */
	}

	public void loginMobile() throws Exception {
		explicitWaitVisibility(RingLoginPage.objLoginMobile, 10);
		Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");

//		trueCallerPopup();
//		if (verifyElementPresent(RingLoginPage.objNoneBtn, "None of the above button")) {
//			String noneOfAboveTxt = getText(RingLoginPage.objNoneBtn);
//			softAssertion.assertEquals(noneOfAboveTxt, "NONE OF THE ABOVE");
//			Aclick(RingLoginPage.objNoneBtn, "None of the above");
//
//			explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
//			logger.info("Verify Mobile Header");
//			String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
//			softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
//			explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
//			explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
//			logger.info("Verify Mobile Header");
//			String verifyMobHeaderTxt1 = getText(RingLoginPage.objVerifyMobHeader);
//			softAssertion.assertEquals(verifyMobHeaderTxt1, "Verify Mobile");
//			explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
//		}
//
//		else {
//			explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
//			logger.info("Verify Mobile Header");
//			String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
//			softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
//			explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
//			explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
//			logger.info("Verify Mobile Header");
//			String verifyMobHeaderTxt1 = getText(RingLoginPage.objVerifyMobHeader);
//			softAssertion.assertEquals(verifyMobHeaderTxt1, "Verify Mobile");
//			explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
//		}
//			explicitWaitVisibility(RingLoginPage.objNoneBtn, 15);
//			String noneOfAboveTxt = getText(RingLoginPage.objNoneBtn);
//			softAssertion.assertEquals(noneOfAboveTxt, "NONE OF THE ABOVE");
//
//			Aclick(RingLoginPage.objNoneBtn, "None of the above");

//		explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
//		logger.info("Verify Mobile Header");
//		String verifyMobHeaderTxt1 = getText(RingLoginPage.objVerifyMobHeader);
//		softAssertion.assertEquals(verifyMobHeaderTxt1, "Verify Mobile");
//		explicitWaitVisibility(RingLoginPage.objMobTextField, 10);

	}

	public void trueCallerPopup() throws Exception {
		if (verifyElementPresent(RingLoginPage.objTruSkipBtn, "True caller popup")) {
			Aclick(RingLoginPage.objTruSkipBtn, "True caller skip button");
		} else {
			System.out.println("Truecaller is not installed");
			logger.info("Truecaller is not installed");
		}
	}

	public String mobileNoValidation(String mobNo) throws Exception {
		// public String noTxt;
		waitTime(4000);
//		if (verifyElementPresent1(RingLoginPage.objTruSkipBtn, "True caller popup")) {
//			Aclick(RingLoginPage.objTruSkipBtn, "True caller skip button");
//			if (verifyElementPresent1(RingLoginPage.objNoneBtn, "None of the above button")) {
//				String noneOfAboveTxt = getText(RingLoginPage.objNoneBtn);
//				softAssertion.assertEquals(noneOfAboveTxt, "NONE OF THE ABOVE");
//				Aclick(RingLoginPage.objNoneBtn, "None of the above");
//
//				explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
//				logger.info("Verify Mobile Header");
//				String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
//				softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
//				waitTime(4000);
//				Aclick(RingLoginPage.objMobTextField, "Mobile text field");
//				type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
//				System.out.println(mobNo);
//				waitTime(5000);
//				String noTxt = getText(RingLoginPage.objMobTextField);
//				if (!verifyIsElementDisplayed(RingLoginPage.objNextBtn)) {
//					logger.info("Navigated to OTP Page");
//				} else {
//					Aclick(RingLoginPage.objNextBtn, "Next Button");
//				}
//			}
//			return noTxt;
//		} else {
		explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
			logger.info("True caller not displayed");
			Aclick(RingLoginPage.objMobTextField, "Mobile text field");
			type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
			System.out.println(mobNo);
			noTxt = getText(RingLoginPage.objMobTextField);
			if (!verifyIsElementDisplayed(RingLoginPage.objNextBtn)) {
				logger.info("Navigated to OTP Page");
			} else {
				Aclick(RingLoginPage.objNextBtn, "Next Button");
			}

			return noTxt;
		}
	//}

	public String mobileNoValidation1(String mobNo) throws Exception {
		waitTime(4000);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
		System.out.println(mobNo);
		waitTime(2000);
		String noTxt = getText(RingLoginPage.objMobTextField);
		if (!verifyIsElementDisplayed(RingLoginPage.objNextBtn)) {
			logger.info("Navigated to OTP Page");
		} else {
			click(RingLoginPage.objNextBtn, "Next Button");
		}
		return noTxt;

	}

	public void enterOtp(String otp) throws Exception {
		// explicitWaitVisibility(RingLoginPage.OtpAutoRead, 10);
		explicitWaitClickable(RingLoginPage.resendOtpTxt, 10);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, otp, "Enter OTP");
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void gmailLogin(String userId, String password) throws Exception {
		extent.HeaderChildNode("User Play store Flow Module");
		cameraPermission();
		enablePermissions();
		explicitWaitVisibility(SignUP_LoginPage.continueWithGmail, 20);
		Aclick(SignUP_LoginPage.continueWithGmail, "Continue With Gmail Account");
		if (verifyElementPresent(GmailLoginPage.addAnotherAccount, "Add Another Account")) {
			Aclick(GmailLoginPage.addAnotherAccount, "Add Another Account");
		}

		explicitWaitVisibility(GmailLoginPage.enterEmailID, 20);
		Aclick(GmailLoginPage.enterEmailID, "Enter Email Field");
		type(GmailLoginPage.enterEmailID, userId, "Entered Email ID");
		Aclick(GmailLoginPage.nextButton, "Next Button");
		explicitWaitVisibility(GmailLoginPage.enterPassword, 20);
		// Aclick(GmailLoginPage.enterPassword, "Enter Password Field");
		type(GmailLoginPage.enterPassword, password, "Password");
		Aclick(GmailLoginPage.nextButton, "Next Button");

		if (verifyElementPresent(GmailLoginPage.txtKeepYourAccountUpdate,
				"Keep Your account updated with phone number")) {
			scrollToBottomOfPage();
			Aclick(GmailLoginPage.btnYesImIn, "Yes I'm in");
		}

		explicitWaitVisibility(GmailLoginPage.btnIAgree, 20);
		Aclick(GmailLoginPage.btnIAgree, "I Agree Button");
		explicitWaitVisibility(GmailLoginPage.txtGoogleServices, 20);

		Aclick(GmailLoginPage.btnMore, "More Button");
		explicitWaitVisibility(GmailLoginPage.btnAccept, 20);
		Aclick(GmailLoginPage.btnAccept, "Accept Button");
		waitTime(10000);
	}

	/**
	 * 
	 * @param mobNo
	 * @param otp
	 * @throws Exception
	 */

	// TC28
	public void otpTimerStart(String mobNo, String otp) throws Exception {
		extent.HeaderChildNode("OTP Timer Start With Caption OTP Box Disable");
		cameraPermission();
		enablePermissions();
		explicitWaitVisibility(RingLoginPage.objLoginMobile, 10);
		Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");
		explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
		hideKeyboard();
		explicitWaitVisibility(MobileLoginPage.txtEnterOTP, 20);
		String getAutoreadValidation = getText(MobileLoginPage.txtAutoReadingOTP);
		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");
		explicitWaitVisibility(MobileLoginPage.txtOtpTimeStamp, 20);
		String otpTimeStamp = getText(MobileLoginPage.txtOtpTimeStamp);
		otpTimeStamp = otpTimeStamp.substring(0, 5);
		System.out.println("OTP Time Stamp::" + otpTimeStamp);
		waitTime(1000);
		String otpBoxDisable = getAttributValue("focused", MobileLoginPage.enterOTPNumberFiled);
		System.out.println("Element Focus:: " + otpBoxDisable);
		softAssertion.assertEquals("00:0", otpTimeStamp);
		if (otpBoxDisable.equals("false")) {
			softAssertion.assertEquals("false", otpBoxDisable);
			logger.info("OTP Box Disable");
			extent.extentLoggerPass("OTP Box Disable", "OTP Box Disable");
		} else {
			logger.info("OTP Box Enabled");
			extent.extentLoggerFail("OTP Box Enable", "OTP Box Enabled");
		}
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, otp, "Enter OTP");
		waitTime(10000);
	}

	// TC29

	public void blockUserAfter_5_Attemp(String mobNo, String otp) throws Exception {
		extent.HeaderChildNode("Block Number For 2 Minutes If Attemp More Than 5 Time");
		cameraPermission();
		enablePermissions();
		explicitWaitVisibility(RingLoginPage.objLoginMobile, 10);
		Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");
		explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
		hideKeyboard();
		explicitWaitVisibility(MobileLoginPage.txtEnterOTP, 20);
		String getAutoreadValidation = getText(MobileLoginPage.txtAutoReadingOTP);
		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");
		explicitWaitVisibility(MobileLoginPage.txtOtpTimeStamp, 20);
		String otpTimeStamp = getText(MobileLoginPage.txtOtpTimeStamp);
		otpTimeStamp = otpTimeStamp.substring(0, 5);
		System.out.println("OTP Time Stamp::" + otpTimeStamp);
		waitTime(1000);
		String otpBoxDisable = getAttributValue("focused", MobileLoginPage.enterOTPNumberFiled);
		System.out.println("Element Focus:: " + otpBoxDisable);
		softAssertion.assertEquals("00:0", otpTimeStamp);
		if (otpBoxDisable.equals("false")) {
			softAssertion.assertEquals("false", otpBoxDisable);
			logger.info("OTP Box Disable");
			extent.extentLoggerPass("OTP Box Disable", "OTP Box Disable");
		} else {
			logger.info("OTP Box Enabled");
			extent.extentLoggerFail("OTP Box Enable", "OTP Box Enabled");
		}
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, otp, "Enter OTP");
		waitTime(10000);
	}

	public void dateOfBirth(String month, String date, String year) throws Exception {
		explicitWaitClickable(UserRegistrationPage.objUserDOB, 10);
		Aclick(UserRegistrationPage.objUserDOB, "DOB field");
//		Aclick(UserRegistrationPage.objUserDOB, "DOB field");
		if (!verifyElementDisplayed(UserRegistrationPage.objGenderCancelBtn)) {
			Aclick(UserRegistrationPage.objUserDOB, "DOB field");
		}

		Aclick(UserRegistrationPage.objDatePickerYear, "Year field");
		clearField(UserRegistrationPage.objDatePickerYear, "Year field");
		type(UserRegistrationPage.objDatePickerYear, year, "Year field");

		explicitWaitVisibility(UserRegistrationPage.objDatePickerMonth, 10);
		Aclick(UserRegistrationPage.objDatePickerMonth, "Month field");
		clearField(UserRegistrationPage.objDatePickerMonth, "Month field");
		type(UserRegistrationPage.objDatePickerMonth, month, "Month field");

		Aclick(UserRegistrationPage.objDatePickerDate, "Date field");
		clearField(UserRegistrationPage.objDatePickerDate, "Date field");
		type(UserRegistrationPage.objDatePickerDate, date, "Date field");

		Aclick(UserRegistrationPage.objOK, "OK button");

	}

	public void dateOfBirthForAPI(String month, String date, String year) throws Exception {
		explicitWaitClickable(UserRegistrationPage.objUserDOB, 10);
		Aclick(UserRegistrationPage.objUserDOB, "DOB field");
//		Aclick(UserRegistrationPage.objUserDOB, "DOB field");
		if (!verifyElementDisplayed(UserRegistrationPage.objGenderCancelBtn)) {
			Aclick(UserRegistrationPage.objUserDOB, "DOB field");
		}

		Aclick(UserRegistrationPage.objDatePickerYear, "Year field");
		clearField(UserRegistrationPage.objDatePickerYear, "Year field");
		type(UserRegistrationPage.objDatePickerYear, year, "Year field");

		explicitWaitVisibility(UserRegistrationPage.objDatePickerMonthSH, 10);
		Aclick(UserRegistrationPage.objDatePickerMonthSH, "Month field");
		clearField(UserRegistrationPage.objDatePickerMonthSH, "Month field");
		type(UserRegistrationPage.objDatePickerMonthSH, month, "Month field");

		Aclick(UserRegistrationPage.objDatePickerDateSH, "Date field");
		clearField(UserRegistrationPage.objDatePickerDateSH, "Date field");
		type(UserRegistrationPage.objDatePickerDateSH, date, "Date field");

		Aclick(UserRegistrationPage.objOK, "OK button");

	}

	public void genderSelect(String gender) throws Exception {
		waitTime(5000);
//		click(UserRegistrationPage.objGenderSelect, "Gender dropdown");
//		click(UserRegistrationPage.objGenderSelect, "Gender dropdown");

		waitTime(3000);
		if (gender.equalsIgnoreCase("Male")) {
			explicitWaitVisibility(UserRegistrationPage.objMale, 10);
			hideKeyboard();
			click(UserRegistrationPage.objMale, "Male gender");
			click(UserRegistrationPage.objMale, "Male gender");
			
		}

		else {
			explicitWaitVisibility(UserRegistrationPage.objFemale, 10);
			hideKeyboard();
			Aclick(UserRegistrationPage.objFemale, "Female gender");
			
		}
	}

	/**
	 * Business method for RingPay Application Login
	 * 
	 * @param = Mobile Number
	 * 
	 */
	public void ringPayLogin(String mobileNumber) throws Exception {
		extent.HeaderChildNode("RingPay App Login");

		explicitWaitVisibility(RingLoginPage.objQrCodeHeader, 10);
		if (verifyElementPresent(RingLoginPage.objQrCodeHeader, "Don't have QR Code header text")) {
			logger.info("Don't have QR Code header");
			extent.extentLoggerPass("Don't have QR Code header", "Don't have QR Code header is displayed");

			if (verifyElementPresent(RingLoginPage.objLoginLink, "Signup/Login link")) {
				logger.info("Signup/Login link");
				Aclick(RingLoginPage.objLoginLink, "Signup/Login link");

				explicitWaitVisibility(RingLoginPage.objLoginHeader, 10);
				if (verifyElementPresent(RingLoginPage.objLoginHeader, "Signup/Login header")) {
					logger.info("Signup/Login Header");
					String loginHeaderTxt = getText(RingLoginPage.objLoginHeader);
					Assert.assertEquals(loginHeaderTxt, "Sign Up / Login");
					extent.extentLoggerPass("Signup/Login header", "Signup/Login header is displayed");

					explicitWaitVisibility(RingLoginPage.objLoginMobile, 10);
					Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");

					Wait(2000);
					if (verifyElementPresent(RingLoginPage.objTruSkipBtn, "Truecaller skip button")) {
						logger.info("True caller popup");
						extent.extentLoggerPass("True caller popup", "True caller popup is displayed");

						Aclick(RingLoginPage.objTruSkipBtn, "Truecaller skip button");
//						explicitWaitVisibility(RingLoginPage.objNoneBtn, 10);
//						Aclick(RingLoginPage.objNoneBtn, "None of the above");

						explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
						String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
						Assert.assertEquals(verifyMobHeaderTxt, "Verify Mobile");

						type(RingLoginPage.objMobTextField, mobileNumber, "Mobile text field");

						explicitWaitVisibility(RingLoginPage.objOtpHeader, 10);
						if (verifyElementPresent(RingLoginPage.objOtpHeader, "OTP page")) {
							String otpHeaderTxt = getText(RingLoginPage.objOtpHeader);
							Assert.assertEquals(otpHeaderTxt, "Enter OTP");

							waitTime(3000);
							Aclick(RingLoginPage.objOtpTxtField1, "OTP Text Field");
							for (int i = 1; i <= 6; i++) {
								type(RingLoginPage.objOtpTxtField1, "8", " " + i + "th OTP text Field");
							}

							explicitWaitVisibility(RingLoginPage.objRingPermissionsHeader, 10);

							if (verifyElementPresent(RingLoginPage.objRingPermissionsHeader, "RingPay permissions")) {

								logger.info("Ring Pay Permissions page (SMS, LOCATION & PHONE)");
								String ringPermissionTxt = getText(RingLoginPage.objRingPermissionsHeader);
								Assert.assertEquals(ringPermissionTxt, "Permissions");

								Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");

								explicitWaitVisibility(RingLoginPage.objLocAccess, 10);

								Aclick(RingLoginPage.objLocAccess, "Location Access option");
								Aclick(RingLoginPage.objPhoneAccess, "Phone access option");
								Aclick(RingLoginPage.objSMSAccess, "SMS access option");
							}

							else {
								logger.info("RingPay permissions page is not displayed");
								extent.extentLoggerFail("RingPay permissions page",
										"RingPay permissions page is not Displayed");

							}
						}

						else {
							logger.info("OTP Page not displayed");
							extent.extentLoggerFail("OTP Page", "OTP Page not displayed");
						}
					}

					else {
//						logger.info("User does not have TrueCaller application");
//						explicitWaitVisibility(RingLoginPage.objNoneBtn, 10);
//						Aclick(RingLoginPage.objNoneBtn, "None of the above");

						explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
						String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
						Assert.assertEquals(verifyMobHeaderTxt, "Verify Mobile");

						if (verifyElementPresent(RingLoginPage.objOtpHeader, "OTP page")) {
							String otpHeaderTxt = getText(RingLoginPage.objOtpHeader);
							Assert.assertEquals(otpHeaderTxt, "Enter OTP");

							waitTime(3000);
							for (int i = 1; i <= 6; i++) {
								type(RingLoginPage.objOtpTxtField1, "8", " " + i + "st OTP text Field");
							}

							explicitWaitVisibility(RingLoginPage.objRingPermissionsHeader, 10);
							if (verifyElementPresent(RingLoginPage.objRingPermissionsHeader, "RingPay permissions")) {

								logger.info("Ring Pay Permissions page (SMS, LOCATION & PHONE)");
								String ringPermissionTxt = getText(RingLoginPage.objRingPermissionsHeader);
								Assert.assertEquals(ringPermissionTxt, "Permissions");

								Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");

								explicitWaitVisibility(RingLoginPage.objLocAccess, 10);

								Aclick(RingLoginPage.objLocAccess, "Location Access option");
								Aclick(RingLoginPage.objPhoneAccess, "Phone access option");
								Aclick(RingLoginPage.objSMSAccess, "SMS access option");
							}

							else {
								logger.info("RingPay permissions page is not displayed");
								extent.extentLoggerFail("RingPay permissions page",
										"RingPay permissions page is not Displayed");

							}

						}

						else {
							logger.info("OTP Page not displayed");
							extent.extentLoggerFail("OTP Page", "OTP Page not displayed");
						}
					}
				}
			}
		}

		else {
			logger.info("Don't have QR Code header is not displayed");
			extent.extentLoggerFail("Don't have QR Code header", "Don't have QR Code header is not Displayed");

		}

	}

	/**
	 * Business method for RingPay payment to merchant
	 * 
	 * @param merchant UPI ID, Above limit amount, Withing limit amount
	 * 
	 */
	public void ringPaymentMerchant(String merchantID, String exceedAmount, String withinLimitAmount) throws Exception {
		extent.HeaderChildNode("RingPay payment - Merchant");

		explicitWaitVisibility(RingLoginPage.objAdHeader, 10);
		if (verifyElementPresent(RingLoginPage.objAdHeader, "AD popup")) {
			String adHeaderTxt = getText(RingLoginPage.objAdHeader);
			Assert.assertEquals(adHeaderTxt, "You’ve been chosen!");

			explicitWaitVisibility(RingLoginPage.objAdCloseBtn, 10);
			Aclick(RingLoginPage.objAdCloseBtn, "AD Close button");

			explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
			if (verifyElementPresent(RingLoginPage.objAvailLimitHeader, "Available limit header")) {
				String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
				Assert.assertEquals(availLimitHeaderTxt, "Available Limit");

				explicitWaitVisibility(RingLoginPage.objScanQRBtn, 10);
				Aclick(RingLoginPage.objScanQRBtn, "QR Code button");

				explicitWaitVisibility(RingLoginPage.objCreditLimitHeader, 10);
				if (verifyElementPresent(RingLoginPage.objCreditLimitHeader, "Merchant payment page")) {
					String creditLimitHeader = getText(RingLoginPage.objCreditLimitHeader);
					Assert.assertEquals(creditLimitHeader, "Use your credit limit to complete this payment.");

					String receiverIDTxt = getText(RingLoginPage.objReceiverID);
					Assert.assertEquals(receiverIDTxt, merchantID);

					// Negative Test case - above credit limit
					logger.info("More than credit limit - Negative Test Case");
					type(RingLoginPage.objPaymentField, exceedAmount, "Amount text field");

					explicitWaitVisibility(RingLoginPage.objExceedLimitMsg, 10);
					String exceedFailTxt = getText(RingLoginPage.objExceedLimitMsg);
					Assert.assertEquals(exceedFailTxt,
							"You have entered a higher amount than your available limit. Re-enter the amount.");
					logger.info(exceedFailTxt);
					extent.extentLoggerPass("Exceeded limit test case", "Exceeded limit failure text is displayed");
					extent.extentLogger("Exceeded Limit Error Message", exceedFailTxt);

					clearField(RingLoginPage.objPaymentField, "Payment text field");

					// Positive test case - within credit limit
					logger.info("Within credit limit - Positive Test Case");
					type(RingLoginPage.objPaymentField, withinLimitAmount, "Amount text field");

					Aclick(RingLoginPage.objPayNowBtn, "Pay now button");

					explicitWaitVisibility(RingLoginPage.objConfPayHeader, 10);
					String confPayHeaderTxt = getText(RingLoginPage.objConfPayHeader);
					Assert.assertEquals(confPayHeaderTxt, "Confirm Payment");
					logger.info("Confirm payment page");
					extent.extentLoggerPass("Confirm payment page", "Confirm payment page is displayed");

					// Negative test case - Wrong MPIN
					logger.info("MPIN Negative Test Case");
					Aclick(RingLoginPage.objPinTxtField, "MPIN field");
					for (int i = 1; i <= 4; i++) {
						type(RingLoginPage.objPinTxtField, "2", " " + i + "th MPIN Text Field");
					}

					Aclick(RingLoginPage.objContinueBtn, "MPIN Continue Button");
					explicitWaitVisibility(RingLoginPage.objIncorrectPinTxt, 10);
					String pinErrorMsg = getText(RingLoginPage.objIncorrectPinTxt);

					logger.info(pinErrorMsg);
					Assert.assertEquals(pinErrorMsg, "Incorrect PIN");
					extent.extentLoggerPass("Negative case - Incorrect case",
							"Incorrect PIN error message is successfully displayed");
					extent.extentLogger("Incorrect MPIN", pinErrorMsg);

					clearField(RingLoginPage.objPinTxtField, "MPIN text field");

					// Positive test case - Correct MPIN
					logger.info("MPIN Positive Test Case");
					Aclick(RingLoginPage.objPinTxtField, "MPIN field");
					for (int i = 1; i <= 4; i++) {
						type(RingLoginPage.objPinTxtField, "1", " " + i + "th MPIN Text Field");
					}

					Aclick(RingLoginPage.objContinueBtn, "MPIN Continue Button");

					explicitWaitVisibility(RingLoginPage.objPaymentDoneHeader, 10);
					if (verifyElementPresent(RingLoginPage.objPaymentDoneHeader, "Payment Done Header")) {
						String paymentDoneTxt = getText(RingLoginPage.objPaymentDoneHeader);
						Assert.assertEquals(paymentDoneTxt, "Payment Done!");
						logger.info("Payment Done confirmation");
						extent.extentLoggerPass("Payment Done", "Payment Done confirmation");

						Aclick(RingLoginPage.objHomePageBtn, "Home page button");

						waitTime(3000);
						explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
						Assert.assertEquals(availLimitHeaderTxt, "Available Limit");
						logger.info("Back to ring pay homepage");
						extent.extentLoggerPass("RingPay homepage",
								"Back to ring pay homepage from Payment confirmation page");

						String currentSpendsTxt = getText(RingLoginPage.objCurrentSpends);
						logger.info("Current spends :: " + currentSpendsTxt);
						extent.extentLoggerPass("Current spends", "Currents spends is :: " + currentSpendsTxt);

					}

					else {
						logger.info("Payment Failed");
						extent.extentLoggerFail("Payment Failed", "Payment Failed message is displayed");

					}
				}

				else {
					logger.info("Invalid merchant QR Code");
					extent.extentLoggerFail("Invalid QR Code", "Invalid merchant QR Code - can't make payment");

				}
			}

			else {
				logger.info("User is not logged in");
				extent.extentLoggerFail("Available limit header", "Available limit header is not Displayed");
			}
		}

		else {
			explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
			if (verifyElementPresent(RingLoginPage.objAvailLimitHeader, "Available limit header")) {
				String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
				Assert.assertEquals(availLimitHeaderTxt, "Available Limit");

				explicitWaitVisibility(RingLoginPage.objScanQRBtn, 10);
				Aclick(RingLoginPage.objScanQRBtn, "QR Code button");

				explicitWaitVisibility(RingLoginPage.objCreditLimitHeader, 10);
				if (verifyElementPresent(RingLoginPage.objCreditLimitHeader, "Merchant payment page")) {
					String creditLimitHeader = getText(RingLoginPage.objCreditLimitHeader);
					Assert.assertEquals(creditLimitHeader, "Use your credit limit to complete this payment.");
				}

				else {
					logger.info("Invalid merchant QR Code");
					extent.extentLoggerFail("Invalid QR Code", "Invalid merchant QR Code - can't make payment");

				}
			}

			else {
				logger.info("User is not logged in");
				extent.extentLoggerFail("Available limit header", "Available limit header is not Displayed");
			}
		}
	}

	/**
	 * Business method for RingPay Transaction Details capturing
	 * 
	 */

	public void ringPayTransactionDetails() throws Exception {
		extent.HeaderChildNode("RingPay Transaction Details");

		explicitWaitVisibility(RingLoginPage.objTransacBtn, 10);
		Aclick(RingLoginPage.objTransacBtn, "Transaction Button");
		waitTime(3000);
		explicitWaitVisibility(RingLoginPage.objRecentTransHeader, 10);
		waitTime(3000);
		String recentTransTxt = getText(RingLoginPage.objRecentTransHeader);
		Assert.assertEquals(recentTransTxt, "Recent Transactions");
		logger.info("Recent Transaction Page is displayed");
		extent.extentLoggerPass("Recent Transactions", "Recent Transaction Page is displayed");

		explicitWaitVisibility(RingLoginPage.objMostRecentTrans, 10);
		Aclick(RingLoginPage.objMostRecentTrans, "Most recent payment transaction");

		explicitWaitVisibility(RingLoginPage.objTransacDetailHeader, 10);
		String transacDetailsTxt = getText(RingLoginPage.objTransacDetailHeader);
		Assert.assertEquals(transacDetailsTxt, "Transaction Details");
		logger.info("User is redirected to Transaction Details page");
		extent.extentLoggerPass("Transactions Details Page", "User is redirected to Transaction Details page");

		String transacNumber = getText(RingLoginPage.objTransacNumber);
		String payee = getText(RingLoginPage.objMostRecentTrans);
		logger.info("Most recent transaction number to payee " + payee + " is :: " + transacNumber);
		extent.extentLoggerPass("Transaction Number",
				"Most recent transaction number to payee " + payee + " is :: " + transacNumber);

		Aclick(RingLoginPage.objBackBtn, "Transaction Details Back Button");

		explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
		String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
		Assert.assertEquals(availLimitHeaderTxt, "Available Limit");
		logger.info("Back to ring pay homepage");
		extent.extentLoggerPass("RingPay homepage", "Back to ring pay homepage from Payment confirmation page");
	}

	/**
	 * Business method for RingPay Application repayment
	 * 
	 * @param CVV
	 * 
	 */

	public void ringRepayment(String cvv, String reloginMobNumber) throws Exception {
		extent.HeaderChildNode("RingPay Repayment");

		Aclick(RingLoginPage.objPayEarlyBtn, "Repayment button");

		explicitWaitVisibility(RingLoginPage.objRepaymentHeader, 10);
		if (verifyElementPresent(RingLoginPage.objRepaymentHeader, "Repayment header")) {
			String repayHeaderTxt = getText(RingLoginPage.objRepaymentHeader);
			Assert.assertEquals(repayHeaderTxt, "Payment");
			logger.info("Repayment page");
			extent.extentLoggerPass("Repayment page", "Repayment header is displayed");

			Aclick(RingLoginPage.objCardOptionBtn, "Net banking & Debit Card");

			explicitWaitVisibility(RingLoginPage.objLoanPaymentHeader, 10);
			if (verifyElementPresent(RingLoginPage.objLoanPaymentHeader, "Ring loan payment header")) {
				String loanPayHeaderTxt = getText(RingLoginPage.objLoanPaymentHeader);
				Assert.assertEquals(loanPayHeaderTxt, "Test Loan payment");

				Aclick(RingLoginPage.objCardOption, "Card option");

				if (verifyElementPresent(RingLoginPage.objOtpRepaymentHeader, "OTP Repayment header")) {
					// explicitWaitVisibility(RingLoginPage.objOtpRepaymentHeader,10);
					waitTime(3000);
					if (verifyElementPresent(RingLoginPage.objOtpForeAllow, "Foreground Allow Button")) {
						Aclick(RingLoginPage.objOtpForeAllow, "Foreground Allow Button");

						waitTime(5000);
						explicitWaitVisibility(RingLoginPage.objVerifyBtn, 10);
						Aclick(RingLoginPage.objVerifyBtn, "OTP Verify Button");

						waitTime(4000);
						if (verifyElementPresent(RingLoginPage.objSavedCardsHeader, "Saved Cards Header")) {
							explicitWaitVisibility(RingLoginPage.objSavedCardsHeader, 10);
							String cardHeaderTxt = getText(RingLoginPage.objSavedCardsHeader);
							Assert.assertEquals(cardHeaderTxt, "YOUR SAVED CARDS");
							logger.info("User is redirected to SAVED Cards page");
							extent.extentLoggerPass("Saved cards page", "User is redirected to SAVED Cards page");

							Aclick(RingLoginPage.objCardSelect, "Saved card ending with 1111");

							explicitWaitVisibility(RingLoginPage.objCVVField, 10);
							type(RingLoginPage.objCVVField, cvv, "CVV Text field");

							Aclick(RingLoginPage.objPayFooterBtn, "Repayment footer button");

							explicitWaitVisibility(RingLoginPage.objRazorPayHeader, 10);
							String razorHeaderTxt = getText(RingLoginPage.objRazorPayHeader);
							Assert.assertEquals(razorHeaderTxt, "Welcome to Razorpay Software Private Ltd Bank");
							logger.info("User is redirected to razor pay page");
							extent.extentLoggerPass("Razor pay", "User is redirected to razor pay page");

							Aclick(RingLoginPage.objSuccessBtn, "Payment success button");

							waitTime(21000);
							// explicitWaitVisibility(RingLoginPage.objRepaySuccessMsg,15);
							if (verifyIsElementDisplayed(RingLoginPage.objRepaySuccessMsg,
									"Your payment was successful")) {
								logger.info("App is not stuck");
								extent.extentLoggerPass("Application Readiness",
										"App is not stuck, proceeding further....");
								// explicitWaitVisibility(RingLoginPage.objRepaySuccessMsg,15);
								// verifyIsElementDisplayed(RingLoginPage.objRepaySuccessMsg,"Your payment was
								// successful");
								String repaySuccessTxt = getText(RingLoginPage.objRepaySuccessMsg);
								Assert.assertEquals(repaySuccessTxt, "Your payment was successful");
								logger.info("User is redirected to repayment success page");
								extent.extentLoggerPass("Repayment success",
										"User is redirected to repayment success page");

								Aclick(RingLoginPage.objGoHomePageBtn, "Go to homepage footer button");

								explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
								String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
								Assert.assertEquals(availLimitHeaderTxt, "Available Limit");
								logger.info("Back to ring pay homepage");
								extent.extentLoggerPass("RingPay homepage",
										"Back to ring pay homepage from transaction details page");
							} else {
								System.out.println("App is stuck");
								logger.info("App is stuck");
								extent.extentLoggerWarning("Application Readiness",
										"Application is hung in repayment success page, killing and relaunching the app...");
								closeAndroidApp();
								// User_Play_Store_Flow();
								ringPayLogin(reloginMobNumber);
								explicitWaitVisibility(RingLoginPage.objAdCloseBtn, 10);
								Aclick(RingLoginPage.objAdCloseBtn, "AD Close button");
								explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
								String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
								Assert.assertEquals(availLimitHeaderTxt, "Available Limit");
								logger.info("Back to ring pay homepage");
								extent.extentLoggerPass("RingPay homepage",
										"Back to ring pay homepage from transaction details page");
//					Aclick(RingLoginPage.objTopMenu,"Top left menu button");
//					
//					explicitWaitVisibility(RingLoginPage.objTopMenu,10);
//					Aclick(RingLoginPage.objProfileSelect,"Profile Select Button");

							}
						}
					} else {
						waitTime(5000);
						String msgOtp = fetchOtp();
						System.out.println(msgOtp);

						explicitWaitVisibility(RingLoginPage.objOtpTextField, 10);
						type(RingLoginPage.objOtpTextField, msgOtp, "OTP Text field");

						explicitWaitVisibility(RingLoginPage.objVerifyBtn, 10);
						Aclick(RingLoginPage.objVerifyBtn, "OTP Verify Button");

						explicitWaitVisibility(RingLoginPage.objSavedCardsHeader, 10);
						String cardHeaderTxt = getText(RingLoginPage.objSavedCardsHeader);
						Assert.assertEquals(cardHeaderTxt, "YOUR SAVED CARDS");
						logger.info("User is redirected to SAVED Cards page");
						extent.extentLoggerPass("Saved cards page", "User is redirected to SAVED Cards page");
					}

				} else {
					waitTime(5000);
					logger.info("User is filling card details again");
					explicitWaitVisibility(RingLoginPage.objRepayFailCardNo, 10);
					type(RingLoginPage.objRepayFailCardNo, "4111111111111111", "Card Number");
					type(RingLoginPage.objRepayFailCardExpiry, "10/25", "Card Expiry");
					type(RingLoginPage.objRepayFailHolderName, "shakir", "Card holder's name");
					type(RingLoginPage.objRepayFailCVV, "123", "Card CVV");

					Aclick(RingLoginPage.objPayFooterBtn, "Repayment footer button");

					explicitWaitVisibility(RingLoginPage.objRepayFailPopup, 15);
					String repayFailTxt = getText(RingLoginPage.objRepayFailPopup);
					Assert.assertEquals(repayFailTxt, "Something went wrong, please try again after sometime.");
					logger.info("Repayment Failed");
					closeAndroidApp();
					extent.extentLoggerWarning("Repayment Failed", "Repayment is failed logging out...");
					// extent.extentLoggerWarning(loanPayHeaderTxt, repayFailTxt)

				}

			}

			else {
				logger.info("User not redirected to loan payment page");
				extent.extentLoggerFail("Loan payment page", "User not redirected to loan payment page");
			}
		}

		else {
			logger.info("User not redirected to repayment page");
			extent.extentLoggerFail("Repayment page", "User not redirected to repayment page");

		}

	}

	/**
	 * Business method for RingPay Application Logout
	 * 
	 */

	public void ringPayLogout() throws Exception {
		extent.HeaderChildNode("RingPay Logout");

		explicitWaitVisibility(RingLoginPage.objTopMenu, 15);
		Aclick(RingLoginPage.objTopMenu, "Top left menu button");

		explicitWaitVisibility(RingLoginPage.objProfileSelect, 10);
		Aclick(RingLoginPage.objProfileSelect, "Profile Select Button");

		explicitWaitVisibility(RingLoginPage.objLogoutBtn, 10);

		Aclick(RingLoginPage.objLogoutBtn, "Logout Button");

		explicitWaitVisibility(RingLoginPage.objLogoutTxt, 10);
		String logoutTxt = getText(RingLoginPage.objLogoutTxt);
		Assert.assertEquals(logoutTxt, "Are you sure you want to logout?");
		logger.info("Logout popup comes up");
		extent.extentLoggerPass("Logout popup", "Logout popup comes up");

		Aclick(RingLoginPage.objYesBtn, "Yes confirmation button");

		explicitWaitVisibility(RingLoginPage.objQrCodeHeader, 10);
		String logoutConfTxt = getText(RingLoginPage.objQrCodeHeader);
		//softAssertion.assertEquals(logoutConfTxt, "Don't have a QR code?");
		logger.info("User is successfully logged out");
		extent.extentLoggerPass("Logout confirmation", "User is successfully logged out");

	}

	public void kycSkipped(String url, String gender, String encrypted_name, String portalEmail, String portalPassword,String portalOTP) throws Exception {
		
		extent.HeaderChildNode("RingPay KYC_Skip");
		

		HashMap mockUserDetails=mockUserAPI(url, gender, encrypted_name);
		
		String firstName=(String) mockUserDetails.get("firstName");
		String middleName=(String) mockUserDetails.get("middleName");
		String lastName=(String) mockUserDetails.get("lastName");
		String mobileNumber=(String) mockUserDetails.get("mobileNumber");
		String otp=(String) mockUserDetails.get("otp");
		String panCard=(String) mockUserDetails.get("panCard");
		String email=(String) mockUserDetails.get("email");
		String motherName=(String) mockUserDetails.get("motherName");
		String genders=(String) mockUserDetails.get("genders");
		String year=(String) mockUserDetails.get("year");
		String monthByName=(String) mockUserDetails.get("month");
		String date=(String) mockUserDetails.get("date");
		
		String roomno=(String) mockUserDetails.get("roomNo");
		String address1=(String) mockUserDetails.get("address1");
		String address2=(String) mockUserDetails.get("address2");
		String landmark=(String) mockUserDetails.get("landmark");
		String pin=(String) mockUserDetails.get("pin");
		
		
		

		cameraPermission();
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink, "Signup/Login link");
		loginMobile();    
		mobileNoValidation1(mobileNumber);
		waitTime(5000);
		enterOtp(otp);
		explicitWaitVisibility(MobileLoginPage.btnReadAndAccept, 10);
		Aclick(MobileLoginPage.btnReadAndAccept, "Read adn Accept");
		explicitWaitVisibility(PermissionPage.btnAllow, 20);
		Aclick(PermissionPage.btnAllow, "Allow Call");
		explicitWaitVisibility(PermissionPage.btnAllowLocation, 20);
		Aclick(PermissionPage.btnAllowLocation, "Allow Location");
		explicitWaitVisibility(PermissionPage.btnAllow, 20);
		//waitTime(15000);
		Aclick(PermissionPage.btnAllow, "Allow SMS");
		explicitWaitVisibility(UserRegistrationPage.objUserDetailsHeader, 10);
		waitTime(20000);
		userDetailsFromAPI(firstName, lastName, email, motherName, genders);
		dateOfBirthForAPI(monthByName, date, year);
		 SessionId androidSession = getDriver().getSessionId();
		System.out.println("session ID-----"+androidSession);
	
        setPlatform("Web");
        new RingPayBusinessLogic("ring");
		
		  waitTime(5000);
		type(TestingPortalWebPage.emailField, portalEmail, "Email ID Entered");
		waitTime(1000);
		JSClick(TestingPortalWebPage.continueBtn, "Continue Button");
		explicitWaitVisibility(TestingPortalWebPage.loginBtn, 10);
		clearField(TestingPortalWebPage.passwordField, "");
		waitTime(2000);
		type(TestingPortalWebPage.passwordField, portalPassword, "Portal Password");
		waitTime(2000);
		clearField(TestingPortalWebPage.otpField, "");
		waitTime(2000);
		type(TestingPortalWebPage.otpField, portalOTP, "OTP Entered");
		waitTime(2000);
		JSClick(TestingPortalWebPage.loginBtn, "Login Button");
		explicitWaitVisibility(TestingPortalWebPage.usersTab, 10);
		JSClick(TestingPortalWebPage.usersTab, "User Tab");
		explicitWaitVisibility(TestingPortalWebPage.dropDownUserRef, 10);
		JSClick(TestingPortalWebPage.dropDownUserRef, "Drop Down");
		selectByVisibleTextFromDD(TestingPortalWebPage.dropDownUserRef, "Mobile Number");
		type(TestingPortalWebPage.enterMobileNo, mobileNumber, "Portal Mobile Number");
		JSClick(TestingPortalWebPage.searchIcon, "Search Icon");
		waitTime(5000);
		String getReferenceNumber = "";
		for (int i = 0; i <= 2; i++) {
			try {
				getReferenceNumber = getWebDriver().findElement(By.xpath("//table[@class='table table-striped table-bordered ']/tbody/tr[1]/td[1]")).getText();
				break;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println("Referance----------------------------" + getReferenceNumber);
		waitTime(2000);
		ScrollToTheElement(TestingPortalWebPage.dragDown);
		System.out.println("drag done");
		waitTime(2000);
		JSClick(TestingPortalWebPage.othersTab, "Other Tab");
		waitTime(1500);
		ScrollToTheElement(TestingPortalWebPage.dragDown);
		waitTime(2000);
		explicitWaitVisibility(TestingPortalWebPage.panNSDLDataTab, 20);
		
		JSClick(TestingPortalWebPage.panNSDLDataTab, "PAN NSDL Data");
		explicitWaitVisibility(TestingPortalWebPage.addNewIcon, 10);
		JSClick(TestingPortalWebPage.addNewIcon, "Add New Icon");
		explicitWaitVisibility(TestingPortalWebPage.firstName, 20);
		type(TestingPortalWebPage.firstName, firstName, "first Name");
		type(TestingPortalWebPage.middleName,middleName, "middle name");
		type(TestingPortalWebPage.lastName,lastName, "lst");
		type(TestingPortalWebPage.pan, panCard, "Pan No");
		JSClick(TestingPortalWebPage.panStatus, "pan statis");
		selectByVisibleTextFromDD(TestingPortalWebPage.panStatus, "Valid");
		JSClick(TestingPortalWebPage.panNsdlSubmitBtn, "Submit Button");
		BrowsertearDown();
		
		ValidatableResponse responseSkipKyc = Utilities.skip_kyc("https://testing-gateway.test.paywithring.com/api/v1/update-upload-equifax/updateUploadEquifax",getReferenceNumber, panCard, firstName, middleName, lastName, encrypted_name);
		String message = responseSkipKyc.extract().jsonPath().getString("message");
		System.out.println("Skip_Kyc-------------" + message);
		
		
	        setPlatform("Android");
	        initDriver();
	       
		Aclick(UserRegistrationPage.objRegister, "Proceed Button");
		
		
		waitTime(20000);
		
		if (verifyElementPresent(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress(roomno,address1,address2,landmark,pin);
		}
		waitTime(20000);
		explicitWaitVisibility(PAN_DetailsPage.btnyesThisIsMyPlan, 20);
		System.out.println(getText(PAN_DetailsPage.btnyesThisIsMyPlan));
		
		softAssertion.assertEquals("Confirm this is your PAN & name", getText(PAN_DetailsPage.txtTittle));
		softAssertion.assertEquals(panCard,getText(PAN_DetailsPage.PanNo));
		softAssertion.assertEquals(firstName+" "+middleName+" "+lastName, getText(PAN_DetailsPage.UserNameOfPAN));
		
		extent.extentLoggerPass("Popup Validation", "Hand Icon,Pan Number,Name of Pan Holder is visible_TC_Ring_Core_167");
		
		Back(2);
		logger.info("Back Button Pressed 2 Times");
		extent.extentLoggerPass("Back Button click", "Validated by doing back popup is not disappear_TC_Ring_Core_168");
		setWifiConnectionToONOFF("Off");
		waitTime(5000);
		Aclick(PAN_DetailsPage.btnnoThisIsNotMine, "No, This Is Not Mine");
		explicitWaitVisibility(PAN_DetailsPage.txtNoInternet, 20);
		softAssertion.assertEquals(" Check your connection & try again ", getText(PAN_DetailsPage.txtCheckyourInternet));
		logger.info("Internet Connection Validation");
		extent.extentLoggerPass("Validate Internet Connection Off", "Check Your Internet Connection_TC_Ring_Core_171");
		setWifiConnectionToONOFF("On");
		waitTime(8000);
		Aclick(PAN_DetailsPage.btnOkaygotIt, "Okay Got It");
		waitTime(5000);
		explicitWaitVisibility(PAN_DetailsPage.txtTittle, 20);
		
		setWifiConnectionToONOFF("Off");
		waitTime(5000);
		Aclick(PAN_DetailsPage.btnyesThisIsMyPlan, "Yes, This is My Pan");
		explicitWaitVisibility(PAN_DetailsPage.txtNoInternet, 20);
		softAssertion.assertEquals(" Check your connection & try again ", getText(PAN_DetailsPage.txtCheckyourInternet));
		logger.info("Internet Connection Validation");
		extent.extentLoggerPass("Validate Internet Connection Off", "Check Your Internet Connection_TC_Ring_Core_173");
		setWifiConnectionToONOFF("On");
		waitTime(8000);
		Aclick(PAN_DetailsPage.btnOkaygotIt, "Okay Got It");
		waitTime(25000);
		explicitWaitVisibility(PAN_DetailsPage.txtTittle, 20);
		Aclick(PAN_DetailsPage.btnnoThisIsNotMine, "No This is not Mine Button");
		explicitWaitVisibility(OfferPage.offerTitle, 20);
		logger.info("Offer Page Open When we Click on 'No this is not mine' Button");
		softAssertion.assertEquals("Accept Offer", getText(OfferPage.btnAcceptOffer));
		extent.extentLoggerPass("Offer Page Visible", "Offer Page visible when network up then press_TC_Ring_Core_172");
		extent.extentLoggerPass("Offer Page Visible", "Offer Page visible when Press we press on 'No this is not mine' Button_TC_Ring_Core_169");
		explicitWaitVisibility(OfferPage.btnAcceptOffer, 10);
		Aclick(OfferPage.ckbAcceptTermandcondition, "check box for term and conditions");
		Aclick(OfferPage.btnAcceptOffer, "Accept Offer");
		explicitWaitVisibility(TransectionPIN.txtTitleOfSetPin , 20);
		Aclick(TransectionPIN.objEnterPin , "Enter Pin");
		type(TransectionPIN.objEnterPin , "1111", "Enter Pin");
		Aclick(TransectionPIN.objReEnterPin, "Re-Enter Pin");
		type(TransectionPIN.objReEnterPin, "1111", "Re-Enter Pin");
		Aclick(TransectionPIN.objSubmit,"Submit Button");
		if (checkElementExist(HomePage.objAdCloseBtn, "Check")) {
			explicitWaitVisibility(HomePage.objAdCloseBtn, 20);
			Aclick(HomePage.objAdCloseBtn, "Ad Cross Button");
		}
		
		explicitWaitVisibility(HomePage.homeTab, 20);
		ringPayLogout();
		
	HashMap mockUserDetailsForYesBtn=mockUserAPI(url, gender, encrypted_name);
		
		String firstNameForYesBtn=(String) mockUserDetailsForYesBtn.get("firstName");
		String middleNameForYesBtn=(String) mockUserDetailsForYesBtn.get("middleName");
		String lastNameForYesBtn=(String) mockUserDetailsForYesBtn.get("lastName");
		String mobileNumberForYesBtn=(String) mockUserDetailsForYesBtn.get("mobileNumber");
		String otpForYesBtn=(String) mockUserDetailsForYesBtn.get("otp");
		String panCardForYesBtn=(String) mockUserDetailsForYesBtn.get("panCard");
		String emailForYesBtn=(String) mockUserDetailsForYesBtn.get("email");
		String motherNameForYesBtn=(String) mockUserDetailsForYesBtn.get("motherName");
		String gendersForYesBtn=(String) mockUserDetailsForYesBtn.get("genders");
		String yearForYesBtn=(String) mockUserDetailsForYesBtn.get("year");
		String monthByNameForYesBtn=(String) mockUserDetailsForYesBtn.get("month");
		String dateForYesBtn=(String) mockUserDetailsForYesBtn.get("date");
		
		String roomnoForYesBtn=(String) mockUserDetailsForYesBtn.get("roomNo");
		String address1ForYesBtn=(String) mockUserDetailsForYesBtn.get("address1");
		String address2ForYesBtn=(String) mockUserDetailsForYesBtn.get("address2");
		String landmarkForYesBtn=(String) mockUserDetailsForYesBtn.get("landmark");
		String pinForYesBtn=(String) mockUserDetailsForYesBtn.get("pin");
		
		
		

		
		Aclick(RingLoginPage.objLoginLink, "Signup/Login link");
		loginMobile();    
		mobileNoValidation1(mobileNumberForYesBtn);
		waitTime(5000);
		enterOtp(otp);
		waitTime(120000);
	
		explicitWaitVisibility(UserRegistrationPage.objUserDetailsHeader, 10);
		waitTime(20000);
		userDetailsFromAPI(firstNameForYesBtn, lastNameForYesBtn, emailForYesBtn, motherNameForYesBtn, gendersForYesBtn);
		dateOfBirthForAPI(monthByNameForYesBtn, dateForYesBtn, yearForYesBtn);
		
	
        setPlatform("Web");
        new RingPayBusinessLogic("ring");
		
		  waitTime(10000);
		type(TestingPortalWebPage.emailField, portalEmail, "Email ID Entered");
		waitTime(3000);
		JSClick(TestingPortalWebPage.continueBtn, "Continue Button");
		explicitWaitVisibility(TestingPortalWebPage.loginBtn, 10);
		clearField(TestingPortalWebPage.passwordField, "");
		waitTime(2000);
		type(TestingPortalWebPage.passwordField, portalPassword, "Portal Password");
		waitTime(2000);
		clearField(TestingPortalWebPage.otpField, "");
		waitTime(2000);
		type(TestingPortalWebPage.otpField, portalOTP, "OTP Entered");
		waitTime(2000);
		JSClick(TestingPortalWebPage.loginBtn, "Login Button");
		explicitWaitVisibility(TestingPortalWebPage.usersTab, 10);
		JSClick(TestingPortalWebPage.usersTab, "User Tab");
		explicitWaitVisibility(TestingPortalWebPage.dropDownUserRef, 10);
		JSClick(TestingPortalWebPage.dropDownUserRef, "Drop Down");
		selectByVisibleTextFromDD(TestingPortalWebPage.dropDownUserRef, "Mobile Number");
		type(TestingPortalWebPage.enterMobileNo, mobileNumberForYesBtn, "Portal Mobile Number");
		JSClick(TestingPortalWebPage.searchIcon, "Search Icon");
		waitTime(5000);
		String getReferenceNumber1 = "";
		for (int i = 0; i <= 2; i++) {
			try {
				getReferenceNumber1 = getWebDriver().findElement(By.xpath("//table[@class='table table-striped table-bordered ']/tbody/tr[1]/td[1]")).getText();
				break;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println("Referance----------------------------" + getReferenceNumber1);
		waitTime(2000);
		ScrollToTheElement(TestingPortalWebPage.dragDown);
		System.out.println("drag done");
		waitTime(2000);
		JSClick(TestingPortalWebPage.othersTab, "Other Tab");
		waitTime(1500);
		ScrollToTheElement(TestingPortalWebPage.dragDown);
		waitTime(2000);
		explicitWaitVisibility(TestingPortalWebPage.panNSDLDataTab, 20);
		
		JSClick(TestingPortalWebPage.panNSDLDataTab, "PAN NSDL Data");
		explicitWaitVisibility(TestingPortalWebPage.addNewIcon, 10);
		JSClick(TestingPortalWebPage.addNewIcon, "Add New Icon");
		explicitWaitVisibility(TestingPortalWebPage.firstName, 20);
		type(TestingPortalWebPage.firstName, firstNameForYesBtn, "first Name");
		type(TestingPortalWebPage.middleName,middleNameForYesBtn, "middle name");
		type(TestingPortalWebPage.lastName,lastNameForYesBtn, "Last Name");
		type(TestingPortalWebPage.pan, panCardForYesBtn, "Pan No");
		JSClick(TestingPortalWebPage.panStatus, "pan statis");
		selectByVisibleTextFromDD(TestingPortalWebPage.panStatus, "Valid");
		JSClick(TestingPortalWebPage.panNsdlSubmitBtn, "Submit Button");
		BrowsertearDown();
		
		ValidatableResponse responseSkipKyc1 = Utilities.skip_kyc("https://testing-gateway.test.paywithring.com/api/v1/update-upload-equifax/updateUploadEquifax",getReferenceNumber1, panCardForYesBtn, firstNameForYesBtn, middleNameForYesBtn, lastNameForYesBtn, encrypted_name);
		String message1 = responseSkipKyc1.extract().jsonPath().getString("message");
		System.out.println("Skip_Kyc-------------" + message1);
		
		
	        setPlatform("Android");
	        initDriver();
	       
		Aclick(UserRegistrationPage.objRegister, "Proceed Button");
		
		
		waitTime(200000);
		if (verifyElementPresent(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress(roomnoForYesBtn,address1ForYesBtn,address2ForYesBtn,landmarkForYesBtn,pinForYesBtn);
		}
		
		
		explicitWaitVisibility(PAN_DetailsPage.btnyesThisIsMyPlan, 20);
		click(PAN_DetailsPage.btnyesThisIsMyPlan, "Yes, This is my PAN Button");
		
		explicitWaitVisibility(OfferPage.offerTitle, 20);
		logger.info("Offer Page Open When we Click on 'Yes, This is my PAN");
		softAssertion.assertEquals("Accept Offer", getText(OfferPage.btnAcceptOffer));
		extent.extentLoggerPass("Offer Page Visible", "Offer Page visible when network up then press_TC_Ring_Core_170");
		extent.extentLoggerPass("Offer Page Visible", "Offer Page visible when Press we press on 'Yes, This is my PAN' Button_TC_Ring_Core_174");


		waitTime(20000);
	}
		
	 
	
		

		public void addAddress(String room,String address1,String address2,String landmark,String pin) throws Exception {
			   extent.HeaderChildNode("RingPay App Merchant Flow");
			//  System.out.println(getText(AddAddressPage.objAddCurrentAddressHeader));
			   explicitWaitVisibility(AddAddressPage.objAddCurrentAddressHeader, 30);
			   //waitTime(15000);
			   Aclick(AddAddressPage.objRoomNoField,"Room");
			   type(AddAddressPage.roomNopulkit,room,"Room");
			   Aclick(AddAddressPage.objAddressLineOneField,"AddressLine1");
			   type(AddAddressPage.objAddressLineOneFieldpulkit,address1,"AddressLine1");
			   Aclick(AddAddressPage.objAddressLineTwoField,"AddressLine2");
			   type(AddAddressPage.objAddressLineTwoFieldpulkit,address2,"AddressLine2");
			   Aclick(AddAddressPage.objLandmarkTextFields,"Landmark");
			   type(AddAddressPage.objLandmarkTextFieldspulkit,landmark,"Landmark");
			   hideKeyboard();
			   Aclick(AddAddressPage.objPinCodeField,"PinCode");
			   type(AddAddressPage.objPinCodeFieldpulkit,pin,"PinCode");
			   hideKeyboard();
			   waitTime(5000);
			   Aclick(AddAddressPage.objSubmitButton,"Submit");
			   
			
	}
		
		
		public void bannerLogicOnHomePage(String url, String gender, String encrypted_name,String portalEmail,String portalPassword,String portalOTP) throws Exception {
			
			extent.HeaderChildNode("Banner logic on Home screen");
			 PropertyFileReader files=new PropertyFileReader("properties\\TestData.properties");
			
			/*------------------------------Data From API----------------------------*/
			
			HashMap mockUserDetails=mockUserAPI(url, gender, encrypted_name);
			String firstName=(String) mockUserDetails.get("firstName");
			String middleName=(String) mockUserDetails.get("middleName");
			String lastName=(String) mockUserDetails.get("lastName");
			String mobileNumber=(String) mockUserDetails.get("mobileNumber");
			String otp=(String) mockUserDetails.get("otp");
			String panCard=(String) mockUserDetails.get("panCard");
			String email=(String) mockUserDetails.get("email");
			String motherName=(String) mockUserDetails.get("motherName");
			String genders=(String) mockUserDetails.get("genders");
			String year=(String) mockUserDetails.get("year");
			String monthByName=(String) mockUserDetails.get("month");
			String date=(String) mockUserDetails.get("date");
			
			String roomno=(String) mockUserDetails.get("roomNo");
			String address1=(String) mockUserDetails.get("address1");
			String address2=(String) mockUserDetails.get("address2");
			String landmark=(String) mockUserDetails.get("landmark");
			String pin=(String) mockUserDetails.get("pin");
			
			/*------------------------------Front End----------------------------*/

			cameraPermission();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink, "Signup/Login link");
			loginMobile();
			mobileNoValidation1(mobileNumber);
			enterOtp(otp);
			explicitWaitVisibility(MobileLoginPage.btnReadAndAccept, 10);
			Aclick(MobileLoginPage.btnReadAndAccept, "Read adn Accept");
			explicitWaitVisibility(PermissionPage.btnAllow, 20);
			Aclick(PermissionPage.btnAllow, "Allow Call");
			explicitWaitVisibility(PermissionPage.btnAllowLocation, 20);
			Aclick(PermissionPage.btnAllowLocation, "Allow Location");
			explicitWaitVisibility(PermissionPage.btnAllow, 20);
			Aclick(PermissionPage.btnAllow, "Allow SMS");
			waitTime(20000);
			explicitWaitVisibility(UserRegistrationPage.objUserDetailsHeader, 30);
			String userName=files.getproperty("userName");
			String userLastName=files.getproperty("lastName");
			String userMotherName=files.getproperty("motherName");
			String userGender=files.getproperty("gender");
			String userBirthDate=files.getproperty("dateOfBirthDate");
			String userBirthMonth=files.getproperty("dateOfBirthMonth");
			String userBirthYear=files.getproperty("dateOfBirthYear");
			userDetailsFromAPI(userName, userLastName, userName+userLastName+new Random().nextInt(10000)+"@example.com", userMotherName, userGender);
			dateOfBirthForAPI(userBirthMonth, userBirthDate, userBirthYear);
			Aclick(UserRegistrationPage.objRegister, "Proceed Button");
			waitTime(30000);
			if (verifyElementPresent(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
				addAddress(roomno,address1,address2,landmark,pin);
			}
			explicitWaitVisibility(OfferPage.btnAcceptOffer, 10);
			Aclick(OfferPage.ckbAcceptTermandcondition, "check box for term and conditions");
			Aclick(OfferPage.btnAcceptOffer, "Accept Offer");
			explicitWaitVisibility(TransectionPIN.txtTitleOfSetPin , 20);
			Aclick(TransectionPIN.objEnterPin , "Enter Pin");
			type(TransectionPIN.objEnterPin , "1111", "Enter Pin");
			Aclick(TransectionPIN.objReEnterPin, "Re-Enter Pin");
			type(TransectionPIN.objReEnterPin, "1111", "Re-Enter Pin");
			Aclick(TransectionPIN.objSubmit,"Submit Button");
			if (verifyElementPresent(HomePage.objAdCloseBtn, "")) {
				
				explicitWaitVisibility(HomePage.objAdCloseBtn, 20);
				Aclick(HomePage.objAdCloseBtn, "Ad Cross Button");
				explicitWaitVisibility(HomePage.homeTab, 20);
			}
			
			if(verifyElementPresent(HomePage.purpleBannerVerifyNow,"Searching Purple Banner")){
			boolean purpleBanner=swipeElementAndroid(HomePage.bannerTray,HomePage.purpleBannerVerifyNow);
			logger.info("Purple Banner--Verify KYC Doucment");
			softAssertion.assertEquals("Verify your document to get", getText(HomePage.purpleBannerText1));
			extent.extentLoggerPass("Verify your document to get", "Purple Banner is visible with text 'Verify your document to get higher limit & cashback up to ₹100 Verify Now' ----TC_Ring_Core_260");
			Aclick(HomePage.purpleBannerVerifyNow, "KYC Documents Page");
			explicitWaitVisibility(KycDocument.kycHeader, 30);
			logger.info("KYC Documents Page Open");
			softAssertion.assertEquals("KYC Documents", getText(KycDocument.kycHeader));
			extent.extentLoggerPass("KYC Document Page", "After click on verify now of purple banner,Kyc Document page Open_TC_Ring_Core_261");
			Back(1);
			explicitWaitVisibility(KycDocument.txtPopupWhileBackBtn, 20);
			Aclick(KycDocument.yesBtn, "Yes Button");
			explicitWaitVisibility(HomePage.homeTab, 20);
			}
			
			swipeElementAndroid(HomePage.bannerTray,HomePage.lightBlueBannerTransferNow);
			
			waitTime(10000);
			explicitWaitVisibility(HomePage.lightBlueBannerTransferNow, 20);
			logger.info("Ligh Blue Banner----Transfer Now");
			softAssertion.assertEquals("Transfer your RING", getText(HomePage.lightBluetext2));
			extent.extentLoggerPass("Cash Crunch? Transfer your RING limit to your bank account", "Light Blue Banner is visible with text 'Cash Crunch? Transfer your RING limit to your bank account Transfer Now'");
			boolean green=swipeElementAndroid(HomePage.bannerTray,HomePage.greenBannerText1);
			System.out.println(green);
			waitTime(10000);

			if (green==true) {
				logger.info("micro payment flag is enabled for user from Configuration");
				System.out.println(getText(HomePage.greenBannerPayNow));
				extent.extentLoggerPass("micro payment flag", "micro payment flag is enabled for user from Configuration-----------TC_Ring_Core_265");
				extent.extentLoggerPass("RINGGGG! Transactions below", "Green Banner is visible with text 'RINGGGG! Transactions below 200 are absolutely FREE! Pay Now'--------------TC_Ring_Core_266");
				Aclick(HomePage.greenBannerPayNow, "Pay Now");
				
				explicitWaitVisibility(HomePage.objScanAnyQRToPay,10);
				String sScanAnyQRCode = getText(HomePage.objScanAnyQRToPay);
				softAssertion.assertEquals("Scan any QR code to pay",sScanAnyQRCode);
				verifyElementPresent(HomePage.objScanAnyQRToPay,getTextVal(HomePage.objScanAnyQRToPay,"Text"));
				
				//Scan here
				/*------------------------------WEB----------------------------*/
				Utilities.setPlatform = "Web";
	            new CommandBase("Chrome");
	            waitTime(4000);
	            String projectPath=System.getProperty("user.dir");
	            getWebDriver().get(projectPath+"\\Mock_Files\\QRCode.png");
	            waitTime(25000);
	            BrowsertearDown();
	            
	            /*------------------------------Android----------------------------*/
	            setPlatform("Android");
	            initDriver();
				
				if(verifyElementPresent(PaymentPage.objAmountTextField,"Amount Text Field")) {
					   Aclick(PaymentPage.objAmountTextField, "Amount Field");
					   type(PaymentPage.objAmountTextField, "100", "Amount Field");
				}
				Aclick(PaymentPage.objPayNowBtn, "Pay Now Button");
				waitTime(30000);
				if(verifyElementPresent(PaymentPage.objConfirmPayment,getTextVal(PaymentPage.objConfirmPayment,"Text"))){
					   Aclick(PaymentPage.objEditTransactionPin,"Edit Pin Field");
					   type(PaymentPage.objEditTransactionPin,"1111","Edit Pin Field");
					   Aclick(PaymentPage.objContinue,"Continue Button");
				}
				if(verifyElementPresent(PaymentPage.objPaymentDone,getTextVal(PaymentPage.objPaymentDone,"Text"))){
					   String sPaymentDone = getText(PaymentPage.objPaymentDone);
					   softAssertion.assertEquals("Payment Done!", sPaymentDone);
					   verifyElementPresent(PaymentPage.objGreenTickMark,"Payment Done green tick mark");
					   verifyElementPresent(PaymentPage.objRupeesLogo,getTextVal(PaymentPage.objRupeesLogo,"is Rupees Logo"));
					   verifyElementPresent(PaymentPage.objMerchantNameReceipt,getTextVal(PaymentPage.objMerchantNameReceipt,"Text"));
					   verifyElementPresent(PaymentPage.objTaxDateAndTime,getTextVal(PaymentPage.objTaxDateAndTime,"is Tax date and time"));
					   verifyElementPresent(PaymentPage.objSeekBar,"Seek Bar");
					   verifyElementPresent(PaymentPage.objAvailableLimit,getTextVal(PaymentPage.objAvailableLimit,"Text"));
					   verifyElementPresent(PaymentPage.ObjAvailableLimitAmount,getTextVal(PaymentPage.ObjAvailableLimitAmount,"is Available Limit Amount"));
					   verifyElementPresent(PaymentPage.objGoToHomePage,getTextVal(PaymentPage.objGoToHomePage,"Text"));
					   System.out.println("-----------------------------------------------------------");
					   Aclick(PaymentPage.objGoToHomePage, "Goto HomePage");
					   waitTime(5000);
					  if (verifyElementPresent(HomePage.txtAreYouEnjoyingApp,"Text")) {
						Aclick(HomePage.noBtn, "No Button");
					  }
					}
				
				
			}
			else {
				logger.warn("micro payment flag is Not enabled for user from Configuration");
			}
			
			/*------------------------------WEB----------------------------*/
			
			setPlatform("Web");
			new RingPayBusinessLogic("ring");
			waitTime(3000);
			type(TestingPortalWebPage.emailField, portalEmail, "Email ID Entered");
			waitTime(3000);
			JSClick(TestingPortalWebPage.continueBtn, "Continue Button");
			explicitWaitVisibility(TestingPortalWebPage.loginBtn, 10);
			clearField(TestingPortalWebPage.passwordField, "");
			waitTime(2000);
			type(TestingPortalWebPage.passwordField, portalPassword, "Portal Password");
			waitTime(2000);
			clearField(TestingPortalWebPage.otpField, "");
			waitTime(2000);
			type(TestingPortalWebPage.otpField, portalOTP, "OTP Entered");
			waitTime(2000);
			JSClick(TestingPortalWebPage.loginBtn, "Login Button");
			explicitWaitVisibility(TestingPortalWebPage.usersTab, 10);
			JSClick(TestingPortalWebPage.usersTab, "User Tab");
			explicitWaitVisibility(TestingPortalWebPage.dropDownUserRef, 10);
			JSClick(TestingPortalWebPage.dropDownUserRef, "Drop Down");
			selectByVisibleTextFromDD(TestingPortalWebPage.dropDownUserRef, "Mobile Number");
			type(TestingPortalWebPage.enterMobileNo, mobileNumber, "Portal Mobile Number");
			JSClick(TestingPortalWebPage.searchIcon, "Search Icon");
			waitTime(5000);
			String getReferenceNumber = "";
			for (int i = 0; i <= 2; i++) {
				try {
					getReferenceNumber = getWebDriver().findElement(By.xpath("//table[@class='table table-striped table-bordered ']/tbody/tr[1]/td[1]")).getText();
					break;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			System.out.println("Referance----------------------------" + getReferenceNumber);
			
			BrowsertearDown();
			
			/*------------------------------WEB----------------------------*/
			
			Utilities.setPlatform = "Web";
            new CommandBase("Chrome");
            waitTime(4000);
            getWebDriver().get("https://new-admin-panel.test.ideopay.in/sign-in");
            waitTime(3000);
            explicitWaitVisibility(TestingPortalWebPage.emailFieldNewPortal, 20);
            type(TestingPortalWebPage.emailFieldNewPortal, "shashi.thakur@collabera.com", "Email Entered");
            JSClick(TestingPortalWebPage.continueNewButton,"Continue Button");
            explicitWaitVisibility(TestingPortalWebPage.tabBNPL, 20);
            JSClick(TestingPortalWebPage.tabBNPL, "BNPL Link");
            explicitWaitVisibility(TestingPortalWebPage.tabBNPL_Line, 20);
            JSClick(TestingPortalWebPage.tabBNPL_Line, "BNPL Line Link");
            explicitWaitVisibility(TestingPortalWebPage.iconBulkUpload, 20);
            waitTime(2000);
            JSClick(TestingPortalWebPage.iconBulkUpload, "Bulk Upload Icon");
            explicitWaitVisibility(TestingPortalWebPage.menuBulkuploadcashbackWhitelisting, 20);
            JSClick(TestingPortalWebPage.menuBulkuploadcashbackWhitelisting, "Bulk Upload Cashback WhiteListing");
            clickElementWithWebLocator(TestingPortalWebPage.uploadExcelFile);
            waitTime(3000);
            System.out.println(System.getProperty("user.dir"));
            String projectPath=System.getProperty("user.dir");
            //Enter Data into Excel
            
            /*------------------------------Write In Excel----------------------------*/
           
           
            ExcelFunctions ex=new ExcelFunctions();
			ex.writeXLSXFile(projectPath+"\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx",1,0,getReferenceNumber);
			ex.writeXLSXFile(projectPath+"\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx",1,1,files.getproperty("cashbackPercent"));
			ex.writeXLSXFile(projectPath+"\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx",1,2,files.getproperty("billingMaxCap"));
			ex.writeXLSXFile(projectPath+"\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx",1,4,files.getproperty("expireDate"));
            waitTime(5000);//2022-11-25
            Robot rb = new Robot();
            // copying File path to Clipboard
            StringSelection str = new StringSelection(projectPath+"\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx");
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
            
            rb.keyPress(KeyEvent.VK_CONTROL);
            rb.keyPress(KeyEvent.VK_V);
            
            // release Contol+V for pasting
            
            rb.keyRelease(KeyEvent.VK_CONTROL);
            rb.keyRelease(KeyEvent.VK_V);
            waitTime(5000);
            rb.keyPress(KeyEvent.VK_ENTER);
            rb.keyRelease(KeyEvent.VK_ENTER);
            System.out.println("completed");
			waitTime(5000);
			JSClick(TestingPortalWebPage.submitBtn, "Submit Button");
			waitTime(10000);
			BrowsertearDown();
			
		//	insertNewRecord(getReferenceNumber, "20", "100", "1", "2022-11-25");

			/*------------------------------Android----------------------------*/
			
			setPlatform("Android");
			initDriver();
			Swipe("DOWN", 1);
			boolean yellow=swipeElementAndroid(HomePage.bannerTray,HomePage.txt1yellowBanner(files.getproperty("cashbackPercent")));
			if (yellow==true) {
				logger.info("Yellow Banner--CashBack Banner is visible");
				softAssertion.assertEquals("Up to "+files.getproperty("cashbackPercent")+"%* cashback on all transactions", getText(HomePage.txt1yellowBanner(files.getproperty("cashbackPercent"))));
				extent.extentLoggerPass("CashBack Banner", "CashBack Banner is Visible--------------TC_Ring_Core_263");
				Aclick(HomePage.objScanAndPay, "Scan Button");
				
			//	Scan here
				Utilities.setPlatform = "Web";
	            new CommandBase("Chrome");
	            waitTime(4000);
	            getWebDriver().get(projectPath+"\\Mock_Files\\QRCode.png");
	            waitTime(25000);
	            BrowsertearDown();
	            setPlatform("Android");
	            initDriver();
	            
				waitTime(30000);
				explicitWaitVisibility(PaymentPage.objAmountTextField, 30);
				if(verifyElementPresent(PaymentPage.objAmountTextField,"Amount Text Field")) {
					   Aclick(PaymentPage.objAmountTextField, "Amount Field");
					   type(PaymentPage.objAmountTextField, "80", "Amount Field");
				}
				Aclick(PaymentPage.objPayNowBtn, "Pay Now Button");
				waitTime(20000);
				if(verifyElementPresent(PaymentPage.objConfirmPayment,getTextVal(PaymentPage.objConfirmPayment,"Text"))){
					   Aclick(PaymentPage.objEditTransactionPin,"Edit Pin Field");
					   type(PaymentPage.objEditTransactionPin,"1111","Edit Pin Field");
					   Aclick(PaymentPage.objContinue,"Continue Button");
				}
				if(verifyElementPresent(PaymentPage.objPaymentDone,getTextVal(PaymentPage.objPaymentDone,"Text"))){
					   String sPaymentDone = getText(PaymentPage.objPaymentDone);
					   softAssertion.assertEquals("Payment Done!", sPaymentDone);
					   verifyElementPresent(PaymentPage.objGreenTickMark,"Payment Done green tick mark");
					   verifyElementPresent(PaymentPage.objRupeesLogo,getTextVal(PaymentPage.objRupeesLogo,"is Rupees Logo"));
					   verifyElementPresent(PaymentPage.objMerchantNameReceipt,getTextVal(PaymentPage.objMerchantNameReceipt,"Text"));
					   verifyElementPresent(PaymentPage.objTaxDateAndTime,getTextVal(PaymentPage.objTaxDateAndTime,"is Tax date and time"));
					   verifyElementPresent(PaymentPage.objSeekBar,"Seek Bar");
					   verifyElementPresent(PaymentPage.objAvailableLimit,getTextVal(PaymentPage.objAvailableLimit,"Text"));
					   verifyElementPresent(PaymentPage.ObjAvailableLimitAmount,getTextVal(PaymentPage.ObjAvailableLimitAmount,"is Available Limit Amount"));
					   verifyElementPresent(PaymentPage.objGoToHomePage,getTextVal(PaymentPage.objGoToHomePage,"Text"));
					   System.out.println("-----------------------------------------------------------");
					   Aclick(PaymentPage.objGoToHomePage, "Goto HomePage");
					   logger.info("Payment Completed");
					   extent.extentLoggerPass("Payment Done Through Yellow Banner", "After coming yellow banner through scan button transection completed--------------TC_Ring_Core_264");
					   waitTime(5000);
					   if (verifyElementPresent(HomePage.txtAreYouEnjoyingApp,"Text")) {
							Aclick(HomePage.noBtn, "No Button");
						  }
			
				}
			}
			else {
				System.out.println("Yellow Banner not visible");
			}
			
		}
		public void laxman() {}
		public HashMap mockUserAPI(String url, String gender, String encrypted_name) {
			HashMap map=new HashMap();
		ValidatableResponse response = Utilities.MockuserAPI(url, gender, encrypted_name);
		String firstName = response.extract().jsonPath().getString("data.data.first_name");
		System.out.println("First Name= " + firstName);
		String middleName = response.extract().jsonPath().getString("data.data.middle_name");
		System.out.println("Middle Name= " + middleName);
		String lastName = response.extract().jsonPath().getString("data.data.last_name");
		System.out.println("Last Name =" + lastName);
		String panCard = response.extract().jsonPath().getString("data.data.pan");
		System.out.println("Pan Card= " + panCard);
		String mobileNumber = response.extract().jsonPath().getString("data.data.mobile_number");
		System.out.println("Mobile Number= " + mobileNumber);
		String motherName = response.extract().jsonPath().getString("data.data.mother_name");
		System.out.println("Mother Name= " + motherName);
		String email = response.extract().jsonPath().getString("data.data.email");
		System.out.println("Email= " + email);
		String genders = response.extract().jsonPath().getString("data.data.gender");
		System.out.println("Gender= " + genders);
		String otp = response.extract().jsonPath().getString("data.data.otp");
		System.out.println(otp);
		String dob = response.extract().jsonPath().getString("data.data.dob");
		System.out.println(dob);
		String[] dateSplit = dob.split("-");
		String year = dateSplit[0];
		System.out.println("years---" + year);
		String month = dateSplit[1];
		System.out.println("month---" + month);
		String date = dateSplit[2];
		System.out.println("date---" + date);
		int monthNumber = Integer.parseInt(month);
		String monthByName = Month.of(monthNumber).getDisplayName(TextStyle.SHORT, Locale.US);
		System.out.println(monthByName);
		String roomno=response.extract().jsonPath().getString("data.data.present_address.room_number");
		System.out.println(roomno);
		String address1=response.extract().jsonPath().getString("data.data.present_address.line_1");
		System.out.println(address1);
		String address2=response.extract().jsonPath().getString("data.data.present_address.line_2");
		System.out.println(address2);
		String landmark=response.extract().jsonPath().getString("data.data.present_address.landmark");
		System.out.println(landmark);
		String pin=response.extract().jsonPath().getString("data.data.present_address.pincode");
		System.out.println(pin);
		
		// User Details
		
		map.put("firstName", firstName);
		map.put("middleName", middleName);
		map.put("lastName", lastName);
		map.put("panCard", panCard);
		map.put("mobileNumber", mobileNumber);
		map.put("motherName", motherName);
		map.put("email", email);
		map.put("genders", genders);
		map.put("otp", otp);
		map.put("year", year);
		map.put("month", monthByName);
		map.put("date", date);
		
		//New Communication Address
		map.put("roomNo", roomno);
		map.put("address1",address1);
		map.put("address2", address2);
		map.put("landmark", landmark);
		map.put("pin", pin);
	
		return map;
		}
		


		public void simFlow() throws Exception {
			
			extent.HeaderChildNode("Banner logic on Home screen");
			cameraPermission();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink, "Signup/Login link");
			loginMobile();
			mobileNoValidation1("8483828180");
//			click(RingLoginPage.linkVerifyBySendingSMS, "Verify By Sending SMS");
//			explicitWaitVisibility(MobileLoginPage.btnReadAndAccept, 10);
//			Aclick(MobileLoginPage.btnReadAndAccept, "Read adn Accept");
//			explicitWaitVisibility(PermissionPage.btnAllow, 20);
//			Aclick(PermissionPage.btnAllow, "Allow Call");
//			explicitWaitVisibility(PermissionPage.btnAllowLocation, 20);
//			Aclick(PermissionPage.btnAllowLocation, "Allow Location");
//			explicitWaitVisibility(PermissionPage.btnAllow, 20);
//			Aclick(PermissionPage.btnAllow, "Allow SMS");
//			explicitWaitVisibility(SimValidationPage.txtSimNotFound, 20);
//			softAssertion.assertEquals("SIM card not found", getText(SimValidationPage.txtSimNotFound));
//			logger.info("Sim not Found");
//			extent.extentLoggerPass("SIM Not Inserted", "SIM binding with no sim present_TC_Ring_Core_36");
				
			
		}
	
		public void panValidatation(String url, String gender, String encrypted_name,String portalEmail,String portalPassword,String portalOTP) throws Exception {
			
			extent.HeaderChildNode("Banner logic on Home screen");
			 PropertyFileReader files=new PropertyFileReader("properties\\TestData.properties");
			
			/*------------------------------Data From API----------------------------*/
			
			HashMap mockUserDetails=mockUserAPI(url, gender, encrypted_name);
			String firstName=(String) mockUserDetails.get("firstName");
			String middleName=(String) mockUserDetails.get("middleName");
			String lastName=(String) mockUserDetails.get("lastName");
			String mobileNumber=(String) mockUserDetails.get("mobileNumber");
			String otp=(String) mockUserDetails.get("otp");
			String panCard=(String) mockUserDetails.get("panCard");
			String email=(String) mockUserDetails.get("email");
			String motherName=(String) mockUserDetails.get("motherName");
			String genders=(String) mockUserDetails.get("genders");
			String year=(String) mockUserDetails.get("year");
			String monthByName=(String) mockUserDetails.get("month");
			String date=(String) mockUserDetails.get("date");
			
			String roomno=(String) mockUserDetails.get("roomNo");
			String address1=(String) mockUserDetails.get("address1");
			String address2=(String) mockUserDetails.get("address2");
			String landmark=(String) mockUserDetails.get("landmark");
			String pin=(String) mockUserDetails.get("pin");
			
			/*------------------------------Front End----------------------------*/
			//shashi
			//thakur
			//kumar
			//ravi 
			//sharma
			cameraPermission();
			User_Play_Store_Flow(date, roomno, address1, address2, landmark, pin, otp, otp);
			kycSkipped(url, genders, encrypted_name, portalEmail, portalPassword, portalOTP);
			Aclick(null, date);
			Aclick(null, date);
			userDetails();
			Back(2);
			cameraPermission();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink, "Signup/Login link");
			loginMobile();
			mobileNoValidation1(mobileNumber);
			enterOtp(otp);
			explicitWaitVisibility(MobileLoginPage.btnReadAndAccept, 10);
			Aclick(MobileLoginPage.btnReadAndAccept, "Read adn Accept");
			explicitWaitVisibility(PermissionPage.btnAllow, 20);
			Aclick(PermissionPage.btnAllow, "Allow Call");
			explicitWaitVisibility(PermissionPage.btnAllowLocation, 20);
			Aclick(PermissionPage.btnAllowLocation, "Allow Location");
			explicitWaitVisibility(PermissionPage.btnAllow, 20);
			Aclick(PermissionPage.btnAllow, "Allow SMS");
			waitTime(20000);
			System.out.println("Shashi Edit 1");

			System.out.println("Swagatika edit 1");
			System.out.println("Swagatika edit 2");
			System.out.println("Swagatika edit 3");

			System.out.println("Swagatika edit 4");
			System.out.println("Swagatika edit 5");
			System.out.println("Swagatika edit 6");
			

			System.out.println("Swagatika edit 7");
			System.out.println("Swagatika edit 8");
			System.out.println("Swagatika edit 9");

			System.out.println("Shashi Edit 2");
			System.out.println("Shashi Edit 3");
			System.out.println("Shashi Edit 4");
			System.out.println("Shashi Edit 5");
			System.out.println("Shashi Edit 6");
			System.out.println("Shashi Edit 7");
			System.out.println("Shashi Edit 8");
			System.out.println("Shashi Edit 9");
			

			explicitWaitVisibility(UserRegistrationPage.objUserDetailsHeader, 30);
			String userName=files.getproperty("userName");
			String userLastName=files.getproperty("lastName");
			String userMotherName=files.getproperty("motherName");
			String userGender=files.getproperty("gender");
			String userBirthDate=files.getproperty("dateOfBirthDate");
			String userBirthMonth=files.getproperty("dateOfBirthMonth");
			String userBirthYear=files.getproperty("dateOfBirthYear");
			userDetailsFromAPI(userName, userLastName, userName+userLastName+new Random().nextInt(10000)+"@example.com", userMotherName, userGender);
			dateOfBirthForAPI(userBirthMonth, userBirthDate, userBirthYear);
			Aclick(UserRegistrationPage.objRegister, "Proceed Button");
			waitTime(30000);
			waitTime(100);
			waitTime(200);
			
		}
		
		
		
		
		
		
		
		

}





























