package why.din.personal.expenditure.util;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS("00", "SUCCESS"),
    FAILED("", "FAILED"),
    INVALID_CATEGORY("01", "INVALID CATEGORY"),
    UNREGISTERED_CATEGORY("02", "UNREGISTERED CATEGORY"),
    DATABASE_ERROR("31", "DATABASE ERROR"),
    DATABASE_ERROR_ALREADY_EXIST("32", "DATABASE ERROR: "),
    GENERAL_ERROR("99", "GENERAL ERROR");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}