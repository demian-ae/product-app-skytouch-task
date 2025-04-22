# Product App

A simple full-stack CRUD application for product management using **Spring Boot**, **Angular**, **RabbitMQ**, and **PostgreSQL**. This project demonstrates asynchronous communication between microservices using a message queue.

## 🚀 Technologies Used

- **Backend:** Java, Spring Boot (multi-module)
- **Frontend:** Angular
- **Messaging:** RabbitMQ
- **Database:** PostgreSQL
- **Containerization:** Docker, Docker Compose

---

## ⚙️ Requirements

- [Java 17+](https://sdkman.io/usage)
- [Maven 3+](https://sdkman.io/sdks#maven)
- [Node.js 18+](https://nodejs.org/)
- [Angular CLI](https://angular.io/cli)
- Podman & Podman Compose or Docker & Docker Compose 

---

## 📦 Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/demian-ae/product-app-skytouch-task
cd product-app-skytouch-task
```

### 2. Configure Environment Variables
Inside the product-app-back, edit the .env file with your desired config:

```dotenv 
DB_HOST=localhost
DB_PORT=5433
DB_NAME=product_app
DB_USERNAME=postgres
DB_PASSWORD=12345

RABBITMQ_HOST=localhost
RABBITMQ_PORT=5673
RABBITMQ_MANAGEMENT_PORT=15673
RABBITMQ_EXCHANGE=product_exchange
RABBITMQ_QUEUE=product_queue
RABBITMQ_ROUTING_KEY=product_routing_key
```

Then, export the enviroment variables with: 
```bash
export $(grep -v '^#' .env | xargs)
```

### 3. Build the app
```bash
mvn clean package
```

### 4. Start the containers
Inside the product-app-back, use `podman-compose` or `docker-compose`:
```bash
podman-compose up -d
```

## Frontend

### 1. Build the Angular front end
```bash
cd product-app-front
npm install
ng build
```

This will generate a dist/product-app-front/ folder.

### 2. Copy Frontend Build to Backend Static Resources
```bash 
cp -r dist/product-app-front/browser/* ../product-app-back/product-management/src/main/resources/static/
```
This allows Spring Boot to serve the frontend as static content.

### 3. Run the backend 
In the `product-app-back` :
```bash
mvn clean package
```

Then, **run the two spring boot applications.** 

## Access the app
The app will be available at `localhost:8080`

## 📚 Notes
Make sure the frontend build is copied every time changes are made in the Angular app.

## 🙌 Contributing
1. Fork the repo

2. Create your feature branch (git checkout -b feature/your-feature)

3. Commit your changes

4. Push to the branch

5. Open a Pull Request