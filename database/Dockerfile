# Use the latest MS SQL Server 2022 image.
FROM mcr.microsoft.com/mssql/server:2022-latest

# Update ubuntu.
RUN apt-get -y update && apt-get -y install

# Create a config directory.
RUN mkdir -p /usr/config
WORKDIR /usr/config

# Copy config files to new directory.
COPY . /usr/config

# Grand scripts permissions to run.
RUN chmod +x /usr/config/entrypoint.sh
RUN chmod +x /usr/config/configure-db.sh

# Run the scripts.
ENTRYPOINT ["./entrypoint.sh"]