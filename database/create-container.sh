#!/bin/bash

docker build . -t "friendly_stats_db_image"

docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=DefaultPassword1!" -e "TZ=America/New_York" \
  -p 1433:1433 --name friendly_stats_db --hostname friendly_stats_db \
  -v friendly_stats_db_volume:/var/opt/mssql \
  -d friendly_stats_db_image