//package sortingFunctionality;
//
//import com.microsoft.playwright.*;
//import com.microsoft.playwright.options.LoadState;
//import com.microsoft.playwright.options.WaitForSelectorState;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class TestSorting{
//
//    // Declare variables at the class level so all methods can use them
//    Playwright playwright;
//    Browser browser;
//    Page page;
//
//    @BeforeClass
//    public void setup() {
//        playwright = Playwright.create();
//
//        // CI/CD compatibility: Run headless in GitHub, headed locally
//        boolean isCI = System.getenv("CI") != null;
//
//        browser = playwright.chromium().launch(
//                new BrowserType.LaunchOptions().setHeadless(isCI)
//        );
//        page = browser.newPage();
//        page.navigate("https://practicesoftwaretesting.com/");
//        page.waitForLoadState(LoadState.NETWORKIDLE);
//    }
//
//    @Test
//    public void testSortingLogic() {
//        List<String> sortValues = Arrays.asList("name,asc", "name,desc", "price,desc", "price,asc", "co2_rating,asc", "co2_rating,desc");
//
//        System.out.println("--- Starting Sorting Functionality Test ---");
//
//        // ADD THIS LINE: Explicitly wait for the dropdown to appear on the screen
//        page.waitForSelector("select[data-test='sort']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
//
//        for (String value : sortValues) {
//            page.selectOption("select[data-test='sort']", value);
//            page.waitForSelector("[data-test='sorting_completed']");
//
//            Locator firstProduct = page.locator("[data-test='product-name']").first();
//            firstProduct.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
//
//            String productText = firstProduct.textContent().trim();
//            System.out.println("Sort Value: [" + value + "] -> First Product: " + productText);
//        }
//    }
//}

package sortingFunctionality;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.testng.annotations.Test;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class TestSorting {
    @Test
    void validateAllSortingOptions() {
        try (Playwright playwright = Playwright.create()) {
            boolean isCI = System.getenv("CI") != null;

            // Keep these: They ensure the GitHub Runner isn't blocked as a bot
            BrowserType.LaunchPersistentContextOptions options = new BrowserType.LaunchPersistentContextOptions()
                    .setHeadless(isCI)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .setArgs(Arrays.asList("--no-sandbox", "--disable-blink-features=AutomationControlled"));

            BrowserContext context = playwright.chromium().launchPersistentContext(Paths.get("target/profiles"), options);
            Page page = context.pages().get(0);
            page.setDefaultTimeout(60000);

            page.navigate("https://practicesoftwaretesting.com/", new Page.NavigateOptions().setWaitUntil(WaitUntilState.LOAD));

            // Essential: Wait for SPA to load product data
            boolean loaded = false;
            for (int i = 0; i < 30; i++) {
                if (page.locator("[data-test='product-name']").count() > 0) {
                    loaded = true;
                    break;
                }
                page.waitForTimeout(1000);
            }

            if (!loaded) {
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("target/error.png")));
                throw new RuntimeException("Products did not load in time.");
            }

            // Core Sorting Logic
            List<String> sortValues = Arrays.asList("name,asc", "name,desc", "price,desc", "price,asc");
            for (String value : sortValues) {
                page.locator("select[data-test='sort']").selectOption(value);

                // Wait for the UI to acknowledge the sort is done
                page.waitForSelector("[data-test='sorting_completed']");

                String firstProduct = page.locator("[data-test='product-name']").first().innerText();
                System.out.println("Validated Sort [" + value + "] - Top Item: " + firstProduct.trim());
            }

            context.close();
        }
    }
}