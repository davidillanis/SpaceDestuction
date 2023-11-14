package spacedestruction;

import Exceptions.SongException;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import static spacedestruction.POSICION.abajo;
import static spacedestruction.POSICION.arriba;
import static spacedestruction.POSICION.derecha;
import static spacedestruction.POSICION.izquierda;

/*@author Abel123*/
enum POSICION{arriba, abajo, izquierda, derecha};
public class Meteorito extends Obstaculo{
    private int danio;
    private int dir_x, dir_y;
    private POSICION pos=null;
    
    public Meteorito(Juego main) {
        super(new ImageIcon("src/images/meteorite.png"), (int)(Math.random()*32+16), main);
        this.danio=4;
        this.dir_x=2;
        this.dir_y=2;
    }
    public static int FuncionVelocidad(double x){
        if(x>50){
            return 1;
        }
        double y=-x+51;
        return (int)y;
    }
    public boolean IsCoalicion(Rectangle rect){
        return getDimencion().intersects(rect);
   }
    private POSICION PosicicionObjeto(Rectangle rect1, Rectangle rect2) {
        if (rect1.getMaxX() < rect2.getMinX()) {
            pos=POSICION.izquierda;
        } else if (rect1.getMinX() > rect2.getMaxX()) {
            pos=POSICION.derecha;
        } else if (rect1.getMaxY() < rect2.getMinY()) {
            pos=POSICION.arriba;
        } else if (rect1.getMinY() > rect2.getMaxY()) {
            pos=POSICION.abajo;
        }
        return pos;
    }
    public int getDanio() {
        return danio;
    }
    public void setDanio(int danio) {
        this.danio = danio;
    }
    @Override public void Animar(){
        PosicicionObjeto(getDimencion(), getMain().personaje.getDimencion());
        if(IsCoalicion(getMain().personaje.getDimencion())){
            try{
            getMain().sonido.restartSong("inpacto1.wav");
            getMain().sonido.startSong("inpacto1.wav");
            
            }catch(SongException ex){System.out.println(ex);}
            getMain().personaje.setVida(getMain().personaje.getVida()-danio);
            switch (pos) {//el 10 es para aumentar la velocidad del meteorito al hacer una coalicion
                case arriba:
                    dir_y=-Math.abs(dir_y+10);
                    break;
                case abajo:
                    dir_y=Math.abs(dir_y+10);
                    break;
                case derecha:
                    dir_x=Math.abs(dir_x+10);
                    break;
                case izquierda:
                    dir_x=-Math.abs(dir_x+10);
                    break;
                default:
            }
        }
        
        if((pos_x+getDimencion().width>getMain().getWidth() || pos_x<0) && getMain().getWidth()!=0){
            dir_x*=-1;
        }if((pos_y+getDimencion().height>getMain().getHeight()|| pos_y<0) && getMain().getHeight()!=0){
            dir_y*=-1;
        }
        pos_x+=dir_x;
        pos_y+=dir_y;
        //esta condicionales redicira su velocidad hasta que se regule
        if(dir_y!=2 && dir_y>0){
            getMain().personaje.setPoint(new Point(getMain().personaje.getPoint().x, getMain().personaje.getPoint().y-2));
            dir_y-=1;
        }if(dir_y!=-2 && dir_y<0){
            getMain().personaje.setPoint(new Point(getMain().personaje.getPoint().x, getMain().personaje.getPoint().y+2));
            dir_y+=1;
        }if(dir_x!=2 && dir_x>0){
            getMain().personaje.setPoint(new Point(getMain().personaje.getPoint().x-2, getMain().personaje.getPoint().y));
            dir_x-=1;
        }if(dir_x!=-2 && dir_x<0){
            getMain().personaje.setPoint(new Point(getMain().personaje.getPoint().x+2, getMain().personaje.getPoint().y));
            dir_x+=1;
        }
        getMain().repaint();
    }
    
}
