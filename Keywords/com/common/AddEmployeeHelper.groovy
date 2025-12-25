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

import org.openqa.selenium.Keys

import internal.GlobalVariable

public class AddEmployeeHelper {
	@Keyword
	def fillEmployeeForm(
		String firstName, String middleName, String lastName,
		String empID,
		String photoPath,
		boolean isCreateLogin,
		String username, String password, String confirmPass
	) {
		// 1. Upload Ảnh (Nếu có đường dẫn)
		if (photoPath != "") {
			// uploadFile là keyword chuyên dụng của Katalon
			WebUI.uploadFile(findTestObject('Page_AddEmployee/btn_UploadPhoto'), photoPath)
		}

		// 2. Điền tên
		if (firstName != "") WebUI.setText(findTestObject('Page_AddEmployee/input_FirstName'), firstName)
		if (middleName != "") WebUI.setText(findTestObject('Page_AddEmployee/input_MiddleName'), middleName)
		if (lastName != "") WebUI.setText(findTestObject('Page_AddEmployee/input_LastName'), lastName)

		// 3. Employee ID (Xóa cũ điền mới)
		if (empID != "") {
			WebUI.click(findTestObject('Page_AddEmployee/input_EmployeeId'))
			// Dùng SendKeys để xóa sạch hơn clearText đôi khi bị lỗi trên field này
			WebUI.sendKeys(findTestObject('Page_AddEmployee/input_EmployeeId'), Keys.chord(Keys.CONTROL, 'a', Keys.BACK_SPACE))
			WebUI.setText(findTestObject('Page_AddEmployee/input_EmployeeId'), empID)
		}

		// 4. Xử lý Toggle Login Details
		if (isCreateLogin) {
			WebUI.click(findTestObject('Page_AddEmployee/switch_CreateLogin'))
			WebUI.delay(1) // Chờ form xổ ra
			
			WebUI.setText(findTestObject('Page_AddEmployee/input_Username'), username)
			WebUI.setText(findTestObject('Page_AddEmployee/input_Password'), password)
			WebUI.setText(findTestObject('Page_AddEmployee/input_ConfirmPassword'), confirmPass)
		}

		// 5. Save
		WebUI.click(findTestObject('Page_AddEmployee/btn_Save'))
	}
}
