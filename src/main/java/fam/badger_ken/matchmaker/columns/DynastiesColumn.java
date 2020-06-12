package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.DynastyHandler;
import fam.badger_ken.matchmaker.filter.DynastyFilter;
import fam.badger_ken.matchmaker.widget.DynastySelector;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Optional;

import static fam.badger_ken.matchmaker.SwingGui.mustNotHaveRow;


public class DynastiesColumn extends Column {

    public DynastiesColumn(Matchmaker matchmaker) {
        super("Dynasties", new DynastyHandler(matchmaker));
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return Util.csvEscape(winner.getDisplayDynasty(saveState));
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final DynastyFilter yesDynastyFilter = new DynastyFilter(resultMaker, true);
        matchmaker.addFilter(yesDynastyFilter);


        JPanel dynastyFilterPanel = new JPanel();

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
                    for (Component child : root.getComponents()) {
                        populateDynasties((JComponent) child);
                    }
                }
            }

            @Override
            public void componentShown(ComponentEvent evt) {
                if (shouldRepopulateData) {
                    populateDynasties((JComponent) evt.getSource());
                }
                shouldRepopulateData = false;
            }
        });

        return Optional.of(dynastyFilterPanel);
    }


}
