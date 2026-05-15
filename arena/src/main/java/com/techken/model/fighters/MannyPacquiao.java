package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.GuardAction;

public class MannyPacquiao extends BaseCharacter {

    public MannyPacquiao() {
        super("Manny Pacquiao", 95, 70, 12, "mannypacquiao");

        this.skills[0] = new AttackAction("Jab", 15);
        this.skills[1] = new GuardAction("High Guard", 20);
        this.skills[2] = new AttackAction("Double Cross", 35);
    }
}