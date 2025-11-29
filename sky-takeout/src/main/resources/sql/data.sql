-- 插入角色数据
INSERT INTO `role` (`id`, `name`, `description`, `status`, `create_time`, `update_time`) VALUES
(1, '管理员', '系统管理员，拥有所有权限', 1, NOW(), NOW()),
(2, '商家', '商家用户，拥有店铺管理权限', 1, NOW(), NOW());

-- 插入权限数据
INSERT INTO `permission` (`id`, `name`, `code`, `description`, `parent_id`, `type`, `url`, `method`, `sort`, `status`, `create_time`, `update_time`) VALUES
-- 用户管理权限
(1, '用户管理', 'user:manage', '用户管理菜单', 0, 1, '/system/user', 'GET', 1, 1, NOW(), NOW()),
(2, '用户列表', 'user:list', '查看用户列表', 1, 2, '/system/user/page', 'GET', 1, 1, NOW(), NOW()),
(3, '用户查询', 'user:query', '查询用户详情', 1, 2, '/system/user/{id}', 'GET', 2, 1, NOW(), NOW()),
(4, '用户添加', 'user:add', '添加用户', 1, 2, '/system/user', 'POST', 3, 1, NOW(), NOW()),
(5, '用户编辑', 'user:update', '编辑用户', 1, 2, '/system/user/{id}', 'PUT', 4, 1, NOW(), NOW()),
(6, '用户删除', 'user:delete', '删除用户', 1, 2, '/system/user/{id}', 'DELETE', 5, 1, NOW(), NOW()),

-- 角色管理权限
(7, '角色管理', 'role:manage', '角色管理菜单', 0, 1, '/system/role', 'GET', 2, 1, NOW(), NOW()),
(8, '角色列表', 'role:list', '查看角色列表', 7, 2, '/system/role/page', 'GET', 1, 1, NOW(), NOW()),
(9, '角色查询', 'role:query', '查询角色详情', 7, 2, '/system/role/{id}', 'GET', 2, 1, NOW(), NOW()),
(10, '角色添加', 'role:add', '添加角色', 7, 2, '/system/role', 'POST', 3, 1, NOW(), NOW()),
(11, '角色编辑', 'role:update', '编辑角色', 7, 2, '/system/role/{id}', 'PUT', 4, 1, NOW(), NOW()),
(12, '角色删除', 'role:delete', '删除角色', 7, 2, '/system/role/{id}', 'DELETE', 5, 1, NOW(), NOW()),

-- 权限管理权限
(13, '权限管理', 'permission:manage', '权限管理菜单', 0, 1, '/system/permission', 'GET', 3, 1, NOW(), NOW()),
(14, '权限列表', 'permission:list', '查看权限列表', 13, 2, '/system/permission/page', 'GET', 1, 1, NOW(), NOW()),
(15, '权限查询', 'permission:query', '查询权限详情', 13, 2, '/system/permission/{id}', 'GET', 2, 1, NOW(), NOW()),
(16, '权限添加', 'permission:add', '添加权限', 13, 2, '/system/permission', 'POST', 3, 1, NOW(), NOW()),
(17, '权限编辑', 'permission:update', '编辑权限', 13, 2, '/system/permission/{id}', 'PUT', 4, 1, NOW(), NOW()),
(18, '权限删除', 'permission:delete', '删除权限', 13, 2, '/system/permission/{id}', 'DELETE', 5, 1, NOW(), NOW());

-- 插入用户数据（密码：123456，已加密）
INSERT INTO `user` (`id`, `username`, `password`, `name`, `phone`, `email`, `status`, `role_id`, `create_time`, `update_time`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '管理员', '13800138000', 'admin@example.com', 1, 1, NOW(), NOW()),
(2, 'shop', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '商家', '13800138001', 'shop@example.com', 1, 2, NOW(), NOW());

-- 插入用户角色关联数据
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- 插入角色权限关联数据
-- 管理员角色拥有所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12),
(1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18);

-- 商家角色拥有部分权限
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(2, 1), (2, 2), (2, 3);
