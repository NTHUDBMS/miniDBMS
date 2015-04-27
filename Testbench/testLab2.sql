create table user1
(userId int primary key,
name varchar(30),
userLocation varchar(30))

create table tweets
(twid int primary key,
tweet varchar(30),
utcDate varchar(30),
city varchar(30),
userId int)

SELECT userLocaiton,name
From user1
SELECT city
From tweets

SELECT *
FROM tweets
WHERE userId =  16285429 OR utcDate = 'Dec 27 2008  3:00PM'

SELECT *
FROM user1
WHERE userLocation = 'Afghanistan'

SELECT tweet
FROM tweets
WHERE twid > 905750092 AND userId > 16200000

SELECT user1.*, tweets.tweet
FROM tweets, user1
WHERE tweets.userId = user1.userId

SELECT user1.userId, tweets.tweet
FROM tweets, user1
WHERE tweets.userId = user1.userId AND user1.userId >10000000

SELECT utcDate
FROM tweets, user1
WHERE user1.userId = tweets.userId AND userLocation = 'Cardiff'

SELECT COUNT (*)
FROM user1
WHERE userLocation = 'Mexico'

SELECT COUNT(tweet)
FROM tweets
WHERE userId = 16060889


SELECT userId
FROM user1, tweets
WHERE userId = 123

SELECT user.userId, tweets.*
FROM user1, tweets
WHERE user1.userId = tweets.userId AND userId = 'hello'