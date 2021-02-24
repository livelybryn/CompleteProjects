import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2021/01/27
 */
public class DNSMessage {

    byte[] fullMessage;
    DNSHeader header;
    List<DNSQuestion> questions;
    List<DNSRecord> answers;
    List<DNSRecord> authorityRecords;
    List<DNSRecord> additionalRecords;
    Map<String, Integer> labelByLocation;

    /**
     * This method takes a byte array and saves the values in a DNS message object
     * @param bytes the byte array of the DNS query
     * @return a DNS message with the values of the byte array
     * @throws IOException
     */
    static DNSMessage decodeMessage(byte[] bytes) throws IOException {
        DNSMessage message = new DNSMessage();
        message.fullMessage = bytes;
        int currentStreamPosition = 0;
        ByteArrayInputStream readMessage = new ByteArrayInputStream(bytes);
        // Reads header
        message.header = DNSHeader.decodeHeader(readMessage);
        currentStreamPosition += 12;

        // reads question
        for (int i = 0; i < message.header.qdcount; i++) {
            message.questions = new ArrayList<>();
            DNSQuestion question = DNSQuestion.decodeQuestion(readMessage, message, currentStreamPosition);
            currentStreamPosition += question.questionLength;
            message.questions.add(question);
        }
        // reads answer
        for (int i = 0; i < message.header.arcount; i++) {
            message.answers = new ArrayList<>();
            DNSRecord record = DNSRecord.decodeRecord(readMessage, message, currentStreamPosition);
            currentStreamPosition += record.recordLength;
            message.answers.add(record);
        }
        //reads authorityRecords
        for (int i = 0; i < message.header.nscount; i++) {
            message.authorityRecords = new ArrayList<>();
            DNSRecord record = DNSRecord.decodeRecord(readMessage, message, currentStreamPosition);
            currentStreamPosition += record.recordLength;
            message.authorityRecords.add(record);
        }
        //reads additionalRecords
        for (int i = 0; i < message.header.arcount; i++) {
            message.additionalRecords = new ArrayList<>();
            DNSRecord record = DNSRecord.decodeRecord(readMessage, message, currentStreamPosition);
            currentStreamPosition += record.recordLength;
            message.additionalRecords.add(record);
        }
        return message;
    }

    /**
     * This method turns a byte array output stream to a byte array
     * @param output the output stream containing a byte response
     * @return a byte array of the output stream
     */
    byte[] toBytes(ByteArrayOutputStream output) {
        return output.toByteArray();
    }

    @Override
    public String toString() {
        return "Bytes: " + Arrays.toString(fullMessage) +
            "\nDNSMessage{" +
            "\nheader=" + header +
            ", \nquestions=" + questions +
            ", \nanswers=" + answers +
            ", \nauthorityRecords=" + authorityRecords +
            ", \nadditionalRecords=" + additionalRecords +
            '}';
    }

}
