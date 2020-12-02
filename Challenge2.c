#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <limits.h>
#include <stdbool.h>
#include <stdlib.h>

static void process_entry_policy_1(char * entry, int * n) {
    //printf("entry is %s\n", entry);

    char condition;
    int total = 0;
    char * rest;
    int at_least = strtol(entry, &rest, 10);
    //printf("at_least is %d\n", at_least);
    //printf("rest is %s\n", rest);
    int at_most = -1 * strtol(rest, &rest, 10);
    //printf("at_most is %d\n", at_most);
    //printf("rest is %s\n", rest);

    for (int i = 0; i < strlen(rest); i++) {

        if (rest[i] == ':') {
            condition = rest[i - 1];
            //printf("condition is %c\n", condition);
        } else if (rest[i] == condition) {
            total = total + 1;
        }
        //printf("rest[%d] is %c\n", i, rest[i]);
    }

    //printf("total = %d\n", total);
    if (total >= at_least && total <= at_most) {
        *n = *n + 1;
    }
    //printf("n is equal to %d\n", *n);
}

static void process_entry_policy_2(char * entry, int * n) {
    //printf("entry is %s\n", entry);

    char condition;
    int total = 0;
    char * rest;
    //add -1 at end because they start counting at 1
    int index1 = strtol(entry, &rest, 10) - 1;
    int index2 = -1 * strtol(rest, &rest, 10) - 1;
    int start;

    for (int i = 0; i < strlen(rest); i++) {

        if (rest[i] == ':') {
            condition = rest[i - 1];
            //printf("condition is %c\n", condition);
            start = i + 2;
            break;
        }
    }

    //printf("total = %d\n", total);

    //printf("Letter at the first index is %c and on the second %c\n", rest[start + index1], rest[start + index2]);
    if ((rest[start + index1] == condition && rest[start + index2] != condition) || (rest[start + index1] != condition && rest[start + index2] == condition)) {
        *n = *n + 1;
    }
    //printf("n is equal to %d\n", *n);
}

int main() {
    FILE *input;
    input = fopen("inputChallenge2.txt", "r");

    //arbitrarily set
    int max = 1001;
    int n_correct = 0;
    char entry[max];


    //while the strings are the same, and we have successfully read an entry
    //while (strcmp(fgets(entry, max, input), entry) == 0) {
    while (fgets(entry, max, input) != NULL) {
        process_entry_policy_2(entry, &n_correct);
    }

    printf("Number of correct passwords is %d\n", n_correct);

    /*int close_status = fclose(input);
    if (close_status == -1) {
        perror("Could not close the file");
    }*/
}
