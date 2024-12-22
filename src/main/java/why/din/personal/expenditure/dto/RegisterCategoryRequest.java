package why.din.personal.expenditure.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCategoryRequest {
    @NotBlank(message = "Category name is required")
    private String category_name;

    private String description;
}
