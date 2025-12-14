# EduStocks

A stock trading learning platform that lets you practice with virtual currency. Built with Spring Boot and React.

## Features

*   **Virtual Trading**: Practice with $10,000 in virtual currency.
*   **Real Market Data**: Fetches real-time stock prices.
*   **Interactive Lessons**: Structured modules to learn trading basics.
*   **AI Trainer**: Integrated AI assistant to answer trading questions.
*   **Progress Tracking**: XP system and leveling.

## Tech Stack

*   **Backend**: Java 21, Spring Boot 3.2
*   **Frontend**: React 18, TypeScript, Tailwind CSS
*   **Database**: Firebase Firestore
*   **Auth**: Firebase Authentication

## Getting Started

### Prerequisites

*   Java 17+
*   Node.js 18+
*   Maven

### Setup

1.  **Clone the repo**
    ```bash
    git clone https://github.com/Amogh9929/EduStocks.git
    ```

2.  **Backend Setup**
    *   Place your `firebase-service-account.json` in `backend/src/main/resources/`.
    *   Configure `application.properties` with your Firebase project ID.
    *   Run:
        ```bash
        cd backend
        mvn spring-boot:run
        ```

3.  **Frontend Setup**
    *   Create a `.env` file in `frontend/` with your Firebase config (see `.env.example`).
    *   Run:
        ```bash
        cd frontend
        npm install
        npm start
        ```

## License

MIT
