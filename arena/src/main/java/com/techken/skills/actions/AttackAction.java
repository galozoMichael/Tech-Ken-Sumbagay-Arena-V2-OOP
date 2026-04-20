package com.techken.skills.actions;

import com.techken.skills.BaseSkill;

public class AttackAction extends BaseSkill {
    private int damage;

    public AttackAction(String skillName, int damage) {
        super(skillName);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}