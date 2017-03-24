package yahtzee;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import helloworld.HelloWorldSpeechlet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Me on 3/23/2017.
 */
public class YahtzeeSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add("amzn1.ask.skill.a726eaf6-d515-4015-9100-e4b777d3d8f8");
    }

    public YahtzeeSpeechletRequestStreamHandler() {
        super(new YahtzeeSpeechlet(), supportedApplicationIds);
    }
}
