# REST API Documentation

## Base URL

```text
http://localhost:8080
```

### Register User

```http
POST /api/users/register
Content-Type: application/json

{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Response:** `201 Created` with user data

### Login

```http
POST /api/users/login
Content-Type: application/json

{
  "usernameOrEmail": "string",
  "password": "string"
}
```

**Response:** `200 OK` with JWT token

### Get User

```http
GET /api/users/{userId}
```

**Response:** `200 OK` with user details

### Check Username

```http
GET /api/users/exists?username={username}
```

**Response:** `200 OK` with `{"exists": boolean}`

### Create Moosage

```http
POST /api/moosages
Authorization: Bearer {token}
Content-Type: application/json

{
  "content": "string",
  "authorId": "uuid"
}
```

**Response:** `201 Created` with moosage data

### Get All Moosages

```http
GET /api/moosages
```

**Response:** `200 OK` with array of moosages

### Get Moosage by ID

```http
GET /api/moosages/{moosageId}
```

**Response:** `200 OK` with moosage details

### Delete Moosage

```http
DELETE /api/moosages/{moosageId}
Authorization: Bearer {token}
```

**Response:** `204 No Content`

### Like Moosage

```http
POST /api/moosages/{moosageId}/like
Authorization: Bearer {token}
```

**Response:** `200 OK` with updated likes count

## Authentication

Include JWT token in header for protected endpoints:

```text
Authorization: Bearer {token}
```

## Error Responses

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Error description"
}
```

Common status codes: `400` (Bad Request), `401` (Unauthorized), `404` (Not Found), `500` (Server Error)
