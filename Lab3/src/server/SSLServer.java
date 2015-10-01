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

            BufferedReader in = new BufferedReader( new InputStreamReader( incoming.getInputStream() ) );
            PrintWriter out = new PrintWriter( incoming.getOutputStream(), true );

            String fileName;
            String fileData;
            int option = Integer.parseInt(in.readLine());

            switch(option) {
                case 1:
                    System.out.println("User requested to download a file");

                    fileName = in.readLine();
                    fileData = readFile(fileName);
                    out.println(fileData);
                    break;
                case 2:
                    System.out.println("User requested to upload a file");
                    fileName = in.readLine();
                    fileData = readStringFromClient(in);

                    download(fileName, fileData);
                    break;
                case 3:
                    System.out.println("User requested to delete a file");
                    fileName = in.readLine();
                    delete(fileName);
                    break;
                default:
                    System.out.println("Unexpected behaviour");
                    break;
            }

            incoming.close();

        } catch(Exception x) {
            System.out.println(x);
            x.printStackTrace();
        }
    }

    public void download(String fileName, String fileData) {
        String serverFileName = "server"+fileName;
        System.out.println(serverFileName);
        PrintWriter writer;

        try
        {
            writer = new PrintWriter(serverFileName, "UTF-8");
            writer.print(fileData);
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println("Error writing to file");
            e.printStackTrace();
        }

    }

    private String readFile(String fileName)
    {
        try
        {

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line!=null)
            {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            // return full text
            return sb.toString();
        }
        catch (Exception e)
        {
            System.out.println("Couldn't read file " + fileName + " Exception: " + e.toString());
            e.printStackTrace();
            return "";
        }
    }
    public void delete(String name) {
        try {
            File mFile = new File(name);
            mFile.delete();
            System.out.println("File deleted");
        }
        catch (Exception e){
            System.out.println("Error when trying to delete file");
            e.printStackTrace();
        }
    }

    private String readStringFromClient(BufferedReader socketIn) {
        try {
            StringBuilder sb = new StringBuilder();
            String line = socketIn.readLine();
            String fullText = "";
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = socketIn.readLine();
            }
            fullText = sb.toString();
            return fullText;
        } catch (Exception e) {
            System.out.println("Error reading string from server");
            e.printStackTrace();
            return "";
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
