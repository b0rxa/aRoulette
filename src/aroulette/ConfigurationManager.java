/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aroulette;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author aitatxoborja
 */
public class ConfigurationManager {
    
    Properties _properties = new Properties();
    String _path =  System.getProperty("user.home") + "/.aRoulette.conf";
    
    
    public final int ACCELERATIONTIME = 10;
    public final double SPEEDFACTOR = 10000;
    public final double INITIALSPEED = 100;
    
    public ConfigurationManager ()
    {   
        System.out.println(_path);
        try {
            if (!new File(_path).exists())
                _properties.load(getClass().getClassLoader().getResourceAsStream("aroulette/resources/roulette.conf"));
            else
                _properties.load(new FileInputStream(_path));
        } catch (IOException ex1) {}
    }
    
    public String getLocale ()
    {
        return _properties.getProperty("language");
    }
    
    public String getAppearance ()
    {
        return _properties.getProperty("appearance");
    }
    
    public String getDefaultDir ()
    {
        String dir = _properties.getProperty("default_directory");
        if (!new File (dir).exists())
            dir = System.getProperty("home.dir");
        return dir;
    }
    
    public double getExponent ()
    {
        return Double.parseDouble(_properties.getProperty("exponent"));
    }
    
    public double getImpulse ()
    {
        return Double.parseDouble(_properties.getProperty("impulse"));
    }
    
    public double getResistance ()
    {
        return Double.parseDouble(_properties.getProperty("resistance"));
    }

    public void setLocale (String locale)
    {
        _properties.setProperty("language",locale);
    }

    public void setAppearance (String appearance)
    {
        _properties.setProperty("appearance",appearance);
    }

    public void setDefaultDir (String dir)
    {
        if (new File (dir).exists())
            _properties.setProperty("default_directory",dir);
    }

    public void setExponent (double exp)
    {
        _properties.setProperty("exponent",""+exp);
    }

    public void setImpulse (double imp)
    {
        _properties.setProperty("impulse",""+imp);
    }

    public void setResistance (double res)
    {
        _properties.setProperty("resistance",""+res);
    }
    
    public void saveConfig ()
    {
        try {
            _properties.store(new FileOutputStream(_path), "");
        } catch (IOException ex) {}
    }
    
    public String [] getLanguages ()
    {
        String [] locales = {"en_GB","es_ES", "eu_ES"};
        return locales;
    }
    
    public String [] getLookAndFeelOptions ()
    {
        LookAndFeelInfo [] looks = UIManager.getInstalledLookAndFeels();
        String [] result = new String [looks.length];
        for (int i=0; i<looks.length; i++)
            result[i] = looks[i].getName();
        return result;
    }
    
    public String getRunTimeInterval ()
    {
        String inter = "";
        
        double impulse_min = getImpulse();
        double impulse_max = impulse_min + 50;
        double resistence = getResistance();
        int accelerationTime = ACCELERATIONTIME;
        double speedFactor = SPEEDFACTOR;
        double speed_min = INITIALSPEED + accelerationTime*impulse_min;
        double speed_max = INITIALSPEED + accelerationTime*impulse_max;
        
        //Deceleration
        int t_max=0;
        int t_min=0;
            
        while (speed_max > 10)
        {
            t_max++;
            double time = ((double)t_max)/25;
            speed_max -= resistence*speed_max/speedFactor*time*time;
            if (speed_min>10) 
            {
                t_min++;
                speed_min -= resistence*speed_min/speedFactor*time*time;
            }
        }

        inter += ((double)(accelerationTime + t_min))/25 + " - " + ((double)(accelerationTime + t_max))/25 + " s";
        
        return inter;
    }
}
