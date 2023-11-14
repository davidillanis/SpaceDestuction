package spacedestruction;

import Exceptions.SongException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * Clase para el control de varios sonidos.
 * Permite gestionar la reproducción, pausa y otras operaciones relacionadas con sonidos en formato .wav.
 * 
 * @author Abel123
 */
public class PluralSoundControl {
    /**
     * Arreglo de identificadores de sonido.
     */
    private String[] id_names;
    /**
     * Arreglo de archivos de sonido correspondientes a los identificadores.
     */
    private File[] file_songs;
    /**
     * Mapa que asocia identificadores de sonido con sus clips correspondientes.
     */
    public HashMap<String, Clip> map_clip = new HashMap<>();

    /**
     * Constructor de la clase PluralSoundControl.
     *
     * @param carpeta_song Directorio que contiene archivos de sonido .wav.
     * @throws SongException Excepción personalizada para errores relacionados con el sonido.
     */
    public PluralSoundControl(File carpeta_song) throws SongException {
        // Verifica si carpeta_song es un directorio y existe
        if (carpeta_song.isDirectory() && carpeta_song.exists()) {
            // Obtiene los nombres de archivo y archivos correspondientes a sonidos .wav
            id_names = carpeta_song.list((dir, name) -> name.contains(".wav"));
            file_songs = carpeta_song.listFiles((dir, name) -> name.contains(".wav"));
            
            try {
                // Crea clips y los asocia con los identificadores en el mapa
                Clip clip;
                for(int i=0; i<id_names.length; i++){
                    clip=AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(file_songs[i]));
                    map_clip.put(id_names[i], clip);
                }
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
                Logger.getLogger(PluralSoundControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // Lanza excepción si carpeta_song no es un directorio o no existe
            throw new SongException("Error con el Directorio o la carpeta no existe");
        }
    }
    /**
     * Inicia la reproducción de un sonido con el identificador especificado.
     *
     * @param id_name Identificador del sonido a reproducir.
     * @throws SongException Excepción si el identificador no existe.
     */
    public void startSong(String id_name) throws SongException {
        Clip clip = map_clip.get(id_name);
        if (map_clip.containsKey(id_name)) {
            clip.start();
        } else {
            throw new SongException("'startSong'->Error el id no existe");
        }
    }
    /**
     * Detiene y cierra la reproducción de un sonido con el identificador especificado.
     *
     * @param id_name Identificador del sonido a reiniciar.
     * @throws SongException Excepción si el identificador no existe o el clip es nulo.
     */
    public void restartSong(String id_name) throws SongException {
        Clip clip = map_clip.get(id_name);
        if (map_clip.get(id_name) != null) {
            clip.stop();
            clip.close();
            try {
                clip.open(AudioSystem.getAudioInputStream(getDirClip(id_name)));
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
                Logger.getLogger(PluralSoundControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new SongException("'restartSong'->error el id no existe o el clip tiene una valor nulo");
        }
    }
    /**
     * Cierra la reproducción de un sonido con el identificador especificado.
     *
     * @param id_name Identificador del sonido a cerrar.
     * @throws SongException Excepción si el identificador no existe o el clip es nulo.
     */
    public void closeSong(String id_name) throws SongException {
        Clip clip = map_clip.get(id_name);
        if (map_clip.get(id_name) != null) {
            clip.stop();
            clip.close();
        } else {
            throw new SongException("'restartSong'->Error al cerrar el sonido o el sonido ya está cerrado");
        }
    }
    /**
     * Pausa la reproducción de un sonido con el identificador especificado.
     *
     * @param id_name Identificador del sonido a pausar.
     * @throws SongException Excepción si el identificador no existe, es nulo o no está en ejecución.
     */
    public void pauseSong(String id_name) throws SongException {
        Clip clip = map_clip.get(id_name);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        } else {
            throw new SongException("'pauseSong'->Error el id no existe, es nulo o no está en corriendo");
        }
    }
    /**
     * Continúa la reproducción de un sonido pausado con el identificador especificado.
     *
     * @param id_name Identificador del sonido a reanudar.
     * @throws SongException Excepción si el identificador no existe, es nulo o ya está en ejecución.
     */
    public void resumeSong(String id_name) throws SongException {
        Clip clip = map_clip.get(id_name);
        if (clip != null && !clip.isRunning()) {
            clip.start();
        } else {
            throw new SongException("'resumeSong'->Error el id no existe, es nulo o ya está en corriendo");
        }
    }
    /**
     * Inicia la reproducción en bucle de un sonido con el identificador especificado.
     *
     * @param id_name Identificador del sonido a reproducir en bucle.
     * @param loopCount Número de repeticiones del bucle.
     * @throws SongException Excepción si el identificador no existe o es nulo.
     */
    public void playLooped(String id_name, int loopCount) throws SongException {
        Clip clip = map_clip.get(id_name);
        if (clip != null) {
            clip.loop(loopCount);
        } else {
            throw new SongException("'playLooped'->Error el id no existe o es nulo");
        }
    }
    /**
     * Retorna true si la música está sonando del 'HashMap' con su correspondiente id.
     *
     * @param id_name Identificador del sonido.
     * @return true si el sonido está en ejecución, false de lo contrario.
     */
    public boolean isPlayingSong(String id_name) {
        Clip clip = map_clip.get(id_name);
        return clip != null && clip.isRunning();
    }
    /**
     * Retorna la duración del sonido asociado al identificador especificado.
     *
     * @param id_name Identificador del sonido.
     * @return Duración del sonido en segundos.
     */
    public long getSongDuration(String id_name) {
        Clip clip = map_clip.get(id_name);
        return clip != null ? clip.getMicrosecondLength() / 1_000_000 : 0;
    }
    /**
     * Retorna los identificadores de sonido disponibles.
     *
     * @return Arreglo de identificadores de sonido.
     */
    public String[] getId_names() {
        return id_names;
    }
    /**
     * Retorna los archivos de sonido disponibles.
     *
     * @return Arreglo de archivos de sonido.
     */
    public File[] getFilesSong() {
        return file_songs;
    }
    /**
     * Retorna el mapa que asocia identificadores de sonido con clips.
     *
     * @return Mapa de identificadores de sonido y clips.
     */
    public HashMap<String, Clip> getMap_clip() {
        return map_clip;
    }
    /**
     * Retorna el clip asociado al identificador especificado.
     *
     * @param id_name Identificador del sonido.
     * @return Clip asociado al identificador.
     */
    public Clip getClip(String id_name) {
        return map_clip.get(id_name);
    }
    /**
     * Retorna la dirección relativa de un sonido mediante su identificador.
     *
     * @param id_name Identificador del sonido.
     * @return Archivo de sonido correspondiente al identificador.
     */
    public File getDirClip(String id_name) {
        Clip clip = map_clip.get(id_name);
        if (clip != null) {
            int index = Arrays.asList(id_names).indexOf(id_name);
            if (index != -1) {
                return file_songs[index];
            }
        }
        return null; // Si no se encuentra el clip o el id_name, retorna null
    }
}