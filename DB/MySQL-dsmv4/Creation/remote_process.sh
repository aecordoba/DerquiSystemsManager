#!/bin/bash
# File: remote_process.sh
# Date: December 6th, 2018
# Author: Adrián E. Córdoba
# Description: Process scripts on remote host.

ssh $2@$1 mkdir dsmdb_scripts
scp -r ./dsmdb_scripts/* $2@$1:~/dsmdb_scripts
ssh $2@$1 ~/dsmdb_scripts/create_database.sh $3
ssh $2@$1 rm -Rf ~/dsmdb_scripts
