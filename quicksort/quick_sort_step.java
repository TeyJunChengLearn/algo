import java.io.*;
import java.util.*;

public class quick_sort_step {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java QuickSortLogger <csvFile> <startLine> <endLine>");
            return;
        }

        String inputCsv = args[0];
        int fromLine = Integer.parseInt(args[1]);
        int toLine = Integer.parseInt(args[2]);
        String outputLog = "quick_sort_step_" + fromLine + "_" + toLine + ".txt";

        try {
            // Grab only the selected lines from the CSV
            List<String[]> segment = loadPartialCsv(inputCsv, fromLine, toLine);

            // Prepare the log writer
            BufferedWriter logger = new BufferedWriter(new FileWriter(outputLog));

            // Log the initial unsorted state
            recordState(segment, -1, logger);

            // Start sorting and logging each pivot step
            quickSort(segment, 0, segment.size() - 1, logger);

            logger.close();
            System.out.println("Quick sort steps written to: " + outputLog);

        } catch (IOException e) {
            System.err.println("Error during file operation: " + e.getMessage());
        }
    }

    /**
     * Reads only a specified range of lines from a CSV file.
     */
    public static List<String[]> loadPartialCsv(String filePath, int start, int end) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int lineNum = 0;

        while ((line = reader.readLine()) != null) {
            if (lineNum >= start && lineNum <= end) {
                String[] parts = line.split(",");
                if (parts.length == 2)
                    data.add(parts);
            }
            if (lineNum > end) break;
            lineNum++;
        }

        reader.close();
        return data;
    }

    /**
     * Recursively applies Quick Sort and logs the array after each partitioning.
     */
    public static void quickSort(List<String[]> items, int left, int right, BufferedWriter log) throws IOException {
        if (left < right) {
            int pivotPosition = partition(items, left, right);
            recordState(items, pivotPosition, log);
            quickSort(items, left, pivotPosition - 1, log);
            quickSort(items, pivotPosition + 1, right, log);
        }
    }

    /**
     * Partitions the list based on the last element and returns the pivot index.
     */
    public static int partition(List<String[]> entries, int start, int end) {
        int pivot = Integer.parseInt(entries.get(end)[0]);
        int i = start - 1;

        for (int j = start; j < end; j++) {
            int current = Integer.parseInt(entries.get(j)[0]);
            if (current <= pivot) {
                i++;
                Collections.swap(entries, i, j);
            }
        }

        Collections.swap(entries, i + 1, end);
        return i + 1;
    }

    /**
     * Logs the current state of the list to the file.
     * Adds "pi=" prefix only if it's a pivot step (not the initial list).
     */
    public static void recordState(List<String[]> listState, int pivotIdx, BufferedWriter logWriter) throws IOException {
        StringBuilder logEntry = new StringBuilder();

        if (pivotIdx >= 0) {
            logEntry.append("pi=").append(pivotIdx).append(" ");
        }

        logEntry.append("[");
        for (int i = 0; i < listState.size(); i++) {
            String[] entry = listState.get(i);
            logEntry.append(entry[0]).append("/").append(entry[1]);
            if (i < listState.size() - 1) {
                logEntry.append(", ");
            }
        }
        logEntry.append("]");

        logWriter.write(logEntry.toString());
        logWriter.newLine();
    }
}
