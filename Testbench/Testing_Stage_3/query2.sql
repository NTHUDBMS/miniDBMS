SELECT COUNT(*)
FROM user1, trans
WHERE user1.attr1 = trans.attr2 AND user1.attr5 > 50000;