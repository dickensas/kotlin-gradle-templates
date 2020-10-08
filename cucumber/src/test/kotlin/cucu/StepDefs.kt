package cucu

import io.cucumber.java.en.Then
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.core.api.Scenario
import io.cucumber.java8.En
import junit.framework.Assert.assertEquals

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait

class StepDefs: En {
    private lateinit var today: String
    private lateinit var actualAnswer: String

	lateinit var driver: WebDriver

    init {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\driver\\chromedriver.exe")
		var options = ChromeOptions()
		options.setExperimentalOptions("useAutomationExtension",false)
		driver = ChromeDriver()
		
		Given("I am on the Google search page") {
            driver.get("https:\\www.google.com")
        }

        When("I search for {string}") { query: String ->
            val element: WebElement = driver.findElement(By.name("q"));
            // Enter something to search for
            element.sendKeys(query)
            // Now submit the form. WebDriver will find the form for us from the element
            element.submit()
        }

        Then("the page title should start with {string}") { titleStartsWith: String ->
            // Google's search is rendered dynamically with JavaScript
            // Wait for the page to load timeout after ten seconds
            WebDriverWait(driver, 10L).until { d ->
                d.title.toLowerCase().startsWith(titleStartsWith)
            }
        }

        After { scenario: Scenario ->
            driver.quit()
        }
	}	
}