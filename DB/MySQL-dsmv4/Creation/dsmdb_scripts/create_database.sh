#!/bin/bash
# File: create_database.sh
# Date: November 9th, 2019
# Author: Adrián E. Córdoba
# Description: Create  the database.

backup_file="./dsmdb_scripts/dsmv4_backup.sql"


cat ./dsmdb_scripts/01-structure.sql | mysql -u root --password=$1 2>&1 | grep -v "Using a password on the command line interface can be insecure."
cat ./dsmdb_scripts/02-data.sql | mysql -u root --password=$1 2>&1 | grep -v "Using a password on the command line interface can be insecure."

cat ./dsmdb_scripts/Functions/*.sql | mysql -u root --password=$1 dsmv4 2>&1 | grep -v "Using a password on the command line interface can be insecure."
cat ./dsmdb_scripts/"Stored Procedures"/*.sql | mysql -u root --password=$1 dsmv4 2>&1 | grep -v "Using a password on the command line interface can be insecure."

if [ -e $backup_file ]
then
    mysql -u root --password=$1 dsmv4 < $backup_file 2>&1 | grep -v "Using a password on the command line interface can be insecure."
fi
