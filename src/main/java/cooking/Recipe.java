package cooking;

import java.util.Arrays;
import java.util.List;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class Recipe {
	
	private String name;
	private List<String> ingrudients = Arrays.asList(
			"two pounds of potatos", 
			"two tablespoons of butter", 
			"half a cup of milk", 
			"salt");
	private List<String> steps = Arrays.asList(
			"In a large pot, bring 4 cups of salted water to a boil",
			"Carefully put diced potatoes in the boiling water",
			"Boil for about 10 minutes or until the potatoes are fork tender",
			"Turn off burner and drain the potatoes",
			"Add the butter and milk then mix with a spoon or spatula until butter is melted",
			"Mash with a potato masher or large fork until smooth and creamy");

	private int step = -1;

	public SpeechletResponse start() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Lets start "+name+". ");
    	
    	sb.append("You will need ");
    	
    	for(int i = 0; i < ingrudients.size(); i++){
    		String item = ingrudients.get(i);
    		if(i == ingrudients.size()-1){
    			sb.append("and "+item+", ");
    		} else{
    			sb.append(item+", ");
    		}
    	}
    	
    	String speechText = sb.toString();

        SimpleCard card = new SimpleCard();
        card.setTitle("Lets start cooking Card");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	public SpeechletResponse next() {
		step++;
        String speechText = steps.get(step);

        SimpleCard card = new SimpleCard();
        card.setTitle("Next Step");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	public SpeechletResponse previous() {
		step--;
        String speechText = steps.get(step);

        SimpleCard card = new SimpleCard();
        card.setTitle("Previous Step");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	public SpeechletResponse repeat() {
        String speechText = steps.get(step);

        SimpleCard card = new SimpleCard();
        card.setTitle("Repeat Step");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	public String getName() {
		return name;
	}
}
