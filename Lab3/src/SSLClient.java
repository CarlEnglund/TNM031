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
    static final String KEYSTORE = "jpatkeystore.ks";
    static final String TRUSTSTORE = "jpattruststore.ks";
    static final String STOREPASSWD = "changeit";
    static final String ALIASPASSWD = "changeit";


    public SSLClient( InetAddress host, int port ) {
        this.host = host;
        this.port = port;
    }
    public void run() {
        try {
            KeyStore ks = KeyStore.getInstance( "JCEKS" );
            ks.load( new FileInputStream( KEYSTORE ), STOREPASSWD.toCharArray() );

            KeyStore ts = KeyStore.getInstance( "JCEKS" );
            ts.load( new FileInputStream( TRUSTSTORE ), STOREPASSWD.toCharArray() );

            KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
            kmf.init( ks, ALIASPASSWD.toCharArray() );

            TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
            tmf.init( ts );

            SSLContext sslContext = SSLContext.getInstance( "TLS" );
            sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
            SSLSocketFactory sslFact = sslContext.getSocketFactory();
            SSLSocket client =  (SSLSocket)sslFact.createSocket(host, port);
            client.setEnabledCipherSuites( client.getSupportedCipherSuites() );
            System.out.println("\n>>>> SSL/TLS handshake completed");


            BufferedReader socketIn;
            socketIn = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
            PrintWriter socketOut = new PrintWriter( client.getOutputStream(), true );

            String numbers = "1.2 3.4 5.6";
            System.out.println( ">>>> Sending the numbers " + numbers+ " to SecureAdditionServer" );
            socketOut.println( numbers );
            System.out.println( socketIn.readLine() );

            socketOut.println ( "" );
        }
        catch( Exception x ) {
            System.out.println( x );
            x.printStackTrace();
        }
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
