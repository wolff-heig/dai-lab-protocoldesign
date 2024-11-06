package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    // Note : It would be more appropriate to set this data as environment variables or secret rather than hard-coded, as is the case here.
    final String SERVER_ADDRESS = "127.0.0.1"; // IP address of the server (localhost for testing on the same machine)
    final int SERVER_PORT = 50001; // Port defined in the protocol specification
    final String SERVER_PREFIX = "Server: "; // Prefix for all messages from the server

    public static void main(String[] args) {
        // Create a new instance of the client and run it
        Client client = new Client();
        client.run();
    }

    private void run() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             Scanner scanner = new Scanner(System.in)) {

            // Read and display the welcome message with supported operations
            String welcomeMessage = in.readLine();
            System.out.println(SERVER_PREFIX + welcomeMessage);

            // Display the supported operations to the user
            System.out.println("Supported operations: ADD, SUB, MUL, DIV. Use the format: <COMMAND> <ARG1> <ARG2>");

            // Loop to send commands
            while (true) {
                System.out.print("Enter command (or type 'exit' to quit): ");
                String command = scanner.nextLine();

                // Send the command to the server
                out.write(command + "\n");
                out.flush();

                if ("exit".equalsIgnoreCase(command)) {
                    // Read and display the closing message
                    String closingMessage = in.readLine();
                    if (closingMessage != null) {
                        System.out.println(SERVER_PREFIX + closingMessage);
                    }
                    break; // Exit the loop
                }

                // Read and display the server's response
                String response = in.readLine();
                System.out.println(SERVER_PREFIX + response);
            }

        } catch (IOException e) {
            System.err.println("Client: exception: " + e.getMessage());
        }
    }
}
