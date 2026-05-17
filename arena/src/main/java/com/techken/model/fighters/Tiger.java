package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;

public class Tiger extends BaseCharacter {

    public Tiger() {
        super("Tiger", 120, 25, 15, "tiger");

        this.skills[0] = new AttackAction("Flying Dragon Kick", 22);
        this.skills[1] = new AttackAction("Bicycle Kick", 22);
        this.skills[2] = new AttackAction("Fireball", 25);
    }
}