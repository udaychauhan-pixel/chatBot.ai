package chatbot.ai;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.List;

public class ChatBotActions {

	// Handle cookie banners if they appear on the site
	public static void handleCookiesIfPresent(WebDriver driver) {
		try {
			// Try to find "Decline" button
			WebElement rejectBtn = driver.findElement(By.xpath("//div[contains(text(),'Decline')]"));
			rejectBtn.click();
			System.out.println("Cookies Rejected");
		} catch (NoSuchElementException e1) {
			try {
				// If decline not found, try "Allow Cookies"
				WebElement acceptBtn = driver.findElement(By.xpath("//div[contains(text(),'Allow Cookies')]"));
				acceptBtn.click();
				System.out.println("Cookies Accepted");
			} catch (NoSuchElementException e2) {
				// If neither found, continue
				System.out.println("No Cookies Found");
			}
		}
	}

	public void verifyChatBotVisible(String url, WebDriver driver) throws InterruptedException {
		driver.get(url);
		WebElement shadowHost = driver.findElement(By.cssSelector("my-component"));
		WebElement chatButton = shadowHost.getShadowRoot()
				.findElement(By.cssSelector("button.ChatButton-module_sdkChatButton__M1mKI"));
		assertTrue(chatButton.isDisplayed(), "Chatbot is not visible for " + url);

	}

	// Open chatbot button inside the shadow DOM
	public static void clickChatBot(String url, WebDriver driver) {
		try {
			WebElement shadowHost = driver.findElement(By.cssSelector("my-component"));
			SearchContext shadowRoot = shadowHost.getShadowRoot();
			// Locate chatbot button inside shadow root
			WebElement chatBtn = shadowRoot
					.findElement(By.cssSelector("button.ChatButton-module_sdkChatButton__M1mKI"));
			chatBtn.click();
			Thread.sleep(3000);
			System.out.println("Chatbot opened for: " + url);
		} catch (Exception e) {
			System.out.println("Couldn't open chatbot for: " + url + e.getMessage());
		}
	}

	// Count the default chatbot suggestions (buttons under input box)
	public static int getDefaultSuggestionsCount(WebDriver driver) {
		try {
			WebElement shadowHost = driver.findElement(By.cssSelector("my-component"));
			SearchContext shadowRoot = shadowHost.getShadowRoot();

			// Look for suggestion buttons inside container
			List<WebElement> items = shadowRoot
					.findElements(By.cssSelector("div[class*='sdkSuggestedQuestionsContainer'] button"));

			int count = items.size();
			System.out.println("Found " + count + " for suggestive responses");
			return count;
		} catch (Exception e) {
			System.out.println("Couldn't count suggestions: " + e.getMessage());
			return 0;
		}
	}

	// Expand chatbot window to full screen mode

	public static void expandChatBot(WebDriver driver) {
		try {
			WebElement shadowHost = driver.findElement(By.cssSelector("my-component"));
			SearchContext shadowRoot = shadowHost.getShadowRoot();

			WebElement expandBtn = shadowRoot
					.findElement(By.cssSelector("button.ChatHeader-module_sdkExpandButton__qtONk"));
			expandBtn.click();
			System.out.println("Chatbot Expanded");
		} catch (Exception e) {
			System.out.println("Couldn't expand chatbot: " + e.getMessage());
		}
	}

	// Close chatbot widget
	public static void closeChatBot(WebDriver driver) {
		try {
			WebElement shadowHost = driver.findElement(By.cssSelector("my-component"));
			SearchContext shadowRoot = shadowHost.getShadowRoot();

			WebElement closeBtn = shadowRoot
					.findElement(By.cssSelector("button.ChatHeader-module_sdkCloseButton__AQvQv"));
			closeBtn.click();
			System.out.println("Chatbot closed");
		} catch (Exception e) {
			System.out.println("Couldn't close chatbot: " + e.getMessage());
		}
	}

	// Click on the schedule meeting (calendar) button inside chatbot
	public static void scheduleMeeting(WebDriver driver) {
		try {
			WebElement shadowHost = driver.findElement(By.cssSelector("my-component"));
			SearchContext shadowRoot = shadowHost.getShadowRoot();

			// Find schedule button inside input box
			WebElement schdBtn = shadowRoot
					.findElement(By.cssSelector("button.ChatInputBox-module_sdkCalendarButton__r3Rt3"));
			schdBtn.click();
			Thread.sleep(4000);

			System.out.println("Schedule Meeting Clicked");
		} catch (Exception e) {
			System.out.println("Couldn't find the schedule meeting button: " + e.getMessage());
		}
	}

	// Get the last reply message text from chatbot
	public static String getLastReply(WebDriver driver) throws InterruptedException {

		// Locating the chatbot shadow host
		WebElement host = driver.findElement(By.cssSelector("my-component"));
		SearchContext root = host.getShadowRoot();

		// Wait until at least one botReply is present
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));

		List<WebElement> botReply = wait.until(d -> {
			List<WebElement> list = root
					.findElements(By.cssSelector("div[class='AiText-module_textContainer__UtZ-U']"));
			return list.isEmpty() ? null : list;
		});

		Thread.sleep(4000);

		// Picking the very last reply botReply from the list
		WebElement lastReply = botReply.get(botReply.size() - 1);

		// Trying to read the direct text inside the botReply
		String direct = lastReply.getText().trim();
		if (!direct.isEmpty()) {
			System.out.println("Bot Reply: " + direct);
		}
		return direct;
	}


	public static String greetingReply(WebDriver driver) throws InterruptedException {
	    // opening the shadow root
	    WebElement host = driver.findElement(By.cssSelector("my-component"));
	    SearchContext root = host.getShadowRoot();

	    // getting the count of replies before sending greeting
	    int initialCount = root.findElements(By.cssSelector("div[class*='AiText-module_textContainer']")).size();

	   //Hi
	    WebElement chatInput = root.findElement(By.cssSelector("textarea[role='textbox'][aria-label='Chat input']"));
	    chatInput.click();
	    chatInput.sendKeys("Hi");

	    // hitting the send button
	    WebElement sendBtn = root.findElement(By.cssSelector("button.ChatInputBox-module_sdkSendButton__CLWm6"));
	    sendBtn.click();

	    // waiting for bot to reply
	    Thread.sleep(4000);

	    // getting all replies
	    List<WebElement> botReply = root.findElements(By.cssSelector("div[class*='AiText-module_textContainer']"));

	    // picking the last reply
	    WebElement lastReply = botReply.get(botReply.size() - 1);

	    String direct = lastReply.getText().trim();
	    System.out.println("Bot Reply after greeting: " + direct);

	    return direct;
	}
	
	

	
	
}
