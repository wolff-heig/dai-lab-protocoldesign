package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Server {
    // Note : It would be more appropriate to set the server's port as environment variable or secret rather than hard-coded, as is the case here.
    final int SERVER_PORT = 50001; // Port where the server will listen for connections

    public static void main(String[] args) {
        // Create a new instance of the server and run it
        Server server = new Server();
        server.run();
    }

    private void run() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is running and listening on port " + SERVER_PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8))) {

                    // Send the initial welcome message to the client, including supported operations
                    out.write("WELCOME Supported operations: ADD (addition), SUB (subtraction), MUL (multiplication), DIV (division). Use format: <COMMAND> <ARG1> <ARG2>\n");
                    out.flush();

                    String command;

                    // Read and process each command received from the client until "exit" is received
                    while ((command = in.readLine()) != null) {
                        if (command.equalsIgnoreCase("exit")) {
                            // Send the closing message and break the loop to terminate connection
                            out.write("GOODBYE Connection closing\n");
                            out.flush();
                            break;
                        }

                        // Split the command into parts and validate the format <COMMAND> <ARG1> <ARG2>
                        String[] parts = command.split(" ");
                        if (parts.length != 3) {
                            out.write("ERROR Invalid Arguments\n");
                        } else {
                            String operation = parts[0];
                            try {
                                int arg1 = Integer.parseInt(parts[1]);
                                int arg2 = Integer.parseInt(parts[2]);
                                int result;

                                switch (operation) {
                                    case "ADD":
                                        result = arg1 + arg2;
                                        out.write(result + "\n");
                                        break;
                                    case "SUB":
                                        result = arg1 - arg2;
                                        out.write(result + "\n");
                                        break;
                                    case "MUL":
                                        result = arg1 * arg2;
                                        out.write(result + "\n");
                                        break;
                                    case "DIV":
                                        // Check for division by zero and write error message if detected
                                        if (arg2 == 0) {
                                            out.write("ERROR Division by Zero\n");
                                        } else {
                                            result = arg1 / arg2;
                                            out.write(result + "\n");
                                        }
                                        break;
                                    default:
                                        out.write("ERROR Invalid Command\n");
                                        break;
                                }
                            // Catch invalid argument format (non-integer or insufficient arguments) and notify client
                            } catch (NumberFormatException e) {
                                out.write("ERROR Invalid Arguments\n");
                            }
                        }
                        out.flush();
                    }

                } catch (IOException e) {
                    System.err.println("Server: exception: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server: exception: " + e.getMessage());
        }
    }
}
