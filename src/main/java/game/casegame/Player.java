package game.casegame;

import java.util.Random;

public class Player {
    private String name;
    private int hp;

    public Player(String name) {
        this.name = name;
        this.hp = 100;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void reduceHp(int amount) {
        this.hp -= amount;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void performAction(String action, Player opponent) {
        Random random = new Random();
        int change;
        switch (action) {
            case "box":
                change = random.nextInt(10) + 5;
                opponent.reduceHp(change);
                System.out.println(name + " attacks with box causing " + change + " damage.");
                break;
            case "kick":
                change = random.nextInt(15) + 10;
                opponent.reduceHp(change);
                System.out.println(name + " attacks with kick causing " + change + " damage.");
                break;
            case "shoot":
                change = random.nextInt(20) + 15;
                opponent.reduceHp(change);
                System.out.println(name + " attacks with shoot causing " + change + " damage.");
                break;
            case "bend":
                change = random.nextInt(5) + 3;
                this.setHp(this.getHp() + change);
                System.out.println(name + " defends with bend recovering " + change + " HP.");
                break;
            case "jump":
                change = random.nextInt(10) + 5;
                this.setHp(this.getHp() + change);
                System.out.println(name + " defends with jump recovering " + change + " HP.");
                break;
            case "run":
                change = random.nextInt(15) + 10;
                this.setHp(this.getHp() + change);
                System.out.println(name + " defends with run recovering " + change + " HP.");
                break;
        }
    }
}
