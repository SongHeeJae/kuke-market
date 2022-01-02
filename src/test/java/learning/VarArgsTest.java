package learning;

import org.junit.jupiter.api.Test;

public class VarArgsTest {

    @Test
    void varArgsTest() {
        test1();
        System.out.println("===============");
        test1("1", "2", "3");
    }

    private void test1(Object... args) {
        System.out.println("test1 args = " + args.length + " " + args);
        test2(args);

        Object[] args2 = args;
        test2(args2);

        Object args3 = args2;
        test2(args3);
    }

    private void test2(Object... args) {
        System.out.println("test2 args = " + args.length + " " + args);
    }
}
