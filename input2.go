module m2

func printBudgetReport() {
    mealBudget := 80
    transportBudget := 35
    museumBudget := 60
    emergencyReserve := 25

    plannedExpenses := mealBudget + transportBudget + museumBudget
    requiredBudget := plannedExpenses + emergencyReserve

    println(plannedExpenses)
    println(requiredBudget)

    if requiredBudget > 150 {
        extraBuffer := requiredBudget - 150
        println(extraBuffer)
    }
}

func printSafetyReport() {
    expectedVisitors := 6
    staffMembers := 2
    visitorsPerStaff := expectedVisitors / staffMembers

    println(visitorsPerStaff)

    if visitorsPerStaff <= 3 {
        println(1)
    }
}
