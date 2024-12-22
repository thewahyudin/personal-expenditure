package why.din.personal.expenditure.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalExpenditureException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    private String detail;
    private String origin;
    private String carriedData;

    public PersonalExpenditureException(String errorCode, String errorMessage, String detail, String origin, String carriedData){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.detail = detail;
        this.origin = origin;
        this.carriedData = carriedData;
    }

    public PersonalExpenditureException(String message) {
        super(message);
    }
}
