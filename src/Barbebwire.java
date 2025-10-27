import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Barbebwire extends Item{
    private int remainingHits=15;
    private final int slow=2;
    private boolean active=true;

    private final Map<Zombie,Integer> slowed = new HashMap<>();
    private boolean placed = false;
    private static BufferedImage useBarbeb;
    static{
        try{
            useBarbeb = ImageIO.read(Barbebwire.class.getResource("/image/Item/barbebwire/usedBarbeb.png"));
        }catch(IOException|IllegalArgumentException e){
            useBarbeb = null;
            System.err.println("Barbebwire: failed to load usedBarbebwire: "+e.getMessage());
        }
    }
    private BufferedImage placeImage = useBarbeb;
    public Barbebwire(){
        super();
    }
    public void setPlacedImage(BufferedImage img){
        this.placeImage = img;
    }
    public BufferedImage getIBufferedImage(){
        return placeImage;
    }
    public BufferedImage getPlacedImage(){
        return placeImage;
    }
    public Barbebwire(int x,int y,int w,int h){
        super(x,y,w,h);
        this.active=true;
        this.placed = false;
    }
    public void setPlaced(boolean p){
        this.placed = p;
    }
    public boolean isPlaced(){
        return placed;
    }
    @Override
    public void onPickup(Player p){
        if(this.placed){
            return;
        }
        if(p!=null){
            p.addItem(this);
        }
    }
    public synchronized void onEnter(Zombie z){
        if(!active||z==null){
            return;
        }
        if(slowed.containsKey(z)){
            return;
        }
        slowed.put(z,z.speed);
        z.speed=Math.max(1,z.speed-slow);
        remainingHits--;
        if(remainingHits<=0){
            active=false;
            for(Map.Entry<Zombie,Integer> e:slowed.entrySet()){
                Zombie zb = e.getKey();
                Integer orig = e.getValue();
                if(zb!=null && orig!=null){
                    zb.speed=orig;
                }
            }
            slowed.clear();
        }
    }
    public synchronized void onExit(Zombie z){
        if(z==null){
            return;
        }
        Integer orig = slowed.remove(z);
        if(orig!=null){
            z.speed=orig;
        }
    }
    public synchronized boolean applyto(Zombie z){
        onEnter(z);
        return active;
    }
    public synchronized boolean isActive(){
        return active;
    }
    public synchronized int getRemainingHits(){
        return remainingHits;
    }
}
    
