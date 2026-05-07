package com.techken.skills;
public abstract class BaseSkill {
    private String skillName;

    public BaseSkill(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillName() {
        return skillName;
    }

    // base damage gi move nako tanan here kay para limpyo
    public int getDamage() {
        return 0;
    }

    // defense boost sa tanks or whatever
    public int getDefenseBoost() {
        return 0;
    }

    // paras sa lifesteal skills
    public int getHealAmount(int damageDealt) {
        return 0;
    }
}