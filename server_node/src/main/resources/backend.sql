drop table if exists shared_filesystem;
create table if not exists Shared_FileSystem (owner varchar(255), filename varchar(255), permission Boolean, portNumber varchar(255));
INSERT INTO shared_FileSystem VALUES ('arsalan', 'sample.txt', false, '1075');
INSERT INTO shared_FileSystem VALUES ('ARSALAN', 'sample.txt', false, '1092');
INSERT INTO shared_FileSystem VALUES ('lemon', 'sweet.txt', false, '1076');
INSERT INTO shared_FileSystem VALUES ('SHAZAM', 'viner.txt', false,'1077');
INSERT INTO shared_FileSystem VALUES ('SUPERMAN', 'asf.txt', false, '1078');
INSERT INTO shared_FileSystem VALUES ('SUPERMAN', 'awes.txt', false, '1079');
INSERT INTO shared_FileSystem VALUES ('BATMAN', 'jake.txt', false, '1080');