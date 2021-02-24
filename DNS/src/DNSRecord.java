import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.GregorianCalendar;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2021/01/27
 */
public class DNSRecord {

    String name;
    int type;
    int rClass;
    int ttl;
    int rdLength;
    String rdData;
    GregorianCalendar creationTime;
    int recordLength;

    /**
     * This method takes an incoming DNS query in the form of a byte array input stream, the DNS
     * message object, and the position to start a byte buffer at for reading the domain name. It
     * reads in the domain name and gets the other record values before returning a DNS record
     * object
     * @param inputStream the DNS query input stream
     * @param message the DNS message object
     * @param streamPosition the position to start reading the domain name from
     * @return a DNS question with all members filled in
     * @throws IOException
     */
    static DNSRecord decodeRecord(ByteArrayInputStream inputStream, DNSMessage message, int streamPosition) {
        DNSRecord record = new DNSRecord();
        record.creationTime = (GregorianCalendar) GregorianCalendar.getInstance();
        ByteBuffer readRecord = ByteBuffer.wrap(message.fullMessage);

        DNSHelpers.DNSMessageStringRead domainName = DNSHelpers.readStringFromMessage(readRecord, streamPosition); // full message needed to do compression
        inputStream.skip(domainName.numBytesUsed); // updates input stream after domain is read
        record.name = domainName.result;
        record.type = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());
        record.rClass = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());
        record.ttl = DNSHelpers.fourBytesToInt(inputStream.read(), inputStream.read(), inputStream.read(), inputStream.read()); // 32-bit int
        record.rdLength = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());
        record.rdData = byteToString(inputStream, record.rdLength);
        record.recordLength = domainName.numBytesUsed + 10 + record.rdLength; // domain name length + bytes read + rdData length
        return record;
    }

    /**
     * This method takes an input stream and a length and returns a String containing the byte values
     * @param inputStream the incoming DNS record
     * @param length the amount of bytes to read
     * @return the String containing the byte values
     */
    static String byteToString(ByteArrayInputStream inputStream, int length) {
        String result = "";
        for (int i = 0; i < length - 1; i++) {
            result += inputStream.read() + ".";
        }
        result += inputStream.read();
        return result;
    }

    /**
     * This method takes a DNS record and a length and deconstructs a string to put into a byte array
     * @param record the DNS record to read
     * @param length the length of the string
     * @return the byte array containing the string value
     */
    static byte[] stringToByteArray(DNSRecord record, int length) {
        byte[] result = new byte[length];
        String[] stringArray = record.rdData.split("\\.");
        for (int i = 0; i < stringArray.length; i++) {
            int stringIntValue =  Integer.parseInt(stringArray[i]);
            result[i] = (byte) stringIntValue;
        }
        return result;
    }


    /**
     * This method takes an output stream, a DNS message response and a DNS record to turn into bytes
     * @param output the output stream to hold the bytes
     * @param response the response to get the domain name string
     * @param record the record to turn into bytes
     */
    static void writeBytes(ByteArrayOutputStream output, DNSMessage response, DNSRecord record) {
        DNSHelpers.toBuffer(output, response.questions.get(0).qname, response.labelByLocation);
        writeRecordPieces(output, record);
    }

    /**
     * This method takes an output stream to write to and the DNS record response to write. Changes the
     * type value to be 41 for the response
     * @param output the output stream to write bytes to
     * @param record the response to turn into bytes to send to the client
     */
    static void writeAdditionalBytes(ByteArrayOutputStream output, DNSRecord record) {
        record.type = 41;
        byte name = 0;
        output.write(name);
        writeRecordPieces(output, record);
    }

    /**
     * This method takes an output stream and a dns record and writes the record into bytes
     * @param output the output stream to hold the byte response
     * @param record the record to turn into bytes
     */
    private static void writeRecordPieces(ByteArrayOutputStream output, DNSRecord record) {
        byte[] type = DNSHelpers.intToTwoByteArray(record.type);
        output.writeBytes(type);
        byte[] rClass = DNSHelpers.intToTwoByteArray(record.rClass);
        output.writeBytes(rClass);
        byte[] ttl = DNSHelpers.intToFourByteArray(record.ttl);
        output.writeBytes(ttl);
        byte[] rdLength = DNSHelpers.intToTwoByteArray(record.rdLength);
        output.writeBytes(rdLength);
        byte[] rdData;
        if (record.rdLength > 0) {
            rdData = stringToByteArray(record, record.rdLength);
            output.writeBytes(rdData);
        }
    }

    /**
     * This method returns if a record has expired or not using the ttl and current time
     * @return true if the record is not expired, false if it is
     */
    boolean timestampValid() {
        GregorianCalendar currentTime = (GregorianCalendar) GregorianCalendar.getInstance();
        this.creationTime.add(GregorianCalendar.SECOND, this.ttl);
        return (currentTime.compareTo(creationTime) < 0);
    }

    public static void main(String[] args) {
        byte[] ipAddressTest = {(byte) 128, 5, 0, 89};
        ByteArrayInputStream bb = new ByteArrayInputStream(ipAddressTest);
    }

    @Override
    public String toString() {
        return "DNSRecord{" +
            "name='" + name + '\'' +
            ", type=" + type +
            ", rClass=" + rClass +
            ", ttl=" + ttl +
            ", rdLength=" + rdLength +
            ", rdData='" + rdData + '\'' +
            '}';
    }
}
