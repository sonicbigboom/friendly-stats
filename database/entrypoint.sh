#!/bin/bash

if [ ! -e /var/opt/mssql/schema.version ]; then
  echo "-0001" > /var/opt/mssql/schema.version
fi

/usr/config/update-schema.sh &

# Start SQL Server.
/opt/mssql/bin/sqlservr