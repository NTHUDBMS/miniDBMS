SELECT SUM(source)
FROM Abstract,Stopword
WHERE Abstract.stopwordId =
Stopword.stopwordId AND tfidf <5;

SELECT COUNT(Abstract.stopwordId)
FROM Abstract, Stopword
WHERE Abstract.stopwordId = 
Stopword.stopwordId AND counts = 50;

SELECT SUM(wordcount)
FROM Abstract, Stopword
WHERE Abstract.stopwordId = Stopword.stopwordId AND freq > 15;

SELECT COUNT(source)
FROM Abstract, Stopword
WHERE Abstract.stopwordId = Stopword.stopwordId AND counts <20;

SELECT SUM(freq)
FROM Abstract, Stopword
WHERE Abstract.stopwordId = Stopword.stopwordId AND wordcount = 100;


SELECT SUM(tfidf)
FROM Abstract, Stopword
WHERE Abstract.stopwordId = Stopword.stopwordId AND Abstract.stopwordId >
100;

SELECT COUNT(Stopword.stopwordId)
FROM Abstract, Stopword
WHERE Abstract.stopwordId = Stopword.stopwordId AND wordcount <> 50;

