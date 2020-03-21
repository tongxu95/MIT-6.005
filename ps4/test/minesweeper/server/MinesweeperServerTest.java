/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Random;

import minesweeper.server.MinesweeperServer;


/**
 * Test for MinesweeperServer 
 */
public class MinesweeperServerTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static final int PORT = 4000 + new Random().nextInt(1 << 15);
    
    private static final int MAX_CONNECTION_ATTEMPTS = 10;

    // Testing strategy: board initialized from a file
	//      basic look, dig, flag, deflag command and X,Y directions
    //		server mode: debug
    //		playerCount
    
	@Test(expected=AssertionError.class)
    public void testAssertionEnabled() {
    	assert false;    	
    }
    
    /**
     * Start a MinesweeperServer in debug mode with a board file from BOARDS_PKG.
     * @param boardFile board to load
     * @return thread running the server
     * @throws IOException if the board file cannot be found
     */
    private static Thread startMinesweeperServer(String boardFile) throws IOException {
        final String boardPath = new File("board_file_5.txt").getAbsolutePath();
        final String[] args = new String[] {
                "--debug",
                "--port", Integer.toString(PORT),
                "--file", boardPath
        };
        Thread serverThread = new Thread(() -> MinesweeperServer.main(args));
        serverThread.start();
        return serverThread;
    }
	
    /**
     * Connect to a MinesweeperServer and return the connected socket.
     * @param server abort connection attempts if the server thread dies
     * @return socket connected to the server
     * @throws IOException if the connection fails
     */
    private static Socket connectToMinesweeperServer(Thread server) throws IOException {
        int attempts = 0;
        while (true) {
            try {
                Socket socket = new Socket(LOCALHOST, PORT);
                socket.setSoTimeout(3000);
                return socket;
            } catch (ConnectException ce) {
                if ( ! server.isAlive()) {
                    throw new IOException("Server thread not running");
                }
                if (++attempts > MAX_CONNECTION_ATTEMPTS) {
                    throw new IOException("Exceeded max connection attempts", ce);
                }
                try { Thread.sleep(attempts * 10); } catch (InterruptedException ie) { }
            }
        }
    }
 
    @Test(timeout = 10000)
    public void publishedTest() throws IOException {

        Thread thread = startMinesweeperServer("board_file_5");

        Socket socket = connectToMinesweeperServer(thread);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertEquals("expected HELLO message with 1 player", in.readLine(), "Welcome to "
        		+ "Minesweeper. Board: 7 columns by 7 rows. Players: 1 including you. Type "
        		+ "'help' for help.");
        
        out.println("look");    
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        
        out.println("dig 3 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 1 - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        Socket socket2 = connectToMinesweeperServer(thread);

        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);

        assertEquals("expected HELLO message with 1 player", in2.readLine(), "Welcome to "
        		+ "Minesweeper. Board: 7 columns by 7 rows. Players: 2 including you. Type "
        		+ "'help' for help.");
        
        out2.println("flag 2 1");      
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - F 1 - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        
        out2.println("deflag 2 1");      
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - - 1 - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        assertEquals("- - - - - - -", in2.readLine());
        
        out2.println("bye");
        socket2.close();
        
        Socket socket3 = connectToMinesweeperServer(thread);

        BufferedReader in3 = new BufferedReader(new InputStreamReader(socket3.getInputStream()));
        PrintWriter out3 = new PrintWriter(socket3.getOutputStream(), true);

        assertEquals("expected HELLO message with 1 player", in3.readLine(), "Welcome to "
        		+ "Minesweeper. Board: 7 columns by 7 rows. Players: 2 including you. Type "
        		+ "'help' for help.");
        
        out3.println("dig 4 1");
        assertEquals("BOOM!", in3.readLine());

        out3.println("look"); // debug mode is on
        assertEquals("             ", in3.readLine());
        assertEquals("             ", in3.readLine());
        assertEquals("             ", in3.readLine());
        assertEquals("             ", in3.readLine());
        assertEquals("             ", in3.readLine());
        assertEquals("1 1          ", in3.readLine());
        assertEquals("- 1          ", in3.readLine());
        
        out.println("bye");
        out3.println("bye");
        socket.close();
        socket3.close();
        
    }
    
}
