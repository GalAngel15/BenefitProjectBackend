# Benefit App - Integrative Project 2024B

This repository contains the code and resources for the Benefit App Project, developed by our team at Afeka College.
The project aims to provide users with a comprehensive platform to manage benefits from various consumer clubs and stores.

## Team Members and Roles:
- **Gal Angel** - Team Leader
- **Yahav Ler** - SCRUM Master
- **Yam Horin** - QA Engineer / Product Owner
- **Diana Gurvits** - System Architect / Technical Writer
- **Shaked Ben Guy** - DevOps
- **Yarden Krispel** - DBA
- **Alon Bitran** - UIX Engineer

## Project Overview
The Benefit App consists of two main components: the server-side component called "SuperApp" and the client application known as "MiniApp". The MiniApp includes both a Java client and an Android client. The app is designed to allow users to browse and explore points of interest, manage their benefits, and retrieve data from the SuperApp server, which communicates with the database.

### Key Features:
- **User Management**: Users can create accounts, log in, and manage their personal information.
- **Benefit Tracking**: Users can track and manage benefits from different consumer clubs and stores.
- **Points of Interest**: Users can browse and explore various points of interest provided by the MiniApp.
- **Data Synchronization**: Real-time data synchronization between the MiniApp clients and the SuperApp server.

## Project Structure

### 1. Server-Side (SuperApp)
The server-side component handles all the backend functionalities, including user authentication, benefit management, and communication with the database. It is built using Java and Spring Boot and deployed using Docker for containerization.

- **Controllers**: Handle HTTP requests and define API endpoints.
- **Services**: Contain the business logic of the application.
- **Repositories**: Interact with the database to perform CRUD operations.
- **Models**: Define the data structures and entities.

### 2. Client-Side (MiniApp)
The client-side component includes both a Java client and an Android client. These clients allow users to interact with the system, browse points of interest, and manage their benefits.

- **Java Client**: Built using JavaFX for the user interface.
- **Android Client**: Built using Java and Android Studio for mobile interaction.
- **APIs**: Communicate with the SuperApp server to fetch and update data.
- **Views**: Define the user interface and user experience.

### Technologies Used
- **Java**: Core language for both server-side and client-side development.
- **Spring Boot**: Framework for building the server-side application.
- **Docker**: Containerization of the server-side application for easy deployment.
- **JavaFX**: Framework for building the desktop client application.
- **Android Studio**: IDE for building the Android client application.
- **H2 Database**: In-memory database for development and testing.
- **Git**: Version control system for managing the project codebase.

## Installation

### Prerequisites
- Java JDK
- Docker
- Spring Tool Suite (STS) or IntelliJ IDEA
- Android Studio (for Android client)

### Docker Installation
1. Visit the official [Docker website](https://www.docker.com/).
2. Choose the appropriate Docker version for your operating system.
3. Download and run the installer.
4. Follow the installation instructions and ensure Docker is running on your computer.

### Accessing the Database
Access the H2 console via the link: [http://localhost:8085/h2-console](http://localhost:8085/h2-console).

### Setting Up the Workspace
1. Download Spring Tool Suite from [Spring Tool Suite](https://spring.io/tools/).
2. Install the appropriate version for your operating system.
3. Launch Spring Tool Suite and select a workspace directory.

### Cloning the Repository
```sh
git clone [repository_url]
