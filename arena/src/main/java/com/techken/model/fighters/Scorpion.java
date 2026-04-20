package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.GuardAction;

public class Scorpion extends BaseCharacter {

    public Scorpion() {
        super("Scorpion", 110, 30, 10);

        // Skills: High damage AttackActions and a GuardAction
        this.skills[0] = new AttackAction("Spear (Get Over Here!)", 35);
        this.skills[1] = new AttackAction("Hellfire", 40);
        this.skills[2] = new GuardAction("Netherrealm Guard", 15);
    }
}
