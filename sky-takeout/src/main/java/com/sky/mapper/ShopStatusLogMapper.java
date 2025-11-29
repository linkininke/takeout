package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.ShopStatusLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺状态变更日志Mapper
 */
@Mapper
public interface ShopStatusLogMapper extends BaseMapper<ShopStatusLog> {

}