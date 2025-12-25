package com.common

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.Cookie

import internal.GlobalVariable

public class LoginHelper {
	@Keyword
	def loginAndGetCookie(String username, String password) {
		WebUI.openBrowser('')
		WebUI.navigateToUrl('http://localhost/web/index.php/auth/login')
		WebUI.waitForElementVisible(findTestObject('Page_Login/input_Username'), 10)
		WebUI.setText(findTestObject('Page_Login/input_Username'), username)
		WebUI.setText(findTestObject('Page_Login/input_Password'), password)
		WebUI.click(findTestObject('Page_Login/button_Login'))
		WebUI.waitForPageLoad(10)
		
		Set<Cookie> allCookies = DriverFactory.getWebDriver().manage().getCookies()
		String cookieString = ""
		for (Cookie c : allCookies) { cookieString += c.getName() + "=" + c.getValue() + "; " }
		return cookieString
	}
}
