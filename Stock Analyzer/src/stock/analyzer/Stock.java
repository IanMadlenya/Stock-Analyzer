/*
 * Stock Class
 */

package stock.analyzer;

//imports
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.ArrayList;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Stock {
    
    //fields
    ArrayList<GregorianCalendar> dates;
    ArrayList<Double> closes;
    int stocklen;
    double stockmax;	
    double stockmin;	

    
    public Stock(String symbol, GregorianCalendar start, GregorianCalendar end) {
        
        dates = new ArrayList<GregorianCalendar>();
        closes = new ArrayList<Double>();

        String[] mos = new String[] {"Jan","Fed","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",};
        
        //accessing google finance API
        String url = "http://www.google.com/finance/historical?q=" + symbol + 
                "&startdate=" + mos[start.get(Calendar.MONTH)] + "%20" + start.get(Calendar.DAY_OF_MONTH) + "," + "%20" + start.get(Calendar.YEAR) +
                "&enddate=" + mos[end.get(Calendar.MONTH)] + "%20" + end.get(Calendar.DAY_OF_MONTH) + "," + "%20" + end.get(Calendar.YEAR) + 
                "&output=csv";

        try {
            
            //opening url
            URL yhoofin = new URL(url);
            URLConnection data = yhoofin.openConnection();
            Scanner input  = new Scanner(data.getInputStream());
            
            //ignoring first line for headers
            if (input.hasNext()) {
                input.nextLine();
            }
            
            //reading in data
            while (input.hasNextLine()) {
                
                String line = input.nextLine();
                
                String date = line.substring(0,line.indexOf(","));
                
                for (int i = 0; i < 4; i++) {
                    line = line.substring(line.indexOf(",") + 1,line.length());
                }
                double price = Double.parseDouble(line.substring(0,line.indexOf(",")));
                
                String date2 = date.substring(date.indexOf("-") + 1,date.length());
                String mo = date2.substring(0,date2.indexOf("-"));
                int m = 0;
                
                for (int i = 0; i < mos.length; i++) {
                    if (mos[i].equals(mo)) {
                        m = i;
                        break;
                    }
                }
                
                String date3 = date2.substring(date2.indexOf("-") + 1,date2.length());

                //we are assuming the prices will never be older than 2000
                GregorianCalendar d = new GregorianCalendar(2000 + Integer.parseInt(date3),m,Integer.parseInt(date.substring(0,date.indexOf("-"))));

                dates.add(d);
                closes.add(price);
            }
            
            stocklen = closes.size();
            
        } catch (Exception e) {}   
    }
    
    //finding min stock price
    public double findMin() {
        
        int minIndex = 0;
        for (int i = 0; i < stocklen;i++) {
            if (closes.get(i) < closes.get(minIndex)) {
                minIndex = i;
            }
        }
        return closes.get(minIndex);
    }
    
    //finding max stock price
    public double findMax() {
        
        int maxIndex = 0;
        for (int i = 0; i < stocklen;i++) {
            if (closes.get(i) > closes.get(maxIndex)) {
                maxIndex = i;
            }
        }
        return closes.get(maxIndex);
    }
}