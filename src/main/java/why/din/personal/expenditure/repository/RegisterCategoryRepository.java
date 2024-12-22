package why.din.personal.expenditure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import why.din.personal.expenditure.exception.PersonalExpenditureException;
import why.din.personal.expenditure.util.ResponseCode;

import java.security.Timestamp;

@Repository
public class RegisterCategoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createCategory(String name, String description) throws PersonalExpenditureException {
        try {
            String sql = "INSERT INTO PERSONAL_EXPENDITURE_CATEGORY (name, description, CREATED_AT) " +
                    "VALUES (?, ?, CURRENT_TIMESTAMP)";
            jdbcTemplate.update(sql, name, description);
        } catch (Exception e){
            throw new PersonalExpenditureException(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getMessage(),
                    e.getMessage(), this.getClass().getSimpleName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        }
    }
}
