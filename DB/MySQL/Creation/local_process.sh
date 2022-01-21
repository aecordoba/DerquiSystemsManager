#!/bin/bash
# File: local_process.sh
# Date: December 6th, 2018
# Author: Adrián E. Córdoba
# Description: Process scripts on localhost.

./dsmdb_scripts/create_database.sh $1 2>&1 | grep -v "Using a password on the command line interface can be insecure."

