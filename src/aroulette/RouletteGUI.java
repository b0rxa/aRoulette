/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aroulette;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author aitatxoborja
 */
public class RouletteGUI extends javax.swing.JFrame {

    private Object _holder; //For synchronization with the application
    private String _currentDir;
    private String _currentListFile; //File from where the current list has been loaded (null if no list has been loaded=
    private boolean _fileModified = false; //Flag to know whether the file has been modified
    private ConfigurationManager _config;
    private ConfigWindow cw;
    private ProbabilityGenerator _pgen;
    //Definition of the state of the roulette
    private int _state = this.IDLE;
    public final int IDLE = 0;
    public static final int RUNNING = 1;
    public static final int STOPPRESSED = 2;
    

    
    /**
     * Creates new form RouletteGUI
     */
    public RouletteGUI(Object holder, ConfigurationManager config) {
        _holder=holder; 
        _config=config;
        _currentDir = _config.getDefaultDir();
        _pgen = new LinearInverseProbability(_config.getExponent());
        
        initComponents();
        cw = new ConfigWindow(_config);
        this.refresh();
        //Intercept the closing of the window to check the file status
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            doClose();}});
        
    }

    //This function is called when the application is going to close
    private void doClose ()
    {
        if (_fileModified && _currentListFile!=null)
        {
            int selection = JOptionPane.showConfirmDialog(this, java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("THE FILE HAS BEEN MODIFIED SINCE IT WAS OPENED. DO YOU WANT TO SAVE IT?"),
                    java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("WARNING"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (selection == JOptionPane.YES_OPTION){
                boolean ok = this.saveFile();
                if (ok)
                {
                    this.dispose();
                    _config.saveConfig();
                    System.exit(0);
                }
            }else if (selection == JOptionPane.NO_OPTION){
                synchronized(_holder)
                {
                    this.dispose();
                    _config.saveConfig();
                    System.exit(0);
                }
            }
                
        }else{
            this.dispose();
            _config.saveConfig();
            System.exit(0);
        }
    }
    
    //Method to pass the roulette to the application
    public RoulettePanel getRoulette ()
    {
        return (RoulettePanel)jPanelRoulette;
    }
    
    //For the comunication with the application
    public int getState ()
    {
        return _state;
    }
    
    //Method to run when the roulette has finished running
    public void rouletteFinished()
    {
        _state = this.IDLE;
        jListNames.setEnabled(true);
        jButtonInvertSelection.setEnabled(true);
        jButtonSelectAll.setEnabled(true);
        jButtonSelectNone.setEnabled(true);
        jButtonUpdate.setEnabled(true);
        jComboBoxPuntuation.setEnabled(true);
        jComboBoxSelectedName.setEnabled(true);
        int selected = this.getRoulette().getSelected();
        if (selected >= 0)
            jComboBoxSelectedName.setSelectedIndex(selected);
        this.repaint();
    }
    
    //Method to start the roulette (set as unable some controls, etc.)
    private void startRoulette()
    {
        jListNames.setEnabled(false);
        jButtonInvertSelection.setEnabled(false);
        jButtonSelectAll.setEnabled(false);
        jButtonSelectNone.setEnabled(false);
        jButtonUpdate.setEnabled(false);
        jComboBoxPuntuation.setEnabled(false);
        jComboBoxSelectedName.setEnabled(false);
        synchronized(_holder)
        {
            _state = this.RUNNING;
            _holder.notifyAll();
        }
    }
    
    // Returns true when ok is pressed and false in any other case
    private boolean showMessege (String mssg)
    {
        int selection = JOptionPane.showConfirmDialog(this, mssg,
                    java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("WARNING"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        return selection == JOptionPane.OK_OPTION;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListNames = new javax.swing.JList();
        jButtonSelectAll = new javax.swing.JButton();
        jButtonInvertSelection = new javax.swing.JButton();
        jButtonSelectNone = new javax.swing.JButton();
        jComboBoxSelectedName = new javax.swing.JComboBox();
        jPanelRoulette = new RoulettePanel(_pgen);
        jLabelSelectedName = new javax.swing.JLabel();
        jButtonRun = new javax.swing.JButton();
        jButtonStop = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();
        jComboBoxPuntuation = new javax.swing.JComboBox();
        jButtonLookFor = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemLoadList = new javax.swing.JMenuItem();
        jMenuItemSaveList = new javax.swing.JMenuItem();
        jMenuItemConfig = new javax.swing.JMenuItem();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("aroulette/resources/Bundle"); // NOI18N
        jMenu1.setText(bundle.getString("JMENU1")); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("aRoulette\n");
        setResizable(false);

        jListNames.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jListNames.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListNames.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListNamesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListNames);

        jButtonSelectAll.setText(bundle.getString("ALL")); // NOI18N
        jButtonSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectAllActionPerformed(evt);
            }
        });

        jButtonInvertSelection.setText(bundle.getString("INVERT")); // NOI18N
        jButtonInvertSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInvertSelectionActionPerformed(evt);
            }
        });

        jButtonSelectNone.setText(bundle.getString("NONE")); // NOI18N
        jButtonSelectNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectNoneActionPerformed(evt);
            }
        });

        jComboBoxSelectedName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanelRouletteLayout = new javax.swing.GroupLayout(jPanelRoulette);
        jPanelRoulette.setLayout(jPanelRouletteLayout);
        jPanelRouletteLayout.setHorizontalGroup(
            jPanelRouletteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelRouletteLayout.setVerticalGroup(
            jPanelRouletteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );

        jLabelSelectedName.setBackground(new java.awt.Color(192, 192, 192));
        jLabelSelectedName.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabelSelectedName.setForeground(new java.awt.Color(204, 0, 0));
        jLabelSelectedName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSelectedName.setText(bundle.getString("JLABEL1")); // NOI18N

        jButtonRun.setText(bundle.getString("RUN")); // NOI18N
        jButtonRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRunActionPerformed(evt);
            }
        });

        jButtonStop.setText(bundle.getString("STOP")); // NOI18N
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });

        jButtonUpdate.setText(bundle.getString("UPDATE")); // NOI18N
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jComboBoxPuntuation.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
            ResourceBundle.getBundle("aroulette/resources/Bundle").getString("VOLUNTEER"),
            ResourceBundle.getBundle("aroulette/resources/Bundle").getString("PICKEDDONE"),
            ResourceBundle.getBundle("aroulette/resources/Bundle").getString("PICKEDNOTDONE"),
            ResourceBundle.getBundle("aroulette/resources/Bundle").getString("REFUSSED")}));

jButtonLookFor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aroulette/resources/diana.png"))); // NOI18N
jButtonLookFor.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonLookForActionPerformed(evt);
    }
    });

    jMenuFile.setText(bundle.getString("FILE")); // NOI18N

    jMenuItemLoadList.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItemLoadList.setText(bundle.getString("LOAD LIST")); // NOI18N
    jMenuItemLoadList.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemLoadListActionPerformed(evt);
        }
    });
    jMenuFile.add(jMenuItemLoadList);

    jMenuItemSaveList.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItemSaveList.setText(bundle.getString("SAVE LIST")); // NOI18N
    jMenuItemSaveList.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemSaveListActionPerformed(evt);
        }
    });
    jMenuFile.add(jMenuItemSaveList);

    jMenuItemConfig.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItemConfig.setText(bundle.getString("OPTIONS")); // NOI18N
    jMenuItemConfig.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemConfigActionPerformed(evt);
        }
    });
    jMenuFile.add(jMenuItemConfig);

    jMenuBar.add(jMenuFile);

    setJMenuBar(jMenuBar);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jButtonInvertSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonSelectNone, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane1))
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanelRoulette, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                    .addComponent(jComboBoxSelectedName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButtonLookFor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jComboBoxPuntuation, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabelSelectedName, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButtonRun, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonStop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanelRoulette, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(82, 82, 82)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelSelectedName, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonStop)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(68, 68, 68)
                            .addComponent(jButtonRun, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButtonLookFor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonInvertSelection)
                    .addComponent(jButtonSelectNone)
                    .addComponent(jButtonSelectAll)
                    .addComponent(jComboBoxSelectedName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonUpdate)
                    .addComponent(jComboBoxPuntuation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRunActionPerformed
        startRoulette();
    }//GEN-LAST:event_jButtonRunActionPerformed

    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopActionPerformed
        if (_state == this.RUNNING)
            _state = this.STOPPRESSED;
    }//GEN-LAST:event_jButtonStopActionPerformed

    private void jMenuItemLoadListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLoadListActionPerformed
        boolean ok=true;
        if (_fileModified)
        {
            int selection = JOptionPane.showConfirmDialog(this, java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("THE FILE HAS BEEN MODIFIED SINCE IT WAS OPENED. DO YOU WANT TO SAVE IT?"),
                    java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("WARNING"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (selection == JOptionPane.YES_OPTION)
                ok = this.saveFile();
            
        }
            if (ok)
        {
            JFileChooser fc = new JFileChooser(_currentDir);
            fc.setPreferredSize(new Dimension(650, 500));
            fc.setFileFilter(new FileNameExtensionFilter(java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("COMA SEPARATED VALUES"), java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("CSV")));
            fc.setDialogTitle(java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("LOAD STUDENT LIST"));
            int rc = fc.showDialog(null, java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("LOAD"));
            if (rc == JFileChooser.APPROVE_OPTION)
            {
                _currentDir = fc.getSelectedFile().getParent();
                try {
                    ((RoulettePanel)jPanelRoulette).loadList(fc.getSelectedFile().getPath());
                    _currentListFile=fc.getSelectedFile().getName();
                } catch (FileNotFoundException ex) {
                
                } catch (ParsingException ex) {
                    showMessege(java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("THERE HAVE BEEN PROBLEMS PARSING THE FILE AT ")+fc.getSelectedFile().getPath()+"\n"+ 
                        java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("PLEASE CHECK THAT IT IS A VALID COMA SEPARATED VALUES FILE."));
                }
            }
          
            this.selectAll();
            this.refresh();
            _fileModified=false;
        }
    }//GEN-LAST:event_jMenuItemLoadListActionPerformed

    private void jListNamesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListNamesValueChanged
        RoulettePanel roulette = this.getRoulette();       
        int [] indices = jListNames.getSelectedIndices();
        roulette.setFilter(indices);
        this.refreshCombo();
        this.refreshLabel();
        jPanelRoulette.repaint();
    }//GEN-LAST:event_jListNamesValueChanged

    private void jButtonSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectAllActionPerformed
        this.selectAll();
    }//GEN-LAST:event_jButtonSelectAllActionPerformed

    private void jButtonSelectNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectNoneActionPerformed
        jListNames.clearSelection();
    }//GEN-LAST:event_jButtonSelectNoneActionPerformed

    private void jButtonInvertSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInvertSelectionActionPerformed
        int size = jListNames.getModel().getSize();
        int [] newSelection = new int [size-jListNames.getSelectedIndices().length];
        int k=0;
        for (int i=0; i<size;i++)
        {
            if (!jListNames.isSelectedIndex(i))
            {
                newSelection[k]=i;
                k++;
            }
        }
        jListNames.setSelectedIndices(newSelection);
    }//GEN-LAST:event_jButtonInvertSelectionActionPerformed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        ((RoulettePanel)jPanelRoulette).updatePoints(jComboBoxSelectedName.getSelectedIndex(), jComboBoxPuntuation.getSelectedIndex());
        _fileModified=true;
        this.repaint();
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void jButtonLookForActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLookForActionPerformed
        int index = jComboBoxSelectedName.getSelectedIndex();
        if (index>=0)
        {
            this.getRoulette().setSelected(index);
            this.refreshLabel();
            jPanelRoulette.repaint();
        }
    }//GEN-LAST:event_jButtonLookForActionPerformed

    private void jMenuItemSaveListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveListActionPerformed
        saveFile();
    }//GEN-LAST:event_jMenuItemSaveListActionPerformed

    private void jMenuItemConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConfigActionPerformed
        cw.setVisible(true);
    }//GEN-LAST:event_jMenuItemConfigActionPerformed

     
    private boolean saveFile ()
    {
        JFileChooser fc = new JFileChooser(_currentDir);
        fc.setSelectedFile(new File (_currentDir , _currentListFile));
        fc.setPreferredSize(new Dimension(650, 500));
        fc.setFileFilter(new FileNameExtensionFilter(java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("COMA SEPARATED VALUES"), java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("CSV")));
        fc.setDialogTitle(java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("SAVE STUDENT LIST"));
        int rc = fc.showDialog(null, java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("SAVE"));
        boolean savedOk=false;
        if (rc == JFileChooser.APPROVE_OPTION)
        {
            _currentDir = fc.getSelectedFile().getParent();
            try {
                ((RoulettePanel)jPanelRoulette).saveList(fc.getSelectedFile().getPath());
                _currentListFile=fc.getSelectedFile().getName();
                savedOk=true;
                _fileModified=false;
            } catch (FileNotFoundException ex) {
                
            } catch (Exception ex) {
                showMessege(java.util.ResourceBundle.getBundle("aroulette/resources/Bundle").getString("THERE HAVE BEEN PROBLEMS WRITING THE FILE AT ")+fc.getSelectedFile().getPath()+"\n");
            }
        }
        return savedOk;
    }
    
    private void selectAll()
    {
        int size = jListNames.getModel().getSize();
        int [] indices = new int [size];
        for (int i=0; i<size;i++)
            indices[i] = i;
        jListNames.setSelectedIndices(indices);
    }
    
    public void moveRoulette (int degrees)
    {   
        ((RoulettePanel)jPanelRoulette).move (degrees);
        this.refreshLabel();
        this.repaint();
    }
    
    public void refresh()
    {
        this.refreshList();
        this.refreshCombo();
        this.refreshLabel();
        this.repaint();
    }
    
    private void refreshList ()
    {
        RoulettePanel roulette = this.getRoulette();
        String [] names = roulette.getNames();
        jListNames.setListData(names);
        jComboBoxSelectedName.removeAllItems();
        for (int i=0;i<names.length;i++)
            jComboBoxSelectedName.addItem(names[i]);
        this.selectAll(); 
    }
    
    public void refreshCombo()
    {
        RoulettePanel roulette = this.getRoulette();
        String [] names = roulette.getNames();
        jComboBoxSelectedName.removeAllItems();
        for (int i=0;i<names.length;i++)
            jComboBoxSelectedName.addItem(names[i]);
        int selected = roulette.getSelected();
        if (selected > 0)
            jComboBoxSelectedName.setSelectedIndex(selected);
    }
    
    public void refreshLabel ()
    {
        RoulettePanel roulette = this.getRoulette();
        int selected = roulette.getSelected();
        if (selected >= 0)
        {
            jLabelSelectedName.setText(roulette.getNames()[selected]);
        }else{
            jLabelSelectedName.setText("");
        }
    }
    
    public void updateRoulette()
    {
        RoulettePanel roulette = this.getRoulette();
        roulette.setFilter(jListNames.getSelectedIndices());
        roulette.setSelected(jComboBoxSelectedName.getSelectedIndex());     
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonInvertSelection;
    private javax.swing.JButton jButtonLookFor;
    private javax.swing.JButton jButtonRun;
    private javax.swing.JButton jButtonSelectAll;
    private javax.swing.JButton jButtonSelectNone;
    private javax.swing.JButton jButtonStop;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JComboBox jComboBoxPuntuation;
    private javax.swing.JComboBox jComboBoxSelectedName;
    private javax.swing.JLabel jLabelSelectedName;
    private javax.swing.JList jListNames;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemConfig;
    private javax.swing.JMenuItem jMenuItemLoadList;
    private javax.swing.JMenuItem jMenuItemSaveList;
    private javax.swing.JPanel jPanelRoulette;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
