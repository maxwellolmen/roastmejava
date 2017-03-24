package yahtzee;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import helloworld.HelloWorldSpeechlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Me on 3/23/2017.
 */
public class YahtzeeSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldSpeechlet.class);

    public static boolean inGame = false;

    public static boolean rollPending = false;

    public static int players = 0;

    public static int turn = 0;

    public static int[] dice;

    public static boolean[] savedDice;

    public static YahtzeeScores[] scorePlayers;

    public static List<String> PLAYS = Arrays.asList(new String[] {"aces", "twos", "threes", "fours", "fives", "sixes", "three of a kind", "four of a kind", "full house", "small straight", "large straight", "chance", "yahtzee"});
    public static List<String> DIETHS = Arrays.asList(new String[] {"first", "second", "third", "fourth", "fifth"});

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

        if ("PlayerAmountIntent".equals(intentName)) {
            return getPlayerAmountResponse(request.getIntent());
        } else if ("StartGameIntent".equals(intentName)) {
            return getStartGameResponse();
        } else if ("RollDiceIntent".equals(intentName)) {
            return getRollDiceResponse();
        } else if ("RepeatDiceIntent".equals(intentName)) {
            return getRepeatDiceResponse();
        } else if ("SaveDieIntent".equals(intentName)) {
            return getSaveDieResponse(request.getIntent());
        } else if ("PlayDiceIntent".equals(intentName)) {
            return getPlayDiceResponse(request.getIntent());
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

    private void rollDice() {
        for (int i = 0; i < 5; i++) {
            if (!savedDice[i]) {
                dice[i] = new Random().nextInt(6) + 1;
            }
        }

        rollPending = false;
        scorePlayers[turn].rolls--;
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "";

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

    private SpeechletResponse getPlayerAmountResponse(final Intent intent) {
        try {
            players = Integer.parseInt(intent.getSlot("Amount").getValue());
        } catch (NumberFormatException ex) {
            String speechText = "I didn't understand that number.";

            SimpleCard card = new SimpleCard();
            card.setTitle("MisunderstandingMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        if (players < 2 || players > 8) {
            String speechText = "You must have between two and eight players.";

            SimpleCard card = new SimpleCard();
            card.setTitle("InvalidAmountMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        String speechText = "Awesome. I now know that there are " + players + " players. You may start the game by saying start game.";

        SimpleCard card = new SimpleCard();
        card.setTitle("AmountConfirmationMessage");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getStartGameResponse() {
        if (inGame) {
            String speechText = "You're already in a game.";

            SimpleCard card = new SimpleCard();
            card.setTitle("AlreadyGameMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        inGame = true;
        scorePlayers = new YahtzeeScores[players];

        for (int i = 0; i < players; i++) {
            scorePlayers[i] = new YahtzeeScores();
        }

        rollPending = true;

        dice = new int[] {-1, -1, -1, -1, -1};
        savedDice = new boolean[] {false, false, false, false, false};

        String speechText = "Let's go! Let me explain the basic rules. For every turn, you must say, roll. I will tell you your dice in order, and you may tell me to save a die by saying, save the third die. You may also tell me, repeat the dice, if you missed them the first time. Once you're ready to make a play with your dice, say, play full house. I will tell you what you scored from the play, and your turn will end. Have fun! Player one, your turn.";

        SimpleCard card = new SimpleCard();
        card.setTitle("GameIntroductionMessage");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getRollDiceResponse() {
        if (!inGame) {
            String speechText = "You're not currently in game.";

            SimpleCard card = new SimpleCard();
            card.setTitle("NotGameMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        if (scorePlayers[turn].rolls == 0) {
            String speechText = "You've already rolled three times. Play your dice by saying, play full house.";

            SimpleCard card = new SimpleCard();
            card.setTitle("AlreadyRolledMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        rollDice();

        String speechText = "You now have " + dice[0] + ", " + dice[1] + ", " + dice[2] + ", " + dice[3] + ", " + dice[4] + ", and " + dice[5] + ".";

        SimpleCard card = new SimpleCard();
        card.setTitle("DiceMessage");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getRepeatDiceResponse() {
        if (!inGame) {
            String speechText = "You're not currently in game.";

            SimpleCard card = new SimpleCard();
            card.setTitle("NotGameMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        if (rollPending) {
            String speechText = "You haven't rolled yet.";

            SimpleCard card = new SimpleCard();
            card.setTitle("NotRolledMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        String speechText = "You now have " + dice[0] + ", " + dice[1] + ", " + dice[2] + ", " + dice[3] + ", " + dice[4] + ", and " + dice[5] + ".";

        SimpleCard card = new SimpleCard();
        card.setTitle("DiceMessage");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getSaveDieResponse(final Intent intent) {
        if (!inGame) {
            String speechText = "You're not currently in game.";

            SimpleCard card = new SimpleCard();
            card.setTitle("NotGameMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        if (rollPending) {
            String speechText = "You haven't rolled yet.";

            SimpleCard card = new SimpleCard();
            card.setTitle("NotRolledMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        if (!DIETHS.contains(intent.getSlot("Dieth").getValue())) {
            String speechText = "That is not a valid die to save or unsave.";

            SimpleCard card = new SimpleCard();
            card.setTitle("InvalidDieMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        int dieth = diethFromString(intent.getSlot("Dieth").getValue());

        savedDice[dieth] = !savedDice[dieth];

        String speechText = "The " + intent.getSlot("Dieth").getValue() + " die is now " + (savedDice[dieth] ? "saved" : "unsaved");

        SimpleCard card = new SimpleCard();
        card.setTitle("DieSavedMessage");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getPlayDiceResponse(final Intent intent) {
        if (!inGame) {
            String speechText = "You're not currently in game.";

            SimpleCard card = new SimpleCard();
            card.setTitle("NotGameMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        if (rollPending) {
            String speechText = "You haven't rolled yet.";

            SimpleCard card = new SimpleCard();
            card.setTitle("NotRolledMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        if (!PLAYS.contains(intent.getSlot("Play").getValue())) {
            String speechText = "That is not a valid play.";

            SimpleCard card = new SimpleCard();
            card.setTitle("InvalidPlayMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        String play = intent.getSlot("Play").getValue();

        if ((play.equals("three of a kind") || play.equals("four of a kind")) && intent.getSlot("Die") == null) {
            String speechText = "You must specify the die you'd like to use. For example, play three of a kind with threes.";

            SimpleCard card = new SimpleCard();
            card.setTitle("NoDieSpecifiedMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        int type = -1;

        if (play.equals("three of a kind") || play.equals("four of a kind")) {
            type = stringToType(intent.getSlot("Die").getValue());

            if (type == -1) {
                String speechText = "You specified an invalid die number.";

                SimpleCard card = new SimpleCard();
                card.setTitle("InvalidDieMessage");
                card.setContent(speechText);

                PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
                speech.setText(speechText);

                Reprompt reprompt = new Reprompt();
                reprompt.setOutputSpeech(speech);

                return SpeechletResponse.newAskResponse(speech, reprompt, card);
            }
        }

        YahtzeeScores scorePlayer = scorePlayers[turn];

        boolean success = scorePlayer.requestPlay(play, dice, type);

        if (!success) {
            String speechText = "You may no longer make that play.";

            SimpleCard card = new SimpleCard();
            card.setTitle("InvalidDieMessage");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }

        int lastAdd = scorePlayer.lastAdd;

        scorePlayer.rolls = 3;

        String speechText = "That play gave you " + lastAdd + " points.";

        rollPending = true;
        savedDice = new boolean[] {false, false, false, false, false};

        if (turn > players - 2) {
            turn = 0;
        } else {
            turn++;
        }

        if (isOver()) {
            return endGame();
        }

        while (scorePlayers[turn].turnsLeft == 0) {
            if (turn > players - 2) {
                turn = 0;
            } else {
                turn++;
            }
        }

        speechText+=" Player " + (turn + 1) + ", your turn.";

        SimpleCard card = new SimpleCard();
        card.setTitle("InvalidDieMessage");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public int stringToType(String string) {
        if (string.equals("aces")) {
            return 1;
        } else if (string.equals("twos")) {
            return 2;
        } else if (string.equals("threes")) {
            return 3;
        } else if (string.equals("fours")) {
            return 4;
        } else if (string.equals("fives")) {
            return 5;
        } else if (string.equals("sixes")) {
            return 6;
        }

        return -1;
    }

    public int diethFromString(String string) {
        if (string.equals("first")) {
            return 1;
        } else if (string.equals("second")) {
            return 2;
        } else if (string.equals("third")) {
            return 3;
        } else if (string.equals("fourth")) {
            return 4;
        } else if (string.equals("fifth")) {
            return 5;
        }

        return -1;
    }

    public boolean isOver() {
        for (YahtzeeScores scorePlayer : scorePlayers) {
            if (scorePlayer.turnsLeft > 0) {
                return false;
            }
        }

        return true;
    }

    public SpeechletResponse endGame() {
        String speechText = "";

        for (int i = 0; i < players; i++) {
            YahtzeeScores scorePlayer = scorePlayers[i];

            String playerSpeech = " Player " + i + ", you " + (scorePlayer.tallyUpperSection() >= 63 ? "did " : "did not ") + "receive the upper section bonus points. Your final score is " + (scorePlayer.tallyUpperSection() + scorePlayer.tallyLowerSection() + scorePlayer.getBonus());

            speechText+=playerSpeech;
        }

        speechText+=" Thank you for playing! This game was created by Maxwell Olmen.";

        SimpleCard card = new SimpleCard();
        card.setTitle("FinalScoreMessage");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        SpeechletResponse sr = SpeechletResponse.newAskResponse(speech, reprompt, card);
        sr.setShouldEndSession(true);

        return sr;
    }
}
