package com.framework.core;

import com.framework.config.ConfigManager;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.HashMap;
import java.util.Map;

/**
 * One instance per scenario, shared by all step classes through Cucumber's DI.
 * The browser starts lazily, so API and DB-only scenarios stay fast.
 */
public class ScenarioContext {
    

    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;
    private APIRequestContext api;
    private final Map<String, Object> store = new HashMap<>();

    private Playwright playwright() {
        if (playwright == null) {
            playwright = Playwright.create(new Playwright.CreateOptions()
                    .setEnv(Map.of("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1")));
        }
        return playwright;
    }

    public Page page() {
        if (page == null) {
            browser = playwright().chromium().launch(
             new BrowserType.LaunchOptions()
                .setHeadless(ConfigManager.headless())
                .setChannel("msedge"));
            
            browserContext = browser.newContext(
                    new Browser.NewContextOptions().setBaseURL(ConfigManager.require("ui.baseUrl")));
            page = browserContext.newPage();
        }
        return page;
    }

    public boolean hasPage() {
        return page != null;
    }

    public APIRequestContext api() {
        if (api == null) {
            APIRequest.NewContextOptions apiOptions = new APIRequest.NewContextOptions()
                    .setBaseURL(ConfigManager.require("api.baseUrl"));
            String token = ConfigManager.get("api.token");
            if (token != null && !token.isBlank()) {
                apiOptions.setExtraHTTPHeaders(Map.of("Authorization", "Bearer " + token));
            }
            api = playwright().request().newContext(apiOptions);
        }
        return api;
    }

    public void put(String key, Object value) {
        store.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) store.get(key);
    }

    /** Safe to call more than once (a step may close the browser before the After hook runs). */
    public void close() {
        if (api != null) api.dispose();
        if (browserContext != null) browserContext.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
        api = null;
        browserContext = null;
        browser = null;
        playwright = null;
        page = null;
    }
}
