package com.bean.search.exception;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */
public class SearchException extends RuntimeException {

    private static final long serialVersionUID = -9137870101657363708L;

    public SearchException(String message) {
        super(message);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
