package old_versions;

import udp.UDPServer;
import java.lang.Math;

public class Robo_v1 extends UDPServer{
	public int NumberPlayer;				// Anzhl der Spieler
	public String myName;					// Name des Teams
	public String Side;
	public double Stamina;					// Wert der Ausdauer
	public boolean play;					// wird gespielt oder nicht
	public double x;						// X-Position des Spielers
	public double y;						// Y-Position des Spielers
	public double worldAngle=0;				//
	public int goals_me=0;
	public int goals_them=0;

	public String closestFlagName;
	public double closestFlagX;
	public double closestFlagY;
	public double closestFlagDist;
	public double closestFlagAngle;

	public String lineName;
	public double lineDist;
	public double lineAngle;

	public double ballDist;
	public double ballAngle;
	public double ballX;
	public double ballY;
	public double ballSpeed;
	public boolean seeBall;
	public boolean getBall;

	public double myPlayerValues[][];
	public String myPlayerName[];

	public String otherPlayerName[];
	public double otherPlayerValues[][];

	//Werden nicht immer wieder aktualisiert, nur wenn seeGoal == true
	public String goalName;
	public boolean seeGoal;
	public double goalAngle;
	public double goalDist;

	public boolean seeCornerGoalLeft;
	public String cornerGoalLeft;
	public double cornerGoalLeftAngle;
	public double cornerGoalLeftDist;
	public boolean seeCornerGoalRight;
	public String cornerGoalRight;
	public double cornerGoalRightDist;
	public double cornerGoalRightAngle;

	public String beforeGoalName="(f p l t)";
	public boolean seeBeforeGoal;
	public double beforeGoalAngle;
	public double beforeGoalDist;

	public String [] NameFlags;
	public double  [][] ValueFlags;

	public String [] playerTypes={"striker","back","back","back","back","back","back","back","back","back","back"};
	public int playerPosition [][]={{-10,-20},{-10,+20},{-30,-20},{-30,+20},{-50,0},{-50,-20},{-50,+20},{-25,-30},{-25,+30},{-30,-30},{-30,+30}};

	//Name der Flagen auf dem Spielfeld
	private String AllFlagNames[]={
			"(f t 0)","(f t r 10)","(f t r 20)","(f t r 30)","(f t r 40)","(f t r 50)",
			"(f r t 30)","(f r t 20)","(f r t 10)","(f r 0)", "(f r b 10)","(f r b 20)","(f r b 30)",
			"(f b r 50)","(f b r 40)","(f b r 30)","(f b r 20)","(f b r 10)","(f b 0)",
			"(f t l 10)","(f t l 20)","(f t l 30)","(f t l 40)","(f t l 50)",
			"(f l t 30)","(f l t 20)","(f l t 10)",
			"(f l 0)",
			"(f l b 10)","(f l b 20)","(f l b 30)",
			"(f b l 50)","(f b l 40)","(f b l 30)","(f b l 20)","(f b l 10)",
			"(f c t)", "(f r t)", "(f g r t)","(g r)","(f g r b)","(f r b)", "(f c b)","(f l b)","(f g l b)","(g l)","(f g l t)","(f l t)",
			"(f c)","(f p r t)","(f p r c)","(f p r b)","(f p l t)","(f p l c)","(f p l b)"
	};
	//Zugehörige Koordinaten der Flaggen
	private double AllFlagPositions[][]={
			{0,-39}, {10,-39}, {20,-39}, {30,-39}, {40,-39}, {50,-39},
			{57.5,-30},	{57.5,-20},	{57.5,-10},	{57.5,0},{57.5,10},	{57.5,20},	{57.5,30},
			{50,39},{40,39},{30,39},{20,39},{10,39},{0,39},
			{-10,-39},{-20,-39},{-30,-39},{-40,-39},{-50,-39},
			{-30,-57.5},{-20,-57.5},{-10,-57.5},{0,-57.5},{10,-57.5},{20,-57.5},{30,-57.5},
			{-50,39},{-40,39},{-30,39},{-20,39},{-10,39},
			{0,-34},{52.5,-34},{52.5,-7},{52.5,0},{52.5,7},{52.5,34},{0,34},{-52.5,34},{-52.5,7},{-52.5,0},{-52.5,-7},{-52.5,-34},{0,0},
			{36,-20},{36,0},{36,20},{-36,-20},{36,0},{36,20}	
	};


	public boolean noGegnerInWayLeft (double tolDist, double tolAngle){
		for( int i=0; i < otherPlayerName.length; i++)
			if( otherPlayerValues[i][1] > -tolAngle && otherPlayerValues[i][1] < 0 && otherPlayerValues[i][0] < tolDist)
				return false;
		
		return true;
	}
	
	public boolean noGegnerInWayRight (double tolDist, double tolAngle){
		for(int i=0; i<otherPlayerName.length;i++){
			if(otherPlayerValues[i][1]<tolAngle&&otherPlayerValues[i][1]>0){
				if(otherPlayerValues[i][0]<tolDist){
					return false;
				}
			}
		}
		return true;

	}


	public double myRandom(double low, double high) {
		return Math.random() * (high - low) + low;
	}

	public Robo_v1(String ip, int port, String MyName) throws Exception
	{	super(ip,port);
	this.myName=MyName;
	}

	public void init(String name, int version) throws Exception
	{	send("(init "+name+" (version "+version+"))");
	}

	public void bye() throws Exception
	{	send("(bye)");
	close();
	}

	public void calculate_player_position(double lineAngle, double lineDistance){
		double angle=0;
		/*
		 * Berechnen der Player-Position und aktualisieren der Koordinaten x und y.
		 * Da die Variablen lineAngle und lineDistance verändert werden, hier als Übergabeparameter. 
		 */

		//Überprüfen, auf welche Line geguckt wird
		if(lineName.equals("(l t)")){
			if(lineAngle<0){
				lineAngle+=180;
			}
		}
		else if(lineName.equals("(l r)")){
			if (lineAngle>0){
				lineAngle-=90;
			}
			else {
				lineAngle+=90;
			}
		}
		else if(lineName.equals("(l b)")){
			if(lineAngle>0){
				lineAngle-=180;
			}
		}
		else if(lineName.equals("(l l)")){
			if(lineAngle>0){
				lineAngle+=90;
			}else{
				lineAngle-=90;
			}
		}

		//Um die Koordinaten von beliebigen Objekten, welche gesehen werden zu berechnen
		//wird die globale Variable worldAngle benutzt.
		worldAngle=lineAngle;

		//Winkel zwischen Line und closest Flag berechnen
		angle=lineAngle-closestFlagAngle;
		if(angle>180){
			angle-=360;
		}
		if(angle<-180){
			angle+=360;
		}

		//Koordinaten berechnen, an denen der Player steht
		x=  (closestFlagX-Math.cos(Math.toRadians(angle))*closestFlagDist);
		y=  (closestFlagY+Math.sin(Math.toRadians(angle))*closestFlagDist);	
	}

	public double [] calculate_object_position(double objectDistance, double objectAngle){
		/*
		 * Berechnen der Position von beliebigen Objekten, die vom Spieler gesehen werden.
		 * Da die globale Variable worldAngle benutzt wird, muss zuerst die Funktion
		 * calculate_player_position aufgerufen werden.
		 * Die Koordinaten werden als Array zurückgegeben.
		 */
		double objectPosition[]={0,0};
		double angle=worldAngle-objectAngle;
		if (angle>180){
			angle-=360;
		}
		if(angle<-180){
			angle+=360;
		}
		objectPosition[0]=(x+Math.cos(Math.toRadians(angle))*objectDistance);
		objectPosition[1]=(y-Math.sin(Math.toRadians(angle))*objectDistance);
		return objectPosition;
	}

	public void parse_see(String msg){
		/*
		 * Mit dieser Funktion kann der See-String geparst werden. Die Funktion 
		 * ermittelt aus den Objekten automatisch die nächste Flagge (closestFlag), 
		 * die angegebene Line und den Ball. 
		 * Außerdem setzt sie die Variablen entsprechend.
		 */

		//Ausschneiden der Flags aus dem See-String
		//System.out.println(msg);
		if(msg.contains("((")&&msg.endsWith("))")){		//Bei vielen Spielern kann es vorkommen, dass der String abgeschnitten wird.

			
			String See=cutOut(msg,"((","))")+"))";

			//Splitten des See-Strings so, dass jedes Objekt einen einzelnen String darstellt
			String Flags[]=	See.split("\\(\\(");

			//Speichern der Objektnamen im String-Array
			NameFlags=new String [Flags.length];

			//Speichern der Parameter der Objekte. Die Objekte haben maximal 6 Parameter. (Spieler)

			ValueFlags= new double [NameFlags.length][6];

			for (int i=0; i<Flags.length;i++){

				NameFlags[i]="("+cutOut(Flags[i],"",")")+")";

				String Value=cutOut(Flags[i],") ",")");
				String ValueArray []=Value.split(" ");

				for(int j=0;j<ValueArray.length;j++){
					if(!ValueArray[j].contains("k")){		//Kontrollieren, ob eine Zahl enthalten ist.
						ValueFlags[i][j]=Double.parseDouble(ValueArray[j]);
					}
				}
			}



			//Bestimmen von closestFlag
			double length=200; 
			int flagge=0;
			boolean flag_exist=false;
			for(int i=0; i<NameFlags.length;i++){
				//Es sollen nur die Flags, nicht aber die Lines berücksichtigt werden.
				if(NameFlags[i].contains("(f")){			//Hier darf nicht F berücksichtigt werden, da man nicht weiß welche das ist. Diese ist nicht im Blickfeld
					flag_exist = true;
					if(length>ValueFlags[i][0]){
						flagge=i;
						length=ValueFlags[i][0];
					}
				}
			}


			if(flag_exist){
				closestFlagName=NameFlags[flagge];
				closestFlagDist=ValueFlags[flagge][0];
				closestFlagAngle=ValueFlags[flagge][1];

				//Herraussuchen der Koordinaten von closestFlag aus dem Array
				//Suchen der Flag mit dem gleichen Namen

				for(int i=0; i<AllFlagNames.length;i++){
					if(AllFlagNames[i].equals(closestFlagName)){
						closestFlagX=AllFlagPositions[i][0];
						closestFlagY=AllFlagPositions[i][1];
					}
				}
			}

			int AnzahlGegner=0;
			//Setzen der Line-Variablen
			for(int i=0; i<NameFlags.length;i++){
				//Zählen der Spieler
				if((NameFlags[i].contains("(p")||NameFlags[i].contains("(P"))&&(!NameFlags[i].contains("(p)")||!NameFlags[i].contains("(p)"))){
					if(!NameFlags[i].contains(myName)){
						AnzahlGegner++;
					}
				}
				//Finden des Line-Eintrags
				if(NameFlags[i].contains("(L")||NameFlags[i].contains("(l")){
					lineName=NameFlags[i];
					lineDist=ValueFlags[i][0];
					lineAngle=ValueFlags[i][1];
					calculate_player_position(lineAngle, lineDist);
				}
			}


			//Filtern der anderen Spieler aus dem String
			otherPlayerName= new String[AnzahlGegner];
			otherPlayerValues= new double[AnzahlGegner][6];
			int array_index=0;
			for(int i=0;i<NameFlags.length;i++){
				if((NameFlags[i].contains("(p")||NameFlags[i].contains("(P"))&&(!NameFlags[i].contains("(p)")||!NameFlags[i].contains("(p)"))){
					if(!NameFlags[i].contains(myName)){
						//Gegner

						otherPlayerName[array_index]=NameFlags[i];
						for(int j=0; j<otherPlayerValues[array_index].length;j++){
							otherPlayerValues[array_index][j]=ValueFlags[i][j];
						}
						array_index++;


					}
				}
			}


			//Jetzt nach dem Ball suchen. Wenn dieser im NameFlags array vorkommt, wird die Position berechnet.
			seeBall=false;
			getBall=false;
			seeGoal=false;
			seeBeforeGoal=false;
			seeCornerGoalRight=false;
			seeCornerGoalLeft=false;
			for(int i=0; i<NameFlags.length;i++){
				//Nach dem Tor suchen
				if(NameFlags[i].contains(goalName)){
					seeGoal=true;
					goalDist=ValueFlags[i][0];
					goalAngle=ValueFlags[i][1];
				}
				if(NameFlags[i].contains(beforeGoalName)){
					seeBeforeGoal=true;
					beforeGoalDist=ValueFlags[i][0];
					beforeGoalAngle=ValueFlags[i][1];
				}

				if(NameFlags[i].contains(cornerGoalLeft)){
					cornerGoalLeftAngle=ValueFlags[i][0];
					cornerGoalLeftDist=ValueFlags[i][1];
					seeCornerGoalLeft=true;
				}
				if(NameFlags[i].contains(cornerGoalRight)){
					cornerGoalRightAngle=ValueFlags[i][0];
					cornerGoalRightDist=ValueFlags[i][1];
					seeCornerGoalRight=true;
				}


				if(NameFlags[i].equals("(B)")||NameFlags[i].equals("(b)")){

					ballDist=ValueFlags[i][0];
					ballAngle=ValueFlags[i][1];
					ballSpeed=ValueFlags[i][2];

					double ballPosition[];
					ballPosition=calculate_object_position(ballDist, ballAngle);
					ballX=ballPosition[0];
					ballY=ballPosition[1];
					//Wenn der Ball gesehen worden ist, seeBall=true
					seeBall=true;
					//Sind wir am Ball nah genug dran? 
					if(NameFlags[i].equals("(B)")){
						getBall=true;
					}
				}

			}
		}

	}

	public String cutOut(String msg, String startPattern, String endPattern)
	{
		return msg.substring(msg.indexOf(startPattern)+startPattern.length(), msg.indexOf(endPattern, msg.indexOf(startPattern)+startPattern.length()));
	}

	public void parse_init(String msg){
		String Init=cutOut(msg,"(init ",")");
		String[] params=Init.split(" ");
		if(params[0].equals("l")){
			Side="left";
			goalName="(g r)";
			cornerGoalLeft="(f g r t)";
			cornerGoalRight="(f g r b)";
		}
		else{
			Side="right";
			goalName="(g l)";
			cornerGoalLeft="(f g l t)";
			cornerGoalRight="(f g l b)";

		}
		NumberPlayer=Integer.parseInt(params[1]);

		if(params[2].contains("before_kick_off")){
			play=false;
		}
		if(params[2].contains("play_on")){
			play=true;
		}
	}

	public void parse_hear(String msg){
		if(msg.contains("referee")){
			String ref = cutOut(msg,"referee ",")");
			if((ref.equals("kick_off_r")&&Side.equals("right"))||(ref.equals("kick_off_l")&&Side.equals("left")))
				play=true;

			if(ref.contains("goal")){
				if((ref.contains("r")&&Side.equals("right"))||(ref.contains("l")&&Side.equals("left"))){
					goals_me++;
					System.out.println("Tor für "+myName);
					play=false;
				}
				else{
					goals_them++;
					System.out.println("Tor für den Gegner!");
					play=false;
				}

			}			
		}
	}

	public void tactic_striker () throws Exception{
		//Zuerst: Sehe ich den Ball? Wenn nicht, dann drehe ich mich um 30°
		if(!seeBall){
			turn(30);
		}

		//Wenn ich den Ball nun sehe, schaue ich, ob die Entfernung größer als 1 ist.
		else if(ballDist>0.5){
			//Die Entfernung ist größer als 1. Somit muss ich zuerst auf den Ball zulaufen.
			//Schaue ich auf den Ball? 
			if(ballAngle>2||ballAngle<-2){
				//Der Winkel, mit dem ich auf den Ball schaue ist größer als |2| grad.
				//Also muss ich mich um denselben betrag drehen.
				turn(ballAngle);
			}
			//Wenn nun der Winkel nicht größer als |2| grad ist, kann ich loslaufen.
			else{
				dash(100);
			}
		}
		else{
			//Nun ist die Distanz kleiner als 0.5, somit bin ich direkt am Ball. 
			//Jetzt suche ich mir einen Weg zum Tor.
			if(seeGoal){
				//Ich sehe das Tor.
				if(goalDist>30){
					//Das Tor ist noch weiter weg als 35
					if(goalAngle>1||goalAngle<-1){
						//Ich gucke nicht direkt aufs tor. Ich muss mich drehen.
						turn(goalAngle);
					}
					else{
						//ich gucke direkt aufs Tor. Ich muss nur noch überprüfen ob Gegner im Weg sind. 
						if(noGegnerInWayLeft(40,2)&&noGegnerInWayRight(40,2))
							kick(40,0);
						else{
							kick(20,-10);
							System.out.println("Gegner im Weg!Mitte");
						}
					}
				}
				else{
					//Das Tor ist näher als 35
					if(goalAngle>1||goalAngle<-1){
						//Ich gucke nicht direkt aufs tor. Ich muss mich drehen.
						turn(goalAngle);
					}
					else{
						//ich gucke direkt aufs Tor. Ich muss nur noch überprüfen ob Gegner im Weg sind. 
						if(noGegnerInWayLeft(40,2)&&noGegnerInWayRight(40,2))
							kick(100,0);
						else{
							if(seeCornerGoalRight){
								turn(cornerGoalRightAngle-2);
								if(noGegnerInWayLeft(40,2)&&noGegnerInWayRight(40,2)){
									kick(100,0);
								}
							}
							else if(seeCornerGoalLeft){
								turn(cornerGoalLeftAngle+2);
								if(noGegnerInWayLeft(40,2)&&noGegnerInWayRight(40,2)){
									kick(100,0);
								}
							}
							else{
								System.out.println("Gegner im Weg,Sehe keinen Pfosten! ");
							}
						}
					}							
				}
			}
			else if(seeCornerGoalRight){
				if(cornerGoalRightAngle>1||cornerGoalRightAngle<-1){
					turn(cornerGoalRightAngle);
				}
				else{
					if(noGegnerInWayLeft(40,5)&&noGegnerInWayRight(40,5))
						kick(40,0);
					else
						System.out.println("Gegner im Weg! CornerRight");
				}

			}
			else if(seeCornerGoalLeft){
				if(cornerGoalLeftAngle>1||cornerGoalLeftAngle<-1){
					turn(cornerGoalLeftAngle);
				}
				else{
					if(noGegnerInWayLeft(40,5)&&noGegnerInWayRight(40,5))
						kick(40,0);
					else{
						kick(20,-10);
						System.out.println("Gegner im Weg! CornerLeft");
					}
				}

			}
			else{
				turn(90);
			}
		}
	}



	public void parse_sense(String msg){
		Stamina = Double.parseDouble(cutOut(msg,"(stamina "," "));


	}

	public void parse(String msg) throws Exception{

		if(msg.contains("(init")){
			parse_init(msg);
		}
		if(msg.contains("(hear")){
			parse_hear(msg);
		}
		if(msg.contains("(sense_body "))
		{
			parse_sense(msg);	
		}
		if(msg.contains("play_on")){
			play=true;
		}
		if(msg.contains("before_kick_off")){
			play=false;
		}

		if(msg.contains("(see "))
		{	
			parse_see(msg);

			if(play)
			{
				if(playerTypes[NumberPlayer-1].contains("strike")){
					tactic_striker();
				}

			}else
			{
				//Startposition aus dem Array 
				move(playerPosition[NumberPlayer-1][0],playerPosition[NumberPlayer-1][1]);
			}
		}
	}

	public void tactic1 () throws Exception
	{
		for(int i=0;i<10;i++)
		{	dash(100);
		}
	}

	public void turn(double angle) throws Exception
	{
		send("(turn "+angle+")");
		Thread.sleep(100);
	}

	public void dash(int power) throws Exception
	{
		send("(dash "+power+")");
		Thread.sleep(100);
	}
	public void move(double x, double y) throws Exception
	{
		send("(move "+x+" "+y+")");
		Thread.sleep(100);
	}
	public void kick(int power, int angle) throws Exception
	{
		send("(kick "+power+" "+angle+")");
		Thread.sleep(100);
	}
}




