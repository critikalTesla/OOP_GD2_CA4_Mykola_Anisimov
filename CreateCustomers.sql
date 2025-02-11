DROP TABLE IF EXISTS customers;


CREATE TABLE customers(custID int NOT NULL AUTO_INCREMENT,
             firstName VARCHAR(20),
             lastName VARCHAR(50),
             dob date,
			 PRIMARY KEY (custID));
			 


INSERT INTO customers VALUES (101, 'Tom', 'Brady', '2018-11-11' );
INSERT INTO customers VALUES (107, 'Mary', 'Reilly', '2019-6-12');
INSERT INTO customers VALUES (105, 'James', 'Ryan', '2019-9-23');
INSERT INTO customers VALUES (106, 'Alice', 'O''Brien', '2019-12-1');