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

Run the following to download the SQL Server image:

`sudo docker pull mcr.microsoft.com/mssql/server:2022-latest`

Run the following to create the sql server:

TODO: This command should run the `createTables.sql` script.

```
sudo docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=DefaultPassword1!" \
   -p 1433:1433 --name friendly_stats --hostname friendly_stats \
   -d \
   mcr.microsoft.com/mssql/server:2022-latest
```

TODO: Expand the following section with detailed instructions.

Change the `sa` password to a secure password.  Create a new sysadmin account with a strong password. Disable the `sa` account and use the new sysadmin account instead.

Create a new user with a strong password for the server to connect to the database. This user should only have access to the `stats` database.

### Server

Set the following environment variables appropriately, based on the database.

`FRIENDLY_STATS_DB_HOST` - The database host. For example: `localhost:1433`\
`FRIENDLY_STATS_DB_USERNAME` - The database username. For example: `sa`\
`FRIENDLY_STATS_DB_PASSWORD` - The database host. For example: `DefaultPassword1!`

## Goals

- Users information is tied to an account (oauth2?)
- Users can create a group that tracks game data
- This group can track any number of games desired
	- One of these games is poker
		- This should track money over time
		- This should also handle buy in and cash outs
	- Another 'game' is bets
	- It should be easy for users to make their own games / stat counters