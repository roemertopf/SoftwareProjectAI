package old_versions;

import udp.UDPServer;
import java.lang.Math;

public class Robo_v0 extends UDPServer{
	public int stamina_level, stamina_effort;
	
	
	String flag_id[] = {
			"(f t 0)",
			"(f t r 10)",
			"(f t r 20)",
			"(f t r 30)",
			"(f t r 40)",
			"(f t r 50)",
			"(f r t 30)",
			"(f r t 20)",
			"(f r t 10)",
			"(f r 0)",
			"(f r b 10)",
			"(f r b 20)",
			"(f r b 30)",
			"(f b r 50)",
			"(f b r 40)",
			"(f b r 30)",
			"(f b r 20)",
			"(f b r 10)",
			"(f b 0)",
			
			"(f t l 10)",
			"(f t l 20)",
			"(f t l 30)",
			"(f t l 40)",
			"(f t l 50)",
			"(f l t 30)",
			"(f l t 20)",
			"(f l t 10)",
			"(f l 0)",
			"(f l b 10)",
			"(f l b 20)",
			"(f l b 30)",
			"(f b l 50)",
			"(f b l 40)",
			"(f b l 30)",
			"(f b l 20)",
			"(f b l 10)",
			
			"(f c t)",
			"(f r t)",
			"(f g r t)",
			"(g r)",
			"(f g r b)",
			"(f r b)",
			"(f c b)",
			"(f l b)",
			"(f g l b)",
			"(g l)",
			"(f g l t)",
			"(f l t)",
			
			"(f c)",
			"(f p r t)",
			"(f p r c)",
			"(f p r b)",
			"(f p l t)",
			"(f p l c)",
			"(f p l b)"
	};

	double flag_position[][] = { // { X , Y }
			{    0,	-39},
			{   10,	-39},
			{   20,	-39},
			{   30,	-39},
			{   40,	-39},
			{   50,	-39},
			{ 57.5,	-30},
			{ 57.5,	-20},
			{ 57.5,	-10},
			{ 57.5,	  0},
			{ 57.5,	 10},
			{ 57.5,	 20},
			{ 57.5,	 30},
			{   50,	 39},
			{   40,	 39},
			{   30,	 39},
			{   20,	 39},
			{   10,	 39},
			{    0,	 39},
			
			{-10,	-39},
			{-20,	-39},
			{-30,	-39},
			{-40,	-39},
			{-50,	-39},
			{-57.5,	-30},
			{-57.5,	-20},
			{-57.5,	-10},
			{-57.5,	  0},
			{-57.5,	 10},
			{-57.5,	 20},
			{-57.5,	 30},
			{-  50,	 39},
			{-  40,	 39},
			{-  30,	 39},
			{-  20,	 39},
			{-  10,	 39},
			
			{    0,	-34},
			{ 52.5,	-34},
			{ 52.5,	- 7},
			{ 52.5,	  0},
			{ 52.5,	  7},
			{ 52.5,	 34},
			{    0,	 34},
			{-52.5,	 34},
			{-52.5,	  7},
			{-52.5,	  0},
			{-52.5,	- 7},
			{-52.5,	-34},
			
			{    0,	  0},
			{   36,	-20},
			{   36,	  0},
			{   36,	 20},
			{  -36,	-20},
			{  -36,	  0},
			{  -36,	 20}
	};

	
	
	/**
	 * Gibt ein Array mit den passenden X- und Y-Koordinaten der Flagge zurück
	 * @param flag
	 * @return
	 */
	public double[] getFlagPosition( String flag)
	{
		double position[] = {0,0};
		int i = 0;
		
		while(true){
			if( flag.equals( flag_id[i] ) ){
				break;
			}
			i++;
		}
			
		position[0] = flag_position[i][0];
		position[1] = flag_position[i][1];
		
		return position;
	}
	
	/**
	 * Gibt dem Clienten (Spieler) seine aktuellen X- und Y-Koordinaten.
	 * @param flag1
	 * @param angel1
	 * @param flag2
	 * @param angel2
	 * @return
	 */
	public int[] getPosition( String f1, float angel1, String f2, float angel2)
	{
		int position[] = {0,1};
		
		double flag1[] = getFlagPosition( f1 );
		double flag2[] = getFlagPosition( f2 );
		
		// BERECHNUNG
		
		return position;
	}
	
	public void catch_ball(int dir) throws Exception
	{
		send("(catch " + dir + ")");
		Thread.sleep(100);
	}
	
	public void chance_view(String width, String quality) throws Exception
	{
		send("(change_view " + width + " " + quality +  ")");
		Thread.sleep(100);
	}

	public void kick(int power, int dir) throws Exception
	{
		send("(kick " + power + " " + dir + ")");
		Thread.sleep(100);
	}
	
	public void move(int x, int y) throws Exception
	{
		send("(move " + x + " " + y + ")");
		Thread.sleep(100);
	}

	public void run(int power) throws Exception
	{
		send("(dash " + power + ")");
		Thread.sleep(100);
	}

	public void say(String message) throws Exception
	{
		send("(say " + message + ")");
		Thread.sleep(100);
	}
	
	public void turn(int angel) throws Exception
	{
		send("(turn " + angel + ")");
		Thread.sleep(100);
	}
	
	
	
	public void turn_to_ball() throws Exception
	{
		//
	}
	
	public void set_values(String msg) throws Exception
	{
		stamina_level = Integer.parseInt(cutOut(msg,"(stamina "," "));
		stamina_effort = Integer.parseInt(cutOut(msg,"(stamina ",")").split(" ")[1]);
	}
	
	
	
	public void tactic_1() throws Exception{
		while(true){
			for(int i=0; i<20; i++){
				send("(dash 100)");
				Thread.sleep(100);
			}
			
			turn(90);
		}
	}
	
	
	
	public Robo_v0(String ip, int port, String MyName) throws Exception
	{
		super(ip,port);
	}
	
	public void init(String name, int version) throws Exception
	{	send("(init "+name+" (version "+version+"))");
		Thread.sleep(100);
		move(0,0);
	}
	
	public void bye() throws Exception
	{	send("(bye)");
		close();
	}
	
	public String cutOut(String msg, String startPattern, String endPattern)
	{
		return msg.substring(msg.indexOf(startPattern)+startPattern.length(), msg.indexOf(endPattern, msg.indexOf(startPattern)+startPattern.length()));
	}
	
	public void parse(String msg) throws Exception{
		if(msg.contains("(sense_body "))
		{
			set_values(msg);
			//stamina_level = Integer.parseInt(cutOut(msg,"(stamina "," "));
			//int  first_value = Integer.parseInt(cutOut(msg,"(stamina ",")").split(" ")[0]);
			//int second_value = Integer.parseInt(cutOut(msg,"(stamina ",")").split(" ")[1]);
			
//			System.out.println("Body: "+msg);
		}else if(msg.contains("(see "))
		{	
			System.out.println("See: "+msg);
		
		}else if(msg.contains("play_on"))
		{
			//tactic_1();
		
		}else
		{
//			System.out.println(msg);
		}

//		double flag[] = getFlagPosition( "(f b 0)" );
//		System.out.println("X: " + flag[0] );
//		System.out.println("Y: " + flag[1] );
	}
}
