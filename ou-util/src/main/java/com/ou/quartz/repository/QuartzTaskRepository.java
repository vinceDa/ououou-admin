package com.ou.quartz.repository;

import com.ou.quartz.domain.QuartzTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author vince
* @date 2020/02/02 07:24:34
*/
public interface QuartzTaskRepository extends JpaRepository<QuartzTask, Long>, JpaSpecificationExecutor {


}
