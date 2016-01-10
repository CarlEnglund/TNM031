import java.security.*;
import javax.net.ssl.*;
import java.io.*;
import java.util.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.MessageDigest;


public class CLA { 
    private int port;

    static final int DEFAULT_PORT = 8889;
    static final String KEYSTORE = "CTF.ks";
    static final String TRUSTSTORE = "CTFtrust.ks";
    static final String STOREPASSWORD = "CTF123";
    static final String ALIASPASSWORD = "CTF123";
   
    private BufferedReader socketReaderFromCTF;
    private PrintWriter socketPrinterToCTF;
    private SSLSocket socketCTF;
    private SSLSocket socketClient;
    private boolean running = true;

    private SSLSocket streamCTF;
    private SSLSocket streamClient;
    private BufferedReader socketReaderFromClient;
    private PrintWriter socketPrinterToClient;
    
    private Map <String, Person> votersList; 
    private List <Person> registeredVoters;


    public void run() {
        try {
            votersList = new HashMap <String, Person>();

            registeredVoters = new ArrayList<Person>();


            registeredVoters.add(new Person("Bob", "123"));
            registeredVoters.add(new Person("Charlie", "1234"));
            registeredVoters.add(new Person("Harley", "12345"));
           // registeredVoters.add(new Person("Mors", "123456"));
          

            //Load Keystore
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(new FileInputStream(KEYSTORE), STOREPASSWORD.toCharArray());

            //Load Truststore
            KeyStore ts = KeyStore.getInstance("JCEKS");
            ts.load(new FileInputStream(TRUSTSTORE), STOREPASSWORD.toCharArray());

            //Init Keystore
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, ALIASPASSWORD.toCharArray() );
            
            //Init Truststore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ts);

            //Create a SSLContext object with the keystore and truststore, no extra protocol
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();

       
            //Set the ciphersuites to the ones supported by the Client and CTF
            SSLServerSocket socketClient = (SSLServerSocket) sslServerFactory.createServerSocket(8891);
            socketClient.setEnabledCipherSuites(socketClient.getSupportedCipherSuites());

            SSLSocketFactory sslFact = sslContext.getSocketFactory();
            socketCTF = (SSLSocket) sslFact.createSocket("localhost", 8192);
            socketCTF.setEnabledCipherSuites(sslFact.getSupportedCipherSuites());

            //Set Auth
            socketClient.setNeedClientAuth(true);
            socketCTF.setNeedClientAuth(true);

            System.out.println("CLA active");

            
            streamClient = (SSLSocket) socketClient.accept();


            socketReaderFromCTF = new BufferedReader(new InputStreamReader(socketCTF.getInputStream()));
            socketPrinterToCTF = new PrintWriter(socketCTF.getOutputStream(), true);

            socketReaderFromClient = new BufferedReader(new InputStreamReader(streamClient.getInputStream()));
            socketPrinterToClient = new PrintWriter(streamClient.getOutputStream(), true);
            
            //Send size of allowed voters
            socketPrinterToCTF.println(registeredVoters.size());

            while(true) {
                String message = socketReaderFromClient.readLine();
                String[] parts = message.split(":");
                String val = generateValidationNumber(parts[0], parts[1], parts[2]);
                socketPrinterToClient.println(val);
                socketPrinterToCTF.println(val);
            }
          

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String generateValidationNumber(String name, String socialsecuritynumber, String verify) {
        SecureRandom random = new SecureRandom();
        Person p = new Person(name, socialsecuritynumber);
        String validationNumber = generateHash(BigInteger.probablePrime(1024, random));
        List <Person> listOfVoters = new ArrayList<Person>(votersList.values());
        
        if(!checkIfAuthorizedVoter(name, socialsecuritynumber) && !(verify.equals("verify")))
            return "Cheater";

        if(votersList.isEmpty()) 
            votersList.put(validationNumber, p);
        else {
            if(checkifVoted(listOfVoters, p) && !(verify.equals("verify")))
                validationNumber = "Cheater";
            votersList.put(validationNumber, p);
        }
        return validationNumber;
    }

    private boolean checkIfAuthorizedVoter(String name, String socialsecuritynumber)
    {
        Person p = new Person(name, socialsecuritynumber);

        for (Person registered : registeredVoters)
        {
            if (p.equals(registered))
            {
                return true;
            }
        }
        return false;
    }

    public boolean checkifVoted(List<Person> listOfVoters, Person p) {

        if(listOfVoters.contains(p.getSocialSecurityNumber())) {
            return true;
        }
        return false;
    }

    //http://stackoverflow.com/questions/2624192/good-hash-function-for-strings
    public String generateHash(BigInteger validationNumber) {
        
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(String.valueOf(validationNumber).getBytes());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return "FAILED";

    }

    public static void main(String[] args) {
        CLA server = new CLA();
        server.run();
    }
    
}
