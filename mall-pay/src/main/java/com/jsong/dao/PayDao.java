package com.jsong.dao;

import com.jsong.bean.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 2020/5/12 13:32
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Repository
public interface PayDao extends CrudRepository<Pay, String>, JpaRepository<Pay, String>, JpaSpecificationExecutor<Pay> {
}
