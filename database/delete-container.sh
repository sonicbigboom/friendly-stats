#!/bin/bash

docker stop friendly_stats_db
docker rm friendly_stats_db
docker rmi friendly_stats_db_image