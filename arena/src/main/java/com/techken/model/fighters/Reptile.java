package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.HealthStealDamageAction;

public class Reptile extends BaseCharacter {

    public Reptile() {
        super("Reptile", 90, 40, 10);
        // Skills: Uses HealthSteal to simulate poison/acid
        this.skills[0] = new AttackAction("Force Ball", 20);
        this.skills[1] = new HealthStealDamageAction("Acid Spit", 20);
        this.skills[2] = new HealthStealDamageAction("Toxic Cloud", 15);
    }

    @Override
    public void takeDamage(int damage) {
        // High evasion: 30% chance to dodge the attack completely
        if (Math.random() < 0.30) {
            System.out.println(">>> Reptile DODGED the attack! (Evasion) <<<");
        } else {
            super.takeDamage(damage);
        }
    }
}


