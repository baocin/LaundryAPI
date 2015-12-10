drop database Laundry;
create database Laundry;
use Laundry;

CREATE TABLE rooms (
        room_id int NOT NULL AUTO_INCREMENT,
        floor_number int,
        hall_name varchar(50),
        url text,
        washer_count int,
        dryer_count int,
        PRIMARY KEY (room_id)
);

CREATE TABLE dryer_room_map (
        map_id bigint NOT NULL AUTO_INCREMENT,
        dryer_name varchar(4),
        room_id int NOT NULL,
        time_left int,
        raw_status tinytext,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_dryer_room_id FOREIGN KEY (room_id) REFERENCES rooms(room_id),
        PRIMARY KEY (map_id)

);

CREATE TABLE washer_room_map (
        map_id bigint NOT NULL AUTO_INCREMENT,
        washer_name varchar(4),
        room_id int NOT NULL,
        time_left int,
        raw_status tinytext,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_washer_room_id FOREIGN KEY (room_id) REFERENCES rooms(room_id),
        PRIMARY KEY (map_id)

);
