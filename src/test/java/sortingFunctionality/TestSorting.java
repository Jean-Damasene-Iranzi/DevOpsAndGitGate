package sortingFunctionality;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class TestSorting{

    // Declare variables at the class level so all methods can use them
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();

        // CI/CD compatibility: Run headless in GitHub, headed locally
        boolean isCI = System.getenv("CI") != null;

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(isCI)
        );
        page = browser.newPage();
        page.navigate("https://practicesoftwaretesting.com/");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Test
    public void testSortingLogic() {
        List<String> sortValues = Arrays.asList("name,asc", "name,desc", "price,desc", "price,asc", "co2_rating,asc", "co2_rating,desc");

        System.out.println("--- Starting Sorting Functionality Test ---");

        // ADD THIS LINE: Explicitly wait for the dropdown to appear on the screen
        page.waitForSelector("select[data-test='sort']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

        for (String value : sortValues) {
            page.selectOption("select[data-test='sort']", value);
            page.waitForSelector("[data-test='sorting_completed']");

            Locator firstProduct = page.locator("[data-test='product-name']").first();
            firstProduct.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            String productText = firstProduct.textContent().trim();
            System.out.println("Sort Value: [" + value + "] -> First Product: " + productText);
        }
    }
}