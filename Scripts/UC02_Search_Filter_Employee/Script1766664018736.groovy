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

WebUI.navigateToUrl('http://localhost/web/index.php/pim/viewEmployeeList')
WebUI.waitForPageLoad(10)

CustomKeywords.'com.common.PIMSearchHelper.resetForm'()

WebUI.comment("--- RUNNING TC: " + tc_id + " ---")

CustomKeywords.'com.common.PIMSearchHelper.fillSearchForm'(
	employee_name,
	is_select_name.toBoolean(), // Chuyển "TRUE" thành true
	employee_id,
	status,
	include,
	supervisor,
	is_select_supervisor.toBoolean(),
	job_title,
	sub_unit
)

WebUI.delay(2)

if (expect_type == 'VerifyError') {
	// Kiểm tra thông báo lỗi (Invalid)
	if (expect_content == 'Invalid') {
		WebUI.verifyElementVisible(findTestObject('Page_PIM/msg_Invalid'))
	} else if (expect_content == 'Invalid Parameter') {
		WebUI.verifyElementVisible(findTestObject('Page_PIM/msg_InvalidParameter'))
	}
	
} else if (expect_type == 'VerifyText') {
	// Kiểm tra kết quả tìm kiếm
	if (expect_content == 'No Records Found') {
		WebUI.verifyElementVisible(findTestObject('Page_PIM/msg_NoRecords'))
	} else {
		// Trường hợp tìm thấy record
		if (WebUI.verifyElementVisible(findTestObject('Page_PIM/msg_RecordsFound'), FailureHandling.OPTIONAL)) {
			WebUI.comment("PASSED: Found records as expected.")
		} else {
			// Nếu mong đợi tìm thấy mà lại ra No Record -> Fail
			if (WebUI.verifyElementVisible(findTestObject('Page_PIM/msg_NoRecords'), FailureHandling.OPTIONAL)) {
				WebUI.comment("FAILED: Expected records but found NONE.")
				FailureHandling.STOP_ON_FAILURE
			}
		}
	}
}
