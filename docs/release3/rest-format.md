# REST API Documentation

## Base URL

```text
http://localhost:8080/api
```

### Sign Up

```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Response:** `200 OK` with success message

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}
```

**Response:** `200 OK`

```json
{
  "success": true,
  "data": {
    "username": "string",
    "email": "string",
    "sessionToken": "string"
  }
}
```

### Logout

```http
POST /api/auth/logout
Content-Type: application/json
Session-Token: {sessionToken}
```

**Response:** `200 OK` with logout confirmation

### Create Moosage

```http
POST /api/moosages
Content-Type: application/json
Session-Token: {sessionToken}

{
  "content": "string"
}
```

**Response:** `201 Created` with moosage data

### Get All Moosages

```http
GET /api/moosages
Content-Type: application/json
Session-Token: {sessionToken}
```

**Response:** `200 OK` with array of moosages

```json
{
  "success": true,
  "data": [
    {
      "moosageId": 1,
      "content": "string",
      "author": {
        "userId": "uuid",
        "username": "string"
      },
      "timestamp": "2025-11-06T12:00:00",
      "likes": 0
    }
  ]
}
```

### Update Moosage

```http
PUT /api/moosages/{moosageId}
Content-Type: application/json
Session-Token: {sessionToken}

{
  "content": "string"
}
```

**Response:** `200 OK` with updated moosage data

### Delete Moosage

```http
DELETE /api/moosages/{moosageId}
Content-Type: application/json
Session-Token: {sessionToken}
```

**Response:** `200 OK` with deletion confirmation

### Toggle Like on Moosage

```http
POST /api/moosages/{moosageId}/like
Content-Type: application/json
Session-Token: {sessionToken}
```

**Response:** `200 OK` with updated moosage data (including new like count)

## Authentication

All protected endpoints require a session token in the header:

```text
Session-Token: {sessionToken}
```

The session token is obtained after successful login and should be included in all subsequent requests to protected endpoints.

## Error Responses

```json
{
  "success": false,
  "message": "Error description"
}
```

Common status codes: `400` (Bad Request), `401` (Unauthorized), `404` (Not Found), `500` (Server Error)
