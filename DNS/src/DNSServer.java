import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2021/02/01
 */
public class DNSServer {

    int incomingPort = 8080;
    int googlePort = 53;
    byte[] receive = new byte[512];

    /**
     * This method runs the DNS server
     * @throws IOException
     */
    public void run() throws IOException {

        DatagramSocket socket = new DatagramSocket(incomingPort); // creates a socket to receive packets
        DatagramPacket packet = new DatagramPacket(receive, receive.length); // receiving packet
        DNSCache cache = new DNSCache(); // New cache
        cache.cache = new HashMap<>(); // cache initialization
        while (true) { // keeps the server running
            socket.receive(packet); // waits for packet to be received
            byte[] incomingBytes = packet.getData(); // gets packet message
            DNSMessage incomingMessage = DNSMessage.decodeMessage(incomingBytes);
            System.out.println("Incoming Message: " + incomingMessage.toString());
            for (int i = 0; i < incomingMessage.questions.size(); i++) { // loops through all questions
                DNSRecord answerRecord = cache.checkCache(incomingMessage.questions.get(i)); // checks cache for answer
                // if cache has record, send record back with response
                if (answerRecord != null) {
                    System.out.println("Record found");
                    byte[] response = createResponse(incomingMessage, answerRecord);
                    DatagramPacket responsePacket = new DatagramPacket(
                        response, response.length, packet.getAddress(), packet.getPort());
                    socket.send(responsePacket);
                // if cache does not have record, ask google and save google's response in cache
                } else {
                    DNSMessage googleAnswer = askGoogle(incomingMessage); // ask google
                    if (googleAnswer.header.rcode == 3) { // check if domain name exists or not
                        DatagramPacket responsePacket = new DatagramPacket(
                            googleAnswer.fullMessage, googleAnswer.fullMessage.length, packet.getAddress(), packet.getPort());
                        socket.send(responsePacket);
                    } else {
                        cache.cache.put(googleAnswer.questions.get(0),
                            googleAnswer.answers.get(0)); // add google's response to cache
                        System.out.println("Answer added to cache: " + cache.cache.toString());
                        byte[] response = createResponse(incomingMessage,
                            googleAnswer.answers.get(0));
                        System.out.println("My Response: " + Arrays.toString(response));
                        DatagramPacket responsePacket = new DatagramPacket(
                            response, response.length, packet.getAddress(), packet.getPort());
                        socket.send(responsePacket);
                    }
                }
            }
        }
    }

    /**
     * This method sends in the incoming DNS query and sends it to google to get google's answer
     * @param incomingMessage the incoming DNS query
     * @return the DNS message containing google's answer
     * @throws IOException
     */
    DNSMessage askGoogle(DNSMessage incomingMessage) throws IOException {
        InetAddress googleIPA = InetAddress.getByName("8.8.8.8"); // google's IP Address
        DatagramSocket googleSocket = new DatagramSocket(8053); // Port on my comp
        DatagramPacket googleSendPacket = new DatagramPacket(
            incomingMessage.fullMessage, incomingMessage.fullMessage.length, googleIPA, googlePort); // asking google on googles port
        googleSocket.send(googleSendPacket); // Send query to google

        byte[] googleReceive = new byte[512];
        DatagramPacket googleReceivePacket = new DatagramPacket(googleReceive, googleReceive.length);
        googleSocket.receive(googleReceivePacket); // Receive response from google
        byte[] incomingBytes = googleReceivePacket.getData();
        googleSocket.close();
        System.out.println("Google Answer: " + Arrays.toString(incomingBytes));

        return DNSMessage.decodeMessage(incomingBytes);
    }

    /**
     * This method takes the incoming DNS query and the DNS answer record and creates a response in
     * bytes to send back to the client
     * @param incomingMessage the incoming DNS query
     * @param answerRecord the answer record to send to the client
     * @return the byte array to send to the client in a packet
     * @throws IOException
     */
    byte[] createResponse(DNSMessage incomingMessage, DNSRecord answerRecord) throws IOException {
        DNSMessage response = new DNSMessage();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response.labelByLocation = new HashMap<>();
        response.header = DNSHeader.buildResponseHeader(incomingMessage, response);
        DNSHeader.writeBytes(outputStream, response.header); // write header to output stream

        // Write questions to output stream
        for (int i = 0; i < response.header.qdcount; i++) {
            response.questions = new ArrayList<>();
            response.questions.add(incomingMessage.questions.get(i));
            DNSQuestion.writeBytes(outputStream, response);
        }
        // write answers to output stream
        for (int i = 0; i < response.header.ancount; i++) {
            response.answers = new ArrayList<>();
            response.answers.add((answerRecord));
            DNSRecord.writeBytes(outputStream, response, response.answers.get(0));
        }
        // write authority records to output stream
        for (int i = 0; i < response.header.nscount; i++) {
            response.authorityRecords = new ArrayList<>();
            response.authorityRecords = incomingMessage.authorityRecords;
            DNSRecord.writeBytes(outputStream, response, response.authorityRecords.get(0));
        }
        // write additional records to output stream
        for (int i = 0; i < response.header.arcount; i++) {
            response.additionalRecords = new ArrayList<>();
            response.additionalRecords = incomingMessage.additionalRecords;
            DNSRecord.writeAdditionalBytes(outputStream, response.additionalRecords.get(0));
        }
        return outputStream.toByteArray(); // returns the output stream as a byte array
    }

    public static void main(String[] args) throws IOException {
        DNSServer server = new DNSServer();
        server.run();
    }
}
