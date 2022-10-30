package com.donation.repository.favorite;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Deprecated
@Repository
public class FavoriteRedisRepository {

    private RedisTemplate<String, String> redisTemplate;
    private SetOperations<String, String> setOperations;

    public FavoriteRedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.setOperations = redisTemplate.opsForSet();
    }

    public SetOperations<String, String> getSetOperations() {
        return setOperations;
    }

    public Boolean save(Long key, Long value){
        setOperations.add(generate(key), generate(value));
        return true;
    }

    public Boolean findById(Long key, Long value){
        return setOperations.isMember(generate(key), generate(value));
    }

    public List<Long> findAll(Long key){
        return setOperations.members(generate(key)).stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public Long count(Long key){
        return setOperations.size(generate(key));
    }

    public Boolean delete(Long key, Long value){
        setOperations.remove(generate(key), generate(value));
        return true;
    }

    public Boolean deleteAll(Long key){
        findAll(key).forEach(value -> delete(key, value));
        return true;
    }
    private String generate(Long key) {
        return key.toString();
    }
}
