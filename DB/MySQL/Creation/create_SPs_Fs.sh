#!/bin/bash
# File: create_SPs_Fs.sh
# Date: July 30th, 2016
# Author: Adrián E. Córdoba
# Description: Create functions and stored procedures in a database from all sql scripts 
# in the current directory.

echo -n "Enter MySQL root's password on localhost: "
read -s password
echo

cat ./dsmdb_scripts/Functions/*.sql | mysql -u root --password=$password ac1 2>&1 | grep -v "Warning: Using a password"
cat ./dsmdb_scripts/"Stored Procedures"/*.sql | mysql -u root --password=$password ac1 2>&1 | grep -v "Warning: Using a password"
