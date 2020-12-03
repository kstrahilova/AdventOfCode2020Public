#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <limits.h>
#include <stdbool.h>
#include <stdlib.h>

int main() {
    FILE *input;
    //arbitrarily set
    int max = 100000;
    char row[max];
    int length_row;
    int slopes[5][2] = {{1 ,1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}};
    long long int result = 1;

    for (int i = 0; i < 5; i++) {
        input = fopen("inputChallenge3.txt", "r");
        int n_trees = 0;
        int position = -slopes[i][0];
        int counter = 0;
        counter = 0;
        int row_n = -1;

        while (fgets(row, max, input) != NULL) {
            row_n = row_n + 1;
            if (counter == 0) {
                length_row = strlen(row);
                //we have 31 numbers, so indices go from 0 to 30, but length is 32 because of the '\0' at the end of the row, so we need mod length - 1
                position = (position + slopes[i][0]) % (length_row - 1);
                if (row[position] == '#') {
                    n_trees = n_trees + 1;
                }
                counter = slopes[i][1] - 1;
            } else {
                counter = counter - 1;
            }
        }

        result = result * n_trees;
    }

    printf("result is %lld\n", result);
}
