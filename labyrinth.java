import java.util.Random;

/*
 * Diese Klasse erzeugt zufällige Labyrinthe und löst sie anschließend mit einer recursiven Methode
 * Author: Jascha Kruschel
 * Datum: 07.06.2021
 * Version: 1.0
 */

public class labyrinth {

	private char[][] Layout; //in diesem 2-dimensionalen Array wird das Labyrinth gespeichert.
	int X; //Zähler für Anzahl 'X' in der Zeile
	int maxX; //Maximale Anzahl an 'X', die in der Zeile generiert werden können
	
	int cursorX; //Es wird ein 'cursor' verwendet, um zu tracken, wo im Labyrinth sich der Lösealgorithmus befindet.
	int cursorY;
	
	int direction; //Die präferierte Bewegungsrichtung beim Lösen des Labyrinths.
	
	int tries = 0; //Zähler, wie oft die Rekursive Funktion durchlaufen wurde
	int maxTries = 20; //Limit für wie viele Durchläufe erfolgen, bevor aufgegeben wird.
	
	
	public labyrinth() {
		this.Layout = new char[10][10]; //Labyrinthgröße nicht dynamisch. Könnte sie aber auch sein.
		this.X = 0;
		this.maxX = 0;
		this.cursorX = 4; //Cursor startet in der Mitte
		this.cursorY = 4; //Cursor startet in der Mitte
		buildLab();
	}
	
	//Durchläuft die Zeilen des zu erzeugenden Labyrinths und ruft für jede den Zeilenbuilder auf.
	public void buildLab() {
		for(int i = 0;i<10;i++) { //Für jede Zeile
			X = 0;
			maxX = rand(4,7); //Diese Spanne ändern um schwerere oder leichtere Labyrinthe zu generieren
			while (X<maxX) {
				buildLine(i);
			}

		}
		Layout[4][4] = '@'; //Startposition markieren
	}
	
	//Erzeugt die Zeile mit der übergebenen Nummer
	public void buildLine(int zeilennummer) {
		X = 0;
		for(int i=0;i<10;i++) { //Für jede Stelle in der Zeile
			if(X < maxX) { //Wenn noch nicht die für diese Zeile maximale Anzahl an X erreicht ist
				Layout[zeilennummer][i] = Math.random() < 0.5 ? 'X' : ' '; //10% Chance ein X zu schreiben, sonst eine Leerstelle
				X = (Layout[zeilennummer][i] == 'X') ? X=X+1 : X ; //Wenn wir ein 'X' geschrieben haben den Zähler erhöhen, sonst nicht
			}
			else if(X == maxX) { // Wenn die maximalzahl von 'X' vor Ende der Zeile erreicht ist, wird aufgefüllt.
				Layout[zeilennummer][i] = ' ';
			}
		}
		

	}
	
	//Gibt das erzeugte Labyrinth als String in der Konsole aus
	public void printLab() {
        var sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(new String(Layout[i]));
            sb.append(System.lineSeparator());
        }
        System.out.println(sb.toString());
	}
	
	//Löst das Labyrinth und hinterlässt '*' auf dem Lösungspfad
	public void solve() {
		try {
			Thread.sleep(200);
		}
		catch(Exception e) {
			System.out.println("Fehler beim Warten");
		}
		char left = Layout[cursorY][cursorX - 1]; //Was ist links von mir?
		char right = Layout[cursorY][cursorX + 1]; //Was ist rechts von mir?
		char up = Layout[cursorY - 1][cursorX]; //Was ist über mir?
		char down = Layout[cursorY + 1][cursorX]; //Was ist unter mir?
		switch(direction) { //Die verschiedenen Cases generieren unterschiedliche Prioritäten bei der Wegfindung. So werden bei folgenden Iterationen unterschiedliche Pfade genommen.
		case 0: //Wir versuchen nach oben zu lösen
				if(up == ' ') {
					moveUp();
				}
				else if(left==' ' && right==' ') { //Falls der Weg nach rechts und links frei ist soll variiert werden.
					if(Math.random()>0.5) { moveLeft();}
					else { moveRight();}
				}
				else if(left==' ') {
					moveLeft();
				}
				else if(right==' '){
					moveRight();
				}
				else if(down==' ') {
					moveDown();
				}
				else {
					System.out.println("stuck");
					rerollDirection(); //Wenn wir hängen bleiben, lösen wir in eine andere Richtung
				}
				printLab();
				break;
		case 1: //Wir versuchen nach links zu lösen
			if(left==' ') {
				moveLeft();
			}
			else if(up==' ' && down==' ') {
				if(Math.random()>0.5) {moveUp();}
				else {moveDown();}
			}
			else if(up==' ') {
				moveUp();
			}
			else if(down==' '){
				moveDown();
			}
			else if(right==' ') {
				moveRight();
			}
			else {
				System.out.println("stuck");
				rerollDirection(); //Wenn wir hängen bleiben, lösen wir in eine andere Richtung
			}
			printLab();
			break;
		case 2: //Wir versuchen nach rechts zu lösen
			if(right==' ') {
				moveRight();
			}
			else if(up==' ' && down==' ') {
				if(Math.random()>0.5) {moveUp();}
				else {moveDown();}
			}
			else if(up==' ') {
				moveUp();
			}
			else if(down==' '){
				moveDown();
			}
			else if(left==' ') {
				moveLeft();
			}
			else {
				System.out.println("stuck");
				rerollDirection(); //Wenn wir hängen bleiben, lösen wir in eine andere Richtung
			}
			printLab();
			break;
		case 3: //Wir versuchen nach unten zu lösen
			if(down == ' ') {
				moveDown();
			}
			else if(left==' ' && right==' ') {
				if(Math.random()>0.5) { moveLeft();}
				else { moveRight();}
			}
			else if(left==' ') {
				moveLeft();
			}
			else if(right==' '){
				moveRight();
			}
			else if(up==' ') {
				moveUp();
			}
			else {
				System.out.println("stuck");
				rerollDirection(); //Wenn wir hängen bleiben, lösen wir in eine andere Richtung
			}
			printLab();
			break;
			
		}
		
		if(!checkFinished()) { //Es wird geprüft, ob wir das Labyrinth erfolgreich gelöst haben.
			if(tries<maxTries) solve();
			else System.out.println("Labyrinth nicht lösbar.");
		}
	}
	
	public void rerollDirection() {
		for(int i = 0; i<10;i++) { //Durch alles loopen und die gesetzten Sterne entfernen
			for(int j = 0; j<10;j++) {
				if(Layout[i][j]=='*') {
					Layout[i][j]=' ';
				}
			}
		}
		cursorX = 4; //Cursor zurücksetzen
		cursorY = 4; //Cursor zurücksetzen
		direction = (int) Math.round(Math.random()*3.0); //Neue Richtung auswürfeln
		tries++; //Counter erhöhen um zu wissen wann wir aufgeben.
		if(tries<maxTries) solve(); //Check, ob wir jetzt aufgeben.
		else System.out.println("Labyrinth nicht lösbar."); //Nachricht wird nach Aufgabe angezeigt.
	}
	
	//Diese Methoden bewegen jeweils den Cursor und platzieren die Sterne.
	public void moveLeft() {
		cursorX = cursorX -1;
		Layout[cursorY][cursorX] = '*';
		}
	
	public void moveRight() {
		cursorX = cursorX +1;
		Layout[cursorY][cursorX] = '*';
		}
	
	public void moveUp() {
		cursorY = cursorY -1;
		Layout[cursorY][cursorX] = '*';
		}
	
	public void moveDown() {
		cursorY = cursorY +1;
		Layout[cursorY][cursorX] = '*';
		}
	
	//Überprüft, ob wir am Rand des Labyrinths angelangt sind und gibt dann "true" zurück. sonst "false"
	public boolean checkFinished() {
		if(cursorX*cursorY==0) return true; //Falls einer der beiden 0 ist, ist die Bedingung wahr
		else if (cursorX==9 || cursorY==9) return true; //Falls einer der beiden 9 ist, ist die Bedingung wahr
		else 
			return false;
	}

	//Erzeugt zufällige Nummern innerhalb des angegebenen Bereichs.Inklusive low, exklusive high.
	public int rand(int low, int high) {
		Random r = new Random();
		return r.nextInt(high-low) + low;
	}
	
}
