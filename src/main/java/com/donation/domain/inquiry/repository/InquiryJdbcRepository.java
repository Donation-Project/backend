package com.donation.domain.inquiry.repository;

import com.donation.domain.inquiry.dto.InquiryFindReqDto;
import com.donation.domain.inquiry.dto.InquirySaveReqDto;
import com.donation.domain.inquiry.dto.InquiryUpdateReqDto;
import com.donation.domain.inquiry.entity.Inquiry;
import com.donation.domain.inquiry.entity.InquiryState;
import com.donation.domain.user.entity.User;
import com.donation.infrastructure.embed.Write;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.*;

public class InquiryJdbcRepository implements InquiryRepository{

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public InquiryJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("Inquiry")
                .usingGeneratedKeyColumns("inquiry_id");
    }

    /**
     * 게시글 저장
     * @param dto
     */
    @Override
    public void save(InquirySaveReqDto dto) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("state",dto.getInquiryState());
        params.put("title",dto.getTitle());
        params.put("content",dto.getContent());
        params.put("user_id",dto.getUser_id());
        jdbcInsert.executeAndReturnKey(params).longValue();
    }

    /**
     * 게시글 조회
     * @param inquiry_id
     * @return
     */
    @Override
    public InquiryFindReqDto findById(Long inquiry_id) {
        String sql = "select INQUIRY_ID, TITLE, CONTENT, USER_ID from INQUIRY where INQUIRY_ID = :inquiry_id";
        Map<String, Object> param = Collections.singletonMap("inquiry_id",inquiry_id);
        try{
            return template.queryForObject(sql, param, RowMapper());
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    /**
     * 게시글 전체 조회
     * @return
     */
    @Override
    public List<InquiryFindReqDto> findAll() {
        String sql = "select INQUIRY_ID, TITLE, CONTENT, USER_ID from INQUIRY";

        return template.query(sql, RowMapper());
    }

    /**
     * 게시글 수정
     * @param dto
     */
    @Override
    public void update(InquiryUpdateReqDto dto) {
        String sql = "update Inquiry set title = :title, content = :content where inquiry_id = :inquiry_id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("title", dto.getTitle())
                .addValue("content", dto.getContent())
                .addValue("inquiry_id", dto.getInquiry_id());
        template.update(sql,param);
    }

    /**
     * 게시글 삭제
     * @param inquiry_id
     */
    @Override
    public void delete(Long inquiry_id) {
        String sql = "delete from Inquiry where inquiry_id = :inquiry_id";

        template.update(sql, Collections.singletonMap("inquiry_id",inquiry_id));
    }


    /**
     * 게시글 키워드(제목)으로 검색
     * @param title
     * @return
     */
    @Override
    public List<InquiryFindReqDto> findByTitle(String title) {
        String sql = "select INQUIRY_ID, TITLE, CONTENT, USER_ID from INQUIRY where TITLE like '%'||:title||'%'";
        Map<String, Object> param = Collections.singletonMap("title", title);
        return template.query(sql, param, RowMapper());
    }

    private RowMapper<InquiryFindReqDto> RowMapper(){
        return (rs, rowNum) -> {
            InquiryFindReqDto findReqDto = new InquiryFindReqDto(
                    rs.getLong("inquiry_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getLong("user_id")
            );
            return findReqDto;
        };

    }

    @Override
    public void clear() {
        List<InquiryFindReqDto> all = findAll();
        for(InquiryFindReqDto dto : all){
            delete(dto.getInquiry_id());
        }
    }

    private RowMapper<Inquiry> inquiryRowMapper(){
        return BeanPropertyRowMapper.newInstance(Inquiry.class);
    }


}
