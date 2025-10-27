public class Mode{
    protected final int TOTAL_DAYS;
    protected int day=1;
    protected DaySpawn[] schedule;

    public Mode(int totalDays, DaySpawn[] schedule){
        this.TOTAL_DAYS=totalDays;
       this.schedule=schedule!=null?schedule:new DaySpawn[0];
    }
    public DaySpawn getSpawnsForDay(int d){
        if(d<1){
            d=1;
        }
        if(d>TOTAL_DAYS){
            d=TOTAL_DAYS;
        }
        return schedule[d-1];
    }
    public DaySpawn getSpawnsForCurrentDay(){
        int idx=Math.max(0,Math.min(schedule.length-1,day-1));
        if(schedule.length==0){
            return new DaySpawn(0,0,0,0);
        }
        return schedule[idx]!=null?schedule[idx]:new DaySpawn(0,0,0,0);
    }
    public boolean advanceDay(){
        if(day<TOTAL_DAYS){
            day++;
            return true;
        }
        return false;
    }
    public int getDay(){
        return day;
    }

    public static class DaySpawn{
        public int n_zombie;
        public int r_zombie;
        public int d_zombie;
        public int b_zombie;
        public DaySpawn(int normal,int run,int durability,int boss){
            this.n_zombie=normal;
            this.r_zombie=run;
            this.d_zombie=durability;
            this.b_zombie=boss;
        }
        @Override
        public String toString(){
            return "n="+n_zombie+" r="+r_zombie+" d="+d_zombie+" b="+b_zombie;
        }
    }
    public static class easy extends Mode{
         public easy(){
            super(7,new DaySpawn[]{
                new DaySpawn(5,0,0,0),
                new DaySpawn(8, 1, 1,0),
                new DaySpawn(10, 2, 2,1),
                new DaySpawn(12, 4, 5,0),
                new DaySpawn(15, 7, 10,2),
                new DaySpawn(20, 10, 15,0),
                new DaySpawn(25, 15, 10,3)
            });
         }
    }
    public static class normal extends Mode{
        public normal(){
            super(7,new DaySpawn[]{
                new DaySpawn(10,0,0,0),
                new DaySpawn(15, 2, 1,0),
                new DaySpawn(20, 4, 3,1),
                new DaySpawn(25, 6, 5,0),
                new DaySpawn(30, 10, 10,2),
                new DaySpawn(35, 15, 15,0),
                new DaySpawn(40, 20, 20,3)
            });
        }
    }
    public static class hard extends Mode{
        public hard(){
            super(7,new DaySpawn[]{
                new DaySpawn(15,0,0,0),
                new DaySpawn(20, 3, 1,0),
                new DaySpawn(30, 6, 3,1),
                new DaySpawn(40, 10, 5,0),
                new DaySpawn(50, 15, 10,2),
                new DaySpawn(60, 20, 15,0),
                new DaySpawn(70, 25, 20,3)
            });
        }
    }
}
