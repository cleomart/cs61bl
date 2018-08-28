import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BinaryTreeTest {
    @Test
    public void treeFormatTest() {
        BinarySearchTree<String> x = new BinarySearchTree<String>();
        x.add("C");
        x.add("A");
        x.add("E");
        x.add("B");
        x.add("D");
        boolean result = x.contains("A");
        assertEquals(true, result);
        assertEquals(false, x.contains("G"));
        assertEquals(true, x.contains("E"));


//        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        PrintStream oldOut = System.out;
//        System.setOut(new PrintStream(outContent));
//        BinaryTree.print(x, "x");
//        System.setOut(oldOut);
//        assertEquals("x in preorder\r\nC A B E D \r\nx in inorder\r\nA B C D E \r\n\r\n".trim(),
//                     outContent.toString().trim());
    }
}
