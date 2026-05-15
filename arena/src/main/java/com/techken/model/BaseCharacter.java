package com.techken.model;

import com.techken.skills.BaseSkill;

public abstract class BaseCharacter {
    public enum Pose {
        STANCE,
        ATTACK,
        DEFEND,
        WIN,
        LOSE
    }

    private String name;
    private int health;
    private final int maxHealth;
    private int speed;
    private int defense;
    private final int originalDefense;
    private final String spriteId;
    private Pose currentPose = Pose.STANCE;
    protected BaseSkill[] skills; // store lng skills here, protected para ma read sa uban chars

    public BaseCharacter(String name, int health, int speed, int defense, String spriteId) {
        this.name = name;
        this.health = health;
        this.maxHealth = health; // Initial health is max health
        this.speed = speed;
        this.defense = defense;
        this.originalDefense = defense;
        this.spriteId = spriteId;
        this.skills = new BaseSkill[3]; // store skill here, change if naay more
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void resetDefense() {
        this.defense = this.originalDefense;
    }

    public BaseSkill[] getSkills() {
        return skills;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public String getSpriteId() {
        return spriteId;
    }

    public Pose getCurrentPose() {
        return currentPose;
    }

    public void setPose(Pose pose) {
        this.currentPose = pose;
    }

    public String getSpritePath(Pose pose) {
        String poseName = pose.name().substring(0, 1).toUpperCase() + pose.name().substring(1).toLowerCase();
        return String.format("/Images/sprites/%s/%s%s.png", spriteId, spriteId, poseName);
    }

    public Pose getPoseForSkill(BaseSkill skill) {
        if (skill == null) {
            return Pose.STANCE;
        }
        return skill.getDefenseBoost() > 0 ? Pose.DEFEND : Pose.ATTACK;
    }
}
