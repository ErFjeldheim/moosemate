# REST API Documentation

## Base URL

```text
http://localhost:8080/api
```

## Authentication Endpoints

### Test API

```http
GET /api/auth/test
```

**Response:** `200 OK`

```json
{
  "success": true,
  "message": "Moosemate REST API is working!",
  "data": "API Ready"
}
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
  "message": "string",
  "data": {
    "username": "string",
    "email": "string",
    "sessionToken": "string",
    "userID":"string"
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

### Verify Session

```http
GET /api/auth/verify
Session-Token: {sessionToken}
```

**Response:** `200 OK`

```json
{
  "success": true,
  "message": "Session valid",
  "data": true
}
```

**Error Response:** `401 Unauthorized` if session is invalid

```json
{
  "success": false,
  "message": "Invalid session",
  "data": null
}
```

## Moosage Endpoints

### Create Moosage

```http
POST /api/moosages
Content-Type: application/json
Session-Token: {sessionToken}

{
  "content": "string"
}
```

**Response:** `201 Created`

```json
{
  "success": true,
  "message": "Moosage created successfully",
  "data": {
    "id": 1,
    "content": "string",
    "authorId": "uuid",
    "authorUsername": "string",
    "time": "2025-11-06T12:00:00",
    "likedByUserIds": [],
    "edited": false
  }
}
```

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
      "id": 1,
      "content": "string",
      "authorId": "uuid",
      "authorUsername": "string",
      "time": "2025-11-06T12:00:00",
      "likedByUserIds": [],
      "edited": false
    },
    {
        // Another mossage
    }
  ]
}
```

### Get Moosage by ID

```http
GET /api/moosages/{id}
Content-Type: application/json
Session-Token: {sessionToken}
```

**Response:** `200 OK`

```json
{
  "success": true,
  "message": "Moosage found",
  "data": {
    "id": 1,
    "content": "string",
    "authorId": "uuid",
    "authorUsername": "string",
    "time": "2025-11-06T12:00:00",
    "likedByUserIds": [],
    "edited": false
  }
}
```

**Error Response:** `404 Not Found` if moosage doesn't exist

### Update Moosage

```http
PUT /api/moosages/{id}
Content-Type: application/json
Session-Token: {sessionToken}

{
  "content": "string"
}
```

**Response:** `200 OK` with updated moosage data

```json
{
  "success": true,
  "message": "Moosage found",
  "data": {
    "id": 1,
    "content": "string",
    "authorId": "uuid",
    "authorUsername": "string",
    "time": "2025-11-06T12:00:00",
    "likedByUserIds": [],
    "edited": false
  }
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid session
- `403 Forbidden` - Not the author of the moosage
- `404 Not Found` - Moosage doesn't exist

### Delete Moosage

```http
DELETE /api/moosages/{id}
Content-Type: application/json
Session-Token: {sessionToken}
```

**Response:** `200 OK` with deletion confirmation

**Error Responses:**

- `401 Unauthorized` - Invalid session
- `403 Forbidden` - Not the author of the moosage
- `404 Not Found` - Moosage doesn't exist

### Toggle Like on Moosage

```http
POST /api/moosages/{id}/like
Content-Type: application/json
Session-Token: {sessionToken}
```

**Response:** `200 OK` with updated moosage data (including new like count)

```json
{
  "success": true,
  "message": "Moosage found",
  "data": {
    "id": 1,
    "content": "string",
    "authorId": "uuid",
    "authorUsername": "string",
    "time": "2025-11-06T12:00:00",
    "likedByUserIds": [
        // Updated with userId who liked it
    ],
    "edited": false
  }
}
```

## Authentication

All protected endpoints require a session token in the header:

```text
Session-Token: {sessionToken}
```

The session token is generated after successful login and should be included in all requests to the api-server in order to authenticate the user.

## Error Responses

All error responses follow this structure:

```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

**HTTP Status Codes:**

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid input or validation error
- `401 Unauthorized` - Invalid credentials or session token
- `403 Forbidden` - User not authorized to perform this action
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists (e.g., duplicate username)
- `500 Internal Server Error` - Server error
 