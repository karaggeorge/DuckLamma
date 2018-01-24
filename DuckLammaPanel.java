import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.Image;
import javax.swing.*;
import java.util.Collections;
import java.util.List;
import javax.sound.sampled.*;


public class DuckLammaPanel extends JPanel{
	
	  private final int DELAY = 100;
    private final int WIDTH = 600;
    private final int HEIGHT = 500;
    private final int lammaImages = 10;
    private final int duckImages = 10;
    private List<ImageIcon> front = new ArrayList();
    private boolean gameOver = false, started = false;
    private int score = 0;
    private int currentImage = 0;
    private double time = 0;
    private int ticks;
    private double maxTime = 1.0;
    private List<ImageIcon> imageList = new ArrayList<ImageIcon> ();
    private JLabel label1,scoreLabel,timeLabel,label2;
    private JButton btnDuck,btnLamma,btnRestart,btnQuit;
    private Timer t;
    private ImageIcon loseDuck,loseLamma,loseTime,intro;
    private dLabel imageLabel;
    private Clip correctClip,wrongClip,introClip;
    //private final JLabel label;
 /**
  * Class constructor creates and defines the buttons.
  */
   public DuckLammaPanel(){
	   super(); //TODO jpanel constructor should be used to modify the panel layout to border layout 
	   
      DuckLammaPanel currentPanel = this;
       this.setLayout(new GridBagLayout());
       GridBagConstraints c = new GridBagConstraints();
       c.fill = GridBagConstraints.HORIZONTAL;

       loadImages();
       loadMusic();

       label1 = new JLabel("Is it a Duck or a Lamma? You have " + maxTime + " seconds to decide!",JLabel.CENTER);
       label2 = new JLabel("Sortcuts: Press Alt+D for Duck or Alt+L for Lamma",JLabel.CENTER);
       scoreLabel = new JLabel(("Score:  " + score),JLabel.CENTER);
       timeLabel = new JLabel(("Time:  " + String.format("%.1f",maxTime)),JLabel.CENTER);
       imageLabel = new dLabel(intro,"");
       btnDuck = new JButton("  Duck  ");
       btnLamma = new JButton("Lamma");
       btnRestart = new JButton("Start");
       btnQuit = new JButton("Quit");
       
       introClip.loop(Clip.LOOP_CONTINUOUSLY);

       btnLamma.addActionListener(createButtonListener("lamma"));
       btnDuck.addActionListener(createButtonListener("duck"));
       
       btnDuck.setMnemonic(KeyEvent.VK_D);
       btnLamma.setMnemonic(KeyEvent.VK_L);
       btnRestart.setMnemonic(KeyEvent.VK_S);
       btnQuit.setMnemonic(KeyEvent.VK_Q);

       btnQuit.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(currentPanel);
            frame.dispose();
          }
       });

       btnRestart.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            introClip.stop();
            if(!started){
              imageLabel.setDisplayText("");
              Collections.shuffle(imageList);
              
              score = 0;
              scoreLabel.setText(("Score:  " + score));
              currentImage = 0;
              imageLabel.setIcon(imageList.get(currentImage));
              ticks = -1;
              t.start();  
              btnRestart.setText("Restart");
              started = true;
              gameOver = false;
              btnRestart.setMnemonic(KeyEvent.VK_R);
            }
            else{
              t.stop();
              score = 0;

              imageLabel.setIcon(intro);
              scoreLabel.setText(("Score:  " + score));
              timeLabel.setText(("Time:  " + String.format("%.1f",maxTime)));
              btnRestart.setText("Start");
              started = false;
              btnRestart.setMnemonic(KeyEvent.VK_S);
            }
                 
          }
       });

       ticks = -1;
       t = new Timer(DELAY, new
        ActionListener()
         {
           public void actionPerformed(ActionEvent event)
           {
              ticks += 1;
              time = maxTime - (ticks * 0.1);
              timeLabel.setText(("Time:  " + String.format("%.1f",time)));
              if(ticks == (int)(maxTime/0.1)){
                t.stop();
                advanceWrong(3);
              }
           }
         });

       

       c.weightx = 0.4;
       c.gridwidth = 4;
       c.gridx = 0;
       c.gridy = 0;
       this.add(label1,c);

       c.weightx = 0.4;
       c.gridwidth = 4;
       c.gridx = 0;
       c.gridy = 1;
       this.add(label2,c);
       
       c.weightx = 0.4;
       c.gridwidth = 2;
       c.gridx = 0;
       c.gridy = 2;
       this.add(scoreLabel,c);
       
       c.weightx = 0.4;
       c.gridwidth = 2;
       c.gridx = 2;
       c.gridy = 2;
       this.add(timeLabel,c);
       
       c.weightx = 0.4;
       c.gridwidth = 4;
       c.gridx = 0;
       c.gridy = 3;
       this.add(imageLabel,c);
       
       c.weightx = 0.4;
       c.gridwidth = 2;
       c.gridx = 0;
       c.gridy = 4;
       c.ipady = 25;
       this.add(btnDuck,c);
       
       c.weightx = 0.4;
       c.gridwidth = 2;
       c.gridx = 2;
       c.gridy = 4;
       c.ipady = 25;
       this.add(btnLamma,c);
       
       c.weightx = 0.4;
       c.gridwidth = 2;
       c.gridx = 0;
       c.gridy = 5;
       c.ipady = 0;
       c.insets = new Insets(10,0,0,0);
       this.add(btnRestart,c);

       c.weightx = 0.4;
       c.gridwidth = 2;
       c.gridx = 2;
       c.gridy = 5;
       this.add(btnQuit,c);
       
   }

   void loadImages(){

      System.out.println("Loading Images..");
      for(int i = 1;i <= lammaImages;i++){
        ImageIcon icon = createImageIcon(("/images/lamma"+i+".gif"),("lamma"));
        icon = resizeImage(icon,WIDTH,HEIGHT);
        imageList.add(icon);
      }

      for(int i = 1;i <= duckImages;i++){
        ImageIcon icon = createImageIcon(("/images/duck"+i+".gif"),("duck"));
        icon = resizeImage(icon,WIDTH,HEIGHT);
        imageList.add(icon);
      }

      Collections.shuffle(imageList);

      loseDuck = resizeImage( createImageIcon("/images/gameOverDuck.gif","lose") , WIDTH,HEIGHT);
      loseLamma = resizeImage( createImageIcon("/images/gameOverLamma.gif","lose") , WIDTH,HEIGHT);
      loseTime = resizeImage( createImageIcon("/images/gameOverTime.gif","lose") , WIDTH,HEIGHT);
      intro = resizeImage( createImageIcon("/images/intro.gif","intro") , WIDTH,HEIGHT);
   }

   public void loadMusic(){
      System.out.println("Loading Audio Files..");
    try{
  
          correctClip = AudioSystem.getClip();
          correctClip.open(AudioSystem.getAudioInputStream(DuckLammaPanel.class.getResource("/music/correct.wav")));

          wrongClip = AudioSystem.getClip();
          wrongClip.open(AudioSystem.getAudioInputStream(DuckLammaPanel.class.getResource("/music/wrong.wav")));
        
          introClip = AudioSystem.getClip();
          introClip.open(AudioSystem.getAudioInputStream(DuckLammaPanel.class.getResource("/music/intro.wav")));
            
       }
       catch(Exception e){
        System.out.println("Something went wrong! " + e);
       }
  }

   ImageIcon resizeImage(ImageIcon img, int x, int y){
      String desc = img.getDescription();
      return new ImageIcon(img.getImage().getScaledInstance(x,y,Image.SCALE_SMOOTH),desc);
   }

   /** Returns an ImageIcon, or null if the path was invalid. 
    * A good encapsulation for the validation and retrival of the image file. error */
   private static ImageIcon createImageIcon(String path, String desc) {
       java.net.URL imgURL = DuckLammaPanel.class.getResource(path);
       if (imgURL != null) {
           return new ImageIcon(imgURL,desc);
       } else {
           System.err.println("Couldn't find file: " + path);
           return null;
       }
   }
 

   public void advanceCorrect(){
    correctClip.loop(1);
      score += 10;
      currentImage++;// = (currentImage+1)%(lammaImages+duckImages);
      if(currentImage >= lammaImages + duckImages){
        currentImage = 0;
        Collections.shuffle(imageList);
      } 
      scoreLabel.setText(("Score:  " + score));
      imageLabel.setIcon(imageList.get(currentImage));
      ticks = -1;
   }

   public void advanceWrong(int reason){
      wrongClip.loop(1);

      if(reason == 1) imageLabel.setIcon(loseDuck);
      else if(reason == 2) imageLabel.setIcon(loseLamma);
      else if(reason == 3) imageLabel.setIcon(loseTime);
      
      imageLabel.setDisplayText("Final Score: " + score);

      t.stop();
      gameOver = true;
      started = false;
      
   }


  public  ActionListener createButtonListener(final String id){
     return new
         ActionListener(){
       public void actionPerformed(ActionEvent event){
          if(!gameOver && started){
              correctClip.stop();
              correctClip.flush();
              if(imageList.get(currentImage).getDescription().compareTo(id) == 0){
                advanceCorrect();
              }
              else{
                if(imageList.get(currentImage).getDescription().compareTo("duck") == 0) advanceWrong(1);
                else advanceWrong(2);
              }

          }
         
     }
   };
  }

  private class dLabel extends JLabel{

    private String str;

    public dLabel(ImageIcon icon, String str){
      super(icon);
      this.str = str;
    }

    public void setDisplayText(String str){
      this.str = str;
      repaint();
    }

    public void paintComponent(Graphics g){
      super.paintComponent(g);
      Graphics2D brush = (Graphics2D) g;
       brush.setFont(new Font("Serif",Font.PLAIN,35));
       brush.setColor(Color.white);
       brush.drawString(str,160,100);
    }
  }
}
