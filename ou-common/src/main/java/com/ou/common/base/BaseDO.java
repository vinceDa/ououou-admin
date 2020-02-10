package com.ou.common.base;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author vince
 * @date 2019/10/10 23:32
 */
@Data
@MappedSuperclass
public class BaseDO {

    /**
     *  主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     *  创建人id
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     *  更新人id
     */
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     *  创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     *  更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;



}
