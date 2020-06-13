package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.AgeHandler;
import fam.badger_ken.matchmaker.filter.AgeFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class AgeColumn extends Column {

    public AgeColumn() {
        super("Age", new AgeHandler(), AgeFilter.class, SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.ageInYears);
    }

    @Override
    public Component setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final AgeFilter minAgeFilter = new AgeFilter(resultMaker, true);
        matchmaker.addFilter(minAgeFilter);
        final AgeFilter maxAgeFilter = new AgeFilter(resultMaker, false);
        matchmaker.addFilter(maxAgeFilter);

        JPanel ageFilterPanel = new JPanel();
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
        return ageFilterPanel;
    }


}
