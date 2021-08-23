import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FlightScheduler {

    private static FlightScheduler instance;
	//private  ArrayList<Location> Locations = new ArrayList<Location>();
	private  ArrayList<Flight> Flights = new ArrayList<Flight>();
	private HashMap<String,Location> Locations = new HashMap<String,Location>();
	private int Flightid;
	
    public static void main(String[] args)  
    {
        instance = new FlightScheduler(args);
        try
        {
        instance.run();
        }
		catch(Exception e)
		{
			//e.printStackTrace();
			//System.out.println();
		}
    }


    public static FlightScheduler getInstance() 
    {
        return instance;
    }

    public FlightScheduler(String[] args) 
    {
    	
         
    }
    
	public HashMap<String,Location> getLocations()
	{
		return Locations;
	}
	
	public ArrayList<Flight> getFlights()
	{
		return Flights;
	}
    

    public void run() throws Exception 

    {
        // Do not use System.exit() anywhere in your code,
        // otherwise it will also exit the auto test suite.
        // Also, do not use static attributes otherwise
        // they will maintain the same values between testcases.

        // START YOUR CODE HERE
		String Exit="begin";
		Scanner sc = new Scanner(System.in);
		while(!Exit.equals("exit"))
		{
			
		System.out.print("User: ");
		
		String str = sc.nextLine();
		Exit=str;
		
		//System.out.println(Exit);
		Stringcheck(str);
		  
		}
		
		sc.close();
		
    }
    
    
    public void Stringcheck(String S) throws Exception
	{
    	//获取用户手动输入的命令
		String str = S;
		
		//用空格拆分
		String[] splitstr=str.split(" ");
		String[] importstr=str.split(",");
		if(splitstr.length==1) 
		{
		 if(splitstr[0].equals("flight"))
		 {
			 
		System.out.println("Usage:\nFLIGHT <id> [BOOK/REMOVE/RESET] [num]\nFLIGHT ADD <departure time> <from> <to> <capacity>\nFLIGHT IMPORT/EXPORT <filename>");
		System.out.println();
		 }
		  else if(splitstr[0].equals("flights"))
			{
			  	Comparator <Flight> Comparator= new Comparator<Flight>(){
		    		public int compare(Flight o1,Flight o2) {
		    			int x1= o1.getindex();
		    			int x2=o2.getindex();
		    			int DayComp = x1-x2;
		                if (DayComp != 0) {
		                    return DayComp;
		                 } else {
		                    String y1 = o1.getdetime();
		                    String y2 = o2.getdetime();
		                    int TimeComp=y1.compareTo(y2);
		                    if(TimeComp != 0)
		                    {
		                    	return TimeComp;
		                    } else{
		                    	String z1=o1.getsource();
		                    	String z2=o1.getsource();
		                    	return z1.compareTo(z2);//地址
		                    }
		                 }
		    		}
			  	};
			  	
			  	ArrayList<Flight> AllFlights = Flights;
			  	Collections.sort(AllFlights,Comparator);
				System.out.println("Flights");
				System.out.println("-------------------------------------------------------");
				System.out.println("ID   Departure   Arrival     Source --> Destination");
				System.out.println("-------------------------------------------------------");
				if(AllFlights.size()==0)
					System.out.println("(None)");
				for(int i=0;i<AllFlights.size();i++)
				{
					AllFlights.get(i).flights();
				}
				
				System.out.println();	
			}
		  else if(splitstr[0].equals("exit"))
		  {
			  System.out.println("Application closed.");
		  }
			else if(splitstr[0].equals("locations"))
			{
				ArrayList <String> Result= new ArrayList<String>();
				System.out.println("Locations "+"("+Locations.size()+")"+":");
				Iterator iter = Locations.entrySet().iterator();
		
	
				while(iter.hasNext())
				{
					
					Map.Entry entry= (Map.Entry) iter.next();
					Object key = entry.getKey();
					Result.add(Locations.get(key).getlocation());
				}
				
			  	Comparator <String> d= new Comparator<String>(){
		    		public int compare(String o1,String o2) {
		    			return o1.compareTo(o2);
		    		}
		    	};
		    	
		    	
				Collections.sort(Result,d);
				if(Result.size()==0)
					System.out.println("(None)");
				for(int x=0;x<Result.size();x++)
				{
					System.out.print(Result.get(x));
					if(Result.size()>1&&x!=Result.size()-1)
						System.out.print(", ");
					else
						System.out.println();
				}
				System.out.println();
			}
			else if(splitstr[0].toLowerCase().equals("help"))
			{
				System.out.println("FLIGHTS - list all available flights ordered by departure time, then departure location name");
				System.out.println("FLIGHT ADD <departure time> <from> <to> <capacity> - add a flight");
				System.out.println("FLIGHT IMPORT/EXPORT <filename> - import/export flights to csv file");
				System.out.println("FLIGHT <id> - view information about a flight (from->to, departure arrival times, current ticket price, capacity, passengers booked)");
				System.out.println("FLIGHT <id> BOOK <num> - book a certain number of passengers for the flight at the current ticket price, "
				+"and then adjust the ticket price to reflect the reduced capacity remaining. If no number is given, book 1 "
				+"passenger. If the given number of bookings is more than the remaining capacity, only accept bookings "
				+"until the capacity is full.");
				System.out.println("FLIGHT <id> REMOVE - remove a flight from the schedule");
				System.out.println("FLIGHT <id> RESET - reset the number of passengers booked to 0, and the ticket price to its original state.");
				System.out.println();
				System.out.println("LOCATIONS - list all available locations in alphabetical order");
				System.out.println("LOCATION ADD <name> <lat> <long> <demand_coefficient> - add a location");
				System.out.println("LOCATION <name> - view details about a location (it's name, coordinates, demand coefficient)");
				System.out.println("LOCATION IMPORT/EXPORT <filename> - import/export locations to csv file");
				System.out.println("SCHEDULE <location_name> - list all departing and arriving flights, in order of the time they arrive/depart");
				System.out.println("DEPARTURES <location_name> - list all departing flights, in order of departure time");
				System.out.println("ARRIVALS <location_name> - list all arriving flights, in order of arrival time");
				System.out.println();
				System.out.println("TRAVEL <from> <to> [sort] [n] - list the nth possible flight route between a starting location and "
						+"destination, with a maximum of 3 stopovers. Default ordering is for shortest overall duration. If n is not "
						+"provided, display the first one in the order. If n is larger than the number of flights available, display the "
						+"last one in the ordering.");
				System.out.println();
				System.out.println("can have other orderings:");
				System.out.println("TRAVEL <from> <to> cost - minimum current cost");
				System.out.println("TRAVEL <from> <to> duration - minimum total duration");
				System.out.println("TRAVEL <from> <to> stopovers - minimum stopovers");
				System.out.println("TRAVEL <from> <to> layover - minimum layover time");
				System.out.println("TRAVEL <from> <to> flight_time - minimum flight time");
				System.out.println();
				System.out.println("HELP - outputs this help string.");
				System.out.println("EXIT - end the program.");
				System.out.println();
			}
			else if(splitstr[0].equals("location"))
			{
				System.out.println("Usage:\\nLOCATION <name>\\nLOCATION ADD "
						+ "<name> <latitude> <longitude> "
						+ "<demand_coefficient>\\nLOCATION "
						+ "IMPORT/EXPORT <filename>");
				System.out.println();
			}

		 else
		 {
			 System.out.println("Invalid command. Type 'help' for a list of commands.");
			 System.out.println();
		 }
		}

		else if(S.toLowerCase().contains("fli"))
		{
			//插入航班
			 if(S.toLowerCase().contains("add"))
			{
				//获取用户输入的关键词	 
					boolean noproblem=true;
			   //判断参数有无问题
				if(splitstr.length<7)
				{
					System.out.println("Usage:   FLIGHT ADD <departure time> <from> <to> <capacity>\nExample: FLIGHT ADD Monday 18:00 Sydney Melbourne 120");
					System.out.println();
		        	noproblem=false;
		        	return;
				}
				else
				{
					//判断输入的字符串是否合法 但是不用检查已经是字符串的location 能不能从String转换成int
					//检查latitue是否合法
					try {
						String deday = splitstr[2];
						String detime = splitstr[3];
					}
					catch(Exception e)
					{
			        	System.out.println("Invalid departure time. Use the format <day_of_week> <hour:minute>, with 24h time.");
			        	System.out.println();
			        	noproblem=false;
			        	return;
					}
					if(noproblem)
					{
						try {
							String source = splitstr[4];
						}
						catch(Exception e)
						{
							System.out.println("Invalid starting location.");
							System.out.println();
				        	noproblem=false;
				        	return;
						}
					}
					
					if(noproblem)
					{
						try {
							String destination = splitstr[5];
						}
						catch(Exception e)
						{
							System.out.println("Invalid ending location.");
							System.out.println();
				        	noproblem=false;
				        	return;
						}
					}
					if(noproblem)
					{
						try {
							int capacity = Integer.parseInt(splitstr[6]);
						}
						catch(Exception e)
						{
							System.out.println("Invalid positive integer capacity.");
							System.out.println();
				        	noproblem=false;
				        	return;
						}
					}

				}
				
				if(noproblem)
				{
					//此时所有的变量都是有值的 而且是有意义合法的 接下来要检查他的取值是否恰当
					String deday = splitstr[2];
					String detime = splitstr[3];
					String source = splitstr[4];
				    String destination = splitstr[5];
				    int capacity = Integer.parseInt(splitstr[6]);
				    boolean s=Locations.containsKey(source.toLowerCase());
				    boolean d=Locations.containsKey(destination.toLowerCase());
				 if(isDate(deday)==false||isTime(detime)==false)
		        {
		        	System.out.println("Invalid departure time. Use the format <day_of_week> <hour:minute>, with 24h time.");
		        	noproblem=false;
		        }
				
				else if(!s)
		        {
		        	System.out.println("Invalid starting location.");
		        	noproblem=false;
		        }
		        
				else if(!d)
		        {
		        	System.out.println("Invalid ending location.");
		        	noproblem=false;
		        }
		        
				else if(capacity<0)
		        {
		        	System.out.println("Invalid positive integer capacity.");
		        	noproblem=false;
		        }
		        
				else if(source.equals(destination))
		        {
		        	System.out.println("Source and destination cannot be the same place.");
		        	noproblem=false;
		        }
				}

				  //调用构造器
		
				String deday = splitstr[2];
				String detime = splitstr[3];
				String source = splitstr[4];
			    String destination = splitstr[5];
			    int capacity = Integer.parseInt(splitstr[6]);
			    Flight F= new Flight(deday,detime,source,destination,capacity);
			   
			    //判断有无冲突
			    if(noproblem)
			    {
			    	//如果数据格式都正确 检查是否冲突
			    	String reason=F.setparamter();
				 if(reason!=null)
			        {
			        	System.out.println(reason);
			        	noproblem=false;
			        }
				
			    }
		        //如果没有问题就插入航班 
			    if(noproblem)
			    {
			    	Flightid++;
			    instance.Flights.add(F);
				System.out.println("Successfully added Flight "+F.getflightID()+".");
			    }
		
				System.out.println();
			}
			  else if(splitstr[0].equals("flights"))
				{
				  	Comparator <Flight> Comparator= new Comparator<Flight>(){
			    		public int compare(Flight o1,Flight o2) {
			    			int x1= o1.getindex();
			    			int x2=o2.getindex();
			    			int DayComp = x1-x2;
			                if (DayComp != 0) {
			                    return DayComp;
			                 } else {
			                    String y1 = o1.getdetime();
			                    String y2 = o2.getdetime();
			                    int TimeComp=y1.compareTo(y2);
			                    if(TimeComp != 0)
			                    {
			                    	return TimeComp;
			                    } else{
			                    	String z1=o1.getsource();
			                    	String z2=o1.getsource();
			                    	return z1.compareTo(z2);//地址
			                    }
			                 }
			    		}
				  	};
				  	
				  	ArrayList<Flight> AllFlights = Flights;
				  	Collections.sort(AllFlights,Comparator);
					System.out.println("Flights");
					System.out.println("-------------------------------------------------------");
					System.out.println("ID   Departure   Arrival     Source --> Destination");
					System.out.println("-------------------------------------------------------");
					if(AllFlights.size()==0)
						System.out.println("(None)");
					for(int i=0;i<AllFlights.size();i++)
					{
						AllFlights.get(i).flights();
					}
					
					System.out.println();	
				}
				//通过CSV文件导入航班
				else if(S.toLowerCase().contains("import"))
				{
					if(!splitstr[0].toLowerCase().equals("flight"))
					{
						System.out.println(S);
						System.out.println();
					}
					else
					{
		            importFlights(splitstr);
		            System.out.println();
					}
				}
				//导出数据库的航班内容为CSV文件
				else if(S.toLowerCase().contains("export"))
				{
					exportFlights(splitstr);
					System.out.println();
				}
				else if(S.contains("book"))
				{
					//给航班订机票
			
			
					try{
						
						try {
							if(splitstr[0].toLowerCase().equals("flight"))
							{
								int FID=Integer.parseInt(splitstr[1]);
								if(FID<0)throw new Exception();
							}
						}
						catch(Exception e)
						{
							System.out.println("Invalid Flight ID.");
							System.out.println();
							return;
						}
						
				
				
						
						int FID=Integer.parseInt(splitstr[1]);
						int number=Integer.parseInt(splitstr[3]);
						if(!ifhaveflight(FID))
						{
							System.out.println("Invalid Flight ID.");
							System.out.println();
						}
						else if(!splitstr[0].equals("flight")||!splitstr[2].equals("book"))
						{
							System.out.println("Invalid command. Type 'help' for a list of commands.");
							System.out.println();
						}
						else if(number<0)
						{
							System.out.println("Invalid number of passengers to book.");
							System.out.println();
						}
                       /*
						else if(Flights.get(FID).isFull())
						{
							System.out.println("Flight is now full.");
							System.out.println();
						}
						*/
						else if(FID<0)
						{
							System.out.println("Invalid Flight ID.");
							System.out.println();
						}
						else
						{
						//当订票之后不超过最大容量
						if(number+Flights.get(FID).getbook()<=Flights.get(FID).getcapacity())
						{
						double Totalprice=Flights.get(FID).book(number);
						System.out.println("Booked "+number+" passengers on flight "+ FID+" for a total cost of $"+String.format("%.2f", Totalprice).toString());
						if(Flights.get(FID).isFull())
							System.out.println("Flight is now full.");
						System.out.println();
						}
						//当订票之后超过最大容量
						else if(number+Flights.get(FID).getbook()>Flights.get(FID).getcapacity())
						{
							int maxnumber=Flights.get(FID).getcapacity()-Flights.get(FID).getbook();
						double Totalprice=Flights.get(FID).book(number);
						System.out.println("Booked "+maxnumber+" passengers on flight "+ FID+" for a total cost of $"+String.format("%.2f", Totalprice).toString());
						         if(Flights.get(FID).isFull())
							System.out.println("Flight is now full.");
						System.out.println();
						}
						}
						
					}
						catch(Exception e)
						{
							System.out.println("Invalid command. Type 'help' for a list of commands.");
							System.out.println();
						}
					
				}
			     
				else if(S.toLowerCase().contains("reset"))
				{
					try
					{
						int FID=Integer.parseInt(splitstr[1]);
					}
					catch(Exception e)
					{
						System.out.println("Invalid Flight ID.");
						System.out.println();
						return;
					}
					int FID=Integer.parseInt(splitstr[1]);
					if(ifhaveflight(FID))
					{
						int Flightsindex=0;
						for(int i=0;i<Flights.size();i++)
						{
							if(Flights.get(i).getflightID()==FID)
							{
								Flightsindex=i;
								break;
							}
						}
						Flights.get(Flightsindex).resetbook();
						System.out.println("Reset passengers booked to 0 for Flight "+FID+", "+Flights.get(Flightsindex).getformatofdeparturetime()
								+" "+Locations.get(Flights.get(Flightsindex).getsource().toLowerCase()).getlocation()+" -->"+" "+
								Locations.get(Flights.get(Flightsindex).getdestination().toLowerCase()).getlocation()+".");
						System.out.println();
					}
					else
					{
						System.out.println("Invalid Flight ID.");
						System.out.println();
					}
					
				}
				else if(S.toLowerCase().contains("remove"))
				{
					try
					{
						int FID=Integer.parseInt(splitstr[1]);
					}
					catch(Exception e)
					{
						System.out.println("Invalid Flight ID.");
						System.out.println();
						return;
					}
					int FID=Integer.parseInt(splitstr[1]);
					if(ifhaveflight(FID))
					{
						String source = null;
						String destination=null;
						int Flightsindex=0;
						int Sourceindex=0;
						int Destinationindex=0;
						for(int i=0;i<Flights.size();i++)
						{
							if(Flights.get(i).getflightID()==FID)
							{
								Flightsindex=i;
								source=Flights.get(Flightsindex).getsource();
								destination=Flights.get(Flightsindex).getdestination();
								break;
							}
						}
						ArrayList<Flight> SourceFlight = Locations.get(source.toLowerCase()).getderecord();
						ArrayList<Flight> DestinationFlight = Locations.get(destination.toLowerCase()).getarrecord();
						for(int i=0;i<SourceFlight.size();i++)
						{
							if(SourceFlight.get(i).getflightID()==FID)
							{
								Sourceindex=i;
								break;
							}
						}
						for(int i=0;i<DestinationFlight.size();i++)
						{
							if(DestinationFlight.get(i).getflightID()==FID)
							{
								Destinationindex=i;
								break;
							}
						}
						Locations.get(source.toLowerCase()).getderecord().remove(Sourceindex);
						Locations.get(destination.toLowerCase()).getarrecord().remove(Destinationindex);
						System.out.println("Removed Flight "+FID+", "+Flights.get(Flightsindex).getformatofdeparturetime()
								+" "+Locations.get(Flights.get(Flightsindex).getsource().toLowerCase()).getlocation()+" -->"+" "+
								Locations.get(Flights.get(Flightsindex).getdestination().toLowerCase()).getlocation()+", from the flight schedule.");
						Flights.remove(Flightsindex);
						System.out.println();
					}
					else
					{
						System.out.println("Invalid Flight ID.");
						System.out.println();
					}
				}
				else if(splitstr[0].equals("flight")&&!splitstr[1].equals("add"))
				{
					int flightindex=0;
					try {
						
					flightindex=Integer.parseInt(splitstr[1]);
					
					}
					catch(Exception e)
					{
						//发现第二位不是数字产生报错 
						flightindex=instance.Flights.size();
						
					}
					if(flightindex>=instance.Flights.size()||flightindex<0)
					{
						System.out.println("Invalid Flight ID.");
					    System.out.println();
					}
					else
					instance.Flights.get(flightindex).flight();
					
				}
				else
				{
					System.out.println("Invalid command. Type 'help' for a list of commands.");
					System.out.println();
				}
		}
		else if(S.toLowerCase().contains("location"))
		{
			  if(splitstr[0].equals("location")&&splitstr[1].equals("add"))
				{
					

					
					boolean noproblem=true;
					if(splitstr.length<6)
					{
						System.out.println("Usage:   LOCATION ADD <name> <lat> <long> <demand_coefficient>\nExample: LOCATION ADD Sydney -33.847927 150.651786 0.2");
						noproblem=false;
					}
					else
					{
						//判断输入的字符串是否合法 但是不用检查已经是字符串的location 能不能从String转换成int
						//检查latitue是否合法
						try {
							 double latitude = Double.parseDouble(splitstr[3]);
							 
							 
						}
						catch(Exception e)
						{
						    System.out.println("Invalid latitude. It must be a number of degrees between -85 and +85.");
						    noproblem=false;
						}
						//检查longitude是否合法
						if(noproblem)
						{
						try {
							 double longitude= Double.parseDouble(splitstr[4]);
						}
						catch(Exception e)
						{
							System.out.println("Invalid longitude. It must be a number of degrees between -180 and +180.");
						    noproblem=false;
						}
						
						}
						
						//检查cofficient是否合法
						if(noproblem)
						{
						try {
							
							double coefficient= Double.parseDouble(splitstr[5]);
							 
						}
						catch(Exception e)
						{
							System.out.println("Invalid demand coefficient. It must be a number between -1 and +1.");
						    noproblem=false;
						}
						}
					     
		                   //检查取值范围是否符合要求
						if(noproblem)
						{
							//获取用户输入的关键词 此时的值都是有意义的 只有检查它们的取值是否恰当
							
							String location = splitstr[2];
							double latitude = Double.parseDouble(splitstr[3]);
							double longitude= Double.parseDouble(splitstr[4]);
							double coefficient= Double.parseDouble(splitstr[5]);
							
							//检查是否有地名重复
					 if(Locations.containsKey(location.toLowerCase()))
					{
						System.out.println("This location already exists.");
						noproblem=false;
					}
					       //检查是否在规定的经度范围
					else if(latitude>85||latitude<-85)
					{
					    System.out.println("Invalid latitude. It must be a number of degrees between -85 and +85.");
					    noproblem=false;
					}
					       //检查是否在规定的维度范围
					else if(longitude>180||longitude<-180)
					{
					    System.out.println("Invalid longitude. It must be a number of degrees between -180 and +180.");
					    noproblem=false;
					}
					       //检查是否在规定的需求系数范围
					else if(coefficient>1||coefficient<-1)
					{
						System.out.println("Invalid demand coefficient. It must be a number between -1 and +1.");
						noproblem=false;
					}
					}
						
					}//end else
					
					//检查成功 没有任何问题 就向hashmap里面插入值
					if(noproblem)
					{
						String location = splitstr[2];
						double latitude = Double.parseDouble(splitstr[3]);
						double longitude= Double.parseDouble(splitstr[4]);
						double coefficient= Double.parseDouble(splitstr[5]);
						//调用构造器
					Location L= new Location(location,latitude,longitude,coefficient);
					instance.Locations.put(location.toLowerCase(),L);
					System.out.println("Successfully added location "+location+".");
					
					}
					 
			
					System.out.println();
				}
				//通过CSV文件导入地址
				else if(splitstr[0].equals("location")&&splitstr[1].equals("import"))
				{
					importLocations(splitstr);
					System.out.println();
				}

				//导出数据库的地址内容为CSV文件
				else if(splitstr[0].equals("location")&&splitstr[1].equals("export"))
				{
					exportLocations(splitstr);
					System.out.println();
				}
			    
				else if(splitstr[0].equals("location"))
				{
					if(Locations.containsKey(splitstr[1].toLowerCase()))
					{
						String positive=null;
						if(Locations.get(splitstr[1].toLowerCase()).getcoefficient()>0)
							positive="+";
						else if(Locations.get(splitstr[1].toLowerCase()).getcoefficient()<0)
							positive="-";

							
						System.out.println("Location:    "+Locations.get(splitstr[1].toLowerCase()).getlocation());
						System.out.println("Latitude:    "+String.format("%.6f", Locations.get(splitstr[1].toLowerCase()).getlatitude()));
						System.out.println("Longitude:   "+String.format("%.6f", Locations.get(splitstr[1].toLowerCase()).getlongitude()));
						System.out.println("Demand:      "+positive+String.format("%.4f", Locations.get(splitstr[1].toLowerCase()).getcoefficient()));
						System.out.println();
					}
					else
					{
						System.out.println("Invalid location name.");
						System.out.println();
					}
				}
			  
				else
				{
					System.out.println("Invalid command. Type 'help' for a list of commands.");
					System.out.println();
				}

		}

		
		//打印当地全部记录
		else if(splitstr[0].equals("schedule"))
		{
			try {
				
			ArrayList<Flight> record1=Locations.get(splitstr[1].toLowerCase()).getarrecord();
			ArrayList<Flight> record2=Locations.get(splitstr[1].toLowerCase()).getderecord();
			record1.addAll(record2);
		  	Comparator <Flight> Comparator= new Comparator<Flight>(){
	    		public int compare(Flight o1,Flight o2) {
	    			int x1=0;
	    			int x2=0;
	    			if(o1.getsource().toLowerCase().equals(splitstr[1].toLowerCase()))
	    			    x1=o1.getindex();
	    			else
	    				 x1=o1.getindex2();
	    			if(o2.getsource().toLowerCase().equals(splitstr[1].toLowerCase()))
	    			    x2=o2.getindex();
	    			else
	    				 x2=o2.getindex2();
	    			int DayComp = x1-x2;
	                if (DayComp != 0) {
	                    return DayComp;
	                 } else {
		                    String y1 = null;
		                    String y2 = null;
	                  if(o1.getsource().toLowerCase().equals(splitstr[1].toLowerCase()))
                               y1=o1.getdetime();
	                  else
	                	  y1=o1.getartime();
	                  if(o2.getsource().toLowerCase().equals(splitstr[1].toLowerCase()))
                          y2=o2.getdetime();
                      else
               	               y2=o2.getartime();
	                  
	                    int TimeComp=y1.compareTo(y2);
	                    if(TimeComp != 0)
	                    {
	                    	return TimeComp;
	                    } else{
	                    	String z1=o1.getsource();
	                    	String z2=o1.getsource();
	                    	return z1.compareTo(z2);//地址
	                    }
	                 }
	    		}
		  	};
		  	
		  	Collections.sort(record1,Comparator);
		  	System.out.println(Locations.get(splitstr[1].toLowerCase()).getlocation());
		  	System.out.println("-------------------------------------------------------");
		  	System.out.println("ID   Time        Departure/Arrival to/from Location");
		  	System.out.println("-------------------------------------------------------");
		  	
		  	
				    for(int i=0;i<record1.size();i++)
					{
				    	String space=null;
				    	if(String.valueOf(record1.get(i).getflightID()).length()==1)
				    		space="  ";
				    	else if(String.valueOf(record1.get(i).getflightID()).length()==2)
				    	    space=" ";
				    	else
				    		space="";
				    	if(record1.get(i).getsource().toLowerCase().equals(splitstr[1].toLowerCase()))
						System.out.println(space+" "+record1.get(i).getonetorecord());
				    	else
				    		System.out.println(space+" "+record1.get(i).getonefromrecord());	
					}
				    record1.removeAll(record2);
				    System.out.println();
			}
			catch (Exception e)
			{
				System.out.println("This location does not exist in the system.");
				System.out.println();
			}
		}

		

		//打印起飞记录
		
		else if(splitstr[0].equals("departures"))
		{
			try {
			ArrayList<Flight> record=Locations.get(splitstr[1].toLowerCase()).getderecord();
		  	Comparator <Flight> Comparator= new Comparator<Flight>(){
	    		public int compare(Flight o1,Flight o2) {
	    			int x1= o1.getindex();
	    			int x2=o2.getindex();
	    			int DayComp = x1-x2;
	                if (DayComp != 0) {
	                    return DayComp;
	                 } else {
	                    String y1 = o1.getdetime();
	                    String y2 = o2.getdetime();
	                    int TimeComp=y1.compareTo(y2);
	                    if(TimeComp != 0)
	                    {
	                    	return TimeComp;
	                    } else{
	                    	String z1=o1.getsource();
	                    	String z2=o1.getsource();
	                    	return z1.compareTo(z2);//地址
	                    }
	                 }
	    		}
		  	};
		  	
		  	Collections.sort(record,Comparator);
		  	System.out.println(Locations.get(splitstr[1].toLowerCase()).getlocation());
		  	System.out.println("-------------------------------------------------------");
		  	System.out.println("ID   Time        Departure/Arrival to/from Location");
		  	System.out.println("-------------------------------------------------------");
			for(int i=0;i<record.size();i++)
			{
		    	String space=null;
		    	if(String.valueOf(record.get(i).getflightID()).length()==1)
		    		space="  ";
		    	else if(String.valueOf(record.get(i).getflightID()).length()==2)
		    	    space=" ";
		    	else
		    		space="";
				System.out.println(space+" "+record.get(i).getonetorecord());
			}
			System.out.println();
			}
			catch (Exception e)
			{
				System.out.println("This location does not exist in the system.");
				System.out.println();
			}
			
		}

		
		//打印降落记录
		else if(splitstr[0].equals("arrivals"))
		{
			try {
			
			ArrayList<Flight> record=Locations.get(splitstr[1].toLowerCase()).getarrecord();
		  	Comparator <Flight> Comparator= new Comparator<Flight>(){
	    		public int compare(Flight o1,Flight o2) {
	    			int x1= o1.getindex2();
	    			int x2=o2.getindex2();
	    			int DayComp = x1-x2;
	                if (DayComp != 0) {
	                    return DayComp;
	                 } else {
	                    String y1 = o1.getartime();
	                    String y2 = o2.getartime();
	                    int TimeComp=y1.compareTo(y2);
	                    if(TimeComp != 0)
	                    {
	                    	return TimeComp;
	                    } else{
	                    	String z1=o1.getsource();
	                    	String z2=o1.getsource();
	                    	return z1.compareTo(z2);//地址
	                    }
	                 }
	    		}
		  	};
		  	
		  	Collections.sort(record,Comparator);
		  	System.out.println(Locations.get(splitstr[1].toLowerCase()).getlocation());
		  	System.out.println("-------------------------------------------------------");
		  	System.out.println("ID   Time        Departure/Arrival to/from Location");
		  	System.out.println("-------------------------------------------------------");
				    for(int i=0;i<record.size();i++)
					{
				    	String space=null;
				    	if(String.valueOf(record.get(i).getflightID()).length()==1)
				    		space="  ";
				    	else if(String.valueOf(record.get(i).getflightID()).length()==2)
				    	    space=" ";
				    	else
				    		space="";
						System.out.println(space+" "+record.get(i).getonefromrecord());
					}
				    System.out.println();
			}
			catch (Exception e)
			{
				System.out.println("This location does not exist in the system.");
				System.out.println();
			}
		}	
		
	}

    
    
    // Add a flight to the database
	// handle error cases and return status negative if error 
	// (different status codes for different messages)
	// do not print out anything in this function
	public int addFlight(String date1, String date2, String start, String end, String capacity, int booked) throws ParseException {
		
			//获取用户输入的关键词	 
				boolean noproblem=true;
		   //判断参数有无问题
	
			if(noproblem)
			{
				//判断输入的字符串是否合法 但是不用检查已经是字符串的location 能不能从String转换成int
				//检查latitue是否合法
				try {
					String deday = date1;
					String detime = date2;
				}
				catch(Exception e)
				{
		        	
		        	noproblem=false;
				}
				if(noproblem)
				{
					try {
						String source = start;
					}
					catch(Exception e)
					{
						
			        	noproblem=false;
					}
				}
				
				if(noproblem)
				{
					try {
						String destination = end;
					}
					catch(Exception e)
					{
						
			        	noproblem=false;
					}
				}
				if(noproblem)
				{
					try {
						int thiscapacity = Integer.parseInt(capacity);
					}
					catch(Exception e)
					{
						
			        	noproblem=false;
					}
				}
				
				

			}
			
			if(noproblem)
			{
				//此时所有的变量都是有值的 而且是有意义合法的 接下来要检查他的取值是否恰当
				String deday = date1;
				String detime = date2;
				String source = start;
			    String destination = end;
			    int thiscapacity = Integer.parseInt(capacity);
			    boolean s=Locations.containsKey(source.toLowerCase());
			    boolean d=Locations.containsKey(destination.toLowerCase());
			 if(isDate(deday)==false||isTime(detime)==false)
	        {
	        	
	        	noproblem=false;
	        }
			
			else if(!s)
	        {
	        	
	        	noproblem=false;
	        }
	        
			else if(!d)
	        {
	        	
	        	noproblem=false;
	        }
	        
			else if(thiscapacity<0)
	        {
	        
	        	noproblem=false;
	        }
	        
			else if(source.equals(destination))
	        {
	        	
	        	noproblem=false;
	        }
			}

			  //调用构造器
			String deday = date1;
			String detime = date2;
			String source = start;
		    String destination = end;
		    int thiscapacity = Integer.parseInt(capacity);
		    Flight F= new Flight(deday,detime,source,destination,thiscapacity,booked);
		    String reason=F.setparamter();
		    
		    //判断有无冲突
			 if(reason!=null&&noproblem)
		        {
		        	
		        	noproblem=false;
		        }
			
       
	        //如果没有问题就插入航班 
		    if(noproblem)
		    {
		    	Flightid++;
		    instance.Flights.add(F);
			return 1;
		    }
	        else
		    return -1;
		
	}
	
	// Add a location to the database
    // do not print out anything in this function
    // return negative numbers for error cases
	public int addLocation(String name, String lat, String lon, String demand) {
		boolean noproblem=true;
             if(noproblem)
		{
			//判断输入的字符串是否合法 但是不用检查已经是字符串的location 能不能从String转换成int
			//检查latitue是否合法
			try {
				 double latitude = Double.parseDouble(lat);
			}
			catch(Exception e)
			{
			    noproblem=false;
			}
			//检查longitude是否合法
			if(noproblem)
			{
			try {
				 double longitude= Double.parseDouble(lon);
			}
			catch(Exception e)
			{
			    noproblem=false;
			}
			
			}
			
			//检查cofficient是否合法
			if(noproblem)
			{
			try {
				
				double coefficient= Double.parseDouble(demand);
				 
			}
			catch(Exception e)
			{
			    noproblem=false;
			}
			}
		     
               //检查取值范围是否符合要求
			if(noproblem)
			{
				//获取用户输入的关键词 此时的值都是有意义的 只有检查它们的取值是否恰当
				
				String location = name;
				double latitude = Double.parseDouble(lat);
				double longitude= Double.parseDouble(lon);
				double coefficient= Double.parseDouble(demand);
				
				//检查是否有地名重复
		 if(Locations.containsKey(location.toLowerCase()))
		{
			noproblem=false;
		}
		       //检查是否在规定的经度范围
		else if(latitude>85||latitude<-85)
		{
		    noproblem=false;
		}
		       //检查是否在规定的维度范围
		else if(longitude>180||longitude<-180)
		{
		    noproblem=false;
		}
		       //检查是否在规定的需求系数范围
		else if(coefficient>1||coefficient<-1)
		{
			noproblem=false;
		}
		}
			
		}//end else
		
		//检查成功 没有任何问题 就向hashmap里面插入值
		if(noproblem)
		{
			String location = name;
			double latitude = Double.parseDouble(lat);
			double longitude= Double.parseDouble(lon);
			double coefficient= Double.parseDouble(demand);
			//调用构造器
		Location L= new Location(location,latitude,longitude,coefficient);
		instance.Locations.put(location.toLowerCase(),L);
		return 1;
		}
		else
	    return -1;
		
	}
	
	//flight import <filename>
	public void importFlights(String[] command) throws ParseException {
		try {
			if (command.length < 3) throw new FileNotFoundException();
			BufferedReader br = new BufferedReader(new FileReader(new File(command[2])));
			String line;
			int count = 0;
			int err = 0;
			
			while ((line = br.readLine()) != null) {
				String[] lparts = line.split(",");
				if (lparts.length < 5) continue;
				String[] dparts = lparts[0].split(" ");
				if (dparts.length < 2) continue;
				int booked = 0;
				
				try {
					booked = Integer.parseInt(lparts[4]);
					
				} catch (NumberFormatException e) {
					continue;
				}
				
				int status = addFlight(dparts[0], dparts[1], lparts[1], lparts[2], lparts[3], booked);
				if (status < 0) {
					err++;
					continue;
				}
				count++;
			}
			br.close();
			System.out.println("Imported "+count+" flight"+(count!=1?"s":"")+".");
			if (err > 0) {
				if (err == 1) System.out.println("1 line was invalid.");
				else System.out.println(err+" lines were invalid.");
			}
		} catch (IOException e) {
			System.out.println("Error reading file.");
			return;
		}
	}
	
	//location import <filename>
	public void importLocations(String[] command) {
		try {
			if (command.length < 3) throw new FileNotFoundException();
			BufferedReader br = new BufferedReader(new FileReader(new File(command[2])));
			String line;
			int count = 0;
			int err = 0;
			
			while ((line = br.readLine()) != null) {
				String[] lparts = line.split(",");
				if (lparts.length < 4) continue;
								
				int status = addLocation(lparts[0], lparts[1], lparts[2], lparts[3]);
				if (status < 0) {
					err++;
					continue;
				}
				count++;
			}
			br.close();
			System.out.println("Imported "+count+" location"+(count!=1?"s":"")+".");
			if (err > 0) {
				if (err == 1) System.out.println("1 line was invalid.");
				else System.out.println(err+" lines were invalid.");
			}
			
		} catch (IOException e) {
			System.out.println("Error reading file.");
			return;
		}
	}
	public void exportFlights(String[] command) {
		try {
			if (command.length < 3) throw new FileNotFoundException();
			BufferedWriter br = new BufferedWriter(new FileWriter(new File(command[2])));
			int count = 0;
		  	Comparator <Flight> Comparator= new Comparator<Flight>(){
	    		public int compare(Flight o1,Flight o2) {
	    			int x1= o1.getindex();
	    			int x2=o2.getindex();
	    			int DayComp = x1-x2;
	                if (DayComp != 0) {
	                    return DayComp;
	                 } else {
	                    String y1 = o1.getdetime();
	                    String y2 = o2.getdetime();
	                    int TimeComp=y1.compareTo(y2);
	                    if(TimeComp != 0)
	                    {
	                    	return TimeComp;
	                    } else{
	                    	String z1=o1.getsource();
	                    	String z2=o1.getsource();
	                    	return z1.compareTo(z2);//地址
	                    }
	                 }
	    		}
		  	};
		  	
		  	ArrayList<Flight> AllFlights = Flights;
		  	Collections.sort(AllFlights,Comparator);
			for(int i=0;i<AllFlights.size();i++) 
			{
				String line=AllFlights.get(count).getdeday()+" "+AllFlights.get(count).getdetime()+","+Locations.get(AllFlights.get(count).getsource().toLowerCase()).getlocation()+","
			+Locations.get(AllFlights.get(count).getdestination().toLowerCase()).getlocation()
						+","+AllFlights.get(count).getcapacity()+","+AllFlights.get(count).getbook()+"\n";
				br.write(line);
                count++;
			}
			br.close();
			System.out.println("Exported "+count+" flight"+(count!=1?"s":"")+".");
			
		} catch (IOException e) {
			System.out.println("Error writing file.");
			return;
		}
	}
	
	public void exportLocations(String[] command) {
		try {
			if (command.length < 3) throw new FileNotFoundException();
			BufferedWriter br = new BufferedWriter(new FileWriter(new File(command[2])));
			int count = 0;
			ArrayList <String> Result= new ArrayList<String>();
			Iterator iter = Locations.entrySet().iterator();	
			while(iter.hasNext())
			{
				
				Map.Entry entry= (Map.Entry) iter.next();
				Object key = entry.getKey();
				String oneline=Locations.get(key).getlocation()+","+Locations.get(key).getlatitude()+","+Locations.get(key).getlongitude()+","+Locations.get(key).getcoefficient();
				Result.add(oneline);
			}
			
		  	Comparator <String> d= new Comparator<String>(){
	    		public int compare(String o1,String o2) {
	    			return o1.compareTo(o2);
	    		}
	    	};
	    	
	    	
			Collections.sort(Result,d);
		     
			for(int i=0;i<Result.size();i++) 
			{
				
				br.write(Result.get(count)+"\n");
                count++;
			}
			br.close();
			System.out.println("Exported "+count+" location"+(count!=1?"s":"")+".");
			
		} catch (IOException e) {
			System.out.println("Error writing file.");
			return;
		}
	}
	
	
	public boolean isTime(String s)
	{
		String[] Str=s.split(":");

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		try 
		{
			int hour=Integer.parseInt(Str[0]);
			int min=Integer.parseInt(Str[1]);
			sdf.parse(s);
			if(hour>=24||hour<0||min>=60||min<0)
				return false;
			else
			return true;
		}
		catch(Exception e)
		{
			return false;
		}

	}
	
	public boolean isDate(String s)
	{
		boolean found=false;
		String[] strs= {"monday","tuesday","wednesday","thursday","friday","saturday","sunday"};

        for(String str:strs)
        {
            if(str.indexOf(s.toLowerCase())>=0)
            {
                found=true;
                break;
            }
            else
            	found=false;
        }
        return found;

	}
	public boolean ifhaveflight(int S)
	{   
		
		boolean found=false;
		int flightindex=0;//航班号
		try {
			
		flightindex=S;
		
		for(int i=0;i<Flights.size();i++)
		{
			if(Flights.get(i).getflightID()==flightindex)
			{
				flightindex=i;
				found=true;
				break;
			}
		}
		if(!found)throw new Exception();
		if(Flights.size()==0)throw new Exception();
		}
		catch(Exception e)
		{
			//发现第二位不是数字产生报错 
			flightindex=instance.Flights.size();
			
		}
		if(flightindex>=instance.Flights.size()||flightindex<0)
		{
			return false;
		}
		else
			return true;
		
	}
	public int getfid()
	{
		return this.Flightid;
	}
}
