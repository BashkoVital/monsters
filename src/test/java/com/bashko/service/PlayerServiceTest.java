package com.bashko.service;

import com.bashko.entity.Dice;
import com.bashko.entity.Monster;
import com.bashko.entity.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {
    private Player testPlayer;
    private PlayerService playerService;
    @Mock
    private Random random;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void init() {
        testPlayer = Player.builder()
                .name("Иван")
                .health(100)
                .attack(1)
                .protection(1)
                .countOfHealing(4)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();

        playerService = new PlayerService(random);
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void attackToMonsterWhenModifierOfAttackOne() {
        Monster testMonster1 = Monster.builder()
                .name("Monster#1")
                .health(100)
                .attack(1)
                .protection(1)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();
        Monster testMonster2 = Monster.builder()
                .name("Monster#2")
                .health(100)
                .attack(1)
                .protection(1)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();
        Dice testDice = new Dice();
        List<Monster> monsters = new ArrayList<>();
        monsters.add(testMonster1);
        monsters.add(testMonster2);

        when(random.nextInt(monsters.size())).thenReturn(0);
        when(random.nextInt(1, testDice.getMaxValue() + 1)).thenReturn(5);
        when(random.nextInt(testPlayer.getDamage().length)).thenReturn(2);

        Monster monsterExpected = Monster.builder()
                .name(testMonster1.getName())
                .health(testMonster1.getHealth())
                .attack(testMonster1.getAttack())
                .protection(testMonster1.getProtection())
                .damage(testMonster1.getDamage())
                .build();
        monsterExpected.setHealth(monsterExpected.getHealth() - 3);

        playerService.attackToMonster(testPlayer, monsters, testDice);
        assertEquals(monsterExpected.getHealth(), testMonster1.getHealth());
    }

    @Test
    void attackToMonsterWhenModifierOfAttackMoreOne() {
        Player localTestPlayer = Player.builder()
                .name("Иван")
                .health(100)
                .attack(4)
                .protection(2)
                .countOfHealing(4)
                .damage(new Integer[]{3, 4, 5, 6, 7, 8})
                .build();
        Monster testMonster1 = Monster.builder()
                .name("Monster#1")
                .health(100)
                .attack(1)
                .protection(1)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();
        Monster testMonster2 = Monster.builder()
                .name("Monster#2")
                .health(100)
                .attack(1)
                .protection(1)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();
        Dice testDice = new Dice();
        List<Monster> monsters = new ArrayList<>();
        monsters.add(testMonster1);
        monsters.add(testMonster2);

        when(random.nextInt(monsters.size())).thenReturn(0);
        when(random.nextInt(1, testDice.getMaxValue() + 1)).thenReturn(5);
        when(random.nextInt(testPlayer.getDamage().length)).thenReturn(2);

        Monster monsterExpected = Monster.builder()
                .name(testMonster1.getName())
                .health(testMonster1.getHealth())
                .attack(testMonster1.getAttack())
                .protection(testMonster1.getProtection())
                .damage(testMonster1.getDamage())
                .build();
        monsterExpected.setHealth(monsterExpected.getHealth() - 5);

        playerService.attackToMonster(localTestPlayer, monsters, testDice);
        assertEquals(monsterExpected.getHealth(), testMonster1.getHealth());
    }

    @Test
    void attackToMonsterWhenKillMonster() {
        Player localTestPlayer = Player.builder()
                .name("Иван")
                .health(100)
                .attack(4)
                .protection(2)
                .countOfHealing(4)
                .damage(new Integer[]{3, 4, 5, 6, 7, 8})
                .build();
        Monster testMonster1 = Monster.builder()
                .name("Monster#1")
                .health(3)
                .attack(1)
                .protection(1)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();
        Monster testMonster2 = Monster.builder()
                .name("Monster#2")
                .health(100)
                .attack(1)
                .protection(1)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build();
        Dice testDice = new Dice();
        List<Monster> monsters = new ArrayList<>();
        monsters.add(testMonster1);
        monsters.add(testMonster2);

        when(random.nextInt(monsters.size())).thenReturn(0);
        when(random.nextInt(1, testDice.getMaxValue() + 1)).thenReturn(5);
        when(random.nextInt(testPlayer.getDamage().length)).thenReturn(2);


        String expected = "Вы убили монстра\r\n";
        List<Monster> expectedList = List.of(testMonster2);
        playerService.attackToMonster(localTestPlayer, monsters, testDice);

        assertEquals(expected, outputStream.toString());
        assertEquals(expectedList, monsters);
    }


    @Test
    void getMoreHealthForPlayerCaseLessFull() {
        int healthExpected = 90;
        int countOfHealingExpected = 3;

        testPlayer.setHealth(60);
        playerService.getMoreHealthForPlayer(testPlayer);
        int healthActual = testPlayer.getHealth();
        int countOfHealingActual = testPlayer.getCountOfHealing();

        assertEquals(healthExpected, healthActual);
        assertEquals(countOfHealingExpected, countOfHealingActual);
    }

    @Test
    void getMoreHealthForPlayerCaseMoreFull() {
        int healthExpected = 100;

        testPlayer.setHealth(80);
        playerService.getMoreHealthForPlayer(testPlayer);
        int healthActual = testPlayer.getHealth();

        assertEquals(healthExpected, healthActual);

    }

    @Test
    void isPlayerAliveFalse() {
        boolean expected = false;
        testPlayer.setHealth(0);
        boolean actual = playerService.isPlayerAlive(testPlayer);
        assertEquals(expected, actual);
    }

    @Test
    void isPlayerAliveTrue() {
        boolean expected = true;
        boolean actual = playerService.isPlayerAlive(testPlayer);
        assertEquals(expected, actual);
    }

    @Test
    void createNewPlayer() {
        Player expectedPlayer = testPlayer;
        Player personage = PlayerService.thePersonagesOfTheGame.get("Человек");
        expectedPlayer.setAttack(personage.getAttack());
        expectedPlayer.setProtection(personage.getProtection());
        expectedPlayer.setDamage(personage.getDamage());
        Player actual = playerService.createNewPlayer(personage, "Иван");
        assertEquals(expectedPlayer, actual);
    }

    @Test
    void printPlayer() {
        StringBuilder sb = new StringBuilder();
        sb.append(testPlayer.getName()).append(" : ");
        for (int i = 0; i < testPlayer.getHealth(); i++) {
            sb.append("*");
        }
        sb.append("  (").append(testPlayer.getHealth()).append("%)\r\n");
        playerService.printPlayer(testPlayer);
        assertEquals(sb.toString(), outputStream.toString());
    }


    @Test
    void toStringPersonage() {
        Player personage = PlayerService.thePersonagesOfTheGame.get("Человек");
        String expectedStringPersonage = "\"" + personage.getName() + "\": " + "атака - " + personage.getAttack() + ", защита - " + personage.getProtection() + ", урон от " + personage.getDamage()[0] + " до " + personage.getDamage()[5] + ".";
        String actualStringPersonage = playerService.toStringPersonage(personage);
        assertEquals(expectedStringPersonage, actualStringPersonage);
    }

    @AfterEach
    void closedAll() {
        System.setOut(System.out);
    }
}