import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testCompareTo(){
        User leo = new User(110, "Leo" , "cris");
        User mart = new User(111, "Mart", "tomo");
        int result = leo.compareTo(mart);
        assertEquals(result, -1);
        int j = mart.compareTo(leo);
        assertEquals(j, 1);
    }
}
