
public class Main {

	public static void main(String[] args) {
		
		int numOfFloors = 6;
		int numOfElevators = 1;
		int numOfPeople = 6;
		
		
		Predio predio = new Predio(numOfFloors, numOfElevators, numOfPeople);
		
		
		
		Passageiro[] people = new Passageiro[numOfPeople];
		

		for(int i = 0; i < numOfPeople; i++)
		{
			people[i] = new Passageiro(predio, i);
			people[i].start();
		}
	}
}
