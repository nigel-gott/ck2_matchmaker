// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import fam.badger_ken.matchmaker.columns.Columns;
import fam.badger_ken.matchmaker.filter.FiltererFilter;

import java.io.*;
import java.util.*;

/**
 * The main top-level driver class.
 */
public class Matchmaker implements ParserPruner {

    public static final String VERSION_NUMBER = "2.0";
    public SaveState saveState;
    public GameConfig gameConfig;
    public List<Person> winners;
    private Set<Filterer> filters = new HashSet<>();
    private String saveFileTail = "";
    // used by the parser pruner - nodes with these tags are discarded.
    private final String[] tagsToPrune =
            {"ai", "ledger", "army", "navy", "history", "active_war", "combat", "tech_focus",
                    "coat_of_arms", "technology", "levy"};
    Set<String> loadedModNames = null;
    public StringBuilder traitsTrace = null;

    public Matchmaker() {
        saveState = new SaveState();
        gameConfig = new GameConfig();
    }

    public void addFilter(Filterer filter) {
        filters.add(filter);
    }

    public void removeFilters(FiltererFilter filterfilter) {
        // copy it over, then remove, as you can't remove from a set
        // you're iterating over.
        Set<Filterer> newSet = new HashSet<>(filters);
        for (Filterer filter : filters) {
            if (!filterfilter.passes(filter)) {
                newSet.remove(filter);
            }
        }
        filters = newSet;
    }

    public int numActiveFilters() {
        if (filters == null) return 0;
        int count = 0;
        for (Filterer filter : filters) {
            if (filter.isOn()) {
                count++;
            }
        }
        return count;
    }

    public void process(String installationDir, File saveFile, ReligionGroup.PruningMode religionsToLoad,
                        Set<String> modsToConsult, String traitsBase)
            throws FileNotFoundException {
        loadedModNames = modsToConsult;
        // this is tricky. Normally you would load the save-independent game configuration
        // first, then the save file. however, to save memory, we only want to load the
        // parts of the game config that we need - there are vastly more dynasties than
        // are used, e.g.
        // however, part of the save game parsing DOES need config state, e.g. what the
        // religions and religion groups are. so we zig-zag:
        // first, just part of the game config:
        if (gameConfig == null) gameConfig = new GameConfig();
        gameConfig.loadReligions(installationDir + File.separator + "common" + File.separator + "religions");
        // 'stamp' the religion groups as per pruning mode:
        for (ReligionGroup group : gameConfig.religionGroupsByLabel.values()) {
            switch (religionsToLoad) {
                case ALL:
                    group.ignoreOnLoad = false;
                    break;
                case CHRISTIAN_ONLY:
                    group.ignoreOnLoad = !group.label.equals("christian");
                    break;
                case MUSLIM_ONLY:
                    group.ignoreOnLoad = !group.label.equals("muslim");
            }
        }
        // then the save file
        saveFileTail = saveFile.getName();
        if (saveState == null) {
            saveState = new SaveState();
        }
        Node root = new Node("save");
        ParadoxParser parser = new ParadoxParser(saveFile.getPath(), "save file parser");
        parser.Parse(root, this);
        saveState.rawDate = root.findAttribute("date");
        if (saveState.rawDate == null) {
            System.out.println("Its null, dump!");
            root.dumpAttributes();
            throw new RuntimeException("Failed blah");
        }
        saveState.loadArtifacts(root.findDescendant("artifacts"));

        saveState.playerRealm = root.findAttribute("player_realm");
        // figure out what all the places are that are 'held' by a character,
        // and which. the place can be a barony, county, duchy, kingdom, or empire.
        saveState.loadHeldPlaces(root.findDescendant("title"));
        Node characters = root.findDescendant("character");
        saveState.loadCharacters(characters);
        saveState.setPlayerCharacter(root.findDescendant("player"));
        // do AFTER finding characters
        // load the dynasties from the save-game file:
        Node dynasties = root.findDescendant("dynasties");
        saveState.loadDynasties(dynasties);

        // and load each from dynasties.txt:
//    parser = new ParadoxParser(
//        installationDir + File.separator + "common" + File.separator + "dynasties.txt",
//        "dynasties.txt parser");
//    dynasties = new Node("dynasties.txt parser");
//    parser.Parse(dynasties);
//    saveState.loadDynasties(dynasties);
        parseDynastiesDir(installationDir + File.separator + "common" + File.separator + "dynasties");
        saveState.pruneAndFinalize();

        // then the rest of the game config
        traitsTrace = new StringBuilder();
        parseTraitsDir(traitsBase + File.separator + "common" + File.separator + "traits", traitsTrace);
        gameConfig.loadLocalisation(installationDir + File.separator + "localisation",
                saveState.neededCultures(),
                saveState.neededTitles(), saveState.neededDemesnes(), traitsTrace);
        if (modsToConsult != null) {
            for (String modName : modsToConsult) {
                String modLocalisationDir = installationDir + File.separator + "mod"
                        + File.separator + modName + File.separator + "localisation";
                gameConfig.loadLocalisation(modLocalisationDir,
                        saveState.neededCultures(),
                        saveState.neededTitles(), saveState.neededDemesnes(), traitsTrace);
            }
        }
        removeUnneededReligions();
    }

    /**
     * Any religions which were defined in the config, but which we loaded no people for,
     * shouldn't show up in the UI, it's too confusing.
     */
    private void removeUnneededReligions() {
        Set<String> allLabels = new HashSet<>(gameConfig.religionsByLabel.keySet());
        for (Person person : saveState.people.values()) {
            allLabels.remove(person.religionLabel);
        }
        for (String unused : allLabels) {
            gameConfig.religionsByLabel.remove(unused);
        }

    }

    public void clear() {
        saveState = null;
        gameConfig = null;
    }


    private void parseDynastiesDir(String dirBase) {
        File dir = new File(dirBase);
        if (!dir.isDirectory() || !dir.canRead()) return;
        File[] filesIn = dir.listFiles((dir1, filename) -> filename != null && filename.endsWith(".txt"));
        for (File file : Util.sortFiles(Objects.requireNonNull(filesIn))) {
            try {
                ParadoxParser parser = new ParadoxParser(file.getCanonicalPath(), "dynasties parser");
                Node dynastiesRoot = new Node("dynasties");
                parser.Parse(dynastiesRoot);
                saveState.loadDynasties(dynastiesRoot);
            } catch (IOException ignored) {
            }
        }
    }


    private void parseTraitsDir(String dirBase, final StringBuilder traitsTrace) {
        if (traitsTrace != null) {
            traitsTrace.append("\nlooking for traits files in ").append(dirBase);
        }
        File dir = new File(dirBase);
        if (!dir.isDirectory() || !dir.canRead()) return;
        File[] filesIn = dir.listFiles((dir1, filename) -> {
            boolean passes = filename != null && filename.endsWith(".txt");
            if (traitsTrace != null) {
                traitsTrace.append("\nfile ").append(filename).append(passes ? " will " : " will NOT").append(" be looked at for traits");
            }
            return passes;
        });
        gameConfig.traits.clear();
        gameConfig.traitsByLabel.clear();
        for (File file : Util.sortFiles(Objects.requireNonNull(filesIn))) {
            try {
                ParadoxParser parser = new ParadoxParser(file.getCanonicalPath(), "traits parser");
                Node traitsRoot = new Node("traits");
                if (traitsTrace != null) {
                    traitsTrace.append("\nlooking for traits in '").append(file.getCanonicalPath()).append("'");
                }
                parser.Parse(traitsRoot);
                gameConfig.parseTraits(traitsRoot, traitsTrace);

            } catch (IOException ignored) {
            }
        }
    }

    public String loadSummary() {
        if (saveState == null) {
            return "failed to load.";
        }

        String answer = "loaded " + saveState.people.size() + " nobles "
                + " and " + saveState.dynasties.size() + " dynasties"
                + " from " + saveFileTail;
        if (loadedModNames != null && !loadedModNames.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(answer);
            sb.append(" with the ");
            int i = 0;
            for (String modName : loadedModNames) {
                if (i != 0 && i != loadedModNames.size() - 1) {
                    sb.append(", ");
                } else if (i != 0 && i == loadedModNames.size() - 1) {
                    sb.append(" and ");
                }
                sb.append("'");
                sb.append(modName);
                sb.append("'");
                i++;
            }
            answer = sb.toString();
            answer += (loadedModNames.size() == 1) ? " mod" : " mods";
        }
        return answer;
    }

    /**
     * Apply the current filters to get winners.
     */
    public String filter() {
        int maxToShow = 400;
        if (saveState == null || saveState.people.size() == 0) {
            winners = new ArrayList<>();
            return " none to show";
        }
        winners = new ArrayList<>(maxToShow);
        String descr;
        int numThatPass = 0;
        for (Person person : saveState.people.values()) {
            boolean winner = true;
            if (filters != null) {
                for (Filterer filter : filters) {
                    if (!filter.passes(person)) {
                        winner = false;
                        break;
                    }
                }
            }
            if (winner) {
                numThatPass++;
                if (winners.size() < maxToShow) {
                    winners.add(person);
                }
            }
        }
        descr = numThatPass + " pass";
        if (numThatPass > winners.size()) {
            descr += " -- showing a random " + winners.size();
        }
        return descr;
    }


    public void csvExportTo(File selectedFile, ViewTableModel tableModel, boolean showAll) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile));
            writeHeader(bw, tableModel);
            writeWinners(bw, showAll, tableModel.getColumns());
        } catch (IOException e) {
            System.err.println("Could not open output file");
        }
    }

    private final static String SEP = ",";

    private void writeWinners(BufferedWriter bw, boolean showAll, Columns columns) throws IOException {
        if (winners == null) {
            return;
        }
        for (Person winner : showAll ? saveState.people.values() : winners) {
            bw.write(winner.key.toString());
            for (Column column : columns) {
                bw.write(SEP + column.convertPersonToCsv(winner, gameConfig, saveState));
            }


//      // ruler?
//      //bw.write(SEP + (winner.isRuler() ? "1" : "0"));

            bw.newLine();
        }
        bw.close();
    }


    private void writeHeader(BufferedWriter bw, ViewTableModel tableModel) throws IOException {
        // first comes the 'id'
        bw.write("id");
        // now the columns in the UI:
        for (int col = 0; col < tableModel.getColumnCount(); col++) {
            String colName = tableModel.getColumnName(col);
            bw.write(SEP);
            bw.write(Util.csvEscape(colName));
        }
        bw.newLine();
    }

    @Override
    public boolean discard(Node parent, Node child) {
        if (parent == null) {
            return false;
        }
        if (child == null) {
            return true;
        }
        if (child.tag == null) {
            return false;
        }
        for (String blacklisted : tagsToPrune) {
            if (blacklisted.equals(child.tag)) {
                return true;
            }
        }
        /*
         * We don't care about dead people.
         * If the child has a 'death_date' attribute,
         * and it's parent is the 'character' node, kill it.
         */
        if (parent.tag.equals("character")) {
            if (child.findAttribute("d_d") != null) {
                return true;
            }
            String religionLabel = child.findAttribute("rel");
            Religion religion = gameConfig.religionsByLabel.get(religionLabel);
            return religion != null && religion.group != null && religion.group.ignoreOnLoad;
        }
        return false;
    }

    public String getTraitsDump() {
        return traitsTrace.toString();
    }
}
