import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static final String LETTERS = "RLRFR";
    public static final int ROUTE_LENGTH = 100;
    public static final int AMOUNT_OF_THREADS = 100;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        final String LETTERS = "RLRFR";
        final int ROUTE_LENGTH = 100;
        final int AMOUNT_OF_THREADS = 100;
        final Map<Integer, Integer> sizeToFreq = new HashMap<>();

        for (int i = 0; i < AMOUNT_OF_THREADS; i++) {
            new Thread(() -> {
                String route = generateRoute(LETTERS, ROUTE_LENGTH);
                int frequency = (int) route.chars().filter(ch -> ch == 'R').count();

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(frequency)) {
                        sizeToFreq.put(frequency, sizeToFreq.get(frequency) + 1);
                    } else {
                        sizeToFreq.put(frequency, 1);
                    }
                    sizeToFreq.put(frequency, sizeToFreq.getOrDefault(frequency, 0) + 1);
                }
            }).start();
        }

        Map.Entry<Integer, Integer> max = sizeToFreq
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();

        System.out.printf("Самое частое кол-во повторений %d (встретилось %d раз)%n", max.getKey(), max.getValue());

        System.out.println("Другие размеры: ");
        sizeToFreq
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> System.out.println(" - " + e.getKey() + " (" + e.getValue() + " раз)"));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
