package why.din.personal.expenditure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCategoryResponse {
    String res_code;
    String res_msg;
    String status;

    public RegisterCategoryResponse(String res_code, String res_msg, String status) {
        this.res_code = res_code;
        this.res_msg = res_msg;
        this.status = status;
    }
}
