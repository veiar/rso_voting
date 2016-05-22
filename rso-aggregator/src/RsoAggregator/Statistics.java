package RsoAggregator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Statistics {
    private long womenAllCount;  // kobiet glosujacych w sumie
    private long menAllCount;    // mezczyzn glosujacych w sumie

    Statistics(){
        this.menAllCount = 0;
        this.womenAllCount = 0;
    }


    public void setWomenAllCount(long count) {this.womenAllCount = count; }
    public void setMenAllCount(long count) {this.menAllCount = count; }
    public long getWomenAllCount() {return this.womenAllCount; }
    public long getMenAllCount() {return this.menAllCount; }

//select age_id, age_group, split_part(age_group, '-', 1), split_part(age_group, '-', 2) from d_age ;

    public int calcAgeFromPesel(String pesel){
        Date now = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yyMMdd");
        long diff = 0;
        try{
            Date birth = df.parse(pesel.substring(0, 6));
            diff = (now.getTime() - birth.getTime())/ 1000 / 60 / 60 / 24 / 365;
            return (int)diff;
        }
        catch (Exception e){
            System.err.println(e.getClass().getName()+ e.getMessage());
        }
        return 0;
    }
}
