#include <stdio.h>

int main() {
    int a = 20;
    int b = 4;
    int c = 2;
    int d = 3;
    int result = ((((a + b) * c) - (a / b)) + (d * (c + b)));
    int comparison = (result >= 40);
    printf("%d\n", result);
    printf("%d\n", comparison);
    if ((result >= 40)) {
        printf("%d\n", result);
        int inner = (result / 2);
        printf("%d\n", inner);
        if ((inner != 0)) {
            int nested = ((inner + a) * (b + c));
            printf("%d\n", nested);
            if ((nested > result)) {
                printf("%d\n", nested);
            }
        }
    }
    if (((a + b) == 24)) {
        printf("%d\n", (a + b));
    }
    if ((result != 999)) {
        printf("%d\n", 123);
    }
    while ((result < 0)) {
        printf("%d\n", result);
    }

    return 0;
}
