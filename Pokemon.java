//Pokemon.java
//Karl Zhu
//ICS 4U
//These are pokemon objects that have the information for each pokemon in the loaded data file
//They contain name, HP, type, resistance, weakness, and the attacks
import java.util.*;
import java.io.*;
public class Pokemon{
	//Stats of the pokemon
	private String name;
	private int maxHP;
	private int HP;
	private int energy;
	private String type;
	private String resistance;
    private String weakness;
    private int numAttacks;
    private ArrayList <Attack> attackList = new ArrayList<Attack> ();//The attacks that the pokemon can do
 	private boolean stun;
 	private boolean disable;

    public Pokemon (String information){
    	//Constructor
    	String info[]= information.split(",");//The string of the pokemon stats that is given to us in the loading file
    	//Comes out like this:
    	//String name, int HP, String type, String resistance, String weakness, int numAttacks, ArrayList<Attack> AttackList
   		//We split the info string and assign the variables to each part
   		this.name=info[0];
   		maxHP=Integer.parseInt(info[1]);
   		HP=Integer.parseInt(info[1]);
   		energy=50;//All pokemon start with 50 energy
   		type=info[2];
   		resistance=info[3];
   		weakness=info[4];
   		numAttacks=Integer.parseInt(info[5]);
   		for (int i=1; i<=numAttacks; i++){//Parts of the attacks
   			int a=4*i+2;
   			int b=4*i+3;
   			int c=4*i+4;
   			int d=4*i+5;
			attackList.add(new Attack(info[a], Integer.parseInt(info[b]), Integer.parseInt(info[c]), info[d]));//Each attack for the pokemon
   		}
   		//Not stunned and disabled at the beginning
   		stun=false;
   		disable=false;
	}
	
	public String getName(){//Gets the name of the pokemon
		return name;
	}
	
	public int getHP(){//Gets the current HP for the pokemon
		return this.HP;
	}
	
	public int getMaxHP(){//Gets the maximum HP the pokemon could have
		return maxHP;
	}
	
	public void setHP(int h){//Sets the pokemon's HP to a given value
		this.HP = h;
	}
	
	public String getType(){//Gets the type for the pokemon
		return type;
	}
	
	public int getEnergy(){//Gets the current energy for the pokemon
		return energy;
	}
	
	public void setEnergy(int e){//Sets the pokemon's energy to a given value
		energy = e;
	}
	public ArrayList<Attack> getAttacks(){//Gets the possible attacks for the pokemon
		return attackList;
	}
	
	public boolean isStunned(){//Gets the stun indicator for the pokemon
		return stun;
	}
	public void stunned(){//Stuns the pokemon
		stun=true;
	}
	
	public void unStun(){//Makes the stun indicator false
		stun=false;
	}
	
	public boolean isDisabled(){//Gets the disable indicator for the pokemon
		return disable;
	}
	
	public void disabled(){//We disable a pokemon
		disable=true;
	}
	
	public void unDisable(){//Makes the disable indicator false
		disable=false;
	}
	
	public boolean isFainted(){//Checks to see if the trainer's pokemon is fainted or not
		if(this.HP<=0){//HP is less than 0
			System.out.println("Your "+this.name+" is dead. Choose another pokemon.");
			return true;
		}
		
		return false;
	}
	
	public boolean eFainted(){//If the enemy's pokemon is fainted or not
		if(this.HP<=0){//HP is less than 0
			System.out.println("The wild "+this.name+" is knocked out. Good job!.");
			return true;
		}
		
		return false;
	}
	
	public int attack (int indexAttack, Pokemon OPP) {//Attack method to attack the opponent's pokemon
		//The attack's index and the opponent pokemon
		Attack att=attackList.get(indexAttack);//Gets the attack from the possible attacklist that the user wants
		double damage=att.getDamage();//Our current damage
		double multiplier = 1;//Current multiplier to our damage. 
		//New boolean to see if the special attacks by chance will activate or not
		Random rnd = new Random();
		boolean special =rnd.nextBoolean();
		
		if (OPP.resistance.equals(type)) {//If this attack is not effective to the opponent pokemon
			multiplier=multiplier/2;//Only half of the damage will be dealt
			System.out.println("It's not very effective...");
		}
		
		if (OPP.weakness.equals(type)) {//If this attack is super effective to the opponent pokemon
			multiplier*=2;//Twice the damage will be dealt!
			System.out.println("It's super effective!");
		}
		
		if (isDisabled()){//Checks if the pokemon is disabled 
			damage=Math.min(damage-10, att.getDamage());
		}
		
		if (att.getSpecial().equals("stun")){//If the special is stun
			//50/50 percent chance of being stunned and not being able to attack or retreat
			if (special){
				OPP.stunned();
				System.out.printf("%s was stunned!\n", OPP.getName());
			}
			else{
				System.out.println("Nothing special happened!");
			}	
		}	
		
		if (att.getSpecial().equals("wild card")){//Special is wild card
			//50/50 chance that the attack may be canceled out
			if (special){
				System.out.println("No damage was done as it was canceled by the special attack!");
				multiplier=0;
			}
			else{
				System.out.println("Nothing special happened!");
			}		
		}
		
		if (att.getSpecial().equals("wild storm")){//Special is wild storm
			int storm=0;//Additional "wild card" multiplier that we can increase onto
			while (true){//Until we do not get a false
				boolean wildCard =rnd.nextBoolean();//Indicator to see if we go on or not
				if (wildCard){
					storm+=1;
				}
				else{//Break when we get a false
					break;
				}
			}
			System.out.printf("The attack's damage was increased by a factor of %d!\n", storm);
			multiplier+=storm;//We add on the additional to the overall mutliplier		
		}
		
		if (att.getSpecial().equals("disable")){//Special is disable
			//If the pokemon has yet to be disabled, it is now
			if (OPP.isDisabled()) {
				System.out.printf("%s was already disabled!\n", OPP.getName());	
			}
			else {
				System.out.printf("%s was disabled!\n", OPP.getName());
				OPP.disabled();
			}
		}
		
		if (att.getSpecial().equals("recharge")){//If the special is recharge
			//We add 20 energy to the pokemon, to a maximum of 50
			if (OPP.getEnergy()+20<=50){
				OPP.setEnergy(OPP.getEnergy()+20);
			}
			else{
				OPP.setEnergy(50);
			}
		}
		double totalDamage= damage*multiplier;//The total damage that will be delath	
		this.energy -= att.getCost();//We subtract the energy cost of the attack from the pokemon's energy
		OPP.setHP(OPP.HP-(int)totalDamage);//We subtract the total damage from the opponent pokemon
		return (int)totalDamage;//Returns the total damage.
	}
}