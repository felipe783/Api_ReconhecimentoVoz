package dio.budgeting;

import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches =".+")
public class OpenAiTranscriptionModelIT {

    @Autowired
    OpenAiAudioTranscriptionModel openAiTranscriptionMode;


    @ParameterizedTest
    @CsvSource({
        "record-1.m4a, R$ 3,90",
        "record-2.m4a, 70 milhões de reais"
    })
    public void should_containExpectedKeyWords_when_audioFilesAreProcessed(String fileName, String expectedkeyWord){
        var record = new ClassPathResource("audio/" + fileName);

        var response = openAiTranscriptionMode.call(record);

        assertThat(response).contains(expectedkeyWord);
        System.out.println(response);
    }
}
