import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.Cookie
import org.openqa.selenium.Keys
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent
import groovy.json.JsonSlurper
import java.text.SimpleDateFormat

// ============================================================
// BƯỚC 1: TẠO CANDIDATE QUA API (Như cũ)
// ============================================================

//WebUI.openBrowser('')
//WebUI.navigateToUrl('http://localhost/web/index.php/auth/login')
//WebUI.setText(findTestObject('Page_Login/input_Username'), 'admin') 
//WebUI.setText(findTestObject('Page_Login/input_Password'), 'OrangeAdmin@123') 
//WebUI.click(findTestObject('Page_Login/button_Login'))
//WebUI.waitForPageLoad(10)

// Lấy Cookie
//Set<Cookie> allCookies = DriverFactory.getWebDriver().manage().getCookies()
String cookieForHeader = CustomKeywords.'com.common.LoginHelper.loginAndGetCookie'('admin', 'OrangeAdmin@123')

// Gọi API
//String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
//String uniqueEmail = "test" + System.currentTimeMillis() + "@gmail.com" 
//
//RequestObject createCandidateReq = new RequestObject("CreateCandidate_Direct")
//createCandidateReq.setRestUrl("http://localhost/web/index.php/api/v2/recruitment/candidates")
//createCandidateReq.setRestRequestMethod("POST")
//def httpHeaders = new ArrayList<TestObjectProperty>()
//httpHeaders.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json"))
//httpHeaders.add(new TestObjectProperty("Cookie", ConditionType.EQUALS, cookieForHeader))
//createCandidateReq.setHttpHeaderProperties(httpHeaders)
//
//String jsonBody = """
//{ "firstName": "Auto", "lastName": "Bot", "email": "${uniqueEmail}", "vacancyId": 2, "dateOfApplication": "${todayDate}", "consentToKeepData": false }
//"""
//createCandidateReq.setBodyContent(new HttpTextBodyContent(jsonBody, "UTF-8", "application/json"))
//def response = WS.sendRequest(createCandidateReq)
//WS.verifyResponseStatusCode(response, 200)
//
//def candidateId = new JsonSlurper().parseText(response.getResponseText()).data.id

def candidateId = CustomKeywords.'com.common.APIHelper.createCandidate'(cookieForHeader, 'Auto', 'Bot', 'AUTO', '2')

WebUI.navigateToUrl("http://localhost/web/index.php/recruitment/addCandidate/" + candidateId)
WebUI.waitForPageLoad(5)

// ============================================================
// BƯỚC 2: XỬ LÝ CHUYỂN TRẠNG THÁI (FAST FORWARD)
// ============================================================

// Lấy trạng thái mong muốn từ file Excel (Giả sử bạn gán cứng để test trước)
// String targetState = findTestData('Data_States').getValue('Start_State', 1)
String targetState = "Interview Passed" // <--- THỬ THAY ĐỔI CÁI NÀY ĐỂ TEST: Shortlisted, Interview Scheduled...

WebUI.comment("--- ĐANG TUA ĐẾN TRẠNG THÁI: " + targetState + " ---")

// Logic xếp chồng: Muốn đến C phải qua A và B
boolean needShortlist = false
boolean needSchedule = false
boolean needPass = false
boolean needOffer = false

if (targetState == 'Shortlisted') {
	needShortlist = true
} else if (targetState == 'Interview Scheduled') {
	needShortlist = true; needSchedule = true
} else if (targetState == 'Interview Passed') {
	needShortlist = true; needSchedule = true; needPass = true
} else if (targetState == 'Job Offered') {
	needShortlist = true; needSchedule = true; needPass = true; needOffer = true
}

// --- THỰC HIỆN CÁC BƯỚC ---

if (needShortlist) {
	WebUI.comment("Executing: Shortlist")
	WebUI.click(findTestObject('Page_Candidate/btn_Shortlist'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), 'Shortlisted by Katalon')
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))
	WebUI.waitForElementVisible(findTestObject('Page_Candidate/btn_ScheduleInterview'), 5)
}

if (needSchedule) {
	WebUI.comment("Executing: Schedule Interview")
	WebUI.click(findTestObject('Page_Candidate/btn_ScheduleInterview'))
	
	// Điền form phỏng vấn
	WebUI.setText(findTestObject('Page_Candidate/input_InterviewTitle'), 'Auto Interview')
	WebUI.setText(findTestObject('Page_Candidate/input_Interviewer'), 'a') // Gõ 'a' để hiện gợi ý
	WebUI.delay(2)
	// Chọn người đầu tiên trong gợi ý
	WebUI.click(findTestObject('Page_Candidate/item_Dropdown_Suggestion', [('name'): 'a'])) 
	
	WebUI.setText(findTestObject('Page_Candidate/input_Date'), todayDate)
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))
	WebUI.waitForElementVisible(findTestObject('Page_Candidate/btn_MarkInterviewPassed'), 5)
}

if (needPass) {
	WebUI.comment("Executing: Mark Interview Passed")
	WebUI.click(findTestObject('Page_Candidate/btn_MarkInterviewPassed'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), 'Passed Excellent')
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))
	WebUI.waitForElementVisible(findTestObject('Page_Candidate/btn_OfferJob'), 5)
}

if (needOffer) {
	WebUI.comment("Executing: Offer Job")
	WebUI.click(findTestObject('Page_Candidate/btn_OfferJob'))
	WebUI.setText(findTestObject('Page_Candidate/input_Note'), 'Offering Salary 1000$')
	WebUI.click(findTestObject('Page_Candidate/btn_Save'))
	WebUI.waitForElementVisible(findTestObject('Page_Candidate/btn_Hire'), 5)
}

// ============================================================
// BƯỚC 3: THỰC HIỆN TEST CASE CHÍNH
// ============================================================

WebUI.comment("--- ĐÃ ĐẾN VẠCH XUẤT PHÁT. BẮT ĐẦU TEST ---")
String currentStatusText = WebUI.getText(findTestObject('Page_Candidate/text_StatusLabel'))
WebUI.comment("Trạng thái hiện tại trên Web: " + currentStatusText)

// Tại đây bạn viết tiếp logic của Test Case cụ thể
// Ví dụ: Nếu Test Case là "Verify Reject", bạn click nút Reject và kiểm tra