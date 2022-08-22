# Redpanda Gaming Industry Data Report 2022

This repository contains the five examples projects discussed in the Gaming Industry Report 2022, from Redpanda Data.

The examples are named according to the order they have been discussed in report. Each example contains a Docker Compose project that you can quickly start by typing `docker compose up -d` in the terminal.

The example projects are as follows.

- `1-analytics` - Contains the real-time gaming data analytics solution.
- `2-leaderboard` - Contains the real-time gaming leaderboard solution.
- `3-ads` - Contains the real-time in-game personalization solution.
- `4-chat` - Contains the real-time chat application.
- `5-matchmaker` - Contains the real-time player matching solution.

## Prerequisites

Before trying to run these examples on your local machine, ensure it meets the following requirements.

- Install Docker and Docker Compose.
- Allocate at least 6 CPU cores and 8 GB of RAM for the Docker daemon.
- Install a JDK version 8 or higher and set the JAVA_HOME environment variable.
- Install Python 3.
- Install the Maven build tool.
- Optionally install the Quarkus CLI.

## Running the examples

To run a specific example, navigate to the example folder on a terminal, and type:

```bash
docker compose up -d
```

Then follow the solution specific instructions mentioned in the relevant section in the report.

## Cleaning up

Clean up the containers by:

```bash
docker compose down
```