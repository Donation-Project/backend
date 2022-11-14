package com.donation.domain.post.repository;

import com.donation.domain.post.entity.Post;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostJdbcRepository {

    private static final String TABLE = "post";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PostJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public void bulkInsert(List<Post> post) {
        var sql = String.format("INSERT INTO `%s` (user_id, state, category, title, content, amount, current_amount, create_at, update_at) " +
                "VALUES (:user.id, :state, :category, :write.title, :write.content, :amount, :currentAmount, :createAt, :updateAt)", TABLE);

        SqlParameterSource[] params = post.stream()
                .map(u -> new BeanPropertySqlParameterSource(u) {
                    @Override
                    public Object getValue(String paramName) throws IllegalArgumentException {
                        Object value = super.getValue(paramName);
                        if (value instanceof Enum) {
                            return value.toString();
                        }
                        return value;
                    }
                }).toArray(SqlParameterSource[]::new);


        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
