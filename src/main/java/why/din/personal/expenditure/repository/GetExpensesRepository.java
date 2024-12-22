package why.din.personal.expenditure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import why.din.personal.expenditure.dto.ExpensesData;
import why.din.personal.expenditure.exception.PersonalExpenditureException;
import why.din.personal.expenditure.util.ResponseCode;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
public class GetExpensesRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ExpensesData getTotalAndDetail(String itemName, String categoryName, String startDate, String endDate) throws PersonalExpenditureException {
        Map<String, String> total = new HashMap<>();
        LinkedList<Map<String, String>> detail = new LinkedList<>();

        try {
            StringBuilder sqlTotal = new StringBuilder("SELECT CURRENCY, SUM(COST) AS TOTAL_COST " +
                    "FROM personal_expenditure_items WHERE 1=1");

            StringBuilder sqlDetail = new StringBuilder("SELECT NAME, CATEGORY, COST, CURRENCY, EXPENSE_DATE, NOTES " +
                    "FROM personal_expenditure_items WHERE 1=1");

            List<Object> params = new ArrayList<>();

            // Apply filters
            if (itemName != null && !itemName.isEmpty()) {
                sqlTotal.append(" AND NAME = ?");
                sqlDetail.append(" AND NAME = ?");
                params.add(itemName);
            }
            if (categoryName != null && !categoryName.isEmpty()) {
                sqlTotal.append(" AND category = ?");
                sqlDetail.append(" AND category = ?");
                params.add(categoryName);
            }
            if (startDate != null && !startDate.isEmpty()) {
                sqlTotal.append(" AND EXPENSE_DATE >= ?");
                sqlDetail.append(" AND EXPENSE_DATE >= ?");
                params.add(Date.valueOf(startDate));
            }
            if (endDate != null && !endDate.isEmpty()) {
                if (startDate != null && !startDate.isEmpty()) {
                    sqlTotal.append(" AND EXPENSE_DATE <= ?");
                    sqlDetail.append(" AND EXPENSE_DATE <= ?");
                } else {
                    sqlTotal.append(" AND EXPENSE_DATE < ?");
                    sqlDetail.append(" AND EXPENSE_DATE < ?");
                }
                params.add(Date.valueOf(endDate));
            }

            // Total
            sqlTotal.append(" GROUP BY CURRENCY");
            String finalSqlTotal = sqlTotal.toString();

            RowMapper<Map<String, String>> rowMapperTotal = (rs, rowNum) -> {
                Map<String, String> row = new HashMap<>();
                row.put(rs.getString("CURRENCY"), String.valueOf(rs.getDouble("TOTAL_COST")));
                return row;
            };

            List<Map<String, String>> resultTotal = jdbcTemplate.query(finalSqlTotal, rowMapperTotal, params.toArray());
            for (Map<String, String> row : resultTotal) {
                total.putAll(row);
            }

            // Detail
            String finalSqlDetail = sqlDetail.toString();

            RowMapper<Map<String, String>> rowMapperDetail = (rs, rowNum) -> {
                Map<String, String> row = new HashMap<>();
                row.put("NAME", rs.getString("NAME"));
                row.put("CATEGORY", rs.getString("CATEGORY"));
                row.put("COST", String.valueOf(rs.getDouble("COST")));
                row.put("CURRENCY", rs.getString("CURRENCY"));
                row.put("EXPENSE_DATE", rs.getDate("EXPENSE_DATE").toString());
                row.put("NOTES", rs.getString("NOTES"));
                return row;
            };

            List<Map<String, String>> resultDetail = jdbcTemplate.query(finalSqlDetail, rowMapperDetail, params.toArray());
            detail.addAll(resultDetail);

        } catch (Exception e) {
            throw new PersonalExpenditureException(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getMessage(),
                    e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        }
        ExpensesData result = new ExpensesData(total, detail);
        return result;
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
        } catch (Exception e) {
            throw new PersonalExpenditureException(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getMessage(),
                    e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        }
        return data;
    }
}
