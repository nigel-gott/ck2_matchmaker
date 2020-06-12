package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.NameHandler;

import java.awt.*;
import java.util.Optional;


public class NameColumn extends Column {

    public NameColumn(Matchmaker matchmaker) {
        super("Name", new NameHandler(matchmaker));
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return Util.csvEscape(winner.getDisplayName(gameConfig));
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        return Optional.empty();
    }


}
