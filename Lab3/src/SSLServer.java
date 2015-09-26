import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.StringTokenizer;

public class SSLServer {


    static final int DEFAULT_PORT = 8189;
    static final String KEYSTORE = "jpatkeystore.ks";
    static final String TRUSTSTORE = "jpattruststore.ks";
    static final String STOREPASSWD = "changeit";
    static final String ALIASPASSWD = "changeit";

    public void run() {
        try {
            KeyStore ks = KeyStore.getInstance( "JCEKS" );
            ks.load( new FileInputStream( KEYSTORE ), STOREPASSWD.toCharArray() );

            KeyStore ts = KeyStore.getInstance( "JCEKS" );
            ts.load( new FileInputStream( TRUSTSTORE ), STOREPASSWD.toCharArray() );

            KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
            kmf.init( ks, ALIASPASSWD.toCharArray() );

        } catch(Exception x) {
            System.out.println(x);
            x.printStackTrace();
        }
    }
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
