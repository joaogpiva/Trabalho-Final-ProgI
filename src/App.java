import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        Stack<Card> deck = new Stack<Card>();

        for (int i = 1; i <= 109; i++) {
            deck.add(new Card(i));
        }

        Collections.shuffle(deck);

        int playerAmount = getPlayerAmount(scanner);
        
        ArrayList<Player> players = new ArrayList<Player>();

        for (int i = 0; i < playerAmount; i++) {
            players.add(new Player());
            players.get(i).drawCards(deck, 12);
            System.out.println("Digite o nome do jogador " + (i+1) + ": ");
            players.get(i).name = scanner.nextLine();
            System.out.println("=============");
        }

        ArrayList<ArrayList<Card>> board = new ArrayList<ArrayList<Card>>();

        for (int i = 0; i < 5; i++) {
            board.add(new ArrayList<Card>());
            for (int j = 0; j < 5; j++) {
                if (j == 0) {
                    board.get(i).add(deck.pop());
                }else{
                    board.get(i).add(new Card());
                }
            }
        }

        while (!players.get(0).hand.isEmpty()) {
            clearScreen();

            ArrayList<Card> cardsPlayedInRound = new ArrayList<Card>();

            printBoard(board);
            System.out.println();
            printPointAmount(players);

            for (Player player : players) {
                System.out.println("Mão do jogador " + player.name + ": ");
                System.out.println(player.printHand());
                int indexOfCard = getCardToPlay(player.hand, scanner);
                cardsPlayedInRound.add(player.hand.remove(indexOfCard));
            }

            Collections.sort(cardsPlayedInRound);

            System.out.println("Cartas jogadas essa rodada: ");
            for (Card card : cardsPlayedInRound) {
                System.out.print(card.value + " ");
            }
            System.out.println();

            insertCards(board, cardsPlayedInRound);

            System.out.println("Pressione enter para continuar");

            scanner.nextLine();
        }

        clearScreen();

        Collections.sort(players);

        ArrayList<Player> winners = new ArrayList<Player>();
        int winnerPoints = Integer.MAX_VALUE;

        System.out.println("========== RANKING ==========");

        for (Player player : players) {
            if(player.pointTotal <= winnerPoints){
                winnerPoints = player.pointTotal;
                winners.add(player);
            }
            System.out.println(player.name);
            System.out.println("Cartas coletadas: " + player.printCollectedCards());
            System.out.println("Pontuação: " + player.pointTotal);
            System.out.println();
        }

        System.out.println("**** VENCEDOR(ES) ****");

        for (Player winner : winners) {
            System.out.println(winner.name);
        }

        scanner.close();
    }

    private static void printPointAmount(ArrayList<Player> players) {
        for (Player player : players) {
            System.out.println(player.name + " - " + player.pointTotal + " pontos");
            System.out.println();
        }
    }

    private static void insertCards(ArrayList<ArrayList<Card>> board, ArrayList<Card> cardsPlayedInRound) {
        for (Card cardPlayed : cardsPlayedInRound) {
            int indexOfRow = getIndexOfRowWithMinimumDifference(board, cardPlayed);

            if (indexOfRow == -1) {
                indexOfRow = getIndexOfRowWithLargestNumber(board);
            }

            ArrayList<Card> cardsRemovedFromRow = new ArrayList<Card>();
            int pointTotal = 0;

            var row = board.get(indexOfRow);

            for (int i = 0; i < row.size(); i++) {

                if (row.get(i).value != 0){
                    cardsRemovedFromRow.add(row.get(i));
                    pointTotal += row.get(i).pointValue;
                }

                if (row.get(i).value == 0) {
                    if(row.get(i-1).value > cardPlayed.value){
                        cardPlayed.owner.collectedCards.addAll(cardsRemovedFromRow);
                        cardPlayed.owner.pointTotal += pointTotal;
                        row.clear();
                        row.add(cardPlayed);
                        for (int j = 1; j < 5; j++) {
                            row.add(new Card());
                        }
                    }else{
                        row.remove(i);
                        row.add(i, cardPlayed);
                    }
                    break;
                }else if(i == 4){
                    cardPlayed.owner.collectedCards.addAll(cardsRemovedFromRow);
                    cardPlayed.owner.pointTotal += pointTotal;
                    row.clear();
                    row.add(cardPlayed);
                    for (int j = 1; j < 5; j++) {
                        row.add(new Card());
                    }
                    break;
                }
            }

            System.out.println("Carta " + cardPlayed.value + " inserida na linha " + (indexOfRow + 1));
        }
    }

    private static int getIndexOfRowWithLargestNumber(ArrayList<ArrayList<Card>> board){
        int largestNumber = Integer.MIN_VALUE, indexOfRow = -1;

        for (int i = 0; i < board.size(); i++) {
            var row = board.get(i);
            for (int j = row.size() - 1; j >= 0; j--) {
                if(row.get(j).value != 0){
                    if (row.get(j).value > largestNumber) {
                        largestNumber = row.get(j).value;
                        indexOfRow = i;
                    }
                    break;
                }
            }
        }

        return indexOfRow;
    }

    private static int getIndexOfRowWithMinimumDifference(ArrayList<ArrayList<Card>> board, Card cardPlayed){
        int finalDifference = Integer.MAX_VALUE, indexOfRow = -1;

        for (int i = 0; i < board.size(); i++) {
            var row = board.get(i);
            for (int j = row.size() - 1; j >= 0; j--) {
                if(row.get(j).value != 0){
                    if (row.get(j).value < cardPlayed.value) {
                        if (cardPlayed.value - row.get(j).value < finalDifference) {
                            finalDifference = cardPlayed.value - row.get(j).value;
                            indexOfRow = i;
                        }
                    }
                    break;
                }
            }
        }

        return indexOfRow;
    }

    private static int getCardToPlay(ArrayList<Card> hand, Scanner scanner){
        boolean isValidCard = false;
        int value = 0, index = -1;
        System.out.println("Digite uma carta para jogar");

        while (!isValidCard) {
            String input = scanner.nextLine();

            try{
                value = Integer.parseInt(input);
                for (int i = 0; i < hand.size(); i++) {
                    if (hand.get(i).value == value) {
                        isValidCard = true;
                        index = i;
                        break;
                    }
                }

                if (!isValidCard) {
                    System.out.println("Carta não está na mão");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida");
            }
            
        }

        return index;
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
    }

    private static void printBoard(ArrayList<ArrayList<Card>> board) {
        for (ArrayList<Card> row : board) {
            for (Card card : row) {
                System.out.print("[ ");
                if (card.value != 0) {
                    System.out.print(card.value);
                }
                System.out.print(" ]");
            }
            System.out.println();
        }
    }

    private static int getPlayerAmount(Scanner scanner) {
        int playerAmount = 0;
        boolean validPlayerAmount = false;

        while(!validPlayerAmount){
            System.out.println("Digite o número de jogadores (entre 3 e 6): ");
            String input = scanner.nextLine();

            try{
                playerAmount = Integer.parseInt(input);

                if (playerAmount >= 3 && playerAmount <= 6) {
                    validPlayerAmount = true;
                } else {
                    System.out.println("Número fora do limite.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida");
            }
            
        }

        return playerAmount;
    }
}
