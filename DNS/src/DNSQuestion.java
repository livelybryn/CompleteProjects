import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2021/01/27
 */
public class DNSQuestion {

    String qname;
    int qtype;
    int qclass;
    int questionLength;

    public DNSQuestion() {
    }

    /**
     * This method takes an incoming DNS query in the form of a byte array input stream, the DNS
     * message object, and the position to start a byte buffer at for reading the domain name. It
     * reads in the domain name and gets the other question values before returning a DNS question
     * object
     * @param inputStream the DNS query input stream
     * @param message the DNS message object
     * @param streamPosition the position to start reading the domain name from
     * @return a DNS question with all members filled in
     * @throws IOException
     */
    static DNSQuestion decodeQuestion(ByteArrayInputStream inputStream, DNSMessage message, int streamPosition)
        throws IOException {

        ByteBuffer forStrings = ByteBuffer.wrap(message.fullMessage); // copy for the strings so we can move around on it
        DNSQuestion question = new DNSQuestion();
        DNSHelpers.DNSMessageStringRead messageStringRead = DNSHelpers.readStringFromMessage(forStrings, streamPosition); // full message is needed to do compression reading
        question.qname = messageStringRead.result;
        forStrings.position(messageStringRead.numBytesUsed);
        inputStream.skip(messageStringRead.numBytesUsed); // Set inputStream to correct spot to keep reading
        question.qtype = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());
        question.qclass = DNSHelpers.twoBytesToInt(inputStream.read(), inputStream.read());
        question.questionLength = messageStringRead.numBytesUsed + 4; // length of domain name + qtype + qclass
        return question;
    }

    /**
     * This method takes an output stream to write to and the DNS message response to write
     * @param output the output stream to write bytes to
     * @param response the response to turn into bytes to send to the client
     */
    static void writeBytes(ByteArrayOutputStream output, DNSMessage response) {
        DNSHelpers.toBuffer(output, response.questions.get(0).qname, response.labelByLocation); // gets domain name
        byte[] qtype = {0, 1};
        byte[] qclass = {0, 1};
        output.writeBytes(qtype);
        output.writeBytes(qclass);
    }

    @Override
    public String toString() {
        return "DNSQuestion{" +
            "qname='" + qname + '\'' +
            ", qtype=" + qtype +
            ", qclass=" + qclass +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DNSQuestion question = (DNSQuestion) o;
        return qtype == question.qtype &&
            qclass == question.qclass &&
            qname.equals(question.qname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qname, qtype, qclass);
    }
}
