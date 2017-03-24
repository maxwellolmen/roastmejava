/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package helloworld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

/**
 * This sample shows how to create a simple speechlet for handling speechlet requests.
 */
public class HelloWorldSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldSpeechlet.class);

    public static String name = "Player";

    private String[] ROASTS_MAXWELL = new String[] {};
    private String[] ROASTS_KAREEM = new String[] {};
    private String[] ROASTS_BRYCE = new String[] {"If I wanted to talk to a penguin, I would go to Antarctica.", "You are a piece of ass cancer.", "Nice try getting me to roast you, you aren't even worth my time."};
    private String[] ROASTS_COLE = new String[] {};
    private String[] ROASTS_EKAANTH = new String[] {};
    private String[] ROASTS_CYNEAN = new String[] {};
    private String[] ROASTS_CYNWEI = new String[] {};
    private String[] ROASTS_NOAH = new String[] {};
    private String[] ROASTS_JACK = new String[] {};
    private String[] ROASTS_SAGE = new String[] {};
    private String[] ROASTS_KIAN = new String[] {};
    private String[] ROASTS_OTHER = new String[] {};

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("MyNameIsIntent".equals(intentName)) {
            return getNameResponse(request.getIntent(), session);
        } else if ("WhatIsMyNameIntent".equals(intentName)) {
            return getWhatIsMyNameResponse();
        } else if ("RoastMeIntent".equals(intentName)) {
            return getRoastMeResponse();
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Hey bro. Ready to get roasted? Start by telling me your name. For example, my name is John.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("WelcomeMessage");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getNameResponse(final Intent intent, final Session session) {
        String speechText = "Awesome. I now know your name. You may now say roast me at any time.";

        name = intent.getSlot("Name").getValue();

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("NameResponse");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getWhatIsMyNameResponse() {
        String speechText = "Your name is " + name;

        SimpleCard card = new SimpleCard();
        card.setTitle("WhatIsMyNameResponse");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getRoastMeResponse() {
        String speechText = getRoast(name);
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "You can say hello to me!";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private String getRoast(String name) {
        if (name.equals("Maxwell")) {

        } else if (name.equals("Kareem")) {

        } else if (name.equals("Bryce")) {

        } else if (name.equals("Cole")) {

        } else if (name.equals("Ekaanth")) {

        } else if (name.equals("Cyn-Ean")) {

        } else if (name.equals("Cyn-Wei")) {

        } else if (name.equals("Noah")) {

        } else if (name.equals("Jack")) {

        } else if (name.equals("Kian")) {

        } else if (name.equals("Sage")) {

        } else {

        }
    }
}
