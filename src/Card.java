public class Card implements Comparable<Card>{
    public int value;
    public boolean lastDigitIsFive;
    public boolean isMultipleOfTen;
    public boolean hasRepeatedDigits;
    public int pointValue;
    public Player owner;

    public Card(){
        this.value = 0;
        this.pointValue = 0;
    }

    public Card(int value){
        this.value = value;
        this.owner = new Player();
        this.lastDigitIsFive = value % 10 == 5;
        this.isMultipleOfTen = value % 10 == 0;
        this.hasRepeatedDigits = value % 11 == 0;
        this.pointValue = 1;

        if(this.lastDigitIsFive){
            this.pointValue += 1;
        }

        if(this.isMultipleOfTen){
            this.pointValue += 2;
        }

        if(this.hasRepeatedDigits){
            this.pointValue += 4;
        }
    }

    public String printCard(){
        return this.value + " - " + this.pointValue;
    }

    @Override
    public int compareTo(Card c) {
        if (this.value < c.value) {
            return -1;
        }else{
            return 1;
        }
    }
}