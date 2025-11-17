# Architecture Documentation

## System Overview

EduStocks is a full-stack web application designed to provide an interactive stock trading learning experience. The system follows a modern three-tier architecture with clear separation of concerns.

```
┌─────────────────┐
│   Frontend      │  React + TypeScript
│   (Port 3000)   │  Tailwind CSS
└────────┬────────┘
         │ HTTP/REST
         │
┌────────▼────────┐
│   Backend       │  Spring Boot + Java 21
│   (Port 8080)   │  REST API
└────────┬────────┘
         │
    ┌────┴─────────────┬──────────────┐
    │                  │              │
┌───▼────┐      ┌──────▼─────┐  ┌───▼────────┐
│Firebase│      │Stock APIs  │  │ OpenAI API │
│Firestore│     │Yahoo Finance│  │   GPT      │
└────────┘      └────────────┘  └────────────┘
```

## Architecture Decisions

### 1. **Microservices vs Monolithic**
**Decision**: Monolithic architecture for MVP  
**Rationale**:
- Simpler deployment for small team
- Lower operational complexity
- Easier development and debugging
- Can migrate to microservices later if needed

**Tradeoffs**:
- ✅ Faster initial development
- ✅ Easier to maintain consistency
- ❌ Harder to scale individual components
- ❌ Entire app must be deployed together

### 2. **Backend Framework: Spring Boot**
**Decision**: Spring Boot 3.2.0 with Java 21  
**Rationale**:
- Mature ecosystem with extensive libraries
- Enterprise-grade security features
- Excellent Firebase integration
- Strong type safety
- Great for portfolio demonstration

**Alternatives Considered**:
- Node.js/Express: Rejected due to weaker type safety
- Django: Rejected due to team's Java expertise
- Go: Rejected due to smaller ecosystem

### 3. **Frontend Framework: React**
**Decision**: React 18.2 with TypeScript  
**Rationale**:
- Component reusability
- Large community and ecosystem
- Virtual DOM for performance
- TypeScript for type safety
- Excellent developer tools

**Alternatives Considered**:
- Vue.js: Rejected due to smaller job market
- Angular: Rejected due to steeper learning curve
- Svelte: Rejected due to smaller ecosystem

### 4. **Database: Firebase Firestore**
**Decision**: Firebase Firestore as primary database  
**Rationale**:
- NoSQL flexibility for evolving schema
- Real-time sync capabilities
- Built-in authentication integration
- Generous free tier
- Managed service (no DevOps overhead)

**Tradeoffs**:
- ✅ No server management
- ✅ Real-time capabilities
- ✅ Automatic scaling
- ❌ Vendor lock-in
- ❌ Complex queries limited
- ❌ Higher costs at scale

### 5. **Authentication: Firebase Auth**
**Decision**: Firebase Authentication  
**Rationale**:
- Battle-tested security
- Easy integration with Firestore
- Multiple auth providers support
- Built-in session management
- Industry-standard JWT tokens

### 6. **State Management: Context API**
**Decision**: React Context API (not Redux)  
**Rationale**:
- Sufficient for application complexity
- No additional dependencies
- Simpler learning curve
- Built into React

**When to Reconsider**:
- If app grows significantly
- If state management becomes complex
- If debugging becomes difficult

## System Architecture

### Backend Architecture

#### Layered Design
```
┌─────────────────────────────────┐
│      Controllers Layer          │  ← HTTP Endpoints
│  (REST API, Request Handling)   │
└────────────┬────────────────────┘
             │
┌────────────▼────────────────────┐
│       Services Layer            │  ← Business Logic
│  (Portfolio, Progress, etc.)    │
└────────────┬────────────────────┘
             │
┌────────────▼────────────────────┐
│    Repository Layer             │  ← Data Access
│  (Firebase Firestore Access)    │
└────────────┬────────────────────┘
             │
┌────────────▼────────────────────┐
│      Data Models                │  ← Domain Objects
│  (Portfolio, User, Lesson)      │
└─────────────────────────────────┘
```

#### Key Components

**Controllers** (`controller/`)
- Handle HTTP requests/responses
- Input validation
- Route mapping
- Error handling
- Return DTOs (not domain models)

**Services** (`service/`)
- Core business logic
- Transaction management
- External API calls
- Complex calculations
- Orchestrate multiple repositories

**Repositories** (`repository/`)
- Data access abstraction
- Firebase Firestore operations
- Query construction
- Data mapping

**Security** (`security/`)
- Firebase token verification
- Request authentication
- Authorization checks
- CORS configuration

### Frontend Architecture

#### Component Structure
```
src/
├── components/          # Reusable UI components
│   └── Navbar.tsx
├── pages/              # Route-level components
│   ├── Dashboard.tsx
│   ├── Trading.tsx
│   ├── Lessons.tsx
│   └── AITrainer.tsx
├── contexts/           # State management
│   └── AuthContext.tsx
├── services/           # API clients
│   └── api.ts
└── firebase/           # Firebase config
    └── config.ts
```

#### Design Patterns

**Container/Presentational Pattern**
- Pages = Smart containers
- Components = Dumb presenters
- Clear separation of concerns

**Custom Hooks**
- `useAuth()` - Authentication state
- `usePortfolio()` - Portfolio data
- `useProgress()` - User progress

**Context Providers**
- AuthContext for authentication
- Future: PortfolioContext, ThemeContext

## Data Models

### Core Entities

```typescript
// User (Firebase Auth)
interface User {
  uid: string;
  email: string;
  displayName?: string;
  photoURL?: string;
}

// Portfolio
interface Portfolio {
  userId: string;
  cash: number;
  holdings: Holding[];
  totalValue: number;
  createdAt: Date;
  updatedAt: Date;
}

// Holding
interface Holding {
  symbol: string;
  quantity: number;
  averagePrice: number;
  currentPrice: number;
  totalValue: number;
  profitLoss: number;
  profitLossPercent: number;
}

// UserProgress
interface UserProgress {
  userId: string;
  level: number;
  xp: number;
  rank: string;
  completedLessons: string[];
  achievements: string[];
  createdAt: Date;
  updatedAt: Date;
}

// Lesson
interface Lesson {
  id: string;
  title: string;
  description: string;
  level: 'beginner' | 'intermediate' | 'advanced';
  content: string;
  questions: Question[];
  xpReward: number;
}
```

## API Design

### REST Principles
- Resource-based URLs
- Standard HTTP methods (GET, POST, PUT, DELETE)
- JSON request/response format
- Stateless communication
- HTTP status codes for responses

### Example Endpoints
```
POST   /api/auth/verify           - Verify Firebase token
GET    /api/portfolio              - Get user portfolio
POST   /api/portfolio/buy          - Execute buy order
POST   /api/portfolio/sell         - Execute sell order
GET    /api/stocks                 - List all stocks
GET    /api/stocks/{symbol}        - Get stock details
GET    /api/lessons                - List all lessons
POST   /api/lessons/{id}/complete  - Mark lesson complete
GET    /api/progress               - Get user progress
POST   /api/ai-trainer/question    - Get practice question
```

### Authentication Flow
```
1. User logs in via Firebase (frontend)
2. Firebase returns ID token
3. Frontend includes token in Authorization header
4. Backend verifies token with Firebase Admin SDK
5. Backend extracts user ID from verified token
6. Backend processes request with user context
```

## Security Architecture

### Defense in Depth

**Frontend Security**
- Input validation
- XSS prevention (React built-in)
- CSRF protection
- Secure token storage
- Environment variable protection

**Backend Security**
- Firebase token verification on every request
- Input sanitization
- SQL injection prevention (NoSQL)
- Rate limiting (future)
- CORS policy
- Secure headers

**Infrastructure Security**
- HTTPS only
- Environment-based configs
- Secret management
- Firebase Security Rules
- Docker container isolation

### Firebase Security Rules
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only read/write their own data
    match /portfolios/{userId} {
      allow read, write: if request.auth != null 
        && request.auth.uid == userId;
    }
    
    match /progress/{userId} {
      allow read, write: if request.auth != null 
        && request.auth.uid == userId;
    }
    
    // Lessons are read-only for all authenticated users
    match /lessons/{lessonId} {
      allow read: if request.auth != null;
      allow write: if false;
    }
  }
}
```

## Performance Optimizations

### Frontend
- Code splitting with React.lazy()
- Image optimization
- Bundle size minimization
- Memoization with useMemo/useCallback
- Debouncing for search inputs
- Lazy loading for routes

### Backend
- Connection pooling
- Caching strategies (future)
- Async operations where possible
- Efficient database queries
- Pagination for large datasets

### Database
- Indexed fields for common queries
- Denormalization where appropriate
- Batch operations
- Optimistic updates on frontend

## Scalability Considerations

### Current Bottlenecks
1. Stock API rate limits
2. Firebase Firestore costs at scale
3. Synchronous AI calls

### Future Scaling Strategy
```
Phase 1 (Current - 1K users)
- Single region deployment
- Firebase free tier
- Basic monitoring

Phase 2 (1K - 10K users)
- Add Redis caching layer
- Implement CDN for static assets
- Database query optimization
- Add application monitoring

Phase 3 (10K - 100K users)
- Multi-region deployment
- Microservices migration
- Load balancing
- Message queue for async tasks
- Dedicated database cluster

Phase 4 (100K+ users)
- Auto-scaling infrastructure
- Distributed caching
- Database sharding
- Event-driven architecture
- Full observability stack
```

## Monitoring & Observability

### Metrics to Track
- API response times
- Error rates by endpoint
- Active users
- Database query performance
- Frontend bundle sizes
- Page load times

### Logging Strategy
```
Level    |  Use Case
---------|-----------------------------------
ERROR    |  System failures, exceptions
WARN     |  Deprecated features, high latency
INFO     |  User actions, business events
DEBUG    |  Development troubleshooting
```

### Health Checks
- `/api/auth/health` - Backend health
- Database connectivity
- External API availability
- Memory/CPU usage

## Testing Strategy

### Test Pyramid
```
       /\
      /E2E\         ← Few, critical paths
     /------\
    /  API  \       ← Moderate, API contracts
   /----------\
  / Unit Tests \    ← Many, business logic
 /--------------\
```

### Coverage Goals
- Unit Tests: 80%+ coverage
- Integration Tests: Key flows
- E2E Tests: Critical user journeys

## Deployment Architecture

### Development Environment
```
Local Machine
├── Frontend (localhost:3000)
└── Backend (localhost:8080)
    └── Firebase Emulator (localhost:9099)
```

### Production Environment
```
Cloud Platform (AWS/Heroku/Vercel)
├── Frontend (CDN + Static Hosting)
├── Backend (Container/Serverless)
└── External Services
    ├── Firebase (Auth + Firestore)
    ├── Stock API
    └── OpenAI API
```

### CI/CD Pipeline
```
Code Push → GitHub Actions
    ├── Run Tests
    ├── Build Docker Images
    ├── Security Scans
    ├── Deploy to Staging
    ├── Run E2E Tests
    └── Deploy to Production (manual approval)
```

## Technology Choices Summary

| Component | Technology | Reason |
|-----------|-----------|---------|
| Frontend Framework | React 18 | Component model, ecosystem |
| Frontend Language | TypeScript | Type safety, better DX |
| Frontend Styling | Tailwind CSS | Rapid development, consistency |
| Frontend State | Context API | Sufficient for complexity |
| Frontend Router | React Router v6 | Standard, feature-rich |
| Backend Framework | Spring Boot 3.2 | Mature, enterprise-ready |
| Backend Language | Java 21 | Type safety, performance |
| Backend Security | Spring Security | Industry standard |
| Database | Firebase Firestore | Real-time, managed service |
| Authentication | Firebase Auth | Secure, easy integration |
| Stock Data API | Yahoo Finance | Free, reliable |
| AI Service | OpenAI GPT | Best-in-class NLP |
| Build Tool (FE) | npm/webpack | Standard React tooling |
| Build Tool (BE) | Maven | Java standard |
| Containerization | Docker | Portability, consistency |
| CI/CD | GitHub Actions | Free, integrated |

## Lessons Learned

### What Worked Well
- Layered architecture made testing easier
- TypeScript caught many bugs early
- Firebase reduced DevOps complexity
- Component-based frontend enabled rapid iteration

### What We'd Do Differently
- Start with comprehensive E2E tests earlier
- Implement caching from day one
- Use GraphQL instead of REST (considering)
- Add observability sooner

### Key Takeaways
- Start with MVP, iterate based on feedback
- Good architecture pays off long-term
- User feedback is invaluable
- Documentation is essential

---

**Last Updated**: November 17, 2025  
**Version**: 1.0.0  
**Maintainer**: Amogh Agarwal
