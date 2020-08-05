DROP TABLE IF EXISTS Employee;
 
CREATE TABLE StudentModel(
  rollno INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  department VARCHAR(250) NOT NULL
);
 
INSERT INTO StudentModel(rollno, name, department) VALUES
  (100, 'Dangote', 'CSE'),
  (101, 'Gates', 'PT'),
  (102, 'Alakija', 'AERO');
