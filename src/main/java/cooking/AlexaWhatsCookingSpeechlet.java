package cooking;

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

public class AlexaWhatsCookingSpeechlet implements Speechlet {
    private static final String EXIT = "you can say quit to exit the application";
    private static final String RECIPE_CONTROL = "You can say next to go to the next step, previous to go to the previous step, repeat to repeat the step, or "+EXIT;

	private static final Logger log = LoggerFactory.getLogger(AlexaWhatsCookingSpeechlet.class);
    
    private Recipe recipe;

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
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

        if ("StartCookingIntent".equals(intentName)){
        	return getStartCookingResponse();
        }else if("NextStepIntent".equals(intentName)){
        	return recipe.next();
        }else if ("LastStepIntent".equals(intentName)){
        	return recipe.previous();
        }else if ("RepeatStepIntent".equals(intentName)){
        	return recipe.repeat();
        }else if("QuitIntent".equals(intentName)){
        	return getQuitResponse();
        }else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
        	return getHelpResponse();
        }
    }

    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Welcome to the Alexa what's cooking. Say a dish you would like to prepare.";

        SimpleCard card = new SimpleCard();
        card.setTitle("Welcome Card");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
    
    private SpeechletResponse getStartCookingResponse() {
    	if(recipe != null){
    		return getRecipeControls("You are already cooking "+recipe.getName()+". ");
    	}
    	
    	recipe = new Recipe();
    	
    	return recipe.start();
	}

    private SpeechletResponse getHelpResponse() {
    	if(recipe != null){
    		return getRecipeControls("");
    	} else {
    		return getMenuControls();
    	}
    }
    
	private SpeechletResponse getMenuControls() {
		String speechText = "Say a dish you want to cook. You can say some thing like mashed potatoes, or "+EXIT;

        SimpleCard card = new SimpleCard();
        card.setTitle("Menu Controls");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	private SpeechletResponse getRecipeControls(String prepend) {
		String speechText = prepend + RECIPE_CONTROL;

        SimpleCard card = new SimpleCard();
        card.setTitle("Menu Controls");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	private SpeechletResponse getQuitResponse() {
		String speechText = "Enjoy your meal!";

        SimpleCard card = new SimpleCard();
        card.setTitle("Good bye");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
	}
    
	@Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }
}
