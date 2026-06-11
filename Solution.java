
import java.util.Arrays;

public class Solution {

    private record QueryPair(int index, int time) {}

    public int[] countServers(int numberOfServers, int[][] logs, int timeRange, int[] queries) {
        QueryPair[] queryPairs = new QueryPair[queries.length];
        for (int i = 0; i < queries.length; ++i) {
            queryPairs[i] = new QueryPair(i, queries[i]);
        }

        Arrays.sort(queryPairs, (first, second) -> first.time - second.time);
        Arrays.sort(logs, (first, second) -> first[1] - second[1]);

        return findNumberOfServersNotReceivingRequestsPerQuery(numberOfServers, logs, timeRange, queryPairs);
    }

    private int[] findNumberOfServersNotReceivingRequestsPerQuery(int numberOfServers, int[][] logs, int timeRange, QueryPair[] queryPairs) {
        int front = 0;
        int back = 0;
        int countServersReceivingRequest = 0;
        int[] frequencyPerQueryForServerID = new int[numberOfServers + 1];
        int[] numberOfServersNotReceivingRequestsPerQuery = new int[queryPairs.length];

        for (int i = 0; i < queryPairs.length; ++i) {
            while (front < logs.length && logs[front][1] <= queryPairs[i].time) {
                if (frequencyPerQueryForServerID[logs[front][0]] == 0) {
                    ++countServersReceivingRequest;
                }
                ++frequencyPerQueryForServerID[logs[front][0]];
                ++front;
            }

            while (back < logs.length && logs[back][1] < queryPairs[i].time - timeRange) {
                if (frequencyPerQueryForServerID[logs[back][0]] == 1) {
                    --countServersReceivingRequest;
                }
                --frequencyPerQueryForServerID[logs[back][0]];
                ++back;
            }
            numberOfServersNotReceivingRequestsPerQuery[queryPairs[i].index] = numberOfServers - countServersReceivingRequest;
        }

        return numberOfServersNotReceivingRequestsPerQuery;
    }
}
