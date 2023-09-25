-- --------------------------------------------------------
-- Máy chủ:                      127.0.0.1
-- Server version:               10.4.25-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Phiên bản:           12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table shopplaza.cart_items
CREATE TABLE IF NOT EXISTS `cart_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `cart_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.cart_items: ~2 rows (approximately)
INSERT INTO `cart_items` (`id`, `user_id`, `product_id`, `quantity`, `created_at`) VALUES
	(62, 19, 60, 5, '2023-08-25 09:25:08.000000'),
	(68, 19, 56, 1, '2023-08-25 23:31:36.000000');

-- Dumping structure for table shopplaza.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.category: ~5 rows (approximately)
INSERT INTO `category` (`id`, `name`, `image`) VALUES
	(1, 'Điện thoại', 'dienthoai.png'),
	(2, 'Laptop', 'laptop.png'),
	(3, 'Thời trang', 'thoitrang.png'),
	(4, 'Đồ gia dụng', 'dogiadung.png'),
	(5, 'Đồ chơi', 'dochoi.png');

-- Dumping structure for table shopplaza.message
CREATE TABLE IF NOT EXISTS `message` (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user1` int(11) NOT NULL DEFAULT 0,
  `user2` int(11) NOT NULL DEFAULT 0,
  `text` varchar(255) DEFAULT NULL,
  `time_chat` datetime(6) DEFAULT NULL,
  `violate` tinyint(1) DEFAULT 0,
  `status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`message_id`) USING BTREE,
  KEY `FK_message_users` (`user1`),
  KEY `FK_message_users_2` (`user2`),
  CONSTRAINT `FK_message_users` FOREIGN KEY (`user1`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_message_users_2` FOREIGN KEY (`user2`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.message: ~18 rows (approximately)
INSERT INTO `message` (`message_id`, `user1`, `user2`, `text`, `time_chat`, `violate`, `status`) VALUES
	(21, 18, 21, 'alo', '2023-09-01 08:54:44.000000', 0, b'1'),
	(22, 21, 18, '3das', '2023-09-01 08:57:20.000000', 0, b'1'),
	(23, 21, 18, 'fda', '2023-09-01 09:03:40.000000', 0, b'1'),
	(24, 18, 21, 'dưa', '2023-09-01 09:03:51.000000', 0, b'1'),
	(25, 18, 21, 'fcwa', '2023-09-01 09:04:03.000000', 0, b'1'),
	(26, 18, 21, 'css', '2023-09-01 09:04:06.000000', 0, b'1'),
	(27, 18, 21, 'ừdr', '2023-09-01 09:07:51.000000', 0, b'1'),
	(28, 18, 21, 'd', '2023-09-01 09:07:53.000000', 0, b'1'),
	(29, 21, 18, 'd', '2023-09-01 09:07:55.000000', 0, b'1'),
	(30, 18, 21, 'alo', '2023-09-01 09:09:18.000000', 0, b'1'),
	(31, 18, 21, 'g', '2023-09-01 09:11:19.000000', 0, b'1'),
	(32, 18, 21, 's', '2023-09-01 09:11:23.000000', 0, b'1'),
	(33, 18, 21, 'cd', '2023-09-01 09:17:56.000000', 0, b'1'),
	(34, 16, 17, NULL, '2023-09-01 10:07:10.000000', 0, b'0'),
	(35, 18, 15, '4', '2023-09-01 10:07:40.000000', 0, b'1'),
	(36, 18, 21, 'd', '2023-09-01 10:40:23.000000', 0, b'1'),
	(37, 19, 1, 'fcff', '2023-09-01 11:23:53.000000', 0, b'1'),
	(38, 18, 19, 'con mẹ mày', '2023-09-01 22:26:08.000000', 0, b'1');

-- Dumping structure for table shopplaza.notify
CREATE TABLE IF NOT EXISTS `notify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `note` text NOT NULL,
  `read_status` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.notify: ~53 rows (approximately)
INSERT INTO `notify` (`id`, `title`, `user_id`, `content`, `note`, `read_status`, `created_at`) VALUES
	(17, 'Nạp tiền thành công', 16, 'Bạn đã nạp thành công 1111.0 Coins vào tài khoản.', 'cc', b'1', '2023-08-29 13:18:45.000000'),
	(18, 'Nạp tiền thành công', 16, 'Bạn đã nạp thành công 100.0 Coins vào tài khoản.', 'cc', b'1', '2023-08-29 14:33:02.000000'),
	(19, 'Mua VIP thành công!', 18, 'Chúc mừng test đã đạt VIP3', 'Bạn đã mua thành công gói [VIP3] vào lúc:2023-08-30 22:40:34.123 .Khi mua gói VIP bạn sẽ được hưởng các ưu đãi như ưu tiên hiển thị với những người dưới VIP3, và phí sàn trên mỗi sản phẩm sẽ được giảm 3.0% trên mỗi đơn hàng.Ưu đãi của bạn sẽ hết vào lúc: 2023-09-29 22:40:34.123 (30 Ngày).Sau khi hết hạnVIP,các ưu đãi của bạn cũng sẽ mất và trở về như ban đầu.', b'1', '2023-08-30 22:40:34.000000'),
	(20, 'Gửi Đơn Đăng Kí Thành Công', 18, ' Admin đã nhận được câu trả lời của bạn', 'Quản trị viên đã nhận được thông tin về bạn,vui lòng chờ cho tới khi được phê duyệt\nKết quả sẽ có sau 1-2 ngày làm việc', b'1', '2023-08-30 22:41:41.000000'),
	(21, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZA 0PH8N', 'Vui lòng nhận hàng bạn đã đặt', b'1', '2023-08-30 22:58:11.000000'),
	(22, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZA 0PH8N\nMã đơn hàng: SHOPPLAZA 8ECZ5', 'Vui lòng nhận hàng bạn đã đặt', b'1', '2023-08-30 22:58:11.000000'),
	(23, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZA TDQM6', 'Vui lòng nhận hàng bạn đã đặt', b'1', '2023-08-30 22:59:25.000000'),
	(24, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZANFDY6', 'Vui lòng nhận hàng bạn đã đặt', b'1', '2023-08-30 23:06:25.000000'),
	(25, 'Thông Báo Có Đơn Hàng Mới', 16, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZANFDY6', 'Người Mua test đã đặt đơn hàng SHOPPLAZANFDY6 từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-30 23:06:25.000000'),
	(26, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZANFDY6\nMã đơn hàng: SHOPPLAZA8BL2U', 'Vui lòng nhận hàng bạn đã đặt', b'1', '2023-08-30 23:06:25.000000'),
	(27, 'Thông Báo Có Đơn Hàng Mới', 19, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZANFDY6\nMã đơn hàng: SHOPPLAZA8BL2U', 'Người Mua test đã đặt đơn hàng SHOPPLAZA8BL2U từ bạn,vui lòng xử lí đơn hàng này!', b'1', '2023-08-30 23:06:25.000000'),
	(28, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAUTNKU', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 07:28:25.000000'),
	(29, 'Thông Báo Có Đơn Hàng Mới', 19, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAUTNKU từ bạn,vui lòng xử lí đơn hàng này!', b'1', '2023-08-31 07:28:25.000000'),
	(30, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZA93AAP', 'Vui lòng nhận hàng bạn đã đặt', b'1', '2023-08-31 07:30:15.000000'),
	(31, 'Thông Báo Có Đơn Hàng Mới', 19, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZA93AAP từ bạn,vui lòng xử lí đơn hàng này!', b'1', '2023-08-31 07:30:15.000000'),
	(32, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAW507A', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 07:41:40.000000'),
	(33, 'Thông Báo Có Đơn Hàng Mới', 19, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAW507A từ bạn,vui lòng xử lí đơn hàng này!', b'1', '2023-08-31 07:41:40.000000'),
	(34, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZA3C1U3', 'Vui lòng nhận hàng bạn đã đặt', b'1', '2023-08-31 07:44:28.000000'),
	(35, 'Thông Báo Có Đơn Hàng Mới', 15, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZA3C1U3 từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 07:44:28.000000'),
	(36, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAKH43U', 'Vui lòng nhận hàng bạn đã đặt', b'1', '2023-08-31 07:48:30.000000'),
	(37, 'Thông Báo Có Đơn Hàng Mới', 15, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAKH43U từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 07:48:30.000000'),
	(38, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZABULA7', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 10:18:48.000000'),
	(39, 'Thông Báo Có Đơn Hàng Mới', 15, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZABULA7 từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 10:18:48.000000'),
	(40, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZA13UQC', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 13:31:04.000000'),
	(41, 'Thông Báo Có Đơn Hàng Mới', 15, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZA13UQC từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 13:31:04.000000'),
	(42, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAPBESN', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 22:43:55.000000'),
	(43, 'Thông Báo Có Đơn Hàng Mới', 15, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAPBESN từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 22:43:55.000000'),
	(44, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAY1SQU', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 22:45:57.000000'),
	(45, 'Thông Báo Có Đơn Hàng Mới', 20, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAY1SQU từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 22:45:57.000000'),
	(46, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAY1SQU\nMã đơn hàng: SHOPPLAZAGYR8I', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 22:45:57.000000'),
	(47, 'Thông Báo Có Đơn Hàng Mới', 15, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAGYR8I từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 22:45:57.000000'),
	(48, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAPACQL', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 23:00:01.000000'),
	(49, 'Thông Báo Có Đơn Hàng Mới', 20, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAPACQL từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 23:00:01.000000'),
	(50, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAI73NQ', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 23:34:14.000000'),
	(51, 'Thông Báo Có Đơn Hàng Mới', 21, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAI73NQ từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 23:34:14.000000'),
	(52, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZA9V1VV', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 23:53:42.000000'),
	(53, 'Thông Báo Có Đơn Hàng Mới', 21, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZA9V1VV từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 23:53:42.000000'),
	(54, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZA9V1VV\nMã đơn hàng: SHOPPLAZAXHI48', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-08-31 23:53:42.000000'),
	(55, 'Thông Báo Có Đơn Hàng Mới', 20, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAXHI48 từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-08-31 23:53:42.000000'),
	(56, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAQ8TU1', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-09-01 00:02:32.000000'),
	(57, 'Thông Báo Có Đơn Hàng Mới', 21, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZAQ8TU1 từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-09-01 00:02:32.000000'),
	(58, 'Thông báo đặt hàng Thành Công', 18, 'Bạn đã đặt các đơn hàng sau:\nMã đơn hàng: SHOPPLAZAQ8TU1\nMã đơn hàng: SHOPPLAZALYTSR', 'Vui lòng nhận hàng bạn đã đặt', b'0', '2023-09-01 00:02:33.000000'),
	(59, 'Thông Báo Có Đơn Hàng Mới', 20, 'Vui lòng chuẩn bị đơn hàng!', 'Người Mua test đã đặt đơn hàng SHOPPLAZALYTSR từ bạn,vui lòng xử lí đơn hàng này!', b'0', '2023-09-01 00:02:33.000000'),
	(60, 'Đơn hàng số 59 có mã tracking là SHOPPLAZAY1SQU đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 8.55 Coints (đã trừ chiết khấu 5.0%)', b'1', '2023-09-02 13:52:30.000000'),
	(61, 'Đơn hàng số 61 có mã tracking là SHOPPLAZAPACQL đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 380.0 Coints (đã trừ chiết khấu 5.0%)', b'1', '2023-09-02 13:54:02.000000'),
	(62, 'Đơn hàng số 63 có mã tracking là SHOPPLAZA9V1VV đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 448.63 Coints (đã trừ chiết khấu 9.0%)', b'1', '2023-09-02 14:02:49.000000'),
	(63, 'Đơn hàng số 64 có mã tracking là SHOPPLAZAXHI48 đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 364.0 Coints (đã trừ chiết khấu 9.0%)', b'1', '2023-09-02 14:04:54.000000'),
	(64, 'Đơn hàng số 66 có mã tracking là SHOPPLAZALYTSR đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 364.0 Coints (đã trừ chiết khấu 9.0%)', b'0', '2023-09-02 14:06:59.000000'),
	(65, 'Đơn hàng số 65 có mã tracking là SHOPPLAZAQ8TU1 đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 246.28 Coints (đã trừ chiết khấu 6.0%)', b'0', '2023-09-02 14:11:19.000000'),
	(66, 'Đơn hàng số 62 có mã tracking là SHOPPLAZAI73NQ đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 29.14 Coints (đã trừ chiết khấu 6.0%)', b'0', '2023-09-02 14:12:18.000000'),
	(67, 'Đơn hàng số 59 có mã tracking là SHOPPLAZAY1SQU đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 8.46 Coints (đã trừ chiết khấu 6.0%)', b'0', '2023-09-02 14:19:48.000000'),
	(68, 'Đơn hàng số 60 có mã tracking là SHOPPLAZAGYR8I đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 7.52 Coints (đã trừ chiết khấu 6.0%)', b'0', '2023-09-02 14:24:06.000000'),
	(69, 'Đơn hàng số 61 có mã tracking là SHOPPLAZAPACQL đã được người dùng xác nhận thành công', 19, 'Thông Báo hoàn thành đơn hàng', 'Đơn hàng của bạn đã được xác nhận thành công, bạn nhận được 376.0 Coints (đã trừ chiết khấu 6.0%)', b'0', '2023-09-02 14:24:27.000000');

-- Dumping structure for table shopplaza.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `seller_id` int(11) NOT NULL,
  `tracking_code` varchar(255) DEFAULT NULL,
  `order_date` datetime(6) DEFAULT NULL,
  `order_time_update` datetime(6) DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FK_orders_users` (`user_id`),
  KEY `FK_orders_users_2` (`seller_id`),
  CONSTRAINT `FK_orders_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_orders_users_2` FOREIGN KEY (`seller_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.orders: ~8 rows (approximately)
INSERT INTO `orders` (`order_id`, `user_id`, `seller_id`, `tracking_code`, `order_date`, `order_time_update`, `total_amount`, `status`) VALUES
	(59, 18, 19, 'SHOPPLAZAY1SQU', '2023-08-31 22:45:57.000000', '2023-09-02 14:19:48.000000', 9, 'SUCCESS'),
	(60, 18, 19, 'SHOPPLAZAGYR8I', '2023-08-31 22:45:57.000000', '2023-09-02 14:24:06.000000', 8, 'SUCCESS'),
	(61, 18, 19, 'SHOPPLAZAPACQL', '2023-08-31 23:00:01.000000', '2023-09-02 14:24:27.000000', 400, 'SUCCESS'),
	(62, 18, 19, 'SHOPPLAZAI73NQ', '2023-08-31 23:34:14.000000', '2023-09-02 14:12:18.000000', 31, 'PENDING'),
	(63, 18, 19, 'SHOPPLAZA9V1VV', '2023-08-31 23:53:42.000000', '2023-09-02 14:02:49.000000', 493, 'PENDING'),
	(64, 18, 19, 'SHOPPLAZAXHI48', '2023-08-31 23:53:42.000000', '2023-09-02 14:04:54.000000', 400, 'PENDING'),
	(65, 18, 19, 'SHOPPLAZAQ8TU1', '2023-09-01 00:02:32.000000', '2023-09-02 14:03:39.000000', 262, 'PENDING'),
	(66, 18, 19, 'SHOPPLAZALYTSR', '2023-09-01 00:02:32.000000', '2023-09-02 14:06:59.000000', 400, 'PENDING');

-- Dumping structure for table shopplaza.order_details
CREATE TABLE IF NOT EXISTS `order_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `total_amount` double DEFAULT NULL,
  `amount_received` double DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `percent` double DEFAULT NULL,
  `full_name_order` varchar(255) DEFAULT NULL,
  `phone_order` varchar(255) DEFAULT NULL,
  `address_order` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_order_details_orders` (`order_id`) USING BTREE,
  CONSTRAINT `FK_order_details_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.order_details: ~8 rows (approximately)
INSERT INTO `order_details` (`id`, `order_id`, `total_amount`, `amount_received`, `payment_method`, `percent`, `full_name_order`, `phone_order`, `address_order`) VALUES
	(19, 59, 9, 8.64, 'COINTS', 4, NULL, NULL, NULL),
	(20, 60, 8, 7.4399999999999995, 'COINTS', 7, NULL, NULL, NULL),
	(21, 61, 400, 384, 'COD', 4, NULL, NULL, NULL),
	(22, 62, 31, 30.07, 'COINTS', 3, 'Trần Phát Đạt', '0862221686', 'T1-D7'),
	(23, 63, 493, 478.21, 'COD', 3, 'Trần Phát Đạt', '0862221686', 'T1-D7'),
	(24, 64, 400, 384, 'COD', 4, 'Trần Phát Đạt', '0862221686', 'T1-D7'),
	(25, 65, 262, 254.14, 'COD', 3, 'Trần Phát Đạt', '0862221686', 'T1-D7'),
	(26, 66, 400, 384, 'COD', 4, 'Trần Phát Đạt', '0862221686', 'T1-D7');

-- Dumping structure for table shopplaza.order_items
CREATE TABLE IF NOT EXISTS `order_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity_order` int(11) NOT NULL,
  `total_quantity_product` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_order_items_orders` (`order_id`) USING BTREE,
  KEY `FK_order_items_products` (`product_id`),
  CONSTRAINT `FK_order_items_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_order_items_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.order_items: ~15 rows (approximately)
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `quantity_order`, `total_quantity_product`) VALUES
	(53, 59, 57, 9, 9),
	(54, 60, 56, 8, 8),
	(55, 61, 49, 1, 200),
	(56, 61, 50, 1, 200),
	(57, 62, 58, 1, 31),
	(58, 63, 60, 2, 62),
	(59, 63, 45, 2, 400),
	(60, 63, 58, 1, 31),
	(61, 64, 50, 1, 200),
	(62, 64, 49, 1, 200),
	(63, 65, 58, 1, 31),
	(64, 65, 45, 1, 200),
	(65, 65, 60, 1, 31),
	(66, 66, 50, 1, 200),
	(67, 66, 49, 1, 200);

-- Dumping structure for table shopplaza.payment_method
CREATE TABLE IF NOT EXISTS `payment_method` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.payment_method: ~0 rows (approximately)

-- Dumping structure for table shopplaza.products
CREATE TABLE IF NOT EXISTS `products` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `seller_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `images` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `sold` int(11) NOT NULL DEFAULT 0,
  `quantity_stock` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `category_id` int(11) NOT NULL DEFAULT 0,
  `hide` bit(1) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `fk_products_category` (`category_id`),
  CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.products: ~17 rows (approximately)
INSERT INTO `products` (`product_id`, `seller_id`, `name`, `images`, `description`, `price`, `sold`, `quantity_stock`, `created_at`, `updated_at`, `category_id`, `hide`) VALUES
	(38, 21, 'Áo Cà Sa', 'ce6c5d9b-aa0c-4041-b3b2-2b81a521c895.png', '1', 200, 0, 10, '2023-08-19 07:35:06.000000', '2023-08-31 22:45:06.000000', 3, b'1'),
	(45, 21, 'Áo Võ', 'ea6231f0-11ec-40d4-9e61-8f8273806a24.png', '1', 200, 6, 0, '2023-08-20 17:56:45.000000', '2023-08-31 22:45:06.000000', 3, b'0'),
	(47, 16, 'Laptop gaming', '6ae011a8-b2fd-4365-bea8-555c88c9f107.jpg', '1', 200, 0, 10, '2023-08-20 18:09:14.000000', '2023-08-25 07:30:17.000000', 2, b'1'),
	(49, 20, 'Áo Du Lịch', '48b66a70-aace-43ef-968a-1084bbfea8d1.png', '1', 200, 1, 6, '2023-08-20 11:56:58.000000', '2023-08-31 22:44:58.000000', 3, b'0'),
	(50, 20, 'Quần Bơi', '3263e66e-4420-41da-a997-1e6b2244c70e.png', '1', 200, 0, 6, '2023-08-20 11:57:16.000000', '2023-08-31 22:44:58.000000', 3, b'0'),
	(51, 16, 'Laptop Z', 'a91a1700-6e85-4db8-814a-ed1cbb831e7c.jpg', '1', 200, 0, 10, '2023-08-20 13:31:00.000000', '2023-08-24 23:13:38.000000', 2, b'1'),
	(52, 14, 'iphone 8', '4d690055-f16f-479f-913c-c18a07d99ec5.jpg', '1', 1, 0, 10, '2023-08-20 18:09:42.000000', '2023-08-24 23:13:30.000000', 1, b'1'),
	(53, 15, 'iphone 89', '341d8c48-113c-40e6-90a8-174fc8e6e31b.jpg', '1', 200, 0, 10, '2023-08-20 17:55:19.000000', '2023-08-24 23:13:32.000000', 1, b'1'),
	(54, 15, 'Laptop S', '68f520e5-ff84-4444-932d-9124fea1db7a.png', '', 200, 0, 10, '2023-08-20 17:38:13.000000', '2023-08-31 10:03:10.000000', 2, b'1'),
	(55, 16, 'iphone 9', '87e4cbad-c2ee-4c15-b566-bd812aa478e5.jpg', '17', 11, 0, 10, '2023-08-20 18:21:06.000000', '2023-08-24 23:13:34.000000', 1, b'1'),
	(56, 15, 'Quần Lội', 'e852f7dd-bc40-4ec7-9c19-6b3f26e2539d.png', 'NFofs', 1, 1, 0, '2023-08-20 19:42:21.000000', '2023-08-31 10:03:16.000000', 3, b'0'),
	(57, 20, 'Con Dao 9 lưỡi', 'be2ecf21-75ea-40da-9510-3af92cef296f.jpg', 'conmm', 1, 10, 0, '2023-08-22 09:43:07.000000', '2023-08-31 22:44:59.000000', 4, b'0'),
	(58, 21, 'Dao 2 lưỡi', '95f8e272-6817-40c8-b289-ce41ba1c2dfe.png', 'fsfsfs', 31, 2, 4, '2023-08-22 09:43:39.000000', '2023-08-31 22:45:02.000000', 4, b'0'),
	(59, 20, 'Con quay beyblade', 'e65f4335-651e-4f9b-a237-bb3e8811cabc.png', 'vdzfn', 200, 0, 10, '2023-08-25 08:38:26.000000', '2023-08-31 22:45:00.000000', 5, b'1'),
	(60, 21, 'Con quay ', '66394d98-a69f-4886-84d5-3a023aa9916d.png', '31fvsd', 31, 8, 6, '2023-08-28 21:32:47.000000', '2023-08-31 22:45:04.000000', 5, b'0'),
	(61, 21, 'bún cá', 'f931b6eb-0dd5-4bd2-8d81-bb419a347dbd.jpg', 'mhffg', 23, 0, 10, '2023-08-25 08:27:30.000000', '2023-08-31 22:45:03.000000', 5, b'1'),
	(64, 19, 'student1332616', '78cce108-60de-4329-8cdc-a689a7a5e5ed.png', 'fa', 0, 0, 1, '2023-09-02 10:02:21.000000', '2023-09-02 10:02:21.000000', 1, b'0');

-- Dumping structure for table shopplaza.register_seller
CREATE TABLE IF NOT EXISTS `register_seller` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `registration_date` varchar(255) DEFAULT NULL,
  `update_time` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.register_seller: ~12 rows (approximately)
INSERT INTO `register_seller` (`id`, `user_id`, `registration_date`, `update_time`, `description`, `status`) VALUES
	(8, 17, '23-08-2023 15:25:37', '24-08-2023 17:42:26', 'cc', 'ACCEPT'),
	(9, 18, '23-08-2023 15:53:56', '23-08-2023 15:53:56', 's', 'DECLINE'),
	(10, 18, '23-08-2023 16:17:28', '23-08-2023 16:17:28', 'cc', 'DECLINE'),
	(11, 18, '23-08-2023 16:18:07', '23-08-2023 17:18:34', 'vvv', 'ACCEPT'),
	(12, 18, '23-08-2023 16:38:29', '23-08-2023 16:38:29', 'had\r\n', 'DECLINE'),
	(13, 17, '23-08-2023 16:40:15', '23-08-2023 16:40:15', 'k', 'DECLINE'),
	(14, 19, '23-08-2023 17:18:53', '24-08-2023 17:41:26', 'd', 'ACCEPT'),
	(15, 19, '25-08-2023 06:45:45', '25-08-2023 06:46:38', 'l', 'ACCEPT'),
	(19, 19, '28-08-2023 16:43:28', '28-08-2023 21:21:31', 'h', 'ACCEPT'),
	(20, 19, '28-08-2023 21:33:11', '28-08-2023 21:33:28', 'con', 'ACCEPT'),
	(21, 18, '28-08-2023 21:46:47', '28-08-2023 21:46:47', 'fcsgj', 'DECLINE'),
	(22, 18, '30-08-2023 22:41:41', '30-08-2023 22:41:41', 'b', 'PENDING');

-- Dumping structure for table shopplaza.reviews
CREATE TABLE IF NOT EXISTS `reviews` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `images_review` tinytext DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `comment` tinytext DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.reviews: ~2 rows (approximately)
INSERT INTO `reviews` (`id`, `images_review`, `order_id`, `product_id`, `user_id`, `rating`, `comment`, `created_at`) VALUES
	(30, 'b01e6e0d-1dae-4103-8725-a108a3acbd2d.png', 60, 56, 18, 4, 'đ', '2023-09-02 12:11:50.000000'),
	(31, 'b01e6e0d-1dae-4103-8725-a108a3acbd2d.png', 60, 56, 18, 5, 'b01e6e0d-1dae-4103-8725-a108a3acbd2d.png', '2023-09-02 12:55:06.000000');

-- Dumping structure for table shopplaza.transaction
CREATE TABLE IF NOT EXISTS `transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `transaction_time` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.transaction: ~0 rows (approximately)

-- Dumping structure for table shopplaza.transactions
CREATE TABLE IF NOT EXISTS `transactions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `transaction_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.transactions: ~0 rows (approximately)

-- Dumping structure for table shopplaza.users
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` tinyint(4) NOT NULL DEFAULT 0,
  `created_at` datetime(6) DEFAULT NULL,
  `is_ban` bit(1) DEFAULT NULL,
  `coint` double DEFAULT NULL,
  `vip_id` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.users: ~9 rows (approximately)
INSERT INTO `users` (`user_id`, `username`, `password`, `role`, `created_at`, `is_ban`, `coint`, `vip_id`) VALUES
	(1, 'user1', '$2a$10$8QOdKaEhveLLd5.4DpqV8uSUwXgFQGG0YAzHryrBZpK', 1, '2023-08-18 20:56:30.000000', b'0', 0, 1),
	(14, 'user2', '$2y$10$CTPBZ5XqBIWPfBKdYIQ3TeLHjYZgYmrGKSDqL7n2Kq2', 1, '2023-08-18 20:56:30.000000', b'0', 0, 2),
	(15, 'seller1', '$2y$10$CTPBZ5XqBIWPfBKdYIQ3TeLHjYZgYmrGKSDqL7n2Kq2', 1, '2023-08-18 20:56:30.000000', b'0', 0, 3),
	(16, 'admin', '$2a$10$lSh/O9xRTEtsEF5gF1og4.xCaMEvndgbJxMXge6K3kpTTQ0.Z3Yb2', 2, '2023-08-18 20:56:30.000000', b'0', 2200, 3),
	(17, 'haidz', '$2a$10$cz18Rez.t8MUZ2lwl16yiOChZcv256zcs/xZBSyXNx0', 0, '2023-08-19 19:01:42.000000', b'0', 0, 4),
	(18, 'test', '$2a$10$lSh/O9xRTEtsEF5gF1og4.xCaMEvndgbJxMXge6K3kpTTQ0.Z3Yb2', 0, '2023-08-19 22:52:45.000000', b'0', 111, 3),
	(19, 'test11', '$2a$10$lSh/O9xRTEtsEF5gF1og4.xCaMEvndgbJxMXge6K3kpTTQ0.Z3Yb2', 1, '2023-08-19 22:53:15.000000', b'0', 928, 4),
	(20, 'test111', '$2a$10$xAZTzhjhnbB4vAlLm11BnuLoXi30BsYnUnuUFTLG7yn', 1, '2023-08-19 22:53:37.000000', b'0', 0, 6),
	(21, 'admin1', '$2a$10$lSh/O9xRTEtsEF5gF1og4.xCaMEvndgbJxMXge6K3kpTTQ0.Z3Yb2', 0, '2023-08-28 07:42:26.000000', b'0', 0, 7);

-- Dumping structure for table shopplaza.users_info
CREATE TABLE IF NOT EXISTS `users_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `image_avatar` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `updated_time` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `users_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.users_info: ~7 rows (approximately)
INSERT INTO `users_info` (`id`, `user_id`, `image_avatar`, `fullname`, `email`, `phone`, `gender`, `date`, `address`, `updated_time`) VALUES
	(6, 16, '7cdcce26-bf8c-4d71-a359-41a116469599.jpg', 'Hải dz', 'haindth2108034@fpt.edu.vn', '08622216861', 'Nam', '2023-08-15', 'T1-D7\r\nKhu Do THi Dang Xa', '2023-08-26 14:07:30'),
	(7, 14, '9dfd0807-cc6b-413b-b109-e1c60e6d32eb.jpg', 'Hải ĐÌnh Nguyễn', 'haindth2108034@fpt.edu.vn', '0383087656', 'Female', '2023-08-16', 'T1-D7\r\nKhu Do THi Dang Xa', '2023-08-26 14:07:35'),
	(17, 18, '23da7351-15d4-4c5f-83cc-ec15a32afd5e.jpg', '425', 'valtryek1223@gmail.com', '0862221686', 'Female', '2023-08-25', 'T1-D7\r\nKhu Do THi Dang Xa', '2023-08-26 14:10:41'),
	(26, 1, '7c0749fa-1472-46b4-a18c-1b71c1776685.jpg', 'Hải ĐÌnh Nguyễn', 'haindth2108034@fpt.edu.vn', '0383087656', 'Male', '2023-08-23', 'T1-D7\r\nKhu Do THi Dang Xa', '2023-08-27 03:35:39'),
	(28, 15, '67408949-070e-41e9-a6ab-8804a58ba3c9.jpg', 'Trần Phát Đạt', 'haindth2108034@fpt.edu.vn', '0862221686', 'Nam', '2023-08-31', 'T1-D7\r\nKhu Do THi Dang Xa', '2023-08-27 04:41:09'),
	(31, 21, '4c0e2e85-c7b7-4df1-9cbd-b8e7b35db1b2.jpg', 'Trần Phát Đạt', 'haindth2108034@fpt.edu.vn', '0862221686', 'Male', '2023-08-21', 'T1-D7\r\nKhu Do THi Dang Xa', '2023-08-28 09:33:25'),
	(33, 19, '36e611ec-f695-4bea-9615-44ca3f7f6be3.png', 'Trần Phát Đạt', 'valtryek1223@gmail.com', '0862221686', 'Male', '2023-08-02', 'T1-D7\r\nKhu Do THi Dang Xa', '2023-08-28 10:11:30');

-- Dumping structure for table shopplaza.vip_by_user
CREATE TABLE IF NOT EXISTS `vip_by_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `vip_id` int(11) NOT NULL DEFAULT 0,
  `create_time` datetime(6) DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_vip_by_user_users` (`user_id`),
  KEY `FK_vip_by_user_vip_package` (`vip_id`),
  CONSTRAINT `FK_vip_by_user_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_vip_by_user_vip_package` FOREIGN KEY (`vip_id`) REFERENCES `vip_package` (`vip_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.vip_by_user: ~2 rows (approximately)
INSERT INTO `vip_by_user` (`id`, `user_id`, `vip_id`, `create_time`, `end_time`, `status`) VALUES
	(44, 16, 3, '2023-08-28 22:29:01.000000', '2023-09-27 22:29:01.000000', 'ONLINE'),
	(45, 18, 3, '2023-08-30 22:40:34.000000', '2023-09-29 22:40:34.000000', 'ONLINE');

-- Dumping structure for table shopplaza.vip_package
CREATE TABLE IF NOT EXISTS `vip_package` (
  `vip_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `days` bigint(20) DEFAULT NULL,
  `percent_off` double DEFAULT NULL,
  PRIMARY KEY (`vip_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table shopplaza.vip_package: ~3 rows (approximately)
INSERT INTO `vip_package` (`vip_id`, `name`, `description`, `price`, `days`, `percent_off`) VALUES
	(1, 'VIP 1', 'Khi mua gói này,bạn sẽ có 30 ngày để sử dụng,khi sử dụng sẽ có các đặc quyền như ưu tiên hiển thị trên thanh tìm kiếm', 20, 30, 1),
	(2, 'VIP 2', 'Khi mua gói này,bạn sẽ có 30 ngày để sử dụng,khi sử dụng sẽ có các đặc quyền như ưu tiên hiển thị trên thanh tìm kiếm', 99, 30, 2),
	(3, 'VIP 3', 'Khi mua gói này,bạn sẽ có 30 ngày để sử dụng,khi sử dụng sẽ có các đặc quyền như ưu tiên hiển thị trên thanh tìm kiếm', 100, 30, 3);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
