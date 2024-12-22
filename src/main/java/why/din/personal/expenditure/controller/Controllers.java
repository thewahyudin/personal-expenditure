package why.din.personal.expenditure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import why.din.personal.expenditure.dto.*;
import why.din.personal.expenditure.service.GetExpensesService;
import why.din.personal.expenditure.service.InputExpenseService;
import why.din.personal.expenditure.service.RegisterCategoryService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
@Validated
public class Controllers {

    private final RegisterCategoryService registerCategoryService;
    private final InputExpenseService inputExpenseService;
    private final GetExpensesService getExpensesService;

    @PostMapping("register-category")
    public ResponseEntity<RegisterCategoryResponse> regCat(@Valid @RequestBody RegisterCategoryRequest regCatReq) {
        return registerCategoryService.register(regCatReq);
    }

    @PostMapping("input-expense")
    public ResponseEntity<InputExpenseResponse> inExp(@Valid @RequestBody InputExpenseRequest inExpReq) {
        return inputExpenseService.inputExpense(inExpReq);
    }

    @PostMapping("get-expenses")
    public ResponseEntity<GetExpensesResponse> getExp(@Valid @RequestBody GetExpensesRequest getExpReq) {
        return getExpensesService.getExpenses(getExpReq);
    }
}
