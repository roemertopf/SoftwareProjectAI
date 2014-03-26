package old_versions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;

public class RoboClient_v0 {
	
	public static void main(String[] args) throws Exception {
		
		final int AnzahlSpieler = 11, AnzahlGegner = 11;
		String Team1 = "Flo", Team2 = "Bully";
		
		//Team 1
		Robo Spieler[], Gegner[];
		Spieler = new Robo[AnzahlSpieler];
		Gegner = new Robo[AnzahlGegner];
		
		for(int i=0; i<AnzahlSpieler; i++){
			Spieler[i]= new Robo("127.0.0.1", 6000, Team1);
			Spieler[i].init(Team1, 15);
		}
		for(int i=0;i<AnzahlGegner;i++){
			Gegner[i]= new Robo("127.0.0.1",6000, Team2);
			Gegner[i].init(Team2, 15);
		}
		
		Thread.sleep(500);
		
		for( int i=0; i < AnzahlSpieler; i++)
			Spieler[i].turn(180);
		
		
//		Robo robo1 = new Robo("192.168.2.101", 6000);
//		Robo robo1 = new Robo("127.0.0.1", 6000);
//		robo1.init("Flo1", 15);
		
		
		boolean running = true;
		String input = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(running)
		{
			try {
	            System.out.print("> ");
	            input = reader.readLine();
	            if(input.equals("exit"))
	            	running = false;
	            else
	            	for( int i=0; i < 11; i++){
	            		Spieler[i].send(input);
	            		Gegner[i].send(input);
	            	}
//	            	robo1.send(input);
	            input = "";
	        }
	        catch(IOException e) {
	            e.printStackTrace();
	        }			
		}
		System.out.println("Tschö!");
		
		for( int i=0; i < 11; i++){
			Spieler[i].bye();
			Gegner[i].bye();
		}
//		robo1.bye();
		
	}
}
