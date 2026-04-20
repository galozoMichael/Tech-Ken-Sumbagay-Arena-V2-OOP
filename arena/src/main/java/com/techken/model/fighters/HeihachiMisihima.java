package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.GuardAction;

public class HeihachiMisihima extends BaseCharacter {

    public HeihachiMisihima() {
        // Tank Stats: High Health (150), Low Speed (5), High Defense (25)
        super("Heihachi Mishima", 150, 5, 25);
        
        this.skills[0] = new AttackAction("Electric Wind God Fist", 30);
        this.skills[1] = new AttackAction("Stonehead", 25);
        this.skills[2] = new GuardAction("Vajra Block", 20);
    }
}
