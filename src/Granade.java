import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Granade extends Item{
    private int damage=100;
    private int radius = 120;
    private int fuseTicks = 45;
    private int timer = 0;
    private boolean thrown = false;
    private boolean armed = false; 
    private boolean exploded = false;

    private double vx =0;
    private double vy =0;
    private double gravity=0.8;
    private int prevX;
    private int prevY;

    private int explosionDuration = 30;
    private int explosionTimer=0;
    // private int explodedMaxRadius=160;

    private double angle = 0.0;
    private double angularVelocity = 0.0;
    public Granade(){
        super();
        this.width =32;
        this.height =32;
        this.active =false;
    }

    public Granade(int x,int y,int w,int h){
        super(x,y,w,h);
        this.width=w;
        this.height=h;
        this.active = true;
        this.thrown=false;
        this.exploded=false;
        this.prevX=this.x;
        this.prevY=this.y;
        this.angle = 0.0;
        this.angularVelocity = 0.0;
    }
    @Override
    public void onPickup(Player p){
        if(p==null){
            return;
        }
        p.addItem(this);
    }
    public void use(int startX,int startY,int targetX,int targetY){
        this.thrown=true;
        this.armed = true;
        this.exploded = false;
        this.active = true;
        this.timer=fuseTicks;
        this.x=startX-this.width/2;
        this.y=startY-this.height/2;

        double dx = targetX-startX;
        double dy = targetY-startY;

        this.vx = dx/15.0;
        this.vy = (dy/15.0)-6.0;
        this.angularVelocity = (this.vx >= 0) ? 0.35 : -0.35;
    }
    public void tick(java.util.List<Zombie>zombies){
       prevX=x;
       prevY=y;
       if(thrown&&!exploded){
            x+=(int)Math.round(vx);
            y+=(int)Math.round(vy);
            vy+=gravity;
            angle += angularVelocity;
       }
       if(armed && !exploded){
            if(timer>0){
                timer--;
                if(timer<=0){
                    explode(zombies);
                }
            }
       }
       if(exploded){
            if(explosionTimer>0){
            explosionTimer--;
            if(explosionTimer<=0){
                this.active=false;
            }
        }
       }
    }
    private void explode(java.util.List<Zombie> zombies){
        if(exploded){
            return;
        }
        exploded = true;
        this.active = true;
        this.explosionTimer=explosionDuration;
        System.out.println("Granade.explode at: "+(x+width/2)+","+(y+height/2)+" radius="+radius+" timer="+timer);
        if(zombies==null||zombies.isEmpty()){
            System.out.println("->no zombies list or empty");
            return;
        }
        int cx = this.x+this.width/2;
        int cy = this.y+this.height/2;

        for(Zombie z:zombies){
            if(z==null){
                continue;
            }
            int zx = z.getX()+z.getBounds().width/2;
            int zy = z.getY()+z.getBounds().height/2;
            double dist = Math.hypot(zx-cx, zy-cy);
            System.out.println(" check zombie "+z.getClass().getSimpleName()+" at "+zx+","+zy+" dist="+String.format("%.1f",dist)+" hpBefore="+z.getHP());
            if(dist<=radius){
                z.takeDamage(damage);
                System.out.println(" -> hit! damage="+damage+" hpAfter="+z.getHP());
            }
            else{
            System.out.println(" ->out of range");
            }
        }
    }
    public boolean isExploded(){
        return exploded;
    }
    public void landOnPlatform(int platformTopY){
        this.y=platformTopY-this.height;
        this.vx=0;
        this.vy=0;
        this.thrown=false;
        this.angularVelocity = 0.0;
    }
    public int getPrevX(){
        return prevX;
    }
    public int getPrevY(){
        return prevY;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }
    public boolean isThrown(){
        return thrown;
    }
    public boolean isArmed(){
        return armed;
    }
    // public void render(java.awt.Graphics g){
    //     if(!active){
    //         return;
    //     }
    //     Graphics2D g2 = (Graphics2D)g;
    //     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    //     if(!exploded){
    //         Color col = new Color(80,120,30);
    //         g2.setColor(col);
    //         g2.fillOval(x, y, width, height);
    //         if(thrown){
    //             g2.setColor(Color.WHITE);
    //             g2.drawString(String.valueOf(Math.max(0,timer)),x+width/2-4,y-2);
    //         }
    //     }
    //     else{
    //         int cx = x+width/2;
    //         int cy = y+height/2;
    //         double prog = 1.0-(explosionTimer/(double)Math.max(1,explosionDuration));
    //         prog=Math.max(0.0,Math.min(1.0,prog));
    //         int visR = (int)(radius*(0.3+0.7*prog));
    //         float alpha = (float)(1.0-prog);
    //         alpha = Math.max(0f,Math.min(1f,alpha));

    //         g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Math.min(0.9f,alpha*1.0f)));
    //         g2.setColor(new Color(255,200,60));
    //         g2.fillOval(cx-visR,cy-visR,visR*2,visR*2);

    //         g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Math.min(0.6f,alpha*0.7f)));
    //         g2.setColor(new Color(255,140,0));
    //         g2.fillOval(cx-(int)(visR*1.4),cy-(int)(visR*1.4),(int)(visR*2.8),(int)(visR*2.8));

    //         g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Math.min(0.8f,alpha)));
    //         g2.setColor(new Color(255,220,140,(int)(alpha*255)));
    //         g2.setStroke(new java.awt.BasicStroke(3));
    //         g2.drawOval(cx-visR,cy-visR,visR*2,visR*2);

    //         g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
    //     }
    // }
    public void render(java.awt.Graphics g, BufferedImage sprite){
        if(!active){
            return;
        }
       Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

       if(!exploded){           
        int cx = x + width/2;
          int cy = y + height/2;
           g2.translate(cx, cy);
           g2.rotate(angle);
            if(sprite != null){
               g2.drawImage(sprite, -width/2, -height/2, width, height, null);
           } else {
                Color col = new Color(80,120,30);
                g2.setColor(col);
                g2.fillOval(-width/2, -height/2, width, height);
            }
            // draw fuse timer over rotated context (optional)
            if(armed){
                g2.setColor(Color.WHITE);
                g2.drawString(String.valueOf(Math.max(0,timer)), -4, -height/2 - 4);
            }
            // restore transform
            g2.rotate(-angle);
            g2.translate(-cx, -cy);
        }
        else{
            int cx = x+width/2;
           int cy = y+height/2;
            double prog = 1.0-(explosionTimer/(double)Math.max(1,explosionDuration));
            prog=Math.max(0.0,Math.min(1.0,prog));
            int visR = (int)(radius*(0.3+0.7*prog));
            float alpha = (float)(1.0-prog);
            alpha = Math.max(0f,Math.min(1f,alpha));

          g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Math.min(0.9f,alpha*1.0f)));
           g2.setColor(new Color(255,200,60));
           g2.fillOval(cx-visR,cy-visR,visR*2,visR*2);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Math.min(0.6f,alpha*0.7f)));
            g2.setColor(new Color(255,140,0));
            g2.fillOval(cx-(int)(visR*1.4),cy-(int)(visR*1.4),(int)(visR*2.8),(int)(visR*2.8));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Math.min(0.8f,alpha)));
            g2.setColor(new Color(255,220,140,(int)(alpha*255)));
            g2.setStroke(new java.awt.BasicStroke(3));
            g2.drawOval(cx-visR,cy-visR,visR*2,visR*2);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
        }
    }
}
