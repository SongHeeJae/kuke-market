package learning;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetClassTest {

    class MyObj { }

    @Test
    void getClassTest() {
        Object obj = new MyObj();
        Class<?> clazz = obj.getClass();
        Assertions.assertThat(clazz.getSimpleName()).isEqualTo("MyObj");
    }
}
