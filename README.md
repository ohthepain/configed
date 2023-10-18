# Configed

Config Editor Backend

Stores config records in memory. Each record has:
● memberName, format ^[a-z0-9]+([-\.][a-z0-9]+)*$
● maxConnections, integer in range 0-32
● Status, [Active, Suspended, Decommissioned]

## How to build and run

./gradlew bootRun

Note that I have only tested on macOS. I have left the windows files in the repo untested.

## API Endpoints

### Create a Record

**Endpoint:** `POST /api/create-record`

- **Parameters:**
  - `param` (String, required): The parameter value.
  - `maxConnections` (Integer, required): The maximum connections allowed.
  - `status` (String, required): The status of the record (e.g., Active, Suspended, Decommissioned).

- **Description:** Creates a new configuration record.

### Update a Record

**Endpoint:** `PUT /api/update-record`

- **Parameters:**
  - `param` (String, required): The parameter value to identify the record.
  - `maxConnections` (Integer, optional): The new maximum connections allowed.
  - `status` (String, optional): The new status of the record (e.g., Active, Suspended, Decommissioned).

- **Description:** Updates an existing configuration record identified by `param`.

### List Records

**Endpoint:** `GET /api/list`

- **Description:** Retrieves a list of all configuration records.

## Example curl commands:
curl -X POST http://localhost:8080/record\?memberName\=doubledot\&maxConnections\=20\&status\=Active
curl -X POST http://localhost:8080/record\?memberName\=doubledot2\&maxConnections\=20\&status\=Decommisioned
curl -X POST http://localhost:8080/record\?memberName\=doubledot3\&maxConnections\=20\&status\=Suspended
curl -X GET http://localhost:8080/records
curl -X PUT http://localhost:8080/record\?memberName\=doubledot\&maxConnections\=5\&status\=Suspended
