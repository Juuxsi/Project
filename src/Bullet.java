import java.awt.*;

public class Bullet{
    private int x;
    private int y;
    private int w;
    private int h;
    private int vx;
    private int vy;
    private int damage;
    private boolean active=true;
    public Bullet(int x,int y,int w,int h,int vx,int vy,int damage){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.vx=vx;
        this.vy=vy;
        this.damage=damage;
    }
    public void update(){
       x+=vx;
       y+=vy;
       if(x+w<-100||x>2000||y+h<-100||y>2000){
            active=false;
        }
    }
    public void draw(Graphics g){
        if(!active){
            return;
        }
        Color old =g.getColor();
        g.setColor(Color.YELLOW);
        g.fillRect(x,y,w,h);
        g.setColor(old);
    }
    public boolean isActive(){
        return active;
    }
    public boolean isOffscreen(){
        return !active;
    }
    public Rectangle getBounds(){
        return new Rectangle(x,y,w,h);
    }
    public int dodamage(){
        return damage;
    }
    public void setActive(boolean a){
        this.active=a;
    }
}