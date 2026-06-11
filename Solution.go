
package main
import "slices"

type QueryPair struct {
    index int
    time  int
}

func countServers(numberOfServers int, logs [][]int, timeRange int, queries []int) []int {
    queryPairs := make([]QueryPair, len(queries))
    for i := range queries {
        queryPairs[i] = QueryPair{i, queries[i]}
    }

    slices.SortFunc(queryPairs, func(first QueryPair, second QueryPair) int { return first.time - second.time })
    slices.SortFunc(logs, func(first []int, second []int) int { return first[1] - second[1] })

    return findNumberOfServersNotReceivingRequestsPerQuery(numberOfServers, logs, timeRange, queryPairs)
}

func findNumberOfServersNotReceivingRequestsPerQuery(numberOfServers int, logs [][]int, timeRange int, queryPairs []QueryPair) []int {
    front := 0
    back := 0
    countServersReceivingRequest := 0
    frequencyPerQueryForServerID := make([]int, numberOfServers + 1)
    numberOfServersNotReceivingRequestsPerQuery := make([]int, len(queryPairs))

    for i := range queryPairs {
        for front < len(logs) && logs[front][1] <= queryPairs[i].time {
            if frequencyPerQueryForServerID[logs[front][0]] == 0 {
                countServersReceivingRequest++
            }
            frequencyPerQueryForServerID[logs[front][0]]++
            front++
        }

        for back < len(logs) && logs[back][1] < queryPairs[i].time-timeRange {
            if frequencyPerQueryForServerID[logs[back][0]] == 1 {
                countServersReceivingRequest--
            }
            frequencyPerQueryForServerID[logs[back][0]]--
            back++
        }
        numberOfServersNotReceivingRequestsPerQuery[queryPairs[i].index] = numberOfServers - countServersReceivingRequest
    }

    return numberOfServersNotReceivingRequestsPerQuery
}
