import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

CustomKeywords.'com.common.LoginHelper.loginAndGetCookie'('admin', 'OrangeAdmin@123')
WebUI.navigateToUrl('http://localhost/web/index.php/pim/addEmployee')
WebUI.waitForPageLoad(10)

WebUI.comment("--- TEST CASE: " + tc_id + " ---")

CustomKeywords.'com.common.AddEmployeeHelper.fillEmployeeForm'(
	first_name,
	middle_name,
	last_name,
	employee_id,
	photo_path,
	is_create_login.toBoolean(),
	username,
	password,
	confirm_password
)

WebUI.delay(1)

if (expect_type == 'Success') {
	// Happy Path: Phải chuyển sang trang Personal Details
	if (WebUI.verifyElementVisible(findTestObject('Page_AddEmployee/header_PersonalDetails'), FailureHandling.OPTIONAL)) {
		WebUI.comment("PASSED: Redirected to Personal Details page.")
	} else {
		WebUI.takeScreenshot()
		WebUI.comment("FAILED: Did not redirect to Personal Details.")
		FailureHandling.STOP_ON_FAILURE
	}

} else if (expect_type == 'Error') {
	// Kiểm tra xem có dòng chữ lỗi mong đợi xuất hiện không
	// Dùng Dynamic Object với biến expect_message
	if (WebUI.verifyElementVisible(findTestObject('Page_AddEmployee/msg_DynamicError', [('errorText'): expect_message]), FailureHandling.OPTIONAL)) {
		WebUI.comment("PASSED: Found error message: " + expect_message)
	} else {
		WebUI.takeScreenshot()
		WebUI.comment("FAILED: Expected error '" + expect_message + "' but not found.")
		FailureHandling.STOP_ON_FAILURE
	}
}