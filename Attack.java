//Attack.java
//Karl Zhu
//ICS 4U
//Attack class to construct each individual attack object for the pokemon's attacks
//They contain name, cost, damage, and special
import java.util.*;
public class Attack {
	//The components for each attack
	private String name;
	private int cost;
	private int damage;
	private String special;

	public Attack (String name, int cost, int damage, String special ) {
		//Constructor for an attack
		//[<attack name>, <energy cost>,<damage>,<special>]--How the attacks look like
   		this.name=name;
   		this.cost=cost;
   	   	this.damage=damage;
   		this.special=special;
	}
	
	public String getName(){//Gets the name of the attack
		return name;
	}
	
	public int getDamage(){//Gets the damage of the attack
		return damage;
	}
	
	public int getCost(){//Gets the cost of the attack
		return cost;
	}
	
	public String getSpecial(){//Gets the special feautre of the attack
		return special;
	}
}	