package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;

public class JohnnyCage extends BaseCharacter {

    public JohnnyCage() {
        // Moderate Health (100), High Speed (50) for turn priority, Moderate Defense (15)
        super("Johnny Cage", 105, 50, 12, "johnnycage");

        // Skillset: Fast AttackActions
        this.skills[0] = new AttackAction("Shadow Kick", 22);
        this.skills[1] = new AttackAction("Nut Punch", 28);
        this.skills[2] = new AttackAction("Forceball", 18);
    }
}
