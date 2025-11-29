package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Point;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointMapper extends BaseMapper<Point> {
    /**
     * 根据顾客ID获取积分余额
     * @param customerId 顾客ID
     * @return 积分余额
     */
    Integer getPointBalance(Long customerId);
}