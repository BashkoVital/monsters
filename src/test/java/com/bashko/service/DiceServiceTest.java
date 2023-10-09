package com.bashko.service;

import com.bashko.entity.Dice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceServiceTest {

    private DiceService diceService;

    @BeforeEach
    void init(){
        diceService = new DiceService();
    }

    @Test
    void getDice() {
        Dice expectedDice = new Dice();

        Dice actualDice = diceService.getDice();

        assertEquals(expectedDice, actualDice);
    }
}