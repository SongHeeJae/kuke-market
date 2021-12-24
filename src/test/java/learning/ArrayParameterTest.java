package learning;


import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ArrayParameterTest {

    static class MyTest {
        public void test() {
            System.out.println("Test.test1");
        }
        public void test(Integer... integers) {
            System.out.println("Test.test2");
        }
    }

    @Test
    void typeTest() {
        MyTest myTest = new MyTest();
        myTest.test();
        myTest.test(1);
    }
}
