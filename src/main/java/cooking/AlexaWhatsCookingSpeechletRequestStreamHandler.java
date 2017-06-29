package cooking;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public final class AlexaWhatsCookingSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        supportedApplicationIds.add("amzn1.ask.skill.831645a3-c65d-41a9-882d-4ab93b0be1a2");
    }

    public AlexaWhatsCookingSpeechletRequestStreamHandler() {
        super(new AlexaWhatsCookingSpeechlet(), supportedApplicationIds);
    }
}
