package why.din.personal.expenditure.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import why.din.personal.expenditure.dto.RegisterCategoryRequest;
import why.din.personal.expenditure.dto.RegisterCategoryResponse;
import why.din.personal.expenditure.exception.PersonalExpenditureException;
import why.din.personal.expenditure.repository.RegisterCategoryRepository;
import why.din.personal.expenditure.util.ResponseCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class RegisterCategoryService {
    @Autowired
    private RegisterCategoryRepository registerCategory;
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public ResponseEntity<RegisterCategoryResponse>  register (RegisterCategoryRequest req){
        String categoryName = req.getCategory_name();
        String description = req.getDescription();
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
            log.info(String.format("%s - Register category service started. Inc Req: %s", logsId, reqStr));

            startProcess = LocalDateTime.now();
            registerCategory.createCategory(categoryName.toUpperCase(), description);
            status = ResponseCode.SUCCESS.getMessage();
            res_code = ResponseCode.SUCCESS.getCode();
            res_msg = ResponseCode.SUCCESS.getMessage();
            log.info(String.format("%s - Category successfully inserted to database. Time elapsed: %d ms",
                    logsId, Duration.between(startProcess, LocalDateTime.now()).toMillis()));

//            if (!categoryName.isEmpty()){
//                startProcess = LocalDateTime.now();
//                registerCategory.createCategory(categoryName.toUpperCase(), description);
//                status = ResponseCode.SUCCESS.getMessage();
//                res_code = ResponseCode.SUCCESS.getCode();
//                res_msg = ResponseCode.SUCCESS.getMessage();
//                log.info(String.format("%s - Category successfully inserted to database. Time elapsed: %d ms",
//                        logsId, Duration.between(startProcess, LocalDateTime.now()).toMillis()));
//            } else {
//                res_code = ResponseCode.INVALID_CATEGORY.getCode();
//                res_msg = ResponseCode.INVALID_CATEGORY.getMessage();
//                log.error(String.format("%s - %s", logsId, res_msg));
//            }
        } catch (PersonalExpenditureException e){
            String err_detail = e.getDetail();
            res_code = e.getErrorCode();
            res_msg = e.getErrorMessage();
            if (!err_detail.isEmpty()){
                if(err_detail.contains("already exists")){
                    res_code = ResponseCode.DATABASE_ERROR_ALREADY_EXIST.getCode();
                    res_msg = ResponseCode.DATABASE_ERROR_ALREADY_EXIST.getMessage()+"CATEGORY NAME ALREADY EXIST";
                }
            }
            log.error(String.format("%s - PersonalExpenditureException %s: %s", logsId, e.getOrigin(), e.getDetail()));
        } catch (Exception e){
            res_code = ResponseCode.GENERAL_ERROR.getCode();
            res_msg = ResponseCode.GENERAL_ERROR.getMessage();
            log.error(String.format("%s - GeneralException: %s", logsId, e.getMessage()));
        }

        RegisterCategoryResponse response = new RegisterCategoryResponse(res_code, res_msg, status);
        String resStr = gson.toJson(response);
        log.info(String.format("%s - Register category service finished. Out Req: %s. Time elapsed: %d ms",
                logsId, resStr, Duration.between(startTime, LocalDateTime.now()).toMillis()));

        return ResponseEntity.ok(response);
    }
}
