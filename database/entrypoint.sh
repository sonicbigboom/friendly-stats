#!/bin/bash
date >> /tmp/log
echo "entrypoint.sh" >> /tmp/log 


if [ -e /var/opt/mssql/db-volume-setup.done ]; then
  date >> /tmp/log
  echo "Skipping db volume setup." >> /tmp/log 
else
  date >> /tmp/log
  echo "Running db volume initial setup." >> /tmp/log 

  /usr/config/configure-db-volume.sh &

  touch /var/opt/mssql/db-volume-setup.done
fi

# Start SQL Server.
/opt/mssql/bin/sqlservr