public class Gun extends Item {
    public int damage;
    public int magazine;
    public int maxMagazine;
    public int firerate;
    private int cooldown;

    public int reloadTime = 60;
    private int reloadTimer =0;
    private boolean reloading = false;
    public Gun(int damage,int maxMagazine,int firerate){
        this.damage=damage;
        this.maxMagazine=maxMagazine;
        this.magazine=maxMagazine;
        this.firerate=Math.max(0,firerate);
        this.cooldown=0;
        this.reloadTime=60;
    }
    public Gun(int damage,int maxMagazine,int firerate,int x,int y,int w,int h){
        super(x,y,w,h);
        this.damage=damage;
        this.maxMagazine=maxMagazine;
        this.magazine=maxMagazine;
        this.firerate=Math.max(0,firerate);
        this.cooldown=0;
        this.reloadTime = 60;
    }
     @Override
    public void onPickup(Player p){
        if(p==null) return;
        p.addItem(this);
        p.equipGun(this);
    }
    @Override
    public int use(){
        if(canfire()){
            magazine--;
            cooldown=firerate;
            return damage;
        }
        return 0;
    }
    public void reload(){
        if(reloading){
            return;
        }
        if(magazine >= maxMagazine){
            return;
        }
        reloading = true;
        reloadTimer=Math.max(1,reloadTime);
        cooldown=0;
    }
    @Override
    public void tick(){
        if(cooldown>0){
            cooldown--;
        }
        if(reloading){
            if(reloadTimer>0){
                reloadTimer--;
            }
            if(reloadTimer<=0){
                magazine=maxMagazine;
                reloading = false;
                reloadTimer=0;
            }
        }
    }
    public boolean canfire(){
        return magazine>0&&cooldown==0 && !reloading;
    }
    public int getCooldown(){
        return cooldown;
    }
    public int getMagazine(){
        return magazine;
    }
    public int getMaxMagazine(){
        return maxMagazine;
    }
    public boolean isReloading(){
        return reloading;
    }
    public int getReloadTimer(){
        return reloadTimer;
    }
    public int getReloadTime(){
        return reloadTime;
    }
    public void setReloadTime(int ticks){
        this.reloadTime = Math.max(1,ticks);
    }
}
class Glock extends Gun{
    Glock(){
        super(2,15,8);
        this.reloadTime = 60;
    }
    // Glock(int x,int y,int w,int h){
    //     super(2,15,8,x,y,w,h);
    //     this.reloadTime=60;ฆฆ
    // }
}
class DesertEagle extends Gun{
    DesertEagle(){
        super(5,7,15);
        this.reloadTime=90;
    }
    DesertEagle(int x,int y,int w,int h){
        super(5,7,15,x,y,w,h);
        this.reloadTime=90;
    }
}
class AK47 extends Gun{
    AK47(){
        super(4,30,6);
        this.reloadTime=80;
    }
    AK47(int x,int y,int w,int h){
        super(4,30,6,x,y,w,h);
        this.reloadTime=80;
    }
}   
class Uzi extends Gun{
    Uzi(){
        super(3,40,4);
        this.reloadTime=70;
    }
    Uzi(int x,int y,int w,int h){
        super(3,40,4,x,y,w,h);
        this.reloadTime=70;
    }
}