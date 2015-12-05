drop database Laundry;
create database Laundry;
use Laundry;
CREATE TABLE dryers (
        dryer_id int NOT NULL,
        name tinytext,
        time_left int,
        available bit,
        raw_status text ,
        is_washer bit,
        PRIMARY KEY (dryer_id)
);

CREATE TABLE washers (
        washer_id int NOT NULL,
        name tinytext,
        time_left int,
        available bit,
        raw_status text ,
        is_washer bit,
        PRIMARY KEY (washer_id)
);

CREATE TABLE rooms (
        room_id int NOT NULL,
        floor_number int,
        hall_name tinytext,
        url text,
        available_washers int,
        available_dryers int,
        washer_count int,
        dryer_count int,
        PRIMARY KEY (room_id)
);

CREATE TABLE dryer_room_map (
        dryer_id int NOT NULL,
        room_id int NOT NULL,
        CONSTRAINT fk_dryer_id FOREIGN KEY (dryer_id) REFERENCES dryers(dryer_id)
        CONSTRAINT fk_room_id FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE dryer_room_map (
        washer_id int NOT NULL,
        room_id int NOT NULL,
        CONSTRAINT fk_washer_id FOREIGN KEY (washer_id) REFERENCES washers(washer_id)
        CONSTRAINT fk_room_id FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);
