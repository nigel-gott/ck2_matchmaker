package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.MarriageHandler;
import fam.badger_ken.matchmaker.filter.SpousesFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class SpouseColumn extends Column {

    public SpouseColumn() {
        super("Spouses", new MarriageHandler(), SpousesFilter.class, SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.getNumSpouses());
    }

    @Override
    public Component setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final SpousesFilter minSpousesFilter = new SpousesFilter(resultMaker, true);
        final SpousesFilter maxSpousesFilter = new SpousesFilter(resultMaker, false);
        matchmaker.addFilter(minSpousesFilter);
        matchmaker.addFilter(maxSpousesFilter);

        JPanel spouseFilterPanel = new JPanel();

        spouseFilterPanel.setLayout(new BoxLayout(spouseFilterPanel, BoxLayout.Y_AXIS));

        JPanel minSpousesPanel = new JPanel();
        spouseFilterPanel.add(minSpousesPanel);

        JLabel lblNewLabel_2 = new JLabel("min Spouses:");
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
        minSpousesPanel.add(lblNewLabel_2);

        UnsettableTextField minSpousesField = new UnsettableTextField(3, minSpousesFilter);
        minSpousesPanel.add(minSpousesField);

        JPanel maxSpousesPanel = new JPanel();
        spouseFilterPanel.add(maxSpousesPanel);

        JLabel lblNewLabel_3 = new JLabel("max Spouses:");
        lblNewLabel_3.setHorizontalAlignment(SwingConstants.LEFT);
        maxSpousesPanel.add(lblNewLabel_3);

        UnsettableTextField maxSpousesField = new UnsettableTextField(3, maxSpousesFilter);
        maxSpousesPanel.add(maxSpousesField);

        return spouseFilterPanel;
    }


}
