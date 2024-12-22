package why.din.personal.expenditure.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import why.din.personal.expenditure.dto.ExpensesData;
import why.din.personal.expenditure.dto.GetExpensesRequest;
import why.din.personal.expenditure.dto.GetExpensesResponse;
import why.din.personal.expenditure.exception.PersonalExpenditureException;
import why.din.personal.expenditure.repository.GetExpensesRepository;
import why.din.personal.expenditure.util.ResponseCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Service
@Slf4j
public class GetExpensesService {
    @Autowired
    private GetExpensesRepository getExpensesRepository;
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public ResponseEntity<GetExpensesResponse> getExpenses(GetExpensesRequest req){
        String itemName = req.getItem_name();
        String categoryName = req.getCategory_name();
        String startDate = req.getStart_date();
        String endDate = req.getEnd_date();

        String res_code = "";
        String res_msg = "";
        String status = ResponseCode.FAILED.getMessage();
        Map<String, String> total = new HashMap<>();
        LinkedList<Map<String, String>> detail = new LinkedList<>();
        LocalDateTime startProcess;

        // LogsId
        LocalDateTime startTime = LocalDateTime.now();
        String microseconds = String.format("%06d", startTime.getNano() / 1000);
        String logsId = String.format("%s%s", startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), microseconds);
        try{
            String reqStr = gson.toJson(req);
            log.info(String.format("%s - Get expenses service started. Inc Req: %s", logsId, reqStr));

            // Retrieve expenses data
            startProcess = LocalDateTime.now();
            ExpensesData expensesData = getExpensesRepository.getTotalAndDetail(itemName, categoryName.toUpperCase(), startDate, endDate);
            total = expensesData.getTotal();
            detail = expensesData.getDetail();
            log.info(String.format("%s - Expenses data successfully retrieve from database. Time elapsed: %d ms",
                    logsId, Duration.between(startProcess, LocalDateTime.now()).toMillis()));
            res_code = ResponseCode.SUCCESS.getCode();
            res_msg = ResponseCode.SUCCESS.getMessage();
            status = ResponseCode.SUCCESS.getMessage();
        } catch (PersonalExpenditureException e){
            res_code = e.getErrorCode();
            res_msg = e.getErrorMessage();
            log.error(String.format("%s - PersonalExpenditureException %s: %s", logsId, e.getOrigin(), e.getDetail()));
        } catch (Exception e){
            res_code = ResponseCode.GENERAL_ERROR.getCode();
            res_msg = ResponseCode.GENERAL_ERROR.getMessage();
            log.error(String.format("%s - GeneralException: %s", logsId, e.getMessage()));
        }

        GetExpensesResponse response = new GetExpensesResponse(total, detail, res_code, res_msg, status);
        String resStr = gson.toJson(response);
        log.info(String.format("%s - Get expenses service finished. Out Req: %s. Time elapsed: %d ms",
                logsId, resStr, Duration.between(startTime, LocalDateTime.now()).toMillis()));

        return ResponseEntity.ok(response);
    }
}
