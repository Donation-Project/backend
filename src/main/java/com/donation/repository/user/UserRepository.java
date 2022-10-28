package com.donation.repository.user;


import com.donation.common.response.user.UserRespDto;
import com.donation.domain.entites.User;
import com.donation.exception.DonationDuplicateException;
import com.donation.exception.DonationNotFoundException;
import com.donation.repository.utils.PageCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(final Long id);
    boolean existsByEmail(final String email);
    Optional<User> findByEmail(String email);
    List<User> findAllByIdIn(List<Long> id);
    default User getById(final Long id){
        return findById(id)
                .orElseThrow(() -> new DonationNotFoundException("존재하지 않는 회원입니다."));
    }
    default User getByEmail(final String email){
        return findByEmail(email)
                .orElseThrow(() -> new DonationNotFoundException("존재하지 않는 회원입니다."));
    }
    default void validateExistsById(final Long id){
        if (!existsById(id)){
            throw new DonationNotFoundException("존재하지 않는 회원번호입니다.");
        }
    }
    default void validateExistsByEmail(final String email){
        if(existsByEmail(email)){
            throw new DonationDuplicateException("이미 존재하는 이메일입니다.");
        }
    }
    default PageCustom<UserRespDto> getPageDtoList(Pageable pageable){
        List<UserRespDto> content = findAll(pageable).stream().map(UserRespDto::new).collect(Collectors.toList());
        return new PageCustom<>(PageableExecutionUtils.getPage(content, pageable, this::count));
    }
}
