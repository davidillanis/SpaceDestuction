package spacedestruction;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/*@author Abel123*/
abstract class Obstaculo {
    private ImageIcon spray;
    public int pos_x, pos_y;
    private int lado;
    private Juego main;
    public Obstaculo(ImageIcon spray, int lado, Juego main) {
        this.spray = spray;
        this.lado = lado;
        this.main=main;
        this.pos_x=100;
        this.pos_y=100;
    }
    public Obstaculo(ImageIcon spray, int lado, int pos_x, int pos_y, Juego main) {
        this.spray = spray;
        this.lado = lado;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.main = main;
    }
    public void paint(Graphics2D g){
        spray.setImage(spray.getImage().getScaledInstance(lado, lado, Image.SCALE_SMOOTH));
        g.drawImage(spray.getImage(), pos_x, pos_y, main);
    }
    public Rectangle getDimencion(){
        return new Rectangle(pos_x, pos_y, lado, lado);
    }
    public abstract void Animar();
    public Juego getMain() {
        return main;
    }
    public ImageIcon getSpray() {
        return spray;
    }
    public void setSpray(ImageIcon spray) {
        this.spray = spray;
    }
}
