package com.bashko.controllers;

import com.bashko.entity.Monster;
import com.bashko.entity.Player;
import com.bashko.service.DiceService;
import com.bashko.service.MonsterService;
import com.bashko.service.PlayerService;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Log4j2
public class InitController {
    private PlayController playController;
    private PlayerService playerService;
    private MonsterService monsterService;
    private DiceService diceService;
    private Scanner scanner;


    public void run() {
        this.monsterService = new MonsterService(new Random());
        this.playerService = new PlayerService(new Random());
        this.diceService = new DiceService();
        this.scanner = new Scanner(System.in);
        this.playController = new PlayController(playerService, monsterService, scanner);
        init();
    }

    private void init() {
        System.out.println("Введите свое имя...");

        String playerName = scanner.next();
        while (!playerName.matches("[а-яёА-ЯЁ]+")) {
            log.debug("An incorrect name has been introduced - {}", playerName);
            System.out.println("Введено некорректное имя. Имя должно состоять из букв кириллического алфавита");
            playerName = scanner.next();
        }
        System.out.println("Добро пожаловать в игру, " + playerName + "!");

        System.out.println("Выберите игрового персонажа");
        Player person = PlayerService.thePersonagesOfTheGame.get("Человек");
        Player elf = PlayerService.thePersonagesOfTheGame.get("Эльф");
        System.out.println("Введите \"1\" для выбора персонажа " + playerService.toStringPersonage(person));
        System.out.println("Введите \"2\" для выбора персонажа " + playerService.toStringPersonage(elf));

        String choosePersonage = scanner.next();
        while (!choosePersonage.matches("[1-2]")) {
            log.debug("The wrong number has been introduced - {}", choosePersonage);
            System.out.println("Вы ввели неверное значение. Попробуйте еще раз...");
            choosePersonage = scanner.next();
        }
        int personage = Integer.parseInt(choosePersonage);
        Player player;
        if (personage == 1) {
            player = playerService.createNewPlayer(person, playerName);
            log.info("Player {} chose personage \"{}\", player was created", playerName, person.getName());
        } else {
            player = playerService.createNewPlayer(elf, playerName);
            log.info("Player {} chose personage \"{}\", player was created", playerName, elf.getName());
        }
        log.info("Player {} joined the game", player.getName());

        System.out.println("Введите количество монстров от 1 до 5");

        String countOfMonsters = scanner.next();
        while (!countOfMonsters.matches("[1-5]")) {
            log.debug("The wrong number of monsters have been introduced - {}", countOfMonsters);
            System.out.println("Вы ввели неверное значение. Попробуйте еще раз...");
            countOfMonsters = scanner.next();
        }
        List<Monster> monsters = monsterService.createMonsters(Integer.parseInt(countOfMonsters));
        log.info("Player {} play against {} monster(s)", player.getName(), countOfMonsters);

        System.out.println("Для начала игры введите команду - \"старт\"");
        System.out.println("Для совершения хода введите - \"б\"");
        System.out.println("Для восстановления здоровья на 30%, но помните это можно сделать только 4 раза, введите - \"з\"");
        System.out.println("Для выхода из игры введите-\"выход\"");

        System.out.println("Ваше решение? (старт/выход)...");

        String initCommand = scanner.next();
        while (!(initCommand.equalsIgnoreCase("старт") || initCommand.equalsIgnoreCase("выход"))) {
            log.debug("The wrong command has been introduced - {}", initCommand);
            System.out.println("Вы ввели неверное значение. Попробуйте еще раз...");
            initCommand = scanner.next();
        }

        if (initCommand.equalsIgnoreCase("старт")) {
            log.info("Player {} started to play", player.getName());
            playController.play(monsters, player, diceService.getDice());
        }
        log.info("Player {} finished to play", player.getName());
        System.out.println("До новых встреч!");
        scanner.close();
    }
}
