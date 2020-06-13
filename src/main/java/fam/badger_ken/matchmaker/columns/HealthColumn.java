package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.HealthHandler;
import fam.badger_ken.matchmaker.filter.HealthFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class HealthColumn extends Column {

    public HealthColumn() {
        super("Health", new HealthHandler(), HealthHandler.class, SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.health);
    }

    @Override
    public Component setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final HealthFilter minHealthFilter = new HealthFilter(resultMaker, true);
        matchmaker.addFilter(minHealthFilter);
        final HealthFilter maxHealthFilter = new HealthFilter(resultMaker, false);
        matchmaker.addFilter(maxHealthFilter);

        JPanel ageFilterPanel = new JPanel();
        ageFilterPanel.setLayout(new BoxLayout(ageFilterPanel, BoxLayout.Y_AXIS));

        JPanel minAgePanel = new JPanel();
        ageFilterPanel.add(minAgePanel);

        JLabel lblNewLabel_2 = new JLabel("min health:");
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
        minAgePanel.add(lblNewLabel_2);

        UnsettableTextField minAgeField = new UnsettableTextField(3, minHealthFilter);
        minAgePanel.add(minAgeField);

        JPanel maxAgePanel = new JPanel();
        ageFilterPanel.add(maxAgePanel);

        JLabel lblNewLabel_3 = new JLabel("max health:");
        lblNewLabel_3.setHorizontalAlignment(SwingConstants.LEFT);
        maxAgePanel.add(lblNewLabel_3);

        UnsettableTextField maxAgeField = new UnsettableTextField(3, maxHealthFilter);
        maxAgePanel.add(maxAgeField);
        return ageFilterPanel;
    }


}
