package lifts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Lift {
    private final int countALLLifts;
    private int maxCountLifts;
    private final Map<Integer, int[]> map = new HashMap<>();
    private List<int[]> list = new ArrayList<>();

    public Lift(String pathFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
            String line = reader.readLine();
            this.countALLLifts = (validation(Integer.parseInt(line))) ? Integer.parseInt(line) : 0;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(" ");
                int[] values = Arrays.stream(arr)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                if (validation(values)) {
                    this.map.put(i++, values);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.maxCountLifts = 0;
    }

    private boolean validation(int value) {
        return value >= 1 && value <= Math.pow(10, 5);
    }

    private boolean validation(int[] arr) {
        if (countALLLifts > 0) {
            return arr[0] >= 0 && arr[1] <= Math.pow(10, 9) && arr[0] < arr[1];
        } else {
            System.out.println("The number of lifts is absent");
            return false;
        }
    }

    private List<int[]> addEl(List<int[]> ls, int[] arr) {
        List<int[]> list1 = new ArrayList<>(ls);
        list1.add(arr);
        return list1;
    }

    public int getMaxCountLifts() {
//        if (maxCountLifts ==0 ) {
            calcMaxCountLifts(1, 0, new ArrayList<>());
//        }
        return maxCountLifts;
    }

    public void printRoute() {
        for (int[] arr : list) {
            for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
                if (Arrays.equals(entry.getValue(), arr))
                    System.out.println("Номер лифта : " + entry.getKey() + "  номера этажей : " + entry.getValue()[0] + " --> " + entry.getValue()[1]);
            }
        }
    }

    private void calcMaxCountLifts(int curFloor, int countOfLifts, List<int[]> ls) {
        if (map.values().stream().noneMatch(v -> v[0] == curFloor)) {
            if (maxCountLifts == 0 || maxCountLifts < countOfLifts) {
                maxCountLifts = countOfLifts;
                this.list = ls;
            }
        } else {
            map.values().stream()
                    .filter(v -> v[0] == curFloor)
                    .forEach(v ->
                            calcMaxCountLifts(v[1], countOfLifts + 1, addEl(ls, v)));
        }
    }

    public static void main(String[] args) throws IOException {
//        creating data file
        Map<Integer, int[]> list = new HashMap<>();
        list.put(1, new int[]{1, 3});
        list.put(2, new int[]{1, 5});
        list.put(3, new int[]{3, 4});
        list.put(4, new int[]{3, 5});
        list.put(5, new int[]{4, 5});
        list.put(6, new int[]{4, 7});
        list.put(7, new int[]{5, 7});
        list.put(8, new int[]{5, 8});
        list.put(9, new int[]{6, 8});
        list.put(10, new int[]{6, 11});
        list.put(11, new int[]{7, 10});
        list.put(12, new int[]{8, 9});
        list.put(13, new int[]{9, 11});
        list.put(14, new int[]{9, 13});
        list.put(15, new int[]{10, 13});
        list.put(16, new int[]{11, 13});
        list.put(17, new int[]{13, 14});
        list.put(18, new int[]{13, 19});
        list.put(19, new int[]{15, 18});
        list.put(20, new int[]{18, 20});
        list.put(21, new int[]{19, 22});
        list.put(22, new int[]{20, 22});
        try (FileWriter fileWriter = new FileWriter("routs_lifts.txt")) {
            fileWriter.write(list.size() + "\n");
            for (Map.Entry<Integer, int[]> entry : list.entrySet()) {
                String s = entry.getValue()[0] + " " + entry.getValue()[1] + "\n";
                fileWriter.write(s);
            }
        }
//        end creating data file
        Lift lift = new Lift("routs_lifts.txt");
        System.out.println("Максимальное число использованных лифтов : " + lift.getMaxCountLifts());
        try (FileWriter writer = new FileWriter("result.txt")){
            writer.write(Integer.toString(lift.getMaxCountLifts()));
        }
        lift.printRoute();
    }
}
