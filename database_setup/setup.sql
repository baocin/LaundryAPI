drop database Laundry;
create database Laundry;
use Laundry;
CREATE TABLE dryers (
        dryer_id bigint NOT NULL AUTO_INCREMENT,
        name tinytext,
        time_left int,
        available bit,
        raw_status tinytext,
        PRIMARY KEY (dryer_id)
);

CREATE TABLE washers (
        washer_id bigint NOT NULL AUTO_INCREMENT,
        name tinytext,
        time_left int,
        available bit,
        raw_status tinytext,
        PRIMARY KEY (washer_id)
);

CREATE TABLE rooms (
        room_id int NOT NULL AUTO_INCREMENT,
        floor_number int,
        hall_name varchar(50),
        url text,
        num_available_washers int,
        num_available_dryers int,
        washer_count int,
        dryer_count int,
        PRIMARY KEY (room_id)
);

CREATE TABLE dryer_room_map (
        dryer_id bigint NOT NULL,
        room_id int NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_dryer_id FOREIGN KEY (dryer_id) REFERENCES dryers(dryer_id),
        CONSTRAINT fk_dryer_room_id FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE washer_room_map (
        washer_id bigint NOT NULL,
        room_id int NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_washer_id FOREIGN KEY (washer_id) REFERENCES washers(washer_id),
        CONSTRAINT fk_washer_room_id FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);
