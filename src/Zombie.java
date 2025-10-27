import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;
import java.awt.Rectangle;
public abstract class Zombie {
    int HP;
    int speed;
    int damage;
    protected int x;
    protected int y;
    protected int spriteW=48;
    protected int spriteH=48;
    public Zombie(){

    }
    public Zombie(int startX,int startY){
        this.x=startX;
        this.y=startY;
    }
    public Rectangle getBounds(){
        return new Rectangle(x,y,spriteW,spriteH);
    }
    public int getDamage(){
        return damage;
    }
    public void takeDamage(int amount){
        this.HP = Math.max(0, this.HP - amount);
    }
    public int getHP(){
        return HP;
    }
    public Item createDrop(Random rnd,int ix,int iy,int iw,int ih){
        return null;
    }
    protected void walkLeft(){
        x-=speed;
    }
    abstract void move();
    public void draw(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(x, y, spriteW, spriteH);
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setPosition(int nx,int ny){
        x=nx;
        y=ny;
    }
    protected int feetAdjust=0;
    public int getFeetAdjust(){
        return feetAdjust;
    }
    public void setFeetAdjust(int v){
        feetAdjust = v;
    }
    
}
class n_Zombie extends Zombie{
    private static BufferedImage n_zombiepng;
    static{
        try{
            n_zombiepng = ImageIO.read(n_Zombie.class.getResource("/image/n_zom/n_z.png"));
        }catch(IOException|IllegalArgumentException e){
            n_zombiepng = null;
            System.err.println("Failed to load n_zombie image: "+e.getMessage());
        }
    }
    public n_Zombie(){
        super();
        this.HP=10;
        this.speed=3;
        this.damage=1;
        if(n_zombiepng!=null){
            int imgW = n_zombiepng.getWidth();
            int imgH = n_zombiepng.getHeight();
            double scale = Math.min(1.0, (double)100 / imgW);
            this.spriteW = (int)Math.max(16, Math.round(imgW * scale));
            this.spriteH = (int)Math.max(16, Math.round(imgH * scale));
        } else {
            this.spriteW = 64;
            this.spriteH = 64;
        }
    }
    @Override
    void move(){
        walkLeft();
    }
    // public Item createDrop(Random rnd,int ix,int iy,int iw,int ih){
    //     int pick = rnd.nextInt(6);
    //     switch (pick) {
    //         case 0: return new Barbebwire(ix,iy,iw,ih);
    //         case 1: return new FirstAid(ix,iy,iw,ih);
    //         case 2: return new DesertEagle(ix,iy,iw,ih);
    //         case 3: return new AK47(ix,iy,iw,ih);
    //         case 4: return new Uzi(ix,iy,iw,ih);
    //         default: return new Granade(ix,iy,iw,ih);
    //     }
    // }
    @Override
    public void draw(Graphics g){
        if(n_zombiepng != null){
            g.drawImage(n_zombiepng,x,y,spriteW,spriteH,null);
        }else{
           super.draw(g);
        }
    }
}
class r_Zombie extends Zombie{
     private static BufferedImage r_zombiepng;
     static{
        try{
            r_zombiepng = ImageIO.read(r_Zombie.class.getResource("/image/r_zom/r_z.png"));
        }catch(IOException|IllegalArgumentException e){
            r_zombiepng = null;
            System.err.println("Failed to load r_zombie image: "+e.getMessage());
        }
    }
    public r_Zombie(){
        super();
        this.HP=6;
        this.speed=4;
        this.damage=1;
        if(r_zombiepng!=null){
           int imgW = r_zombiepng.getWidth();
            int imgH = r_zombiepng.getHeight();
            double scale = Math.min(1.0, (double)100 / imgW);
            this.spriteW = (int)Math.max(16, Math.round(imgW * scale));
            this.spriteH = (int)Math.max(16, Math.round(imgH * scale));
        } else {
            this.spriteW = 64;
            this.spriteH = 64;
        }
    }
    @Override
    void move(){
        walkLeft();
    }
    @Override
    public void draw(Graphics g){
        if(r_zombiepng != null){
            
            g.drawImage(r_zombiepng,x,y,-spriteW,spriteH,null);
        }else{
           super.draw(g);
        }
    }
}
class d_Zombie extends Zombie{
    private static BufferedImage d_zombiepng;
    static{
        try{
            d_zombiepng = ImageIO.read(b_Zombie.class.getResource("/image/d_zom/d_zombie.png"));
        }catch(IOException|IllegalArgumentException e){
            d_zombiepng = null;
            System.err.println("Failed to load d_zombie image: "+e.getMessage());
        }
    }
    public d_Zombie(){
        super();
        this.HP=20;
        this.speed=2;
        this.damage=1;
        if(d_zombiepng!=null){
            int imgW = d_zombiepng.getWidth();
            int imgH = d_zombiepng.getHeight();

            double scale = Math.min(1.0,(double)100/imgW);
            this.spriteW=(int)Math.max(16,Math.round(imgW*scale));
            this.spriteH=(int)Math.max(16,Math.round(imgH*scale));
        }
        else{
            this.spriteW=64;
            this.spriteH=64;
        }
        this.setFeetAdjust(6);
    }
    @Override
    void move(){
        walkLeft();
    }
    @Override
    public void draw(Graphics g){
        if(d_zombiepng!=null){
            g.drawImage(d_zombiepng, x, y,spriteW,spriteH, null);
        }
        else{
            super.draw(g);
        }
    }
}
class b_Zombie extends Zombie{
    private static BufferedImage b_zombiepng;
    static{
        try{
            b_zombiepng = ImageIO.read(b_Zombie.class.getResource("image/b_zom/b_zombie.gif"));
        }catch(IOException|IllegalArgumentException e){
            b_zombiepng = null;
            System.err.println("Failed to load b_zombie image: "+e.getMessage());
        }
    }
    public b_Zombie(){
        super();
        this.HP = 30;
        this.speed = 1;
        this.damage = 3; 
        if(b_zombiepng!=null){
            int imgW = b_zombiepng.getWidth();
            int imgH = b_zombiepng.getHeight();
            
            int targetWidth = 200;
            double scaleMultiplier=(double)targetWidth/(double)imgW;
            this.spriteW = (int)Math.max(16,Math.round(imgW*scaleMultiplier));
            this.spriteH = (int)Math.max(16,Math.round(imgH*scaleMultiplier));
        }
        else{
            this.spriteW=120;
            this.spriteH=120;
        }
        this.setFeetAdjust(18);
    }
    @Override
    void move(){
        walkLeft();
    }
    public Item createDrop(Random rnd,int ix,int iy,int iw,int ih){
        int pick = rnd.nextInt(6);
        switch (pick) {
            case 0: return new Barbebwire(ix,iy,iw,ih);
            case 1: return new FirstAid(ix,iy,iw,ih);
            case 2: return new DesertEagle(ix,iy,iw,ih);
            case 3: return new AK47(ix,iy,iw,ih);
            case 4: return new Uzi(ix,iy,iw,ih);
            default: return new Granade(ix,iy,iw,ih);
        }
    }
    @Override
    public void draw(Graphics g){
        if(b_zombiepng !=null){
            g.drawImage(b_zombiepng, x, y,spriteW,spriteH,null);
        }
        else{
            super.draw(g);
        }
    }
}


