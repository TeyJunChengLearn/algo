import java.io.*;
import java.util.*;

public class MergeSortStep {
    static List<String> steps = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java MergeSortStep <filename> <startRow> <endRow>");
            return;
        }

        String inputFile = args[0];
        int startRow = Integer.parseInt(args[1]);
        int endRow = Integer.parseInt(args[2]);

        try {
            List<String[]> data = readCSVRange(inputFile, startRow, endRow);
            mergeSort(data, 0, data.size() - 1);
            writeStepsToFile("merge_sort_step_" + startRow + "_" + endRow + ".txt");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static List<String[]> readCSVRange(String filename, int startRow, int endRow) throws IOException {
        List<String[]> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int index = 0;

        while ((line = reader.readLine()) != null) {
            if (index >= startRow && index <= endRow) {
                String[] parts = line.split(",");
                result.add(parts);
            }
            if (index > endRow) break;
            index++;
        }
        reader.close();
        return result;
    }

    public static void mergeSort(List<String[]> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
            logCurrentStep(list);
        }
    }

    public static void merge(List<String[]> list, int left, int mid, int right) {
        List<String[]> leftPart = new ArrayList<>(list.subList(left, mid + 1));
        List<String[]> rightPart = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;

        while (i < leftPart.size() && j < rightPart.size()) {
            int val1 = Integer.parseInt(leftPart.get(i)[0]);
            int val2 = Integer.parseInt(rightPart.get(j)[0]);

            if (val1 <= val2) {
                list.set(k++, leftPart.get(i++));
            } else {
                list.set(k++, rightPart.get(j++));
            }
        }

        while (i < leftPart.size()) list.set(k++, leftPart.get(i++));
        while (j < rightPart.size()) list.set(k++, rightPart.get(j++));
    }

    public static void logCurrentStep(List<String[]> list) {
        StringBuilder sb = new StringBuilder();
        for (String[] entry : list) {
            sb.append(entry[0]).append("/").append(entry[1]).append(", ");
        }
        // Remove last comma and space
        if (sb.length() >= 2) sb.setLength(sb.length() - 2);
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
