public class TestKlasse {
	
	//Testklasse für die Erzeugung von Labyrinthen.
	
	public static boolean run = true;
	
	public static void main(String[] args) {
		labyrinth lab1 = new labyrinth();
		lab1.printLab();
		lab1.solve();
	}

}
