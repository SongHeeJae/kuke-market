package learning;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class GenericTypeTest {
    interface TestInter<T> {
        default void test(T t) {
            System.out.println("t.getClass() = " + t.getClass());
        }
    }

    class TestInterLongImpl implements TestInter<Long> { }

    @Test
    void typeTest() {
        TestInterLongImpl longImpl = new TestInterLongImpl();
        ReflectionTestUtils.invokeMethod(longImpl, "test", 1L);
        ReflectionTestUtils.invokeMethod(longImpl, "test", "string value");
    }

}
