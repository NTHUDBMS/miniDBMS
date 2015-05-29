CREATE TABLE Stopword (
stopwordId int PRIMARY KEY,
stopwordText varchar(50),
tfidf int,
counts int,
freq int
);

CREATE TABLE Abstract (
abstractId int PRIMARY KEY,
text varchar(50),
wordcount int,
source int,
stopwordId int
);