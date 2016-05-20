package RsoAggregator;

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
}
