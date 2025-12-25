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
import groovy.json.JsonSlurper
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent
import com.kms.katalon.core.testobject.RequestObject
import java.text.SimpleDateFormat
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType

import internal.GlobalVariable

public class APIHelper {
	@Keyword
	def createCandidate(String cookie, String firstName, String lastName, String email, String vacancyId) {
		String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
		if (email == "AUTO" || email == null || email == "") {
			email = "auto" + System.currentTimeMillis() + "@gmail.com"
		}

		RequestObject req = new RequestObject("CreateCandidate_Direct")
		req.setRestUrl("http://localhost/web/index.php/api/v2/recruitment/candidates")
		req.setRestRequestMethod("POST")

		def headers = new ArrayList<TestObjectProperty>()
		headers.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json"))
		headers.add(new TestObjectProperty("Cookie", ConditionType.EQUALS, cookie))
		req.setHttpHeaderProperties(headers)

		String body = """{ "firstName": "${firstName}", "lastName": "${lastName}", "email": "${email}", "vacancyId": ${vacancyId}, "dateOfApplication": "${todayDate}", "consentToKeepData": false }"""
		req.setBodyContent(new HttpTextBodyContent(body, "UTF-8", "application/json"))

		def response = WS.sendRequest(req)
		WS.verifyResponseStatusCode(response, 200)
		return new JsonSlurper().parseText(response.getResponseText()).data.id 
	}
}
