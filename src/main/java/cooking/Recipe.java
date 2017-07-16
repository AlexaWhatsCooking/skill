package cooking;

import java.util.Arrays;
import java.util.List;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

import materal.Ingredient;

public class Recipe {
	
	private String name = "mashed potatoes";
	private List<Ingredient> ingredients = Arrays.asList(
			new Ingredient("potatoes", 2, "pounds"),
			new Ingredient("butter", 2, "tablespoons"),
			new Ingredient("milk", .5, "cups"),
			new Ingredient("salted water", 4, "cups")
			);
	private List<String> steps;

	private int step = -1;

	public Recipe(Slot slot) {
		name = slot.getValue();
		buildSteps();
	}

	private void buildSteps() {
		steps = Arrays.asList(
				"In a large pot, bring "+ingredients.get(3).getName()+" to a boil",
				"Carefully put diced "+ingredients.get(0).getName()+" in the boiling water",
				"Boil for about 10 minutes or until the "+ingredients.get(0).getName()+" are fork tender",
				"Turn off burner and drain the "+ingredients.get(0).getName(),
				"Add the "+ingredients.get(1).getName()+" and "+ingredients.get(2).getName()+" then mix with a spoon or spatula until butter is melted",
				"Mash with a potato masher or large fork until smooth and creamy");
	}

	public SpeechletResponse start() {
    	StringBuilder speachBuilder = new StringBuilder();
    	StringBuilder listOfIngredientsBuilder = new StringBuilder();
    	speachBuilder.append("Lets start cooking "+name+". ");
    	
    	speachBuilder.append("You will need ");
    	
    	for(int i = 0; i < ingredients.size(); i++){
    		Ingredient ingredient = ingredients.get(i);
    		String item = ingredient.toString();
    		if(i == ingredients.size()-1){
    			speachBuilder.append("and "+item+", ");
    			listOfIngredientsBuilder.append(ingredient.toString());
    		} else{
    			speachBuilder.append(item+", ");
    			listOfIngredientsBuilder.append(ingredient.toString()+"\n");
    		}
    	}
    	
    	String speechText = speachBuilder.toString();

        SimpleCard card = new SimpleCard();
        card.setTitle("List of ingredient");
        card.setContent(listOfIngredientsBuilder.toString());

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        return SpeechletResponse.newTellResponse(speech, card);
	}

	public SpeechletResponse next() {
		step++;
		
		if(step >= steps.size()){
			String speechText = "You have completed cooking "+name+". Bon appetit!";

	        SimpleCard card = new SimpleCard();
	        card.setTitle("Step "+step);
	        card.setContent(speechText);

	        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
	        speech.setText(speechText);
	        
	        step = steps.size();

	        return SpeechletResponse.newTellResponse(speech, card);
		}
		
        String speechText = steps.get(step);

        SimpleCard card = new SimpleCard();
        card.setTitle("Step "+step);
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
        card.setTitle("Step "+step);
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        return SpeechletResponse.newTellResponse(speech, card);
	}

	public SpeechletResponse repeat() {
        String speechText = steps.get(step);

        SimpleCard card = new SimpleCard();
        card.setTitle("Step "+step);
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
	}
	
	public SpeechletResponse replace(Intent intent) {
		String oldFood = intent.getSlot("oldFood").getValue();
		String newFood = intent.getSlot("newFood").getValue();
		
		String speechText = null;
		
		for(Ingredient ing : ingredients){
			String oldIngDis = ing.toString();
			if(ing.getName().equalsIgnoreCase(oldFood)){
				ing.replace(newFood);
				
				speechText = "Replaced "+oldIngDis+" with "+ing.toString();
			}
		}
		
		if(speechText == null){
			speechText = oldFood+" was not an ingredient.";
		}
		
		SimpleCard card = new SimpleCard();
        card.setTitle("Replace Ingredient");
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
