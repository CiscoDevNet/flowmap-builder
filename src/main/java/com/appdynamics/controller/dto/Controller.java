package com.appdynamics.controller.dto;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.Asserts;

/**
 * Created by abey.tom on 6/4/16.
 */
public class Controller {
    protected String url;
    protected String userName;
    protected String accountName;
    protected String password;
    protected String passwordEncrypted;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordEncrypted() {
        return passwordEncrypted;
    }

    public void setPasswordEncrypted(String passwordEncrypted) {
        this.passwordEncrypted = passwordEncrypted;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void validate() {
        Asserts.check(!StringUtils.isEmpty(url), "The controller url should be present");
        Asserts.check(!StringUtils.isEmpty(accountName), "The controller account name should be present");
        Asserts.check(!StringUtils.isEmpty(userName), "The controller user name should be present");
        Asserts.check(!StringUtils.isEmpty(password) || !StringUtils.isEmpty(passwordEncrypted),
                "Either Controller Password or Password encrypted should be set");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Controller)) return false;

        Controller that = (Controller) o;

        if (!url.equals(that.url)) return false;
        if (!userName.equals(that.userName)) return false;
        if (!accountName.equals(that.accountName)) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return passwordEncrypted != null ? passwordEncrypted.equals(that.passwordEncrypted) : that.passwordEncrypted == null;
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + accountName.hashCode();
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (passwordEncrypted != null ? passwordEncrypted.hashCode() : 0);
        return result;
    }
}
