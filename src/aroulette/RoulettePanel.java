/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aroulette;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author aitatxoborja
 */
public class RoulettePanel extends JPanel {

    private String[] _name = {java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("STUDENT 1"), 
        java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("STUDENT 2"), 
        java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("STUDENT 3"), 
        java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("STUDENT 4"), 
        java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("STUDENT 5")};
    private int[] _points = {4, 2, 0, 1, 1};
    private double[] _puntuation = {2, 1, 0, -1}; //This does not depend on which students are selected
    private float _currentPosition; //In degrees, it indicates to where the roulette points
    private int[] _filter = {0, 1, 2, 3, 4};  //Selected students
    private float[] _arcLengths;
    private Color [] _colorList;
    private double _exp = 2;
    private double _rouletteSize = 0.8;
    private ProbabilityGenerator _pGen;
    
    
    //Construction of the roulette
    public RoulettePanel(ProbabilityGenerator pgen) {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        //Initialize with a random position
        _currentPosition = r.nextInt(360);
        _pGen = pgen;
        this.updateRoulette();
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (_arcLengths == null || _arcLengths.length < 2) {
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHints(rh);
            ((Graphics2D) g).drawString(java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("SELECT AT LEAST TWO STUDENTS TO CREATE A ROULETTE"), 0, this.getHeight() / 2);
        } else {
            int w = this.getWidth();
            int h = this.getHeight();
            int size = (int) (((double) Math.min(w, h)) * _rouletteSize);

            int minX = w / 2 - size / 2;
            int minY = h / 2 - size / 2;
            
            Graphics2D g2d = (Graphics2D) g;

            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(rh);

            Polygon arrow = new Polygon();
            int xpos = minX + size + 10;
            int ypos = minY + size / 2;
            arrow.addPoint(xpos, ypos);
            arrow.addPoint(xpos + 10, ypos - 10);
            arrow.addPoint(xpos + 10, ypos - 5);
            arrow.addPoint(xpos + 25, ypos - 5);
            arrow.addPoint(xpos + 25, ypos + 5);
            arrow.addPoint(xpos + 10, ypos + 5);
            arrow.addPoint(xpos + 10, ypos + 10);
            arrow.addPoint(xpos, ypos);

            g2d.fill(arrow);


            float aux_pos = -1 * _currentPosition % 360;
            int col_index = 0;
            for (int s = 0; s < _filter.length; s++) {
                g2d.setColor(_colorList[col_index]);
                col_index = (col_index + 1) % _colorList.length;
                g2d.fill(new Arc2D.Float(minX, minY, size, size, aux_pos, _arcLengths[s], Arc2D.Float.PIE));
                aux_pos = (aux_pos + _arcLengths[s]) % 360;
            }
        }
    }
    
    public String[] getNames() {
        return _name;
    }

    public int getSelected() {
        int res = -1;
        if (_arcLengths != null) {
            int index = 0;
            float sum = _arcLengths[index];
            while (sum < _currentPosition) {
                index++;
                sum += _arcLengths[index];
            }

            res = _filter[index];
        }
        return res;
    }

    public void updatePoints(int stdIndex, int ptIndex) {
        _points[stdIndex] += _puntuation[ptIndex];
        int selected = this.getSelected();
        this.updateRoulette();
        this.setSelected(selected);//As the distribution of the roulette has changed, we need to re-center it
    }

    // Function to load a student list from a csv file
    public void loadList(String path) throws FileNotFoundException, ParsingException {
        BufferedReader in = new BufferedReader(new FileReader(path));
        String line;
        String [] newname;
        int [] newpoints;
        boolean pointsRead = true;
        try {
            line = in.readLine();
            //Check whether we only have names or names and starting points
            String[] spt = line.split(",");
            int i = 0;
            newname = new String[1];
            newpoints = new int[1];
            newname[i] = spt[0];
            if (spt.length == 1) {
                pointsRead = false;
            } else {
                pointsRead = true;
                newpoints[i] = Integer.parseInt(spt[1]);
            }
            line = in.readLine();
            i++;
            while (line != null) {
                spt = line.split(",");
                newname = append(spt[0],newname);
                if (pointsRead)
                    newpoints = append(Integer.parseInt(spt[1]),newpoints);
                line = in.readLine();
                i++;
            }
            _name=newname;
            _points=newpoints;
            _filter = new int[_name.length];
            for (int f = 0; f < _filter.length; f++) {
                _filter[f] = f;
            }
            this.updateRoulette();
        } catch (Exception ex) {
            throw new ParsingException(java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").
                    getString("PROBLEMS FOUND WHILE READING THE STUDENTS LIST. CHECK THE FILE AT {0}"));
        }
    }

    //Method to save the list in a given file
    public void saveList(String path) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(path);
        out.print(this);
        out.close();
    }

    public void setFilter(int[] filter) {
        _filter = filter;
        updateRoulette();
        this.setPalette();
   
    }

    private void setPalette() {
        if (_filter.length > 1) {
            Color c1 = new Color(0, 112, 250);
            Color c2 = new Color(237, 250, 0);
            Color c3 = new Color(250, 0, 112);
            Color c4 = new Color(112, 250, 0);
            Color c5 = new Color(0, 237, 250);
            Color c6 = new Color(250, 0, 237);
            Color[] wholePalette = {c1,c2,c3,c4,c5,c6};

            int numColors = 2;
            while (_filter.length % numColors == 1) {
                numColors++;
            }
            _colorList = new Color[numColors];
            for (int c = 0; c < _colorList.length; c++) {
                _colorList[c] = wholePalette[c];
            }
        }
    }

    
    //We move the roulette "angle" degrees
    public void move(float angle) {
        _currentPosition += angle;
        //Current position is always lower than 360
        _currentPosition = _currentPosition % 360;
    }

    //Add a new student without points
    private String [] append(String name, String [] names) {
        String[] aux = new String[names.length + 1];
        for (int a = 0; a < names.length; a++) {
            aux[a] = names[a];
        }
        aux[aux.length - 1] = name;
        return aux;
    }

    //Add a student with some points
    private int [] append(int pt, int[] pts) {
        int[] aux = new int[pts.length + 1];
        for (int a = 0; a < pts.length; a++) {
            aux[a] = pts[a];
        }
        aux[aux.length - 1] = pt;
        return aux;
    }

    //Empty the list
    public void clearList() {
        _name = null;
        _points = null;
    }

    //Remove all the points
    public void clearPoints() {
        _points = new int[_name.length];
        for (int i = 0; i < _points.length; i++) {
            _points[i] = 0;
        }
    }

    //Function to print the list as csv
    public String toString() {
        String out = "";
        for (int l = 0; l < _name.length; l++) {
            out += _name[l] + "," + _points[l] + "\n";
        }
        return out;
    }
    
    public void setSelected(int index) {
        //If the index is not in the filter, then we do nothing
        if (index < _name.length & index >= 0) {
            int aux_ind = 0;
            boolean valid = false;
            while (!valid  && aux_ind<_filter.length) {
                if (_filter[aux_ind] == index)
                {
                    valid=true;
                }else{
                    aux_ind++;
                }
            }
            if (valid) {
                float start = 0;
                for (int i = 0; i < aux_ind; i++) {
                    start += _arcLengths[i];
                }
                float end = start + _arcLengths[aux_ind];
                _currentPosition = (start + end) / 2;
            }
        }
    }

    public void updateRoulette() {
        if (_filter.length == 0) {
            _arcLengths = null;
        } else {
            double[] aux = new double[_filter.length];
            for (int a=0; a<aux.length; a++)
                aux[a] = _points[_filter[a]];
            
            double [] prob = _pGen.generateRoulette(aux);

            this._arcLengths = new float[_filter.length];
            for (int i = 0; i < this._arcLengths.length; i++) {
                this._arcLengths[i] = (float) prob[i];
            }
        }
    }

    public void setPosition(int pos) {
        //Ensure that the current position is between 0 and 359
        _currentPosition = pos % 360;
        if (_currentPosition < 0) {
            _currentPosition += 360;
        }
    }

    public void setRandomPosition() {
        Random rnd = new Random(System.currentTimeMillis());
        setPosition(rnd.nextInt(360));
    }
}
