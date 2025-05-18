#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

using namespace std;

struct RowData {
    int value;
    string label;
};

// Reads specific rows from CSV file into a dynamically allocated array
int loadCSVSection(const string& filename, RowData*& data, int startLine, int endLine) {
    ifstream file(filename);
    if (!file.is_open()) {
        cerr << "Cannot open input file: " << filename << endl;
        return 0;
    }

    string line;
    int currentLine = 0, index = 0;
    int maxLines = endLine - startLine + 1;
    data = new RowData[maxLines]; // Allocate only needed size

    while (getline(file, line)) {
        if (currentLine >= startLine && currentLine <= endLine) {
            stringstream ss(line);
            string numStr, word;
            getline(ss, numStr, ',');
            getline(ss, word);

            if (!numStr.empty() && !word.empty()) {
                data[index++] = { stoi(numStr), word };
            }
        }
        if (currentLine > endLine) break;
        currentLine++;
    }

    file.close();
    return index; // actual number of valid rows
}

// Logs current sorting state to file with optional pivot index
void logCurrentStep(ofstream& out, RowData data[], int size, int pivot = -1) {
    if (pivot >= 0) {
        out << "pi=" << pivot << " ";
    }
    out << "[";
    for (int i = 0; i < size; ++i) {
        out << data[i].value << "/" << data[i].label;
        if (i != size - 1) out << ", ";
    }
    out << "]\n";
}

// Lomuto partitioning
int partition(RowData arr[], int low, int high) {
    int pivot = arr[high].value;
    int marker = low - 1;

    for (int j = low; j < high; ++j) {
        if (arr[j].value <= pivot) {
            ++marker;
            swap(arr[marker], arr[j]);
        }
    }

    swap(arr[marker + 1], arr[high]);
    return marker + 1;
}

// Recursive Quick Sort with logging after each partition
void quickSortWithLog(RowData arr[], int left, int right, int fullSize, ofstream& out) {
    if (left < right) {
        int pivotIndex = partition(arr, left, right);
        logCurrentStep(out, arr, fullSize, pivotIndex);
        quickSortWithLog(arr, left, pivotIndex - 1, fullSize, out);
        quickSortWithLog(arr, pivotIndex + 1, right, fullSize, out);
    }
}

int main(int argc, char* argv[]) {
    if (argc != 4) {
        cout << "Usage: quick_sort_step_array <inputCSV> <startRow> <endRow>\n";
        return 1;
    }

    string csvFile = argv[1];
    int start = stoi(argv[2]);
    int end = stoi(argv[3]);

    RowData* data = nullptr;
    int size = loadCSVSection(csvFile, data, start, end);

    if (size == 0) {
        cerr << "No data found in the specified range.\n";
        delete[] data;
        return 1;
    }

    // Prepare output file
    string logFile = "quick_sort_step_" + to_string(start) + "_" + to_string(end) + ".txt";
    ofstream out(logFile);
    if (!out.is_open()) {
        cerr << "Cannot create output file: " << logFile << endl;
        delete[] data;
        return 1;
    }

    // Log the initial unsorted list
    logCurrentStep(out, data, size);

    // Begin recursive sort with step logging
    quickSortWithLog(data, 0, size - 1, size, out);

    out.close();
    delete[] data;

    cout << "Quick sort steps saved to: " << logFile << endl;
    return 0;
}
