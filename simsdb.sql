-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 30, 2022 lúc 08:22 PM
-- Phiên bản máy phục vụ: 10.4.21-MariaDB
-- Phiên bản PHP: 7.4.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `simsdb`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `admin`
--

CREATE TABLE `admin` (
  `AdminId` int(11) NOT NULL,
  `AdminEmail` varchar(255) NOT NULL,
  `AdminPassword` varchar(255) NOT NULL,
  `AdminName` varchar(255) NOT NULL,
  `AdminAvatar` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `admin`
--

INSERT INTO `admin` (`AdminId`, `AdminEmail`, `AdminPassword`, `AdminName`, `AdminAvatar`) VALUES
(1, 'admin@gmail.com', '1', 'Xuan Canh', 'http://192.168.31.14/SIMS/admin/images/1640552977479_1640552981820.jpg'),
(5, 'test@mail.ru', '1', 'TEST', 'http://192.168.31.7/SIMS/admin/images/1620665594214_1620666233471.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `students`
--

CREATE TABLE `students` (
  `StudentId` int(11) NOT NULL,
  `StudentPassword` varchar(100) NOT NULL,
  `StudentName` varchar(100) DEFAULT NULL,
  `StudentNo` varchar(50) DEFAULT NULL,
  `StudentEmail` varchar(100) DEFAULT NULL,
  `StudentGender` int(11) NOT NULL DEFAULT -1,
  `StudentDOB` varchar(10) DEFAULT NULL,
  `StudentClass` varchar(50) DEFAULT NULL,
  `StudentActive` int(11) DEFAULT 0,
  `StudentAvatar` varchar(255) NOT NULL DEFAULT '',
  `StudentPhone` varchar(50) DEFAULT NULL,
  `StudentNotice` varchar(255) DEFAULT '',
  `StudentReport` varchar(255) DEFAULT '',
  `StudentReply` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `students`
--

INSERT INTO `students` (`StudentId`, `StudentPassword`, `StudentName`, `StudentNo`, `StudentEmail`, `StudentGender`, `StudentDOB`, `StudentClass`, `StudentActive`, `StudentAvatar`, `StudentPhone`, `StudentNotice`, `StudentReport`, `StudentReply`) VALUES
(17, '2', 'Xuan Canh', 'L141299', 'xuancanhit99@gmail.com', 0, '14/09/1999', 'IKBO-07-19', 1, 'http://192.168.31.14/SIMS/images/1639391982849_1639391985292.jpg', '89858944005', 'Please enter your student info', '123', 'abc'),
(30, '123456', 'Le Dinh Cuong', 'L12345', 'dinhcuong@ru.ru', 1, '04/16/1999', 'IKB0-07-19', 1, 'http://192.168.31.7/SIMS/images/1620752784899_1620752790030.jpg', '123456789', 'Please enter your student info', 'reportCuong', 'fix'),
(43, '1', 'Tran Hoang Anh', '007', 'anh@com.ru', 1, '05/10/1956', 'IVBO-01-19', 0, 'http://192.168.31.7/SIMS/images/1620665594214_1620665596667.jpg', '3123123123', 'Please enter your student info', 'reportAnh', 'canh'),
(70, '1', 'Hong Nhung', 'L001', 'hong@nhung.vn', 0, '07/09/2003', 'Lop 12', 1, '', '090720031232', 'Please enter your student info', '123', '');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`AdminId`);

--
-- Chỉ mục cho bảng `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`StudentId`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `admin`
--
ALTER TABLE `admin`
  MODIFY `AdminId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT cho bảng `students`
--
ALTER TABLE `students`
  MODIFY `StudentId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
