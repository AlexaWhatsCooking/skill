package materal;

public class Ingrudient {
	
	private String name;
	private double amount;
	private String unit;
	
	public Ingrudient(String name, double amount, String unit){
		this.name = name;
		this.amount = amount;
		this.unit = unit;
	}
	
	public void replace(String newName){
		this.name = newName;
	}
	
	public String getName(){
		return name;
	}
	
	public double getAmount(){
		return amount;
	}
	
	public String getUnit(){
		return unit;
	}
	
	@Override
	public String toString(){
		return Double.toString(amount)+" "+unit+" of "+name;
	}

}
