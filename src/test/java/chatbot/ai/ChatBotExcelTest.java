package chatbot.ai;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatBotExcelTest {

	WebDriver driver;

	@BeforeMethod
	public void setUp() {
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	// Read Excel file and return data in Object[][] format
	public Object[][] getExcelData(String filePath) throws IOException {
		List<String> urls = new ArrayList<>();

		// Opening Excel file
		FileInputStream fis = new FileInputStream(filePath);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);

		 // Created iterator for rows
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next(); //skip header row

		 // Loop through remaining rows
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Cell cell = row.getCell(1);  // getting column B (index 1)
			urls.add(cell.getStringCellValue());  // adding URL to list
		}

		workbook.close();
		fis.close();

		// Convert list of URLs into Object[][] for DataProvider
		Object[][] data = new Object[urls.size()][1];
		for (int i = 0; i < urls.size(); i++) {
			data[i][0] = urls.get(i);
		}
		return data;
	}

	//URLs from Excel to tests
	@DataProvider(name = "chatbotUrls")
	public Object[][] provideUrls() throws IOException {
		return getExcelData("src/test/resources/chatbot_urls.xlsx");
	}

	@Test(dataProvider = "chatbotUrls", priority = 2)
	public void testSites(String urls) throws InterruptedException {
		driver.get(urls);
		ChatBotActions.handleCookiesIfPresent(driver);
		ChatBotActions.clickChatBot(urls, driver);
		ChatBotActions.expandChatBot(driver);
		int suggestions = ChatBotActions.getDefaultSuggestionsCount(driver);
		Assert.assertEquals(suggestions, 3, "Suggestions mismatch for " + urls);
		ChatBotActions.scheduleMeeting(driver);
		ChatBotActions.getLastReply(driver);
		ChatBotActions.greetingReply(driver);
		ChatBotActions.clickRandomSuggestion(driver);
		ChatBotActions.closeChatBot(driver);
	}

}
