# Protocol Specification for Client-Server Application

## 1. Overview

This protocol defines the communication between a client and a server for performing basic arithmetic operations.<br>
The server supports four operations: addition, subtraction, multiplication, and division.<br>
The client sends a request with an operation, and the server processes the request and returns the result.

Only integer values are supported for operations. Decimal or floating-point numbers are not allowed and will result in an error.

The protocol includes a welcome message from the server when the client connects, and a closing message before the 
connection is terminated, making interactions clearer for the client.

By defining specific commands (`ADD`, `SUB`, `MUL`, `DIV`), 
the protocol ensures that the client only sends requests that the server can handle effectively.<br>
Error handling has been built into the protocol to cover cases of invalid commands, non-numeric arguments, and division 
by zero, with appropriate error messages sent back to the client.
This approach prevents unexpected behavior and keeps the client-server interaction predictable.

## 2. Transport Layer Protocol

- **Protocol**: TCP
- **IP Address**: The client must know the server’s IP address and its port.
- **Port**: The server listens on TCP port 50001.
- **Connection Initiation**: The client initiates the connection.
- **Connection Termination**: The server closes the connection when the client sends the `exit` command, and it sends a 
closing message before terminating.

This use of a TCP connection ensures reliable delivery of messages between the client and server, guaranteeing that all 
requests and responses are accurately transmitted.

## 3. Messages

### Client Messages

- **Show supported opeartions**: Upon receiving the welcome message from the server, the client summarizes the operations supported 
by displaying the message <br> `Supported operations: ADD, SUB, MUL, DIV. Use the format: <COMMAND> <ARG1> <ARG2>`
and wait for the user to enter a command.

- **ARG1** and **ARG2**: Integer values for the operation. Only integer inputs are supported; decimal or floating-point numbers will result in an error.

- **Request Format**: `<COMMAND> <ARG1> <ARG2>`
    - **COMMAND**: Specifies the arithmetic operation. Valid commands include:
        - `ADD` - for addition
        - `SUB` - for subtraction
        - `MUL` - for multiplication
        - `DIV` - for division
    - **Special Command**: `exit` - Instructs the server to close the connection.
    - **ARG1** and **ARG2**: Integer values for the operation.

  **Examples**:
    - `ADD 15 10` - requests the addition of 15 and 10 to the server.
    - `exit` - instructs the server to close the connection.

### Server Messages

- **Welcome Message and sending supported operations**: `WELCOME Supported operations: ADD (addition), SUB (subtraction), MUL (multiplication), DIV (division). 
Use format: <COMMAND> <ARG1> <ARG2>`
- **Response Format**:
    - `<RESULT>` for successful operations.
    - `ERROR <DESCRIPTION>` for error responses.
- **Closing Message**: `GOODBYE Connection closing` - Sent only when the client sends the `exit` command.

#### Server Responses:
- **Success**:
    - The server sends the computed result as a single line.
- **Error Cases**:
    - **Invalid Command**: `ERROR Invalid Command`
    - **Division by Zero**: `ERROR Division by Zero`
    - **Invalid Arguments**: `ERROR Invalid Arguments`

The server closes the connection only when the client sends the `exit` command and responds with the `GOODBYE Connection closing` message before terminating.

## 4. Specific Elements

### Error Handling

The server validates each request and responds with a specific error message if there’s an issue:

1. **Invalid Command**: If the command is not recognized, respond with `ERROR Invalid Command`.
2. **Division by Zero**: For a division by zero attempt, respond with `ERROR Division by Zero`.
3. **Invalid Arguments**: For non-numeric or incomplete arguments, respond with `ERROR Invalid Arguments`.

All error messages begin with `ERROR`, followed by a description, allowing the client to handle errors effectively.

Error handling in this protocol is designed to ensure that all client requests receive a clear and appropriate response, regardless of validity. 
The protocol defines explicit error messages for unrecognized commands, invalid arguments (such as non-integer values), and division by zero.

### Extensibility

The protocol can be extended with additional commands, following the format `<COMMAND> <ARG1> <ARG2>`. 
Unsupported commands result in `ERROR Invalid Command`, ensuring compatibility with newer commands.

## 5. Example Dialogs

### Generic Client-Server Mechanism

<img src="images/protocol-Generic-Client-Server-Mechanism.svg" alt="Generic Client-Server Mechanism" height="400px">

*Sequence diagram showing the general mechanism of the client-server interaction.*

### Successful Operation Example (Addition)

**Dialog**: The client connects, performs an addition, receives the result, and sends `exit` to close the connection.

**Sequence**:
1. **Client → Server**: Connects to server on port 50001
2. **Server → Client**: `WELCOME Supported operations: ADD (addition), SUB (subtraction), MUL (multiplication), DIV (division). Use format: <COMMAND> <ARG1> <ARG2>`
3. **Client → Server**: `ADD 10 5`
4. **Server → Client**: `15`
5. **Client → Server**: `exit`
6. **Server → Client**: `GOODBYE Connection closing`
7. **Server → Client**: Disconnects

<img src="images/protocol-Successful-Operation-Example.svg" alt="Successful Operation Example (Addition)" height="400px">

*Sequence diagram illustrating a successful calculation scenario with the addition command.*

### Error Scenario: Division by Zero

**Dialog**: The client attempts to divide by zero, receives an error message, and sends `exit` to close the connection.

**Sequence**:
1. **Client → Server**: Connects to server on port 50001
2. **Server → Client**: `WELCOME Supported operations: ADD (addition), SUB (subtraction), MUL (multiplication), DIV (division). Use format: <COMMAND> <ARG1> <ARG2>`
3. **Client → Server**: `DIV 10 0`
4. **Server → Client**: `ERROR Division by Zero`
5. **Client → Server**: `exit`
6. **Server → Client**: `GOODBYE Connection closing`
7. **Server → Client**: Disconnects

<img src="images/protocol-Error-Scenario-Example.svg" alt="Error Scenario Example (Division by Zero)" height="400px">

*Sequence diagram showing the error handling for a division by zero.*

### Error Scenario: Invalid Command

**Dialog**: The client sends an unsupported command, receives an error, and sends `exit` to close the connection.

**Sequence**:
1. **Client → Server**: Connects to server on port 50001
2. **Server → Client**: `WELCOME Supported operations: ADD (addition), SUB (subtraction), MUL (multiplication), DIV (division). Use format: <COMMAND> <ARG1> <ARG2>`
3. **Client → Server**: `MOD 10 3`
4. **Server → Client**: `ERROR Invalid Command`
5. **Client → Server**: `exit`
6. **Server → Client**: `GOODBYE Connection closing`
7. **Server → Client**: Disconnects

### Error Scenario: Invalid Arguments

**Dialog**: The client sends an operation with invalid arguments, receives an error, and sends `exit` to close the connection.

**Sequence**:
1. **Client → Server**: Connects to server on port 50001
2. **Server → Client**: `WELCOME Supported operations: ADD (addition), SUB (subtraction), MUL (multiplication), DIV (division). Use format: <COMMAND> <ARG1> <ARG2>`
3. **Client → Server**: `ADD ten 5`
4. **Server → Client**: `ERROR Invalid Arguments`
5. **Client → Server**: `exit`
6. **Server → Client**: `GOODBYE Connection closing`
7. **Server → Client**: Disconnects
