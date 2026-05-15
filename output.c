#include <stdio.h>

void m2_printBudgetReport();
void m2_printSafetyReport();

int main() {
    int m1_ticketPrice = 45;
    int m1_visitorCount = 6;
    int m1_serviceFee = 12;
    int m1_discountLimit = 250;
    int m1_ticketSubtotal = (m1_ticketPrice * m1_visitorCount);
    int m1_estimatedTotal = (m1_ticketSubtotal + m1_serviceFee);
    int m1_averagePerVisitor = (m1_estimatedTotal / m1_visitorCount);
    printf("%d\n", m1_ticketSubtotal);
    printf("%d\n", m1_estimatedTotal);
    printf("%d\n", m1_averagePerVisitor);
    if (m1_estimatedTotal >= m1_discountLimit) {
        int m1_discountAmount = (m1_estimatedTotal / 10);
        int m1_finalTotal = (m1_estimatedTotal - m1_discountAmount);
        printf("%d\n", m1_discountAmount);
        printf("%d\n", m1_finalTotal);
    }
    if (m1_averagePerVisitor < m1_ticketPrice) {
        printf("%d\n", m1_averagePerVisitor);
    }
    m2_printBudgetReport();
    m2_printSafetyReport();
    while (m1_estimatedTotal < 0) {
        printf("%d\n", m1_estimatedTotal);
    }

    return 0;
}

void m2_printBudgetReport() {
    int m2_mealBudget = 80;
    int m2_transportBudget = 35;
    int m2_museumBudget = 60;
    int m2_emergencyReserve = 25;
    int m2_plannedExpenses = ((m2_mealBudget + m2_transportBudget) + m2_museumBudget);
    int m2_requiredBudget = (m2_plannedExpenses + m2_emergencyReserve);
    printf("%d\n", m2_plannedExpenses);
    printf("%d\n", m2_requiredBudget);
    if (m2_requiredBudget > 150) {
        int m2_extraBuffer = (m2_requiredBudget - 150);
        printf("%d\n", m2_extraBuffer);
    }
}

void m2_printSafetyReport() {
    int m2_expectedVisitors = 6;
    int m2_staffMembers = 2;
    int m2_visitorsPerStaff = (m2_expectedVisitors / m2_staffMembers);
    printf("%d\n", m2_visitorsPerStaff);
    if (m2_visitorsPerStaff <= 3) {
        printf("%d\n", 1);
    }
}

