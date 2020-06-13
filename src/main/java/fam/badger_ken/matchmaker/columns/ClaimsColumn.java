package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.ClaimsHandler;
import fam.badger_ken.matchmaker.filter.ClaimsFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class ClaimsColumn extends Column {

    public ClaimsColumn() {
        super("Claims", new ClaimsHandler(), ClaimsFilter.class, SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.numClaims);
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final ClaimsFilter minClaimsFilter = new ClaimsFilter(resultMaker, true);
        final ClaimsFilter maxClaimsFilter = new ClaimsFilter(resultMaker, false);
        matchmaker.addFilter(minClaimsFilter);
        matchmaker.addFilter(maxClaimsFilter);

        JPanel claimsFilterPanel = new JPanel();
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


        return Optional.of(claimsFilterPanel);
    }


}
