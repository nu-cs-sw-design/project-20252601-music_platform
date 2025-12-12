package controller.engine;

import model.Pitch;
import model.Velocity;

public interface Instrument {
    void noteOn(Pitch pitch, Velocity velocity);
    void noteOff(Pitch pitch);
    void close();
}
