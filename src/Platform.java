import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
public class Platform {
    public int x;
    public int y;
    public int w;
    public int h;
    public boolean visible;

    public Platform(int x,int y,int w,int h){
        this(x,y,w,h,true);
    }
    public Platform(int x,int y,int w,int h,boolean visible){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.visible = visible;
    }
    public Rectangle getBounds(){
        return new Rectangle(x,y,w,h);
    }
    public boolean intersects(Rectangle r){
        if(r==null){
            return false;
        }
        return getBounds().intersects(r);
    }
    public void draw(Graphics g){
        if(!visible){
            return;
        }
        Color c=new Color(100,100,100,160);
        g.setColor(c);
        g.fillRect(x, y, w, h);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, w-1, h-1);
    }
    public void setVisible(boolean v){
        this.visible=v;
    }
    public boolean isVisible(){
        return visible;
    }
}
