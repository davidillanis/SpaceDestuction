package spacedestruction;

import Exceptions.SongException;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/*@author Abel123*/
enum OBCIONES{speed, heart, reduce_speed};
public class Obstaculo2 extends Obstaculo implements Runnable{
    private boolean is_run;
    private OBCIONES obc;
    private int tiempo_espera;
    
    public Obstaculo2(Juego main, ImageIcon spray, OBCIONES obc) {
        super(spray, 32, main);
        this.obc=obc;
        this.is_run=true;
        this.tiempo_espera=0;
        pos_x=0;
        pos_y=0;
    }
    public void Stop(){
        int aleatorio=(int)(Math.random()*3);//0,1,2
        pos_x=(int)(Math.random()*getMain().getWidth());
        pos_y=-getDimencion().height;
        is_run=false;
        switch (aleatorio) {
            case 0:
                setSpray(new ImageIcon("src/images/heart.png"));
                obc=OBCIONES.heart;
                break;
            case 1:
                setSpray(new ImageIcon("src/images/speed.png"));
                obc=OBCIONES.speed;
                break;
            case 2:
                setSpray(new ImageIcon("src/images/reduce_speed.png"));
                obc=OBCIONES.reduce_speed;
                break;
            default:
                break;
        }
    }
    private int FuncionVelocidad(int x){
        if(x>50){
            return 1;
        }
        double y=-x+51;
        return (int)y;
    }
    private int FuncionPosicion(int x){
        int var_y=(getMain().getHeight()/2)-20;
        double var_x=(getMain().getWidth()/16);
        double y=(var_y*Math.sin(x/var_x))-var_y;
        return (int)(y*-1);
    }
    public void LogicaCoalicion() throws SongException{
        switch (obc) {
            case speed:
                if(getMain().personaje.getSpeed()<50){
                    getMain().personaje.setSpeed(getMain().personaje.getSpeed()+1);//aumentar la velocidad del personaje
                    
                    getMain().sonido.restartSong("item1.wav");
                    getMain().sonido.startSong("item1.wav");//SONIDO
                }
                break;
            case heart:
                if(getMain().personaje.getVida()<=100){
                    getMain().personaje.setVida(getMain().personaje.getVida()+5);//aumentar la vida del personaje
                    
                    getMain().sonido.restartSong("item2.wav");
                    getMain().sonido.startSong("item2.wav");//SONIDO
                }
                break;
            case reduce_speed:
                
                if(getMain().personaje.getSpeed()>1){
                    getMain().personaje.setSpeed(getMain().personaje.getSpeed()-1);//reducir la velocidad del personaje
                    
                    getMain().sonido.restartSong("item2.wav");
                    getMain().sonido.startSong("item2.wav");//SONIDO
                }else{
                    getMain().personaje.setSpeed(1);
                }
                break;
            default:break;
        }
    }
    public boolean IsCoalicion(Rectangle rect){
        return getDimencion().intersects(rect);
    }
    public boolean isRun(){
        return is_run;
    }
    public void Star(){
        this.is_run=true;
    }
    @Override public void Animar() {
        if(is_run){
            switch (obc) {
                case speed:
                    pos_x++;
                    pos_y=FuncionPosicion(pos_x);
                    break;
                case heart:
                    pos_y++;
                    pos_x=FuncionPosicion(pos_y);
                    break;
                case reduce_speed:
                    pos_y+=2;
                    pos_x=FuncionPosicion(pos_y);
                    break;
                default:
                    throw new AssertionError();
            }
            if((pos_x>getMain().getWidth() || pos_y>getMain().getHeight() || pos_x<0) && (getMain().getHeight()!=0 && getMain().getWidth()!=0)){
                Stop();
            }
            getMain().repaint();
        }
    }
    @Override public void run(){
        while (!Thread.currentThread().isInterrupted()) {            
            try {
                Animar();
                if(IsCoalicion(getMain().personaje.getDimencion())){
                    LogicaCoalicion();
                    Stop();
                }if(!isRun()){//tiempo de espera ______
                    tiempo_espera++;
                    if(tiempo_espera%100==0){
                        Star();
                        tiempo_espera=0;
                    }
                }//____________________________________
                Thread.sleep(FuncionVelocidad(getMain().personaje.getSpeed()));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch(SongException e){
                System.out.println(e);
            }
        }
    }
}
