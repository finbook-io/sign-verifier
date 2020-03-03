import io.finbook.TextGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(org.junit.runners.Parameterized.class)
public class TextGenerator_ {

    private static Object[][] cases;

    static {
        cases = new Object[][] {
                {"Man", "TWFu"},
                {"any carnal pleasure.", "YW55IGNhcm5hbCBwbGVhc3VyZS4="},
                {"any carnal pleasure", "YW55IGNhcm5hbCBwbGVhc3VyZQ=="}
        };
    }

    private String originalText;
    private String base64Text;

    public TextGenerator_(String originalText, String base64Text) {
        this.originalText = originalText;
        this.base64Text = base64Text;
    }

    @Test
    public void execute(){
        assertThat(TextGenerator.getBase64TextFrom(originalText)).isEqualTo(base64Text);
    }

    @Parameterized.Parameters
    public static Object[][] cases() {
        return cases;
    }
}
