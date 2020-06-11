// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import fam.badger_ken.matchmaker.ReligionGroup.PruningMode;
import fam.badger_ken.matchmaker.filter.AgeFilter;
import fam.badger_ken.matchmaker.filter.AttributeFilter;
import fam.badger_ken.matchmaker.filter.ClaimsFilter;
import fam.badger_ken.matchmaker.filter.CultureFilter;
import fam.badger_ken.matchmaker.filter.DynastyFilter;
import fam.badger_ken.matchmaker.filter.FiltererFilter;
import fam.badger_ken.matchmaker.filter.GenderFilter;
import fam.badger_ken.matchmaker.filter.HoldingLevelFilter;
import fam.badger_ken.matchmaker.filter.KidsFilter;
import fam.badger_ken.matchmaker.filter.SpousesFilter;
import fam.badger_ken.matchmaker.filter.PietyFilter;
import fam.badger_ken.matchmaker.filter.ReligionFilter;
import fam.badger_ken.matchmaker.filter.RulerFilter;
import fam.badger_ken.matchmaker.filter.TraitFilter;
import fam.badger_ken.matchmaker.filter.WealthFilter;
import fam.badger_ken.matchmaker.widget.AnyDropdownable;
import fam.badger_ken.matchmaker.widget.AnyItemForDropdown;
import fam.badger_ken.matchmaker.widget.DynastySelector;
import fam.badger_ken.matchmaker.widget.TwoPlusAnyComboBox;
import fam.badger_ken.matchmaker.widget.Unsettable;
import fam.badger_ken.matchmaker.widget.UnsettableComboBox;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

/**
 * The Gui class. The code is pretty messy as a lot of it was generated
 * by GuiBuilder.
 */
public class SwingGui implements ResultMaker {

  private static final String INSTALL_DIR_PREF_KEY = "install_dir";
  private JFrame frmCkMatchmaker;
  JPanel filterPanel = new JPanel();
  private String installationDirName = null;
  private File saveGameFile = null;
  JButton doLoadButton = new JButton("  Load  ");
  private Matchmaker matchmaker = new Matchmaker();
  private Color warningColor = new Color(255, 200, 200);
  private Color inProgressColor = new Color(255, 255, 100);
  private Color clickMeColor = new Color(100, 255, 100);
  // keep the choosers around so their state stays.
  JFileChooser installationDirChooser = new JFileChooser();
  JFileChooser saveGameFileChooser = new JFileChooser();
  private ViewTable viewTable;
  private ViewTableModel viewTableModel;
  JPanel viewPanel = new JPanel();
  JLabel viewCaption = new JLabel("-- Results --");

  private JLabel numFiltersLabel = new JLabel(" 0 ");
  public static final Color FILTER_OFF_COLOR = Color.WHITE;
  public static final Color FILTER_ON_COLOR = new Color(204, 255, 255);
  private static final String SAVE_GAME_FILE_PREF_KEY = "save_game_file";
  //private static final String LOAD_CHRISTIANS_PREF_KEY = "only_load_christians";
  private static final String RELIGIOUS_GROUP_PREF_KEY = "religious_group_to_load";
  // have we sized things yet?
  protected boolean firstTime = true;
  JButton resetButton = new JButton("Reset");
  private boolean resultMakerIsEnabled = true;
  Set<String> modsToConsult = new HashSet<String>();
  String traitsDir;
  JButton traitsDumpButton = new JButton("Traits info...");
  JButton exportAllButton = new JButton("export ALL");
  // have we initialized drop-down boxes yet?
  private boolean initializeTraitsPane = true;
  private boolean initializeCulturePane = true;
  private boolean initializeReligionPane = true;
  private boolean initializeDynastyPane = true;
  // my preferences - remember install dir, save game.
  Preferences prefs = Preferences.userNodeForPackage(Matchmaker.class);
  
  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          SwingGui window = new SwingGui();
          window.frmCkMatchmaker.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public SwingGui() {
    try {
      initialize();
    } catch (OutOfMemoryError e) {
      System.err.println("No memory, sorry!");
    }
  }

  private void checkLoadButton() {
    if (installationDirName != null && saveGameFile != null) {
      doLoadButton.setEnabled(true);
      doLoadButton.setBackground(clickMeColor);
    }		
  }

  /**
   * The main routine to calculate and then show the results.
   */
  @Override
  public void makeResults() {
    if (!resultMakerIsEnabled) return;
    viewPanel.setVisible(true);
    String description = matchmaker.filter();
    int numActiveFilters = matchmaker.numActiveFilters();
    numFiltersLabel.setText(Integer.toString(numActiveFilters) + " active");
    numFiltersLabel.setBackground(
        numActiveFilters > 0 ? FILTER_ON_COLOR : FILTER_OFF_COLOR);
    resetButton.setVisible(numActiveFilters > 0);
    resetButton.setEnabled(numActiveFilters > 0);
    viewCaption.setText("Results: " + description);
    viewTableModel.reset(matchmaker.winners);
    viewTable.setRowHeights();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    final ResultMaker resultMaker = this;
    
    final AgeFilter minAgeFilter = new AgeFilter(resultMaker, true);
    matchmaker.addFilter(minAgeFilter);
    final AgeFilter maxAgeFilter = new AgeFilter(resultMaker, false);
    matchmaker.addFilter(maxAgeFilter);
    final ClaimsFilter minClaimsFilter = new ClaimsFilter(resultMaker, true);
    final ClaimsFilter maxClaimsFilter = new ClaimsFilter(resultMaker, false);
    matchmaker.addFilter(minClaimsFilter);
    matchmaker.addFilter(maxClaimsFilter);
    final KidsFilter minKidsFilter = new KidsFilter(resultMaker, true);
    final KidsFilter maxKidsFilter = new KidsFilter(resultMaker, false);
    matchmaker.addFilter(minKidsFilter);
    matchmaker.addFilter(maxKidsFilter);
    final PietyFilter minPietyFilter = new PietyFilter(resultMaker, true);
    final PietyFilter maxPietyFilter = new PietyFilter(resultMaker, false);
    matchmaker.addFilter(minPietyFilter);
    matchmaker.addFilter(maxPietyFilter);
    final WealthFilter minWealthFilter = new WealthFilter(resultMaker, true);
    final WealthFilter maxWealthFilter = new WealthFilter(resultMaker, false);
    matchmaker.addFilter(minWealthFilter);
    matchmaker.addFilter(maxWealthFilter);
    final GenderFilter genderFilter = new GenderFilter(resultMaker);
    matchmaker.addFilter(genderFilter);
    final SpousesFilter minSpousesFilter = new SpousesFilter(resultMaker, true);
    final SpousesFilter maxSpousesFilter = new SpousesFilter(resultMaker, false);
    matchmaker.addFilter(minSpousesFilter);
    matchmaker.addFilter(maxSpousesFilter);
    
    final RulerFilter rulerFilter = new RulerFilter(resultMaker);
    matchmaker.addFilter(rulerFilter);
    final ArrayList<AttributeFilter> attrFilters = new ArrayList<AttributeFilter>(GameConfig.NUM_ATTRIBUTES * 2);
    for (int i = 0; i < GameConfig.NUM_ATTRIBUTES; i++) {
      final AttributeFilter filter = new AttributeFilter(resultMaker, matchmaker, i, true);
      attrFilters.add(filter);
      matchmaker.addFilter(filter);
      final AttributeFilter maxFilter = new AttributeFilter(resultMaker, matchmaker, i, false);
      attrFilters.add(maxFilter);
      matchmaker.addFilter(maxFilter);
    }
    final DynastyFilter yesDynastyFilter = new DynastyFilter(resultMaker, true);
    matchmaker.addFilter(yesDynastyFilter);
    
    frmCkMatchmaker = new JFrame();
    frmCkMatchmaker.setTitle("The CK2 Matchmaker, Version " + Matchmaker.VERSION_NUMBER);
    frmCkMatchmaker.setBounds(100, 100, 800, 200);
    frmCkMatchmaker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frmCkMatchmaker.getContentPane().setLayout(new BoxLayout(frmCkMatchmaker.getContentPane(), BoxLayout.Y_AXIS));
   
    // big border to left, non-zero in other dimensions, else looks choppy.
    resetButton.setBorder(new EmptyBorder(3, 18, 3, 3));  

    JPanel loadPanel = new JPanel();
    frmCkMatchmaker.getContentPane().add(loadPanel);
    loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));

    JPanel installDirPanel = new JPanel();
    FlowLayout flowLayout_1 = (FlowLayout) installDirPanel.getLayout();
    flowLayout_1.setAlignment(FlowLayout.LEFT);
    loadPanel.add(installDirPanel);

    final JLabel installDirLabel = new JLabel("Installation Directory:");
    installDirPanel.add(installDirLabel);


    final JButton installDirButton = new JButton("Choose...");
    installDirButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        installationDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (installationDirName != null) {
          installationDirChooser.setCurrentDirectory(new File(installationDirName));
        }
        installationDirChooser.setDialogTitle("Choose installation directory...");
        int code = installationDirChooser.showOpenDialog(installDirButton);
        if (code != JFileChooser.APPROVE_OPTION) {
          return;
        }
        onInstallDirSet(installationDirChooser.getSelectedFile().getPath());
      }
    });
    installDirPanel.add(installDirButton);

    JPanel saveGamePanel = new JPanel();
    FlowLayout flowLayout = (FlowLayout) saveGamePanel.getLayout();
    flowLayout.setAlignment(FlowLayout.LEFT);
    //loadPanel.add(saveGamePanel);
    installDirPanel.add(saveGamePanel);

    final JLabel saveGameFileLabel = new JLabel("Save game file:");
    saveGamePanel.add(saveGameFileLabel);

    final JButton saveGameFileButton = new JButton("Choose...");
    saveGamePanel.add(saveGameFileButton);

    final JPanel loadActionPanel = new JPanel();
    loadPanel.add(loadActionPanel);
    loadActionPanel.setLayout(new BoxLayout(loadActionPanel, BoxLayout.X_AXIS));

    
    loadActionPanel.add(new JLabel("Religions to load:"));
    String[] groups = { "only Christian", "only Muslim", "All" };
    final JComboBox religionGroupBox = new JComboBox(groups);
    religionGroupBox.setPreferredSize(new Dimension(200, 20));
    religionGroupBox.setSelectedItem(getReligionGroupPref());
    
    final JLabel loadMessageLabel = new JLabel("");
    doLoadButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        matchmaker.clear();
        loadMessageLabel.setText("loading...");
        loadMessageLabel.setBackground(inProgressColor);
        loadMessageLabel.repaint();
        try {
          String traitsBase = getTraitsBase(installationDirName, modsToConsult);
          storeReligionGroupPref(religionGroupBox.getSelectedItem());
          ReligionGroup.PruningMode pruningMode;
          String whatToLoad = religionGroupBox.getSelectedItem().toString();
          if (whatToLoad.toLowerCase().contains("christian")) {
            pruningMode = PruningMode.CHRISTIAN_ONLY;
          } else if (whatToLoad.toLowerCase().contains("muslim")) {
            pruningMode = PruningMode.MUSLIM_ONLY;
          } else {
            pruningMode = PruningMode.ALL;
          }
          matchmaker.process(installationDirName, saveGameFile,
              pruningMode,
              modsToConsult, traitsBase);
          loadMessageLabel.setText(matchmaker.loadSummary());
          frmCkMatchmaker.pack();
          loadMessageLabel.setBackground(Color.WHITE);
          filterPanel.setVisible(true);
          traitsDumpButton.setVisible(true);
          traitsDumpButton.setEnabled(true);
          //traitsDumpButton.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 15));
          traitsDumpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
              JTextArea textArea = new JTextArea(matchmaker.getTraitsDump());
              textArea.setEditable(false);
              JScrollPane scrollPane = new JScrollPane(textArea);
              JOptionPane.showInternalMessageDialog(loadActionPanel, scrollPane);
            }
          });
          exportAllButton.setVisible(true);
          exportAllButton.setEnabled(true);
          //exportAllButton.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 15));
          exportAllButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
              JFileChooser chooser = new JFileChooser();
              chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
              chooser.setDialogTitle("Choose CSV file name...");
              int code = chooser.showSaveDialog(exportAllButton);
              if (code != JFileChooser.APPROVE_OPTION) {
                return;
              }
              matchmaker.csvExportTo(chooser.getSelectedFile(), viewTableModel, true);
            }
          });
          
          if (firstTime) {
            firstTime = false;
            frmCkMatchmaker.setPreferredSize(new Dimension(900, 800));
            frmCkMatchmaker.pack();
          }
          makeResults();
        } catch (FileNotFoundException e) {
          System.out.println(e.getMessage());
          e.printStackTrace();
          loadMessageLabel.setText("Couldn't open file!");
          loadMessageLabel.setBackground(warningColor);
        } catch (OutOfMemoryError e) {
          System.err.println("Out of memory!");
          System.exit(1);
        }
      }

      /**
       * Given the installation directory, and the set of mods,
       * figure out which to use for 'traits' files.
       */
      private String getTraitsBase(String installationDirName,
          Set<String> modsToConsult) {
        if (modsToConsult == null || modsToConsult.isEmpty()) {
          return installationDirName;
        }
        for (String mod : modsToConsult) {
          String modBase = installationDirName + File.separator + "mod" + File.separator + mod;
          String modDirName = modBase
            + File.separator + "common"
            + File.separator + "traits";
          File modDir = new File(modDirName);
          if (modDir.canRead() && modDir.isDirectory()) {
            JFrame frame = new JFrame();
            String message = "The '" + mod + "' mod defines it own set of traits."
              + " Do you wish to use those traits instead of the pre-defined ones?";
            int answer = JOptionPane.showConfirmDialog(frame, message);
            if (answer == JOptionPane.YES_OPTION) {
              return modBase;
            } else if (answer == JOptionPane.NO_OPTION) {
              continue;
            }
          }
        }
        return installationDirName;
      }
    });
    
    doLoadButton.setHorizontalAlignment(SwingConstants.LEFT);
    doLoadButton.setEnabled(false);
    loadActionPanel.add(religionGroupBox);
    loadActionPanel.add(doLoadButton);

    loadActionPanel.add(loadMessageLabel);
    saveGameFileButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        saveGameFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        saveGameFileChooser.setDialogTitle("Choose save game file...");
        if (saveGameFile != null && saveGameFile.canRead()) {
          saveGameFileChooser.setSelectedFile(saveGameFile);
          //saveGameFileChooser.setCurrentDirectory(saveGameFile);
        }
        int code = saveGameFileChooser.showOpenDialog(saveGameFileButton);
        if (code != JFileChooser.APPROVE_OPTION) {
          return;
        }
        onSaveGameFileSet(saveGameFileChooser.getSelectedFile());
      }
    });
    final JPanel loadMoreActionPanel = new JPanel();
    loadPanel.add(loadMoreActionPanel);
    loadMoreActionPanel.setLayout(new BoxLayout(loadMoreActionPanel, BoxLayout.X_AXIS));
    
    loadMoreActionPanel.add(traitsDumpButton);
    traitsDumpButton.setVisible(false);
    traitsDumpButton.setEnabled(false);
    loadMoreActionPanel.add(new JLabel("   "));
    loadMoreActionPanel.add(exportAllButton);
    exportAllButton.setVisible(false);
    exportAllButton.setEnabled(false);

    filterPanel.setVisible(false);
    frmCkMatchmaker.getContentPane().add(filterPanel);
    
    filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

    JPanel filterHeaderPanel = new JPanel();
    filterPanel.add(filterHeaderPanel);
    filterHeaderPanel.setLayout(new BoxLayout(filterHeaderPanel, BoxLayout.X_AXIS));

    JLabel lblNewLabel = new JLabel("Filters:");
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel.setFont(new Font("Arial Rounded MT Bold", Font.ITALIC, 16));
    filterHeaderPanel.add(lblNewLabel);

    numFiltersLabel.setOpaque(true);
    numFiltersLabel.setFont(new Font("Arial Rounded MT Bold", Font.ITALIC, 16));
    filterHeaderPanel.add(numFiltersLabel);
    
    JLabel lblNewLabel_12 = new JLabel("     ");
    filterHeaderPanel.add(lblNewLabel_12);
    resetButton.addMouseListener(new MouseAdapter() {
      private void unsetUnsettables(Container root) {
        if (root == null) return;
        for (int i = 0; i < root.getComponentCount(); i++) {
          Component kid = root.getComponent(i);
          if (kid instanceof Unsettable) {
            ((Unsettable) kid).unset();
          } else if (kid instanceof Container) {
            unsetUnsettables((Container) kid);
          }
        }
      }
      @Override
      public void mouseClicked(MouseEvent arg0) {
        resultMaker.setEnabled(false);
        unsetUnsettables(filterPanel);
        resultMaker.setEnabled(true);
        resultMaker.makeResults();
      }
    });
    

    filterHeaderPanel.add(resetButton);
    resetButton.setEnabled(false);

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    filterPanel.add(tabbedPane);

    JPanel ageFilterPanel = new JPanel();
    tabbedPane.addTab("Age", null, ageFilterPanel, null);
    ageFilterPanel.setLayout(new BoxLayout(ageFilterPanel, BoxLayout.Y_AXIS));

    JPanel minAgePanel = new JPanel();
    ageFilterPanel.add(minAgePanel);

    JLabel lblNewLabel_2 = new JLabel("min age:");
    lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
    minAgePanel.add(lblNewLabel_2);

    UnsettableTextField minAgeField = new UnsettableTextField(3, minAgeFilter);
    minAgePanel.add(minAgeField);

    JPanel maxAgePanel = new JPanel();
    ageFilterPanel.add(maxAgePanel);

    JLabel lblNewLabel_3 = new JLabel("max age:");
    lblNewLabel_3.setHorizontalAlignment(SwingConstants.LEFT);
    maxAgePanel.add(lblNewLabel_3);

    UnsettableTextField maxAgeField = new UnsettableTextField(3, maxAgeFilter);
    maxAgePanel.add(maxAgeField);

    JPanel genderFilterPanel = new JPanel();
    tabbedPane.addTab("Gender", null, genderFilterPanel, null);

    TwoPlusAnyComboBox genderComboBox = new TwoPlusAnyComboBox("Male", "Female", genderFilter);
    genderFilterPanel.add(genderComboBox);

    JPanel spouseFilterPanel = new JPanel();
    tabbedPane.addTab("Spouses", null, spouseFilterPanel, null);

    ageFilterPanel.setLayout(new BoxLayout(ageFilterPanel, BoxLayout.Y_AXIS));

    JPanel minSpousesPanel = new JPanel();
    spouseFilterPanel.add(minSpousesPanel);

    lblNewLabel_2 = new JLabel("min Spouses:");
    lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
    minSpousesPanel.add(lblNewLabel_2);

    UnsettableTextField minSpousesField = new UnsettableTextField(3, minSpousesFilter);
    minSpousesPanel.add(minSpousesField);

    JPanel maxSpousesPanel = new JPanel();
    spouseFilterPanel.add(maxSpousesPanel);

    lblNewLabel_3 = new JLabel("max Spouses:");
    lblNewLabel_3.setHorizontalAlignment(SwingConstants.LEFT);
    maxSpousesPanel.add(lblNewLabel_3);

    UnsettableTextField maxSpousesField = new UnsettableTextField(3, maxSpousesFilter);
    maxSpousesPanel.add(maxSpousesField);
    
    JPanel kidsFilterPanel = new JPanel();
    tabbedPane.addTab("Kids", null, kidsFilterPanel, null);
    kidsFilterPanel.setLayout(new BoxLayout(kidsFilterPanel, BoxLayout.Y_AXIS));
    
    JPanel panel_5 = new JPanel();
    kidsFilterPanel.add(panel_5);
    
    JLabel lblMinOf_1 = new JLabel("min # of Kids: ");
    panel_5.add(lblMinOf_1);
    
    UnsettableTextField minKidsField = new UnsettableTextField(3, minKidsFilter);
    panel_5.add(minKidsField);

    
    JPanel panel_6 = new JPanel();
    kidsFilterPanel.add(panel_6);
    
    JLabel lblMaxOf = new JLabel("max # of Kids: ");
    panel_6.add(lblMaxOf);
    
    UnsettableTextField maxKidsField = new UnsettableTextField(3, maxKidsFilter);
    panel_6.add(maxKidsField);
    
    JPanel dynastyFilterPanel = new JPanel();

    tabbedPane.addTab("Dynasties", null, dynastyFilterPanel, null);
    dynastyFilterPanel.setLayout(new BoxLayout(dynastyFilterPanel, BoxLayout.X_AXIS));
    JPanel dynastyYesPanel = new JPanel();
    dynastyYesPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
    dynastyFilterPanel.add(dynastyYesPanel);
    dynastyYesPanel.setLayout(new BoxLayout(dynastyYesPanel, BoxLayout.Y_AXIS));

    JLabel label = new JLabel("Must have:");
    label.setHorizontalAlignment(JLabel.CENTER);
    dynastyYesPanel.add(label);
    final DynastySelector yesDynastyField = new DynastySelector(yesDynastyFilter);
    dynastyYesPanel.add(yesDynastyField);
    
    JPanel dynastyNoPanel = new JPanel();
    dynastyFilterPanel.add(dynastyNoPanel);
    dynastyNoPanel.setLayout(new BoxLayout(dynastyNoPanel, BoxLayout.Y_AXIS));

    dynastyNoPanel.add(mustNotHaveRow());
    for (int j = 0; j < 5; j++) {
      Filterer filter = new DynastyFilter(resultMaker, false);
      matchmaker.addFilter(filter);
      dynastyNoPanel.add(new DynastySelector(filter));
    }
    
    dynastyFilterPanel.addComponentListener(new ComponentAdapter() {
      private void populateDynasties(JComponent root) {
        if (root == null) return;
        if (root instanceof DynastySelector) {
          ((DynastySelector) root).setDynasties(matchmaker.saveState.getSortedDynasties());
        } else if (root instanceof JPanel) {
          for (Component child: ((JPanel) root).getComponents()) {
            populateDynasties((JComponent) child);
          }
        }
      }
      
      @Override
      public void componentShown(ComponentEvent evt) {
        if (initializeDynastyPane) {
          populateDynasties((JComponent) evt.getSource());
        }
        initializeDynastyPane = false;
      }      
    });
    

    JPanel rulerFilterPanel = new JPanel();
    rulerFilterPanel.setLayout(new BoxLayout(rulerFilterPanel, BoxLayout.Y_AXIS));
    tabbedPane.addTab("Holdings", null, rulerFilterPanel, null);
    for (int i = 0; i < 2; i++) { 
      JPanel row1Panel = new JPanel();
      row1Panel.setLayout(new BoxLayout(row1Panel, BoxLayout.X_AXIS));

      //TwoPlusAnyComboBox rulerComboBox = new TwoPlusAnyComboBox("Yes", "No", rulerFilter);
      //rulerFilterPanel.add(rulerComboBox);
      UnsettableComboBox holdingsBox = new UnsettableComboBox();
      Vector<AnyDropdownable> entries = new Vector<AnyDropdownable>();
      entries.add(new AnyItemForDropdown());
      entries.add(HoldingLevel.BARONY);
      entries.add(HoldingLevel.COUNTY);
      entries.add(HoldingLevel.DUCHY);
      entries.add(HoldingLevel.KINGDOM);
      entries.add(HoldingLevel.EMPIRE);
      boolean isMin = (i == 0);
      Filterer filter = new HoldingLevelFilter(resultMaker, isMin);
      matchmaker.addFilter(filter);
      holdingsBox.populate(entries, filter);
      //holdingsBox.setBorder(BorderFactory.createEmptyBorder(0,50,0,150));
      holdingsBox.setMaximumSize(new Dimension(500, 30));

      row1Panel.add(new JLabel(isMin ? "minimum holdings level: " : "maximum holdings level"));
      row1Panel.add(holdingsBox);
      JLabel spacer = new JLabel("");
      spacer.setMinimumSize(new Dimension(250, 0));
      row1Panel.add(spacer);
      rulerFilterPanel.add(row1Panel);
    }

    JPanel claimsFilterPanel = new JPanel();
    tabbedPane.addTab("Claims", null, claimsFilterPanel, null);
    claimsFilterPanel.setLayout(new BoxLayout(claimsFilterPanel, BoxLayout.Y_AXIS));

    JPanel panel = new JPanel();
    claimsFilterPanel.add(panel);

    JLabel lblMinOf = new JLabel("min # of claims:");
    lblMinOf.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(lblMinOf);

    UnsettableTextField minClaimsField = new UnsettableTextField(3, minClaimsFilter);
    panel.add(minClaimsField);

    JPanel panel_1 = new JPanel();
    claimsFilterPanel.add(panel_1);

    JLabel lblMaxClaims = new JLabel("max # claims:");
    lblMaxClaims.setHorizontalAlignment(SwingConstants.LEFT);
    panel_1.add(lblMaxClaims);

    UnsettableTextField maxClaimsField = new UnsettableTextField(3, maxClaimsFilter);
    panel_1.add(maxClaimsField);

    JPanel attrFilterPanel = new JPanel();
    tabbedPane.addTab("Attributes", null, attrFilterPanel, null);
    attrFilterPanel.setLayout(new BoxLayout(attrFilterPanel, BoxLayout.Y_AXIS));

    JPanel diplomacyPanel = new JPanel();
    FlowLayout flowLayout_4 = (FlowLayout) diplomacyPanel.getLayout();
    flowLayout_4.setAlignment(FlowLayout.LEFT);
    attrFilterPanel.add(diplomacyPanel);

    JLabel lblDiplomacy = new JLabel("Diplomacy...");
    lblDiplomacy.setFont(new Font("Tahoma", Font.BOLD, 13));
    diplomacyPanel.add(lblDiplomacy);

    JLabel label_7 = new JLabel("min: ");
    diplomacyPanel.add(label_7);

    int attrNum = 0;
    UnsettableTextField minDiplomacyField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    diplomacyPanel.add(minDiplomacyField);

    JLabel label_8 = new JLabel(" max: ");
    diplomacyPanel.add(label_8);

    UnsettableTextField maxDiplomacyField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    diplomacyPanel.add(maxDiplomacyField);

    JPanel martialPanel = new JPanel();
    FlowLayout fl_martialPanel = (FlowLayout) martialPanel.getLayout();
    fl_martialPanel.setAlignment(FlowLayout.LEFT);
    attrFilterPanel.add(martialPanel);

    JLabel lblMartial = new JLabel("Martial...");
    lblMartial.setHorizontalAlignment(SwingConstants.RIGHT);
    lblMartial.setFont(new Font("Tahoma", Font.BOLD, 13));
    martialPanel.add(lblMartial);

    JLabel label_1 = new JLabel("min: ");
    martialPanel.add(label_1);

    UnsettableTextField minMartialField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    martialPanel.add(minMartialField);

    JLabel label_2 = new JLabel(" max: ");
    martialPanel.add(label_2);

    UnsettableTextField maxMartialField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    martialPanel.add(maxMartialField);

    JPanel stewardshipPanel = new JPanel();
    FlowLayout fl_stewardshipPanel = (FlowLayout) stewardshipPanel.getLayout();
    fl_stewardshipPanel.setAlignment(FlowLayout.LEFT);
    attrFilterPanel.add(stewardshipPanel);

    JLabel lblNewLabel_1 = new JLabel("Stewardship...");
    lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
    stewardshipPanel.add(lblNewLabel_1);

    JLabel lblNewLabel_4 = new JLabel("min: ");
    stewardshipPanel.add(lblNewLabel_4);

    UnsettableTextField minStewardshipField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    stewardshipPanel.add(minStewardshipField);

    JLabel lblNewLabel_5 = new JLabel(" max: ");
    stewardshipPanel.add(lblNewLabel_5);

    UnsettableTextField maxStewardshipField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    stewardshipPanel.add(maxStewardshipField);

    JPanel intriguePanel = new JPanel();
    FlowLayout flowLayout_2 = (FlowLayout) intriguePanel.getLayout();
    flowLayout_2.setAlignment(FlowLayout.LEFT);
    attrFilterPanel.add(intriguePanel);

    JLabel lblIntrigue = new JLabel("Intrigue...");
    lblIntrigue.setFont(new Font("Tahoma", Font.BOLD, 13));
    intriguePanel.add(lblIntrigue);

    JLabel label_3 = new JLabel("min: ");
    intriguePanel.add(label_3);

    UnsettableTextField minIntrigueField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    intriguePanel.add(minIntrigueField);

    JLabel label_4 = new JLabel(" max: ");
    intriguePanel.add(label_4);

    UnsettableTextField maxIntrigueField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    intriguePanel.add(maxIntrigueField);

    JPanel learningPanel = new JPanel();
    FlowLayout flowLayout_3 = (FlowLayout) learningPanel.getLayout();
    flowLayout_3.setAlignment(FlowLayout.LEFT);
    attrFilterPanel.add(learningPanel);

    JLabel lblLearning = new JLabel("Learning...");
    lblLearning.setFont(new Font("Tahoma", Font.BOLD, 13));
    learningPanel.add(lblLearning);

    JLabel label_5 = new JLabel("min: ");
    learningPanel.add(label_5);

    UnsettableTextField minLearningField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    learningPanel.add(minLearningField);

    JLabel label_6 = new JLabel(" max: ");
    learningPanel.add(label_6);

    UnsettableTextField maxLearningField = new UnsettableTextField(3, attrFilters.get(attrNum++));
    learningPanel.add(maxLearningField);

    JPanel traitsFilterPanel = new JPanel();
    traitsFilterPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent evt) {
        // the first time the traits tab is shown, populate the traits
        // drop-downs - they have no content until the load happens.
        if (initializeTraitsPane) {
        populateDropdownPane((JComponent) evt.getSource(),
            new DropdownMakerHelper() {
              @Override
              public Filterer makeFilter(boolean isInclusion) {
                return new TraitFilter(resultMaker, isInclusion);
              }
              @Override
              public Collection<? extends AnyDropdownable> getOriginalItems() {
                return matchmaker.gameConfig.traits.values();
              }
              @Override
              public boolean passes(Filterer filterer) {
               return !(filterer instanceof TraitFilter);
              }
            });
        }
        initializeTraitsPane = false;
        //populateTraitsDropdowns((JComponent) evt.getSource(), matchmaker.gameConfig);
      }      
    });
    tabbedPane.addTab("Traits", null, traitsFilterPanel, null);
    traitsFilterPanel.setLayout(new BoxLayout(traitsFilterPanel, BoxLayout.X_AXIS));

    JPanel traitsYesPanel = new JPanel();
    traitsYesPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
    traitsFilterPanel.add(traitsYesPanel);
    traitsYesPanel.setLayout(new BoxLayout(traitsYesPanel, BoxLayout.Y_AXIS));

    JLabel lblNewLabel_6 = new JLabel("Must have:");
    traitsYesPanel.add(lblNewLabel_6);

    for (int i = 0; i < 5; i++) {
      traitsYesPanel.add(new UnsettableComboBox());
    }

    JPanel traitsNoPanel = new JPanel();
    traitsNoPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
    traitsFilterPanel.add(traitsNoPanel);
    traitsNoPanel.setLayout(new BoxLayout(traitsNoPanel, BoxLayout.Y_AXIS));

    traitsNoPanel.add(mustNotHaveRow());

    for (int i = 0; i < 5; i++) {
      traitsNoPanel.add(new UnsettableComboBox());
    }

    JPanel religionFilterPanel = new JPanel();
    tabbedPane.addTab("Religions", null, religionFilterPanel, null);
    religionFilterPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent evt) {
        // the first time the religion tab is shown, populate the
        // drop-downs - they have no content until the load happens.
        if (initializeReligionPane) {
          populateDropdownPane((JComponent) evt.getSource(),
              new DropdownMakerHelper() {
            @Override
            public Filterer makeFilter(boolean isInclusion) {
              return new ReligionFilter(resultMaker, isInclusion);
            }
            @Override
            public Collection<? extends AnyDropdownable> getOriginalItems() {
              return matchmaker.gameConfig.religionsByLabel.values();
            }
            @Override
            public boolean passes(Filterer filterer) {
              return !(filterer instanceof ReligionFilter);
            }
          });
        }
        initializeReligionPane = false;
      }      
    });
    religionFilterPanel.setLayout(new GridLayout(1, 2, 5, 0));

    JPanel religionYesPanel = new JPanel();
    religionFilterPanel.add(religionYesPanel);
    religionYesPanel.setLayout(new BoxLayout(religionYesPanel, BoxLayout.Y_AXIS));

    JLabel lblNewLabel_10 = new JLabel("Must have:");
    religionYesPanel.add(lblNewLabel_10);
    
    UnsettableComboBox religionYesBox = new UnsettableComboBox();
    religionYesPanel.add(religionYesBox);
    
    JPanel religionNoPanel = new JPanel();
    religionFilterPanel.add(religionNoPanel);
    religionNoPanel.setLayout(new BoxLayout(religionNoPanel, BoxLayout.Y_AXIS));
    religionNoPanel.add(mustNotHaveRow());
    
    for (int i = 0; i < 5; i++) {
      religionNoPanel.add(new UnsettableComboBox());
    }
    
    JPanel cultureFilterPanel = new JPanel();
    tabbedPane.addTab("Cultures", null, cultureFilterPanel, null);
    cultureFilterPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent evt) {
        if (initializeCulturePane) {
        // the first time the traits tab is shown, populate the traits
        // drop-downs - they have no content until the load happens.
        populateDropdownPane((JComponent) evt.getSource(),
            new DropdownMakerHelper() {
              @Override
              public Filterer makeFilter(boolean isInclusion) {
                return new CultureFilter(resultMaker, isInclusion);
              }
              @Override
              public Collection<? extends AnyDropdownable> getOriginalItems() {
                return matchmaker.gameConfig.culturesByLabel.values();
              }
              @Override
              public boolean passes(Filterer filterer) {
                return !(filterer instanceof CultureFilter);
              }
            });
        }
        initializeCulturePane = false;
        //populateTraitsDropdowns((JComponent) evt.getSource(), matchmaker.gameConfig);
      }      
    });
    
    cultureFilterPanel.setLayout(new GridLayout(0, 2, 5, 0));
    
    JPanel cultureYesPanel = new JPanel();
    cultureFilterPanel.add(cultureYesPanel);
    cultureYesPanel.setLayout(new BoxLayout(cultureYesPanel, BoxLayout.Y_AXIS));
    
    JLabel label_11 = new JLabel("Must have:");
    label_11.setHorizontalAlignment(SwingConstants.CENTER);
    cultureYesPanel.add(label_11);
    
    JPanel cultureNoPanel = new JPanel();
    cultureFilterPanel.add(cultureNoPanel);
    cultureNoPanel.setLayout(new BoxLayout(cultureNoPanel, BoxLayout.Y_AXIS));
    cultureNoPanel.add(mustNotHaveRow());
    
    UnsettableComboBox yesbox = new UnsettableComboBox();
    cultureYesPanel.add(yesbox);
    for (int i = 0; i < 5; i++) {
      cultureNoPanel.add(new UnsettableComboBox());
    }
    
    JPanel pietyFilterPanel = new JPanel();
    tabbedPane.addTab("Piety", null, pietyFilterPanel, null);
    pietyFilterPanel.setLayout(new BoxLayout(pietyFilterPanel, BoxLayout.Y_AXIS));
    
    JPanel panel_7 = new JPanel();
    pietyFilterPanel.add(panel_7);
    
    JLabel lblNewLabel_11 = new JLabel("min piety: ");
    panel_7.add(lblNewLabel_11);
    
    UnsettableTextField minPietyField = new UnsettableTextField(3, minPietyFilter);
    panel_7.add(minPietyField);
    
    JPanel panel_8 = new JPanel();
    pietyFilterPanel.add(panel_8);
    
    JLabel lblMaxPiety = new JLabel("max piety: ");
    panel_8.add(lblMaxPiety);
    
    UnsettableTextField maxPietyField = new UnsettableTextField(3, maxPietyFilter);
    panel_8.add(maxPietyField);

    
    JPanel wealthFilterPanel = new JPanel();
    tabbedPane.addTab("Wealth", null, wealthFilterPanel, null);
    wealthFilterPanel.setLayout(new BoxLayout(wealthFilterPanel, BoxLayout.Y_AXIS));
    
    panel = new JPanel();
    wealthFilterPanel.add(panel);
    label = new JLabel("min wealth: ");
    panel.add(label);
    
    UnsettableTextField minWealthField = new UnsettableTextField(3, minWealthFilter);
    panel.add(minWealthField);
    
    panel = new JPanel();
    wealthFilterPanel.add(panel);
    JLabel lblMaxWealth = new JLabel("max wealth: ");
    panel.add(lblMaxWealth);
    
    UnsettableTextField maxWealthField = new UnsettableTextField(3, maxWealthFilter);
    panel.add(maxWealthField);
    


    frmCkMatchmaker.getContentPane().add(viewPanel);
    viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

    JPanel resultsRowPanel = new JPanel();
    viewPanel.add(resultsRowPanel);
    viewCaption.setHorizontalAlignment(SwingConstants.LEFT);
    resultsRowPanel.add(viewCaption);

    viewCaption.setFont(new Font("Tahoma", Font.BOLD, 16));

    final JButton exportButton = new JButton("Export...");
    exportButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Choose CSV file name...");
        int code = chooser.showSaveDialog(exportButton);
        if (code != JFileChooser.APPROVE_OPTION) {
          return;
        }
        matchmaker.csvExportTo(chooser.getSelectedFile(), viewTableModel, false);
      }
    });
    exportButton.setHorizontalAlignment(SwingConstants.RIGHT);
    resultsRowPanel.add(exportButton);

    viewTableModel = new ViewTableModel(matchmaker);
    viewTable = new ViewTable(viewTableModel);
    viewTable.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
    viewTable.setColumnSelectionAllowed(true);
    viewTable.setFillsViewportHeight(true);
    viewTable.setRowSelectionAllowed(false);
    viewTable.setCellRenderers(matchmaker);
    viewTable.setColumnWidths();
    viewTable.setRowSorter(new ViewTableSorter(matchmaker, viewTableModel));

    JScrollPane scrollPane = new JScrollPane(viewTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    frmCkMatchmaker.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{installDirButton, saveGameFileButton, doLoadButton, exportButton, scrollPane, viewTable}));
    viewPanel.add(scrollPane);
    viewPanel.setVisible(false);
    frmCkMatchmaker.pack();
    //installationDirName = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\crusader kings ii";
    //saveGameFile = new File("C:\\Users\\Us\\Documents\\Paradox Interactive\\Crusader Kings II\\save games\\Aragon1401_05_02warwson.ck2");
    String instDir = prefs.get(INSTALL_DIR_PREF_KEY, null);
    if (instDir != null) {
      onInstallDirSet(instDir);
    }
    String fName = prefs.get(SAVE_GAME_FILE_PREF_KEY, null);
    if (fName != null) {
      File saveFil = new File(fName);
      if (saveFil != null && saveFil.canRead()) {
        onSaveGameFileSet(saveFil);
      }
    }
  }



  protected void checkForMods(String rootDir) {
    // first zero out the prior set of mods.
    modsToConsult.clear();
    if (rootDir == null || rootDir.isEmpty()) {
      return;
    }
    final File dirFile = new File(rootDir);
    if (dirFile == null || !dirFile.canRead() || !dirFile.isDirectory()) {
      return;
    }
    File [] files = dirFile.listFiles(new FileFilter() {
      @Override
      public boolean accept(File arg0) {
        return arg0.isDirectory() && !arg0.equals(dirFile);
      }}
    );
    if (files == null) return;
    for (File fil : files) {
      modsToConsult.add(fil.getName());
    }
  }

  /**
   * This helper class is a bit of a 'factory', it helps all the
   * various panes that generate a set of drop-down boxes with an 'any'
   * value to rule things in/out share the same logic.
   */
  private interface DropdownMakerHelper extends FiltererFilter {
    /**
     * @return the original set of items that are copied into each dropdown.
     * They are copied because otherwise Swing requires that they share the
     * same selection value.
     */
    public Collection<? extends AnyDropdownable> getOriginalItems();
    
    /**
     * @param isInclusion whether the dropdown box is inclusive.
     * @return a new filter to attach to a new dropdown box.
     */
    public Filterer makeFilter(boolean isInclusion);
  }

  /**
   * Populate the drop-downs under this panel, and hook up their filters.
   * It's assumed that (a) there is a left panel with 0 or more inclusion drop-downs,
   * (b) there is a right panel with 0 or more exclusion drop-downs,
   * (c) all the dropdowns are Any-style dropdowns.
   * @param panel the panel
   * @param makerHelper specifies the semantics
   */
  private void populateDropdownPane(JComponent panel, DropdownMakerHelper helper) {
    Vector<AnyDropdownable> originalItems = new Vector<AnyDropdownable>();
    for (AnyDropdownable item : helper.getOriginalItems()) {
      originalItems.add(item);
    }
    originalItems.add(new AnyItemForDropdown());

    Collections.sort(originalItems, new Comparator<AnyDropdownable>() {
      @Override
      public int compare(AnyDropdownable arg0, AnyDropdownable arg1) {
        if (arg0.isAny() && arg1.isAny()) {
          return 0;
        } else if (arg0.isAny()) {
          return -1;
        } else if (arg1.isAny()) {
          return 1;
        } else {
          return arg0.toString().compareTo(arg1.toString());
        }
      }});

    // kill any old filters, they will get remade....
    matchmaker.removeFilters(helper);

    int i = 0;
    boolean isInclusionPanel = true;
    for (Component kid : panel.getComponents()) {
      // assumes first panel is for inclusion, second for exclusion
      // Also assumes we want them both to have the same width.
      // Also assumes we want all dropdowns to have the same height.
      if (kid instanceof JPanel) {
        for (Component grandkid : ((JPanel) kid).getComponents()) {
          if (grandkid instanceof UnsettableComboBox) {
            Filterer filter = helper.makeFilter(isInclusionPanel);
            matchmaker.addFilter(filter);
            ((UnsettableComboBox) grandkid).populate(originalItems, filter);
          }
        }
        isInclusionPanel = false;
      }
      i++;
    }
    panel.setPreferredSize(panel.getPreferredSize());
  }
  
  /**
   * Utility routine, returns a row 
   * with the text 'must NOT have'
   */
  private JPanel mustNotHaveRow() {
    JPanel panel_row = new JPanel();
    
    JLabel label = new JLabel("Must ");
    panel_row.add(label);
    
    label = new JLabel(" NOT ");
    label.setOpaque(true);
    label.setForeground(Color.RED);
    label.setFont(new Font("Tahoma", Font.BOLD, 13));
    panel_row.add(label);
    
    label = new JLabel("have");
    panel_row.add(label);
    return panel_row;
  }
  

  @Override
  public void setEnabled(boolean b) {
    resultMakerIsEnabled = b;
  } 
  
  /**
   * called when the installation directory is set.
   * @param newDir newDirectory.
   */
  private void onInstallDirSet(String newDir) {
    //installDirLabel.setText("Installation directory: " + installationDirChooser.getSelectedFile());
    if (newDir.equals(installationDirName)) {
      return;
    }
    installationDirName = newDir;
    checkForMods(installationDirName + File.separator + "mod");
    initializeCulturePane = initializeReligionPane = initializeTraitsPane 
      = initializeDynastyPane = true;
    prefs.put(INSTALL_DIR_PREF_KEY, newDir);
    checkLoadButton();
  }
  
  private void onSaveGameFileSet(File newFile) {
    if (newFile.equals(saveGameFile)) {
      return;
    }
    saveGameFile = newFile;
    initializeCulturePane = initializeReligionPane 
      = initializeDynastyPane = true;
    try {
      prefs.put(SAVE_GAME_FILE_PREF_KEY, saveGameFile.getCanonicalPath());
    } catch (IOException e) {
      System.err.println("couldn't save save-game-file pref");
    }
    checkLoadButton();    
  }
  
  /*
  private boolean getChristiansCheckboxPref() {
    return prefs.getBoolean(LOAD_CHRISTIANS_PREF_KEY, true);
  }
  protected void storeChristiansCheckboxPref(boolean selected) {
    prefs.putBoolean(LOAD_CHRISTIANS_PREF_KEY, selected);
  } 
  */ 
  protected void storeReligionGroupPref(Object selectedItem) {
    prefs.put(RELIGIOUS_GROUP_PREF_KEY, selectedItem.toString());
  }
  private String getReligionGroupPref() {
    return prefs.get(RELIGIOUS_GROUP_PREF_KEY, "All");
  }
}
