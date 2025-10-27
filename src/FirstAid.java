public class FirstAid extends Item{
    private final int healAmount;
    public FirstAid(int x,int y,int w,int h){
        super(x,y,w,h);
        this.healAmount =1;
    }
    public FirstAid(){
        super();
        this.healAmount=1;
    }
    @Override
    public void onPickup(Player p){
        if(p==null){
            return;
        }
        try{
            p.addItem(this);
        }catch(Throwable t){
            try{
                java.lang.reflect.Method m = p.getClass().getMethod("heal", int.class);
                m.invoke(p, healAmount);
            }catch(Exception ex){
                try{
                    java.lang.reflect.Method m2 = p.getClass().getMethod("addHeart", int.class);
                    m2.invoke(m2, healAmount);
                }catch(Exception ex2){
                    System.out.println("picked up FirstAid");
                }
            }
        }
    }
}
