#!/bin/bash

# Wait 60 seconds for SQL Server to start up by ensuring that 
# calling SQLCMD does not return an error code, which will ensure that sqlcmd is accessible
# and that system and user databases return "0" which means all databases are in an "online" state
# https://docs.microsoft.com/en-us/sql/relational-databases/system-catalog-views/sys-databases-transact-sql?view=sql-server-2017 

DBSTATUS=1
ERRCODE=1
i=0

while [[ ( $DBSTATUS -ne 0 || $ERRCODE -ne 0 ) && $i -lt 60 ]]; do
	sleep 1
	i=$(($i+1))
	DBSTATUS=$(/opt/mssql-tools18/bin/sqlcmd -C -h -1 -t 1 -U sa -P $MSSQL_SA_PASSWORD -Q "SET NOCOUNT ON; Select SUM(state) from sys.databases")
	ERRCODE=$?
done

if [[ $DBSTATUS -ne 0 || $ERRCODE -ne 0 ]]; then 
	echo "SQL Server took more than 60 seconds to start up or one or more databases are not in an ONLINE state"
	exit 1
fi

# Run the update schema scripts as necessary.
current=`cat /var/opt/mssql/schema.version`
for entry in /usr/config/updates/*
do
	index=${entry:20:4}
	test=${entry:25:4}
	if [[ $index -gt $current ]]; then
		if [[ $test != "TEST" || $POPULATE_TEST_DATA = "Y" ]]; then
			echo "Running update: $index"
			/opt/mssql-tools18/bin/sqlcmd -C -S localhost -U sa -P $MSSQL_SA_PASSWORD -d master -i $entry
		else
			echo "Skipping test update: $index"
		fi
		current=$index
	fi
done

echo $current > /var/opt/mssql/schema.version