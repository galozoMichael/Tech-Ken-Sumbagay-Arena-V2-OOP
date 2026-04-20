package com.techken.utils;

import com.techken.model.BaseCharacter;
import com.techken.skills.BaseSkill;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.HealthStealDamageAction;

public class CombatMath {

    public int calculateDamage(BaseSkill skill, BaseCharacter defender) {
        int baseDamage = 0;

        if (skill instanceof AttackAction) {
            baseDamage = ((AttackAction) skill).getDamage();
        } else if (skill instanceof HealthStealDamageAction) {
            baseDamage = ((HealthStealDamageAction) skill).getDamage();
        } else {
            return 0;
        }

        int finalDamage = baseDamage - defender.getDefense();

        return Math.max(finalDamage, 1);
    }
}