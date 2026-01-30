# Social Media - Post Service

## Responsibilities
- Manage posts and comments
- Enforce OAuth2 scope-based authorization
- Support Social Service integration (post existence checks)

## APIs
- POST /posts
- GET /posts/{id}
- GET /posts?authorId=&authorIds=&limit=
- DELETE /posts/{id}
- POST /posts/{id}/comments
- GET /posts/{id}/comments
- GET /posts/internal/{id}/exists

## Security
- posts.read → GET endpoints
- posts.write → POST/DELETE endpoints
