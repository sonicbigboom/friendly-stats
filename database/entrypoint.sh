#!/bin/bash
date >> /tmp/log
echo "entrypoint.sh" >> /tmp/log 


if [ -e /usr/config/setup.done ]; then
  date >> /tmp/log
  echo "Skipping setup." >> /tmp/log 
else
  date >> /tmp/log
  echo "Running initial setup." >> /tmp/log 

  /usr/config/configure-db.sh &

  touch /usr/config/setup.done
fi

# Start SQL Server.
/opt/mssql/bin/sqlservr