package spacedestruction;

import javax.swing.JOptionPane;

/*@author Abel123*/
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Juego j=new Juego();
        j.MostrarVentana(900, 700);
        while (true) {            
            Thread.sleep(100);
            if(j.Is_game_over){
                int obc=JOptionPane.showConfirmDialog(j, "Fin del Juego, desea reiniciar","Game Over",0);
                if(obc==0){//si
                    j.ventana.dispose();
                    j=new Juego();
                    j.MostrarVentana(900, 700);
                }else if(obc==1){//no
                    j=null;
                    System.exit(0);
                }
            }
        }
        
    }
}
