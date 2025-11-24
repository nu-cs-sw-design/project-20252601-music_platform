package controller.engine;

/**
 * Strategy interface for instruments.
 * Different instruments (piano, guitar, drums, etc.) can implement this.
 */
public interface Instrument {

    void noteOn(int pitch, int vel);

    void noteOff(int pitch);

    void close();
}
