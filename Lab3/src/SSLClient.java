/**
 * Created by englund on 26/09/15.
 */

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.StringTokenizer;
public class SSLClient {

    private InetAddress host;
    private int port;
    // This is not a reserved port number
    static final int DEFAULT_PORT = 8189;
    static final String KEYSTORE = "LIUkeystore.ks";
    static final String TRUSTSTORE = "LIUtruststore.ks";
    static final String STOREPASSWD = "123456";
    static final String ALIASPASSWD = "123456";


    public SSLClient( InetAddress host, int port ) {
        this.host = host;
        this.port = port;
    }
    public void run() {
        try {
            KeyStore ks = KeyStore.getInstance( "JCEKS" );
            ks.load(new FileInputStream(KEYSTORE), STOREPASSWD.toCharArray());

            KeyStore ts = KeyStore.getInstance( "JCEKS" );
            ts.load(new FileInputStream(TRUSTSTORE), STOREPASSWD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init( ks, ALIASPASSWD.toCharArray() );

            TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
            tmf.init(ts);

            SSLContext sslContext = SSLContext.getInstance( "TLS" );
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLSocketFactory sslFact = sslContext.getSocketFactory();
            SSLSocket client =  (SSLSocket)sslFact.createSocket(host, port);
            client.setEnabledCipherSuites( client.getSupportedCipherSuites() );
            System.out.println("\n>>>> SSL/TLS handshake completed");

            BufferedReader socketIn;
            socketIn = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
            PrintWriter socketOut = new PrintWriter( client.getOutputStream(), true );

            printMenu();
            String input = new BufferedReader(new InputStreamReader(System.in)).readLine();
            int option = Integer.parseInt(input);


            socketOut.println(option);


            switch(option) {
                case 1:
                    System.out.println("Option 1");
                    break;
                case 2:
                    System.out.println("Option 2");
                    break;
                case 3:
                    System.out.println("Please enter the name of the file you want to delete.");
                    try {
                        String fileName = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        System.out.println("Deleting file from the server");
                        socketOut.println(fileName);
                    }
                    catch (Exception e){
                        System.out.println("There was an error when handling your request");
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Wrong option, byebye");
                    break;
            }



        }
        catch( Exception x ) {
            System.out.println( x );
            x.printStackTrace();
        }
    }

    public void download() {

    }

    public void upload() {

    }

    public void delete() {

    }

    public void printMenu() {
        System.out.println("SSL Lab 3 Menu, have fun!");
        System.out.println("1. Download file from server?");
        System.out.println("2. Upload file to server?");
        System.out.println("3. Delete file from server?");

    }

    public static void main( String[] args ) {
        try {
            InetAddress host = InetAddress.getLocalHost();
            int port = DEFAULT_PORT;
            if ( args.length > 0 ) {
                port = Integer.parseInt( args[0] );
            }
            if ( args.length > 1 ) {
                host = InetAddress.getByName( args[1] );
            }
            SSLClient client = new SSLClient( host, port );
            client.run();
        }
        catch ( UnknownHostException uhx ) {
            System.out.println( uhx );
            uhx.printStackTrace();
        }
    }
}
