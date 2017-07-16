package cooking;

import java.util.Arrays;
import java.util.List;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

import materal.Ingrudient;

public class Recipe {
	
	private String name = "mashed potatoes";
	private List<Ingrudient> ingrudients = Arrays.asList(
			new Ingrudient("potatoes", 2, "pounds"),
			new Ingrudient("butter", 2, "tablespoons"),
			new Ingrudient("milk", .5, "cups"),
			new Ingrudient("salted water", 4, "cups")
			);
	private List<String> steps;

	private int step = -1;

	public Recipe(Slot slot) {
		name = slot.getValue();
		buildSteps();
	}

	private void buildSteps() {
		steps = Arrays.asList(
				"In a large pot, bring "+ingrudients.get(3).getName()+" to a boil",
				"Carefully put diced "+ingrudients.get(0).getName()+" in the boiling water",
				"Boil for about 10 minutes or until the "+ingrudients.get(0).getName()+" are fork tender",
				"Turn off burner and drain the "+ingrudients.get(0).getName(),
				"Add the "+ingrudients.get(1).getName()+" and "+ingrudients.get(2).getName()+" then mix with a spoon or spatula until butter is melted",
				"Mash with a potato masher or large fork until smooth and creamy");
	}

	public SpeechletResponse start() {
    	StringBuilder speachBuilder = new StringBuilder();
    	StringBuilder listOfIngrudientsBuilder = new StringBuilder();
    	speachBuilder.append("Lets start "+name+". ");
    	
    	speachBuilder.append("You will need ");
    	
    	for(int i = 0; i < ingrudients.size(); i++){
    		Ingrudient ingrudient = ingrudients.get(i);
    		String item = ingrudient.toString();
    		if(i == ingrudients.size()-1){
    			speachBuilder.append("and "+item+", ");
    			listOfIngrudientsBuilder.append(ingrudient.getName());
    		} else{
    			speachBuilder.append(item+", ");
    			listOfIngrudientsBuilder.append(ingrudient.getName()+"\n");
    		}
    	}
    	
    	String speechText = speachBuilder.toString();

        SimpleCard card = new SimpleCard();
        card.setTitle("List of ingrudients");
        card.setContent(listOfIngrudientsBuilder.toString());

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        return SpeechletResponse.newTellResponse(speech, card);
	}

	public SpeechletResponse next() {
		step++;
		
		if(step >= steps.size()){
			String speechText = "You have completed cooking "+name+". Bon appetit!";

	        SimpleCard card = new SimpleCard();
	        card.setTitle("Next Step");
	        card.setContent(speechText);

	        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
	        speech.setText(speechText);
	        
	        step = steps.size();

	        return SpeechletResponse.newTellResponse(speech, card);
		}
		
        String speechText = steps.get(step);

        SimpleCard card = new SimpleCard();
        card.setTitle("Next Step");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
	}

	public SpeechletResponse previous() {
		step--;
		StringBuilder speechTextSB = new StringBuilder();
		if(step < 0){
			speechTextSB.append("You are on the first step. ");
			step = 0;
		}
		
		speechTextSB.append(steps.get(step));
        String speechText = speechTextSB.toString();

        SimpleCard card = new SimpleCard();
        card.setTitle("Previous Step");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        return SpeechletResponse.newTellResponse(speech, card);
	}

	public SpeechletResponse repeat() {
        String speechText = steps.get(step);

        SimpleCard card = new SimpleCard();
        card.setTitle("Repeat Step");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
	}
	
	public SpeechletResponse replace(Intent intent) {
		String oldFood = intent.getSlot("oldFood").getValue();
		String newFood = intent.getSlot("newFood").getValue();
		
		String speechText = null;
		
		for(Ingrudient ing : ingrudients){
			String oldIngDis = ing.toString();
			if(ing.getName().equalsIgnoreCase(oldFood)){
				ing.replace(newFood);
				
				speechText = "Replaced "+oldIngDis+" with "+ing.toString();
			}
		}
		
		if(speechText == null){
			speechText = oldFood+" was not an ingrudient.";
		}
		
		SimpleCard card = new SimpleCard();
        card.setTitle("Replace Ingrudient");
		card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        buildSteps();
		return SpeechletResponse.newTellResponse(speech, card);
	}

	public String getName() {
		return name;
	}
}
