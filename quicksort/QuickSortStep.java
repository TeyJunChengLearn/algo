import java.io.*;
import java.util.*;

public class QuickSortStep {
    static List<String> steps = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java QuickSortStep <filename> <startRow> <endRow>");
            return;
        }

        String fileName = args[0];
        int startRow = Integer.parseInt(args[1]);
        int endRow = Integer.parseInt(args[2]);

        try {
            List<String[]> list = readCSVRange(fileName, startRow, endRow);
            quickSort(list, 0, list.size() - 1);
            writeStepsToFile("quick_sort_step_" + startRow + "_" + endRow + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readCSVRange(String fileName, int start, int end) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int row = 0;

        while ((line = reader.readLine()) != null) {
            if (row >= start && row <= end) {
                String[] parts = line.split(",");
                if (parts.length == 2)
                    data.add(parts);
            }
            if (row > end) break;
            row++;
        }

        reader.close();
        return data;
    }

    public static void quickSort(List<String[]> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            logCurrentStep(list, pi);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    public static int partition(List<String[]> list, int low, int high) {
        int pivot = Integer.parseInt(list.get(high)[0]);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            int current = Integer.parseInt(list.get(j)[0]);
            if (current <= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }

        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public static void logCurrentStep(List<String[]> list, int pivotIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append("pi=").append(pivotIndex).append(" [");
        for (String[] entry : list) {
            sb.append(entry[0]).append("/").append(entry[1]).append(", ");
        }
        if (sb.length() > 2) sb.setLength(sb.length() - 2); // remove last ", "
        sb.append("]");
        steps.add(sb.toString());
    }

    public static void writeStepsToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String step : steps) {
            writer.write(step);
            writer.newLine();
        }
        writer.close();
        System.out.println("Steps written to: " + filename);
    }
}
