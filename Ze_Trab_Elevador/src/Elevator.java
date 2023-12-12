
public class Elevator {
	
	boolean isRunning;
	
	boolean isOccupied;
	int numOfFloors;
	
	int targetFloor;
	int currentFloor;
	
	boolean isDoorOpen;
	boolean isBeingCalled;
	boolean alreadyGotHere;
	
	Building building;
	
	public Elevator(int _numOfFloors, Building _building) {
		numOfFloors = _numOfFloors;
		building = _building;
		
		currentFloor = 1;
		
		isRunning = true;
		targetFloor = numOfFloors;
	}
	
	public void Update(int _targetFloor) {
		
		if(targetFloor != _targetFloor) {
			targetFloor = _targetFloor;
		}
		
		if(building.isSomebodyInThisFloor(currentFloor) && !isDoorOpen && !isOccupied) {
			AbrirPorta();
		}
		
		if(!isDoorOpen) {
			MoveTowardsTarget();
		}
	}
	
	public void FecharPorta(){
		building.Print("O elevador fechou a porta no andar "+ currentFloor);
		this.isDoorOpen = false;
	}
	
	public void AbrirPorta(){
		building.Print("O elevador abriu a porta no andar "+ currentFloor);
		isBeingCalled = false;
		this.isDoorOpen = true;
	}
	
	public void SomeoneCalled() {
		//building.Print("Elevador está sendo chamado ");
		isBeingCalled = true;
	}

	public void SomeoneEntered() {
		this.isOccupied = true;
		FecharPorta();
	}
	
	public void SomeoneExited() {
		this.isOccupied = false;
		
		if(!building.isSomebodyInThisFloor(currentFloor))
			FecharPorta();
	}
	
	private void MoveTowardsTarget() {
		
		if(currentFloor < targetFloor) {
			currentFloor += 1;
			building.Print("Elevador passando para o andar " + currentFloor);
		}
		else if(currentFloor > targetFloor) {
			currentFloor -= 1;
			building.Print("Elevador passando para o andar " + currentFloor);
		}
		else{
			GotInTargetFloor();
		}
	}

	

	private void GotInTargetFloor() {
		building.Print("Elevador chegou no andar " + currentFloor);
		
		AbrirPorta();
	}
	

	public void SetDestination(int _targetFloor) {
		if(targetFloor == _targetFloor)
			return;
		
		this.targetFloor = _targetFloor;
	}
}
