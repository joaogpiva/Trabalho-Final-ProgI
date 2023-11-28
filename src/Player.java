import java.util.ArrayList;
import java.util.Stack;

public class Player implements Comparable<Player>{
    public ArrayList<Card> hand = new ArrayList<Card>();
    public ArrayList<Card> collectedCards = new ArrayList<Card>();
    public String name;
    public int pointTotal = 0;

    public void drawCards(Stack<Card> deck, int amount){
        for (int i = 0; i < amount; i++) {
            Card card = deck.pop();
            card.owner = this;
            this.hand.add(card);
        }
    }

    public String printHand(){
        String res = "";
        for (Card card : hand) {
            res += card.value + " ";
        }
        return res;
    }

    public String printCollectedCards(){
        String res = "";
        for (Card card : collectedCards) {
            res += card.value + " ";
        }
        return res;
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(this.pointTotal, other.pointTotal);
    }
}
