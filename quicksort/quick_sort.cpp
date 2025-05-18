#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <chrono>

using namespace std;
using namespace chrono;

struct RowData {
    int number;
    string label;
};

// First pass: count number of rows
int countRows(const string& filename) {
    ifstream file(filename);
    if (!file.is_open()) return 0;

    string line;
    int count = 0;
    while (getline(file, line)) {
        if (!line.empty()) ++count;
    }
    file.close();
    return count;
}

// Second pass: load CSV into dynamic array
void readCSV(const string& filename, RowData* data, int& count) {
    ifstream file(filename);
    string line;
    int i = 0;

    while (getline(file, line)) {
        stringstream ss(line);
        string numStr, word;
        getline(ss, numStr, ',');
        getline(ss, word);
        if (!numStr.empty() && !word.empty()) {
            data[i++] = { stoi(numStr), word };
        }
    }
    count = i;
    file.close();
}

// Writes array to CSV file
void writeCSV(const string& filename, RowData* data, int size) {
    ofstream out(filename);
    for (int i = 0; i < size; ++i) {
        out << data[i].number << "," << data[i].label << "\n";
    }
    out.close();
}

// Lomuto partition
int partition(RowData* arr, int low, int high) {
    int pivot = arr[high].number;
    int i = low - 1;

    for (int j = low; j < high; ++j) {
        if (arr[j].number <= pivot) {
            ++i;
            swap(arr[i], arr[j]);
        }
    }
    swap(arr[i + 1], arr[high]);
    return i + 1;
}

// Recursive Quick Sort
void quickSort(RowData* arr, int low, int high) {
    if (low < high) {
        int pivotIndex = partition(arr, low, high);
        quickSort(arr, low, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, high);
    }
}

int main(int argc, char* argv[]) {
    if (argc != 2) {
        cout << "Usage: quick_sort <inputCSV>" << endl;
        return 1;
    }

    string inputFile = argv[1];
    int totalRows = countRows(inputFile);

    if (totalRows == 0) {
        cerr << "No rows found or file could not be opened: " << inputFile << endl;
        return 1;
    }

    RowData* rows = new RowData[totalRows];
    int actualRows = 0;
    readCSV(inputFile, rows, actualRows);

    if (actualRows == 0) {
        cerr << "No valid data to sort in file: " << inputFile << endl;
        delete[] rows;
        return 1;
    }

    cout << "Loaded " << actualRows << " rows from " << inputFile << endl;

    auto start = high_resolution_clock::now();
    quickSort(rows, 0, actualRows - 1);
    auto end = high_resolution_clock::now();

    auto duration = duration_cast<milliseconds>(end - start).count();
    cout << "Quick Sort execution time: " << duration << " ms" << endl;

    string outputFile = "quick_sort_" + to_string(actualRows) + ".csv";
    writeCSV(outputFile, rows, actualRows);
    cout << "Sorted output saved to: " << outputFile << endl;

    delete[] rows;
    return 0;
}
