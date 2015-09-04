package aroulette;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.prefs.BackingStoreException;


public class ARoulette extends Thread{

    private RouletteGUI _mainWindow;
    private RoulettePanel _roulette;
    private Object _lock = new Object();
    private ConfigurationManager _config = new ConfigurationManager();

    public void runRulette()
    {        
        Random rnd = new Random(System.currentTimeMillis());
        double impulse = _config.getImpulse() + rnd.nextDouble()*50;
        double resistence = _config.getResistance();
        int accelerationTime = _config.ACCELERATIONTIME;
        double speedFactor = _config.SPEEDFACTOR;
        double speed = _config.INITIALSPEED;
        
        //Acceleration                               
        for (int t=0; t<accelerationTime && _mainWindow.getState() == RouletteGUI.RUNNING; t++)
        {
            speed += impulse;
            try {
                _mainWindow.moveRoulette((int)speed/10);
                this.sleep(40);
            } catch (InterruptedException ex1) {}
        }
        
        //Deceleration
        int t=0;
        while (speed > 10 && _mainWindow.getState() == RouletteGUI.RUNNING)
        {
            t++;
            double time = ((double)t)/25;
            speed -= resistence*speed/speedFactor*time*time;
            if (speed>0)
            {
                try {
                    _mainWindow.moveRoulette((int)speed/10);
                    this.sleep(40);
                } catch (InterruptedException ex1) {}
            }
        }
        
        _mainWindow.rouletteFinished();
    }
    
    public ARoulette() throws InterruptedException, IOException, BackingStoreException {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (_config.getAppearance().equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}
        StringTokenizer st = new StringTokenizer(_config.getLocale(),"_");
        String language = st.nextToken();
        String country = st.nextToken();
        Locale.setDefault(new Locale(language,country));
     
        String path = _config.getDefaultDir();
        if (path==null || !new File(path).exists())  _config.setDefaultDir(System.getProperty("user.home"));
        
        _mainWindow = new RouletteGUI(_lock,_config);
        _roulette = _mainWindow.getRoulette();
        _mainWindow.setVisible(true);

        while (true) 
        {
            synchronized (_lock) 
            {
                _lock.wait();
                if(_mainWindow.getState() == RouletteGUI.RUNNING)
                        runRulette();
            }
        }
    }
    
    
    //Method to be invoqued to finish the program
    public void closeProgram() {
        _mainWindow.dispose();
        _config.saveConfig();
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ARoulette r = new ARoulette();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
