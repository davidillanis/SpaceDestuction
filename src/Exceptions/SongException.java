package Exceptions;

/*@author Abel123*/
/**
 * Excepción personalizada para manejar errores relacionados con el sonido.
 */
public class SongException extends Exception {

    /**
     * Crea una nueva instancia de SongException con el mensaje de error
     * especificado.
     *
     * @param error El mensaje de error.
     */
    public SongException(String error) {
        super(error);
    }
    /**
     * Crea una nueva instancia de SongException con el mensaje de error y la causa especificados.
     *
     * @param string El mensaje de error.
     * @param thrwbl La causa del error.
     */
    public SongException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
    
}
