import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2021/01/27
 */
public class DNSHeader {

    int id;
    boolean qr;
    int opcode;
    boolean aa;
    boolean tc;
    boolean rd;
    boolean ra;
    boolean z;
    boolean ad;
    boolean cd;
    int rcode;
    int qdcount;
    int ancount;
    int nscount;
    int arcount;

    /**
     * This method takes an input stream, creates a DNSHeader object, and stores the values from the
     * input stream into designated sections in the DNSHeader object
     * @param inputStream The byte array input stream containing a DNS query header
     * @return A DNSHeader object containing the values of the input stream
     * @throws IOException
     */
    static DNSHeader decodeHeader(ByteArrayInputStream inputStream) throws IOException {
        DNSHeader header = new DNSHeader();

        header.id = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());
        byte[] byte3and4 = inputStream.readNBytes(2);
        header.qr = intToBoolean(byte3and4[0] >> 7); // 10000000 --> 00000001
        header.opcode = (byte3and4[0] >> 3) & 1; // 01110000 --> 00000111
        header.aa = intToBoolean((byte3and4[0] >> 2) & 1); // 00000100 --> 00000001
        header.tc = intToBoolean((byte3and4[0] >> 1) & 1); // 00000010 --> 00000001
        header.rd = intToBoolean(byte3and4[0] & 1); // 00000001
        header.ra = intToBoolean(byte3and4[1] >> 7); // 10000000 --> 00000001
        header.z = intToBoolean((byte3and4[1] >> 6) & 1); // 01000000 --> 00000001
        header.ad = intToBoolean((byte3and4[1] >> 5) & 1); // 00100000 --> 00000001
        header.cd = intToBoolean((byte3and4[1] >> 4) & 1); // 00010000 --> 00000001
        header.rcode = (byte) (byte3and4[1] & 0xf); // 00001111
        header.qdcount = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());
        header.ancount = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());
        header.nscount = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());
        header.arcount = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());

        return header;
    }

    /**
     * This method takes an int and converts it to a boolean.
     * @param byteToConvert The byte to convert
     * @return a boolean value. 1 for true, 0 for false
     */
    static boolean intToBoolean(int byteToConvert) {
        return byteToConvert == 1;
    }

    /**
     * This method takes a boolean and converts it to an int value
     * @param booleanToConvert the boolean to convert
     * @return An int value. 1 for true, 0 for false
     */
    static int booleanToInt(boolean booleanToConvert) {
        if (booleanToConvert) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * This method builds a response header, changing the query flag to 1, the recursion flag to 0,
     * and the answer count to 1
     * @param request The incoming DNS query
     * @param response The outgoing DNS response
     * @return The DNSHeader response
     * @throws IOException
     */
    static DNSHeader buildResponseHeader(DNSMessage request, DNSMessage response) // response to send to client
        throws IOException {
        ByteArrayInputStream readRequest = new ByteArrayInputStream(request.fullMessage);
        DNSHeader responseHeader = decodeHeader(readRequest); // Copies over query header
        responseHeader.qr = true; // Changes qr to 1 instead of 0 since we're sending a response
        responseHeader.rd = false; // changes recursion to 0
        responseHeader.ancount = 1; // changes answer count to 1
        return responseHeader; // Returns response header
    }

    /**
     * This method writes a DNSResponse header in bytes to send to the client
     * @param outputStream The output stream to hold the response bytes while the response is built
     * @param responseHeader The response header object to write into bytes
     */
    static void writeBytes(ByteArrayOutputStream outputStream, DNSHeader responseHeader){
        // write bytes from header to send in packet
        byte[] id = DNSHelpers.intToTwoByteArray(responseHeader.id);
        // combining flags for byte 2
        byte flagsByte1 = (byte) ((byte) booleanToInt(responseHeader.qr) << 7 |
            ((byte)(responseHeader.opcode << 6)) |
            ((byte)booleanToInt(responseHeader.aa)) << 2 |
            ((byte)booleanToInt(responseHeader.tc)) << 1 |
            ((byte)booleanToInt(responseHeader.rd)));
        // combining flags for byte 3
        byte flagsByte2 = (byte) ((booleanToInt(responseHeader.ra) << 7) |
            ((byte)booleanToInt(responseHeader.z)) << 6 |
            ((byte)booleanToInt(responseHeader.ad)) << 5 |
            ((byte)booleanToInt(responseHeader.cd)) << 4 |
            ((byte)(responseHeader.rcode) << 3));
        byte[] qdcount = DNSHelpers.intToTwoByteArray(responseHeader.qdcount);
        byte[] ancount = DNSHelpers.intToTwoByteArray(responseHeader.ancount);
        byte[] nscount = DNSHelpers.intToTwoByteArray(responseHeader.nscount);
        byte[] arcount = DNSHelpers.intToTwoByteArray(responseHeader.arcount);
        // writing out to the output stream
        outputStream.writeBytes(id);
        outputStream.write(flagsByte1);
        outputStream.write(flagsByte2);
        outputStream.writeBytes(qdcount);
        outputStream.writeBytes(ancount);
        outputStream.writeBytes(nscount);
        outputStream.writeBytes(arcount);
    }


    @Override
    public String toString() {
        return "DNSHeader{" +
            "id=" + id +
            ", qr=" + qr +
            ", opcode=" + opcode +
            ", aa=" + aa +
            ", tc=" + tc +
            ", rd=" + rd +
            ", ra=" + ra +
            ", z=" + z +
            ", ad=" + ad +
            ", cd=" + cd +
            ", rcode=" + rcode +
            ", qdcount=" + qdcount +
            ", ancount=" + ancount +
            ", nscount=" + nscount +
            ", arcount=" + arcount +
            '}';
    }
}
