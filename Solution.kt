
class Solution {

    private data class QueryPair(val index: Int, val time: Int)

    fun countServers(numberOfServers: Int, logs: Array<IntArray>, timeRange: Int, queries: IntArray): IntArray {
        val queryPairs = arrayOfNulls<QueryPair>(queries.size)
        for (i in queries.indices) {
            queryPairs[i] = QueryPair(i, queries[i])
        }

        queryPairs.sortWith() { first, second -> first!!.time - second!!.time }
        logs.sortWith() { first, second -> first[1] - second[1] }

        return findNumberOfServersNotReceivingRequestsPerQuery(numberOfServers, logs, timeRange, queryPairs)
    }

    private fun findNumberOfServersNotReceivingRequestsPerQuery(numberOfServers: Int, logs: Array<IntArray>, timeRange: Int, queryPairs: Array<QueryPair?>): IntArray {
        var front = 0
        var back = 0
        var countServersReceivingRequest = 0
        val frequencyPerQueryForServerID = IntArray(numberOfServers + 1)
        val numberOfServersNotReceivingRequestsPerQuery = IntArray(queryPairs.size)

        for (i in queryPairs.indices) {
            while (front < logs.size && logs[front][1] <= queryPairs[i]!!.time) {
                if (frequencyPerQueryForServerID[logs[front][0]] == 0) {
                    ++countServersReceivingRequest
                }
                ++frequencyPerQueryForServerID[logs[front][0]]
                ++front
            }

            while (back < logs.size && logs[back][1] < queryPairs[i]!!.time - timeRange) {
                if (frequencyPerQueryForServerID[logs[back][0]] == 1) {
                    --countServersReceivingRequest
                }
                --frequencyPerQueryForServerID[logs[back][0]]
                ++back
            }
            numberOfServersNotReceivingRequestsPerQuery[queryPairs[i]!!.index] =
                numberOfServers - countServersReceivingRequest
        }

        return numberOfServersNotReceivingRequestsPerQuery
    }
}
