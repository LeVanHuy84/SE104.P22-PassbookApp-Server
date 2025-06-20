-- Insert permissions first (no foreign key dependencies)
INSERT INTO permissions(name, description)
VALUES ('VIEW_MY_INFO', 'Xem thông tin cá nhân'),
    ('VIEW_USERS', 'Xem danh sách người dùng'),
    (
        'SET_ACTIVE_USER',
        'Vô hiệu hoá/ kích hoạt tài khoản'
    ),
    (
        'VIEW_ACTIVE_SAVINGTYPES',
        'Xem danh sách loại tiết kiệm đang hoạt động'
    ),
    (
        'VIEW_INACTIVE_SAVINGTYPES',
        'Xem dang sách loại tiết kiệm đã ngừng hoạt động'
    ),
    ('CREATE_SAVINGTYPE', 'Tạo loại tiết kiệm mới'),
    (
        'UPDATE_SAVINGTYPE',
        'Cập nhật thông loại tiết kiệm'
    ),
    ('DELETE_SAVINGTYPE', 'Xoá loại tiết kiệm'),
    (
        'SET_ACTIVE_SAVINGTYPE',
        'Vô hiệu hoá/ kích hoạt loại tiết kiệm'
    ),
    (
        'VIEW_REPORTS',
        'Xem báo cáo gồm cả báo cáo ngày và tháng'
    ),
    (
        'VIEW_ALL_SAVINGTICKETS',
        'Xem tất cả phiếu gửi tiết kiệm'
    ),
    (
        'VIEW_MY_SAVINGTICKETS',
        'Xem phiếu gửi tiết kiệm của tôi'
    ),
    (
        'CREATE_SAVINGTICKET',
        'Tạo phiếu gửi tiết kiệm'
    ),
    (
        'CREATE_WITHDRAWALTICKET',
        'Tạo phiếu rút tiền'
    ),
    (
        'VIEW_ALL_TRANSACTIONS',
        'Xem tất cả lịch sử giao dịch'
    ),
    (
        'VIEW_MY_TRANSACTIONS',
        'Xem lịch sử giao dịch của tôi'
    ),
    ('DEPOSIT', 'Nạp tiền vào tài khoản'),
    ('WITHDRAWAL', 'Rút tiền khỏi tài khoản'),
    (
        'VIEW_PARAMETERS',
        'Xem danh sách tham số hệ thống'
    ),
    (
        'UPDATE_PARAMETERS',
        'Cập nhật tham số hệ thống'
    ) On CONFLICT DO NOTHING;
-- Insert groups second (no foreign key dependencies)
INSERT INTO groups (id, name, description)
VALUES (1, 'ADMIN', 'Quản trị viên'),
    (2, 'STAFF', 'Nhân viên'),
    (3, 'CUSTOMER', 'Khách hàng') On CONFLICT DO NOTHING;
-- Insert group_permissions last (depends on both groups and permissions)
INSERT INTO group_permissions (group_id, permission_id)
VALUES -- ADMIN permissions
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7),
    (1, 8),
    (1, 9),
    (1, 19),
    (1, 20),
    -- STAFF permissions
    (2, 1),
    (2, 2),
    (2, 4),
    (2, 10),
    (2, 11),
    (2, 13),
    (2, 14),
    (2, 15),
    (2, 17),
    (2, 18),
    -- CUSTOMER permissions
    (3, 1),
    (3, 4),
    (3, 12),
    (3, 13),
    (3, 14),
    (3, 16),
    (3, 17),
    (3, 18) On CONFLICT DO NOTHING;
-- Reset sequences if using auto-increment
SELECT setval(
        'permissions_id_seq',
        (
            SELECT MAX(id)
            FROM permissions
        )
    );
SELECT setval(
        'groups_id_seq',
        (
            SELECT MAX(id)
            FROM groups
        )
    );