--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4
--
-- Data for Name: groups; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.groups (id, description, name) VALUES (1, 'Quản trị viên', 'ADMIN');
INSERT INTO public.groups (id, description, name) VALUES (3, 'Quản lý', 'MANAGER');
INSERT INTO public.groups (id, description, name) VALUES (4, 'Nhân viên', 'STAFF');
INSERT INTO public.groups (id, description, name) VALUES (2, 'Khách hàng', 'CUSTOMER');


--
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.permissions (id, description, name) VALUES (1, 'Xem danh sách người dùng', 'VIEW_USERS');
INSERT INTO public.permissions (id, description, name) VALUES (2, 'Vô hiệu hoá/ kích hoạt tài khoản', 'SET_ACTIVE_USER');
INSERT INTO public.permissions (id, description, name) VALUES (3, 'Xem danh sách loại tiết kiệm đang hoạt động', 'VIEW_ACTIVE_SAVINGTYPES');
INSERT INTO public.permissions (id, description, name) VALUES (4, 'Xem dang sách loại tiết kiệm đã ngừng hoạt động', 'VIEW_INACTIVE_SAVINGTYPES');
INSERT INTO public.permissions (id, description, name) VALUES (5, 'Tạo loại tiết kiệm mới', 'CREATE_SAVINGTYPE');
INSERT INTO public.permissions (id, description, name) VALUES (6, 'Cập nhật thông loại tiết kiệm', 'UPDATE_SAVINGTYPE');
INSERT INTO public.permissions (id, description, name) VALUES (7, 'Xoá loại tiết kiệm', 'DELETE_SAVINGTYPE');
INSERT INTO public.permissions (id, description, name) VALUES (8, 'Vô hiệu hoá/ kích hoạt loại tiết kiệm', 'SET_ACTIVE_SAVINGTYPE');
INSERT INTO public.permissions (id, description, name) VALUES (9, 'Xem báo cáo gồm cả báo cáo ngày và tháng', 'VIEW_REPORTS');
INSERT INTO public.permissions (id, description, name) VALUES (10, 'Xem tất cả phiếu gửi tiết kiệm', 'VIEW_ALL_SAVINGTICKETS');
INSERT INTO public.permissions (id, description, name) VALUES (11, 'Xem phiếu gửi tiết kiệm của tôi', 'VIEW_MY_SAVINGTICKETS');
INSERT INTO public.permissions (id, description, name) VALUES (12, 'Tạo phiếu gửi tiết kiệm', 'CREATE_SAVINGTICKET');
INSERT INTO public.permissions (id, description, name) VALUES (13, 'Tạo phiếu rút tiền', 'CREATE_WITHDRAWALTICKET');
INSERT INTO public.permissions (id, description, name) VALUES (14, 'Xem tất cả lịch sử giao dịch', 'VIEW_ALL_TRANSACTIONS');
INSERT INTO public.permissions (id, description, name) VALUES (15, 'Xem lịch sử giao dịch của tôi', 'VIEW_MY_TRANSACTIONS');
INSERT INTO public.permissions (id, description, name) VALUES (16, 'Nạp tiền vào tài khoản', 'DEPOSIT');
INSERT INTO public.permissions (id, description, name) VALUES (17, 'Rút tiền khỏi tài khoản', 'WITHDRAW');
INSERT INTO public.permissions (id, description, name) VALUES (18, 'Xem danh sách tham số hệ thống', 'VIEW_PARAMETERS');
INSERT INTO public.permissions (id, description, name) VALUES (19, 'Cập nhật tham số hệ thống', 'UPDATE_PARAMETER');
INSERT INTO public.permissions (id, description, name) VALUES (20, 'Gán nhóm quyền cho người dùng', 'ADMIN_PREVILAGE');
INSERT INTO public.permissions (id, description, name) VALUES (21, 'Đổi mật khẩu', 'CHANGE_PASSWORD');


--
-- Data for Name: group_permissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.group_permissions (group_id, permission_id) VALUES (2, 15);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (2, 11);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (2, 12);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (2, 17);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (2, 13);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (2, 16);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (2, 3);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (2, 21);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (1, 20);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 1);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 3);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 4);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 5);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 6);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 7);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 8);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 9);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 10);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 12);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 13);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 14);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 16);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 17);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 18);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 19);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (3, 21);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (4, 1);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (4, 3);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (4, 10);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (4, 12);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (4, 13);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (4, 14);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (4, 16);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (4, 17);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (4, 21);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (1, 2);
INSERT INTO public.group_permissions (group_id, permission_id) VALUES (1, 1);


--
-- Data for Name: saving_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.saving_types (id, duration, interest_rate, is_active, type_name) VALUES (2, 3, 0.0500, true, 'Kỳ hạn ngắn');
INSERT INTO public.saving_types (id, duration, interest_rate, is_active, type_name) VALUES (3, 6, 0.0550, true, 'Kỳ hạn trung bình');
INSERT INTO public.saving_types (id, duration, interest_rate, is_active, type_name) VALUES (1, 0, 0.0050, true, 'Không kỳ hạn');


--
-- Data for Name: monthly_reports; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (1, 8000000.00, '2025-01-01', 0.00, 8000000.00, 2);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (2, 5000000.00, '2025-01-01', 0.00, 5000000.00, 3);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (3, 3000000.00, '2025-01-01', 0.00, 3000000.00, 1);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (4, 1000000.00, '2025-02-01', 0.00, 1000000.00, 2);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (5, 2000000.00, '2025-02-01', 0.00, 2000000.00, 3);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (6, 1000000.00, '2025-02-01', 0.00, 1000000.00, 1);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (7, 15000000.00, '2025-03-01', 0.00, 15000000.00, 2);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (8, 0.00, '2025-03-01', 0.00, 0.00, 3);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (9, 0.00, '2025-03-01', 0.00, 0.00, 1);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (10, -7400000.00, '2025-04-01', 8400000.00, 1000000.00, 2);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (11, 0.00, '2025-04-01', 0.00, 0.00, 3);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (12, 0.00, '2025-04-01', 0.00, 0.00, 1);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (13, -1050000.00, '2025-05-01', 1050000.00, 0.00, 2);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (14, 0.00, '2025-05-01', 0.00, 0.00, 3);
INSERT INTO public.monthly_reports (id, difference, report_month, total_expense, total_income, saving_type_id) VALUES (15, 1000000.00, '2025-05-01', 0.00, 1000000.00, 1);


--
-- Data for Name: parameters; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.parameters (id, max_transaction_amount, min_age, min_saving_amount, min_transaction_amount) VALUES (1, 1000000000.00, 18, 1000000.00, 10000.00);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

-- INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (1, NULL, 0.00, '000000000000', '2025-06-21 07:27:39.339099', NULL, 'admin@gmail.com', 'Administrator', true, '$2a$10$Zf8f1frxwN3BxvB.AZtXZ.AFCppc3DzFgKeiVa1R6kBu3AREsWd5G', NULL, 1);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (5, '123 Đường ABC, Quận 1, TP.HCM', 500000.00, '123456789002', '2025-06-20 20:34:02.969578', '2000-05-16', 'user1@gmail.com', 'Lê Thanh Liêm', true, '$2a$10$3Zg4NvgFGwHpXSmWkXC1Wu547jkf6JOFLCSGhS.FiOa.teepCHuOa', '0909123456', 2);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (9, '05 Đường Thống Nhất, Dĩ An, Bình Dương', 9450000.00, '123456789673', '2025-06-21 14:07:46.695313', '2000-04-11', 'user6@hosliy.com', 'Ma Văn Đức', true, '$2a$10$ja6SzekbbjNWpXyG3Xf4mevkYSVOknnRPOyM8Usk76EI3AnptNQii', '09012337033', 2);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (4, '123456789', 6000000.00, '0123456789', '2025-06-20 15:27:50.989613', '2000-06-14', 'pewav80791@ihnpo.com', 'quach vinh co', true, '$2a$10$yrcwcyxEMTrr67wEwpGQ1eTF2mldBLWc/aCPr9zNaYXZLQaL1/2qO', '0123456789', 2);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (6, '50 Đường Thống Nhất, Dĩ An, Bình Dương', 9250000.00, '123456789223', '2025-03-02 08:04:59.507624', '1995-09-12', 'pitemes508@hosliy.com', 'Trẫn Hữu', true, '$2a$10$vyoQVqKWzg0ZmlWQWiXyBOsOWND7Y..6ysYYD7503MBo4NuyBFeC.', '0901239788', 2);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (2, '29 Đường Thống Nhất, Dĩ An, Bình Dương', 0.00, '123456789835', '2025-06-21 08:00:36.118893', '1990-04-12', 'manager@gmail.com', 'Nguyễn Thành Công', true, '$2a$10$xBWsr5FmzcT8J1kodylwweVLxcR5s/7xOu6fJLxBLzgD8BKaCXL.W', '0903579912', 3);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (12, '25 Đường Thống Nhất, Dĩ An, Bình Dương', 0.00, '123456784536', '2025-06-21 14:13:31.466248', '1995-06-02', 'user9@hosliy.com', 'Lê Văn Luyện', true, '$2a$10$PiG9gu3aE4ZbR4XZEa98wuT8X/BPHN8via589Pmra2Nz68N8.LrRe', '0901235382', 4);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (10, '10 Đường Thống Nhất, Dĩ An, Bình Dương', 0.00, '123456788773', '2025-06-21 14:08:25.535272', '2001-06-19', 'user7@hosliy.com', 'Mai Hoàng Long', true, '$2a$10$PaPAmdEGvkf3X01BapO8eOXN0OIQfzhNZNv3Lwj8bU51RgJwwtwGS', '09012337027', 2);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (3, '123 Đường ABC, Quận 1, TP.HCM', 0.00, '123456789012', '2025-06-20 15:27:50.989613', '2000-04-16', 'staff@gmail.com', 'Nguyễn Văn A', true, '$2a$10$xBWsr5FmzcT8J1kodylwweVLxcR5s/7xOu6fJLxBLzgD8BKaCXL.W', '0909123456', 4);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (7, '01 Đường Thống Nhất, Dĩ An, Bình Dương', 0.00, '123456789113', '2025-06-21 14:05:19.921537', '2000-09-12', 'user4@gmail.com', 'Trẫn Hộ Pháp', true, '$2a$10$JDf2I1/mKZ3FzVfuTwekdu0cqqY8WyiXX3x4qnAbPLw6sDNZRvqEe', '0901239778', 2);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (11, '19 Đường Thống Nhất, Dĩ An, Bình Dương', 0.00, '123456788223', '2025-06-21 14:12:01.026499', '1995-06-02', 'user8@hosliy.com', 'Nguyễn Hải Dương', true, '$2a$10$7xUwTO9jddtlhT3KUFNvR.siG8vmb4DTA3BphA1/n6ZwHZ22VsbPS', '0901237097', 2);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (8, '03 Đường Thống Nhất, Dĩ An, Bình Dương', 0.00, '123456789193', '2025-06-21 14:07:03.988871', '2000-09-12', 'user5@hosliy.com', 'Hữu Duy', true, '$2a$10$aAueGJer.iXPt1fbxCSz/ehy690qoRQo6fObw4RICby7Y5Q0PTJ16', '0901233704', 2);
INSERT INTO public.users (id, address, balance, citizenid, created_at, date_of_birth, email, full_name, is_active, password, phone, group_id) VALUES (13, '30 Đường Thống Nhất, Dĩ An, Bình Dương', 0.00, '123456780384', '2025-06-21 14:16:11.258197', '1995-07-04', 'user10@hosliy.com', 'Phạm Hữu Gia Long', true, '$2a$10$woS2gtq3O5SdyDPAeiKYmeztqpqqa5tmCVH4zl7PglXnfyk8QbQ1G', '0901236382', 3);


--
-- Data for Name: saving_tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (1, 1000000.00, 500000.00, '2025-06-20 16:26:40.300766', 3, 0.0500, true, '2025-09-20', 2, 4);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (2, 1000000.00, 1000000.00, '2025-06-20 20:55:19.761765', 0, 0.0050, true, NULL, 1, 4);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (4, 2000000.00, 2000000.00, '2025-06-10 08:33:53.888009', 6, 0.0550, true, '2025-12-10', 3, 6);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (3, 5000000.00, 0.00, '2025-03-20 08:32:08.863931', 3, 0.0500, false, '2025-06-20', 2, 6);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (5, 1000000.00, 500000.00, '2025-06-21 20:18:45.510725', 0, 0.0040, true, NULL, 1, 4);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (6, 10000000.00, 10000000.00, '2025-03-21 08:19:04.086843', 3, 0.0500, true, '2025-06-21', 2, 7);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (7, 1000000.00, 1000000.00, '2025-04-22 08:26:32.297266', 3, 0.0500, true, '2025-07-22', 2, 8);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (8, 1000000.00, 1000000.00, '2025-05-22 08:28:20.735226', 0, 0.0050, true, NULL, 1, 8);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (9, 3000000.00, 3000000.00, '2025-01-22 10:24:24.318421', 0, 0.0050, true, NULL, 1, 9);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (11, 5000000.00, 5000000.00, '2025-01-22 10:27:06.035722', 6, 0.0550, true, '2025-07-22', 3, 9);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (12, 1000000.00, 1000000.00, '2025-02-22 10:28:02.287488', 0, 0.0050, true, NULL, 1, 9);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (14, 2000000.00, 2000000.00, '2025-02-22 10:29:55.667702', 6, 0.0550, true, '2025-08-22', 3, 9);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (10, 8000000.00, 0.00, '2025-01-22 10:25:23.021352', 3, 0.0500, false, '2025-04-22', 2, 9);
INSERT INTO public.saving_tickets (id, amount, balance, created_at, duration, interest_rate, is_active, maturity_date, saving_type_id, user_id) VALUES (13, 1000000.00, 0.00, '2025-02-22 10:28:55.951498', 3, 0.0500, false, '2025-05-22', 2, 9);


--
-- Data for Name: transaction_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (1, 2000000.00, '2025-06-20 16:26:06.498588', 2000000.00, 0, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (2, 1000000.00, '2025-06-20 16:26:40.311147', 1000000.00, 2, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (3, 500000.00, '2025-06-20 16:47:44.851531', 1500000.00, 3, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (4, 500000.00, '2025-06-20 20:40:11.052172', 500000.00, 0, 5);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (5, 1000000.00, '2025-06-20 20:55:19.773972', 500000.00, 2, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (6, 2000000.00, '2025-06-20 21:01:31.621804', 2500000.00, 0, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (7, 500000.00, '2025-06-20 22:15:19.884284', 2000000.00, 1, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (8, 10000000.00, '2025-03-02 08:07:24.459419', 10000000.00, 0, 6);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (9, 5000000.00, '2025-03-20 08:32:08.91265', 5000000.00, 2, 6);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (10, 2000000.00, '2025-06-10 08:33:53.89001', 3000000.00, 2, 6);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (11, 5250000.00, '2025-06-21 16:31:14.639424', 8250000.00, 3, 6);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (12, 5000000.00, '2025-06-21 20:17:53.650906', 7000000.00, 0, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (13, 1000000.00, '2025-06-21 20:18:45.518572', 6000000.00, 2, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (14, 500000.00, '2025-06-21 20:19:41.210193', 6500000.00, 3, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (15, 500000.00, '2025-06-21 20:25:53.452062', 6000000.00, 1, 4);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (16, 1000000.00, '2025-06-21 20:38:46.832347', 9250000.00, 0, 6);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (17, 10000000.00, '2025-03-21 08:17:49.038801', 10000000.00, 0, 7);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (18, 10000000.00, '2025-03-21 08:19:04.101845', 0.00, 2, 7);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (19, 2000000.00, '2025-04-22 08:25:19.802383', 2000000.00, 0, 8);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (20, 1000000.00, '2025-04-22 08:26:32.299272', 1000000.00, 2, 8);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (21, 1000000.00, '2025-05-22 08:28:20.735226', 0.00, 2, 8);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (22, 20000000.00, '2025-01-22 10:23:04.280321', 20000000.00, 0, 9);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (23, 3000000.00, '2025-01-22 10:24:24.32852', 17000000.00, 2, 9);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (24, 8000000.00, '2025-01-22 10:25:23.025463', 9000000.00, 2, 9);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (25, 5000000.00, '2025-01-22 10:27:06.037868', 4000000.00, 2, 9);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (26, 1000000.00, '2025-02-22 10:28:02.289488', 3000000.00, 2, 9);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (27, 1000000.00, '2025-02-22 10:28:55.953517', 2000000.00, 2, 9);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (28, 2000000.00, '2025-02-22 10:29:55.6704', 0.00, 2, 9);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (29, 8400000.00, '2025-04-22 10:31:10.27655', 8400000.00, 3, 9);
INSERT INTO public.transaction_histories (id, amount, created_at, remaining_balance, transaction_type, user_id) VALUES (30, 1050000.00, '2025-05-22 10:32:15.073374', 9450000.00, 3, 9);


--
-- Data for Name: withdrawal_tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.withdrawal_tickets (id, actual_amount, created_at, withdrawal_amount, saving_ticket_id) VALUES (1, 500000.00, '2025-06-20 16:47:44.845787', 500000.00, 1);
INSERT INTO public.withdrawal_tickets (id, actual_amount, created_at, withdrawal_amount, saving_ticket_id) VALUES (5, 5250000.00, '2025-06-21 16:31:14.636465', 5000000.00, 3);
INSERT INTO public.withdrawal_tickets (id, actual_amount, created_at, withdrawal_amount, saving_ticket_id) VALUES (6, 500000.00, '2025-06-21 20:19:41.204733', 500000.00, 5);
INSERT INTO public.withdrawal_tickets (id, actual_amount, created_at, withdrawal_amount, saving_ticket_id) VALUES (7, 8400000.00, '2025-04-22 10:31:10.271036', 8000000.00, 10);
INSERT INTO public.withdrawal_tickets (id, actual_amount, created_at, withdrawal_amount, saving_ticket_id) VALUES (8, 1050000.00, '2025-05-22 10:32:15.07036', 1000000.00, 13);


--
-- Name: groups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT setval('public.groups_id_seq', (
            SELECT MAX(id)
            FROM groups
        ), true);


--
-- Name: monthly_reports_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT setval('public.monthly_reports_id_seq', (
            SELECT MAX(id)
            FROM monthly_reports
        ), true);


--
-- Name: permissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT setval('public.permissions_id_seq', (
            SELECT MAX(id)
            FROM permissions
        ));


--
-- Name: saving_tickets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT setval('public.saving_tickets_id_seq', (
            SELECT MAX(id)
            FROM saving_tickets
        ));


--
-- Name: saving_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT setval('public.saving_types_id_seq', (
            SELECT MAX(id)
            FROM saving_types
        ));


--
-- Name: transaction_histories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT setval('public.transaction_histories_id_seq', (
            SELECT MAX(id)
            FROM transaction_histories
        ));


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT setval('public.users_id_seq', (
            SELECT MAX(id)
            FROM users
        ));


--
-- Name: withdrawal_tickets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT setval('public.withdrawal_tickets_id_seq', (
            SELECT MAX(id)
            FROM withdrawal_tickets
        ));


--
-- PostgreSQL database dump complete
--

