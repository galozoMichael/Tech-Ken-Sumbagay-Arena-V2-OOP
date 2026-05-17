package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.GuardAction;

public class HeihachiMisihima extends BaseCharacter {

    public HeihachiMisihima() {
        // Tank Stats: High Health (150), Low Speed (5), High Defense (25)
        super("Heihachi Mishima", 140, 5, 22, "heihachimishima");
        
        this.skills[0] = new AttackAction("Electric Wind God Fist", 28); // nerfed gamay kay op
        this.skills[1] = new AttackAction("Stonehead", 18); // also dis
        this.skills[2] = new GuardAction("Vajra Block", 25);
    }
}
