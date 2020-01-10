//PokemonArena.java
//Karl Zhu
//Support received from Mr. McKenzie
//ICS 4U
//Pokemon Arena Text Game
//This game allows users to pick 4 pokemon from a list of pokemon, and rotates the others not chosen for battle against the user
//If they defeat all the others, they will be the Trainer Supreme
//To do so, they have individual battles, where a user pokemon and the opponent pokemon exchange actions 
//(attack, retreat-only for user, and pass) in a series of rounds
import java.util.*;
import java.io.*;

public class PokemonArena {
	private static ArrayList <Pokemon> pokeList = new ArrayList<Pokemon> ();//The entire world of pokemon
	private static ArrayList <Pokemon> trainerList = new ArrayList<Pokemon> ();//The pokemon the trainer has
	private static ArrayList <Attack> availableOPP = new ArrayList< Attack> ();//The available attacks that the enemy pokemon has 
	private static int pos;//What our current trainer pokemon is located
	private static boolean alive;//We are starting the game as alive
	private static String username;//The name of our user

	public static void loadPokemon(){
		try{
			File Poke = new File ("pokemon.txt");//Finds the file of pokemon
			Scanner infile= new Scanner (new BufferedReader(new FileReader(Poke)));
			int numofPokemon=Integer.parseInt(infile.nextLine());//How many pokemon are in data file
			for (int i=0; i<numofPokemon; i++){//Adds all the pokemon to our PokeList
				pokeList.add(new Pokemon(infile.nextLine()));
			}
		}
		catch(IOException ex){//If we can't find one
			System.out.println("Where is your pokemon loader file?");
		}
	}

	public static void delay(long n){//Waits for a certain time before doing an action
		try{
			Thread.sleep(n);//Waits n milliseconds
		}
		catch(InterruptedException ex ){//Not a valid time
			System.out.println("Not a valid time to wait");
		}
	}
	
	public static void choosePokemon(){//The trainer will be allowed to choose their pokemon for the arena battles
		System.out.println("Choose four pokemon with you to embark on this journey!");
		delay(500);
		Scanner kb= new Scanner (System.in);
		for (int i=0; i<pokeList.size(); i++){//Outputs the pokemon in the PokeList, their HP's and their type
			System.out.printf("%d. |%-10s|", i + 1, pokeList.get(i).getName());
			System.out.print(" HP: "+Integer.toString(pokeList.get(i).getHP()));
			System.out.printf(" Type: %s\n", pokeList.get(i).getType());
			delay(50);
		}
		int selection=0;//Number of pokemon chosen already
		int want;
		while (selection<4){//We cannot choose more than 4 pokemon
			System.out.println("Enter the corresponding number for the pokemon that you desire");
			//User enters a valid number for their pokemon
			try{//Gets valid input
				want=kb.nextInt();
			}
			catch (InputMismatchException ex){//Gives another chance to the invalid input
				want = 0;
				kb.next();
			}
			if(want >=1 && want<=pokeList.size()) {//We already chose that pokemon at the location
				if(trainerList.contains(pokeList.get(want-1))){
					System.out.println("You already chose that Pokemon. Please choose again. ");
				}
				else{//If it is not yet chosen, we add that to our trainer's list
					trainerList.add(pokeList.get(want-1));
					selection+=1;
				}
			}
			else {//If the choice is not a location in the pokelist
				System.out.println("There isn't a Pokemon available there. Please choose again.");
			}
		}

		System.out.println(username+", here is your team!");
		for (int k=0; k<trainerList.size(); k++){//The team for the trainer
			//Name and stats
			System.out.printf("%d. %s",  k+ 1, trainerList.get(k).getName());
			System.out.print(" HP: "+Integer.toString(trainerList.get(k).getHP()));
			System.out.printf(" Type: %s\n", trainerList.get(k).getType());
			pokeList.remove(trainerList.get(k));
		}
		Collections.shuffle(pokeList);//Shuffles the order of the other opponent pokemon within the list of pokemon 
		//so we can take the first one and have it as our enemy
	}
	
	public static void pass(Pokemon poke){//Pass method
		//Nothing happens for the pokemon, and it is now the opposing side's turn
		System.out.println(poke.getName()+" passed!");
	}
	
    public static void chooseMember(){//Choose a pokemon to attack
		Scanner kb= new Scanner (System.in);
		int want;//The index of the pokemon that the user chooses within their list
		System.out.println("Choose your pokemon to attack! Enter the corresponding number to the pokemon desired.");
   		for (int i=0; i<trainerList.size(); i++){
   			//Outputs the stats of all the trainer's pokemon
   			System.out.printf("%d. %s",  i+ 1, trainerList.get(i).getName());
   			System.out.printf(" HP: %d/%d ",trainerList.get(i).getHP(), trainerList.get(i).getMaxHP());
			System.out.printf(" Energy: %d/50\n",trainerList.get(i).getEnergy());
   		}
   		int selection=0;//Number of pokemon chosen already
   		while (selection<1){//We choose one pokemon only to use
	   		try{//Valid input
				want=kb.nextInt();
				pos=want-1;
			}
			catch (InputMismatchException ex){
				want = 0;
				kb.next();
			}
			if(want >=1 && want<=trainerList.size()) {//Our choice is within the boundaries of the user's list of pokemon
				String chosen=trainerList.get(pos).getName();//Name of the chosen pokemon
	   			System.out.println(chosen+", I choose you!");
	   			alive=true;//We are alive now that we have a pokemon to attack
	   			selection+=1;
			}
			else {//If the choice is not a location in the pokelist
				System.out.println("There isn't a Pokemon available there. Please choose again.");
			}
   		}    
    }
    
    public static void chooseAction(){//Choose from one of 3 possible actions
    	//Attack, retreat and pass
		System.out.println("What do you want to do? Enter the corresponding number to the action desired.");
		System.out.println("1. Attack");
		System.out.println("2. Retreat");
		System.out.println("3. Pass");
		Scanner kb= new Scanner (System.in);
		int choice;
		try{
			choice=kb.nextInt();
		}
		catch (InputMismatchException ex){
			choice = 0;
			kb.next();
		}
		
		//Gets the current user and enemy pokemon
		Pokemon goodPoke = trainerList.get(pos);
		Pokemon enemy = pokeList.get(0);
		ArrayList<Attack>attks = goodPoke.getAttacks();//All of the attacks possible for the current pokemon
		if (choice==1){//If the user chooses attack
				//The stats of the user's pokemon and the enemy pokemon
				System.out.printf("%s.", goodPoke.getName());
				System.out.printf(" HP: %d/%d ",goodPoke.getHP(), goodPoke.getMaxHP());
				System.out.printf(" Energy: %d/50 ",goodPoke.getEnergy());
				System.out.printf(" Type: %s\n", goodPoke.getType());
				System.out.printf("%s.", enemy.getName());
				System.out.printf(" HP: %d/%d ", enemy.getHP(), enemy.getMaxHP());
				System.out.printf(" Energy: %d/50 ", enemy.getEnergy());
				System.out.printf(" Type: %s\n", enemy.getType());
				System.out.print("Which attack do you want? Enter the corresponding number to the attack desired.\n");
				//The possible attacks for the user to do on the current pokemon
				for (int i=0; i<attks.size(); i++){
					System.out.printf("%d. %s. Energy Cost: %d. Damage: %d Special: %s\n",  i+ 1, attks.get(i).getName(), attks.get(i).getCost(), attks.get(i).getDamage(), attks.get(i).getSpecial());
				}
				System.out.printf("%d. Exit\n", attks.size()+1);//Option to go back to the action menu
				int want;//What option the user wants
				try{//Valid Input
					want=kb.nextInt();
				}
				catch (InputMismatchException ex){
					want = 0;
					kb.next();
				}
				if(want==attks.size()+1){//Exit attack option and go to choose options menu
					chooseAction();
				}
				else{//Choosing an attack
					if (goodPoke.isStunned()){//Checks if the pokemon is stunned and if it is, it cannot attack
						System.out.println("You are stunned! Nothing happened.");
						goodPoke.unStun();//Unstuns the pokemon
					}
					else{//We can attack
						if (attks.get(want-1).getCost()<=goodPoke.getEnergy()){//Enough energy to attack, then we attack the enemy
							System.out.printf("%s used %s!\n", goodPoke.getName(), attks.get(want-1).getName());
							int damage= goodPoke.attack(want-1, enemy);
							System.out.printf("%s lost %d HP!\n", enemy.getName(), damage);	
						}	
						else{//Not enough energy to use the attack
							System.out.println("You don't have enough energy to attack");
							chooseAction();//Choose another option
						}
					}	
				}	
			}	
		
		if (choice==2){//Retreat option
			if (goodPoke.isStunned()){//Checks if the pokemon is stunned and if it is, it cannot attack
				System.out.println("Stunned! Nothing happened.");
				goodPoke.unStun();
			}
			else{//We can retreat
				if (trainerList.size()>1){//If there are other pokemon
					int newOne;//The index of the pokemon that the user chooses within their list
					System.out.println("Choose your pokemon to attack! Enter the corresponding number to the pokemon desired.");
		   			for (int i=0; i<trainerList.size(); i++){
		   				//Outputs the stats of all the trainer's pokemon
		   				System.out.printf("%d. %s",  i+ 1, trainerList.get(i).getName());
		   				System.out.printf(" HP: %d/%d ",trainerList.get(i).getHP(), trainerList.get(i).getMaxHP());
						System.out.printf(" Energy: %d/50\n",trainerList.get(i).getEnergy());
		   			}
		   			try{//Valid input
						newOne=kb.nextInt();
					}
					catch (InputMismatchException ex){
						newOne = 0;
						kb.next();
					}
					if (trainerList.get(newOne-1)==goodPoke){//It's the same pokemon, so choose another action
						System.out.println("Still your same pokemon...");
						chooseAction();
					}
					else{//Different Pokemon
						pos=newOne-1;
						String chosen=trainerList.get(pos).getName();//Name of the chosen pokemon
		   				System.out.println(chosen+", I choose you!");
		   				alive=true;//We are alive now that we have a pokemon to attack
					}
				}
				else{//If not, we can't and the only option would be to choose another action
					System.out.println("You do not have any other pokemon to swtich to.");
					chooseAction();
				}
			}	
		}
		
		if (choice==3){//Pass option
			goodPoke.unStun();
			pass(goodPoke);
		}
    }
    
    public static void generateEnemy(){//Generates an random enemy to attack
		System.out.println("Get ready for your opponent!");
		Pokemon enemy=pokeList.get(0);//First enemy within the shuffled list of pokemon
		//The enemy pokemon's name and stats
		System.out.println("Your opponent is "+enemy.getName()+"!");
		System.out.printf("%s.", enemy.getName());
		System.out.printf(" HP: %d/%d ", enemy.getHP(), enemy.getMaxHP());
		System.out.printf(" HP: %d/50 ",enemy.getEnergy());
		System.out.printf(" Type: %s\n", enemy.getType());
    }
    
	public static void pokeDead(){//Checks to see if any pokemon in the round are dead
		if (trainerList.get(pos).isFainted()) {//User's pokemon is dead
			trainerList.remove(pos);//No longer part of our team
			if (trainerList.size()==0){//If we have no more pokemon we are done
				alive=false;
			}
			else{
				chooseMember();//If we do, we just choose another pokemon to attack
			}
		}
		
		else if (pokeList.get(0).eFainted()) {//Enemy pokemon is dead
			pokeList.remove(0);//Removes that pokemon from the pokelist
			alive=false;//Resets the battle
		}
		
		else{//If both are stll alive, we output the current stats of both pokemon in the battle
			System.out.printf("%s.", trainerList.get(pos).getName());
			System.out.printf(" HP: %d/%d ",trainerList.get(pos).getHP(), trainerList.get(pos).getMaxHP());
			System.out.printf(" Energy: %d/50\n",trainerList.get(pos).getEnergy());
			System.out.printf("%s.", pokeList.get(0).getName());
			System.out.printf(" HP: %d/%d ", pokeList.get(0).getHP(), pokeList.get(0).getMaxHP());
			System.out.printf(" Energy: %d/50\n", pokeList.get(0).getEnergy());
		}
	}
	
	public static void enemyAttack(Pokemon OPP){//Generates an atack for the enemy pokemon
		ArrayList<Attack>cando= new ArrayList<Attack>();//All of the attacks the enemy can do
		ArrayList<Attack>attks = OPP.getAttacks();//Gets all the possible attacks for the enemy
		for (int i=0; i<attks.size(); i++){//Looks and sees if any of the possible attacks can be mdoen with the current energy levels
			if (attks.get(i).getCost()<=OPP.getEnergy()){//Checks if the cost is under the energy level
				cando.add(attks.get(i));//Then it is a valid move
			}
		}
		if (cando.size()>0){//If there are moves that the enemy can do
			if (OPP.isStunned()){//Checks if the pokemon is stunned and if it is, it cannot attack
				System.out.println("The enemy was stunned! Nothing happened.");
				OPP.unStun();
			}
			else{//The enemy can attack
				int a = (int)(Math.random() * cando.size());
				//We take a random move and the enemy will use it to attack the user
				int location= attks.indexOf(cando.get(a));//Find the location of the possible attack within the attacklist
				System.out.printf("%s used %s!\n", OPP.getName(), attks.get(location).getName());
				int damage= OPP.attack(location, trainerList.get(pos));
				System.out.printf("%s lost %d HP!\n", trainerList.get(pos).getName(), damage);
				cando.clear();//Reset the possible attacks
			}	
		}
		else{//If there are no possible moves to be done, the enemy will pass
			pass(OPP);
		}
	}
	public static void replenishEnergy(){//After each round, we need to add 10 energy to each pokemon (All of user's pokemon, and the opponent)
		for (int i=0; i<trainerList.size(); i++){//Replenishes 10 energy to all pokemon after each round
			trainerList.get(i).setEnergy(Math.min(trainerList.get(i).getEnergy()+10, 50));
		}
		//Add 10 energy to the enemy pokemon also
		pokeList.get(0).setEnergy(Math.min(pokeList.get(0).getEnergy()+10, 50));			
	}
	public static void battle(){
		//The battle method which I run my own pokemon, choose what action I do, and have the enemy pokemon attack also
		//We check after each attack whether the opponent pokemon is alive or not
		Random rnd = new Random();
		boolean userTurn =rnd.nextBoolean();//To see who goes first
		chooseMember();//Choose what pokemon we want to start the battle with
		generateEnemy();//Generates an random enemy to battle
		while (alive){//While both pokemon in the round are alive
			if (userTurn){//User's turn first
				chooseAction();//Chooses its move
				pokeDead();//Sees if any pokemon are dead yet
				if(!alive){//If anything is knocked out, we reset
					replenishEnergy();//Add 10 energy to pokemon
					break;
				}
				enemyAttack(pokeList.get(0));//If not, the enemy will choose an attack
				pokeDead();//Sees if any pokemon are dead yet
				replenishEnergy();//Add 10 energy to pokemon
			}
			if(!userTurn){//Enemy's turn first
				enemyAttack(pokeList.get(0));//Enemy will choose an attack
				pokeDead();//Sees if any pokemon are dead yet
				if(!alive){//If anything is knocked out, we reset
					replenishEnergy();//Add 10 energy to pokemon
					break;
				}
				chooseAction();//Chooses its move
				pokeDead();//Sees if any pokemon are dead yet
				replenishEnergy();//Add 10 energy to pokemon
			}	
		}
		for (int i=0; i<trainerList.size(); i++){//Heals the pokemon by 20 HP after each battle
			trainerList.get(i).setHP(Math.min(trainerList.get(i).getHP()+20, trainerList.get(i).getMaxHP()));
			trainerList.get(i).unDisable();//Undisable every pokemon after each battle	
		}
	}
	
	public static void main (String [] args) {//Where I load all the methods to comprise the game
		Scanner kb= new Scanner (System.in);
		//The logo for our game
		System.out.println(" _____      ____                ___/__                      ____                                               _____     _____                             ");
        System.out.println("|     |    |    |    |     |   |          ||         ||    |    |    ||          |             ||            |     |   |        ||          |            || ");
        System.out.println("|      |  |      |   |    |    |          | |       | |   |      |   | |         |            |  |           |     |   |        | |         |           |  |");
        System.out.println("|      |  |      |   |   |     |          |  |     |  |   |      |   |  |        |           |    |          |     |   |        |  |        |          |    |  ");
        System.out.println("|      |  |      |   |  |      |          |   |   |   |   |      |   |   |       |          |      |         |     |   |        |   |       |         |      |    ");
        System.out.println("|      |  |      |   | |       |          |    | |    |   |      |   |    |      |         |        |        |     |   |        |    |      |        |        |     ");
        System.out.println("|     |   |      |   ||        |______    |     |     |   |      |   |     |     |        |          |       |_____|   |_____   |     |     |       |          |");
        System.out.println("|____|    |      |   | |       |          |           |   |      |   |      |    |       |------------|      ||        |        |      |    |      |------------|");
        System.out.println("|         |      |   |  |      |          |           |   |      |   |       |   |      |              |     | |       |        |       |   |     |              | ");
        System.out.println("|         |      |   |   |     |          |           |   |      |   |        |  |     |                |    |  |      |        |        |  |    |                |");
        System.out.println("|         |      |   |    |    |          |           |   |      |   |         | |    |                  |   |   |     |        |         | |   |                  |");
        System.out.println("|          |____|    |     |   |______    |           |    |____|    |          ||   |                    |  |    |    |_____   |          ||  |                    |");
        delay(1000);
		System.out.println("The Pulse Presents Pokemon Arena!");
		System.out.println("In this game, you will enter the corresponding number to the option you want");
		delay(1000);
		System.out.println("Trainer, what is your name?");//The user's name
		try{
			username=kb.nextLine();
		}
		catch (InputMismatchException ex){
			username = "";
			kb.next();
		}
		System.out.printf("%s, get ready to be the Trainer Supreme!\n", username);
		loadPokemon();//Loads all of the pokemon that can be used
		delay(1000);
		choosePokemon();//User chooses their 4 pokemon
		delay(1000);
		System.out.printf("You have %d other enemy pokemon to beat before you are named Trainer Supreme!\n", pokeList.size());
		delay(1000);
		while(trainerList.size()>0 && pokeList.size()>0){//If the user and the pokelist both have pokemon available to battle
			battle();//Runs each battle between user and opponent
		}
		if (trainerList.size()==0){//When all of the good pokemon are dead, the user loses
			System.out.println(username+",You have no more pokemon left, failed Trainer Supreme.... Please exit the arena for our next contestant.");
		}
		if (pokeList.size()==0){//When all of the bad pokemon are dead, the user wins
			System.out.println(username+", you are the Trainer Supreme!");
			delay(500);
			//Win logo
			System.out.println("                                                  _________                                              ");
        	System.out.println("|                    ||                    |          |          ||          |      |          ");
        	System.out.println(" |                  |  |                  |           |          | |         |      |   ");
        	System.out.println("  |                |    |                |            |          |  |        |      |      ");
        	System.out.println("   |              |      |              |             |          |   |       |      |      ");
        	System.out.println("    |            |        |            |              |          |    |      |      |       ");
        	System.out.println("     |          |          |          |               |          |     |     |      |    ");
        	System.out.println("      |        |            |        |                |          |      |    |      |   ");
        	System.out.println("       |      |              |      |                 |          |       |   |      |       ");
        	System.out.println("        |    |                |    |                  |          |        |  |      |   ");
        	System.out.println("         |  |                  |  |                   |          |         | |     |||         ");
        	System.out.println("          ||                    ||                ____|____      |          ||     |||   ");	
		}
	}
}