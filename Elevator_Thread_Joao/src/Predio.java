import java.util.Random;
import java.util.concurrent.Semaphore;

public class Predio {
	
	Random r = new Random();
	
	int numOfFloors;
	int numOfElevators;
	int numOfPeople;
	int numOfPeopleThatGotInTargetFloor;
	
	Elevador[] elevators;
	
	int[] waintingInFloor;
	
	Semaphore enterElevatorSemaphore = new Semaphore(1);
	
	Semaphore growCompleteNumberSemaphore = new Semaphore(1);

	Semaphore getElevatorStateSemaphore = new Semaphore(1);
	
	Semaphore leaveElevatorSemaphore = new Semaphore(1);
	
	public Predio(int _numOfFloors, int _numOfElevator, int _numofPeople) {
		numOfFloors = _numOfFloors;
		numOfElevators = _numOfElevator;
		numOfPeople = _numofPeople;
		
		waintingInFloor = new int[numOfPeople];
		
		elevators = new Elevador[numOfElevators];
		
		for(int i = 0; i < numOfElevators; i++)
		{
			elevators[i] = new Elevador(this, i, numOfFloors);
			elevators[i].start();
		}
	}
	
	public void Printar(String texto) {
		System.out.println(texto);
	}
	
	
	
	public int GetRandomInt(int maxValue) {
		return r.nextInt(maxValue);
	}
	
	public int GetNumOffloors() {
		return numOfFloors;
	}
	
	public boolean IsSomeoneStillOutOfTargetFloor() {
		
		int numOfBusyElevators = 0;
			
		/*for (Elevador elevador : elevators) {
			if(elevador == null)
				return true;
			if(elevador.IsBusy())
				numOfBusyElevators++;
		}*/
		
		//Printar(numOfPeopleThatGotInTargetFloor +" : "+  numOfPeople);

		if(numOfPeopleThatGotInTargetFloor  >= numOfPeople) {
			//Printar(false+"");
			return false;
		}
		//Printar(true+"");
		return true;
	}



	public int IsFreeElevatorInFloor(int currentFloor,int targetFloor, int peopleId) {
		
		try {
			enterElevatorSemaphore.acquire();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		int freeElevatorID = -1;
		
		for (Elevador e : elevators) {
			//Printar(e.currentFloor + " : " + currentFloor);
			//Printar(e.currentState);
			//Printar(e.IsBusy() + "");
			
			
			if(e.currentFloor == currentFloor && getElevatorState(e.id) == "Esperando" && !e.IsBusy() && freeElevatorID < 0) {
				
				if(!e.isDoorOpened)
					e.AbrirPorta();
				
				
				Printar("A pessoa " + peopleId + " entrou no elevador " + e.GetID());
				
				e.SetPeopleInElevator(true);
				e.FecharPorta(targetFloor);
				

				freeElevatorID = e.GetID();
			}
		}
		
		enterElevatorSemaphore.release();
		
		return freeElevatorID;
	}
	
	public void LeaveElevator(int id) {
		
		elevators[id].SetPeopleInElevator(false);
		elevators[id].ChangeStateToWaiting();
	}

	public int DidElevatorOpen(int elevatorId) {
		if(getElevatorState(elevatorId) == "Aberto")
			return elevators[elevatorId].currentFloor;
		return 0;
	}

	public void PeoplegotInHisTarget() {
		try {
			growCompleteNumberSemaphore.acquire();
		} catch (Exception e) {
			// TODO: handle exception
		}
				
		numOfPeopleThatGotInTargetFloor += 1;
		
		growCompleteNumberSemaphore.release();
	}
	
	public String getElevatorState(int elevatorId) {
		try {
			getElevatorStateSemaphore.acquire();
		} catch (Exception e) {
			// TODO: handle exception
		}
				
		String state = elevators[elevatorId].currentState;
		
		getElevatorStateSemaphore.release();
		
		return state;
	}

	public boolean CanGoNextFloor(int elevatorWaitingInFloor) {
		boolean canChange = true;
		
		for (int floor : waintingInFloor) {
			if(elevatorWaitingInFloor == floor)
				canChange = false;
		}
		
		return canChange;
		
	}
}
