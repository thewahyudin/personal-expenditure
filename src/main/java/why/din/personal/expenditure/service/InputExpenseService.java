package why.din.personal.expenditure.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import why.din.personal.expenditure.dto.InputExpenseRequest;
import why.din.personal.expenditure.dto.InputExpenseResponse;
import why.din.personal.expenditure.exception.PersonalExpenditureException;
import why.din.personal.expenditure.repository.InputExpenseRepository;
import why.din.personal.expenditure.repository.RegisterCategoryRepository;
import why.din.personal.expenditure.util.ResponseCode;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class InputExpenseService {
    @Autowired
    private InputExpenseRepository inputExpenseRepository;
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public ResponseEntity<InputExpenseResponse> inputExpense (InputExpenseRequest req){
        String itemName = req.getItem_name();
        String categoryName = req.getCategory_name();
        String cost = req.getCost();
        String currency = String.valueOf(req.getCurrency());
        String expenseDate = req.getDate();
        String notes = req.getNotes();

        String res_code = "";
        String res_msg = "";
        String status = ResponseCode.FAILED.getMessage();
        LocalDateTime startProcess;

        // LogsId
        LocalDateTime startTime = LocalDateTime.now();
        String microseconds = String.format("%06d", startTime.getNano() / 1000);
        String logsId = String.format("%s%s", startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), microseconds);

        try{
            String reqStr = gson.toJson(req);
            log.info(String.format("%s - Input expense item service started. Inc Req: %s", logsId, reqStr));

            // Check category
            startProcess = LocalDateTime.now();
            String catInDb = inputExpenseRepository.checkCategory(categoryName.toUpperCase());
            if(categoryName.toUpperCase().equals(catInDb)){
                log.info(String.format("%s - Category is valid. Time elapsed: %d ms",
                        logsId, Duration.between(startProcess, LocalDateTime.now()).toMillis()));

                startProcess = LocalDateTime.now();
                Long costL = Long.parseLong(cost);
                LocalDate expenseDateD = LocalDate.parse(expenseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                inputExpenseRepository.insertExpense(itemName, categoryName.toUpperCase(), costL, currency, expenseDateD, notes);
                status = ResponseCode.SUCCESS.getMessage();
                res_code = ResponseCode.SUCCESS.getCode();
                res_msg = ResponseCode.SUCCESS.getMessage();
                log.info(String.format("%s - Expense item successfully inserted to database. Time elapsed: %d ms",
                        logsId, Duration.between(startProcess, LocalDateTime.now()).toMillis()));
            } else {
                res_code = ResponseCode.UNREGISTERED_CATEGORY.getCode();
                res_msg = ResponseCode.UNREGISTERED_CATEGORY.getMessage();
                log.error(String.format("%s - %s", logsId, res_msg));
            }
        } catch (PersonalExpenditureException e){
            res_code = e.getErrorCode();
            res_msg = e.getErrorMessage();
            log.error(String.format("%s - PersonalExpenditureException %s: %s", logsId, e.getOrigin(), e.getDetail()));
        } catch (Exception e){
            res_code = ResponseCode.GENERAL_ERROR.getCode();
            res_msg = ResponseCode.GENERAL_ERROR.getMessage();
            log.error(String.format("%s - GeneralException: %s", logsId, e.getMessage()));
        }
        InputExpenseResponse response = new InputExpenseResponse(res_code, res_msg, status);
        String resStr = gson.toJson(response);
        log.info(String.format("%s - Input expense item service finished. Out Req: %s. Time elapsed: %d ms",
                logsId, resStr, Duration.between(startTime, LocalDateTime.now()).toMillis()));

        return ResponseEntity.ok(response);
    }
}
