package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.HealthStealDamageAction;

public class Reptile extends BaseCharacter {

    public Reptile() {
        super("Reptile", 100, 40, 10, "reptile");
        // Skills: Uses HealthSteal to simulate poison/acid
        this.skills[0] = new AttackAction("Force Ball", 20);
        this.skills[1] = new HealthStealDamageAction("Acid Spit", 20);
        this.skills[2] = new HealthStealDamageAction("Toxic Cloud", 15);
    }
}


