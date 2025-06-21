-- Insert permissions first (no foreign key dependencies)
INSERT INTO permissions(id, name, description)
VALUES
    (1, 'VIEW_USERS', 'Xem danh sách người dùng'), --1
    (
        2,
        'SET_ACTIVE_USER',
        'Vô hiệu hoá/ kích hoạt tài khoản'
    ),
    (
        3,
        'VIEW_ACTIVE_SAVINGTYPES',
        'Xem danh sách loại tiết kiệm đang hoạt động'
    ),
    (
        4,
        'VIEW_INACTIVE_SAVINGTYPES',
        'Xem dang sách loại tiết kiệm đã ngừng hoạt động'
    ),
    (5, 'CREATE_SAVINGTYPE', 'Tạo loại tiết kiệm mới'),
    (
        6,
        'UPDATE_SAVINGTYPE',
        'Cập nhật thông loại tiết kiệm'
    ),
    (7, 'DELETE_SAVINGTYPE', 'Xoá loại tiết kiệm'),
    (
        8,
        'SET_ACTIVE_SAVINGTYPE',
        'Vô hiệu hoá/ kích hoạt loại tiết kiệm'
    ),
    (
        9,
        'VIEW_REPORTS',
        'Xem báo cáo gồm cả báo cáo ngày và tháng'
    ),
    (
        10,
        'VIEW_ALL_SAVINGTICKETS',
        'Xem tất cả phiếu gửi tiết kiệm'
    ),
    (
        11,
        'VIEW_MY_SAVINGTICKETS',
        'Xem phiếu gửi tiết kiệm của tôi'
    ),
    (
        12,
        'CREATE_SAVINGTICKET',
        'Tạo phiếu gửi tiết kiệm'
    ),
    (
        13,
        'CREATE_WITHDRAWALTICKET',
        'Tạo phiếu rút tiền'
    ),
    (
        14,
        'VIEW_ALL_TRANSACTIONS',
        'Xem tất cả lịch sử giao dịch'
    ),
    (
        15,
        'VIEW_MY_TRANSACTIONS',
        'Xem lịch sử giao dịch của tôi'
    ),
    (
        16,
        'DEPOSIT',
        'Nạp tiền vào tài khoản'
    ),
    (
        17,
        'WITHDRAW',
        'Rút tiền khỏi tài khoản'
    ),
    (
        18,
        'VIEW_PARAMETERS',
        'Xem danh sách tham số hệ thống'
    ),
    (
        19,
        'UPDATE_PARAMETER',
        'Cập nhật tham số hệ thống'
    ),
    (
        20,
        'ADMIN_PREVILAGE',
        'Gán nhóm quyền cho người dùng'
    ),
    (
        21,
        'CHANGE_PASSWORD',
        'Đổi mật khẩu'
    ) On CONFLICT DO NOTHING;
-- Insert groups second (no foreign key dependencies)
INSERT INTO groups (id, name, description)
VALUES (1, 'ADMIN', 'Quản trị viên'),
    (2, 'CUSTOMER', 'Khách hàng'),
    (3, 'MANAGER', 'Quản lý'),
    (4, 'STAFF', 'Nhân viên') On CONFLICT DO NOTHING;
-- Insert group_permissions last (depends on both groups and permissions)
INSERT INTO group_permissions (group_id, permission_id)
VALUES -- ADMIN permissions
    (1, 1),
    (1, 2),
    (1, 20),
    -- CUSTOMER permissions
    (2, 3),
    (2, 11),
    (2, 12),
    (2, 13),
    (2, 15),
    (2, 16),
    (2, 17),
    (2, 21),
    -- MANAGER permissions
    (3, 1),
    (3, 3),
    (3, 4),
    (3, 5),
    (3, 6),
    (3, 7),
    (3, 8),
    (3, 9),
    (3, 10),
    (3, 12),
    (3, 13),
    (3, 14),
    (3, 16),
    (3, 17),
    (3, 18),
    (3, 19),
    (3, 21),
    -- STAFF permissions
    (4, 1),
    (4, 3),
    (4, 10),
    (4, 12),
    (4, 13),
    (4, 13),
    (4, 14),
    (4, 16),
    (4, 17),
    (4, 21)
 On CONFLICT DO NOTHING;
-- Reset sequences if using auto-increment

INSERT INTO public.saving_types (id, duration, interest_rate, is_active, type_name) VALUES 
(1, 0, 0.0050, true, 'Không kỳ hạn'),
(2, 3, 0.0500, true, 'Kỳ hạn ngắn'),
(3, 6, 0.0550, true, 'Kỳ hạn trung bình')
ON CONFLICT DO NOTHING;

INSERT INTO public.parameters(
id, max_transaction_amount, min_age, min_saving_amount, min_transaction_amount)
VALUES
(1, 1000000000, 15, 1000000, 10000) ON CONFLICT DO NOTHING;


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