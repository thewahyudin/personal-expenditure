package why.din.personal.expenditure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import why.din.personal.expenditure.util.Currency;
import why.din.personal.expenditure.validation.ValidDate;


@Getter
@Setter
public class InputExpenseRequest {

    @NotBlank(message = "Item name is required")
    private String item_name;

    @NotBlank(message = "Category name is required")
    private String category_name;

    @NotBlank(message = "Cost is required")
    @Pattern(regexp = "^(0|[1-9]\\d*)(\\.\\d{1,2})?$", message = "Cost cannot be negative and must be a valid number with up to 2 decimal places")
    private String cost;

    @NotNull(message = "Invalid currency. Type an ISO code of respective currency i.e IDR, USD, JPY, etc")
    private Currency currency;

    @NotBlank(message = "Expense date is required in format YYYY-MM-DD i.e 1945-08-17")
    @ValidDate
    private String date;

    private String notes;
}
