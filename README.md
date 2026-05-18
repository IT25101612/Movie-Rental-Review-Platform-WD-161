# Member 4 — Review & Rating Management

## Your Contribution
You are responsible for the **Review & Rating Management** feature.

### Your Key Files
**Backend:**
- `backend/src/main/java/com/example/movierental/entity/Review.java`
- `backend/src/main/java/com/example/movierental/repository/ReviewRepository.java`
- `backend/src/main/java/com/example/movierental/service/ReviewService.java`
- `backend/src/main/java/com/example/movierental/service/impl/ReviewServiceImpl.java`
- `backend/src/main/java/com/example/movierental/controller/ReviewController.java`

**Frontend:**
- `frontend/pages/reviews.html`
- `frontend/js/reviews.js`

### API Endpoints You Own
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reviews` | Add review |
| GET | `/api/reviews` | Get all reviews |
| GET | `/api/reviews/{id}` | Get review by ID |
| PUT | `/api/reviews/{id}` | Update review |
| DELETE | `/api/reviews/{id}` | Delete review |

## Shared Files (do NOT modify these)
Everything else is shared infrastructure kept identical across all repositories.

## How to Run
1. Make sure Java 17+ and Maven are installed
2. In the `backend/` folder run: `./mvnw spring-boot:run`
3. Open `frontend/login.html` in your browser
4. Default admin: `admin@movierental.com` / `Admin@123`
