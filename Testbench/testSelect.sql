SELECT bookId, title, pages, authorId, editorial 
FROM Book;

SELECT *
 FROM Author;

SELECT title  
FROM Book 
 WHERE bookId = 1;

SELECT b.title  
FROM Book AS b  
WHERE pages > 100 AND editorial = 'Prentice Hall';

SELECT *  
FROM Book 
 WHERE authorId = 1 OR pages < 200;

SELECT b.*  
FROM Book AS b, Author AS a 
WHERE b.authorId = a.authorId AND a.name = 'Michael Crichton';

SELECT bookId, title, pages, name  
FROM Book, Author 
 WHERE Book.authorId = Author.authorId;

SELECT a.name, title  
FROM Book, Author AS a 
 WHERE Book.authorId = a.authorId AND Book.pages > 200;

SELECT a.name
 FROM Author AS a, Book AS b
 WHERE a.authorId = b.authorId AND b.title = 'Star Wars';

SELECT a.name, b.title
 FROM Author AS a, Book AS b
 WHERE a.authorId = b.authorId AND a.nationality <> 'Taiwan';

SELECT COUNT(*) 
FROM Book;

SELECT COUNT(editorial) 
FROM Book;

SELECT COUNT(*)
 FROM Author 
WHERE nationality = 'Taiwan';

SELECT SUM(pages) 
FROM Book 
WHERE authorId = 2;

SELECT authorId 
FROM Author, Book 
WHERE Author.authorId = Book.authorId AND Book.title = 'Star Wars';

SELECT * 
FROM Author
 WHERE authorId = 'John';

SELECT Book.*
 FROM Book, Author 
WHERE Book.authorId = Author.name;