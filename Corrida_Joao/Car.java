import java.sql.Time;
import java.util.Random;
import java.util.Timer;

public class Car extends Thread{
	
	Race race;
	
	int id;
	
	float distanciaPercorrida = 0;
	
	boolean running = true;
	
	Random rand = new Random();
	
	public Car(Race race, int id) {
		this.race = race;
		this.id = id + 1;
	}
	
	public void run() {		
		while(running) {
			
			distanciaPercorrida += GenerateRandomSpeed();
			
			if(distanciaPercorrida >= race.raceDistance){
				
				race.StoreWinners(id);
				
				running = false;
			}else {
				Print("Carro " +id+" ja andou " + distanciaPercorrida +" km. ");
			}
		}
	}
	
	public int GenerateRandomSpeed() {
		return rand.nextInt(1, 4);
	}
	
	
	private void Print(String textToPrint) {
		System.out.println(textToPrint);
	}
}
