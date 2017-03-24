package yahtzee;

import java.util.ArrayList;

/**
 * Created by Me on 3/23/2017.
 */
public class YahtzeeScores {
    private int aces, twos, threes, fours, fives, sixes, threeofakind, fourofakind, fullhouse, smallstraight, largestraight, chance, yahtzees;

    public int rolls, turnsLeft, lastAdd;

    public YahtzeeScores() {
        this.aces = -1;
        this.twos = -1;
        this.threes = -1;
        this.fours = -1;
        this.fives = -1;
        this.sixes = -1;
        this.threeofakind = -1;
        this.fourofakind = -1;
        this.fullhouse = -1;
        this.smallstraight = -1;
        this.largestraight = -1;
        this.chance = -1;
        this.yahtzees = -1;

        this.rolls = 3;
        this.turnsLeft = 13;
        this.lastAdd = -1;
    }

    public boolean requestPlay(String play, int[] dice, int type) {
        if (play.equals("aces")) {
            if (aces != -1) {
                return false;
            }

            lastAdd = requestAces(dice);
            turnsLeft--;
        } else if (play.equals("twos")) {
            if (twos != -1) {
                return false;
            }

            lastAdd = requestTwos(dice);
            turnsLeft--;
        } else if (play.equals("threes")) {
            if (threes != -1) {
                return false;
            }

            lastAdd = requestThrees(dice);
            turnsLeft--;
        } else if (play.equals("fours")) {
            if (fours != -1) {
                return false;
            }

            lastAdd = requestFours(dice);
            turnsLeft--;
        } else if (play.equals("fives")) {
            if (fives != -1) {
                return false;
            }

            lastAdd = requestFives(dice);
            turnsLeft--;
        } else if (play.equals("sixes")) {
            if (sixes != -1) {
                return false;
            }

            lastAdd = requestSixes(dice);
            turnsLeft--;
        } else if (play.equals("three of a kind")) {
            if (threeofakind != -1) {
                return false;
            }

            lastAdd = requestThreeOfAKind(dice, type);
            turnsLeft--;
        } else if (play.equals("four of a kind")) {
            if (fourofakind != -1) {
                return false;
            }

            lastAdd = requestFourOfAKind(dice, type);
            turnsLeft--;
        } else if (play.equals("full house")) {
            if (fullhouse != -1) {
                return false;
            }

            lastAdd = requestFullHouse(dice);
            turnsLeft--;
        } else if (play.equals("small straight")) {
            if (smallstraight != -1) {
                return false;
            }

            lastAdd = requestSmallStraight(dice);
            turnsLeft--;
        } else if (play.equals("large straight")) {
            if (largestraight != -1) {
                return false;
            }

            lastAdd = requestLargeStraight(dice);
            turnsLeft--;
        } else if (play.equals("chance")) {
            if (chance != -1) {
                return false;
            }

            lastAdd = requestChance(dice);
            turnsLeft--;
        } else if (play.equals("yahtzee")) {
            if (hasYahtzee(dice)) {
                addYahtzee();
            } else {
                if (yahtzees >= 50) {
                    lastAdd = 0;
                } else {
                    lastAdd = yahtzees = 0;
                }
            }
        }

        return true;
    }

    public int requestAces(int[] dice) {
        int score = 0;

        for (int die : dice) {
            if (die == 1) {
                score+=1;
            }
        }

        return aces = score;
    }

    public int requestTwos(int[] dice) {
        int score = 0;

        for (int die : dice) {
            if (die == 2) {
                score+=2;
            }
        }

        return twos = score;
    }

    public int requestThrees(int[] dice) {
        int score = 0;

        for (int die : dice) {
            if (die == 3) {
                score+=3;
            }
        }

        return threes = score;
    }

    public int requestFours(int[] dice) {
        int score = 0;

        for (int die : dice) {
            if (die == 4) {
                score+=4;
            }
        }

        return fours = score;
    }

    public int requestFives(int[] dice) {
        int score = 0;

        for (int die : dice) {
            if (die == 5) {
                score+=5;
            }
        }

        return fives = score;
    }

    public int requestSixes(int[] dice) {
        int score = 0;

        for (int die : dice) {
            if (die == 6) {
                score+=6;
            }
        }

        return sixes = score;
    }

    public int requestThreeOfAKind(int[] dice, int type) {
        int tally = 0;

        int total = 0;

        for (int die : dice) {
            total+=die;

            if (die == type) {
                tally++;
            }
        }

        if (tally >= 3) {
            return threeofakind = total;
        }

        return threeofakind = 0;
    }

    public int requestFourOfAKind(int[] dice, int type) {
        int tally = 0;

        int total = 0;

        for (int die : dice) {
            total+=die;

            if (die == type) {
                tally++;
            }
        }

        if (tally >= 4) {
            return fourofakind = total;
        }

        return fourofakind = 0;
    }

    public int requestFullHouse(int[] dice) {
        ArrayList<Integer> types = new ArrayList<>();
        int amount = 0;

        for (int die : dice) {
            if (!types.contains(die)) {
                types.add(die);
                amount++;
            }
        }

        if (amount != 2) {
            return fullhouse = 0;
        }

        boolean two = false, three = false;

        for (int type : types) {
            int tally = 0;

            for (int die : dice) {
                if (die == type) {
                    tally++;
                }
            }

            if (tally == 2 && !two) {
                two = true;
            } else if (tally == 3 && !three) {
                three = true;
            } else {
                return fullhouse = 0;
            }
        }

        return fullhouse = 25;
    }

    public int requestSmallStraight(int[] dice) {
        ArrayList<Integer> types = new ArrayList<>();

        for (int die : dice) {
            if (!types.contains(die)) {
                types.add(die);
            }
        }

        if (types.contains(1) && types.contains(2) && types.contains(3) && types.contains(4)) {
            return smallstraight = 30;
        }

        if (types.contains(2) && types.contains(3) && types.contains(4) && types.contains(5)) {
            return smallstraight = 30;
        }

        if (types.contains(3) && types.contains(4) && types.contains(5) && types.contains(6)) {
            return smallstraight = 30;
        }

        return smallstraight = 0;
    }

    public int requestLargeStraight(int[] dice) {
        ArrayList<Integer> types = new ArrayList<>();

        for (int die : dice) {
            if (!types.contains(die)) {
                types.add(die);
            }
        }

        if (types.contains(1) && types.contains(2) && types.contains(3) && types.contains(4) && types.contains(5)) {
            return largestraight = 40;
        }

        if (types.contains(2) && types.contains(3) && types.contains(4) && types.contains(5) && types.contains(6)) {
            return largestraight = 40;
        }

        return largestraight = 0;
    }

    public boolean hasYahtzee(int[] dice) {
        ArrayList<Integer> types = new ArrayList<>();
        int amount = 0;

        for (int die : dice) {
            if (!types.contains(die)) {
                types.add(die);
                amount++;
            }
        }

        return amount == 1;
    }

    public int addYahtzee() {
        if (yahtzees >= 50) {
            yahtzees+=100;
            return 100;
        } else {
            turnsLeft--;
            return yahtzees = 50;
        }
    }

    public int requestChance(int[] dice) {
        int total = 0;

        for (int die : dice) {
            total+=die;
        }

        return chance = total;
    }

    public int tallyUpperSection() {
        return aces + twos + threes + fours + fives + sixes;
    }

    public int tallyLowerSection() {
        return threeofakind + fourofakind + fullhouse + smallstraight + largestraight + yahtzees + chance;
    }

    public int getBonus() {
        if (tallyUpperSection() >= 63) {
            return 35;
        }

        return 0;
    }
}
