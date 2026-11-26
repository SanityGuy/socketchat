# SocketChat
A lightweight TCP-based chat application built with Java and JavaFX. SocketChat provides a desktop interface for hosting chat servers and connecting multiple clients through an organized, tab-based workspace.

## Overview
SocketChat is a desktop chat application designed to explore Java networking, TCP socket communication, multithreading, and JavaFX application development.
You can start a server, connect multiple clients, and communicate in real time, all from the same application window.

## Features

- **TCP communication:** Client-server messaging using Java sockets.
- **Server hosting:** Start and stop a chat server directly from the desktop interface.
- **Multiple connections:** Support multiple connected clients.
- **Real-time messaging:** Messages are broadcast to connected clients.
- **Server dashboard:** View server activity and connected users.
- **Multiple workspace tabs:** Manage server and client sessions independently.
- **Username validation:** Reject empty, excessively long, or duplicate usernames.
- **Message validation:** Ignore empty messages and enforce a message-length limit.
- **Modern interface:** Dark navy and blue JavaFX styling.
- **Maven build system:** Dependency management and executable JAR packaging.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 27 | Main programming language |
| JavaFX | Desktop graphical interface |
| TCP Sockets | Network communication |
| Maven | Build and dependency management |
| CSS | JavaFX interface styling |
| Multithreading | Concurrent client handling |

## Project Structure

```text
SocketChat/
├── src/
│   └── main/
│       ├── java/
│       │   └── me/
│       │       └── souyandev/
│       │           ├── Main.java
│       │           ├── network/
│       │           │   ├── Server.java
│       │           │   ├── Client.java
│       │           │   └── ClientHandler.java
│       │           └── ui/
│       │               ├── SocketChatApp.java
│       │               ├── MainMenu.java
│       │               ├── ServerView.java
│       │               └── ClientView.java
│       └── resources/
│           └── styles/
│               └── style.css
├── pom.xml
└── README.md
```

## Requirements

Before building SocketChat, make sure you have:

- Java Development Kit (JDK) 27
- Apache Maven
- Git (optional, for cloning the repository)

Check your installed versions:

```bash
java -version
mvn -version
```

## Getting Started

### 1. Clone the repository

```bash
git clone <YOUR_REPOSITORY_URL>
```

### 2. Enter the project directory

```bash
cd SocketChat
```

### 3. Build the application

```bash
mvn clean package
```

### 4. Run the application

```bash
java -jar target/SocketChat-1.0-SNAPSHOT.jar
```

Alternatively, launch directly through the JavaFX Maven plugin:

```bash
mvn clean javafx:run
```

## Usage

### Starting a Server

1. Launch SocketChat.
2. Open the server option from the main menu.
3. Enter a port number, such as `5000`.
4. Start the server.
5. Monitor server activity and connected users from the dashboard.

### Connecting a Client

1. Open the client option from the main menu.
2. Enter a username.
3. Enter the server IP address.
4. Enter the server port.
5. Connect to the server.
6. Start exchanging messages.

For testing on one computer, use:

```text
Host: 127.0.0.1
Port: 5000
```

You can open multiple client tabs to test simultaneous connections.

## Networking Architecture

SocketChat follows a client-server architecture.

```text
             SocketChat Server
                    |
          ---------------------
          |         |         |
       Client 1  Client 2  Client 3
          |         |         |
          ---------------------
             TCP Communication
```

The server accepts incoming TCP connections and creates a separate client handler for each connection.

Each handler processes incoming messages and forwards them through the server's broadcast mechanism.

## Current Limitations

SocketChat is an early development project. Some features are not yet implemented.

- Client-side connected-user synchronization is not implemented.
- Communication is not encrypted.
- There is no user authentication.
- There is no persistent message history.
- There is no built-in public server discovery.
- The executable JAR is not a native installer and does not bundle a Java runtime.

Do not expose an unauthenticated, unencrypted server to untrusted networks.

## Roadmap

- [ ] Implement a structured network protocol.
- [ ] Synchronize connected-user lists with clients.
- [ ] Improve connection lifecycle management.
- [ ] Add connection timeouts and limits.
- [ ] Introduce authentication.
- [ ] Add encrypted communication.
- [ ] Add message timestamps.
- [ ] Improve error handling and logging.
- [ ] Add automated networking tests.
- [ ] Create native application packages using `jpackage`.

## Contributing

Contributions, suggestions, and bug reports are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Make your changes.
4. Test your implementation.
5. Submit a pull request.

## License

No license has been specified yet. All rights are reserved unless a license is added to this repository.

---

**SocketChat**

Built with Java. Powered by TCP. Designed for learning and experimentation.