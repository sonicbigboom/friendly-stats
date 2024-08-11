#  Friendly Stats

A website that lets friends track stats with one another. (Poker games, silly bets, gin rummy score, etc...)

## Table of Contents

- [Links](#links)
- [Requirements](#requirements)
- [Setup](#setup)
- [Goal](#goals)

## Links

- [Github](https://github.com/sonicbigboom/friendly-stats)
- [Hosted](https://www.potrt.com/friendly-stats)

## Requirements

- [Java 21](https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.4+7/OpenJDK21U-jdk_x64_windows_hotspot_21.0.4_7.msi)
- Linux VM / Machine

## Setup

The setup is split into 3 parts, the [database](#database), the [server](#server), and the [client](#client).

### Database

The database will be setup on a linux machine, all of the following should be run on linux.

#### Docker

Follow these instructions: https://docs.docker.com/engine/install/

#### SQL Server

Clone this repository `https://github.com/sonicbigboom/friendly-stats.git` and navigate to the database directory.  
Run `sudo chmod +x create-docker.sh` and `sudo chmod +x delete-container.sh` to give the scripts permissions.
Use the following commands as admin as needed:

Create container - `sudo create-docker.sh`  
Start container - `sudo docker start friendly_stats`  
Stop container - `sudo docker stop friendly_stats`  
Delete container and image - `sudo delete-container.sh`

> [!CAUTION]  
> Running the following command will permanently delete all data. Please make a backup first!

Delete volume and data - `sudo docker volume rm friendly_stats_db_volume`

TODO: Expand the following section with detailed instructions.

Change the `sa` password by providing a new secure password to the prompt:

```
sudo docker exec -it friendly_stats /opt/mssql-tools18/bin/sqlcmd \
 -C \
 -S localhost \
 -U SA \
 -P "DefaultPassword1!" \
 -Q "ALTER LOGIN SA WITH PASSWORD=\"$(read -sp "Enter new SA password: "; echo "${REPLY}")\""
```

Create a new sysadmin account with a strong password. Disable the `sa` account and use the new sysadmin account instead.

Create a new user with a strong password for the server to connect to the database. This user should only have access to the `stats` database.

### Server

Set the following environment variables appropriately, based on the database.

`FRIENDLY_STATS_SIGNATURE` - An HMAC-SHA256 hex hash that is the application's secret signature. \
`FRIENDLY_STATS_DB_HOST` - The database host. For example: `localhost:1433`\
`FRIENDLY_STATS_DB_USERNAME` - The database username. For example: `sa`\
`FRIENDLY_STATS_DB_PASSWORD` - The database host. For example: `DefaultPassword1!`\
`FRIENDLY_STATS_GOOGLE_CLIENT_ID` - The google oauth client id.\
`FRIENDLY_STATS_GOOGLE_CLIENT_SECRET` - The google oauth client secret.

## Goals

- Users information is tied to an account (oauth2?)
- Users can create a group that tracks game data
- This group can track any number of games desired
	- One of these games is poker
		- This should track money over time
		- This should also handle buy in and cash outs
	- Another 'game' is bets
	- It should be easy for users to make their own games / stat counters