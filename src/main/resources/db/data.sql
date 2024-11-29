INSERT INTO MEMBER ("name", "password", "type") VALUES
    ('teacher1', '$2a$10$MGsRhnqTU1wElNAJl7dLMOLSGFTPU8IzPoL/Un9V3VdrbYdfc3rI.', 'TEACHER'),
    ('teacher2', '$2a$10$MGsRhnqTU1wElNAJl7dLMOLSGFTPU8IzPoL/Un9V3VdrbYdfc3rI.', 'TEACHER'),
    ('student1', '$2a$10$MGsRhnqTU1wElNAJl7dLMOLSGFTPU8IzPoL/Un9V3VdrbYdfc3rI.', 'STUDENT'),
    ('student2', '$2a$10$MGsRhnqTU1wElNAJl7dLMOLSGFTPU8IzPoL/Un9V3VdrbYdfc3rI.', 'STUDENT'),
    ('student3', '$2a$10$MGsRhnqTU1wElNAJl7dLMOLSGFTPU8IzPoL/Un9V3VdrbYdfc3rI.', 'STUDENT'),
    ('student4', '$2a$10$MGsRhnqTU1wElNAJl7dLMOLSGFTPU8IzPoL/Un9V3VdrbYdfc3rI.', 'STUDENT'),
    ('student5', '$2a$10$MGsRhnqTU1wElNAJl7dLMOLSGFTPU8IzPoL/Un9V3VdrbYdfc3rI.', 'STUDENT');
;

INSERT INTO UNIT_CODE ("id", "unit_code", "name")
SELECT * FROM CSVREAD('classpath:db/unitCodeData.csv');

INSERT INTO PROBLEM ("id", "unit_code","level","type","answer")
SELECT * FROM CSVREAD('classpath:db/problemData.csv');


