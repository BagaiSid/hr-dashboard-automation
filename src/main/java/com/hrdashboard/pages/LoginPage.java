package com.hrdashboard.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "button.btn-login")
    private WebElement loginButton;

    @FindBy(id = "errorMessage")
    private WebElement errorMessage;

    @FindBy(css = ".login-header h1")
    private WebElement loginTitle;

    @FindBy(id = "loginForm")
    private WebElement loginForm;

    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public DashboardPage clickLogin() {
        click(loginButton);
        return new DashboardPage();
    }

    public LoginPage clickLoginExpectingFailure() {
        click(loginButton);
        return this;
    }

    public DashboardPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            return errorMessage.isDisplayed() && !errorMessage.getText().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginTitle);
    }
}
