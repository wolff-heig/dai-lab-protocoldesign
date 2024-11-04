# Protocol Specification for Client-Server Application

## 1. Overview

This protocol defines the communication between a client and a server for performing basic arithmetic operations. The server supports four operations: addition, subtraction, multiplication, and division. The client sends a request with an operation, and the server processes the request and returns the result.

The protocol includes a welcome message when the client connects, and a closing message before the connection is terminated, making interactions clearer for the client.

## 2. Transport Layer Protocol

- **Protocol**: TCP
- **IP Address**: The client must know the server’s IP address.
- **Port**: The server listens on TCP port 5000.
- **Connection Initiation**: The client initiates the connection.
- **Connection Termination**: The server closes the connection after sending the result or an error message, and it sends a closing message before terminating.

This use of a TCP connection ensures reliable delivery of messages between the client and server, guaranteeing that all requests and responses are accurately transmitted.

## 3. Messages

### Client Messages

- **Request Format**: `<COMMAND> <ARG1> <ARG2>`
    - **COMMAND**: Specifies the arithmetic operation. Valid commands include:
        - `ADD` - for addition
        - `SUB` - for subtraction
        - `MUL` - for multiplication
        - `DIV` - for division
    - **ARG1** and **ARG2**: Integer values for the operation.

  **Examples**:
    - `ADD 15 10` - requests the addition of 15 and 10.
    - `SUB 20 5` - requests the subtraction of 5 from 20.
    - `MUL 3 7` - requests the multiplication of 3 and 7.
    - `DIV 10 2` - requests the division of 10 by 2.

**Note**: Each message sent by the client should end with a newline character (`\n`) to indicate the end of the message.

### Server Messages

The server responds with either a result, an error message, or a system message.

- **Welcome Message**: `WELCOME Perform an operation by sending: <COMMAND> <ARG1> <ARG2>`
- **Response Format**:
    - `<RESULT>` for successful operations.
    - `ERROR <DESCRIPTION>` for error responses.
- **Closing Message**: `GOODBYE Connection closing`

#### Server Responses:
- **Success**:
    - The server sends the computed result as a single line.

  **Example**: `25` for a successful addition.

- **Error Cases**:
    - **Invalid Command**: `ERROR Invalid Command`
    - **Division by Zero**: `ERROR Division by Zero`
    - **Invalid Arguments**: `ERROR Invalid Arguments`

## 4. Specific Elements

### Error Handling

The server validates each request and responds with a specific error message if there’s an issue:

1. **Invalid Command**: If the command is not recognized, respond with `ERROR Invalid Command`.
2. **Division by Zero**: For a division by zero attempt, respond with `ERROR Division by Zero`.
3. **Invalid Arguments**: For non-numeric or incomplete arguments, respond with `ERROR Invalid Arguments`.

All error messages begin with `ERROR`, followed by a description, allowing the client to handle errors effectively.

### Extensibility

The protocol can be extended with additional commands, following the format `<COMMAND> <ARG1> <ARG2>`. Unsupported commands result in `ERROR Invalid Command`, ensuring compatibility with newer commands.

## 5. Example Dialogs

### Generic Client-Server Mechanism

<img src="images/protocol-Generic-Client-Server-Mechanism.svg" alt="Generic Client-Server Mechanism" height="400px">

*Sequence diagram showing the general mechanism of client-server interaction.*

### Successful Operation Example (Addition)

**Dialog**: The client connects, performs an addition, receives the result, and closes the connection.

**Sequence**:
1. **Client**: Connects to server on port 5000.
2. **Server**: `WELCOME Perform an operation by sending: <COMMAND> <ARG1> <ARG2>`
3. **Client**: `ADD 10 5`
4. **Server**: `15`
5. **Server**: `GOODBYE Connection closing`
6. **Client**: Disconnects.

<img src="images/protocol-Successful-Operation-Example.svg" alt="Successful Operation Example (Addition)" height="400px">

*Sequence diagram illustrating a successful calculation scenario with the addition command.*

### Error Scenarios

Although diagrams for each error case are not required, the following error examples clarify how the server responds to common issues.

#### Error Scenario: Division by Zero

**Dialog**: The client attempts to divide by zero, receives an error message, and the server closes the connection.

**Sequence**:
1. **Client**: Connects to server.
2. **Server**: `WELCOME Perform an operation by sending: <COMMAND> <ARG1> <ARG2>`
3. **Client**: `DIV 10 0`
4. **Server**: `ERROR Division by Zero`
5. **Server**: `GOODBYE Connection closing`
6. **Client**: Disconnects.

<img src="images/protocol-Error-Scenario-Example.svg" alt="Error Scenario Example (Division by Zero)" height="400px">

*Sequence diagram showing the error handling for a division by zero.*

#### Error Scenario: Invalid Command

**Dialog**: The client sends an unsupported command, receives an error, and the server closes the connection.

**Sequence**:
1. **Client**: Connects to server.
2. **Server**: `WELCOME Perform an operation by sending: <COMMAND> <ARG1> <ARG2>`
3. **Client**: `MOD 10 3`
4. **Server**: `ERROR Invalid Command`
5. **Server**: `GOODBYE Connection closing`
6. **Client**: Disconnects.

#### Error Scenario: Invalid Arguments

**Dialog**: The client sends an operation with invalid arguments, receives an error, and the server closes the connection.

**Sequence**:
1. **Client**: Connects to server.
2. **Server**: `WELCOME Perform an operation by sending: <COMMAND> <ARG1> <ARG2>`
3. **Client**: `ADD ten 5`
4. **Server**: `ERROR Invalid Arguments`
5. **Server**: `GOODBYE Connection closing`
6. **Client**: Disconnects.
