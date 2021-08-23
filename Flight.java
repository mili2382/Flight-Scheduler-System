
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class Flight {

	private int flightID=-1;//��ʼ��������Ϊ-1
	private int index;
	private int index2;
	private String formatofdeparturetime;
	private String formatofarrivaltime;
	private String deday;//������� ���ڼ�
	private String detime;//���ʱ�� ���㼸��
	private String arday;//�������� ���ڼ�
	private String artime;//����ʱ�� ���㼸��
	private String source;//��ɵص�
	private String destination;//����ص�
	private int capacity;//�ɻ�����ؿ���
	private double ticketprice;//��Ʊ�۸�
	private double currentprice;//���ڻ�Ʊ�ļ۸�
	private int bookednumber=0;//��Ʊ������������
	private double distance;//���о���
    private double speed=720;//����ʱ��
    private String duration;//����ʱ��
    private double Dfrom;//demand coefficient for starting location 
    private double Dto;// demand coefficient for destination location
    private int minitueduration;
    

    private String[] week= {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday","Monday"};//8����ܱ���������⵽�����쳬������߽�
    
	//������1
    public Flight(String deday,String detime,String source,String destination,int capacity)
	{
    	//�޸�departure day Ϊ��׼������
    	deday=deday.toLowerCase();
    	deday=deday.substring(0, 1).toUpperCase()+ deday.substring(1);
    	
		this.deday=deday;
		this.detime=detime;
		this.source=source;
		this.destination=destination;
		this.capacity=capacity;
	}
    //������2
	public Flight(String deday,String detime,String source,String destination,int capacity,int bookednumber)
	{
    	//�޸�departure day Ϊ��׼������
    	deday=deday.toLowerCase();
    	deday=deday.substring(0, 1).toUpperCase()+ deday.substring(1);
    	
		this.deday=deday;
		this.detime=detime;
		this.source=source;
		this.destination=destination;
		this.capacity=capacity;
		this.bookednumber=bookednumber;
		
	}
     public String setparamter() throws ParseException
	{
    	
    	 ArrayList<Flight>f=FlightScheduler.getInstance().getFlights();

    	 flightID=FlightScheduler.getInstance().getfid();
    	 Location L = FlightScheduler.getInstance().getLocations().get(source.toLowerCase());//���ܴ���Ĳ���
         Location L2 = FlightScheduler.getInstance().getLocations().get(destination.toLowerCase());
         if(L!=null&&L2!=null)
         {
        	 
         this.Dfrom=L.getcoefficient();
         this.Dto=L2.getcoefficient();
		//������ɵ�ͽ����ľ�γ�ȼ������
          distance=Location.distance(L, L2);
          
		 int hour=new Double(Math.floor(distance/speed)).intValue();//���ݷ��о�������ʱ���Сʱλ
		 int min=new Double(Math.round(((distance%speed)/speed)*60)).intValue();//���ݷ��о�������ʱ��ķ���λ

		 this.duration=hour+"h "+min+"m";//�ַ���ƴ�ӳ���������ʱ���ַ���
	     this.minitueduration=hour*60+min;
		 //�õ����ڵ�ǰ������ĸ
		  String formatdeday=deday.substring(0, 3);
		  
		  

		 int indexofweekarray=-1;//��ʼ�������ݵ�ָ��Ϊ-1
		 for(int i=0;i<week.length;i++)
		 {
			 if(deday.equals(week[i]))
			 {
				 index=i;
				 index2=i;
				 indexofweekarray=i;
				 break;
			 }
		 }
		 String[] spdetime=detime.split(":");
		 int detimehour=Integer.parseInt(spdetime[0]);//��ȡ�����ʱ���Сʱ
		 int detimemin=Integer.parseInt(spdetime[1]);//��ȡ�����ʱ��ķ���
	    
		 int artimehour=(int) (detimehour+hour+Math.floor(((detimemin+min)/60)));
		 int artimemin=(detimemin+min)%60;
		 if(artimehour>=24)
		 {
		  artimehour=artimehour%24;
		  indexofweekarray++;
		  index2=indexofweekarray;
		 }
		 arday=week[indexofweekarray];
		 if(index==6&&index2==7)//���ǡ���������쵽����һ ��ô����Ҫ�����ڻص�
			 index2=0;
		 
		 Date nowTime = new SimpleDateFormat("HH:mm").parse(detime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(nowTime);
			detime = cal.getTime().toString().substring(11, 16);
			
			cal.add(Calendar.MINUTE, minitueduration);
			
			artime=cal.getTime().toString().substring(11, 16);

		    
		 //�õ����ڵ�ǰ������ĸ
	      String formatarday=arday.substring(0, 3);
	      
	      formatofdeparturetime=formatdeday+" "+detime;
		  formatofarrivaltime=formatarday+" "+artime;
		   ticketprice=(distance/100)*(30+4*(Dto-Dfrom));
		   
			double X=Double.valueOf(bookednumber)/Double.valueOf(capacity);//proportion of seats filled (booked/capacity)
			if(X>=0&&X<=0.5)
				currentprice=(1-0.4*X)*ticketprice;
			else if(X>0.5&&X<=0.7)
				currentprice=(X+0.3)*ticketprice;
			else if(X>0.7&&X<=1)
				currentprice=((0.2/Math.PI)*Math.atan(20*X-14)+1)*ticketprice;
		
			
		String reason1=L.hasRunwayDepartureSpace(this);
		String reason2=L2.hasRunwayArrivalSpace(this);
		
		
		
		//���ȷ��û�г�ͻ,��������Ϣ���뵽��Ӧ���е�String List����
		if(reason1==null&&reason2==null)
		{

		       L.addDeparture(this);
		       L2.addArrival(this);
		}
		
		//���س�ͻԭ��  ͬʱ����ͻ �������һ����ͻ
		 if(reason1==null)
	        return reason2;
		 else
			return reason1;
         }
         else 
        	 return null;
         
         
	}
	
	
    //get the number of minutes this flight takes (round to nearest whole number)
    public int getDuration() 
    {
    	
		return this.minitueduration;

    }

    //implement the ticket price formula
    public double getTicketPrice() 
    {
		return this.currentprice;

    }
    public void setcurrentprice()
    {
		double X=Double.valueOf(bookednumber)/Double.valueOf(capacity);//proportion of seats filled (booked/capacity)
		if(X>=0&&X<=0.5)
			this.currentprice=(1-0.4*X)*ticketprice;
		else if(X>0.5&&X<=0.7)
			this.currentprice=(X+0.3)*ticketprice;
		else if(X>0.7&&X<=1)
			this.currentprice=((0.2/Math.PI)*Math.atan(20*X-14)+1)*ticketprice;
    }

    //book the given number of passengers onto this flight, returning the total cost
    public double book(int num) 
    {
    	double totalcost=0;
    	double nowprice=0;
    	//���ɻ�û��Ա���Ҷ���Ʊ֮��Ҳû������ʱ��
    	if(bookednumber+num<=capacity)
    	{
    	for(int i=0;i<num;i++)
    	{
    		
			double X=Double.valueOf(bookednumber)/Double.valueOf(capacity);//proportion of seats filled (booked/capacity)
			if(X>=0&&X<=0.5)
				nowprice=(1-0.4*X)*ticketprice;
			else if(X>0.5&&X<=0.7)
				nowprice=(X+0.3)*ticketprice;
			else if(X>0.7&&X<=1)
				nowprice=((0.2/Math.PI)*Math.atan(20*X-14)+1)*ticketprice;
			
			this.bookednumber++; //�����Ļ�Ʊ������1
			
		    totalcost+=nowprice;
		
    	}
    	setcurrentprice();
    	}
    	//���ɻ�û��Ա���Ƕ���Ʊ����֮��
    	else if(bookednumber<capacity&&bookednumber+num>capacity)
    	{
    		
    		int maxnumber=capacity-bookednumber;//����ܶ��Ļ�Ʊ����
    		for(int i=0;i<maxnumber;i++)
    		{
    			double X=Double.valueOf(bookednumber)/Double.valueOf(capacity);//proportion of seats filled (booked/capacity)
    			if(X>=0&&X<=0.5)
    				nowprice=(1-0.4*X)*ticketprice;
    			else if(X>0.5&&X<=0.7)
    				nowprice=(X+0.3)*ticketprice;
    			else if(X>0.7&&X<=1)
    				nowprice=((0.2/Math.PI)*Math.atan(20*X-14)+1)*ticketprice;
    			
    			this.bookednumber++; //�����Ļ�Ʊ������1
    			this.currentprice=nowprice;
    		    totalcost+=nowprice;
    		}
    		setcurrentprice();
    	}
    	DecimalFormat df = new DecimalFormat("#.00");
    	return totalcost;
    }

    //return whether or not this flight is full
    public boolean isFull() 
    {
	    if(bookednumber<capacity)
	    	return false;
	    else
	    	return true;
		
	}

    //get the distance of this flight in km
    public double getDistance() 
    {
		return this.distance;
		
	}

    //get the layover time, in minutes, between two flights
    public static int layover(Flight x, Flight y) 
    {
		return 0;

    }
    
    public String getonetorecord()
    {
    	String torecord=this.getflightID()+" "+this.getformatofdeparturetime()+"   "+"Departure to "+this.getdestination();
 	   return torecord;
    }
    
    public String getonefromrecord()
    {
    	 String fromrecord=this.getflightID()+" "+this.getformatofarrivaltime()+"   "+"Arrival from "+this.getsource();
 	   return fromrecord;
    }
    
    public int getindex()
    {
    	return index;
    }
    
    public int getindex2()
    {
    	
    	return index2;
    }
    
    public String getdeday()
    {
    	return deday;
    }
    
    public String getarday()
    {
    	return arday;
    }

    
    public String getdetime()
    {
    	return detime;
    }
    public String getartime()
    {
    	return artime;
    }
    
    public String getsource()
    {
    	return source;
    }
    
    public String getdestination()
    {
    	return destination;
    }
    public int getcapacity()
    {
    	return capacity;
    }
    
    public int getbook()
    {
    	return bookednumber;
    }
    
    public void resetbook()
    {
    	this.bookednumber=0;
    	setcurrentprice();
    }
    
	public int getflightID() 
	{
	   return flightID;
	}
	
	public String getformatofdeparturetime()
	{
		return formatofdeparturetime;
	}
	
	public String getformatofarrivaltime()
	{
		return formatofarrivaltime;
	}
	public void flight() {
		
		System.out.println("Flight "+flightID);
        System.out.println("Departure:    "+formatofdeparturetime+" "+FlightScheduler.getInstance().getLocations().get(source.toLowerCase()).getlocation());
        System.out.println("Arrival:      "+formatofarrivaltime+" "+FlightScheduler.getInstance().getLocations().get(destination.toLowerCase()).getlocation());
        System.out.printf("Distance:"+"%,10d",Math.round(distance));
        System.out.println("km");
        System.out.println("Duration:     "+duration);
        System.out.println("Ticket Cost:  $"+String.format("%.2f", getTicketPrice()));
        System.out.println("Passengers:   "+bookednumber+"/"+capacity);
        System.out.println();
	}
	public void flights() {
		 String s= FlightScheduler.getInstance().getLocations().get(source.toLowerCase()).getlocation();
		 String d= FlightScheduler.getInstance().getLocations().get(destination.toLowerCase()).getlocation();
		System.out.println("   "+flightID+" "+formatofdeparturetime+"   "+formatofarrivaltime+"   "+s+" --> "+d);
		
	}
	
}
