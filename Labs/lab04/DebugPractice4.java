public class DebugPractice4 {
    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int c = badMax(a, b); // Accidentally stepped in here.
        int d = 3;            // We'd like to finish executing badMax and pause here.
    }

    public static int badMax(int a, int b) {
        int g = 1;
        return 2;
    }
}