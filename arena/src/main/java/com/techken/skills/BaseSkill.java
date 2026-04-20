package com.techken.skills;
public abstract class BaseSkill {
    private String skillName;

    public BaseSkill(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillName() {
        return skillName;
    }
}