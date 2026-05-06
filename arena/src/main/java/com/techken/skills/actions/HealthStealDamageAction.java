package com.techken.skills.actions;
import com.techken.skills.BaseSkill;
public class HealthStealDamageAction extends BaseSkill {
    private int damage;

    public HealthStealDamageAction(String skillName, int damage) {
        super(skillName);
        this.damage = damage;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public int getHealAmount(int damageDealt) {
        return damageDealt / 2;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }


}