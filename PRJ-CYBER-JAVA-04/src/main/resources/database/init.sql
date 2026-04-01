-- PRJ-CYBER-JAVA-04: Cyber Gaming & F&B Management System
-- Database Initialization Script

-- Tạo Database
create database if not exists cyber_gaming_db
    character set utf8mb4
    collate utf8mb4_unicode_ci;

use cyber_gaming_db;

-- 1. Bảng categories - Khu vực phòng máy

create table if not exists categories (
    id int auto_increment primary key,
    name varchar(50) not null unique,
    description varchar(255)
) ;

-- 2. Bảng users - Người dùng

create table if not exists users (
    id int auto_increment primary key,
    username varchar(50) not null unique,
    password varchar(255) not null,
    full_name varchar(100) not null,
    phone varchar(15),
    balance decimal(12,2) default 0.00,
    role enum('ADMIN', 'STAFF', 'CUSTOMER') not null default 'CUSTOMER',
    created_at datetime default current_timestamp
) ;

-- 3. Bảng pcs - Máy trạm

create table if not exists pcs (
    id int auto_increment primary key,
    pc_number varchar(10) not null unique,
    category_id int not null,
    config_info varchar(255),
    price_per_hour decimal(10,2) not null,
    status enum('AVAILABLE', 'IN_USE', 'MAINTENANCE') not null default 'AVAILABLE',
    foreign key (category_id) references categories(id)
) ;

-- 4. Bảng foods - Menu F&B

create table if not exists foods (
    id int auto_increment primary key,
    name varchar(100) not null,
    description varchar(255),
    price decimal(10,2) not null,
    stock_quantity int not null default 0,
    status enum('AVAILABLE', 'UNAVAILABLE') not null default 'AVAILABLE'
) ;

-- 5. Bảng bookings - Đặt máy

create table if not exists bookings (
    id int auto_increment primary key,
    user_id int not null,
    pc_id int not null,
    start_time datetime not null,
    end_time datetime not null,
    total_price decimal(12,2) not null default 0.00,
    status enum('PENDING', 'CONFIRMED', 'IN_USE', 'COMPLETED', 'CANCELLED') not null default 'PENDING',
    created_at datetime default current_timestamp,
    foreign key (user_id) references users(id),
    foreign key (pc_id) references pcs(id)
) ;

-- 6. Bảng orders - Đơn hàng F&B

create table if not exists orders (
    id int auto_increment primary key,
    user_id int not null,
    booking_id int default null,
    total_price decimal(12,2) not null default 0.00,
    status enum('PENDING', 'PREPARING', 'SERVED', 'COMPLETED', 'CANCELLED') not null default 'PENDING',
    created_at datetime default current_timestamp,
    foreign key (user_id) references users(id),
    foreign key (booking_id) references bookings(id)
) ;

-- 7. Bảng order_details - Chi tiết đơn hàng

create table if not exists order_details (
    id int auto_increment primary key,
    order_id int not null,
    food_id int not null,
    quantity int not null,
    unit_price decimal(10,2) not null,
    subtotal decimal(12,2) not null,
    foreign key (order_id) references orders(id),
    foreign key (food_id) references foods(id)
) ;

-- 8. Bảng transactions - Lịch sử giao dịch (Ví điện tử)

create table if not exists transactions (
    id int auto_increment primary key,
    user_id int not null,
    type enum('DEPOSIT', 'BOOKING_PAYMENT', 'ORDER_PAYMENT', 'REFUND') not null,
    amount decimal(12,2) not null,
    balance_after decimal(12,2) not null,
    description varchar(255),
    created_at datetime default current_timestamp,
    foreign key (user_id) references users(id)
) ;


-- DỮ LIỆU MẪU

-- Khu vực phòng máy
insert into categories (name, description) values
('Standard', 'Khu vực máy tiêu chuẩn, phù hợp cho game online cơ bản'),
('VIP', 'Khu vực VIP, máy cấu hình cao, ghế gaming cao cấp'),
('Stream Room', 'Phòng riêng dành cho streamer, cách âm, có webcam & mic');

-- Admin mặc định (password: admin123 -> SHA-256 hash)
insert into users (username, password, full_name, phone, balance, role) values
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Quản trị viên', '0901234567', 0.00, 'ADMIN');

-- Staff mẫu (password: staff123 -> SHA-256 hash)
insert into users (username, password, full_name, phone, balance, role) values
('staff01', 'da90c57684e3e8d3cc48a8a097dab3c44e92cb0c5c238a3c4f27a27d2a79a8b1', 'Nguyễn Văn A', '0912345678', 0.00, 'STAFF');

-- Customer mẫu (password: cust123 -> SHA-256 hash)
insert into users (username, password, full_name, phone, balance, role) values
('customer01', '5faf1adaca39984bd1a5ddbeb6cebbb3a6a1ab1f8e4dd96a9011c90d0dd4045c', 'Trần Thị B', '0923456789', 500000.00, 'CUSTOMER');

-- Máy trạm mẫu
insert into pcs (pc_number, category_id, config_info, price_per_hour, status) values
('PC-01', 1, 'i5-12400F, 16GB RAM, GTX 1660 Super', 15000.00, 'AVAILABLE'),
('PC-02', 1, 'i5-12400F, 16GB RAM, GTX 1660 Super', 15000.00, 'AVAILABLE'),
('PC-03', 1, 'i5-12400F, 16GB RAM, RTX 3060', 18000.00, 'AVAILABLE'),
('PC-04', 2, 'i7-13700K, 32GB RAM, RTX 4070', 30000.00, 'AVAILABLE'),
('PC-05', 2, 'i7-13700K, 32GB RAM, RTX 4070', 30000.00, 'AVAILABLE'),
('PC-06', 2, 'i9-13900K, 32GB RAM, RTX 4080', 40000.00, 'MAINTENANCE'),
('PC-07', 3, 'i9-13900K, 64GB RAM, RTX 4090, Elgato Stream Deck', 60000.00, 'AVAILABLE'),
('PC-08', 3, 'i9-13900K, 64GB RAM, RTX 4090, Webcam 4K', 60000.00, 'AVAILABLE');

-- Menu F&B mẫu
insert into foods (name, description, price, stock_quantity, status) values
('Mì tôm trứng', 'Mì tôm Hảo Hảo xào trứng ốp la', 25000.00, 50, 'AVAILABLE'),
('Mì tôm xúc xích', 'Mì tôm với xúc xích Đức', 30000.00, 40, 'AVAILABLE'),
('Cơm chiên dương châu', 'Cơm chiên với trứng, lạp xưởng, đậu', 35000.00, 30, 'AVAILABLE'),
('Bánh mì thịt', 'Bánh mì kẹp thịt nướng, rau sống', 20000.00, 25, 'AVAILABLE'),
('Sting dâu', 'Nước tăng lực Sting vị dâu 330ml', 12000.00, 100, 'AVAILABLE'),
('Coca Cola', 'Coca Cola lon 330ml', 12000.00, 100, 'AVAILABLE'),
('Trà sữa trân châu', 'Trà sữa Gong Cha size M', 35000.00, 20, 'AVAILABLE'),
('Nước suối', 'Nước suối Aquafina 500ml', 8000.00, 200, 'AVAILABLE'),
('Bò khô', 'Bò khô miếng 50g', 15000.00, 60, 'AVAILABLE'),
('Snack Oishi', 'Bim bim Oishi tôm cay 40g', 10000.00, 80, 'AVAILABLE');