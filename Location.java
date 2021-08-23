
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
public class Location {

	private String location;
	private double latitude;
	private double longitude;
	private double coefficient;
	private ArrayList<Flight> derecord = new ArrayList<Flight>();
	private ArrayList<Flight> arrecord = new ArrayList<Flight>();
	Map<String, String> map = new HashMap<String, String>(){
        private static final long serialVersionUID = 1L;
        {
            put("Mon", "1");
            put("Tue", "2");
            put("Wed", "3");
            put("Thu", "4");
            put("Fri", "5");
            put("Sat", "6");
            put("Sun", "7");
            
        }
    };
	Map<String, String> fulldatename = new HashMap<String, String>(){
 
        {
            put("Mon", "Monday");
            put("Tue", "Tuesday");
            put("Wed", "Wednesday");
            put("Thu", "Thursday");
            put("Fri", "Friday");
            put("Sat", "Saturday");
            put("Sun", "Sunday");
            
        }
    };
	public Location(String location,double latitude,double longitude,double coefficient)
	{
		this.location=location;
		this.latitude=latitude;
		this.longitude=longitude;
		this.coefficient=coefficient;
		
	}
	public ArrayList<Flight> getderecord()
	{
		
		return this.derecord;
	}
	public ArrayList<Flight> getarrecord()
	{
		
		return this.arrecord;
	}
	
	public String getlocation()	
	{
		return this.location;
	}
	
	public double getlatitude()
	{
		return this.latitude;
	}
	
	public double getlongitude()
	{
		return this.longitude;
	}
	
	public double getcoefficient()
	{
		return this.coefficient;
	}

    //Implement the Haversine formula - return value in kilometres
    public static double distance(Location l1, Location l2) 
    {
    	 final int R = 6371; // Radious of the earth
		Double lat1=l1.getlatitude();
		Double lon1=l1.getlongitude();
		Double lat2=l2.getlatitude();
		Double lon2=l2.getlongitude();

		 Double latDistance = toRad(lat2-lat1);
		 Double lonDistance = toRad(lon2-lon1);
		 Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
		 Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
		 Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		 Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		 
		double distance = R * c;//计算出飞行距离

         return distance;
    }


	
	public void addDeparture(Flight f) 
	{
		
		this.getderecord().add(f);
	}
	
    public void addArrival(Flight f) 
    {
	       
	      
	       this.getarrecord().add(f);
	}
    
	
	/**
	 * Check to see if Flight f can depart from this location.
	 * If there is a clash, the clashing flight string is returned, otherwise null is returned.
	 * A conflict is determined by if any other flights are arriving or departing at this location within an hour of this flight's departure time.
	 * @param f The flight to check.
	 * @return "Flight <id> [departing/arriving] from <name> on <clashingFlightTime>". Return null if there is no clash.
	 * @throws ParseException 
	 */
	public String hasRunwayDepartureSpace(Flight f) throws ParseException 
	{
		
		String conflictreason = null;
	   //先检查本地 是否有起飞的航班
		Location L1=FlightScheduler.getInstance().getLocations().get(f.getsource().toLowerCase());
		Location L2=FlightScheduler.getInstance().getLocations().get(f.getdestination().toLowerCase());
		ArrayList<Flight> record1 =L1.getderecord();
		ArrayList<Flight> record2 =L1.getarrecord();
		if(conflictreason==null)
		{
	       for(int i=0;i<record1.size();i++)
	       {
	    	 //拿到起飞地的时间表并把现在航班的信息与时间表内的信息做对比来检查冲突
	    	String recording[]=record1.get(i).getonetorecord().split(" ");
	   		String S=map.get(recording[1])+" "+recording[2];
			Date nowTime = new SimpleDateFormat("dd HH:mm").parse(S);
			Calendar cal = Calendar.getInstance();
			cal.setTime(nowTime);
			cal.add(Calendar.HOUR, -1);
			Date startTime=cal.getTime();
			cal.setTime(nowTime);
			cal.add(Calendar.HOUR, 1);
			Date endTime =cal.getTime();
			Date planTime = new SimpleDateFormat("dd HH:mm").parse(map.get(f.getdeday().substring(0, 3))+" "+f.getdetime());
			if(isEffectiveDate(planTime, startTime, endTime))
			{
				
				//System.out.println(planTime);
				//System.out.println(startTime);
				//System.out.println(endTime);
				

				String s="Scheduling conflict! This flight clashes with Flight "+recording[0]+" departing from "+L1.getlocation()+ " on "+fulldatename.get(recording[1])+" "+recording[2]+".";
				conflictreason=s;
		           if(nowTime.after(planTime))
						  break;
			}
	 
	       }
		}
		//如果没有起飞的航班 就检查本地有没有降落的航班
		if(conflictreason==null)
		{
		       for(int i=0;i<record2.size();i++)
		       {
		    	 //拿到起飞地的时间表并把现在航班的信息与时间表内的信息做对比来检查冲突
		    	String recording[]=record2.get(i).getonefromrecord().split(" ");
		   		String S=map.get(recording[1])+" "+recording[2];
				Date nowTime = new SimpleDateFormat("dd HH:mm").parse(S);
				Calendar cal = Calendar.getInstance();
				cal.setTime(nowTime);
				cal.add(Calendar.HOUR, -1);
				Date startTime=cal.getTime();
				cal.setTime(nowTime);
				cal.add(Calendar.HOUR, 1);
				Date endTime =cal.getTime();
				Date planTime = new SimpleDateFormat("dd HH:mm").parse(map.get(f.getdeday().substring(0, 3))+" "+f.getdetime());
				if(isEffectiveDate(planTime, startTime, endTime))
				{
					
					//System.out.println(planTime);
					//System.out.println(startTime);
					//System.out.println(endTime);

					String s="Scheduling conflict! This flight clashes with Flight "+recording[0]+" arriving at "+L1.getlocation()+" on "+fulldatename.get(recording[1])+" "+recording[2]+".";
					conflictreason=s;
			           if(nowTime.after(planTime))
							  break;
				}
		 
		       }
			}
		
		return conflictreason;

    }

    /**
	 * Check to see if Flight f can arrive at this location.
	 * A conflict is determined by if any other flights are arriving or departing at this location within an hour of this flight's arrival time.
	 * @param f The flight to check.
	 * @return String representing the clashing flight, or null if there is no clash. Eg. "Flight <id> [departing/arriving] from <name> on <clashingFlightTime>"
     * @throws ParseException 
	 */
	public String hasRunwayArrivalSpace(Flight f) throws ParseException 
	{
		String conflictreason = null;
		Location L1=FlightScheduler.getInstance().getLocations().get(f.getsource().toLowerCase());
		Location L2=FlightScheduler.getInstance().getLocations().get(f.getdestination().toLowerCase());
		ArrayList<Flight> record1 =L2.getderecord();
		ArrayList<Flight> record2 =L2.getarrecord();
		   //先检查本地 是否有起飞的航班
		if(conflictreason==null)
		{
	       for(int i=0;i<record1.size();i++)
	       {												
		    	String recording[]=record1.get(i).getonetorecord().split(" ");
		   		String S=map.get(recording[1])+" "+recording[2];
				Date nowTime = new SimpleDateFormat("dd HH:mm").parse(S);
				Calendar cal = Calendar.getInstance();
				cal.setTime(nowTime);
				cal.add(Calendar.HOUR, -1);
				Date startTime=cal.getTime();
				cal.setTime(nowTime);
				cal.add(Calendar.HOUR, 1);
				Date endTime =cal.getTime();
				Date planTime = new SimpleDateFormat("dd HH:mm").parse(map.get(f.getarday().substring(0, 3))+" "+f.getartime());
				if(isEffectiveDate(planTime, startTime, endTime))
				{
					
					//System.out.println(planTime);
					//System.out.println(startTime);
					//System.out.println(endTime);
					
					String s="Scheduling conflict! This flight clashes with Flight "+recording[0]+" departing from "+L2.getlocation()+ " on "+fulldatename.get(recording[1])+" "+recording[2]+".";
					conflictreason=s;
			           if(nowTime.after(planTime))
							  break;
					
				}
	       }
		}
		
		if(conflictreason==null)
		{
	       for(int i=0;i<record2.size();i++)
	       {												
		    	String recording[]=record2.get(i).getonefromrecord().split(" ");
		   		String S=map.get(recording[1])+" "+recording[2];
				Date nowTime = new SimpleDateFormat("dd HH:mm").parse(S);
				Calendar cal = Calendar.getInstance();
				cal.setTime(nowTime);
				cal.add(Calendar.HOUR, -1);
				Date startTime=cal.getTime();
				cal.setTime(nowTime);
				cal.add(Calendar.HOUR, 1);
				Date endTime =cal.getTime();
				Date planTime = new SimpleDateFormat("dd HH:mm").parse(map.get(f.getarday().substring(0, 3))+" "+f.getartime());
				if(isEffectiveDate(planTime, startTime, endTime))
				{
					
					//System.out.println(planTime);
					//System.out.println(startTime);
					//System.out.println(endTime);
					
					String s="Scheduling conflict! This flight clashes with Flight "+recording[0]+" arriving at "+L2.getlocation()+" on "+fulldatename.get(recording[1])+" "+recording[2]+".";
					conflictreason=s;
			           if(nowTime.after(planTime))
							  break;
					
				}
	       }
		}
		return conflictreason;

    }
	 public  boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) 
		{
			if (nowTime.getTime() == startTime.getTime()
			|| nowTime.getTime() == endTime.getTime()) {
			return false;
			}

			Calendar date = Calendar.getInstance();
			date.setTime(nowTime);

			Calendar begin = Calendar.getInstance();
			begin.setTime(startTime);

			Calendar end = Calendar.getInstance();
			end.setTime(endTime);

			if (date.after(begin) && date.before(end)) 
			{
			return true;
			} 
			else 
			{
			return false;
			}
		}
	private static Double toRad(Double value) 
		 {
			 return value * Math.PI / 180;
		 }
	public void showlocations()
	{
		
		FlightScheduler.getInstance().getLocations();
	}
}
