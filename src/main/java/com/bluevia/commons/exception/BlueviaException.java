package com.bluevia.commons.exception;

public class BlueviaException extends Exception {

	private static final long serialVersionUID = 6874105139023845677L;

    public static final int BAD_REQUEST_EXCEPTION = -1;
    public static final int CONNECTION_ERROR = -2;
    public static final int NOT_IMPLEMENTED_FUNCTION = -3;
    public static final int BAD_ENCODING_EXCEPTION = -4;
    public static final int INVALID_PATH_EXCEPTION = -5;
    public static final int MISSING_PARSER_EXCEPTION = -6;
    public static final int INVALID_MODE_EXCEPTION = -7;
    public static final int PARSER_EXCEPTION = -10;
    public static final int SERIALIZER_EXCEPTION = -11;
    public static final int INTERNAL_CLIENT_ERROR = -12;
	
    private final int code;
    
    public BlueviaException(int code) {
        this.code = code;
    }
    
    public BlueviaException(String detailMessage, int code) {
        super(detailMessage);
        this.code = code;
    }

    public BlueviaException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public BlueviaException(String detailMessage, Throwable throwable, int code) {
        super(detailMessage, throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
}
