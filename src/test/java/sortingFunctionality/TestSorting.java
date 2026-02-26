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
        // Your original list of values
        List<String> sortValues = Arrays.asList(
                "name,asc",
                "name,desc",
                "price,desc",
                "price,asc",
                "co2_rating,asc",
                "co2_rating,desc"
        );

        System.out.println("--- Starting Sorting Functionality Test ---");

        // Instead of just selecting immediately, wait for the element to be present first
        for (String value : sortValues) {
            // 1. Explicitly wait for the dropdown to be attached to the page
            page.waitForSelector("select[data-test='sort']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED));

            // 2. Now select the option
            page.selectOption("select[data-test='sort']", value);

            // Locate first product
            Locator firstProduct = page.locator("[data-test='product-name']").first();

            // Wait for visibility
            firstProduct.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            String productText = firstProduct.textContent().trim();

            // Output result
            System.out.println("Sort Value: [" + value + "] -> First Product: " + productText);
        }

        System.out.println("--- All sorting options tested successfully ---");
    }

    @AfterClass
    public void tearDown() {
        // Safely close the browser
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}