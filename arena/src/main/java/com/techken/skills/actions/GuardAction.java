package com.techken.skills.actions;

import com.techken.skills.BaseSkill;

public class GuardAction extends BaseSkill {
    private int defenseBoost;

    public GuardAction(String skillName, int defenseBoost) {
        super(skillName);
        this.defenseBoost = defenseBoost;
    }

    public int getDefenseBoost() {
        return defenseBoost;
    }
}
