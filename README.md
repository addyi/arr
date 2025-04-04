# Arr 🏴‍☠️

## Building & Running

- `docker-compose up --build`: This command will build the Docker images and start the containers defined in the `docker-compose.yml` file.
- `docker-compose up postgres`: This command will start the PostgreSQL database container.
- `docker-compose up app --build`: This command will build the application image and start the application container.

## Setting Up Environment Variables

To run the project, you need to set up the following environment variables:

- `APP_PORT`: The port on which the application will run.
- `DB_HOST`: The hostname of the PostgreSQL database e.g. localhost or the `container_name` of the database.
- `DB_NAME`: The name of the database.
- `PG_USER`: The username for the PostgreSQL database.
- `PG_PASSWORD`: The password for the PostgreSQL database.

You can set these variables in a `.env` file in the root directory of the project:

```dotenv
APP_PORT=8080
DB_HOST=your_database_host
DB_NAME=your_database_name
PG_USER=your_postgres_user
PG_PASSWORD=your_postgres_password
```

You may create a `.env.local` file to override the default `.env` file.
This is useful for local development or testing environments outside a Docker container.