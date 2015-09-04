/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aroulette;

/**
 *
 * @author aitatxoborja
 */
public class LinearInverseProbability implements ProbabilityGenerator{

    private double _exp;
    
    public LinearInverseProbability (double exp)
    {
        _exp=exp;
    }
    
    
    public double[]  generateRoulette (double [] points) {
        double [] res = new double [points.length];
        double sum = 0;
        double min = points[0];
        for (int f = 0; f < points.length; f++) 
            if (points[f] < min) 
                min = points[f];
        
        for (int p = 0; p < points.length; p++) 
        {
            res[p] = 1 / (Math.pow(points[p] - min, _exp) + 1);// The +1 is to avoid 1/
            sum += res[p];
        }
        
        for (int f = 0; f < res.length; f++)
                res[f] = res[f] / sum * 361;
        
        return res;
    }

    public void setExponent(double exp)
    {
        _exp=exp;
    }
}
