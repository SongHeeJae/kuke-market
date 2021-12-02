package learning;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnumToStringTest {

    public enum TestEnum{
        TEST1, TEST2
    }

    @Test
    void enumToStringTest() {
        Assertions.assertThat(TestEnum.TEST1.toString()).isEqualTo("TEST1");
        Assertions.assertThat(TestEnum.TEST2.toString()).isEqualTo("TEST2");
    }
}
