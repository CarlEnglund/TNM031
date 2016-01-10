/**
 * Created by englund on 19/11/15.
 */
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.StringTokenizer;


public class Client {

    static final int DEFAULT_PORT = 8889;
    static final String KEYSTORE = "CTF.ks";
    static final String TRUSTSTORE = "CTFtrust.ks";
    static final String STOREPASSWORD = "CTF123";
    static final String ALIASPASSWORD = "CTF123";
    private PrintWriter socketPrinterToCTF;
    private PrintWriter socketPrinterToCLA;
    private BufferedReader socketReaderFromCLA;
    private BufferedReader socketReaderFromCTF;
    private SSLSocket streamCLA;
    protected String validationNumber;
    protected String completed;



    public void run() {
        try {
            KeyStore ks = KeyStore.getInstance( "JCEKS" );
            ks.load(new FileInputStream(KEYSTORE), STOREPASSWORD.toCharArray());

            KeyStore ts = KeyStore.getInstance( "JCEKS" );
            ts.load(new FileInputStream(TRUSTSTORE), STOREPASSWORD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init( ks, ALIASPASSWORD.toCharArray() );

            TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
            tmf.init(ts);

            SSLContext sslContext = SSLContext.getInstance( "TLS" );
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            
           
            SSLSocketFactory sslFact = sslContext.getSocketFactory();
            SSLSocket client =  (SSLSocket)sslFact.createSocket("localhost", 7192);
            client.setEnabledCipherSuites( client.getSupportedCipherSuites() );
            client.setNeedClientAuth(true);

            SSLSocket clientToCLA =  (SSLSocket)sslFact.createSocket("localhost", 8891);
            clientToCLA.setEnabledCipherSuites( clientToCLA.getSupportedCipherSuites() );
            clientToCLA.setNeedClientAuth(true);
            

            System.out.println("Client active");



            socketPrinterToCTF = new PrintWriter(client.getOutputStream(), true);
            socketPrinterToCLA = new PrintWriter(clientToCLA.getOutputStream(), true);

            socketReaderFromCLA = new BufferedReader(new InputStreamReader(clientToCLA.getInputStream()));
            socketReaderFromCTF = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
         

        }
        catch( Exception x ) {
            x.printStackTrace();
        }
    }

    public String getMessageFromCTF() {
        try {
            String test = socketReaderFromCTF.readLine();
            System.out.println(test);
            return test;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "Failed";
    }
    public void MessageToCLA(String name, String socialSecurityNumber, String verify) {
        socketPrinterToCLA.println(name + ":" + socialSecurityNumber + ":" + verify);
    }
 
    public void MessageToCTF(String validationNumber, String socialSecurityNumber, String votingOption) {
        socketPrinterToCTF.println(validationNumber + ":" + socialSecurityNumber + ":" + votingOption);
    }



    public String getMessageFromCLA() {
        try {
            String messageFromCLA = socketReaderFromCLA.readLine();
            return messageFromCLA;
        }
        catch(Exception x){
            x.printStackTrace();
        }
        return "Fail";
    }
    public static void main( String[] args ) {        
        Client client = new Client();

        client.run();
    }
}
