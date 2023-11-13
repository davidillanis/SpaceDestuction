package spacedestruction;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*@author Abel123*/
enum SONG_OBTION{
    derrota(1,"src/song/derrota.wav"), disparo(2,"src/song/disparo.wav"), fondo1(3,"src/song/fondo1.wav"), 
    fondo2(4,"src/song/fondo2.wav"), inpacto1(5,"src/song/inpacto1.wav"), item1(6,"src/song/item1.wav"), 
    item2(7,"src/song/item2.wav"), item3(8,"src/song/item3.wav"), rebote(9,"src/song/rebote.wav"), 
    victoria(10,"src/song/victoria.wav");
    private final int id;
    private final String dir_song;

    private SONG_OBTION(int id, String dir_song) {
        this.id = id;
        this.dir_song = dir_song;
    }
    public int getId() {
        return id;
    }
    public String getDirSong() {
        return dir_song;
    }
};


public class Song {
    private final HashMap<Integer, Clip>map_song=new HashMap<>();
    
    @SuppressWarnings("NonPublicExported")
    public void NewStartSong(SONG_OBTION obc){
        StopSong(obc);
        if(!map_song.containsKey(obc.getId())){
            try {
                File file_song=new File(obc.getDirSong());
                Clip clip=AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(file_song));
                clip.start();
                map_song.put(obc.getId(), clip);
                file_song=null;
                clip=null;
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
                System.out.println("Error Song->NewStartSong()"+ex);
            }
        }else{
            System.out.println("La musica se esta reproduciendo");
        }
    }
    
    @SuppressWarnings("NonPublicExported")
    public void StopSong(SONG_OBTION obc){
        if(map_song.containsKey(obc.getId())){
            Clip clip=map_song.get(obc.getId());
            clip.stop();
            clip=null;
            map_song.remove(obc.getId());
        }else{
            System.out.println("La Musica no existe:Song->StopSong()");
        }
    }
    
    @SuppressWarnings("NonPublicExported")//posible error al ejecutar 'StopSong()'
    public void playLooped(SONG_OBTION obc, int numLoops) {
        Clip clip = map_song.get(obc.getId());
        if (clip != null && clip.isOpen()) {
            clip.loop(numLoops);
        }
    }
    
    @SuppressWarnings("NonPublicExported")
    public void pauseSong(SONG_OBTION obc) {
        Clip clip = map_song.get(obc.getId());
        if (clip != null && clip.isOpen()) {
            clip.stop();
        }
    }
    
    @SuppressWarnings("NonPublicExported")
    public void resumeSong(SONG_OBTION obc) {
        Clip clip = map_song.get(obc.getId());
        if (clip != null && clip.isOpen()) {
            clip.start();
        }
    }
    
    @SuppressWarnings("NonPublicExported")
    public boolean isPlayingMusic(SONG_OBTION obc) {
        Clip clip = map_song.get(obc.getId());
        return clip != null && clip.isOpen();
    }
    
    @SuppressWarnings("NonPublicExported")
    public boolean HasSongFinished(SONG_OBTION obc) {//si la musica termino de reproducirce
        Clip clip = map_song.get(obc.getId());
        if (clip != null && clip.isOpen()) {
            long currentPosition = clip.getMicrosecondPosition();
            long totalDuration = clip.getMicrosecondLength();
            return currentPosition >= totalDuration;
        }
        return false;
    }

    
    public HashMap<Integer, Clip> getMap_song() {
        return map_song;
    }
    
    //PlayWithVolume(){} //Volumen
    //getSongDuration(){}//duracion
}
/*ayuda para obtener todos los archibos con extencion especifica de una carpeta
private File []file_music;
public contructor(File dir_song) {
    file_music=dir_song.listFiles((dir, name) -> name.endsWith(".wav"));
    Arrays.stream(file_music).forEach(System.out::println);
}
public contructor() {
    this(new File("src/song"));
}
*/