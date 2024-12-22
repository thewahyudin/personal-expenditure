package why.din.personal.expenditure.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
public class ExpensesData {
    private Map<String, String> total;
    private LinkedList<Map<String, String>> detail;

    public ExpensesData(Map<String, String> total, LinkedList<Map<String, String>> detail) {
        this.total = total;
        this.detail = detail;
    }
}
