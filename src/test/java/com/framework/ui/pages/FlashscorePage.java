package com.framework.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class FlashscorePage extends BasePage {

    private static final String SPORT_MENU_ITEM_4 = "(//div[@id='sportTopMenu']//span)[4]";
    private static final String LIVE_TABLE_ITEM_4 = "(//div[@id='live-table']//span)[4]";

    public FlashscorePage(Page page) {
        super(page);
    }

    public void clickSportMenuItem4() {
        locator(SPORT_MENU_ITEM_4).click();
    }

    public Locator liveTableItem4() {
        return locator(LIVE_TABLE_ITEM_4);
    }
}
