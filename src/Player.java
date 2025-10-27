import java.awt.*;
// import java.awt.event.KeyEvent;
// import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
public class Player{
    int heart = 3;
    String[] item =new String[5];
    private int x;
    private int y;
    private int speed = 6;
    private BufferedImage playerImage;
    private int maxWidth=800;
    private boolean leftPressed=false;
    private boolean rightPressed=false;
    private int spriteWidth=64;
    private int spriteHeight=64;

    private Gun currentGun=new Glock();
    private final List<Bullet>bullets = new ArrayList<>();
    private final java.util.List<InventorySlot> inventory = new java.util.ArrayList<>();
    private Gun equippedGun = null;

    public static class InventorySlot{
        private final Item item;
        private int count;
        public InventorySlot(Item item,int count){
            this.item = item;
            this.count = Math.max(1,count);
        }
        public Item getItem(){
            return item;
        }
        public int getCount(){
            return count;
        }
        public void increment(){
            count++;
        }
        public void decrement(){
            if(count>0){
                count--;
            }
        }
    }

    public boolean firePressed=false;
    public Player(int startX, int startY){
       this(startX, startY, 64, 64);
    }
    public Player(int startX, int startY,int width,int height){
        this.x = startX;
        this.y = startY;
        this.spriteWidth=width;
        this.spriteHeight=height;
        try{
            playerImage = ImageIO.read(getClass().getResource("/image/player/playerGlock.png"));
        }catch(IOException|IllegalArgumentException e){
            playerImage = null;
            System.err.println("Failed to load player image: "+e.getMessage());
        }
        updatePlayerImageForGun();
    }
    private void updatePlayerImageForGun(){
        BufferedImage img = null;
        if(currentGun!=null){
            String cls = currentGun.getClass().getSimpleName().toLowerCase();
            String path = "/image/player/player"+cls+".png";
            try{
                img=ImageIO.read(getClass().getResource(path));
            }catch(Exception ex){
                img=null;
            }
        }
        if(img == null){
            try{
                img=ImageIO.read(getClass().getResource("/image/player.png"));
            }catch(Exception e){
                img=null;
            }
        }
        playerImage=img;
    }
    public void setSpriteSize(int w,int h){
        this.spriteWidth=w;
        this.spriteHeight=h;
    }
    public void setMaxWidth(int w){
        this.maxWidth=Math.max(0,w-this.spriteWidth);
    }
    public void setLeftPressed(boolean pressed){
        this.leftPressed=pressed;
    }
    public void setRightPressed(boolean pressed){
        this.rightPressed=pressed;
    }
    public void setFirePressed(boolean pressed){
        this.firePressed=pressed;
    }
    public void reloadGun(){
        currentGun.reload();
    }
    public List<Bullet>getBullets(){
        return bullets;
    }
    public void tick(){
        if(leftPressed && !rightPressed){
            moveLeft();
        }
        if(rightPressed && !leftPressed){
            moveRight();
        }
        currentGun.tick();
        if(firePressed&&currentGun.canfire()){
            int dmg =  currentGun.use();
            if(dmg>0){
                int bw = 24;
                int bh = 12;
                int bx = x+spriteWidth;
                int by = y+spriteHeight/2 - bh/2;
                int vx = 12;
                int vy = 0;
                bullets.add(new Bullet(bx,by,bw,bh,vx,vy,dmg));
            }
        }
        for(int i = bullets.size()-1;i>=0;i--){
            Bullet b = bullets.get(i);
            b.update();
            if(b.isOffscreen()||!b.isActive()){
                bullets.remove(i);
            }
        }
    }
    public void moveLeft(){
        x -= speed;
        if(x<0){
            x=0;
        }
    }
    public void moveRight(){
        // int w =(playerImage!=null)? playerImage.getWidth():32;
        x += speed;
        if(x>maxWidth){
            x = maxWidth;
        }
    }
    public int getSpriteWidth(){
        return spriteWidth;
    }
    public int getSpriteHeight(){
        return spriteHeight;
    }
    public void setPosition(int nx,int ny){
        x=nx;
        y=ny;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getCenterX(){
        return x+spriteWidth/2;
    }
    public int getCenterY(){
        return y+ spriteHeight/2;
    }
    public int getMagazine(){
        return (currentGun != null)?currentGun.getMagazine():0;
    }
    public int getMaxMagazine(){
        return (currentGun != null)?currentGun.getMaxMagazine():0;  
    }
    public Rectangle getBounds(){
        return new Rectangle(x,y,spriteWidth,spriteHeight);
    }
    public boolean isReloading(){
        return currentGun!=null && currentGun.isReloading();
    }
    public int getReloadTimer(){
        return (currentGun != null)?currentGun.getReloadTimer():0;
    }
    public int getReloadTime(){
        return (currentGun != null)?currentGun.getReloadTime():1;
    }
    public void takeDamage(int amount){
        heart -= amount;
        if(heart<0){
            heart=0;
        }
    }
    public int getHeart(){
        return heart;
    }
    public void addItem(Item it){
        if(it==null){
            return;
        }
        if(it instanceof Gun){
            for(Player.InventorySlot slot:inventory){
                if(slot.getItem().getClass().equals(it.getClass())){
                    System.out.println("Already heave Gun: "+it.getClass().getSimpleName());
                    equipGun((Gun)slot.getItem());
                    return;
                }
            }
            int gunIndex = -1;
            for(int i=0;i<inventory.size();i++){
                if(inventory.get(i).getItem()instanceof Gun){
                    gunIndex=i;
                    break;
                }
            }
            InventorySlot newSlot = new InventorySlot(it,1);
            if(gunIndex>=0){
                inventory.set(gunIndex,newSlot);
            }
            else{
                inventory.add(newSlot);
            }
            equipGun((Gun)it);
            System.out.println("Pick up gun: "+it.getClass().getSimpleName());
            return;
        }
        for(InventorySlot slot:inventory){
            if(slot.getItem().getClass().equals(it.getClass())){
                slot.increment();
                System.out.println("Pick up "+it.getClass().getSimpleName()+" x"+slot.getCount());
                return;
            }
        }
        InventorySlot newSlot = new InventorySlot(it,1);
        inventory.add(newSlot);
        System.out.println("Picked up: "+it.getClass().getSimpleName()+" x1");
        // for(InventorySlot slot:inventory){
        //     if(slot.getItem().getClass().equals(it.getClass())){
        //         if(it instanceof Gun){
        //             System.out.println("Already have gun: "+it.getClass().getSimpleName());
        //             equipGun((Gun)it);
        //             return;
        //         }
        //         else{
        //             slot.increment();
        //             System.out.println("Pick up "+it.getClass().getSimpleName()+" x"+slot.getCount());
        //             return;
        //         }
        //     }
        // }
        // InventorySlot newSlot = new InventorySlot(it, 1);
        // inventory.add(newSlot);
        // if(it instanceof Gun){
        //     equipGun((Gun)it);
        //     System.out.println("Pick up gun: "+it.getClass().getSimpleName());
        // }
        // else{
        //     System.out.println("Picked up: "+it.getClass().getSimpleName()+" x1");
        // }
    }

    public void equipGun(Gun g){
        if(g==null){
            return;
        }
        this.currentGun=g;
        this.equippedGun=g;
        System.out.println("equipGun: now currentGun -> "+g.getClass().getName()
            +" dmg="+g.damage);
        updatePlayerImageForGun();
    }
    
    public java.util.List<InventorySlot> getInventory(){
        return inventory;
    }
    public void draw(Graphics g){
        if(playerImage != null){
            g.drawImage(playerImage,x,y,spriteWidth,spriteHeight,null);
        }else{
            g.setColor(Color.MAGENTA);
            g.fillRect(x,y,spriteWidth,spriteHeight);
        }
        synchronized(bullets){
            for(Bullet b:bullets){
                b.draw(g);
            }
        }
    }
}
