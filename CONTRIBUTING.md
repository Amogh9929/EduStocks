# Contributing to EduStocks

First off, thank you for considering contributing to EduStocks! 🎉

## Code of Conduct

This project and everyone participating in it is governed by common sense and mutual respect. By participating, you are expected to uphold this code.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates. When you create a bug report, include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps to reproduce the problem**
- **Provide specific examples**
- **Describe the behavior you observed and what you expected**
- **Include screenshots if possible**
- **Include your environment details** (OS, Browser, Node version, Java version)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, include:

- **Use a clear and descriptive title**
- **Provide a detailed description of the suggested enhancement**
- **Explain why this enhancement would be useful**
- **List any similar features in other applications if applicable**

### Pull Requests

1. **Fork the repo** and create your branch from `main`
2. **Make your changes** following the code style guidelines
3. **Test your changes** thoroughly
4. **Update documentation** if needed
5. **Ensure the test suite passes**
6. **Make sure your code lints**
7. **Issue that pull request!**

## Development Process

### Setting Up Your Development Environment

1. Fork and clone the repository
2. Install dependencies:
   ```bash
   # Frontend
   cd frontend
   npm install
   
   # Backend
   cd backend
   mvn clean install
   ```
3. Set up environment variables (see README.md)
4. Create a new branch: `git checkout -b feature/my-new-feature`

### Coding Standards

#### Frontend (React/TypeScript)
- Use TypeScript for type safety
- Follow React best practices and hooks guidelines
- Use functional components over class components
- Use meaningful variable and function names
- Keep components small and focused
- Write JSDoc comments for complex functions
- Use Prettier for code formatting

#### Backend (Java/Spring Boot)
- Follow Java naming conventions
- Use meaningful variable and method names
- Write Javadoc comments for public methods
- Keep methods focused and single-purpose
- Use Lombok annotations to reduce boilerplate
- Follow Spring Boot best practices
- Write unit tests for new features

### Commit Messages

- Use the present tense ("Add feature" not "Added feature")
- Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit the first line to 72 characters or less
- Reference issues and pull requests liberally after the first line

Examples:
```
Add AI trainer question generation
Fix portfolio calculation bug in PortfolioService
Update README with deployment instructions
```

### Testing

- Write tests for new features
- Ensure all existing tests pass
- Run tests locally before submitting PR:
  ```bash
  # Frontend
  npm test
  
  # Backend
  mvn test
  ```

### Documentation

- Update the README.md if you change functionality
- Add JSDoc/Javadoc comments for new functions/methods
- Update API documentation if you add/modify endpoints
- Add comments for complex logic

## Project Structure

Please maintain the existing project structure:

```
frontend/
  src/
    components/    # Reusable UI components
    pages/         # Page-level components
    services/      # API and business logic
    contexts/      # React contexts
    
backend/
  src/main/java/com/edustocks/
    controller/    # REST endpoints
    service/       # Business logic
    model/         # Data models
    repository/    # Data access
    config/        # Configuration
```

## Need Help?

Don't hesitate to ask for help! You can:
- Open an issue with your question
- Start a discussion in GitHub Discussions
- Reach out to the maintainers

## Recognition

Contributors will be recognized in the README.md file.

Thank you for contributing to EduStocks! 🚀📈
