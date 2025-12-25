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

import internal.GlobalVariable

public class PIMSearchHelper {
	@Keyword
	def resetForm() {
		WebUI.click(findTestObject('Page_PIM/btn_Reset'))
		WebUI.delay(1.5) // Chờ form reset xong
	}
	
	@Keyword
	def fillSearchForm(
		String empName, boolean isSelectName,
		String empID,
		String status,
		String include,
		String supervisor, boolean isSelectSup,
		String jobTitle,
		String subUnit
	) {
		// 4. Include (Chỉ chọn nếu khác mặc định)
		if (include != "" && include != "Current Employees Only") {
			WebUI.click(findTestObject('Page_PIM/select_Include'))
			WebUI.click(findTestObject('Page_PIM/item_Option_Exact', [('name'): include]))
		}

		// 1. Employee Name
		if (empName != "") {
			WebUI.setText(findTestObject('Page_PIM/input_EmployeeName'), empName)
			WebUI.delay(2) // Chờ gợi ý hiện ra
			if (isSelectName) {
				WebUI.click(findTestObject('Page_PIM/item_Option_Contains', [('name'): empName]))
			} else {
				// Nếu không chọn -> Click ra ngoài (vào Label ID) để đóng gợi ý
				WebUI.click(findTestObject('Page_PIM/label_EmployeeId'))
			}
		}

		// 2. Employee ID
		if (empID != "") {
			WebUI.setText(findTestObject('Page_PIM/input_EmployeeID'), empID)
		}

		// 3. Employment Status
		if (status != "") {
			WebUI.click(findTestObject('Page_PIM/select_EmploymentStatus'))
			WebUI.click(findTestObject('Page_PIM/item_Option_Exact', [('name'): status]))
		}

		// 5. Supervisor Name
		if (supervisor != "") {
			WebUI.setText(findTestObject('Page_PIM/input_SupervisorName'), supervisor)
			WebUI.delay(2)
			if (isSelectSup) {
				// Chọn dòng gợi ý (Thường là tên + id nên ta chọn theo tên supervisor)
				WebUI.click(findTestObject('Page_PIM/item_Option_Contains', [('name'): supervisor]))
			} else {
				WebUI.click(findTestObject('Page_PIM/label_EmployeeId'))
			}
		}

		// 6. Job Title
		if (jobTitle != "") {
			WebUI.click(findTestObject('Page_PIM/select_JobTitle'))
			WebUI.click(findTestObject('Page_PIM/item_Option_Exact', [('name'): jobTitle]))
		}

		// 7. Sub Unit
		if (subUnit != "") {
			WebUI.click(findTestObject('Page_PIM/select_SubUnit'))
			WebUI.click(findTestObject('Page_PIM/item_Option_Exact', [('name'): subUnit]))
		}
		
		// Bấm nút Search
		WebUI.click(findTestObject('Page_PIM/btn_Search'))
	}
}
