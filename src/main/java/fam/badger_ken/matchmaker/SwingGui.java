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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.prefs.Preferences;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import fam.badger_ken.matchmaker.columns.*;
import fam.badger_ken.matchmaker.filter.*;
import fam.badger_ken.matchmaker.widget.*;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import fam.badger_ken.matchmaker.ReligionGroup.PruningMode;

/**
 * The Gui class. The code is pretty messy as a lot of it was generated
 * by GuiBuilder.
 */
public class SwingGui implements ResultMaker {

    private static final String INSTALL_DIR_PREF_KEY = "install_dir";
    public static final String CHANGE = "Change";
    private JFrame frmCkMatchmaker;
    final JPanel filterPanel = new JPanel();
    private String installationDirName = null;
    private File saveGameFile = null;
    final JButton doLoadButton = new JButton("  Load  ");
    private final Matchmaker matchmaker = new Matchmaker();
    private final Color warningColor = new Color(255, 200, 200);
    private final Color inProgressColor = new Color(255, 255, 100);
    private final Color clickMeColor = new Color(100, 255, 100);
    // keep the choosers around so their state stays.
    final JFileChooser installationDirChooser = new JFileChooser();
    final JFileChooser saveGameFileChooser = new JFileChooser();
    private ViewTable viewTable;
    private ViewTableModel viewTableModel;
    final JPanel viewPanel = new JPanel();
    final JLabel viewCaption = new JLabel("-- Results --");

    private final JLabel numFiltersLabel = new JLabel(" 0 ");
    public static final Color FILTER_OFF_COLOR = Color.WHITE;
    public static final Color FILTER_ON_COLOR = new Color(204, 255, 255);
    private static final String SAVE_GAME_FILE_PREF_KEY = "save_game_file";
    //private static final String LOAD_CHRISTIANS_PREF_KEY = "only_load_christians";
    private static final String RELIGIOUS_GROUP_PREF_KEY = "religious_group_to_load";
    // have we sized things yet?
    protected boolean firstTime = true;
    final JButton resetButton = new JButton("Reset");
    private boolean resultMakerIsEnabled = true;
    final Set<String> modsToConsult = new HashSet<>();
    String traitsDir;
    final JButton traitsDumpButton = new JButton("Traits info...");
    final JButton exportAllButton = new JButton("export ALL");
    // my preferences - remember install dir, save game.
    final Preferences prefs = Preferences.userNodeForPackage(Matchmaker.class);

    final List<Column> columns =
            new ArrayList<>(Arrays.asList(
                    new AgeColumn(),
                    new GenderColumn(),
                    new SpouseColumn(),
                    new KidsColumn(),
                    new NameColumn(matchmaker),
                    new DynastiesColumn(matchmaker),
                    new HoldingsColumn(matchmaker),
                    new ClaimsColumn(),
                    new PietyColumn(),
                    new WealthColumn(),
                    new PrestigeColumn(),
                    new ArtifactColumn(),
                    new TraitsColumn(matchmaker),
                    new ReligionColumn(matchmaker),
                    new CultureColumn(matchmaker),
                    new HomeColumn(matchmaker),
                    new HealthColumn()
            ));

    private void repopulateColumns() {
        columns.forEach(Column::repopulate);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SwingGui window = new SwingGui();
                window.frmCkMatchmaker.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
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
        numFiltersLabel.setText(numActiveFilters + " active");
        numFiltersLabel.setBackground(
                numActiveFilters > 0 ? FILTER_ON_COLOR : FILTER_OFF_COLOR);
        resetButton.setVisible(numActiveFilters > 0);
        resetButton.setEnabled(numActiveFilters > 0);
        viewCaption.setText("Results: " + description);
        viewTableModel.reset(matchmaker.winners);
        viewTable.setRowHeights();
    }

    private void addAttributeColumns() {
        final List<String> attributes = Arrays.asList("Diplomacy", "Martial", "Stewardship", "Intrigue", "Learning");
        for (int i = 0; i < attributes.size(); i++) {
            this.columns.add(new AttributeColumn(attributes.get(i), matchmaker, i));
        }

    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        final ResultMaker resultMaker = this;
        addAttributeColumns();


//
//    final RulerFilter rulerFilter = new RulerFilter(resultMaker);
//    matchmaker.addFilter(rulerFilter);

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
                installDirButton.setText(CHANGE);
                installDirButton.setBackground(Color.GREEN);
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
        String[] groups = {"only Christian", "only Muslim", "All"};
        final JComboBox<String> religionGroupBox = new JComboBox<>(groups);
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
                    storeReligionGroupPref(Objects.requireNonNull(religionGroupBox.getSelectedItem()));
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
                saveGameFileButton.setText(CHANGE);
                saveGameFileButton.setBackground(Color.GREEN);
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


        for (Column column : columns) {
            Optional<Component> tab = column.setupFiltersAndMakeTab(matchmaker, resultMaker);
            tab.ifPresent(presentTab -> tabbedPane.addTab(column.getColumnName(), presentTab));
        }
        tabbedPane.addTab("Attributes", null, makeAttributesPanel(resultMaker), null);

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

        viewTableModel = new ViewTableModel(columns);
        viewTable = new ViewTable(viewTableModel);
        viewTable.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
        viewTable.setColumnSelectionAllowed(true);
        viewTable.setFillsViewportHeight(true);
        viewTable.setRowSelectionAllowed(false);
        viewTable.setCellRenderers();
        viewTable.setColumnWidths();
        viewTable.setRowSorter(new ViewTableSorter(matchmaker, viewTableModel));
        JScrollPane scrollPane = new JScrollPane(viewTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frmCkMatchmaker.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{installDirButton, saveGameFileButton, doLoadButton, exportButton, scrollPane, viewTable}));
        viewPanel.add(scrollPane);
        viewPanel.setVisible(false);
        frmCkMatchmaker.pack();
        String instDir = prefs.get(INSTALL_DIR_PREF_KEY, null);
        if (instDir != null) {
            onInstallDirSet(instDir);
            installDirButton.setText(CHANGE);
            installDirButton.setBackground(Color.GREEN);
        }
        String fName = prefs.get(SAVE_GAME_FILE_PREF_KEY, null);
        if (fName != null) {
            File saveFil = new File(fName);
            if (saveFil.canRead()) {
                onSaveGameFileSet(saveFil);
                saveGameFileButton.setText(CHANGE);
                saveGameFileButton.setBackground(Color.GREEN);
            }
        }
    }

    private Component makeAttributeSubPanel(AttributeColumn column, ResultMaker resultMaker) {
        final AttributeFilter minFilter = new AttributeFilter(resultMaker, matchmaker, column.attributeOrd, true);
        matchmaker.addFilter(minFilter);
        final AttributeFilter maxFilter = new AttributeFilter(resultMaker, matchmaker, column.attributeOrd, false);
        matchmaker.addFilter(maxFilter);
        JPanel attributePanel = new JPanel();
        FlowLayout flowLayout_4 = (FlowLayout) attributePanel.getLayout();
        flowLayout_4.setAlignment(FlowLayout.LEFT);

        JLabel attributeLabel = new JLabel(column.attributeName + "...");
        attributeLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        attributePanel.add(attributeLabel);

        JLabel label_7 = new JLabel("min: ");
        attributePanel.add(label_7);

        UnsettableTextField minFilterField = new UnsettableTextField(3, minFilter);
        attributePanel.add(minFilterField);

        JLabel label_8 = new JLabel(" max: ");
        attributePanel.add(label_8);

        UnsettableTextField maxFilterField = new UnsettableTextField(3, maxFilter);
        attributePanel.add(maxFilterField);

        return attributePanel;
    }

    private Component makeAttributesPanel(ResultMaker resultMaker) {
        JPanel attrFilterPanel = new JPanel();
        attrFilterPanel.setLayout(new BoxLayout(attrFilterPanel, BoxLayout.Y_AXIS));

        for (Column column : columns) {
            if (column instanceof AttributeColumn) {
                attrFilterPanel.add(makeAttributeSubPanel((AttributeColumn) column, resultMaker));
            }
        }
        return attrFilterPanel;
    }


    protected void checkForMods(String rootDir) {
        // first zero out the prior set of mods.
        modsToConsult.clear();
        if (rootDir == null || rootDir.isEmpty()) {
            return;
        }
        final File dirFile = new File(rootDir);
        if (!dirFile.canRead() || !dirFile.isDirectory()) {
            return;
        }
        File[] files = dirFile.listFiles(arg0 -> arg0.isDirectory() && !arg0.equals(dirFile)
        );
        if (files == null) return;
        for (File fil : files) {
            modsToConsult.add(fil.getName());
        }
    }


    /**
     * Populate the drop-downs under this panel, and hook up their filters.
     * It's assumed that (a) there is a left panel with 0 or more inclusion drop-downs,
     * (b) there is a right panel with 0 or more exclusion drop-downs,
     * (c) all the dropdowns are Any-style dropdowns.
     *
     * @param panel  the panel
     * @param helper specifies the semantics
     */
    public static void populateDropdownPane(JComponent panel, DropdownMakerHelper helper, Matchmaker matchmaker) {
        Vector<AnyDropdownable> originalItems = new Vector<>(helper.getOriginalItems());
        originalItems.add(new AnyItemForDropdown());

        originalItems.sort((arg0, arg1) -> {
            if (arg0.isAny() && arg1.isAny()) {
                return 0;
            } else if (arg0.isAny()) {
                return -1;
            } else if (arg1.isAny()) {
                return 1;
            } else {
                return arg0.toString().compareTo(arg1.toString());
            }
        });

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
    public static JPanel mustNotHaveRow() {
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
     *
     * @param newDir newDirectory.
     */
    private void onInstallDirSet(String newDir) {
        if (newDir.equals(installationDirName)) {
            return;
        }
        installationDirName = newDir;
        checkForMods(installationDirName + File.separator + "mod");
        repopulateColumns();
        prefs.put(INSTALL_DIR_PREF_KEY, newDir);
        checkLoadButton();
    }

    private void onSaveGameFileSet(File newFile) {
        if (newFile.equals(saveGameFile)) {
            return;
        }
        saveGameFile = newFile;
        repopulateColumns();
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
