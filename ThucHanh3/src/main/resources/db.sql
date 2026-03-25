-- MySQL script for this exercise
-- Create database (optional)
-- CREATE DATABASE thuchanh3 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE thuchanh3;

-- 1) Create table
CREATE TABLE IF NOT EXISTS Accounts (
  AccountId VARCHAR(10) PRIMARY KEY,
  FullName  NVARCHAR(50),
  Balance   DECIMAL(18, 2)
);

-- 2) Seed sample data
INSERT INTO Accounts (AccountId, FullName, Balance)
VALUES
  ('ACC01', 'Nguyen Van A', 5000),
  ('ACC02', 'Tran Thi B', 2000)
ON DUPLICATE KEY UPDATE
  FullName = VALUES(FullName),
  Balance = VALUES(Balance);

-- 3) Stored procedure (CallableStatement)
DROP PROCEDURE IF EXISTS sp_UpdateBalance;
DELIMITER //
CREATE PROCEDURE sp_UpdateBalance (
  IN p_Id VARCHAR(10),
  IN p_Amount DECIMAL(18, 2)
)
BEGIN
  UPDATE Accounts
  SET Balance = Balance + p_Amount
  WHERE AccountId = p_Id;
END //
DELIMITER ;

