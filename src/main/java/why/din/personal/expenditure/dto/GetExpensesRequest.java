package why.din.personal.expenditure.dto;

import lombok.Getter;
import lombok.Setter;
import why.din.personal.expenditure.validation.ValidDate;

@Getter
@Setter
public class GetExpensesRequest {
    private String item_name;
    private String category_name;

    @ValidDate
    private String start_date;
    @ValidDate
    private String end_date;
}
