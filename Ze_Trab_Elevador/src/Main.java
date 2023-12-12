
public class Main {

	public static void main(String[] args) {
		
		
		int numOfThreadsPerson = 5;
		int numOfFloors = 10;
		
		Building building = new Building(numOfFloors, numOfThreadsPerson);
		
		Person[] people = new Person[numOfThreadsPerson];
		
		for(int i = 0; i<numOfThreadsPerson; i++) {
			people[i] = new Person(i, numOfFloors, building);
			people[i].start();
		}
		
	}

}