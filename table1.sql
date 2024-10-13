DROP DATABASE `Photo Diary`;

CREATE DATABASE `Photo Diary`;

CREATE TABLE `Photo Diary`.`Users` (
    UserID INT PRIMARY KEY,
    UserName VARCHAR(50) NOT NULL,
    Password VARCHAR(50) NOT NULL
);

CREATE TABLE `Photo Diary`.`Friends` (
    friendshipID INT PRIMARY KEY,
    userID INT,
    friendID INT,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (FriendID) REFERENCES Users(UserID)
);

CREATE UNIQUE INDEX idx_UserName ON `Photo Diary`. `Users` (UserName);

CREATE TABLE `Photo Diary`.`Entries` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user` VARCHAR(45),
  `image` VARCHAR(45) NULL,
  `caption` VARCHAR(255) NULL,
  `time` DATETIME NULL,
  `longitude` DECIMAL(10,6) NULL,
  `latitude` DECIMAL(10,6) NULL,
  `likeCount` INT NULL,
  `privacy` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user`) REFERENCES Users(`UserName`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE
);

INSERT INTO `Photo Diary`.`Users` (`UserID`, `username`, `password`)
VALUES
(1, 'user1', 'abcd');

INSERT INTO `Photo Diary`.`Users` (`UserID`, `username`, `password`)
VALUES
(2, 'user2', '123');

INSERT INTO `Photo Diary`.`Users` (`UserID`, `username`, `password`)
VALUES
(3, 'user3', 'password');

INSERT INTO `Photo Diary`.`Entries` (`user`, `image`, `caption`, `time`, `longitude`, `latitude`, `likeCount`, `privacy`)
VALUES
('user1', 'image1.jpg', 'Caption for image 1', '2024-04-15 12:00:00', 34.025686533953234, -118.282883, 10, "public");


INSERT INTO `Photo Diary`.`Entries` (`user`, `image`, `caption`, `time`, `longitude`, `latitude`, `likeCount`, `privacy`)
VALUES
('user2', 'image2.jpg', 'Caption for image 2', '2024-04-15 12:00:00', 34.02218600000001, -118.284485, 20, "private");


INSERT INTO `Photo Diary`.`Entries` (`user`, `image`, `caption`, `time`, `longitude`, `latitude`, `likeCount`, `privacy`)
VALUES
('user3', 'image2.jpg', 'Caption for image 3', '2024-04-15 12:00:00', 34.019493000000004, -118.28949, 1, "friends");

SELECT * FROM `Photo Diary`.`Entries`;