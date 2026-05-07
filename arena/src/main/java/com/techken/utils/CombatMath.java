package com.techken.utils;

import com.techken.model.BaseCharacter;
import com.techken.skills.BaseSkill;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.HealthStealDamageAction;

public class CombatMath {


    // rework kay hugaw kaau ang instance di masabtan nako aint no way
    public int calculateDamage(BaseSkill skill, BaseCharacter defender) {
        int baseDamage = skill.getDamage();
        if (baseDamage == 0) {
            return 0;
        }
        int finalDamage = baseDamage - defender.getDefense();
        return Math.max(finalDamage, 1);

    }
}