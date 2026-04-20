package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;

public class LiuKang extends BaseCharacter {

    public LiuKang() {
        super("Liu Kang", 130, 25, 20);

        // Skills: Moderate damage Attack Action
        this.skills[0] = new AttackAction("Flying Dragon Kick", 25);
        this.skills[1] = new AttackAction("Bicycle Kick", 25);
        this.skills[2] = new AttackAction("Fireball", 30);
    }
}