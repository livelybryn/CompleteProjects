import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2021/02/01
 */
public class DNSCache {

    HashMap<DNSQuestion, DNSRecord> cache;

    /**
     * This method checks if the DNS question is in the cache. If it is, it checks if the time stamp
     * is still valid. If the time stamp is valid, the record for the question is returned, otherwise
     * returns null
     * @param question the question to check
     * @return the record if the question is found and is valid, otherwise null
     */
    DNSRecord checkCache(DNSQuestion question) {
        if (this.cache.containsKey(question)) { // if cache contains question
            if (this.cache.get(question).timestampValid()) { // if record is still valid
                return this.cache.get(question); // return record
            } else {
                this.cache.remove(question); // remove cache if record is not valid
                return null;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        DNSCache testCache = new DNSCache();
        testCache.cache = new HashMap<>();

        DNSQuestion testQuestion = new DNSQuestion();
        testQuestion.qname = "Test";
        DNSRecord testRecord = new DNSRecord();
        testRecord.creationTime = (GregorianCalendar) GregorianCalendar.getInstance();
        testRecord.creationTime.add(GregorianCalendar.SECOND, 90);

        DNSQuestion testEqualsQuestion = new DNSQuestion();
        testEqualsQuestion.qname = "Test";

        DNSQuestion testTimeStampQuestion = new DNSQuestion();
        testTimeStampQuestion.qname = "Test";

        DNSQuestion testNotEqualsQuestion = new DNSQuestion();
        testNotEqualsQuestion.qname = "Not Equals";

        testCache.cache.put(testQuestion, testRecord);
        System.out.println("Cache: " + testCache.cache);

        System.out.println("Test Equals:" + testCache.checkCache(testEqualsQuestion));
        System.out.println("Test Time Stamp:" + testCache.checkCache(testTimeStampQuestion));
        System.out.println("Test Not Equals:" + testCache.checkCache(testNotEqualsQuestion));

    }

}
