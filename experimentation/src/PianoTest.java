public class PianoTest {
    public static void main(String[] args) throws Exception {
        Instrument piano = new Instrument();

        int C4 = 60;
        int velocity = 90;

        System.out.println("Playing C4...");
        piano.noteOn(C4, velocity);

        Thread.sleep(1000);

        piano.noteOff(C4);
        piano.close();

        System.out.println("Done.");
    }
}
