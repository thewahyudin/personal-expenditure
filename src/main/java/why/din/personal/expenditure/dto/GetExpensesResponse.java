package why.din.personal.expenditure.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
public class GetExpensesResponse {
    private Map<String, String> total;
    private LinkedList<Map<String, String>> detail;
    private String res_code;
    private String res_msg;
    private String status;

    public GetExpensesResponse(Map<String, String> total, LinkedList<Map<String, String>> detail, String res_code, String res_msg, String status) {
        this.total = total;
        this.detail = detail;
        this.res_code = res_code;
        this.res_msg = res_msg;
        this.status = status;
    }

    @Getter
    @Setter
    public class total{
        private String amount;
        private String currency;

        public total(String amount, String currency) {
            this.amount = amount;
            this.currency = currency;
        }
    }

    @Getter
    @Setter
    public class detail{
        private Map<String, String> detail_item;

        public detail(Map<String, String> detail_item){
            this.detail_item = detail_item;
        }
    }
}
