package com.neko233.easyxml.exception;

/**
 * @author SolarisNeko on 2023-01-01
 **/
public class EasyXmlException extends Exception {

    public EasyXmlException(Throwable cause) {
        super(cause);
    }

    public EasyXmlException(String message, Throwable cause) {
        super(message, cause);
    }
}
