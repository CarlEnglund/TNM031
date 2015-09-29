/**
 * Created by englund on 26/09/15.
 */

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.StringTokenizer;

public class SSLServer {

    private int port;
    static final int DEFAULT_PORT = 8189;
    static final String KEYSTORE = "LIUkeystore.ks";
    static final String TRUSTSTORE = "LIUtruststore.ks";
    static final String STOREPASSWD = "123456";
    static final String ALIASPASSWD = "123456";

    SSLServer( int port ) {
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
            tmf.init(ts);

            SSLContext sslContext = SSLContext.getInstance( "TLS" );
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();
            SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket( port );
            sss.setEnabledCipherSuites( sss.getSupportedCipherSuites() );

            System.out.println("\n>>>> SecureAdditionServer: active ");
            SSLSocket incoming = (SSLSocket)sss.accept();

        } catch(Exception x) {
            System.out.println(x);
            x.printStackTrace();
        }
    }
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0 ) {
            port = Integer.parseInt( args[0] );
        }
        SSLServer server = new SSLServer( port );
        server.run();
    }
}
