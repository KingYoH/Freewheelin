INSERT INTO MEMBER ("name", "password", "type") VALUES
    ('teacher1', '1234', 'TEACHER'),
    ('student1', '1234', 'STUDENT');

INSERT INTO PROBLEM ("id", "unit_code","level","type","answer")
SELECT * FROM CSVREAD('classpath:db/problemData.csv');

INSERT INTO UNIT_CODE ("id", "unit_name", "description")
SELECT * FROM CSVREAD('classpath:db/unitCodeData.csv');
