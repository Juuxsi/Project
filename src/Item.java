import java.awt.*;
public class  Item {
   public enum ItemType{
      WEAPON,CONSUMABLE,THROWABLE,ENVIRONMENT,OTHER
   }
   protected int x;
   protected int y;
   protected int width = 32; 
   protected int height = 32;
   protected boolean active = true;
   protected boolean solid = false;

   public Item(){

   }
   public Item(int x,int y,int w,int h){
      this.x=x;
      this.y=y;
      this.width=w;
      this.height=h;
   }
   public void tick(){

   }
   public void render(Graphics g){
      if(!active){
         return;
      }
        g.setColor(Color.GRAY);
        g.drawRect(x,y,width,height);
   }
   public Rectangle getBounds(){
      return new Rectangle(x,y,width,height);
   }
   public int use(){
      return 0;
   }
   public int useOn(Player player){
      return use();
   }
   public void onPickup(Player p){

   }
   public boolean isActive(){
      return active;
   }
   public void setActive(boolean a){
      this.active=a;
   }
   public ItemType getType(){
      return ItemType.OTHER;
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
}
