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

String cookie = CustomKeywords.'com.common.LoginHelper.loginAndGetCookie'('admin', 'OrangeAdmin@123')
def candidateId = CustomKeywords.'com.common.APIHelper.createCandidate'(cookie, first_name, last_name, email, vacancy_id)

WebUI.navigateToUrl("http://localhost/web/index.php/recruitment/addCandidate/" + candidateId)
WebUI.waitForPageLoad(10)

CustomKeywords.'com.common.StateHelper.toState'(start_state)

WebUI.comment("--- TEST ACTION: " + action + " ---")

if (action == 'Shortlist') {
	WebUI.click(findTestObject('Page_Candidate/btn_Shortlist'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), note)
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))

} else if (action == 'Reject') {
	WebUI.click(findTestObject('Page_Candidate/btn_Reject'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), note)
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))

} else if (action == 'Schedule Interview') {
	WebUI.click(findTestObject('Page_Candidate/btn_ScheduleInterview'))
	WebUI.setText(findTestObject('Page_Candidate/input_InterviewTitle'), interview_title)
	WebUI.setText(findTestObject('Page_Candidate/input_Interviewer'), interviewer)
	WebUI.delay(2)
	WebUI.click(findTestObject('Page_Candidate/item_Dropdown_Suggestion', [('name'): interviewer]))
	WebUI.setText(findTestObject('Page_Candidate/input_Date'), interview_date)
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))

} else if (action == 'Mark Passed') {
	WebUI.click(findTestObject('Page_Candidate/btn_MarkInterviewPassed'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), note)
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))

} else if (action == 'Mark Failed') {
	WebUI.click(findTestObject('Page_Candidate/btn_MarkInterviewFailed'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), note)
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))

} else if (action == 'Offer Job') {
	WebUI.click(findTestObject('Page_Candidate/btn_OfferJob'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), note)
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))

} else if (action == 'Offer Declined') {
	WebUI.click(findTestObject('Page_Candidate/btn_OfferDeclined'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), note)
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))

} else if (action == 'Hire') {
	WebUI.click(findTestObject('Page_Candidate/btn_Hire'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), note)
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))
}

// ============================================================
// 4. VERIFY: KIỂM TRA KẾT QUẢ
// ============================================================
WebUI.delay(2) // Chờ load
String currentStatus = WebUI.getText(findTestObject('Page_Candidate/text_StatusLabel'))
WebUI.comment("Expect: " + expected_status + " | Actual: " + currentStatus)

if (currentStatus.contains(expected_status)) {
	WebUI.comment("PASSED")
} else {
	WebUI.takeScreenshot()
	WebUI.verifyMatch(currentStatus, expected_status, false, FailureHandling.STOP_ON_FAILURE)
}

