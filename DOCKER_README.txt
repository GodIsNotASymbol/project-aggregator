# Create the docker image
docker run --name zero-app-mysql -e MYSQL_ROOT_PASSWORD=P4ssw0rd -d mysql:8.4.1
# use this to enter and create the database
docker exec -it zero-app-mysql bash
# connect as root to mysql and create database named zero
mysql -uroot -pP4ssw0rd
create database zero;
create table phrase (ID int PRIMARY KEY AUTO_INCREMENT, phrase VARCHAR(200) )
# Run the dbscripts located under the resources for the project
