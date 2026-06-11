
using System;

public class Solution
{
    private record QueryPair(int Index, int Time) { }

    public int[] CountServers(int numberOfServers, int[][] logs, int timeRange, int[] queries)
    {
        QueryPair[] queryPairs = new QueryPair[queries.Length];
        for (int i = 0; i < queries.Length; ++i)
        {
            queryPairs[i] = new QueryPair(i, queries[i]);
        }

        Array.Sort(queryPairs, (first, second) => first.Time - second.Time);
        Array.Sort(logs, (first, second) => first[1] - second[1]);

        return FindNumberOfServersNotReceivingRequestsPerQuery(numberOfServers, logs, timeRange, queryPairs);
    }

    private int[] FindNumberOfServersNotReceivingRequestsPerQuery(int numberOfServers, int[][] logs, int timeRange, QueryPair[] queryPairs)
    {
        int front = 0;
        int back = 0;
        int countServersReceivingRequest = 0;
        int[] frequencyPerQueryForServerID = new int[numberOfServers + 1];
        int[] numberOfServersNotReceivingRequestsPerQuery = new int[queryPairs.Length];

        for (int i = 0; i < queryPairs.Length; ++i)
        {
            while (front < logs.Length && logs[front][1] <= queryPairs[i].Time)
            {
                if (frequencyPerQueryForServerID[logs[front][0]] == 0)
                {
                    ++countServersReceivingRequest;
                }
                ++frequencyPerQueryForServerID[logs[front][0]];
                ++front;
            }

            while (back < logs.Length && logs[back][1] < queryPairs[i].Time - timeRange)
            {
                if (frequencyPerQueryForServerID[logs[back][0]] == 1)
                {
                    --countServersReceivingRequest;
                }
                --frequencyPerQueryForServerID[logs[back][0]];
                ++back;
            }
            numberOfServersNotReceivingRequestsPerQuery[queryPairs[i].Index] = numberOfServers - countServersReceivingRequest;
        }

        return numberOfServersNotReceivingRequestsPerQuery;
    }
}
