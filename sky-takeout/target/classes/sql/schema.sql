-- 创建数据库
CREATE DATABASE IF NOT EXISTS sky_takeout DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sky_takeout;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) COMMENT '姓名',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `status` INT DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `role_id` BIGINT COMMENT '角色ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `description` VARCHAR(255) COMMENT '角色描述',
  `status` INT DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `code` VARCHAR(50) NOT NULL COMMENT '权限编码',
  `description` VARCHAR(255) COMMENT '权限描述',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID',
  `type` INT NOT NULL COMMENT '权限类型：1菜单，2按钮，3接口',
  `url` VARCHAR(255) COMMENT '请求URL',
  `method` VARCHAR(10) COMMENT '请求方法',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` INT DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 店铺表
CREATE TABLE IF NOT EXISTS `shop` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '店铺名称',
  `address` VARCHAR(255) NOT NULL COMMENT '店铺地址',
  `phone` VARCHAR(20) NOT NULL COMMENT '店铺电话',
  `description` VARCHAR(500) COMMENT '店铺描述',
  `logo` VARCHAR(255) COMMENT '店铺Logo',
  `status` INT DEFAULT 1 COMMENT '状态：0关闭，1营业',
  `opening_hours` VARCHAR(100) COMMENT '营业时间',
  `delivery_fee` DECIMAL(10,2) DEFAULT 0.00 COMMENT '配送费',
  `delivery_time` INT DEFAULT 30 COMMENT '预计配送时间(分钟)',
  `min_order_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '起送金额',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺表';

-- 菜品分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `type` INT NOT NULL COMMENT '分类类型：1菜品分类，2套餐分类',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` INT DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_category` (`shop_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品分类表';

-- 菜品表
CREATE TABLE IF NOT EXISTS `dish` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '菜品名称',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `price` DECIMAL(10,2) NOT NULL COMMENT '菜品价格',
  `image` VARCHAR(255) COMMENT '菜品图片',
  `description` VARCHAR(500) COMMENT '菜品描述',
  `status` INT DEFAULT 1 COMMENT '状态：0停售，1起售',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_dish` (`shop_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品表';

-- 菜品口味表
CREATE TABLE IF NOT EXISTS `dish_flavor` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dish_id` BIGINT NOT NULL COMMENT '菜品ID',
  `name` VARCHAR(50) NOT NULL COMMENT '口味名称',
  `value` VARCHAR(255) COMMENT '口味值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品口味表';

-- 套餐表
CREATE TABLE IF NOT EXISTS `setmeal` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '套餐名称',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `price` DECIMAL(10,2) NOT NULL COMMENT '套餐价格',
  `image` VARCHAR(255) COMMENT '套餐图片',
  `description` VARCHAR(500) COMMENT '套餐描述',
  `status` INT DEFAULT 1 COMMENT '状态：0停售，1起售',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_setmeal` (`shop_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐表';

-- 套餐菜品关联表
CREATE TABLE IF NOT EXISTS `setmeal_dish` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `setmeal_id` BIGINT NOT NULL COMMENT '套餐ID',
  `dish_id` BIGINT NOT NULL COMMENT '菜品ID',
  `name` VARCHAR(100) NOT NULL COMMENT '菜品名称',
  `price` DECIMAL(10,2) NOT NULL COMMENT '菜品单价',
  `copies` INT NOT NULL COMMENT '菜品份数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐菜品关联表';

-- 库存表
CREATE TABLE IF NOT EXISTS `stock` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dish_id` BIGINT COMMENT '菜品ID',
  `setmeal_id` BIGINT COMMENT '套餐ID',
  `stock_num` INT NOT NULL COMMENT '库存数量',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_dish` (`shop_id`, `dish_id`),
  UNIQUE KEY `uk_shop_setmeal` (`shop_id`, `setmeal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 顾客表
CREATE TABLE IF NOT EXISTS `customer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openid` VARCHAR(100) NOT NULL COMMENT '微信openid',
  `nickname` VARCHAR(100) COMMENT '微信昵称',
  `avatar` VARCHAR(255) COMMENT '微信头像',
  `phone` VARCHAR(20) COMMENT '手机号',
  `gender` INT COMMENT '性别：0未知，1男，2女',
  `status` INT DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='顾客表';

-- 地址簿表
CREATE TABLE IF NOT EXISTS `address_book` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` BIGINT NOT NULL COMMENT '顾客ID',
  `consignee` VARCHAR(50) NOT NULL COMMENT '收货人',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `province` VARCHAR(50) COMMENT '省份',
  `city` VARCHAR(50) COMMENT '城市',
  `district` VARCHAR(50) COMMENT '区县',
  `detail` VARCHAR(255) NOT NULL COMMENT '详细地址',
  `label` VARCHAR(50) COMMENT '标签',
  `is_default` INT DEFAULT 0 COMMENT '是否默认地址：0否，1是',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地址簿表';

-- 购物车表
CREATE TABLE IF NOT EXISTS `shopping_cart` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` BIGINT NOT NULL COMMENT '顾客ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `dish_id` BIGINT COMMENT '菜品ID',
  `setmeal_id` BIGINT COMMENT '套餐ID',
  `dish_flavor` VARCHAR(255) COMMENT '菜品口味',
  `number` INT NOT NULL DEFAULT 1 COMMENT '数量',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单表
CREATE TABLE IF NOT EXISTS `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_number` VARCHAR(50) NOT NULL COMMENT '订单号',
  `customer_id` BIGINT NOT NULL COMMENT '顾客ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `address_book_id` BIGINT NOT NULL COMMENT '地址簿ID',
  `consignee` VARCHAR(50) NOT NULL COMMENT '收货人',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `address` VARCHAR(255) NOT NULL COMMENT '地址',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
  `discount_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
  `payment_amount` DECIMAL(10,2) NOT NULL COMMENT '实付金额',
  `status` INT NOT NULL COMMENT '订单状态：1待付款，2待接单，3待配送，4已完成，5已取消，6退款中，7已退款',
  `pay_status` INT NOT NULL COMMENT '支付状态：0未支付，1已支付，2已退款',
  `pay_method` INT NOT NULL COMMENT '支付方式：1微信支付',
  `remark` VARCHAR(500) COMMENT '备注',
  `cancel_reason` VARCHAR(500) COMMENT '取消原因',
  `estimated_delivery_time` DATETIME COMMENT '预计送达时间',
  `actual_delivery_time` DATETIME COMMENT '实际送达时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_number` (`order_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS `order_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `dish_id` BIGINT COMMENT '菜品ID',
  `setmeal_id` BIGINT COMMENT '套餐ID',
  `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `image` VARCHAR(255) COMMENT '商品图片',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '商品单价',
  `copies` INT NOT NULL COMMENT '商品份数',
  `dish_flavor` VARCHAR(255) COMMENT '菜品口味',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 支付表
CREATE TABLE IF NOT EXISTS `payment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `transaction_id` VARCHAR(100) COMMENT '微信支付交易号',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
  `status` INT NOT NULL COMMENT '支付状态：0未支付，1已支付，2已退款',
  `pay_time` DATETIME COMMENT '支付时间',
  `refund_time` DATETIME COMMENT '退款时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付表';

-- 优惠券表
CREATE TABLE IF NOT EXISTS `coupon` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
  `type` INT NOT NULL COMMENT '优惠券类型：1满减券，2折扣券，3立减券',
  `value` DECIMAL(10,2) NOT NULL COMMENT '优惠券面值',
  `min_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '使用门槛',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `stock` INT NOT NULL COMMENT '库存数量',
  `status` INT DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 用户优惠券关联表
CREATE TABLE IF NOT EXISTS `user_coupon` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` BIGINT NOT NULL COMMENT '顾客ID',
  `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
  `status` INT NOT NULL COMMENT '状态：1未使用，2已使用，3已过期',
  `get_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `use_time` DATETIME COMMENT '使用时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券关联表';

-- 活动表
CREATE TABLE IF NOT EXISTS `activity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '活动名称',
  `type` INT NOT NULL COMMENT '活动类型：1满减活动，2折扣活动，3新品活动',
  `description` VARCHAR(500) COMMENT '活动描述',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `status` INT DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 数据字典表
CREATE TABLE IF NOT EXISTS `dict` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '字典名称',
  `code` VARCHAR(50) NOT NULL COMMENT '字典编码',
  `status` INT DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT COMMENT '创建人',
  `update_user` BIGINT COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';

-- 数据字典明细表
CREATE TABLE IF NOT EXISTS `dict_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_id` BIGINT NOT NULL COMMENT '字典ID',
  `label` VARCHAR(50) NOT NULL COMMENT '字典标签',
  `value` VARCHAR(50) NOT NULL COMMENT '字典值',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` INT DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典明细表';