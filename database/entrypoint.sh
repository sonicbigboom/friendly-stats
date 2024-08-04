#!/bin/bash
/usr/bin/date >> /tmp/log
/usr/bin/echo "entrypoint.sh" >> /tmp/log 

# Start the script to configure the DB in the background.
/usr/config/configure-db.sh &

# Start SQL Server.
/opt/mssql/bin/sqlservr