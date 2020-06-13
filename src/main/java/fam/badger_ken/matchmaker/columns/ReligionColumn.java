package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.ReligionHandler;
import fam.badger_ken.matchmaker.filter.ReligionFilter;
import fam.badger_ken.matchmaker.widget.AnyDropdownable;
import fam.badger_ken.matchmaker.widget.UnsettableComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.Optional;

import static fam.badger_ken.matchmaker.SwingGui.mustNotHaveRow;
import static fam.badger_ken.matchmaker.SwingGui.populateDropdownPane;


public class ReligionColumn extends Column {

    public ReligionColumn(Matchmaker matchmaker) {
        super("Religion", new ReligionHandler(matchmaker), ReligionFilter.class);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return Util.csvEscape(winner.getDisplayReligion(gameConfig));
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        JPanel religionFilterPanel = new JPanel();
        religionFilterPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                // the first time the religion tab is shown, populate the
                // drop-downs - they have no content until the load happens.
                if (shouldRepopulateData) {
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
                            }, matchmaker);
                }
                shouldRepopulateData = false;
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


        return Optional.of(religionFilterPanel);
    }


}
