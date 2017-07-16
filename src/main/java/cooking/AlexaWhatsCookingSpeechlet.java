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
import com.amazon.speech.ui.SimpleCard;

public class AlexaWhatsCookingSpeechlet implements Speechlet {
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
        	return getStartCookingResponse(intent);
        }else if("NextStepIntent".equals(intentName)){
        	return recipe.next();
        }else if ("LastStepIntent".equals(intentName)){
        	return recipe.previous();
        }else if ("GoToFinalStepIntent".equals(intentName)){
        	return recipe.last(true);
        }else if ("HearFinalStepIntent".equals(intentName)){
        	return recipe.last(false);
        }else if ("RepeatStepIntent".equals(intentName)){
        	return recipe.repeat();
        }else if("ReplaceIntent".equals(intentName)){
        	return recipe.replace(intent);
        }else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getWelcomeResponse();
        } else {
        	return getDidNotUnderstandResponse();
        }
    }

	private SpeechletResponse getWelcomeResponse() {
    	StringBuilder speechTextSB = new StringBuilder();
    	speechTextSB.append("Welcome to My Recipe skill. \n");
    	speechTextSB.append("You can ask me to start cooking a recipe. \n");
    	speechTextSB.append("Or navigate the steps of a recipe by asking me to repeat a step, go to the next step, or go to the privious step. \n");
    	speechTextSB.append("For exsample you can say 'Alexa, I want to cook mashed potatoes from My Recipe.' \n");
    	speechTextSB.append("Or 'Alexa, what is the next step from My Recipe.' \n");
    	speechTextSB.append("Make sure you say 'from My Recipe' at the end so I can refer to your recipe. \n");
    	
        String speechText = speechTextSB.toString();

        SimpleCard card = new SimpleCard();
        card.setTitle("Welcome to My Recipe");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
    
    private SpeechletResponse getStartCookingResponse(Intent intent) {
    	recipe = new Recipe(intent.getSlot("food"));
    	
    	return recipe.start();
	}

    private SpeechletResponse getDidNotUnderstandResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }
}
