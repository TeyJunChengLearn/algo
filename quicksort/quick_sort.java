import java.io.*;
import java.util.*;

public class quick_sort {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java QuickSortStep <filename>");
            return;
        }

        String fileName = args[0];

        try {
            List<String[]> list = readCSVRange(fileName);
            long startTime = System.nanoTime();
            quickSort(list, 0, list.size() - 1);
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;            
            System.out.println("Merge Sort Complete in "+durationMs+" ms");
            writeToFile("quick_sort_" + list.size()+ ".csv",list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readCSVRange(String fileName) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2)
                    data.add(parts);
        }

        reader.close();
        return data;
    }

    public static void quickSort(List<String[]> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
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

    public static void writeToFile(String filename,List<String[]> list) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String[] data : list) {
            writer.write(data[0]+","+data[1]);
            writer.newLine();
        }
        writer.close();
        System.out.println("Steps written to: " + filename);
    }
}
