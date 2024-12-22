package why.din.personal.expenditure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import why.din.personal.expenditure.exception.PersonalExpenditureException;
import why.din.personal.expenditure.util.ResponseCode;

import java.time.LocalDate;
import java.util.List;

@Repository
public class InputExpenseRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertExpense(String itemName, String catName, Long cost, String currency, LocalDate expenseDate, String notes) throws PersonalExpenditureException {
        try {
            String sql = "INSERT INTO PERSONAL_EXPENDITURE_ITEMS (name, category, cost, currency, expense_date, notes, CREATED_AT) " +
                    "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
            jdbcTemplate.update(sql, itemName, catName, cost, currency, expenseDate, notes);
        } catch (Exception e){
            throw new PersonalExpenditureException(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getMessage(),
                    e.getMessage(), this.getClass().getSimpleName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        }
    }

    public String checkCategory(String categoryName) throws PersonalExpenditureException {
        String data = "";
        try {
            String sql = "SELECT NAME FROM PERSONAL_EXPENDITURE_CATEGORY WHERE NAME = ?";
            List<String> result = jdbcTemplate.query(sql, new Object[]{categoryName}, new RowMapper<String>() {
                @Override
                public String mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                    return rs.getString("NAME");
                }
            });
            if (!result.isEmpty()) {
                data = result.get(0);
            }
        } catch (Exception e){
            throw new PersonalExpenditureException(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getMessage(),
                    e.getMessage(), this.getClass().getSimpleName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        }
        return data;
    }
}
