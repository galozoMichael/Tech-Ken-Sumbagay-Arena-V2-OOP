package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;

public class JohnnyCage extends BaseCharacter {

    public JohnnyCage() {
        // Moderate Health (100), High Speed (50) for turn priority, Moderate Defense (15)
        super("Johnny Cage", 100, 50, 15);

        // Skillset: Fast AttackActions
        this.skills[0] = new AttackAction("Shadow Kick", 20);
        this.skills[1] = new AttackAction("Nut Punch", 25);
        this.skills[2] = new AttackAction("Forceball", 18);
    }
}
