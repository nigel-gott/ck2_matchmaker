package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.ArtifactsHandler;
import fam.badger_ken.matchmaker.cell.GenderHandler;
import fam.badger_ken.matchmaker.filter.HasArtifactFilter;
import fam.badger_ken.matchmaker.widget.TwoPlusAnyComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class ArtifactColumn extends Column {

    public ArtifactColumn() {
        super("Artifacts", new ArtifactsHandler(), 3 * SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return Util.csvEscape(winner.getDisplayableArtifacts());
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final HasArtifactFilter artifactFilter = new HasArtifactFilter(resultMaker);
        matchmaker.addFilter(artifactFilter);
        JPanel artifactFilterPanel = new JPanel();

        TwoPlusAnyComboBox artifactComboBox = new TwoPlusAnyComboBox("Has Artifact", "No Artifacts", artifactFilter);
        artifactFilterPanel.add(artifactComboBox);


        return Optional.of(artifactFilterPanel);
    }


}
