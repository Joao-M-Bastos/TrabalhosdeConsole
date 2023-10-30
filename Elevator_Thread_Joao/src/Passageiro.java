
public class Passageiro extends Thread{
	Predio predio;
	
	int currentFloor;
	
	int targetFloor;
	
	int id;
	int elevatorId;
	
	boolean gotInTarget;
	
	String currentState;
	String outOfElevator = "Fora", inElevator = "Dentro";
	
	public int GetID() {return id;}
	
	public Passageiro(Predio _predio, int _id) {
		predio = _predio;
		id = _id;
		gotInTarget = false;
		currentFloor = 2 + predio.GetRandomInt(predio.GetNumOffloors() - 1);;
		
		do {
			targetFloor = 1 + predio.GetRandomInt(predio.GetNumOffloors());
		}while(targetFloor == currentFloor);
		
		predio.Printar("_____ Chegou a pessoa " + id + "  no andar " + currentFloor + " e seu destino é " +targetFloor+" ______");
		
		predio.waintingInFloor[id] = this.currentFloor;
		
		currentState = outOfElevator;
	}
	
	public void run() {
		while (!gotInTarget) {
			
			switch (currentState) {
				case "Fora":
					
					if(currentFloor == targetFloor) {
						
						predio.Printar("---------------- A pessoa " + id + " chegou ao andar " + currentFloor +" --------------");
						predio.PeoplegotInHisTarget();
						predio.waintingInFloor[id] = 0;
						gotInTarget = true;
						break;
					}
					
					int freeElevatorInFloor = predio.IsFreeElevatorInFloor(currentFloor,targetFloor, id);
					if(freeElevatorInFloor >= 0) {
						this.EnterElevator(freeElevatorInFloor);
					}
					
					
					break;
				case "Dentro":
					int elevatorOpenedInFloor = predio.DidElevatorOpen(elevatorId);
					
					if(elevatorOpenedInFloor > 0){
						currentFloor = elevatorOpenedInFloor;
						predio.waintingInFloor[id] = currentFloor;
						predio.LeaveElevator(elevatorId);
						ExitElevator();
					}
					break;
			};
			
			
		}
	}
	
	private void EnterElevator(int _elevatorId) {
		elevatorId = _elevatorId;
		currentState = inElevator;
	}
	
	private void ExitElevator() {
		
		elevatorId = -1;
		currentState = outOfElevator;
	}
	
	public void LeaveElevatorInFloor() {
		
	}
}
