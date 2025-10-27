import javax.swing.*;
// import javax.swing.border.Border;
import java.awt.*;
// import java.awt.event.ActionListener;
// import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
// import java.nio.Buffer;
// import java.util.concurrent.Flow;
import javax.imageio.ImageIO;
// import javax.sound.midi.SysexMessage;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
public class Game extends JFrame{
    private BufferedImage bgImage;
    private BufferedImage ingameImage;
    private BufferedImage heartImg;
    private BufferedImage blackHeartImg;
    private BufferedImage barbebItemImg;
    public BufferedImage useBarbedImg;
    private BufferedImage deagle;
    private BufferedImage ak;
    private BufferedImage uzi;
    private BufferedImage firstAid;
    private BufferedImage granade;
    private int maxHearts = 3;
    private JPanel main;
    private Player player;
    private javax.swing.Timer gameTimer;
    private final List<Zombie> zombies = new ArrayList<>();
    private final List<Item> droppedItems = new ArrayList<>();
    private final List<Platform>platforms = new ArrayList<>();
    private final Random rnd = new Random();
    private Mode currentMode;
    private int remN;
    private int remR;
    private int remD;
    private int remB;
    public enum SpawnMode{
        ALWAYS,PROB50
    }
    private SpawnMode spawnMode=SpawnMode.ALWAYS;
    private javax.swing.Timer spawnTimer;
    private int spawnIntervalMs=2000;
    private void resetSpawnCounters(Mode mode){
        this.currentMode=mode;
        Mode.DaySpawn ds = mode.getSpawnsForCurrentDay();
        remN=ds.n_zombie;
        remR=ds.r_zombie;
        remD=ds.d_zombie;
        remB=ds.b_zombie;
    }
    private int totalRemainingSpawns(){
        return remN+remR+remD+remB;
    }
    private void startSpawnTimer(JPanel gamePanel){
        if(spawnTimer != null && spawnTimer.isRunning()){
            spawnTimer.stop();
        }
        int initialDelay = 1000+rnd.nextInt(4001);
        spawnIntervalMs=initialDelay;
        spawnTimer = new javax.swing.Timer(spawnIntervalMs,e->{
            if(totalRemainingSpawns() <=0){
                spawnTimer.stop();
                return;
            }
            boolean doSpawn =(spawnMode == SpawnMode.ALWAYS)?true:rnd.nextBoolean();
            if(doSpawn){
                spawnRandomZombie(gamePanel);
            }
            int nextDelay = 1000+rnd.nextInt(4001);
            spawnTimer.setDelay(nextDelay);
        });
        spawnTimer.setRepeats(true);
        spawnTimer.start();
    }
    private void stopSpawnTimer(){
        if(spawnTimer != null){
            spawnTimer.stop();
        }
    }
    public Game(){
        try{
            bgImage=ImageIO.read(getClass().getResource("/image/background1.png"));
        }catch(IOException|IllegalArgumentException e){
            bgImage =null;
            System.err.println("Failed to load background image: "+e.getMessage());
        }
        try{
            ingameImage=ImageIO.read(getClass().getResource("/image/ingamebg.png"));
        }catch(IOException|IllegalArgumentException e){
            ingameImage=null;
            System.err.println("Failed to load ingame background image: "+e.getMessage());
        }
        try{
            heartImg=ImageIO.read(getClass().getResource("/image/h.png"));
        }catch(IOException|IllegalArgumentException e){
            heartImg = null;
            System.err.println("Failed to load heart image: "+e.getMessage());
        }
        try{
            blackHeartImg=ImageIO.read(getClass().getResource("/image/bh.png"));
        }catch(IOException|IllegalArgumentException e){
            blackHeartImg = null;
            System.err.println("Failed to load black heart image: "+e.getMessage());
        }
        if(player!=null){
            maxHearts = Math.max(maxHearts, player.getHeart());
        }
        try{
            barbebItemImg=ImageIO.read(getClass().getResource("/image/Item/barbebwire/Itembarbeb.png"));
        }catch(IOException|IllegalArgumentException e){
            barbebItemImg = null;
            System.err.println("Failed to load barbed wire item image: "+e.getMessage());
        }
        // try{
        //     useBarbedImg = ImageIO.read(getClass().getResource("/image/Item/barbebwire/usedBarbeb.png"));
        // }catch(IOException|IllegalArgumentException e){
        //     useBarbedImg = null;
        //     System.err.println("Failed to load useBarbeb wire image: "+e.getMessage());
        // }
        try{
            deagle=ImageIO.read(getClass().getResource("/image/Item/Deserteagle/deagle.png"));
        }catch(IOException|IllegalArgumentException e){
            deagle = null;
            System.err.println("Failed to load Deserteagle image: "+e.getMessage());
        }
        try{
            ak=ImageIO.read(getClass().getResource("/image/Item/AK47/AK.png"));
        }catch(IOException|IllegalArgumentException e){
            ak = null;
            System.err.println("Failed to load AK47 image: "+e.getMessage());
        }
        try{
            uzi=ImageIO.read(getClass().getResource("/image/Item/UZI/uzi.png"));
        }catch(IOException|IllegalArgumentException e){
            uzi = null;
            System.err.println("Failed to load Uzi image: "+e.getMessage());
        }
        try{
            firstAid=ImageIO.read(getClass().getResource("/image/Item/first_aid/firstaid.png"));
        }catch(IOException|IllegalArgumentException e){
            firstAid = null;
            System.err.println("Failed to load firstaidkit image: "+e.getMessage());
        }
        try{
            granade=ImageIO.read(getClass().getResource("/image/Item/granage/granade.png"));
        }catch(IOException|IllegalArgumentException e){
            granade = null;
            System.err.println("Failed to load granade image: "+e.getMessage());
        }
        setTitle("Defence the castle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200,650);
        setLocationRelativeTo(null);
        setResizable(false);

        main = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                 if(bgImage != null){
                g.drawImage(bgImage,0,0,getWidth(),getHeight(),this);
                }
            }
        };
        main.setOpaque(true);
        setContentPane(main);

        JLabel title = new JLabel("Defence the castle");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("SansSerif",Font.BOLD,60));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30,10,30,10));
        title.setOpaque(false);
        main.add(title,BorderLayout.NORTH);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER,120,50));
        center.setOpaque(false);

        JButton howtoplay = makeButton("How to play",300,100);
        JButton play = makeButton("Play",300,100);
        play.addActionListener(e->showModeScreen());
        howtoplay.addActionListener(e->showControlScreen());
        center.add(howtoplay);
        center.add(play);
        main.add(center,BorderLayout.CENTER);
    }
    private JButton makeButton(String text,int w,int h){
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(w,h));
        btn.setFont(new Font("SansSerif",Font.BOLD,30));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK,6),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        return btn;
    }
    //
    //Play
    //
    private void showModeScreen(){
        JPanel mode = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                 if(bgImage != null){
                g.drawImage(bgImage,0,0,getWidth(),getHeight(),this);
                }
            }
        };
        JLabel s_mode = new JLabel("Select Mode",SwingConstants.CENTER);
        s_mode.setForeground(Color.WHITE);
        s_mode.setFont(new Font("SansSerif",Font.BOLD,48));
        
        JButton back = makeButton("Back",150,60);
        back.addActionListener(e->{
            stopGameLoop();  
            setContentPane(main);
            revalidate();
            repaint();
        });
        JButton easy = makeButton("EASY",150,70);
        JButton normal = makeButton("NORMAL",200,70);
        JButton hard = makeButton("HARD",150,70);
        easy.addActionListener(e->startGame(new Mode.easy(),SpawnMode.PROB50));
        normal.addActionListener(e->startGame(new Mode.normal(),SpawnMode.PROB50));
        hard.addActionListener(e->startGame(new Mode.hard(),SpawnMode.PROB50));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        left.setOpaque(false);
        left.add(back);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        right.setOpaque(false);
        right.setPreferredSize(left.getPreferredSize());
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER,120,50));
        center.setOpaque(false);
        center.add(easy);
        center.add(normal);
        center.add(hard);

        top.add(left,BorderLayout.WEST);
        top.add(s_mode,BorderLayout.CENTER);
        top.add(right,BorderLayout.EAST);
        top.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
        mode.add(top,BorderLayout.NORTH);
        mode.add(center,BorderLayout.CENTER);
        setContentPane(mode);
        revalidate();
        repaint();
    }
    //
    //Start Game
    //
    private void startGame(Mode mode,SpawnMode modeSpawn){
        this.spawnMode=modeSpawn;
        JPanel gamePanel = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(ingameImage != null){
                g.drawImage(ingameImage,0,0,getWidth(),getHeight(),this);
                }
                // else if(bgImage != null){
                //     g.drawImage(bgImage,0,0,getWidth(),getHeight(),this);
                // }
                else{
                    g.setColor(Color.BLACK);
                    g.fillRect(0,0,getWidth(),getHeight());
                }
                if(player!=null){
                    player.draw(g);
                }
                //Draw Zombies
                synchronized(zombies){
                    for(Zombie z : zombies){
                        z.draw(g);
                    }
                }
                if(player != null){
                    int iconW=64;
                    int iconH=64;
                    int spacing = 6;
                    int sx = 12;
                    int sy = 12;
                    for(int i=0;i<maxHearts;i++){
                        int ix = sx + i*(iconW+spacing);
                        BufferedImage img = (i<player.getHeart())?heartImg:blackHeartImg;
                        if(img!=null){
                            g.drawImage(img,ix,sy,iconW,iconH,this);
                        }
                        else{
                            g.setColor(i<player.getHeart()?Color.RED:Color.DARK_GRAY);
                            g.fillRect(ix,sy,iconW,iconH);
                        }
                    }
                    String ammo = player.getMagazine() + " / "+player.getMaxMagazine();
                    Font prevFont = g.getFont();
                    g.setFont(new Font("SansSerif",Font.BOLD,30));
                    FontMetrics fm = g.getFontMetrics();
                    int sw = fm.stringWidth(ammo);
                    int padding = 12;
                    int ax = getWidth() - sw - padding;
                    int ay = getHeight() - padding;
                    if(player.isReloading()){
                        long t = System.currentTimeMillis()/300;
                        boolean blinkOn = (t%2)==0;
                        g.setColor(new Color(0,0,0,160));
                        g.drawString(ammo,ax+2,ay-4);
                        if(blinkOn){
                            g.setColor(Color.WHITE);
                            g.drawString(ammo,ax,ay-6);
                        }
                        else{
                            g.setColor(new Color(180,180,180,160));
                            g.drawString(ammo,ax,ay-6);
                        }
                        int barW = 120;
                        int barH = 8;
                        int bx = ax - barW -8;
                        int by = ay - 18;
                        int rt = player.getReloadTime();
                        int rtm = Math.max(1,player.getReloadTimer());
                        double prog =1.0-((double)rtm/(double)rt);
                        int fill=(int)(barW*prog);
                        g.setColor(new Color(0,0,0,160));
                        g.fillRect(bx,by,barW,barH);
                        g.setColor(new Color(100,200,255));
                        g.fillRect(bx+2,by+2,Math.max(0,fill-4),Math.max(0,barH-4));
                        g.setColor(Color.DARK_GRAY);
                        g.drawRect(bx, by, barW,barH);
                    }
                    else{
                        g.setColor(new Color(0,0,0,160));
                        g.drawString(ammo,ax+2,ay-4);
                        g.setColor(Color.WHITE);
                        g.drawString(ammo,ax,ay-6);
                    }
                    g.setFont(prevFont);
                    java.util.List<Player.InventorySlot>inv = player.getInventory();
                    if(inv != null&&!inv.isEmpty()){
                        int invIconW = 48;
                        int invIconH = 48;
                        int invSpacing = 8;
                        int startX = 12;
                        int startY=getHeight()-invIconH-12;
                        g.setColor(new Color(0,0,0,120));
                        int bgw = inv.size()*(invIconW+invSpacing)-invSpacing+12;
                        g.fillRoundRect(startX-6, startY-6,bgw,invIconH+12, 8, 8);
                        Font small = new Font("SansSerif",Font.BOLD,12);
                        Font prev = g.getFont();
                        g.setFont(small);
                        for(int i=0;i<inv.size();i++){
                            Player.InventorySlot slot = inv.get(i);
                            Item it = slot.getItem();
                            int ix = startX+i*(invIconW+invSpacing);
                            Rectangle b = new Rectangle(ix,startY,invIconW,invIconH);
                            if(it instanceof Barbebwire&&barbebItemImg!=null){
                                g.drawImage(barbebItemImg,b.x,b.y,b.width,b.height,this);
                            }
                            else if(it instanceof DesertEagle&&deagle!=null){
                                g.drawImage(deagle,b.x,b.y,b.width,b.height,this);
                            }
                            else if(it instanceof FirstAid && firstAid!=null){
                                g.drawImage(firstAid,b.x,b.y,b.width,b.height,this);
                            }
                            else if(it instanceof Granade && granade!=null){
                                g.drawImage(granade,b.x, b.y,b.width,b.height,this);
                            }
                            else if(it instanceof AK47 && ak!=null){
                                g.drawImage(ak,b.x,b.y,b.width,b.height,this);
                            }
                            else if(it instanceof Uzi&&uzi!=null){
                                g.drawImage(uzi,b.x,b.y,b.width,b.height,this);
                            }
                            else{
                                g.setColor(Color.LIGHT_GRAY);
                                g.fillRect(b.x,b.y,b.width,b.height);
                            }
                            int count = slot.getCount();
                            if(count>1){
                                String sCount = String.valueOf(count);
                                int badgeW=22;
                                int badgeH=16;
                                int bx = b.x+b.width-badgeW+6;
                                int by = b.y+b.height-badgeH+4;
                                g.setColor(new Color(0,0,0,200));
                                g.fillRoundRect(bx, by, badgeW, badgeH, 8, 8);
                                g.setColor(Color.WHITE);
                                FontMetrics fm2 = g.getFontMetrics();
                                int tx = bx+(badgeW-fm2.stringWidth(sCount))/2;
                                int ty = by+((badgeH-fm2.getHeight())/2)+fm2.getAscent();
                                g.drawString(sCount,tx,ty);
                            }
                        }
                        g.setFont(prev);
                    }
                }
                for(Platform p : platforms){
                    if(p != null){
                        p.draw(g);
                    }
                }
                synchronized(droppedItems){
                    for(Item it:droppedItems){
                        if(it == null||!it.isActive()){
                            continue;
                        }
                        Rectangle b = it.getBounds();
                        if(it instanceof Barbebwire){
                                Barbebwire bw = (Barbebwire) it;
                                java.awt.image.BufferedImage img_d=null;
                                if(!bw.isPlaced()){
                                    img_d = barbebItemImg;
                                }
                                else{
                                    img_d = bw.getPlacedImage() != null?bw.getPlacedImage():useBarbedImg;
                                }
                                if(img_d!=null){
                                    g.drawImage(img_d,b.x,b.y,b.width,b.height,this);
                                }
                                else{
                                    g.setColor(Color.BLACK);
                                    g.fillRect(b.x,b.y,b.width,b.height);
                                }
                            }
                        
                        else if(it instanceof DesertEagle && deagle != null){
                            g.drawImage(deagle,b.x,b.y,b.width,b.height,this);
                        }
                        else if(it instanceof FirstAid && firstAid!=null){
                            g.drawImage(firstAid, b.x, b.y, b.width, b.height, this);
                        }
                        else if(it instanceof AK47&&ak!=null){
                            g.drawImage(ak,b.x,b.y,b.width,b.height,this);
                        }
                        else if(it instanceof Uzi&&uzi!=null){
                            g.drawImage(uzi,b.x,b.y,b.width,b.height,this);
                        }
                        else if(it instanceof Granade){
                            Granade gr = (Granade)it;
                            gr.render(g, granade);
                        }
                        else{
                            g.setColor(new Color(200,200,200,200));
                           g.fillRect(b.x, b.y, b.width, b.height);
                        }
                    }
                }
            }
        };
        gamePanel.setOpaque(true);
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel dayLabel = new JLabel("Day "+mode.getDay(),SwingConstants.CENTER);
        dayLabel.setFont(new Font("SansSerif",Font.BOLD,28));
        dayLabel.setForeground(Color.WHITE);

        JButton backtomenu = makeButton("Back", 120, 40);
        backtomenu.addActionListener(e->{
            stopGameLoop();
            setContentPane(main);
            revalidate();
            repaint();
        });
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,6,6));
        left.setOpaque(false);

        top.add(left,BorderLayout.WEST);
        top.add(dayLabel,BorderLayout.CENTER);
        top.setBorder(BorderFactory.createEmptyBorder(6,10,0,10));
        gamePanel.add(top,BorderLayout.NORTH);
        setContentPane(gamePanel);
        revalidate();
        repaint();
        platforms.clear();
        platforms.add(new Platform(0,gamePanel.getHeight()-80,gamePanel.getWidth(),8,false));
        gamePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e){
                if(player!=null){
                    player.setMaxWidth(gamePanel.getWidth());
                }
                if(!platforms.isEmpty()){
                    Platform p =platforms.get(0);
                    p.w=gamePanel.getWidth();
                    p.y=gamePanel.getHeight()-p.h;
                    if(player != null){
                         int playerH = player.getBounds().height;
                        player.setPosition(player.getX(), p.y - playerH);
                    }
                }
            }
        });
        resetSpawnCounters(mode);
        startSpawnTimer(gamePanel);
        player = new Player(200, 432,100,100);
        SwingUtilities.invokeLater(()->{
           player.setMaxWidth(gamePanel.getWidth()>0?gamePanel.getWidth():getContentPane().getWidth());
            if(!platforms.isEmpty()){
               Platform ground = platforms.get(0);
               int playerH = player.getBounds().height;
               int spawnY = ground.y - playerH+12;
               player.setPosition(player.getX(), spawnY);
           } else {
               int playerH = player.getBounds().height;
               player.setPosition(player.getX(), gamePanel.getHeight() - playerH - 8+12);
           }
           gamePanel.requestFocusInWindow();
        });
        if(gameTimer != null && gameTimer.isRunning()){
            gameTimer.stop();
        }
        gameTimer = new javax.swing.Timer(40, ev->{
            if(player != null){
                player.tick();
            }
            synchronized(zombies){
                Iterator<Zombie> it = zombies.iterator();
                while(it.hasNext()){
                    Zombie z = it.next();
                    z.move();
                    if(player != null && z.getBounds().intersects(player.getBounds())){
                        player.takeDamage(z.getDamage());
                        it.remove();
                        if(player.getHeart() <=0){
                            stopGameLoop();
                            JOptionPane.showMessageDialog(this, "Game Over! You survived until Day "+mode.getDay(),"Game Over",JOptionPane.INFORMATION_MESSAGE);
                            setContentPane(main);
                            revalidate();
                            repaint();
                            return;
                        }
                        continue;
                    }
                    if(z.getX()+z.getBounds().width <0){
                        it.remove();
                    }
                }
            }
            synchronized(droppedItems){
                for(Item dItem:droppedItems){
                    if(!(dItem instanceof Barbebwire)){
                        continue;
                    }
                    Barbebwire bw = (Barbebwire) dItem;
                    if(!bw.isPlaced()||!bw.isActive()){
                        continue;
                    }
                    Rectangle bwBounds = bw.getBounds();
                    synchronized(zombies){
                        for(Zombie z:zombies){
                            if(z == null){
                                continue;
                            }
                            Rectangle zb = z.getBounds();
                            if(zb.intersects(bwBounds)){
                                bw.applyto(z);
                            }
                            else{
                                bw.onExit(z);
                            }
                        }
                    }
                }
            }
            if(player != null){
                List<Bullet> bullets = player.getBullets();
                synchronized(bullets){
                    synchronized(zombies){
                        for(int bi = bullets.size()-1;bi>=0;bi--){
                            Bullet b = bullets.get(bi);
                            if(!b.isActive()){
                                bullets.remove(bi);
                                continue;
                            }
                            Rectangle br = b.getBounds();
                            boolean hit = false;
                            for(int zi = zombies.size()-1;zi>=0;zi--){
                                Zombie z = zombies.get(zi);
                                if(br.intersects(z.getBounds())){
                                    z.takeDamage(b.dodamage());
                                    b.setActive(false);
                                    bullets.remove(bi);
                                    if(z.getHP()<=0){
                                        int iw = Math.max(24,Math.min(64,z.getBounds().width));
                                        int ih = Math.max(24,Math.min(64,z.getBounds().height));
                                        int ix = z.getX()+(z.getBounds().width-iw)/2;
                                        int iy = z.getY()+z.getBounds().height-ih+10;
                                        if(ix<0){
                                            ix=0;
                                        }
                                        if(iw+iw>gamePanel.getWidth()){
                                            ix = Math.max(0,gamePanel.getWidth() -iw);
                                        }
                                        Item drop = z.createDrop(rnd,ix,iy,iw,ih);
                                        if(drop!=null){
                                            synchronized(droppedItems){
                                                droppedItems.add(drop);
                                            }
                                        }
                                        zombies.remove(zi);
                                    }
                                    hit =true;
                                    break;
                                }
                            }
                            if(hit){
                                continue;
                            }
                        }
                    }
                }
                synchronized(droppedItems){
                    for(int di = droppedItems.size()-1;di>=0;di--){
                        Item it = droppedItems.get(di);
                        if(it==null){
                            continue;
                        }
                        if(it instanceof Granade){
                            Granade gr = (Granade) it;
                            gr.tick(zombies);
                            if(gr.isThrown()){
                                int curTop = gr.getBounds().y;
                                int curBottom = curTop+gr.getBounds().height;
                                int prevTop=gr.getPrevY();
                                int prevBottom=prevTop+gr.getBounds().height;
                                for(Platform p:platforms){
                                    int pTop=p.y;
                                    int pLeft=p.x;
                                    int pRight=p.x+p.w;

                                    boolean crossedVertically=(prevBottom<=pTop&&curBottom>=pTop);
                                    boolean overlapsHoriz = (gr.getBounds().x+gr.getBounds().width>pLeft&&gr.getBounds().x<pRight);
                                    if((crossedVertically&&overlapsHoriz)||p.intersects(gr.getBounds())){
                                        gr.landOnPlatform(pTop);
                                        break;
                                    }
                            }
                            if(!gr.active){
                                droppedItems.remove(di);
                            }
                            else{
                                if(gr.getBounds().y>gamePanel.getHeight()+200){
                                    droppedItems.remove(di);
                                }
                            }
                        }
                    }
                }
            }
            synchronized(zombies){
                    for(int zi = zombies.size()-1; zi >= 0; zi--){
                        Zombie z = zombies.get(zi);
                        if(z == null) continue;
                        if(z.getHP() <= 0){
                            zombies.remove(zi);
                        }
                    }
                }
            if(totalRemainingSpawns() <=0){
                synchronized(zombies){
                    if(zombies.isEmpty()){
                        // stopSpawnTimer();
                        // if(gameTimer != null && gameTimer.isRunning()){
                        //     gameTimer.stop();
                        // }
                        // boolean hasNext = false;
                        // try{
                        //     hasNext = currentMode.advanceDay();
                        // }catch(Exception ex){
                        //     System.err.println("Failed to advance day: "+ex.getMessage());
                        // }
                        // if(!hasNext){
                        //     SwingUtilities.invokeLater(()->{
                        //         JOptionPane.showMessageDialog(this, "Congratulations! You have survived all "+currentMode.getDay()+" days!","You Win!",JOptionPane.INFORMATION_MESSAGE);
                        //         stopGameLoop();
                        //         setContentPane(main);
                        //         revalidate();
                        //         repaint();
                        //     });
                        // }
                        // else{
                        //     SwingUtilities.invokeLater(()->{
                        //         startGame(currentMode, spawnMode);
                        //     });
                        // }
                        // return;
                        stopSpawnTimer();
                        boolean hasNext = false;
                        try{
                            hasNext = currentMode.advanceDay();
                        }catch(Exception ex){
                            System.err.println("Failed to advance day: "+ex.getMessage());
                        }
                        if(!hasNext){
                            SwingUtilities.invokeLater(()->{
                                stopGameLoop();
                                JOptionPane.showMessageDialog(this,"Congratulations! You have survived all "+currentMode.getDay()+" days!","You Win!",JOptionPane.INFORMATION_MESSAGE);
                                // stopGameLoop();
                                setContentPane(main);
                                revalidate();
                                repaint();
                            });
                        }
                        else{
                            SwingUtilities.invokeLater(()->{
                                resetSpawnCounters(currentMode);
                                startSpawnTimer(gamePanel);
                                dayLabel.setText("Day "+currentMode.getDay());
                            });
                        }
                        return;
                    }
                }
            }
            // if(rnd.nextInt(100)<5){
            //     spawnRandomZombie(gamePanel);
            // }
            gamePanel.repaint();
        }});
        gameTimer.start();

        InputMap im = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = gamePanel.getActionMap();

        im.put(KeyStroke.getKeyStroke("pressed A"), "left.press");
        im.put(KeyStroke.getKeyStroke("released A"), "left.release");
        im.put(KeyStroke.getKeyStroke("pressed D"), "right.press");
        im.put(KeyStroke.getKeyStroke("released D"), "right.release");
        im.put(KeyStroke.getKeyStroke("pressed SPACE"),"fire.press");
        im.put(KeyStroke.getKeyStroke("released SPACE"),"fire.release");
        im.put(KeyStroke.getKeyStroke("pressed R"),"reload");
        im.put(KeyStroke.getKeyStroke("pressed E"),"takeitem");
        im.put(KeyStroke.getKeyStroke("pressed Z"),"placeBarbeb");
        im.put(KeyStroke.getKeyStroke("pressed X"),"useFirstAid");
        im.put(KeyStroke.getKeyStroke("pressed C"),"throwGranade");
        am.put("left.press", new AbstractAction(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                player.setLeftPressed(true);
            }
        });
        am.put("left.release", new AbstractAction(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                player.setLeftPressed(false);
            }
        });
        am.put("right.press", new AbstractAction(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                player.setRightPressed(true);
            }
        });
        am.put("right.release", new AbstractAction(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                player.setRightPressed(false);
            }
        });
        am.put("fire.press",new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                player.setFirePressed(true);
            }
        });
        am.put("fire.release",new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                player.setFirePressed(false);
            }
        });
        am.put("reload",new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                player.reloadGun();
            }
        });
        am.put("takeitem",new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                if(player!=null){
                    synchronized(droppedItems){
                        Iterator<Item> it = droppedItems.iterator();
                        while(it.hasNext()){
                            Item itx = it.next();
                            if(itx != null && itx.isActive() && player.getBounds().intersects(itx.getBounds())){
                                if(itx instanceof Barbebwire){
                                    Barbebwire bw = (Barbebwire) itx;
                                    if(bw.isPlaced()){
                                        continue;
                                    }
                                }
                                itx.onPickup(player);
                                itx.setActive(false);
                                it.remove();
                                break;
                            }
                        }
                    }
                }
            }
        });
        am.put("placeBarbeb",new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                if(player == null){
                    return;
                }
                java.util.List<Player.InventorySlot> inv = player.getInventory();
                if(inv==null||inv.isEmpty()){
                    return;
                }
                for(int i=0;i<inv.size();i++){
                    Player.InventorySlot slot=inv.get(i);
                    if(slot==null){
                        continue;
                    }
                    Item it = slot.getItem();
                    if(!(it instanceof Barbebwire)){
                        continue;
                    }
                    int baseW = player.getSpriteWidth()+120;
                    int baseH = player.getSpriteHeight()/2;
                    int w = Math.max(40,Math.min(200,(int)(baseW*1.0)));
                    int h = Math.max(16, Math.min(64,(int)(baseH*0.9)));

                    boolean facingRight = true;
                    try{
                        java.lang.reflect.Method m = player.getClass().getMethod("isFacingRight");
                        facingRight = (boolean)m.invoke(player);
                    }catch(Exception ex){
                        try{
                            java.lang.reflect.Method ml = player.getClass().getMethod("isLeftPressed");
                            java.lang.reflect.Method mr = player.getClass().getMethod("isRightPressed");
                            boolean left = (boolean)ml.invoke(player);
                            boolean right = (boolean)mr.invoke(player);
                            if(left&&!right){
                                facingRight= false;
                            }
                            else if(right&&!left){
                                facingRight = true;
                            }
                            }catch(Exception ex2){

                            }
                    }
                    int offsetX=8;
                    int offsetY=-10;
                    int px;
                    if(facingRight){
                        px=player.getX()+player.getSpriteWidth()+offsetX;
                    }
                    else{
                        px=player.getX()-w-offsetX;
                    }
                    int py = player.getY()+player.getSpriteHeight()-h-offsetY;

                    Barbebwire placed = new Barbebwire(px,py,w,h);
                    placed.setPlaced(true);
                    if(useBarbedImg!=null){
                        placed.setPlacedImage(useBarbedImg);
                    }
                    synchronized(droppedItems){
                        droppedItems.add(placed);
                    }
                    try{
                        java.lang.reflect.Method dec = slot.getClass().getMethod("decrement");
                        dec.invoke(slot);
                        java.lang.reflect.Method getCount=slot.getClass().getMethod("getCount");
                        int c = (int)getCount.invoke(slot);
                        if(c<=0){
                            inv.remove(i);
                        }
                        }catch(Exception ex){
                            try{
                                java.lang.reflect.Field cf =slot.getClass().getDeclaredField("count");
                                cf.setAccessible(true);
                                int c = cf.getInt(slot)-1;
                                if(c<=0){
                                    inv.remove(i);
                                }
                                else{
                                    cf.setInt(slot,c);
                                }
                            }catch(Exception ex2){
                                inv.remove(i);
                            }
                        }
                        System.out.println("Placed Barbebwire at "+px+","+py+" size="+w+"x"+h+"facingRight="+facingRight);
                        break;
                    }
                }
        });
        am.put("useFirstAid",new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                if(player == null){
                    return;
                }
                java.util.List<Player.InventorySlot>inv = player.getInventory();
                if(inv == null||inv.isEmpty()){
                    return;
                }
                for(int i=0;i<inv.size();i++){
                    Player.InventorySlot slot = inv.get(i);
                    if(slot == null){
                        continue;
                    }
                    Item it = slot.getItem();
                    if(it instanceof FirstAid){
                        boolean healed = false;
                        try{
                            java.lang.reflect.Method m = player.getClass().getMethod("heal", int.class);
                            m.invoke(player,1);
                            healed=true;
                        }catch(Exception ex1){
                            try{
                                java.lang.reflect.Method m2 = player.getClass().getMethod("addHeart", int.class);
                                m2.invoke(player, 1);
                                healed = true;
                            }catch (Exception ex2){
                                try{
                                    java.lang.reflect.Method getHeart = player.getClass().getMethod("getHeart");
                                    int cur = (int)getHeart.invoke(player);
                                    java.lang.reflect.Field hf = player.getClass().getDeclaredField("heart");
                                    hf.setAccessible(true);
                                    hf.setInt(player,Math.min(maxHearts,cur+1));
                                    healed = true;
                                }catch(Exception ex3){
                                    System.out.println("Cannot apply heal method on player: "+ex3.getMessage());
                                }
                            }
                        }
                        if(healed){
                            try{
                                java.lang.reflect.Field cf = slot.getClass().getDeclaredField("count");
                                cf.setAccessible(true);
                                int c = cf.getInt(slot)-1;
                                if(c<=0){
                                    inv.remove(i);
                                }
                                else{
                                    cf.setInt(slot,c);
                                }
                            }catch(Exception exc){
                                inv.remove(i);
                            }
                            System.out.println("Used FirstAid");
                        }
                        break;
                    }
                }
            }
        });
        am.put("throwGranade",new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                if(player == null){
                    return;
                }
                java.util.List<Player.InventorySlot> inv = player.getInventory();
                if(inv == null||inv.isEmpty()){
                    return;
                }
                for(int i =0;i<inv.size();i++){
                    Player.InventorySlot slot = inv.get(i);
                    if(slot == null){
                        continue;
                    }
                    Item it = slot.getItem();
                    if(it instanceof Granade){
                        boolean removed = false;
                        try{
                            java.lang.reflect.Field cf = slot.getClass().getDeclaredField("count");
                            cf.setAccessible(true);
                            int c = cf.getInt(slot)-1;
                            if(c<=0){
                                inv.remove(i);
                                removed = true;
                            }
                            else{
                                cf.setInt(slot,c);
                            }
                        }catch(Exception ex){
                            inv.remove(i);
                            removed=true;
                        }
                        int startX=player.getX()+player.getSpriteWidth()/2;
                        int startY = player.getY();
                        int targetX=startX+200;
                        int targetY=startY-40;
                        Granade g = new Granade(startX,startY,24,24);
                        g.use(startX,startY,targetX,targetY);
                        synchronized(droppedItems){
                            droppedItems.add(g);
                        }
                        System.out.println("Thrown Granade");
                        break;
                    }
                }
            }
        });
        gamePanel.addComponentListener(new java.awt.event.ComponentAdapter(){
            @Override
            public void componentResized(java.awt.event.ComponentEvent e){
                if(player != null){
                    player.setMaxWidth(gamePanel.getWidth());
                }
            }
        });
    }
    private void spawnRandomZombie(JPanel gamePanel){
        if(totalRemainingSpawns() <=0){
            return;
        }
        int startX = gamePanel.getWidth();
        int groundY = 435;
        int total = totalRemainingSpawns();
        int pick  = rnd.nextInt(total);
        Zombie z;
        if(pick < remN){
            z = new n_Zombie();
            remN--;
        }
        else if(pick < remN + remR){
            z = new r_Zombie();
            remR--;
        }
        else if(pick < remN+remR+remD){
            z = new d_Zombie();
            remD--;
        }
        else{
            z = new b_Zombie();
            remB--;
        }
        // z.setPosition(startX, groundY);
        int platformTop;
        if(!platforms.isEmpty()){
            platformTop=platforms.get(0).y;
        }
        else{
            platformTop=gamePanel.getHeight();
        }
        int spawnY = platformTop - z.getBounds().height + z.getFeetAdjust()+6;
        z.setPosition(startX, spawnY);
        synchronized(zombies){
            zombies.add(z);
        }
    }
    private void stopGameLoop(){
        if(gameTimer != null){
            gameTimer.stop();
        }
        stopSpawnTimer();
        synchronized(zombies){
            zombies.clear();
        }
    }
    //
    //Control
    //
    private void showControlScreen(){
        JPanel control = new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(bgImage != null){
                    g.drawImage(bgImage,0,0,getWidth(),getHeight(),this);
                }
            }
        };
        control.setOpaque(false);
        JButton back = makeButton("Back", 150, 60);
        back.addActionListener(e->{
            stopGameLoop();
            setContentPane(main);
            revalidate();
            repaint();
        });
        JLabel s_control = new JLabel("Control",SwingConstants.CENTER);
        s_control.setForeground(Color.WHITE);
        s_control.setFont(new Font("SansSerif",Font.BOLD,48));
       
       JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        left.setOpaque(false);
        left.add(back);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setPreferredSize(left.getPreferredSize());

        top.add(left,BorderLayout.WEST);
        top.add(s_control,BorderLayout.CENTER);
        top.add(right,BorderLayout.EAST);
        top.setBorder(BorderFactory.createEmptyBorder(6,10,0,10));
        control.add(top,BorderLayout.NORTH);
       
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        String[] lines={
            "A: Move Left",
            "D: Move Right",
            "R: Reload",
            "E: Take Item",
            "SpaceBar: Shoot",
            "Z: Use Barbeb Wire",
            "X: Use First Aid Kit",
            "C: Use Garnade",
        };
        for(String t:lines){
            JLabel cmd = new JLabel(t);
            cmd.setForeground(Color.WHITE);
            cmd.setFont(new Font("SansSerif",Font.BOLD,28));
            cmd.setAlignmentX(Component.CENTER_ALIGNMENT);
            cmd.setBorder(BorderFactory.createEmptyBorder(10,30,10,10));
            list.add(cmd);
        }
        list.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(list);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        control.add(scroll,BorderLayout.CENTER);

        setContentPane(control);
        revalidate();
        repaint();
    }
    
}
