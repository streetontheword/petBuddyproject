drop database if exists userlogin;

create database userlogin; 

use userlogin;

create table users (

    userId varchar(8),
    username varchar(32) not null unique, 
    email varchar(32) not null unique,
    first_name varchar(32),
    last_name varchar(32),
    password varchar(72) not null,
    role varchar(10),
    inquiryId varchar(45),
    userUrl varchar(256),

    primary key (userId)
);


create table petInformation(
    
    petId int auto_increment,
    name varchar(128),
    dateOfBirth date, 
    dateOfLastVaccination date, 
    microchipNumber BIGINT,
    gender varchar(8),
    comments text, 
    picture text,
    breed text,

    userId varchar(8),


primary key(petId),
constraint fk_email foreign key(userId) references users(userId)


);

create table savedInterestedPets (
	savedInterestedPetId INT AUTO_INCREMENT primary key,
    petId int,
    name varchar(32) not null, 
    primaryBreed varchar(256),
    secondaryBreed varchar(256),
    gender varchar(10),
    url varchar(256),
    userId varchar(8),
    
    
constraint foreign key(userId) references users(userId),
CONSTRAINT uc_pet_user UNIQUE (petId, userId)
    
    
);

create table inquiry (
inquiryId varchar(8) primary key,
first_name varchar(32),
last_name varchar(32),
email varchar(32) not null,
birthdate date,
gender varchar(32),
intended_visit_date date, 
nationality varchar(32),
other varchar(32),
petid varchar(32),
dogName varchar(64), 
url text,
firstTimeOwner boolean default false,
confirmed boolean default false,
selectedHour varchar(8),
declined boolean default false, 

userId varchar(8),

constraint foreign key(userId) references users(userId)

);


create table notifications (
notificationId varchar(8) primary key,
username varchar(32),
notificationRead boolean default false,  
timestamp bigint

);



 