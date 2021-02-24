import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2021/01/31
 */
public class DNSHelpers {

    /**
     * This method takes a two byte array and combines the values into an int
     * @param bytes the bytes to combine
     * @return the int of the two bytes
     */
    public static int twoByteArrayToInt(byte[] bytes) {
        return twoBytesToInt(bytes[0], bytes[1]);
    }

    /**
     * This method takes two bytes and combines them into one int
     * @param one The first byte to combine
     * @param two The second byte to combine
     * @return the int of the two combined bytes
     */
    public static int twoBytesToInt(int one, int two) {
        return ((one & 0xFF) << 8) | (two & 0xFF);
    }

    public static int fourBytesToInt(int one, int two, int three, int four) {
        return ((one & 0xFF) << 24) | ((two & 0xFF) << 16) | ((three & 0xFF) << 8) | (four & 0xFF);
    }

    /**
     * This method takes an int value and separates it into a two byte array
     * @param value the int value to separate
     * @return the byte array containing the value of the int
     */
    public static byte[] intToTwoByteArray(int value) {
        return new byte[] {
            (byte)(value >> 8),
            (byte)value };
    }

    /**
     * This method takes an int value and separates it into a two byte array
     * @param value the int value to separate
     * @return the byte array containing the value of the int
     */
    public static byte[] intToFourByteArray(int value) {
        return new byte[] {
            (byte)(value >> 24),
            (byte)(value >> 16),
            (byte)(value >> 8),
            (byte)value };
    }

    /**
     * This is the object that holds the string domain name and the number of bytes used to read the
     * domain name
     */
    static class DNSMessageStringRead {
        String result;
        int numBytesUsed;

        public DNSMessageStringRead(String result, int numBytesUsed) {
            this.result = result;
            this.numBytesUsed = numBytesUsed;
        }
    }

    /**
     * This method takes a byte message and a position to start the byte buffer at. It reads the bytes and
     * determines if the bytes indicate a pointer to a domain name or if it contains an actual domain
     * name. It builds a DNSMessageStringRead which contains the amount of bytes used for the domain
     * name and the string domain name
     * @param fullMessage the entire byte DNS query in a byte buffer
     * @param position the position to start the byte buffer at
     * @return a DNSMessageStringRead object that contains the domain name string and the number of bytes used
     */
    static DNSMessageStringRead readStringFromMessage(ByteBuffer fullMessage, int position) {
        fullMessage.position(position); // Starts the byte buffer at the correct location
        byte firstByte = fullMessage.get(); // Saves the first byte from fullMessage to pointer

        if (firstByte == 0) {
            return new DNSMessageStringRead(null, 1); // Recursion termination!

        } else if (checkPointer(firstByte)) { // if value is a pointer, go to location and get name
            byte secondByte = fullMessage.get(); // get rest of pointer value
            firstByte = (byte)(firstByte & 0x3F); // mask = 00111111 this cancels out the first 11's indicating pointer
            int location = twoByteArrayToInt(new byte[] {firstByte, secondByte}); // gets the int location from the pointer
            DNSMessageStringRead pointerResult = readStringFromMessage(fullMessage, location); // RECURSION? ASK NICOLE
            pointerResult.numBytesUsed = 2;
            return pointerResult;

        } else {
            int labelLength = firstByte; // gets label length from pointer
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < labelLength; i++) { // loop through the length of the label
                result.append((char) fullMessage.get()); // add the byte as a char to the string
            }

            String thisStepResult = result.toString();
            int numBytesUsedThisStep = labelLength + 1;

            // Now, UNLESS it was terminated, we move onto the next segment
            DNSMessageStringRead nextResult = readStringFromMessage(fullMessage, position + numBytesUsedThisStep);
            if (nextResult.result != null) {
                thisStepResult += "." +  nextResult.result;
            }
            return new DNSMessageStringRead(thisStepResult, nextResult.numBytesUsed + numBytesUsedThisStep);
        }
    }

    /**
     * This method takes a string input and checks a map to see if we already have the location of that
     * string. If not, we break the string apart by "." and save the first part into our map with its
     * location. Then we do the whole process on the second part until all parts are either saved in
     * our map or we find the string in the map and return a pointer
     * @param out the byte array output stream that contains the domain name byte array output for the DNS response
     * @param input the input string to find in the map/write out to the output stream
     * @param labelByLocation the map containing the string and location of all domain names we have come across
     */
    static void toBuffer(ByteArrayOutputStream out, String input, Map<String, Integer> labelByLocation) {
        // check if the map contains the entire string
        if (labelByLocation.containsKey(input)) { // <"Hello.com": 0, "com": 6>
            int location = labelByLocation.get(input);
            byte[] pointer = intToTwoByteArray(location); // create pointer from location
            pointer[0] |= 0xC0; // add pointer values
            out.writeBytes(pointer); // write out pointer
            // done
        // Add first part of string to map and recurse through the rest of the string
        } else {
            labelByLocation.put(input, out.size()); // get location using output stream
            String[] parts = input.split(Pattern.quote("."), 2); // "www.hello.com" -> ["www"][ "hello.com" ]
            out.write(parts[0].length()); // write length of first part of string to outputstream
            out.writeBytes(parts[0].getBytes()); // writes bytes of string
            // if string has more parts, continue with rest of string
            if (parts.length > 1) {
                toBuffer(out, parts[1], labelByLocation);
            // else, that was the last part of the string so we are finished with the domain name
            } else {
                out.write(0);
            }
        }
    }

    /**
     * This method takes a byte input and returns if the byte is a pointer or not
     * @param input the byte to evaluate
     * @return true if the byte is a pointer, false if not
     */
    public static boolean checkPointer(byte input) {
        return (input & 0xC0) == 0xC0; // ?
    }

    /**
     * This method is a test for the readStringFromMessage method
     */
    public static void helloTest() {
        byte[] helloTest = {5, 104, 101, 108, 108, 111, 3, 99, 111, 109, 0};
        ByteBuffer bb = ByteBuffer.allocate(helloTest.length);
        bb.put(helloTest);
        DNSMessageStringRead resultString = readStringFromMessage(bb, 0);
        System.out.println(resultString.result);
    }

    /**
     * This method is a test for the toBuffer method
     */
    public static void fullTest() {
        byte[] fullTest = {
            // 0: 5 hello
            5, 104, 101, 108, 108, 111,

            // 6: 3 com
            3, 99, 111, 109,

            // 10: NULL
            0,

            // 11: pointer to hello.com
            (byte)0xC0, 0x00,

            // 13: NULL to terminate this string
            0,

            // 14: www

            3, 119, 119, 119,

            // 18: google

            6, 103, 111, 111, 103, 108, 101,

            // 25: pointer to com
            (byte)0xC0, 0x06,

            // 27: NULL
            0
        };

        // reading at 0: hello.com (null-1 + com-4 + hello-6 = 11)
        ByteBuffer bb = ByteBuffer.allocate(fullTest.length).put(fullTest);
        DNSMessageStringRead helloResultString = readStringFromMessage(bb, 0);
        System.out.println(helloResultString.result + " (" + helloResultString.numBytesUsed + ")");

        // reading at 11: pointer to hello.com
        DNSMessageStringRead hello2ResultString = readStringFromMessage(bb, 11);
        System.out.println(hello2ResultString.result + " (" + hello2ResultString.numBytesUsed + ")");

        // reading at 14: www.google.com (null-1 + com(pointer)-2 + google(7) + www(4) = 14)
        DNSMessageStringRead googleResultString = readStringFromMessage(bb, 14);
        System.out.println(googleResultString.result + " (" + googleResultString.numBytesUsed + ")");

    }

    public static void toBufferTest() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Map<String, Integer> pointers = new HashMap<>();

        int helloAddr = out.size(); // 0, of course, but where hello.com starts
        toBuffer(out, "hello.com", pointers);
        System.out.println(debugMap(pointers));

        // pointers should be: { "hello.com" : 0, "com": 6 }

        int wwwGoogleAddr = out.size(); // where www.google.com will start
        toBuffer(out, "www.google.com", pointers);
        System.out.println(debugMap(pointers));
        // pointers should additionally contain: { "www.google.com" : 11, "google.com": 15 }

        int storeGoogleAddr = out.size(); // where store.google.com will start
        toBuffer(out, "store.google.com", pointers);
        System.out.println(debugMap(pointers));
        // pointers should additionally contain: { "store.google.com" : 18  }

        ByteBuffer bb = ByteBuffer.wrap(out.toByteArray());

        DNSMessageStringRead helloResultString = readStringFromMessage(bb, helloAddr);
        System.out.println(helloResultString.result + " (" + helloResultString.numBytesUsed + ")");

        // reading at 11: pointer to hello.com
        DNSMessageStringRead wwwResultString = readStringFromMessage(bb, wwwGoogleAddr);
        System.out.println(wwwResultString.result + " (" + wwwResultString.numBytesUsed + ")");

        // reading at 14: www.google.com (null-1 + com(pointer)-2 + google(7) + www(4) = 14)
        DNSMessageStringRead storeGoogleString = readStringFromMessage(bb, storeGoogleAddr);
        System.out.println(storeGoogleString.result + " (" + storeGoogleString.numBytesUsed + ")");
    }

    public static <K, V> String debugMap(Map<K, V> map) {
        return map.entrySet().stream().map((e) -> e.getKey() + ":" + e.getValue()).collect(
            Collectors.joining(" "));
    }

    public static void main(String[] args) {
        DNSHelpers.fullTest();
        DNSHelpers.toBufferTest();
    }

}
