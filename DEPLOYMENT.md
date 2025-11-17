# Deployment Guide

## Prerequisites

Before deploying EduStocks, ensure you have:

- Docker and Docker Compose installed
- Firebase project created and configured
- API keys for:
  - Firebase (service account)
  - OpenAI (for AI Trainer)
  - Stock API (Yahoo Finance or Alpha Vantage)

## Environment Variables

### Backend (.env)
```bash
FIREBASE_PROJECT_ID=your-project-id
OPENAI_API_KEY=your-openai-key
STOCK_API_KEY=your-stock-api-key
SPRING_PROFILES_ACTIVE=prod
```

### Frontend (.env.production)
```bash
REACT_APP_FIREBASE_API_KEY=your-firebase-api-key
REACT_APP_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
REACT_APP_FIREBASE_PROJECT_ID=your-project-id
REACT_APP_FIREBASE_STORAGE_BUCKET=your-project.appspot.com
REACT_APP_FIREBASE_MESSAGING_SENDER_ID=your-sender-id
REACT_APP_FIREBASE_APP_ID=your-app-id
REACT_APP_API_URL=https://your-backend-url.com/api
```

## Docker Deployment

### Quick Start
```bash
# Build and run all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Production Build
```bash
# Build images
docker-compose build

# Run in detached mode
docker-compose up -d

# Check status
docker-compose ps
```

## AWS Deployment

### Using AWS Elastic Beanstalk

1. **Install EB CLI**
```bash
pip install awsebcli
```

2. **Initialize EB**
```bash
cd backend
eb init
```

3. **Create Environment**
```bash
eb create edustocks-prod
```

4. **Deploy**
```bash
eb deploy
```

5. **Set Environment Variables**
```bash
eb setenv FIREBASE_PROJECT_ID=xxx OPENAI_API_KEY=xxx
```

### Using AWS ECS (Fargate)

1. **Build and Push Images**
```bash
# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

# Build and tag
docker build -t edustocks-backend ./backend
docker tag edustocks-backend:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/edustocks-backend:latest

# Push
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/edustocks-backend:latest
```

2. **Create ECS Task Definition**
3. **Create ECS Service**
4. **Configure Load Balancer**

## Heroku Deployment

### Backend
```bash
cd backend

# Create Heroku app
heroku create edustocks-backend

# Set environment variables
heroku config:set FIREBASE_PROJECT_ID=xxx
heroku config:set OPENAI_API_KEY=xxx

# Deploy
git push heroku main
```

### Frontend (Vercel)
```bash
cd frontend

# Install Vercel CLI
npm i -g vercel

# Deploy
vercel

# Production deployment
vercel --prod
```

## Health Checks

After deployment, verify:

### Backend
```bash
curl https://your-backend-url.com/api/auth/health
# Expected: "OK"
```

### Frontend
```bash
curl https://your-frontend-url.com
# Expected: 200 OK
```

## Monitoring

### Application Metrics
- Set up New Relic or Datadog
- Configure alerts for:
  - Error rates > 1%
  - Response time > 2s
  - CPU usage > 80%
  - Memory usage > 80%

### Logging
- Centralized logging with CloudWatch/Papertrail
- Log levels: ERROR, WARN, INFO, DEBUG
- Log rotation and retention policies

## Scaling

### Horizontal Scaling
```bash
# Docker Compose
docker-compose up --scale backend=3

# AWS ECS
aws ecs update-service --service edustocks --desired-count 3
```

### Vertical Scaling
- Increase container resources in docker-compose.yml
- Update instance type in AWS

## Backup Strategy

### Firebase Firestore
- Automated daily backups via Firebase Console
- Export to Cloud Storage for archival

### Application Data
- Database snapshots before major releases
- Configuration backups in version control

## SSL/TLS Setup

### Using Let's Encrypt
```bash
# Install Certbot
sudo apt-get install certbot python3-certbot-nginx

# Get certificate
sudo certbot --nginx -d your-domain.com

# Auto-renewal
sudo certbot renew --dry-run
```

## Rollback Procedure

### Docker
```bash
# Stop current version
docker-compose down

# Pull previous image
docker pull edustocks-backend:previous-tag

# Update docker-compose.yml with previous tag
# Restart
docker-compose up -d
```

### Heroku
```bash
heroku releases
heroku rollback v23
```

## Troubleshooting

### Common Issues

**Issue: Container won't start**
```bash
# Check logs
docker logs container-name

# Check health
docker inspect container-name
```

**Issue: Database connection failed**
- Verify Firebase credentials
- Check security rules
- Ensure network connectivity

**Issue: High memory usage**
- Check for memory leaks
- Increase container limits
- Review application logs

## Performance Optimization

### CDN Setup
- Use CloudFlare or AWS CloudFront
- Cache static assets
- Enable Gzip compression

### Database Optimization
- Index frequently queried fields
- Implement caching layer (Redis)
- Use connection pooling

## Security Checklist

- [ ] All secrets in environment variables
- [ ] HTTPS enabled
- [ ] CORS properly configured
- [ ] Security headers set
- [ ] Rate limiting enabled
- [ ] Input validation on all endpoints
- [ ] Firebase security rules configured
- [ ] Regular dependency updates
- [ ] Automated security scans

## Cost Optimization

### Firebase
- Monitor Firestore read/write operations
- Use caching to reduce API calls
- Implement pagination

### Cloud Services
- Use auto-scaling to match demand
- Schedule non-critical tasks during off-peak hours
- Review and optimize resource allocation

---

**Need Help?** Contact: agarwalamogh8@gmail.com
