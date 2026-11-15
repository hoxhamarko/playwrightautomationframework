package com.framework.ui.pages;

import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private static final String PATH = "/login";

    public LoginPage(Page page) {
        super(page);
    }

    public void open() {
        open(PATH);
    }

    public void login(String username, String password) {
        locator("#username").fill(username);
        locator("#password").fill(password);
        locator("button[type=submit]").click();
    }
}
