package spacedestruction;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/*@author Abel123*/
public class Personaje extends KeyAdapter{
    private ImageIcon spray;
    private int pos_x, pos_y, lado, speed, vida, angulo;
    private final int max_vida;
    private Juego main;

    public Personaje(Juego main) {
        this.spray = spray=new ImageIcon("src/images/astronave.png");
        this.lado=64;
        this.pos_x=800/2;
        this.pos_y=700/2;
        this.speed=1;
        this.vida=100;
        this.max_vida=vida;
        this.vida=100;
        this.main=main;
        this.angulo=0;
    }
    public void paint(Graphics2D g){
        ImageIcon new_img=new ImageIcon();
        new_img.setImage(spray.getImage().getScaledInstance(lado, lado, Image.SCALE_SMOOTH));
        g.rotate(Math.toRadians(angulo), (lado/2)+pos_x,(lado/2)+pos_y);
        g.drawImage(new_img.getImage(), pos_x, pos_y, main);
    }
    private void keyPressedGeneric(){
        spray=new ImageIcon("src/images/astronave2.png");
        if(pos_x<-lado/2){
            pos_x=main.getWidth()-(lado/2);
        }if(pos_x>main.getWidth()-(lado/2)){
            pos_x=-(lado/2);
        }if(pos_y<-lado/2){
            pos_y=main.getHeight()-(lado/2);
        }if(pos_y>main.getHeight()-(lado/2)){
            pos_y=-(lado/2);
        }
    }
    public int getVida() {
        return vida;
    }
    public void setVida(int vida) {
        if(vida<=100){
            this.vida = vida;
        }else{
            this.vida=max_vida;
        }
    }
    public int getMaxVida() {
        return max_vida;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public Rectangle getDimencion(){
        return new Rectangle(pos_x, pos_y, lado, lado);
    }
    public Point getPoint(){
        return new Point(pos_x,pos_y);
    }
    public void setPoint(Point pos){
        this.pos_x=pos.x;
        this.pos_y=pos.y;
    }
    @Override public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==38 || e.getKeyCode()==87){//arriba
            pos_y-=speed;
            angulo=0;
            keyPressedGeneric();
        }if(e.getKeyCode()==40 || e.getKeyCode()==83){//Abajo
            pos_y+=speed;
            angulo=180;
            keyPressedGeneric();
        }if(e.getKeyCode()==39 || e.getKeyCode()==68){//Izquierda
            pos_x+=speed;
            angulo=90;
            keyPressedGeneric();
        }if(e.getKeyCode()==37 || e.getKeyCode()==65){//derecha
            pos_x-=speed;
            angulo=270;
            keyPressedGeneric();
        }
    }
    @Override public void keyReleased(KeyEvent e){
        spray=new ImageIcon("src/images/astronave.png");
    }
}
