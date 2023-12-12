import java.lang.annotation.Target;
import java.util.concurrent.Semaphore;

public class Building{
	
	Elevator elevator;
	
	int numOfFloors;
	int numOfPeople;
	
	int[] peoplewaitingIfFloor;
	
	Semaphore enterSemaphore = new Semaphore(1);
	
	Semaphore callSemaphore = new Semaphore(1);
	
	public Building(int _numOfFloors, int _numOfPeople) {
		numOfFloors = _numOfFloors;
		numOfPeople = _numOfPeople;
		
		peoplewaitingIfFloor = new int[numOfPeople];
		
		elevator = new Elevator(numOfFloors, this);
	}
	
	public void Print(String text) {
		System.out.println(text);
	}
	
	
	public void SetPersonWaitingInFloor(int personId, int waitingInFloor) {
		peoplewaitingIfFloor[personId] = waitingInFloor;
	}
	
	public void UpdadeElevator(int targetFloor) {
		elevator.Update(targetFloor);
	}
	
	public void EnterInElevator(Person person, int personFloor) {
		
		// o semaphoro garante que mesmo se duas threads atravessem o primeiro if apenas uma consiga o segundo
		try {
			enterSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(elevator.currentFloor == personFloor && elevator.isDoorOpen && !elevator.isOccupied) {
			
			
			person.EnterElevator();
			
			elevator.SomeoneEntered();
			
			
			//Caso alquem possa entrar so solta o semaforo depois de entrar
		}
		
		

		enterSemaphore.release();
	}
	
	public void CallElevator(Person person) {
		try {
			callSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!elevator.isDoorOpen || !elevator.isOccupied || !elevator.isBeingCalled) {
			
			person.calledTheElevator = true;
			
			elevator.SomeoneCalled();
			
		}
		
		callSemaphore.release();
		
	}

	public boolean isSomebodyInThisFloor(int elevatorCurrentFloor) {
		
		
		for (int p : peoplewaitingIfFloor) {			
			if(p == elevatorCurrentFloor) {
				return true;
			}
		}
		
		return false;
	}

	public boolean isDoorOpen() {
		return elevator.isDoorOpen;
	}

	public int ExitElevator() {
		elevator.SomeoneExited();
		return elevator.currentFloor;
	}
}
