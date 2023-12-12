import java.util.Random;

public class Person extends Thread{
	
	Random rand = new Random();
	
	Building building;
	
	int id, numOfFloors;
	
	boolean isWaiting;
	boolean isInElevator;
	boolean calledTheElevator;
	
	int currentFloor;
	int targetFloor;
	
	public Person(int _id, int _numOfFloors , Building _building) {
		
		id = _id;
		numOfFloors = _numOfFloors;
		
		isWaiting = true;
		
		building = _building;
		
		currentFloor = rand.nextInt(numOfFloors) + 1;
		
		do {
			targetFloor = rand.nextInt(numOfFloors) + 1;
		}while(currentFloor == targetFloor);
		
		building.SetPersonWaitingInFloor(id, currentFloor);
	
		
		building.Print("_______Pessoa " + id + " esta no andar " + currentFloor + " e busca chegar no " + targetFloor+"__________");
	}
	
	public void run() {
		while(isWaiting){

			building.EnterInElevator(this, currentFloor);
			
			if(building.elevator.isOccupied)
				calledTheElevator = false;
			
				
			if(calledTheElevator){
				building.UpdadeElevator(currentFloor);
			}
			
			building.CallElevator(this);
		}
		
		while(isInElevator){
			if(building.isDoorOpen()) {
				
				building.Print("Pessoa " + id + " desceu do elevador no andar " + targetFloor +" que era seu objetivo");
				
				currentFloor = building.ExitElevator();
				
				if(currentFloor == targetFloor)
					ExitElevator();
			}
			else {
				building.UpdadeElevator(targetFloor);
			}
		}
		
	}

	private void ExitElevator() {
		this.isInElevator = false;
	}

	public void EnterElevator() {
		
		//Mostra ao elevador que a pessoa não está mais esperando
		building.SetPersonWaitingInFloor(id, 0);
		
		building.Print("Pessoa " + id + " entrou no elevador no andar " + currentFloor + " e quer ir ao "+ targetFloor );
		
		this.isWaiting = false;
		calledTheElevator = false;
		this.isInElevator = true;
	}
}
