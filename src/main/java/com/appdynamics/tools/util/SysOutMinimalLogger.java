package com.appdynamics.tools.util;

import org.slf4j.helpers.MarkerIgnoringBase;

/**
 * Created by abey.tom on 5/17/16.
 */
public class SysOutMinimalLogger extends MarkerIgnoringBase {

    public boolean isTraceEnabled() {
        return false;
    }

    public void trace(String s) {
        throw new UnsupportedOperationException();
    }

    public void trace(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    public void trace(String s, Object o, Object o1) {
        throw new UnsupportedOperationException();
    }

    public void trace(String s, Object... objects) {
        throw new UnsupportedOperationException();
    }

    public void trace(String s, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    public boolean isDebugEnabled() {
        return false;
    }

    public void debug(String s) {
        throw new UnsupportedOperationException();
    }

    public void debug(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    public void debug(String s, Object o, Object o1) {
        throw new UnsupportedOperationException();
    }

    public void debug(String s, Object... objects) {
        throw new UnsupportedOperationException();
    }

    public void debug(String s, Throwable throwable) {

    }

    public boolean isInfoEnabled() {
        return true;
    }

    public void info(String s) {
        System.out.println("[INFO] " + s);
    }

    public void info(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    public void info(String s, Object o, Object o1) {
        throw new UnsupportedOperationException();
    }

    public void info(String s, Object... objects) {
        throw new UnsupportedOperationException();
    }

    public void info(String s, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    public boolean isWarnEnabled() {
        return true;
    }

    public void warn(String s) {
        System.out.println("[WARN] " + s);
    }

    public void warn(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    public void warn(String s, Object... objects) {
        throw new UnsupportedOperationException();
    }

    public void warn(String s, Object o, Object o1) {
        throw new UnsupportedOperationException();
    }

    public void warn(String s, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    public boolean isErrorEnabled() {
        return true;
    }

    public void error(String s) {
        System.out.println("[ERROR] " + s);
    }

    public void error(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    public void error(String s, Object o, Object o1) {
        throw new UnsupportedOperationException();
    }

    public void error(String s, Object... objects) {
        throw new UnsupportedOperationException();
    }

    public void error(String s, Throwable throwable) {
        System.out.println("[ERROR] " + s);
        throwable.printStackTrace();
    }
}
