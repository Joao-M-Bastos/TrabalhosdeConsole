import java.util.concurrent.Semaphore;

public class Elevador extends Thread{
	
	Predio predio;
	
	int id;
	
	int currentFloor = 1;
	
	int targetFloor;
	
	int numOfFloors;
	boolean upping;
	
	boolean isDoorOpened;
	
	String currentState;
	String openState = "Aberto",closeState = "Fechado", travellingState = "Andando", waitingState = "Esperando";
	
	private boolean personOnElevator;
	
	public int GetID() {return id;}
	
	public boolean IsBusy() { return personOnElevator;}
	
	public Elevador(Predio _predio, int _id, int _numOfFloors) {
		id = _id;
		predio = _predio;
		numOfFloors = _numOfFloors;
		
		FecharPorta(currentFloor + 1);
	}
	
	
	public void AbrirPorta() {
		predio.Printar("O elevador " + id + " abriu a porta no andar " +currentFloor);
		
		isDoorOpened = true;
		
		currentState = openState;
	}
	
	
	public void FecharPorta(int newTargetFloor) {
		targetFloor = newTargetFloor;
		
		isDoorOpened = false;
		
		predio.Printar("O elevador " + id + " fechou a porta no andar " +currentFloor);
		currentState = closeState;
	}
	
	
	public void ChangeStateToWaiting() {
		
		if(targetFloor == 1)
			upping = true;
		else if(targetFloor == numOfFloors)
			upping = false;
		
		currentState = waitingState;
	}
	

	public void ChangeStateToTravel(int newTargetFloor) {
		targetFloor = newTargetFloor;
		currentState = travellingState;
	}
	
	
	
	public void run() {
		while(predio.IsSomeoneStillOutOfTargetFloor()) { //|| IsBusy()){
			
			String currentElevatorState = predio.getElevatorState(id);
			
			switch (currentElevatorState) {
				case "Aberto": 

					
					break;
				case "Fechado": 
					
					ChangeStateToTravel(targetFloor);
					
					break;
				case "Esperando":
					
					if(predio.CanGoNextFloor(currentFloor)){
						if(isDoorOpened) {
							if(upping)
								FecharPorta(currentFloor +1);
							else
								FecharPorta(currentFloor - 1);
						}else {
							if(upping)
								ChangeStateToTravel(currentFloor +1);
							else
								ChangeStateToTravel(currentFloor - 1);
						}
						
					}
					
					break;
				case "Andando": 
					predio.Printar("{{O elevador " + id + " : está no andar : " +currentFloor + " : e quer chegar no : " +targetFloor +"}}");
					if(targetFloor > currentFloor)
						VisitarAndar(currentFloor + 1);
					else if(targetFloor < currentFloor)
						VisitarAndar(currentFloor - 1);
					else
						GotInFloor();
					break;
			};
			
			//predio.Printar(id + " " + currentState);
		}
	}
	
	private void VisitarAndar(int value) {
		currentFloor = value;
	}
	
	private void GotInFloor() {
		predio.Printar("O elevador " + id + " chegou no andar " + currentFloor);
		
		if(IsBusy()) {
			AbrirPorta();
			//predio.Printar("---------------- O elevador " + id + " deixou a pessoa "+ personOnElevator +" no andar " + currentFloor +" --------------");
		
		}
		else
			ChangeStateToWaiting();
	}
	
	public void SetPeopleInElevator(boolean value) {
		personOnElevator = value;
	}

}
