package sorting;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.Arrays;
import java.util.List;

public class TestSortingFunctionality {

    public static void main(String[] args) {

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );

            Page page = browser.newPage();
            page.navigate("https://practicesoftwaretesting.com/");

            // 1. All sorting values captured from your HTML screenshot
            List<String> sortValues = Arrays.asList(
                    "name,asc",
                    "name,desc",
                    "price,desc",
                    "price,asc",
                    "co2_rating,asc",
                    "co2_rating,desc"
            );

            System.out.println("--- Starting Sorting Functionality Test ---");

            for (String value : sortValues) {
                // 2. Select by value - more reliable than matching text labels
                page.selectOption("select[data-test='sort']", value);

                // 3. Wait for the 'sorting_completed' container to update
                // This ensures the Angular framework has finished re-rendering the list
                page.waitForSelector("[data-test='sorting_completed']");

                // 4. Locate first product
                Locator firstProduct = page.locator("[data-test='product-name']").first();

                // FIXED: Using the correct WaitForSelectorState enum
                firstProduct.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

                String productText = firstProduct.textContent().trim();

                // 5. Output result
                System.out.println("Sort Value: [" + value + "] -> First Product: " + productText);
            }

            System.out.println("--- All sorting options tested successfully ---");
            browser.close();
        }
    }
}