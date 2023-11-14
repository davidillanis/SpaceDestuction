package spacedestruction;

import Exceptions.SongException;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Juego extends JPanel {
    public JFrame ventana;
    public StarAnimation star_animation;
    public Personaje personaje;
    public ImageIcon img_heart, img_speed;
    public List<Meteorito> list_meteorito=new ArrayList<>();
    public Obstaculo2 speed;
    //public Song sonido=new Song();
    public PluralSoundControl sonido;
    public int score=1;
    //hilos
    private Thread hilo_speed;
    private Thread hilo_princ;
    private Thread hilo_meteo;
    public boolean Is_game_over;
    
    public Juego() {
        setLayout(new BorderLayout());
        try {
            sonido=new PluralSoundControl(new File("src/song/"));
        } catch (SongException ex) {
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
        }
        star_animation = new StarAnimation(new ImageIcon("src/images/star_1.png"));
        personaje = new Personaje(this);
        list_meteorito.add(new Meteorito(this));
        speed=new Obstaculo2(this, new ImageIcon("src/images/speed.png"), OBCIONES.speed);
        img_heart = new ImageIcon("src/images/heart.png");
        img_heart.setImage(img_heart.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        img_speed = new ImageIcon("src/images/speed.png");
        img_speed.setImage(img_speed.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        
        ventana = new JFrame();
        ventana.addKeyListener(personaje);
        Is_game_over=false;

        ControlHilos();
    }

    private void ControlHilos() {
        
        class AdminHilos extends Thread {
            int cont=1;
            @Override public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(10);
                        //sonido
                        if((sonido.isPlayingSong("fondo1.wav") || sonido.isPlayingSong("fondo2.wav"))==false){
                            if((int)(Math.random()*4)%2==0){
                                sonido.restartSong("fondo1.wav");
                                sonido.startSong("fondo1.wav");
                            }else{
                                sonido.restartSong("fondo2.wav");
                                sonido.startSong("fondo2.wav");
                            }
                        }
                        //
                        star_animation.Animar();
                        if(cont%1200==0){
                            score++;
                            cont=0;
                            repaint();
                        }
                        cont++;
                        
                        if(personaje.getVida()<0){
                            Is_game_over=true;
                            hilo_speed.interrupt();
                            hilo_princ.interrupt();
                            hilo_meteo.interrupt();
                        }
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    } catch (SongException ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
        hilo_meteo=new Thread(() -> {
            int cont=0;
            while(!Thread.currentThread().isInterrupted()){
                try {
                    list_meteorito.stream().forEach(e->{e.Animar();});
                    if(cont%500==0){
                        list_meteorito.add(new Meteorito(Juego.this));
                        cont=0;
                        repaint();
                    }
                    cont++;
                    Thread.sleep(Meteorito.FuncionVelocidad(getScore()));
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch(ConcurrentModificationException ex2){
                    System.out.println("Error linea 71 clase Main");
                }
            }
        });
        hilo_speed=new Thread(speed);
        hilo_princ=new AdminHilos();
        hilo_meteo.start();
        hilo_speed.start();
        hilo_princ.start();
    }
    
    public final void MostrarVentana(int base, int altura) {
        ventana.add(this);
        ventana.setSize(base, altura);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d_main = (Graphics2D) g.create();
        Graphics2D g2d_stars = (Graphics2D) g.create();
        Graphics2D g2d_perso = (Graphics2D) g.create();
        Graphics2D g2d_meteo = (Graphics2D) g.create();
        Graphics2D g2d_ayuda = (Graphics2D) g.create();

        g2d_stars.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d_main.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d_perso.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d_meteo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ImageIcon img_fondo = new ImageIcon("src/images/galaxy.png");
        img_fondo.setImage(img_fondo.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH));
        g.drawImage(img_fondo.getImage(), 0, 0, this);

        star_animation.paint(g2d_stars);
        personaje.paint(g2d_perso);
        try {
            list_meteorito.stream().forEach(e->{e.paint(g2d_meteo);});
        } catch (ConcurrentModificationException e) {System.out.println("Error linea 108 clase Main");}
        speed.paint(g2d_ayuda);
        //VIDA
        {
            g2d_main.setColor(Color.GREEN);
            g2d_main.fillRect(20, 20, personaje.getVida() * 2, 30);
            g2d_main.setColor(Color.WHITE);
            g2d_main.setFont(new Font("Rockwell", 0, 18));
            g2d_main.drawString(personaje.getVida() + "", 30, 40);
            g2d_main.drawImage(img_heart.getImage(), 65, 25, this);
            g2d_main.setColor(Color.CYAN);
            g2d_main.setStroke(new BasicStroke(5));
            g2d_main.drawRect(20, 20, personaje.getMaxVida() * 2, 30);
        }
        //Velocidad
        {
            g2d_main.setColor(Color.WHITE);
            g2d_main.setFont(new Font("Rockwell", 0, 18));
            g2d_main.drawString("" + personaje.getSpeed(), 320, 40);
            g2d_main.drawImage(img_speed.getImage(), 300, 25, this);
        }
        //Puntaje
        {
            g2d_main.drawString("SCORE: "+getScore(), 420, 40);
        }
    }

    private class StarAnimation{
        private ImageIcon img_star;
        private int lado, speed_growth, max_lado;

        public StarAnimation(ImageIcon img_icon) {
            this.img_star = img_icon;
            lado = 8;
            speed_growth = 1;
            max_lado = lado;
        }
        public void paint(Graphics2D g2d) {
            ImageIcon new_img = new ImageIcon();
            new_img.setImage(img_star.getImage().getScaledInstance(lado, lado, Image.SCALE_SMOOTH));
            for (int i = 0; i < 50; i++) {
                g2d.drawImage(new_img.getImage(), (int) (Math.random() * getWidth()), (int) (Math.random() * getHeight()), ventana);
            }
        }

        public void Animar() {
            if (lado > max_lado) {
                speed_growth = -1;
            }
            if (lado <= 1) {
                speed_growth = 1;
            }
            lado += speed_growth;
        }
    }
    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score=score;
    }
}
