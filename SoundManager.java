/*
 * Created on 2007/04/07
 */

public class SoundManager {
    private static final String[] bgmNames = {"normal","battle","ending","boss"};
    
    private static final String[] seNames = {"attack","enemy_attack","beep","decide_beep","back_beep","stairs","winbattle"};

    // MIDI
    MidiEngine midiEngine = new MidiEngine();
    // WAVE
    WaveEngine waveEngine = new WaveEngine();

    public SoundManager() {
        loadSound();
    }
    
    /**
     * BGMを再生
     * 
     * @param bgmName BGM名
     */
    public void playBGM(String bgmName) {
        midiEngine.play(bgmName);
    }
    
    /**
     * 効果音を再生
     * 
     * @param seName 効果音名
     */
    public void playSE(String seName) {
        waveEngine.play(seName);
    }
    
    /**
     * サウンドをロード
     *
     */
    private void loadSound() {
        // BGMをロード
        for (int i=0; i<bgmNames.length; i++) {
            midiEngine.load(bgmNames[i], "bgm/" + bgmNames[i] + ".mid");
        }
        
        // 効果音をロード
        for (int i=0; i<seNames.length; i++) {
            waveEngine.load(seNames[i], "se/" + seNames[i] + ".wav");
        }
    }
}
