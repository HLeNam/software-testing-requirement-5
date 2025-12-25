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
import java.text.SimpleDateFormat

import internal.GlobalVariable

public class StateHelper {
	@Keyword
	def toState(String targetState) {
		WebUI.comment("=== [StateHelper] Tua đến trạng thái: " + targetState + " ===")
		String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date())

		boolean needShortlist = false
		boolean needSchedule = false
		boolean needPass = false
		boolean needOffer = false

		if (targetState == 'Shortlisted') needShortlist = true
		else if (targetState == 'Interview Scheduled') { needShortlist = true; needSchedule = true }
		else if (targetState == 'Interview Passed') { needShortlist = true; needSchedule = true; needPass = true }
		else if (targetState == 'Job Offered') { needShortlist = true; needSchedule = true; needPass = true; needOffer = true }

		if (needShortlist) {
			WebUI.click(findTestObject('Page_Candidate/btn_Shortlist'))
			WebUI.setText(findTestObject('Page_Candidate/input_Note'), 'Helper Shortlist')
			WebUI.click(findTestObject('Page_Candidate/btn_Save'))
			WebUI.waitForElementVisible(findTestObject('Page_Candidate/btn_ScheduleInterview'), 10)
		}
		if (needSchedule) {
			WebUI.click(findTestObject('Page_Candidate/btn_ScheduleInterview'))
			WebUI.setText(findTestObject('Page_Candidate/input_InterviewTitle'), 'Helper Interview')
			WebUI.setText(findTestObject('Page_Candidate/input_Interviewer'), 'a')
			WebUI.delay(2)
			WebUI.click(findTestObject('Page_Candidate/item_Dropdown_Suggestion', [('name'): 'a']))
			WebUI.setText(findTestObject('Page_Candidate/input_Date'), todayDate)
			WebUI.click(findTestObject('Page_Candidate/btn_Save'))
			WebUI.waitForElementVisible(findTestObject('Page_Candidate/btn_MarkInterviewPassed'), 10)
		}
		if (needPass) {
			WebUI.click(findTestObject('Page_Candidate/btn_MarkInterviewPassed'))
			WebUI.setText(findTestObject('Page_Candidate/input_Note'), 'Helper Pass')
			WebUI.click(findTestObject('Page_Candidate/btn_Save'))
			WebUI.waitForElementVisible(findTestObject('Page_Candidate/btn_OfferJob'), 10)
		}
		if (needOffer) {
			WebUI.click(findTestObject('Page_Candidate/btn_OfferJob'))
			WebUI.setText(findTestObject('Page_Candidate/input_Note'), 'Helper Offer')
			WebUI.click(findTestObject('Page_Candidate/btn_Save'))
			WebUI.waitForElementVisible(findTestObject('Page_Candidate/btn_Hire'), 10)
		}
	}
}
