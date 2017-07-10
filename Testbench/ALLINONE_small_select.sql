-- SELECT userLocation,name
-- From user1;

-- SELECT city
-- From tweets;


-- SELECT *
-- FROM user1
-- WHERE userLocation = 'Spain';

-- SELECT tweet
-- FROM tweets
-- WHERE twid > 1093554706 AND userId > 16200000;


-- SELECT *
-- FROM tweets
-- WHERE userId =  17989391 OR utcDate = 'Dec 31 2008  5:21PM';


-- SELECT user1.*, tweets.tweet
-- FROM tweets, user1
-- WHERE tweets.userId = user1.userId;

-- SELECT user1.userId, tweets.tweet
-- FROM tweets, user1
-- WHERE tweets.userId = user1.userId AND user1.userId >17989390;

-- SELECT utcDate
-- FROM tweets, user1
-- WHERE user1.userId = tweets.userId AND userLocation = 'Cardiff';

-- SELECT COUNT (*)
-- FROM user1
-- WHERE userLocation = 'Mexico';

-- SELECT COUNT(tweet)
-- FROM tweets
-- WHERE userId = 16060889;


-- SELECT userId
-- FROM user1, tweets
-- WHERE userId = 123;

-- SELECT user1.userId, tweets.*
-- FROM user1, tweets
-- WHERE user1.userId = tweets.userId AND userId = 'hello';