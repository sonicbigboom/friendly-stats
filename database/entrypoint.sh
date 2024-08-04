#!/bin/bash

# Start the script to configure the DB in the background.
/usr/config/configure-db.sh &

# Start SQL Server.
/opt/mssql/bin/sqlservr