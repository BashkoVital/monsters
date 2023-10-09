package com.bashko.service;

import com.bashko.entity.Dice;
import com.bashko.entity.Monster;
import com.bashko.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
class MonsterServiceTest {
    private List<Monster> monsters;
    private Monster testMonster1;
    private Monster testMonster2;
    private MonsterService monsterService;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    @Mock
    private Random random;

    @BeforeEach
    void init() {
        testMonster1 = Monster.builder()
                .name("Monster#1")
                .health(100)
                .attack(1)
                .protection(1)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();
        testMonster2 = Monster.builder()
                .name("Monster#2")
                .health(100)
                .attack(1)
                .protection(1)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();

        monsters = List.of(testMonster1, testMonster2);
        monsterService = new MonsterService(random);
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void attackToPlayerWhenModifierOfAttackOne() {
        Player testPlayer = Player.builder()
                .name("Иван")
                .health(100)
                .attack(1)
                .protection(1)
                .countOfHealing(4)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();

        Dice testDice = new Dice();
        when(random.nextInt(monsters.size())).thenReturn(0);
        when(random.nextInt(1, testDice.getMaxValue() + 1)).thenReturn(5);
        when(random.nextInt(testMonster1.getDamage().length)).thenReturn(2);

        Player playerExpected = Player.builder()
                .name(testPlayer.getName())
                .health(testPlayer.getHealth())
                .attack(testPlayer.getAttack())
                .protection(testPlayer.getProtection())
                .damage(testPlayer.getDamage())
                .build();
        playerExpected.setHealth(playerExpected.getHealth() - 3);

        monsterService.attackToPlayer(monsters, testPlayer, testDice);
        assertEquals(playerExpected.getHealth(), testPlayer.getHealth());
    }

    @Test
    void attackToPlayerWhenModifierOfAttackMoreOne() {
        Monster testMonster3 = Monster.builder()
                .name("Monster#2")
                .health(100)
                .attack(4)
                .protection(1)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();
        Player testPlayer = Player.builder()
                .name("Иван")
                .health(100)
                .attack(1)
                .protection(1)
                .countOfHealing(4)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();
        List<Monster> monsters = new ArrayList<>();

        monsters.add(testMonster1);
        monsters.add(testMonster3);

        Dice testDice = new Dice();
        when(random.nextInt(monsters.size())).thenReturn(1);
        when(random.nextInt(1, testDice.getMaxValue() + 1)).thenReturn(5);
        when(random.nextInt(testMonster1.getDamage().length)).thenReturn(2);

        Player playerExpected = Player.builder()
                .name(testPlayer.getName())
                .health(testPlayer.getHealth())
                .attack(testPlayer.getAttack())
                .protection(testPlayer.getProtection())
                .damage(testPlayer.getDamage())
                .build();
        playerExpected.setHealth(playerExpected.getHealth() - 3);

        monsterService.attackToPlayer(monsters, testPlayer, testDice);
        assertEquals(playerExpected.getHealth(), testPlayer.getHealth());
    }

    @Test
    void isMonstersAliveReturnTrue() {
        boolean actual = monsterService.isMonstersAlive(monsters);
        assertTrue(actual);
    }

    @Test
    void isMonstersAliveReturnFalse() {
        for (Monster m : monsters) {
            m.setHealth(0);
        }
        boolean actual = monsterService.isMonstersAlive(monsters);
        assertFalse(actual);
    }

    @Test
    void createMonsters() {
        List<Monster> expected = monsters;
        List<Monster> actual = monsterService.createMonsters(monsters.size());

        assertEquals(expected, actual);
    }

    @Test
    void printMonsters() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < monsters.size(); i++) {

            sb.append(monsters.get(i).getName()).append(" : ");
            for (int j = 0; j < monsters.get(i).getHealth(); j++) {
                sb.append("*");
            }
            sb.append("  (").append(monsters.get(i).getHealth()).append("%)\r\n");

        }
        monsterService.printMonsters(monsters);
        assertEquals(sb.toString(), outputStream.toString());
    }

    @AfterEach
    void closedAll() {
        System.setOut(System.out);
    }
}