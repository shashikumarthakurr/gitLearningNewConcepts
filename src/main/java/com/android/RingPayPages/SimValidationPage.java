package com.android.RingPayPages;

import org.openqa.selenium.By;

public class SimValidationPage {
	
	public static By txtSimNotFound=By.xpath("//*[@text='SIM card not found']");
	
	public static By txtSimErrorMessage=By.xpath("//*[@text='Please ensure a SIM with outgoing messages enabled is inserted into this device and restart your App']");
	
	public static By btnTryAgain=By.xpath("//*[@text='Try Again']");
	
	public static By txtSorry=By.xpath("//*[@text='Sorry!']");
	
}
